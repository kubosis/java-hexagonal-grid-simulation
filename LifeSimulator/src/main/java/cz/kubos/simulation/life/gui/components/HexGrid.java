package cz.kubos.simulation.life.gui.components;

import ch.qos.logback.classic.Logger;
import cz.kubos.simulation.life.board.Biotope;
import cz.kubos.simulation.life.board.Board;
import cz.kubos.simulation.life.board.Tile;
import cz.kubos.simulation.life.entity.Carnivore;
import cz.kubos.simulation.life.entity.Entity;
import cz.kubos.simulation.life.entity.Herbivore;
import cz.kubos.simulation.life.entity.Species;
import cz.kubos.simulation.life.gui.SharedData;
import cz.kubos.simulation.life.gui.Window;
import org.slf4j.LoggerFactory;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * HexGrid is main gui class for simulation. It displays the board. It can paint the whole board or
 * only a single tile - this is used for entity movement
 *
 * HexGrid is a singleton class
 */
public class HexGrid extends JPanel {
    private double hexagonSize;
    private Polygon[][] polygonTiles;
    private Image[] scaledBiotopeImages;
    private static HexGrid instance = null;
    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(HexGrid.class);

    private HexGrid () {
        super();
        Board board = Board.getInstance();
        setBackground(cz.kubos.simulation.life.gui.Window.backgroundColor);

        polygonTiles = new Polygon[board.rows][board.cols];
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getButton() == MouseEvent.BUTTON1) {
                    new Thread() {
                        @Override
                        public void run() {
                            logger.debug("New mouse event thread started");
                            boolean found = false;
                            int x, y;
                            x = e.getX();
                            y = e.getY();
                            for (int r = 0; r < board.rows; ++r) {
                                for (int c = 0; c < board.cols; ++c) {
                                    if (polygonTiles[r][c].contains(x, y)) {
                                        found = true;

                                        Species specie = SharedData.getSpecie();
                                        boolean setFood = SharedData.food;
                                        boolean deleteEntity = SharedData.deleteEntity;

                                        if (specie != null) {
                                            if (specie.inWater == (board.tiles[r][c].getBiotope() == Biotope.WATER)) {
                                                Entity entity;
                                                if (specie.carnivorous) {
                                                    entity = new Carnivore(specie, r, c);
                                                } else {
                                                    entity = new Herbivore(specie, r, c);
                                                }
                                                entity.setHunger(entity.getSpecie().MAX_HUNGER / 2 + 1);
                                                SharedData.addToEntities(entity);
                                                board.tiles[r][c].setEntity(entity);
                                                HexGrid.this.redrawTile(board.tiles[r][c]);
                                            }
                                        }

                                        if (setFood) {
                                            board.tiles[r][c].setFood(true);
                                            HexGrid.this.redrawTile(board.tiles[r][c]);
                                        }

                                        if (deleteEntity) {
                                            if (board.tiles[r][c].getEntity() != null) {
                                                board.tiles[r][c].getEntity().destroy();
                                                HexGrid.this.redrawTile(board.tiles[r][c]);
                                            }
                                        }

                                        break;
                                    }
                                }
                                if (found) {
                                    break;
                                }
                            }

                            logger.debug("New mouse event thread ended");
                        }
                    }.start();
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    cz.kubos.simulation.life.gui.Window window = Window.getInstance(false);
                    window.setCursor(Cursor.getDefaultCursor());
                    SharedData.setSpecie(null);
                    SharedData.setFood(false);
                    SharedData.deleteEntity = false;
                }
            }
        });

        logger.debug("GUI HexGrid initialized");
    }

    public static HexGrid getInstance() {
        if (instance == null) {
            synchronized (HexGrid.class) {
                if (instance == null) {
                    instance = new HexGrid();
                }
            }
        }
        return instance;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Board board = Board.getInstance();

        double wSize =Math.ceil((double) getWidth() / (double) board.cols);
        double wMeasure = wSize / 1.82;
        double hSize = Math.ceil((double)getHeight() / (double)board.rows);
        double hMeasure = hSize / 1.58;

        hexagonSize = Math.min(wMeasure, hMeasure);

        scaledBiotopeImages = new Image[Biotope.values().length];
        int ceilHSize = (int)(1.4 * hSize);
        int ceilWSize = (int)(1.4 * wSize);
        for (int i = 0; i < Biotope.values().length; ++i) {
            scaledBiotopeImages[i] = Biotope.biotopeByID(i).image.getScaledInstance(ceilWSize, ceilHSize, Image.SCALE_DEFAULT);
        }

        for (Tile[] row : board.tiles) {
            for (Tile tile : row) {
                Polygon p = tile.toPolygon(hexagonSize);
                polygonTiles[tile.getRow()][tile.getCol()] = p;

                drawTile(tile, g2d);
            }
        }

        logger.info("HexGrid repaint");
    }

    /**
     * @param tile Tile that will be redrawn by gui
     */
    public void redrawTile(Tile tile)
    {
        Graphics2D g = (Graphics2D) getGraphics();
        drawTile(tile, g);
    }

    private void drawTile(Tile tile, Graphics2D g)
    {
        Polygon p = polygonTiles[tile.getRow()][tile.getCol()];
        Point centre = tile.getCentre(hexagonSize);

        g.setClip(p);
        g.drawImage(scaledBiotopeImages[tile.getBiotope().id], (int)Math.max((centre.x - hexagonSize), 0), (int)Math.max((centre.y - hexagonSize), 0), null);
        g.drawPolyline(p.xpoints, p.ypoints, p.npoints);

        if (tile.getEntity() != null) {
            int entitySize = (int)Math.ceil(hexagonSize * 1.3);
            Image scaled = tile.getEntity().getSpecie().image.getScaledInstance(entitySize, entitySize, Image.SCALE_DEFAULT);

            g.drawImage(scaled, centre.x - entitySize / 2, centre.y - entitySize / 2, null);
        } else if (tile.isFood()) {
            try {
                BufferedImage foodImage = ImageIO.read(new File(tile.getBiotope().foodImPath));

                int foodSize = (int)Math.ceil(hexagonSize);
                Image scaled = foodImage.getScaledInstance(foodSize, foodSize, Image.SCALE_DEFAULT);
                g.drawImage(scaled, centre.x - foodSize / 2, centre.y - foodSize / 2, null);
            } catch (IOException e) {
                logger.error("Could not draw tile because " + e.getMessage());
            }
        }

    }

}
