package gamelogic;

import java.awt.Color;

/**
 * A simple class that holds public static variables to use to tune program values
 *
 * @author Noah Morton
 *
 * Date created: May 3, 2017
 *
 * Part of project: ScrollingGame
 */
public class Tuning {

    //Size of the screen
    public final static int SCREEN_WIDTH = 1920; //640 for normal, 1920 to show whole map
    public final static int SCREEN_HEIGHT = 640;

    //map size
    public final static int MAP_WIDTH = 120;
    public final static int MAP_HEIGHT = 40;

    //the size of the square tiles in pixels
    public final static int TILE_SIZE = 16;

    //set to true if debugging
    public final static boolean DEBUG = false, SHOULD_PRINT_ERRORS = false;

    //colors
    public static final Color PLAYER_MOVE_COLOR = new Color(255, 255, 255, 100);
    public static final Color ENEMY_MOVE_COLOR = new Color(0, 0, 0, 80);

    //enemy count
    public static final int ENEMY_COUNT = 10;

    //if the player is not bound by valid moves
    public static final boolean PLAYER_FREE_MOVE = false;
}
