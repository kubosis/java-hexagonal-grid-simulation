package cz.kubos.simulation.life.board.coordinates;

import cz.kubos.simulation.life.board.Biotope;
import cz.kubos.simulation.life.board.Board;
import cz.kubos.simulation.life.board.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is used for work with coordinates on
 * the hexagonal grid such as
 * conversions between axial, cubic and offset coordinates, because all of them
 * are good for something else
 *
 * Main coordinates used for storing tiles are offset.
 * So this class contains conversions from offset to cubic, cubic to offset, axial to offset, offset to axial
 *
 * Note that simulation uses Odd row hexagonal grid - rows with odd index are shoved to the right
 * So the standard coordinate system is odd row offset
 */
public class CoordinatesEngine {
    /**
     * convert offset coordinates to axial
     * @param row - X coordinate on offset grid
     * @param col - Y coordinate on offset grid
     * @return array representing the two axial coordinates
     */
    public static int[] offsetToAxial(int row, int col) {
        int q = col - (row - (row&1)) / 2; // bitwise &1 is faster than % 2
        return new int[]{row, q};
    }

    /**
     * @param r - X coordinate on axial grid
     * @param q - Y coordinate on axial grid
     * @return array representing the offset coordinates
     */
    public static int[] axialToOffset(int r, int q) {
        int col = q + (r - (r&1)) / 2;
        return new int[]{r, col};
    }

    /**
     * convert offset coordinates to axial
     * @param row - X coordinate on offset grid
     * @param col - Y coordinate on offset grid
     * @return representation of cubic coordinates
     */
    public static Cube offsetToCube(int row, int col) {
        int q = col - (row - (row&1)) / 2;
        return new Cube(q, row, -q - row);
    }

    /**
     * @return array representing the offset coordinates
     */
    public static int[] cubeToOffset(Cube cube) {
        int col = cube.q + (cube.r - (cube.r&1)) / 2;
        return new int[]{cube.r, col};
    }

    /**
     * @return array list of neighbours of tile with coordinates of row, col
     */
    public static ArrayList<Tile> getNeighbours(int row, int col) {
        ArrayList<Tile> neighbours = new ArrayList<>();
        Board board = Board.getInstance();

        Cube cube = offsetToCube(row, col);
        for (Cube direction : Cube.directions) {
            int[] coords = cubeToOffset(cube.add(direction));
            int x = coords[0];
            int y = coords[1];

            if (board.isInBoard(x, y)) {
                neighbours.add(board.tiles[x][y]);
            }
        }

        return neighbours;
    }

    /**
     * Compute distance between two tiles
     */
    public static int tilesDistance(Tile t1, Tile t2)
    {
        Cube cube1 = offsetToCube(t1.getRow(), t1.getCol());
        Cube cube2 = offsetToCube(t2.getRow(), t2.getCol());
        return cube1.distance(cube2);
    }

    /**
     * returns all visible tiles from tile start in distance of sight
     * note: entities cannot see through mountains
     */
    public static Set<Tile> visibleTiles(Tile start, int sight)
    {
        Board board = Board.getInstance();
        HashSet<Tile> visible = new HashSet<>();
        visible.add(start);

        Cube cubeStart = offsetToCube(start.getRow(), start.getCol());

        for (int q =  -sight; q <= sight; ++q) {
            for (int r = Math.max(-sight, -q - sight); r <= Math.min(+sight, -q + sight); ++r) {
                int s = -q - r;
                Cube vector = new Cube(q, r, s);
                int[] coords = cubeToOffset( cubeStart.add(vector) );
                int x = coords[0];
                int y = coords[1];

                if (board.isInBoard(x,y)) {
                    visible.add(board.tiles[x][y]);
                }
            }

        }

        return visible;
    }

    /**
     * @param array that will be tested
     * @param food tile that we want to get tp
     * @return the best element of array (nearest from food)
     */
    public static Tile minDistanceFromFood(ArrayList<Tile> array, Tile food)
    {
        int minDistance = 100;
        int bestIndex = 0;
        for (int i = 0; i < array.size(); ++i) {
            Tile move = array.get(i);
            int distance = CoordinatesEngine.tilesDistance(move, food);
            if ((distance < minDistance) && move.getEntity() == null) {
                bestIndex = i;
                minDistance = distance;
            }
        }
        return array.size() > 0 ? array.get(bestIndex) : null;
    }

}
