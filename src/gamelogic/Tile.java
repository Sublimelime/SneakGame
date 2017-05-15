package gamelogic;

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

    private final int x, y; //stores pixel location
    private int type, movementRange;
    private boolean passable;
    public final static int GRASS = 0, ICE = 1, MUD = 2, SAND = 3, STONE = 4, STONE_BRICKS = 5, WATER = 6, WOOD = 7, VOID = 8;
    private int grassType; //integer, 0-3

    public Tile(int x, int y, int type) {
        this.x = x;
        this.y = y;
        setType(type); //set the movement range based on tile type. Tiles that are impassible also have passable set to false.
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

    public int getMovementRange() {
        return movementRange;
    }

    public boolean isPassable() {
        return passable;
    }

    public void setType(int type) {
        this.type = type;
        switch (type) {
            case 0: //grass
                movementRange = 3;
                grassType = (int) (Math.random() * 3);
                passable = true;
                break;
            case 1: //ice
                movementRange = 4;
                passable = true;
                break;
            case 2: //mud
                movementRange = 1;
                passable = true;
                break;
            case 3: //sand
                movementRange = 2;
                passable = true;
                break;
            case 4: //stone
                movementRange = 6;
                passable = true;
                break;
            case 5: //stone bricks
                movementRange = 0;
                passable = false;
                break;
            case 6: //water
                movementRange = 0;
                passable = false;
                break;
            case 7: //wood
                movementRange = 2;
                passable = true;
                break;
            case 8: //void
                movementRange = 0;
                passable = false;
                break;
            default:
                System.err.println("Cannot determine max movement dist from tile type: " + type);
                passable = false;
                movementRange = 0;
                this.type = Tile.VOID;
                break;
        }
    }

    public int getGrassType() {
        return grassType;
    }

    @Override
    public String toString() {
        String typeLetter;
        switch (type) {
            case 0: //grass
                typeLetter = "Gra";
                break;
            case 1: //ice
                typeLetter = "Ice";
                break;
            case 2: //mud
                typeLetter = "Mud";
                break;
            case 3: //sand
                typeLetter = "San";
                break;
            case 4: //stone
                typeLetter = "Sto";
                break;
            case 5: //stone bricks
                typeLetter = "StB";
                break;
            case 6: //water
                typeLetter = "Wat";
                break;
            case 7: //wood
                typeLetter = "Woo";
                break;
            case 8: //void
                typeLetter = "Voi";
                break;
            default:
                typeLetter = "Err";
                break;
        }

        return typeLetter;
    }

}
