package entities;

import gamelogic.SneakGame;

/**
 * Holds the information about the player,
 *
 * @author Noah Morton
 *
 * Date created: May 9, 2017
 *
 * Part of project: SneakGame
 */
public class Player extends Entity {

    public static final int UP = 0, LEFT = 1, DOWN = 2, RIGHT = 3;
    private int orientation = RIGHT;

    public Player(int x, int y, SneakGame game) {
        super(x, y, game);
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

}
