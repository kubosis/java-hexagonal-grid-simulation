package cz.kubos.simulation.life;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.kubos.simulation.life.board.Board;
import cz.kubos.simulation.life.entity.Entity;
import cz.kubos.simulation.life.gui.Launcher;
import cz.kubos.simulation.life.gui.SharedData;
import cz.kubos.simulation.life.gui.Window;
import cz.kubos.simulation.life.music.MusicPlayer;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class Model is the main class of the simulation
 * It computes every move and then asks the gui to draw it
 *
 * private parameters:
 * entities - array list of all entities on board.
 * board - the simulation world.
 * entity counter - counter of entities.
 */
public class Model {
    /* Model params are not constant -> can be changed via GUI */
    private ArrayList<Entity> entities;
    private Board board;
    private static volatile int entityCounter = 0;
    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(Model.class);

    public static void main (String[] args) {
        logger.info("Application has started");

        Launcher l = new Launcher();

        MusicPlayer mp = new MusicPlayer("music/launcherMusic.wav");

        SharedData.lock.lock();
        try {
            SharedData.cond.await();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } finally {
            SharedData.lock.unlock();
        }

        l.dispose();

        if (!SharedData.isEnd()) {
            mp.changeMusic("music/music.wav");
            Model simulation = null;
            if (SharedData.loadSimulation == null) {
                simulation = new Model();
            } else {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    simulation = objectMapper.readValue(SharedData.loadSimulation,
                            Model.class);
                } catch (IOException e) {
                    logger.error("Could not load the simulation because " + e.getMessage());
                }
            }

            if (simulation != null) {
                simulation.startSimulation();
            }
        }
        mp.stop();

        logger.info("Application has ended");
    }

    private Model()
    {
        initBoard();
        initEntities();
    }

    private void initBoard()
    {
        int rows = SharedData.rows;
        int cols = SharedData.cols;
        int param = Math.max(rows * cols / 30, 1);
        board = Board.getInstance(rows, cols, param);
    }

    private void initEntities()
    {
        entities = new ArrayList<>();
        startEntity(SharedData.getMaxEntities());
    }

    private void startEntity(int count)
    {
        for (int i = 0; i < count; ++i) {
            incrementCounter();
            Entity e = Entity.randomEntity();

            logger.debug(e.getSpecie().name + " was born on " + e.getX() + " ," + e.getY());

            entities.add(e);
        }
    }

    private void save()
    {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        Date date = new Date();

        String savedFileName = "Simulation_" + formatter.format(date);

        try {
            objectMapper.writeValue(new File("saves/" + savedFileName), this);
            System.out.println(savedFileName + " Saved successfully");
        } catch (IOException e) {
            logger.error("Could not save the game because " + e.getMessage());
        }

        SharedData.save = false;
    }

    /**
     * Starts the simulation until it is stopped by gui button exit
     */
    /* */
    public void startSimulation()
    {
        logger.info("Simulation has started");
        logger.debug("Board = \n" + board.toString());

        Window mainFrame = Window.getInstance(true);
        int SLEEP_TIME_MS = 1500;
        int INIT_SLEEP_MS = 2000;
        try {
            Thread.sleep(INIT_SLEEP_MS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int INIT_FOOD_COUNT = 8;
        board.generateFood(INIT_FOOD_COUNT);

        boolean end = false;
        int age;
        while (!end) {

            try {
                Thread.sleep(SLEEP_TIME_MS / SharedData.getSimulationSpeed());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (SharedData.isRunning()) {

                SharedData.incrementAge();
                age = SharedData.getSimulationAge();
                for (Entity e : entities) {
                    if (e.isAlive()) {
                        e.move();
                    }
                }

                ArrayList<Entity> deadEntities = new ArrayList<>();
                for (Entity e : entities) {
                    if (!e.isAlive()) {
                        deadEntities.add(e);
                        decrementCounter();
                    }
                }
                entities.removeAll(deadEntities);

                Entity newEntity;
                while ((newEntity = SharedData.popEntities()) != null) {
                    incrementCounter();
                    entities.add(newEntity);
                }

                if (age % SharedData.getFoodGenerationRate() == 0) {
                    board.generateFood(1);
                }

                if (entityCounter < SharedData.getMaxEntities()) {
                    startEntity(SharedData.getSpawnRate());
                }

                if (age % 10 == 0) {
                    // log every 10th turn of simulation
                    logger.debug("Simulation turn {} with number of entities {}", age, entityCounter);
                }
            }

            if (SharedData.save) {
                save();
            }

            end = SharedData.isEnd();
        }
        mainFrame.removeAll();
        Window.timer.stop();
        mainFrame.dispose();

        logger.info("Simulation has ended");
    }

    public static synchronized int getEntityCounter() {
        return entityCounter;
    }

    private synchronized void incrementCounter()
    {
        entityCounter++;
    }

    private synchronized void decrementCounter()
    {
        entityCounter--;
    }

    /* for the object serializer */
    public Board getBoard() {
        return board;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public static void setEntityCounter(int entityCounter) {
        Model.entityCounter = entityCounter;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

}
