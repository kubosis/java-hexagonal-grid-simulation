package cz.kubos.simulation.life.board.coordinates;

/**
 * The representation of cubical coordinates
 *
 * Has its own class because it is extremely useful for distance computation
 */
public class Cube {
    public final int q, r, s;

    public Cube(int q, int r, int s) {
        this.q = q;
        this.r = r;
        this.s = s;
    }

    /**
     * Compute distance between two cubic coordinates
     * by using the fact that cubic coordinates work like vectors
     */
    public int distance(Cube anotherCube) {
        return (Math.abs(q - anotherCube.q) + Math.abs(r - anotherCube.r) + Math.abs(s - anotherCube.s)) / 2;
    }

    /**
     * Add two cubic coordinates by using the fact that they behave like vectors
     *
     * This is useful for working with directions
     */
    public Cube add(Cube anotherCube) {
        Cube ret = null;
        if (anotherCube != null) {
            ret = new Cube(this.q + anotherCube.q, this.r + anotherCube.r, this.s + anotherCube.s);
        }
        return ret;
    }


    /**
     * The directions are vectors that represent all the possible directions from given cube coordinates
     */
    public static Cube[] directions = new Cube[]{new Cube(+1, 0, -1), new Cube(+1, -1, 0), new Cube(0, -1, +1),
                                                new Cube(-1, 0, +1), new Cube(-1, +1, 0), new Cube(0, +1, -1)};
}
