package gamelogic;

import libraries.Logger;
import tiles.Tile;

/**
 * Game class that holds the game.
 *
 * @author Noah Morton
 *
 * Date created: May 5, 2017
 *
 * Part of project: ScrollingGame
 */
public class SneakGame {

    private Tile[][] grid;

    public SneakGame() {
        grid = new Tile[Tuning.MAP_HEIGHT][Tuning.MAP_WIDTH];

        //init all tiles
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                grid[x][y] = new Tile(y * Tuning.TILE_SIZE, x * Tuning.TILE_SIZE, Tile.VOID);
            }
        }
        Logger.logCodeMessage("Init all tiles.");
        //todo extensive code to generate pretty map.

        Logger.logCodeMessage("Made map.");
    }

    public Tile[][] getGrid() {
        return grid;
    }

    /**
     * Takes int coords in pixels, and resolves it to a tile on the board.
     *
     * @param x X in pixels
     * @param y Y in pixels
     * @return The tile that corresponds to those coords.
     */
    public Tile convertCoords(int x, int y) {
        return grid[y / Tuning.TILE_SIZE][x / Tuning.TILE_SIZE]; //FIXME when we add scrolling, need to account for shift
    }

}
