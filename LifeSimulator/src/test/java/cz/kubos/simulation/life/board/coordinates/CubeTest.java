package cz.kubos.simulation.life.board.coordinates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class CubeTest {
    Cube cube1;
    Cube cube2;
    @BeforeEach
    void initTestCubes() {
        cube1 = new Cube(0,0,0);
        cube2 = new Cube(5,5,5);
    }

    @Test
    void distance() {
        assertEquals(7, cube1.distance(cube2));
    }

    @Test
    void add() {
        Cube expected = new Cube(5, 5, 5);
        Cube actual = cube1.add(cube2);
        assertAll("Cube addition",
                () ->  assertEquals(expected.q, actual.q),
                () ->  assertEquals(expected.r, actual.r),
                () ->  assertEquals(expected.s, actual.s)
        );
    }
}