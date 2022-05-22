package cz.kubos.simulation.life.board.coordinates;

import cz.kubos.simulation.life.board.Board;
import cz.kubos.simulation.life.board.Tile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatesEngineTest {

    @Test
    void offsetToAxial() {
        assertArrayEquals(new int[]{2, 3},
                CoordinatesEngine.offsetToAxial(2, 4));
        assertArrayEquals(new int[]{0, 0},
                CoordinatesEngine.offsetToAxial(0, 0));
    }

    @Test
    void axialToOffset() {
        assertArrayEquals(new int[]{3, 7},
                CoordinatesEngine.axialToOffset(3, 6));

        assertArrayEquals(new int[]{5, 8},
                CoordinatesEngine.axialToOffset(5, 6));
    }

    @Test
    void offsetToCube() {
        Cube expected = new Cube(2,3,-5);
        Cube actual = CoordinatesEngine.offsetToCube(3,3);

        assertEquals(expected.r, actual.r);
        assertEquals(expected.q, actual.q);
        assertEquals(expected.s, actual.s);
    }

    @Test
    void cubeToOffset() {
        Cube cube = new Cube(2,3,-5);

        assertArrayEquals(new int[]{3, 3}, CoordinatesEngine.cubeToOffset(cube));
    }

    @Test
    void getNeighbours() {
        Board board = Board.getInstance(10,10,1);
        HashSet<Tile> actual = new HashSet<>(CoordinatesEngine.getNeighbours(2, 2));
        HashSet<Tile> expected = new HashSet<>(List.of(board.tiles[1][1], board.tiles[1][2], board.tiles[2][1],
                board.tiles[2][3], board.tiles[3][1], board.tiles[3][2]));

        assertEquals(actual, expected);
    }

    @Test
    void tilesDistance() {
        Board board = Board.getInstance(10, 10, 1);
        assertEquals(5, CoordinatesEngine.tilesDistance(board.tiles[0][0], board.tiles[3][3]));
        assertEquals(9, CoordinatesEngine.tilesDistance(board.tiles[1][0], board.tiles[1][9]));
    }

    @Test
    void visibleTiles() {
        Board board = Board.getInstance(10, 10, 1);

        HashSet<Tile> actual = new HashSet<>(CoordinatesEngine.visibleTiles(board.tiles[2][2], 2));
        HashSet<Tile> expected = new HashSet<>(List.of(board.tiles[0][1], board.tiles[0][2], board.tiles[0][3],
                board.tiles[1][0], board.tiles[1][1], board.tiles[1][2], board.tiles[1][3],
                board.tiles[2][0], board.tiles[2][1], board.tiles[2][2], board.tiles[2][3], board.tiles[2][4],
                board.tiles[3][0], board.tiles[3][1], board.tiles[3][2], board.tiles[3][3],
                board.tiles[4][1], board.tiles[4][2], board.tiles[4][3]));

        assertEquals(expected, actual);
    }

    @Test
    void minDistanceFromFood() {
        Board board = Board.getInstance(10,10,1);
        ArrayList<Tile> array = CoordinatesEngine.getNeighbours(0,0);

        assertEquals(board.tiles[0][1], CoordinatesEngine.minDistanceFromFood(array, board.tiles[0][2]));
    }
}