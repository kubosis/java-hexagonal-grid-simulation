package cz.kubos.simulation.life.gui;

import cz.kubos.simulation.life.Model;
import cz.kubos.simulation.life.entity.Entity;
import cz.kubos.simulation.life.entity.Species;

import java.io.File;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class is used as a primitive form of communication
 * between Model and GUI
 *
 * All the parameters here are volatile because more threads can access them at a time
 * */
public class SharedData {
    public static volatile boolean running = true;
    public static volatile int simulationSpeed = 5;
    public static volatile int foodGenerationRate = 10; // number of turns to generate food
    public static volatile int spawnRate = 1;  // number of entities spawned at a time
    public static volatile int maxEntities = 12;  // maximal number of entities on board that are generated (player can add more)
    public static volatile int simulationAge = 0;
    public static volatile boolean end = false;
    public static volatile Species specie = null; // entity to be set to tile
    public static volatile LinkedList<Entity> entities = new LinkedList<>(); // set entities
    public static volatile boolean food = false; // set food with cursor
    public static final Lock lock = new ReentrantLock();
    public static final Condition cond = lock.newCondition();
    public static volatile Queue<String> lastAction = new LinkedList<>();
    public static volatile int rows = 16; // basic size of board
    public static volatile int cols = 25;
    public static volatile boolean save = false; // ask model to save the game
    public static volatile File loadSimulation = null; // load game from launcher
    public static volatile boolean deleteEntity = false;
    public static String thisFolder;

    static {
        try {
            thisFolder = new File(Model.class.getProtectionDomain().getCodeSource().getLocation()
            .toURI()).getPath();
        } catch (URISyntaxException e) {
            System.err.println(e.getMessage());
        }
    }

    ;

    public static synchronized boolean isRunning() {
        return running;
    }

    public static synchronized Species getSpecie() {
        return specie;
    }

    public static boolean isEnd() {
        return end;
    }

    public static synchronized int getSimulationAge() {
        return simulationAge;
    }

    public static synchronized int getSimulationSpeed() {
        return simulationSpeed;
    }

    public static synchronized int getFoodGenerationRate() {
        return foodGenerationRate;
    }

    public static synchronized int getSpawnRate() {
        return spawnRate;
    }

    public static synchronized int getMaxEntities() {
        return maxEntities;
    }


    public static synchronized void setFoodGenerationRate(int foodGenerationRate) {
        SharedData.foodGenerationRate = foodGenerationRate;
    }

    public static synchronized void setRunning(boolean running) {
        SharedData.running = running;
    }

    public static synchronized void setSimulationSpeed(int simulationSpeed) {
        SharedData.simulationSpeed = simulationSpeed;
    }

    public static synchronized void setMaxEntities(int maxEntities) {
        SharedData.maxEntities = maxEntities;
    }

    public static synchronized void setSpawnRate(int spawnRate) {
        SharedData.spawnRate = spawnRate;
    }

    public static synchronized void setSimulationAge(int simulationAge) {
        SharedData.simulationAge = simulationAge;
    }

    public static synchronized void incrementAge() {
        simulationAge += 1;
    }

    public static synchronized void setEnd(boolean end) {
        SharedData.end = end;
    }

    public static synchronized void setSpecie(Species specie) {
        SharedData.specie = specie;
    }

    public static synchronized void addToEntities(Entity e) {
        entities.add(e);
    }

    public static synchronized Entity popEntities()
    {
        Entity e = null;
        if (!entities.isEmpty()) {
            e = entities.get(0);
            entities.remove(0);
        }
        return e;
    }

    public static synchronized void setFood(boolean food) {
        SharedData.food = food;
    }

}
