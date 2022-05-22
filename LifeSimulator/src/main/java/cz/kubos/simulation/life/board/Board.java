/*
 * @author sukdojak
 */

package cz.kubos.simulation.life.board;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.kubos.simulation.life.gui.Launcher;
import cz.kubos.simulation.life.gui.components.HexGrid;
import cz.kubos.simulation.life.textIO.AnsiColors;
import org.slf4j.LoggerFactory;

import java.util.*;

import static cz.kubos.simulation.life.board.Biotope.*;

/**
 * Class board represents the world of the simulation - it represents hexagonal board with tiles
 *
 * Tiles are stored in the Tile[][] tiles array.
 * The size of board is stored in rows and cols.
 * The class board is a singleton class
 */
public class Board {
    private static volatile Board instance;
    public final int rows, cols;
    public final Tile[][] tiles;
    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(Board.class);

    /**
     * @return returns instance of class board
     *
     * This method has to be called after the instance is initialized with getInstance(int rows, int cols, int generationParameter)
     */
    public static Board getInstance() {
        return instance;
    }

    /**
     * @param rows = number of rows on simulation world
     * @param cols = number of columns on simulation world
     * @param generationParameter = on this parameter is based the pseudo-random generation of the world
     * @return returns instance of class board
     */
    public static Board getInstance(int rows, int cols, int generationParameter) {
        if (instance == null) {
            synchronized (Board.class) {
                if (instance == null) {
                    instance = new Board(rows, cols, generationParameter);

                    logger.info("Board initialized");
                }
            }
        }
        return instance;
    }

    private void randomGeneration(int startingTilesCount, Biotope biotope)
    {
        Random rn = new Random();

        for (int i = 0; i < startingTilesCount; ++i) {
            int x,y;

            x = rn.nextInt(rows);
            y = rn.nextInt(cols);
            if (tiles[x][y] != null) {
                continue;
            }


            tiles[x][y] = new Tile(biotope, x, y);
            int[][] directions; // representing potential neighbours on hexagonal grid
            if (x % 2 != 0) {
                directions = new int[][]{{0,-1},{-1,0},{-1,1},{0,1},{1,1},{1,0}};
            } else {
                directions = new int[][]{{0,-1},{-1,-1},{-1,0},{0,1},{1,0},{1,-1}};
            }
            for (int j = 0; j < rows * cols / rn.nextInt(1, startingTilesCount + 1); ++j) {
                for (int[] direction : directions) {
                    int newX = (x + direction[0]) % rows;
                    int newY = (y + direction[1]) % cols;
                    newX = Math.max(newX, 0);
                    newY = Math.max(newY, 0);

                    if (tiles[newX][newY] == null) {
                        tiles[newX][newY] = new Tile(biotope, newX, newY);
                    }
                }

                y = (y + rn.nextInt(-1,2)) % cols;
                x = (x + rn.nextInt(-1,2)) % rows;

            }
        }

    }

    private void fillRest()
    {
        Biotope last = MEADOW;
        for (int r = 0; r < rows; ++r) {
            Biotope tmp = last;
            for (int c = 0; c < cols; ++c) {
                if (tiles[r][c] == null) {
                    tiles[r][c] = new Tile(last, r, c);
                } else {
                    last = tiles[r][c].getBiotope();
                }
            }
            last = tmp;
        }
    }

    private Board(int rows, int cols, int generationParameter) {
        this.rows = rows;
        this.cols = cols;

        tiles = new Tile[rows][cols];

        // pseudo-randomly generate the board

        int startingTilesCount = rows * cols / generationParameter;

        randomGeneration(startingTilesCount / 5, MEADOW); // most probable
        randomGeneration(startingTilesCount / 4, WATER);
        randomGeneration(startingTilesCount / 3, FOREST);
        randomGeneration(startingTilesCount / 2, DESERT);
        randomGeneration(startingTilesCount, MOUNTAIN); // least probable

        fillRest();
    }

    /**
     * @return boolean: is tile with coordinates x, y in board?
     */
    public boolean isInBoard(int x, int y)
    {
        return x >= 0 && y >= 0 && x < rows && y < cols;
    }

    @Override
    public String toString() { // for debugging and TUI
        StringBuilder ret = new StringBuilder();
        for (int r = 0; r < rows; ++r) {
            if (r % 2 == 1) {
                ret.append(" ");
            }
            for (int c = 0; c < cols; ++c) {
                switch (tiles[r][c].getBiotope()) {
                    case DESERT -> ret.append(AnsiColors.YELLOW + "D" + AnsiColors.ANSI_RESET);
                    case FOREST -> ret.append(AnsiColors.BLACK + "F" + AnsiColors.ANSI_RESET);
                    case MEADOW -> ret.append(AnsiColors.GREEN + "G" + AnsiColors.ANSI_RESET);
                    case MOUNTAIN -> ret.append(AnsiColors.RED + "M" + AnsiColors.ANSI_RESET);
                    case WATER -> ret.append(AnsiColors.BLUE + "W" + AnsiColors.ANSI_RESET);
                    default -> { /* nothing to do here */ }
                }
                ret.append(" ");
            }
            ret.append("\n");
        }
        return "Rows: " + rows + ", Cols: " + cols + "\n" + ret;
    }

    /**
     * @param count count of food to be randomly generated across the board
     */
    public void generateFood(int count)
    {
        Random rn = new Random();
        HexGrid hg = HexGrid.getInstance();
        for (int i = 0; i < count; ++i) {
            int row = rn.nextInt(0, rows);
            int col = rn.nextInt(0,cols);
            synchronized (Tile.class) {
                tiles[row][col].setFood(true);
            }

            hg.redrawTile(tiles[row][col]);
        }
    }


    /**
     * Method just for object mapper
     *
     * Don't call it in code directly!
     */
    @JsonCreator
    public Board (
            @JsonProperty("rows") int rows,
            @JsonProperty("cols") int cols,
            @JsonProperty("tiles") Tile[][] tiles) {
        this.rows = rows;
        this.cols = cols;
        this.tiles = tiles;
        instance = this;
    }
}
