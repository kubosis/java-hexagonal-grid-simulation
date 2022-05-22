package cz.kubos.simulation.life.board;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.System.*;

/**
 * Biotopes represent the type of tile. Parameters of each biotope are:
 *
 *  - sight bonus = entities can see as far as (their sight + sight bonus of tile they are on)
 *  - id = identification number
 *  - image = image that will be rendered by GUI
 *  - food image path = each biotope has its own food image that is rendered in case of food on the tile
 */
public enum Biotope {
    DESERT(0, 0, "graphics/biotopes/desert.png", "graphics/food/desertFood.png"), // B
    FOREST(-2, 1, "graphics/biotopes/forest.png", "graphics/food/forestFood.png"), // F
    MEADOW(+2, 2, "graphics/biotopes/meadow.png", "graphics/food/meadowFood.png"), // G
    MOUNTAIN(+3, 3, "graphics/biotopes/mountain.png", "graphics/food/mountainFood.png"), // M
    WATER(0, 4, "graphics/biotopes/water.png", "graphics/food/waterFood.png"), // W
    ;
    public final int sightBonus;
    public final int id;
    public final BufferedImage image;
    public final String foodImPath;

    Biotope(int sightBonus, int id, String imagePath, String foodImPath) {
        BufferedImage tmp;
        this.sightBonus = sightBonus;
        this.id = id;
        this.foodImPath = foodImPath;
        try {
            tmp = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            Logger logger = (Logger) LoggerFactory.getLogger(Biotope.class);
            logger.error("Could not read image of biotope because " + e.getMessage());
            tmp = null;
        }
        this.image = tmp;
    }

    public static Biotope biotopeByID(int id) {
        return Biotope.values()[id];
    }

}
