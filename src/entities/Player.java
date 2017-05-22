package entities;

import gamelogic.SneakGame;
import gamelogic.Tile;

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
    private int orientation = RIGHT, kills = 0;
    private Tile lastPosition = null;

    public Player(int x, int y, SneakGame game) {
        super(x, y, game);
        lastPosition = getCurrentTile();
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public Tile getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Tile lastPosition) {
        this.lastPosition = lastPosition;
    }

}
