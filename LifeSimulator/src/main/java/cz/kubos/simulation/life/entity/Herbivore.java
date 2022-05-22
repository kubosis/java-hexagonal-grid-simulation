package cz.kubos.simulation.life.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.kubos.simulation.life.board.Biotope;
import cz.kubos.simulation.life.board.Board;
import cz.kubos.simulation.life.board.Tile;
import cz.kubos.simulation.life.board.coordinates.CoordinatesEngine;
import cz.kubos.simulation.life.gui.SharedData;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

/**
 * Herbivore class represents herbivorous entities - their aim is to always eat randomly generated food
 */
public class Herbivore extends Entity {
    @JsonCreator
    public Herbivore(
            @JsonProperty("specie") Species specie,
            @JsonProperty("x") int x,
            @JsonProperty("y") int y) {
        super(specie, x, y);
    }

    @Override
    protected Tile findMove(Set<Tile> visibleTiles, ArrayList<Tile> possibleMoves) {
        Tile newTile = null;
        for (Tile tile : possibleMoves) {
            if (tile.isFood()) {
                newTile = tile;
                String action = specie.name + " has eaten food on tile " + newTile.getRow() + " x " + newTile.getCol();

                logger.debug(action);

                SharedData.lastAction.add(action);
                newTile.setFood(false);
                hunger = 0;
            }
        }

        if (newTile == null) {
            int minDistance = 100;
            Tile bestTile = null;
            Board board = Board.getInstance();

            for (Tile tile : visibleTiles) {
                if (tile.isFood()) {
                    // found food
                    int distance = CoordinatesEngine.tilesDistance(board.tiles[x][y], tile);
                    if (distance < minDistance) {
                        minDistance = distance;
                        bestTile = tile;
                    }
                }
            }

            if (bestTile != null) {
                newTile = CoordinatesEngine.minDistanceFromFood(possibleMoves, bestTile);
            }
        }

        int size = possibleMoves.size();
        boolean isPossibleToMove = false;
        for (Tile move : possibleMoves) {
            if (move.getEntity() == null) {
                isPossibleToMove = true;
                break;
            }
        }

        if (newTile == null && size != 0 && isPossibleToMove) {
            Random rn = new Random();
            while (true) {
                newTile = possibleMoves.get(Math.abs(rn.nextInt()) % size);
                if (((newTile.getBiotope() == Biotope.WATER) == specie.inWater) && (newTile.getEntity() == null)) {
                    break;
                }
            }
        }

        return newTile;
    }
}
