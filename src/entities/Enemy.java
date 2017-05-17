package entities;

import gamelogic.SneakGame;
import gamelogic.Tuning;

/**
 * A class for the basic enemy.
 *
 * @author Noah Morton
 *
 * Date created: May 11, 2017
 *
 * Part of project: SneakGame
 */
public class Enemy extends Entity {

    int type;
    public static final int GOOBLIN = 0, TROOL = 1, WEESP = 2;

    public Enemy(int x, int y, SneakGame game, int type) {
        super(x, y, game);
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void doMove(Player player) {
        int yDiff = Math.abs(player.getY() - getY());
        int xDiff = Math.abs(player.getX() - getX());

        if (yDiff > xDiff) {
            if (yDiff >= (getCurrentTile().getMovementRange()) * Tuning.TILE_SIZE) {
                if (isValidMove(getGame().convertCoords(getX(), player.getY()))) {
                    setY(player.getY());
                }
            } else {
                if (isValidMove(getGame().convertCoords(getX(), (player.getCurrentTile().getMovementRange()) * Tuning.TILE_SIZE))) {
                    setY((getCurrentTile().getMovementRange()) * Tuning.TILE_SIZE);
                }
            }
        } else if (xDiff > yDiff) {
            if (xDiff >= (getCurrentTile().getMovementRange()) * Tuning.TILE_SIZE) {
                if (isValidMove(getGame().convertCoords(player.getX(), getY()))) {
                    setX(player.getX());
                }
            } else {
                if (isValidMove(getGame().convertCoords((player.getCurrentTile().getMovementRange()) * Tuning.TILE_SIZE, getY()))) {
                    setX((getCurrentTile().getMovementRange()) * Tuning.TILE_SIZE);
                }
            }
        }
    }
}
