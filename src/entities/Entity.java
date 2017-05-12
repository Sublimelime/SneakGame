package entities;

import gamelogic.SneakGame;
import gamelogic.Tile;

/**
 * Abstract entity.
 *
 * @author Noah Morton
 *
 * Date created: May 12, 2017
 *
 * Part of project: ScrollingGame
 */
public abstract class Entity {

    private int x, y;
    private Tile currentTile;
    private SneakGame game;

    public Entity(int x, int y, SneakGame game) {
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
        currentTile = game.convertCoords(x, y);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        currentTile = game.convertCoords(x, y);
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
    }
}
