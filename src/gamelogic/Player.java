package gamelogic;

import tiles.Tile;

/**
 * Holds the information about the player,
 *
 * @author Noah Morton
 *
 * Date created: May 9, 2017
 *
 * Part of project: SneakGame
 */
public class Player {

    public static final int UP = 0, LEFT = 1, DOWN = 2, RIGHT = 3;
    private int x, y;
    private Tile currentTile;
    private int orientation = LEFT;
    private SneakGame game;

    public Player(int x, int y, SneakGame game) {
        this.x = x;
        this.y = y;
        this.game = game;
        currentTile = game.convertCoords(x, y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

}
