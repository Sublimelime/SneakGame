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

}
