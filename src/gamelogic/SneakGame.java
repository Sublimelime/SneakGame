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
    private final Player player;

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

        for (int i = 0; i < 5; i++) {
            makeSandPit(grid[(int) (Math.random() * 40)][(int) (Math.random() * 30)]);
        }

        Logger.logCodeMessage("Made map.");

        //todo place player in starting pos
        player = new Player((int) (Math.random() * 100), (int) (Math.random() * 100), this);
        Logger.logCodeMessage("Made new player at: " + player.getX() + ", " + player.getY());
    }

    /**
     * Takes a center tile, makes a sand pit around that tile.
     *
     * @param t A tile to center on.
     */
    private void makeSandPit(Tile t) {
        t.setType(Tile.SAND); //set sent tile to sand
        for (int x = t.getX() - (3 * Tuning.TILE_SIZE); x < t.getX() + (3 * Tuning.TILE_SIZE); x += Tuning.TILE_SIZE) {
            for (int y = t.getY() - (3 * Tuning.TILE_SIZE); y < t.getY() + (3 * Tuning.TILE_SIZE); y += Tuning.TILE_SIZE) {
                double rand = Math.random();
                System.out.println(rand);
                if (rand > 0.5) {
                    continue;
                }
                if (convertCoords(x, y) == null) { //outside board
                    continue;
                }
                convertCoords(x, y).setType(Tile.SAND);
            }
        }
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
        try {
            return grid[y / Tuning.TILE_SIZE][x / Tuning.TILE_SIZE]; //FIXME when we add scrolling, need to account for shift
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public Player getPlayer() {
        return player;
    }

}