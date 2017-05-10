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
            makeMudPit(grid[(int) (Math.random() * 40)][(int) (Math.random() * 30)]);
            //makeRiver(grid[0][(int) (Math.random() * 50)]);
        }

        makeRiver(grid[0][10]);

        Logger.logCodeMessage("Cleaning up remaining unset tiles to grass...");
        voidToGrass();
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
                if (Math.random() > 0.7 || convertCoords(x, y) == null) { //outside board
                    continue;
                }
                convertCoords(x, y).setType(Tile.SAND);
            }
        }
    }

    /**
     * Makes a mud pit, given an origin tile
     *
     * @param t Tile to originate from
     */
    private void makeMudPit(Tile t) {
        t.setType(Tile.MUD); //set sent tile to sand
        for (int x = t.getX() - (3 * Tuning.TILE_SIZE); x < t.getX() + (3 * Tuning.TILE_SIZE); x += Tuning.TILE_SIZE) {
            for (int y = t.getY() - (3 * Tuning.TILE_SIZE); y < t.getY() + (3 * Tuning.TILE_SIZE); y += Tuning.TILE_SIZE) {
                if (Math.random() > 0.7 || convertCoords(x, y) == null) { //outside board
                    continue;
                }
                convertCoords(x, y).setType(Tile.MUD);
            }
        }
    }

    /**
     * Creates a river down from the top row. Intersperces random bridges.
     * Note: Does not always make valid passable rivers.
     *
     * @param t Origin tile, should be in row 0.
     */
    private void makeRiver(Tile t) {
        int x = t.getX();
        int y = t.getY();
        if (y != 0) {
            System.err.println("Tried to make river in non-sensical position.");
            return;
        }

        convertCoords(x, y).setType(Tile.WATER);

        for (int i = 0; i < Tuning.MAP_HEIGHT - 1; i++) {
            y += Tuning.TILE_SIZE;
            System.out.println("Y:" + y);
            System.out.println("X" + x);
            if (Math.random() > 0.2) { //go down
                if (Math.random() > 0.1) { //make water
                    convertCoords(x, y).setType(Tile.WATER);
                } else { //make bridge
                    convertCoords(x, y).setType(Tile.WOOD);
                }
            } else { //go sideways
                if (Math.random() > 0.5) {
                    convertCoords(x, y).setType(Tile.WATER);
                    if (x + Tuning.TILE_SIZE < Tuning.MAP_WIDTH * Tuning.TILE_SIZE) {
                        x += Tuning.TILE_SIZE;
                    }
                    convertCoords(x, y).setType(Tile.WATER);
                } else {
                    convertCoords(x, y).setType(Tile.WATER);
                    if (x - Tuning.TILE_SIZE > 0) {
                        x -= Tuning.TILE_SIZE;
                    }
                    convertCoords(x, y).setType(Tile.WATER);
                }
            }
        }
    }

    /**
     * Sets the remainder of all tiles still void to grass.
     */
    private void voidToGrass() {
        for (Tile[] grid1 : grid) {
            for (Tile grid11 : grid1) {
                if (grid11.getType() == Tile.VOID) {
                    grid11.setType(Tile.GRASS);
                }
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
