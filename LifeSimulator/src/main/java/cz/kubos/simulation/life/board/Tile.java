package cz.kubos.simulation.life.board;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.kubos.simulation.life.entity.Entity;

import java.awt.*;

/**
 * Class tile holds all the information about given tile on the board
 *
 * Parameters:
 *  - biotope: element of enum biotope (for more info see java doc of biotope)
 *  - row, col = position on board in offset coordinates
 *  - food = is food on this tile?
 *  - entity = the entity on this tile
 */
public class Tile {
    private final Biotope biotope;
    private final int row, col;
    private volatile boolean food = false;
    private volatile Entity entity = null;

    @JsonCreator
    Tile(
      @JsonProperty("type") Biotope type,
      @JsonProperty("row") int row,
      @JsonProperty("col") int col) {

        this.biotope = type;
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString()
    {
        // for debugging
        String ret = "Biotope: " + biotope.name() + "; x, y = " + row + " ," + col + "; food? " + food;
        if (this.entity != null) {
            ret += "; Entity: " + entity.getSpecie().name;
        }
        return ret;
    }

    /**
     * @param size = size of hexagons on gui hex grid
     * @return returns polygon representation of this tile for gui
     */
    public Polygon toPolygon(double size)
    {
        double width = size * Math.sqrt(3);
        double height = size * 2;

        double off = size / 4;
        double offsetY = (Math.sqrt(Math.pow(size, 2) - Math.pow(width / 2, 2)) );

        double centreX = off + width / 2 + width * col ;
        double centreY = off + size + (height - offsetY) * row;

        centreX = ((row&1) == 0) ? centreX : centreX + width / 2;

        Polygon p = new Polygon();

        p.addPoint((int)Math.ceil(centreX), (int)Math.ceil(centreY - size));
        p.addPoint((int)Math.floor(centreX + width / 2), (int)Math.floor(centreY - offsetY));
        p.addPoint((int)Math.floor(centreX + width / 2), (int)Math.floor(centreY + offsetY));

        p.addPoint((int)Math.floor(centreX), (int)Math.floor(centreY + size));
        p.addPoint((int)Math.ceil(centreX - width / 2), (int)Math.ceil(centreY + offsetY));
        p.addPoint((int)Math.ceil(centreX - width / 2), (int)Math.ceil(centreY - offsetY));

        return p;
    }

    /**
     * @param size = size of hexagons on gui hex grid
     * @return the center of this tile
     */
    public Point getCentre(double size)
    {
        double width = size * Math.sqrt(3);
        double height = size * 2;

        double off = size / 4;
        double offsetY = (Math.sqrt(Math.pow(size, 2) - Math.pow(width / 2, 2)) );

        double centreX = off + width / 2 + width * col ;
        centreX = ((row&1) == 0) ? centreX : centreX + width / 2;
        double centreY = off + size + (height - offsetY) * row;

        return new Point((int)Math.floor(centreX), (int)Math.floor(centreY));
    }

    public Biotope getBiotope() {
        return biotope;
    }

    public synchronized boolean isFood() { return food; }

    public synchronized void setFood(boolean food) {
        this.food = food;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public synchronized Entity getEntity() {
        return entity;
    }

    public synchronized void setEntity(Entity entity) {
        this.entity = entity;
    }
}
