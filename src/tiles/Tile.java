package tiles;

/**
 * Abstract class tile, all tiles extend this.
 *
 * @author Noah Morton
 *
 * Date created: May 5, 2017
 *
 * Part of project: ScrollingGame
 */
public class Tile {

    private final int x, y;
    private final int type;
    private final static int GRASS = 0, ICE = 1, MUD = 2, SAND = 3, STONE = 4, STONE_BRICKS = 5, WATER = 6, WOOD = 7;

    public Tile(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getType() {
        return type;
    }

}
