package cz.kubos.simulation.life.entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.BufferUnderflowException;

/**
 * Enum species represents all kinds of species that live in the simulation
 *
 * This is what makes this application a kind of simulation framework, because it is really easy to add new entity and simulator will already know,
 * how to handle it
 *
 * parameters:
 *  - turnsForMove = number of turns after which the entity move
 *  - strength = the strength of entity - stronger entities can eat weaker ones
 *  - sight = how far does this entity see
 *  - name - string name of the entity for control prints
 *  - carnivorous - is this entity carnivorous?
 *  - MAX_AGE
 *  - MAX_HUNGER
 *  - inWater - is this entity in water?
 *  - image - image of this entity for gui rendering
 */
public enum Species {
    /* carnivorous entities */
    FOX(4,3, 5, "Fox", true, 6 * 365, 400, "graphics/entities/fox.png", false),
    WOLF(3,4, 5, "Wolf", true, 15 * 365, 550, "graphics/entities/wolf.png", false),
    BEAR(4,7, 4, "Bear", true, 25 * 365, 500, "graphics/entities/bear.png", false),



    /* herbivorous entities */
    RAT(3,0, 4, "Rat",false, 2 * 365, 100, "graphics/entities/rat.png", false),
    CHICKEN(5,2, 4, "Chicken", false, 4 * 365, 120, "graphics/entities/chicken.png", false),
    TORTOISE(10,6, 4, "Tortoise",false, 200 * 365, 400, "graphics/entities/tortoise.png", false),
    GORILLA(6,4, 7, "Gorilla",false, 50 * 365, 300, "graphics/entities/gorilla.png", false),

    /* water entities */
    FISH(3, 2, 6, "Fish", false, 10 * 365, 100,"graphics/entities/fish.png", true),
    SHARK(2, 8, 6, "Shark", true, 25 * 365, 400, "graphics/entities/shark.png", true),

    PANDA(1,10,10,"panda",true,20 * 365, 40, "graphics/entities/panda.png", false),
    ;

    public final int turnsForMove, strength, sight;
    public final String name;
    public final boolean carnivorous;
    public final int MAX_AGE, MAX_HUNGER;
    public final BufferedImage image;
    public final boolean inWater;

    Species(int turnsForMove, int strength, int sight, String name, boolean carnivorous, int max_age, int max_hunger, String imagePath, boolean inWater) {
        this.turnsForMove = turnsForMove;
        this.strength = strength;
        this.sight = sight;
        this.name = name;
        this.carnivorous = carnivorous;
        this.MAX_AGE = max_age;
        this.MAX_HUNGER = max_hunger;
        this.inWater = inWater;

        BufferedImage tmp = null;
        try {
            tmp = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Could not read image " + imagePath);
            // because this image is essential, we have to end the program here
            System.exit(1);
        } finally {
            image = tmp;
        }
    }
}
