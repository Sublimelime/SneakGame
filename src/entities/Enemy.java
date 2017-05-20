package entities;

import gamelogic.SneakGame;

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

    final int type;
    public static final int GOOBLIN = 0, TROOL = 1, WEESP = 2;

    public Enemy(int x, int y, SneakGame game, int type) {
        super(x, y, game);
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public void setX(int x) {
        //don't let movement happen onto other enemies
        for (Enemy enemy : getGame().getEnemies()) {
            if (enemy.getCurrentTile() == getGame().getGrid()[getY()][x]) {
                return;
            }
        }
        super.setX(x);
    }

    @Override
    public void setY(int y) {
        //don't let movement happen onto other enemies
        for (Enemy enemy : getGame().getEnemies()) {
            if (enemy.getCurrentTile() == getGame().getGrid()[y][getX()]) {
                return;
            }
        }
        super.setY(y);
    }

    /**
     * Makes the enemy move towards the player.
     *
     * @param player Player to move towards.
     */
    public void doMove(Player player) {
        //get the differences between x of the player, and x of the enemy, etc.
        int yDiff = (Math.abs(player.getCurrentTile().getY() - getCurrentTile().getY()));
        int xDiff = (Math.abs(player.getCurrentTile().getX() - getCurrentTile().getX()));

        //if y difference is larger, try to fix that first
        if (yDiff > xDiff) {
            //if the difference is less than the movement range of the tile we're on
            if (yDiff <= getCurrentTile().getMovementRange()) {
                if (isValidMove(getGame().getGrid()[player.getY()][getX()])) {
                    setY(player.getY());
                }
            } else { //outside of our movement range, so just go max range
                if (getY() < player.getY()) {
                    if (isValidMove(getGame().getGrid()[getCurrentTile().getMovementRange() + getY()][getX()])) {
                        setY(getCurrentTile().getMovementRange() + getY());
                    }
                } else {
                    if (isValidMove(getGame().getGrid()[getY() - getCurrentTile().getMovementRange()][getX()])) {
                        setY(getY() - getCurrentTile().getMovementRange());
                    }
                }
            }
        } else if (xDiff > yDiff) {
            //if the difference is less than the movement range of the tile we're on
            if (xDiff <= getCurrentTile().getMovementRange()) {
                if (isValidMove(getGame().getGrid()[getY()][player.getX()])) {
                    setX(player.getX());
                }
            } else { //outside of our movement range, so just go max range
                if (getX() < player.getX()) { //go right
                    if (isValidMove(getGame().getGrid()[getY()][getX() + getCurrentTile().getMovementRange()])) {
                        setX((getCurrentTile().getMovementRange()) + getX());
                    }
                } else { //go left
                    if (isValidMove(getGame().getGrid()[getY()][getX() - getCurrentTile().getMovementRange()])) {
                        setX(getX() - getCurrentTile().getMovementRange());
                    }
                }
            }
        } else { //they're the same, so we just fix Y manually
            if (isValidMove(getGame().getGrid()[player.getY()][getX()])) {
                setY(player.getY());
            }
        }
    }
}
