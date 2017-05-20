package gamelogic;

import entities.Enemy;
import entities.Player;
import gamewindow.SneakPanel;
import java.util.ArrayList;
import libraries.Logger;

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
    private ArrayList<Enemy> enemies;

    public SneakGame() {
        grid = new Tile[Tuning.MAP_HEIGHT][Tuning.MAP_WIDTH];

        //init all tiles
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                grid[x][y] = new Tile(y, x, Tile.VOID);
            }
        }
        Logger.logCodeMessage("Init all tiles.");

        //SAND/MUD/STONE PITS -------------------------
        for (int i = 0; i < 8; i++) {
            makeSandPit(grid[(int) (Math.random() * Tuning.MAP_HEIGHT)][(int) (Math.random() * Tuning.MAP_WIDTH)]);
            makeMudPit(grid[(int) (Math.random() * Tuning.MAP_HEIGHT)][(int) (Math.random() * Tuning.MAP_WIDTH)]);
            makeStonePatch(grid[(int) (Math.random() * Tuning.MAP_HEIGHT)][(int) (Math.random() * Tuning.MAP_WIDTH)]);
        }

        Logger.logOtherMessage("World Gen", "Made sand pits.");

        //RIVERS -------------------------------
        if (Math.random() > 0.2) {
            makeRiver(grid[0][Tuning.MAP_WIDTH / 4]);
        } else {
            makeIceRiver(grid[0][Tuning.MAP_WIDTH / 4]);
        }
        if (Math.random() > 0.2) {
            makeRiver(grid[0][Tuning.MAP_WIDTH / 2]);
        } else {
            makeIceRiver(grid[0][Tuning.MAP_WIDTH / 2]);
        }
        if (Math.random() > 0.2) {
            makeRiver(grid[0][3 * (Tuning.MAP_WIDTH / 4)]);
        } else {
            makeIceRiver(grid[0][3 * (Tuning.MAP_WIDTH / 4)]);
        }
        Logger.logOtherMessage("World Gen", "Made rivers.");

        //SMALL WALLS ------------------
        for (int i = 0; i < 7; i++) {
            int row = (int) (Math.random() * Tuning.MAP_HEIGHT);
            int column = (int) (Math.random() * Tuning.MAP_WIDTH);
            if (row > Tuning.MAP_HEIGHT - 2 || column > Tuning.MAP_WIDTH - 2 || row < 2 || column < 2) {
                i--;
            } else {
                makeSmallWall(grid[row][column], (Math.random() > 0.5));
            }
        }
        Logger.logOtherMessage("World Gen", "Made small walls.");

        //HOUSES------------------
        makeHouse(grid[10][20], 3);
        makeHouse(grid[25][46], 4);
        makeHouse(grid[19][80], 3);

        Logger.logOtherMessage("World Gen", "Made houses.");

        makeWinCastle(Tuning.MAP_WIDTH - 1);
        Logger.logOtherMessage("World Gen", "Made win castle.");

        //CLEANUP ------------------------
        Logger.logCodeMessage("Cleaning up remaining unset tiles to grass...");
        voidToGrass();
        Logger.logCodeMessage("Made map.");

        //make player
        int rX, rY;
        do {
            rX = (int) (Math.random() * 10);
            rY = (int) (Math.random() * (Tuning.MAP_HEIGHT - 1));
        } while (!grid[rY][rX].isPassable()); //ensures that we spawn him on a valid tile
        player = new Player(rX, rY, this);
        Logger.logCodeMessage("Made new player at: " + player.getX() + ", " + player.getY());

        //make enemies
        enemies = new ArrayList<>();

        for (int i = 0; i < Tuning.ENEMY_COUNT; i++) {
            spawnRandomEnemy();
        }
    }

    /**
     * Spawns an enemy randomly somewhere in the map.
     */
    public void spawnRandomEnemy() {
        int x, y;
        do {
            x = (int) (Math.random() * (Tuning.MAP_WIDTH));
            y = (int) (Math.random() * (Tuning.MAP_HEIGHT));
        } while (!grid[y][x].isPassable() || grid[y][x] == player.getCurrentTile()); //find a valid spot
        enemies.add(new Enemy(x, y, this, (int) (Math.random() * 3)));
    }

    /**
     * Creates a stone patch.
     *
     * @param t Center tile of the stone patch.
     */
    private void makeStonePatch(Tile t) {
        int tX = t.getX();
        int tY = t.getY();

        t.setType(Tile.STONE); //set sent tile to stone
        for (int x = tX - 2; x < tX + 2; x++) {
            for (int y = tY - 2; y < tY + 2; y++) {
                if (Math.random() > 0.3 || y < 0 || y > Tuning.MAP_HEIGHT - 1
                        || x < 0 || x > Tuning.MAP_WIDTH - 1) { //outside board
                    continue;
                }
                grid[y][x].setType(Tile.STONE);
            }
        }

    }

    /**
     * Takes a center tile, makes a sand pit around that tile.
     *
     * @param t A tile to center on.
     */
    private void makeSandPit(Tile t) {
        t.setType(Tile.SAND); //set sent tile to sand
        for (int x = t.getX() - 3; x < t.getX() + 3; x++) {
            for (int y = t.getY() - 3; y < t.getY() + 3; y++) {
                if (Math.random() > 0.7 || y < 0 || y > Tuning.MAP_HEIGHT - 1
                        || x < 0 || x > Tuning.MAP_WIDTH - 1) { //outside board
                    continue;
                }
                grid[y][x].setType(Tile.SAND);
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
        for (int x = t.getX() - 3; x < t.getX() + 3; x++) {
            for (int y = t.getY() - 3; y < t.getY() + 3; y++) {
                if (Math.random() > 0.7 || y < 0 || y > Tuning.MAP_HEIGHT - 1
                        || x < 0 || x > Tuning.MAP_WIDTH - 1) { //outside board
                    continue;
                }
                grid[y][x].setType(Tile.MUD);
            }
        }
    }

    /**
     * Creates a river down from the top row. Intersperces random bridges.
     *
     * @param t Origin tile, should be in row 0.
     */
    private void makeRiver(Tile t) {
        //todo sometimes generates impassible rivers
        int x = t.getX();
        int y = t.getY();
        if (y != 0) {
            System.err.println("Tried to make river in non-sensical position.");
            return;
        }

        grid[y][x].setType(Tile.WATER);
        int bridges = 0;
        for (int i = 0; i < Tuning.MAP_HEIGHT - 1; i++) {
            y++;
            //System.out.println("Y:" + y);
            //System.out.println("X" + x);

            //keep generating river
            if (Math.random() > 0.2) { //go down
                if (Math.random() > 0.1) { //make water
                    grid[y][x].setType(Tile.WATER);
                } else { //make bridge
                    grid[y][x].setType(Tile.WOOD);
                    bridges++;
                }
            } else { //go sideways
                if (Math.random() > 0.5) {
                    grid[y][x].setType(Tile.WATER);
                    if (x + 1 < Tuning.MAP_WIDTH) {
                        x++;
                    }
                    grid[y][x].setType(Tile.WATER);
                } else {
                    grid[y][x].setType(Tile.WATER);
                    if (x - 1 > 0) {
                        x--;
                    }
                    grid[y][x].setType(Tile.WATER);
                }
            }

            //ensure a bridge always gens
            if (i == Tuning.MAP_HEIGHT / 2 && bridges < 1) {
                grid[y][x].setType(Tile.WOOD);
                bridges++;
            }
        }
    }

    /**
     * Creates a river down from the top row, out of ice.
     *
     * @param t Origin tile, should be in row 0.
     */
    private void makeIceRiver(Tile t) {
        //todo sometimes generates impassible rivers
        int x = t.getX();
        int y = t.getY();
        if (y != 0) {
            System.err.println("Tried to make river in non-sensical position.");
            return;
        }

        grid[y][x].setType(Tile.ICE);
        for (int i = 0; i < Tuning.MAP_HEIGHT - 1; i++) {
            y++;
            //System.out.println("Y:" + y);
            //System.out.println("X" + x);

            //keep generating river
            if (Math.random() > 0.2) { //go down
                grid[y][x].setType(Tile.ICE);
            } else { //go sideways
                if (Math.random() > 0.5) {
                    grid[y][x].setType(Tile.ICE);
                    if (x + 1 < Tuning.MAP_WIDTH) {
                        x++;
                    }
                    grid[y][x].setType(Tile.ICE);
                } else {
                    grid[y][x].setType(Tile.ICE);
                    if (x - 1 > 0) {
                        x--;
                    }
                    grid[y][x].setType(Tile.ICE);
                }
            }

        }
    }

    /**
     * Generates a small 3 tile long wall.
     *
     * @param t Middle tile of the wall.
     * @param vertical True if the wall should be vertical.
     */
    private void makeSmallWall(Tile t, boolean vertical) {
        int tX = t.getX();
        int tY = t.getY();
        try {
            if (vertical) {
                t.setType(Tile.STONE_BRICKS);
                grid[tY + 1][tX].setType(Tile.STONE_BRICKS);
                grid[tY - 1][tX].setType(Tile.STONE_BRICKS);
            } else {
                t.setType(Tile.STONE_BRICKS);
                grid[tY][tX + 1].setType(Tile.STONE_BRICKS);
                grid[tY][tX - 1].setType(Tile.STONE_BRICKS);
            }
        } catch (NullPointerException e) {
            if (Tuning.DEBUG && Tuning.SHOULD_PRINT_ERRORS) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates the castle to win.
     *
     * @param column Column to start at
     */
    private void makeWinCastle(int column) {
        for (int i = 0; i < Tuning.MAP_HEIGHT; i++) {
            grid[i][column].setType(Tile.STONE_BRICKS);
        }
        grid[Tuning.MAP_HEIGHT / 2][column].setType(Tile.WOOD);
        grid[Tuning.MAP_HEIGHT / 3][column].setType(Tile.WOOD);
        grid[2 * (Tuning.MAP_HEIGHT / 3)][column].setType(Tile.WOOD);
    }

    /**
     * Creates a house, with the tile provided at the upper left corner.
     *
     * @param t Upper left corner of the house, will be a wall
     * @param internalSize Size to make the internals of the house. Will be ?x?.
     */
    private void makeHouse(Tile t, int internalSize) {
        int tX = t.getX();
        int tY = t.getY();

        //right from tile
        t.setType(Tile.STONE_BRICKS);
        for (int i = tX; i < (internalSize + 1) + tX; i++) {
            grid[tY][i].setType(Tile.STONE_BRICKS);
        }

        //down from tile
        for (int i = tY; i < (internalSize + 1) + tY; i++) {
            grid[i][tX].setType(Tile.STONE_BRICKS);
        }

        //bottom line
        for (int i = tX; i < (internalSize + 2) + tX; i++) {
            grid[tY + (internalSize + 1)][i].setType(Tile.STONE_BRICKS);
        }

        //right wall
        for (int i = tY; i < (internalSize + 1) + tY; i++) {
            grid[i][tX + (internalSize + 1)].setType(Tile.STONE_BRICKS);
        }
        //floor
        for (int x = tX + 1; x < tX + (internalSize + 1); x++) {
            for (int y = tY + 1; y < tY + (internalSize + 1); y++) {
                grid[y][x].setType(Tile.WOOD);
            }
        }

        //create a door
        switch ((int) (Math.random() * 3)) {
            case 0: //top
                grid[tY][tX + (internalSize / 2)].setType(Tile.WOOD);
                break;
            case 1: //right
                grid[tY + (internalSize / 2)][tX + (internalSize + 1)].setType(Tile.WOOD);
                break;
            case 2: //down
                grid[tY + (internalSize + 1)][tX + (internalSize / 2)].setType(Tile.WOOD);
                break;
            case 3: //left
                grid[tY + (internalSize / 2)][tX].setType(Tile.WOOD);
                break;
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
            return grid[y / Tuning.TILE_SIZE][(x / Tuning.TILE_SIZE) + SneakPanel.getShift()];
        } catch (ArrayIndexOutOfBoundsException e) {
            if (Tuning.DEBUG && Tuning.SHOULD_PRINT_ERRORS) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void printGrid() {
        for (Tile[] x : grid) {
            for (Tile y : x) {
                System.out.print(y);
            }
            System.out.println();
        }
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Returns if an enemy is on top of a player, or vice versa.
     *
     * @return True if collision between enemies and player.
     */
    public boolean checkDeath() {
        return enemies.stream().anyMatch((enemy) -> (enemy.getCurrentTile() == player.getCurrentTile()));
    }

    public boolean checkWin() {
        return getPlayer().getX() >= Tuning.MAP_WIDTH - 1;
    }

}
