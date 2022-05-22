package cz.kubos.simulation.life.entity;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.kubos.simulation.life.board.Biotope;
import cz.kubos.simulation.life.board.Board;
import cz.kubos.simulation.life.board.Tile;
import cz.kubos.simulation.life.board.coordinates.CoordinatesEngine;
import cz.kubos.simulation.life.gui.components.HexGrid;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Class entity represents all entities that live in the simulation
 *
 * parameters of entities are:
 *  - specie = enum with more parameters of entity (for more info see java doc of species)
 *  - hunger = int, every move it is incremented. When hunger >= MAX_HUNGER, the entity dies
 *  - age = int, incremented every move, when age >= MAX_AGE, the entity dies
 *  - x,y = position of entity on board
 *  - alive = is this entity alive?
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Carnivore.class, name = "carnivore"),
        @JsonSubTypes.Type(value = Herbivore.class, name = "herbivore")
})
public abstract class Entity {
    protected Species specie;
    protected int hunger = 0;
    protected int x, y;
    protected int age = 0;
    protected boolean alive = true;

    protected static final Logger logger
            = (Logger) LoggerFactory.getLogger(Entity.class);

    public Entity(Species specie, int x, int y) {
        this.specie = specie;
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public Species getSpecie() {
        return specie;
    }

    protected abstract Tile findMove(Set<Tile> visibleTiles, ArrayList<Tile> possibleMoves);

    /**
     * evaluates aging and starving of this entity
     * if hunger > MAX HUNGER of specie -> entity dies
     * if age >= MAX AGE of specie -> entity dies
     */
    public boolean evalTurn()
    {
        boolean died = false;
        if ((hunger++) > specie.MAX_HUNGER || (age++) >= specie.MAX_AGE) {
            destroy();
            died = true;
        }
        return died;
    }

    /**
     * The aim of carnivores is to eat other entities
     * so moves that make them eat another entity are preferable
     *
     * The aim of herbivores is to eat generated food
     * so moves that make them eat generated food are preferable
     *
     * If no such entity or food is in the range, move is random
     */
    public void move()
    {
        boolean died = evalTurn();
        if (!died && (age % specie.turnsForMove == 0)) {
            Board board = Board.getInstance();

            Tile oldTile = board.tiles[x][y];
            ArrayList<Tile> neighbours = CoordinatesEngine.getNeighbours(x,y);
            ArrayList<Tile> possibleMoves = new ArrayList<>();

            for (Tile move : neighbours) {
                if (((move.getBiotope() == Biotope.WATER) && (specie.inWater)) || ((move.getBiotope() != Biotope.WATER) && (!specie.inWater))) {
                    possibleMoves.add(move);
                }
            }

            Set<Tile> visibleTiles = CoordinatesEngine.visibleTiles(oldTile, specie.sight + board.tiles[x][y].getBiotope().sightBonus);
            Set<Tile> allowedTiles = new HashSet<>();
            for (Tile t : visibleTiles) {
                if (((t.getBiotope() == Biotope.WATER) && (specie.inWater)) || ((t.getBiotope() != Biotope.WATER) && (!specie.inWater))) {
                    allowedTiles.add(t);
                }
            }

            Tile newTile = findMove(allowedTiles, possibleMoves);
            newTile = newTile == null ? oldTile : newTile;

            oldTile.setEntity(null);
            newTile.setEntity(this);

            x = newTile.getRow();
            y = newTile.getCol();

            HexGrid hexGrid = HexGrid.getInstance();
            hexGrid.redrawTile(oldTile);
            hexGrid.redrawTile(newTile);

        }
    }

    /**
     * safely destroy this entity and ask HexGrid to redraw this tile
     */
    public void destroy()
    {
        Board board = Board.getInstance();
        board.tiles[x][y].setEntity(null);

        alive = false;
        HexGrid hexGrid = HexGrid.getInstance();
        hexGrid.redrawTile(board.tiles[x][y]);

        logger.debug(specie.name + " has died on " + x + " ," + y);
    }

    public boolean isAlive() {
        return alive;
    }

    /**
     * This function returns new random entity already placed on its tile
     */
    public static Entity randomEntity()
    {
        Random rn = new Random();
        Board board = Board.getInstance();
        int NUM_OF_CARNIVORES = 3;
        int NUM_OF_HERBIVORES = 4;

        Entity entity;
        int startX, startY;
        Tile startTile;

        do {
            startX = rn.nextInt(board.rows);
            startY = rn.nextInt(board.cols);
            startTile = board.tiles[startX][startY];
        } while (startTile.getEntity() != null);

        if (startTile.getBiotope() != Biotope.WATER) {
            if (rn.nextInt(100) % 2 == 0) {
                entity = new Carnivore(Species.values()[rn.nextInt(0, NUM_OF_CARNIVORES)], startX, startY);
            } else {
                entity = new Herbivore(Species.values()[rn.nextInt(NUM_OF_CARNIVORES, NUM_OF_CARNIVORES + NUM_OF_HERBIVORES)], startX, startY);
            }
        } else {
            if (rn.nextInt(100) % 2 == 0) {
                entity = new Carnivore(Species.SHARK, startX, startY);
            } else {
                entity = new Herbivore(Species.FISH, startX, startY);
            }
        }

        startTile.setEntity(entity);

        return entity;
    }

    /* for object mapper */
    public int getAge() {
        return age;
    }

    public int getHunger() {
        return hunger;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSpecie(Species s)
    {
        this.specie = s;
    }
}
