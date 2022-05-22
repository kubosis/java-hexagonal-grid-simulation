package cz.kubos.simulation.life.textIO;

/**
 * ANSI escape codes for colored text
 */
public class AnsiColors {
    /* ansi codes for toString() */
    public static final String ANSI_RESET = "\033[0m";  // Text Reset
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\u001B[35m";
}
