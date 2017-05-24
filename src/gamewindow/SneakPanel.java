package gamewindow;

import entities.Enemy;
import entities.Player;
import gamelogic.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import libraries.ImageTools;
import libraries.Logger;

/**
 * The panel of the game.
 *
 * @author Noah Morton
 *
 * Date created: May 3, 2017
 *
 * Part of project: ScrollingGame
 */
public class SneakPanel extends JPanel implements MouseListener, KeyListener, Runnable {

    private BufferedImage buffer;
    //tiles
    final BufferedImage[] grass;
    final BufferedImage[] ice;
    final BufferedImage[] mud;
    final BufferedImage[] sand;
    final BufferedImage[] stone;
    final BufferedImage[] water;
    final BufferedImage wood, voidTile, stoneBricks;

    //entities
    final BufferedImage playerRight, playerUp, playerDown, playerLeft;
    final BufferedImage gooblin, trool, weesp;

    private static int shift = 0;
    AudioClip win, kill, death;

    private SneakGame game;

    public SneakPanel() {
        setSize(Tuning.SCREEN_WIDTH, Tuning.SCREEN_HEIGHT);
        buffer = new BufferedImage(Tuning.SCREEN_WIDTH, Tuning.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        //load all images
        grass = new BufferedImage[4];
        grass[0] = ImageTools.load("resources/grass.png"); //todo use arrays instead, base index off of rotationType
        grass[1] = ImageTools.rotate(grass[0], 90);
        grass[2] = ImageTools.rotate(grass[0], 180);
        grass[3] = ImageTools.rotate(grass[0], -90);

        ice = new BufferedImage[4];
        ice[0] = ImageTools.load("resources/ice.png");
        ice[1] = ImageTools.rotate(ice[0], 90);
        ice[2] = ImageTools.rotate(ice[0], 180);
        ice[3] = ImageTools.rotate(ice[0], -90);

        mud = new BufferedImage[4];
        mud[0] = ImageTools.load("resources/mud.png");
        mud[1] = ImageTools.rotate(mud[0], 90);
        mud[2] = ImageTools.rotate(mud[0], 180);
        mud[3] = ImageTools.rotate(mud[0], -90);

        sand = new BufferedImage[4];
        sand[0] = ImageTools.load("resources/sand.png");
        sand[1] = ImageTools.rotate(sand[0], 90);
        sand[2] = ImageTools.rotate(sand[0], 180);
        sand[3] = ImageTools.rotate(sand[0], -90);

        stone = new BufferedImage[4];
        stone[0] = ImageTools.load("resources/stone.png");
        stone[1] = ImageTools.rotate(stone[0], 90);
        stone[2] = ImageTools.rotate(stone[0], 180);
        stone[3] = ImageTools.rotate(stone[0], -90);

        water = new BufferedImage[4];
        water[0] = ImageTools.load("resources/water.png");
        water[1] = ImageTools.rotate(water[0], 90);
        water[2] = ImageTools.rotate(water[0], 180);
        water[3] = ImageTools.rotate(water[0], -90);

        stoneBricks = ImageTools.load("resources/stone-bricks.png");
        wood = ImageTools.load("resources/wood.png");
        voidTile = ImageTools.load("resources/void-tile.png");

        //player
        playerRight = ImageTools.load("resources/player.png");
        playerDown = ImageTools.rotate(playerRight, 90);
        playerUp = ImageTools.rotate(playerRight, -90);
        playerLeft = ImageTools.rotate(playerRight, 180);

        //enemies
        gooblin = ImageTools.load("resources/gooblin.png");
        trool = ImageTools.load("resources/trool.png");
        weesp = ImageTools.load("resources/weesp.png");

        //Init sounds
        try {
            URL url1 = new URL("File:" + "resources/sounds/win.wav");
            win = Applet.newAudioClip(url1);

            URL url2 = new URL("File:" + "resources/sounds/kill.wav");
            kill = Applet.newAudioClip(url2);

            URL url3 = new URL("File:" + "resources/sounds/death.wav");
            death = Applet.newAudioClip(url3);

            if (win == null || kill == null || death == null) {
                System.out.println("Error in loading sounds.");
                Logger.logErrorMessage("Unable to load sounds, exiting.");
                Logger.errorWindow("Sounds", "Cannot load sounds, exiting.");
                System.exit(-1);
            }
        } catch (MalformedURLException mE) {
            System.out.println("Error in loading sounds: " + mE.getMessage());
            Logger.logErrorMessage("Unable to load sounds, exiting.");
            Logger.errorWindow("Sounds", "Cannot load sounds, exiting.");
            mE.printStackTrace();
            System.exit(-1);
        }

        reset(); //regen the map, and all entities

        addMouseListener(this);
        addKeyListener(this);

        Logger.logCodeMessage("Initialized panel.");

        Thread t = new Thread(this);
        t.start();

    }

    public void paint(Graphics g) {
        Graphics bg = buffer.getGraphics();
        bg.setColor(Color.white);
        bg.fillRect(0, 0, getWidth(), getHeight());

        drawMap(bg);
        if (Tuning.DEBUG) {
            drawGuidelines(bg, true);
        }
        int playerTileX = game.getPlayer().getCurrentTile().getX() - shift;
        int playerTileY = game.getPlayer().getCurrentTile().getY();

        //draw player
        switch (game.getPlayer().getOrientation()) {
            case 0:
                bg.drawImage(playerUp, playerTileX * Tuning.TILE_SIZE,
                        playerTileY * Tuning.TILE_SIZE, null);
                break;
            case 1:
                bg.drawImage(playerLeft, playerTileX * Tuning.TILE_SIZE,
                        playerTileY * Tuning.TILE_SIZE, null);
                break;
            case 2:
                bg.drawImage(playerDown, playerTileX * Tuning.TILE_SIZE,
                        playerTileY * Tuning.TILE_SIZE, null);
                break;
            case 3:
                bg.drawImage(playerRight, playerTileX * Tuning.TILE_SIZE,
                        playerTileY * Tuning.TILE_SIZE, null);
                break;

        }
        drawValidMoves(bg, game.getPlayer().getCurrentTile(), Tuning.PLAYER_MOVE_COLOR);
        //Draw enemies
        drawEnemies(bg, true);

        drawHUD(bg);

        g.drawImage(buffer, 0, 0, null);
    }

    /**
     * Draws the player's hud across the bottom of the screen.
     *
     * @param g Graphics to draw to.
     */
    private void drawHUD(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, Tuning.SCREEN_HEIGHT - Tuning.RESERVE_HEIGHT,
                Tuning.SCREEN_WIDTH, Tuning.RESERVE_HEIGHT); //background
        //hud border
        g.setColor(Color.red);
        g.drawRoundRect(0, Tuning.SCREEN_HEIGHT - Tuning.RESERVE_HEIGHT + 2,
                Tuning.SCREEN_WIDTH, Tuning.RESERVE_HEIGHT - 5, 10, 20);
        //data
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));

        Player player = game.getPlayer();

        g.drawString("Kills: " + player.getKills(), 10, Tuning.SCREEN_HEIGHT
                - Tuning.RESERVE_HEIGHT + 30);
        g.drawString("Position: " + player.getX() + "," + player.getY(), 10,
                Tuning.SCREEN_HEIGHT - Tuning.RESERVE_HEIGHT + 60);
        g.drawString("Tile: ", 150,
                Tuning.SCREEN_HEIGHT - Tuning.RESERVE_HEIGHT + 30);
        drawTileAt(195, Tuning.SCREEN_HEIGHT - Tuning.RESERVE_HEIGHT + 15, player.getCurrentTile(), g);

        g.drawString("Current tile range: " + player.getCurrentTile().getMovementRange(), 230,
                Tuning.SCREEN_HEIGHT - Tuning.RESERVE_HEIGHT + 30);

        g.drawString("Distance to castle: " + ((Tuning.MAP_WIDTH - 1) - player.getX()), 200,
                Tuning.SCREEN_HEIGHT - Tuning.RESERVE_HEIGHT + 60);

    }

    /**
     * Draws 4 rectangles out from the current tile, displaying valid move positions for that tile.
     *
     * @param g Graphics to draw to.
     * @param currentTile Current tile of reference.
     */
    private void drawValidMoves(Graphics g, Tile currentTile, Color color) {
        g.setColor(color);
        //up from tile
        for (int i = 1; i <= currentTile.getMovementRange(); i++) {
            int y = currentTile.getY() - i;
            int x = currentTile.getX() - shift;

            //stop drawing if the y/x are out of board
            if (x < 0 || x > Tuning.MAP_WIDTH - 1 || y < 0 || y > Tuning.MAP_HEIGHT - 1) {
                break;
            }

            //stop drawing out if we hit an impassible tile
            if (!game.getGrid()[y][x + shift].isPassable()) {
                break;
            }

            y *= Tuning.TILE_SIZE; //adjust values for pixels
            x *= Tuning.TILE_SIZE;
            g.fillRect(x, y, Tuning.TILE_SIZE, Tuning.TILE_SIZE);
        }
        //down from tile
        for (int i = 1; i <= currentTile.getMovementRange(); i++) {
            int y = currentTile.getY() + i;
            int x = currentTile.getX() - shift;

            //stop drawing if the y/x are out of board
            if (x < 0 || x > Tuning.MAP_WIDTH - 1 || y < 0 || y > Tuning.MAP_HEIGHT - 1) {
                break;
            }

            //stop drawing out if we hit an impassible tile
            if (game.getGrid()[y][x] == null
                    || !game.getGrid()[y][x + shift].isPassable()) {
                break;
            }
            y *= Tuning.TILE_SIZE; //adjust values for pixels
            x *= Tuning.TILE_SIZE;
            g.fillRect(x, y, Tuning.TILE_SIZE, Tuning.TILE_SIZE);
        }
        //right from tile
        for (int i = 1; i <= currentTile.getMovementRange(); i++) {
            int y = currentTile.getY();
            int x = currentTile.getX() + i - shift;

            //stop drawing if the y/x are out of board
            if (x < 0 || x + shift > Tuning.MAP_WIDTH - 1 || y < 0 || y > Tuning.MAP_HEIGHT - 1) {
                break;
            }

            //stop drawing out if we hit an impassible tile
            if (game.getGrid()[y][x] == null
                    || !game.getGrid()[y][x + shift].isPassable()) {
                break;
            }
            y *= Tuning.TILE_SIZE; //adjust values for pixels
            x *= Tuning.TILE_SIZE;
            g.fillRect(x, y, Tuning.TILE_SIZE, Tuning.TILE_SIZE);
        }
        //left from tile
        for (int i = 1; i <= currentTile.getMovementRange(); i++) {
            int y = currentTile.getY();
            int x = currentTile.getX() - i - shift;

            //stop drawing if the y/x are out of board
            if (x < 0 || x > Tuning.MAP_WIDTH - 1 || y < 0 || y > Tuning.MAP_HEIGHT - 1) {
                break;
            }

            //stop drawing out if we hit an impassible tile
            if (!game.getGrid()[y][x + shift].isPassable()) {
                break;
            }
            y *= Tuning.TILE_SIZE; //adjust values for pixels
            x *= Tuning.TILE_SIZE;
            g.fillRect(x, y, Tuning.TILE_SIZE, Tuning.TILE_SIZE);
        }
    }

    /**
     * Draws the map onto the screen
     *
     * @param g Graphics to draw onto.
     */
    private void drawMap(Graphics g) {
        for (int x = 0; x < Tuning.SCREEN_WIDTH / Tuning.TILE_SIZE; x++) {
            for (int y = 0; y < (Tuning.SCREEN_HEIGHT - Tuning.RESERVE_HEIGHT) / Tuning.TILE_SIZE; y++) {
                int xInPixels = x * Tuning.TILE_SIZE;
                int yInPixels = y * Tuning.TILE_SIZE;
                drawTileAt(xInPixels, yInPixels, game.getGrid()[y][x + shift], g);
            }
        }
    }

    /**
     * Draws a specified tile at a specified set of coords.
     *
     * @param x X in pixels to draw at
     * @param y Y in pixels to draw at
     * @param t Tile to draw
     * @param g Graphics to draw onto.
     */
    private void drawTileAt(int x, int y, Tile t, Graphics g) {

        switch (t.getType()) { //draw based on type
            case Tile.GRASS:
                g.drawImage(grass[t.getRotationType()], x, y, null);
                break;
            case Tile.ICE:
                g.drawImage(ice[t.getRotationType()], x, y, null);
                break;
            case Tile.MUD:
                g.drawImage(mud[t.getRotationType()], x, y, null);
                break;
            case Tile.SAND:
                g.drawImage(sand[t.getRotationType()], x, y, null);
                break;
            case Tile.STONE:
                g.drawImage(stone[t.getRotationType()], x, y, null);
                break;
            case Tile.STONE_BRICKS:
                g.drawImage(stoneBricks, x, y, null);
                break;
            case Tile.WATER:
                g.drawImage(water[t.getRotationType()], x, y, null);
                break;
            case Tile.WOOD:
                g.drawImage(wood, x, y, null);
                break;
            case Tile.VOID:
                g.drawImage(voidTile, x, y, null);
                break;
            default:
                System.err.println("Cannot determine image to draw from tile type: " + t.getType());
                Logger.logErrorMessage("Cannot determine image to draw from tile type: " + t.getType());
                Logger.errorWindow("Tile drawing error", "Cannot determine image to draw from tile type: " + t.getType());
                break;
        }
    }

    /**
     * Draws guidelines based on tile size and screen size. Used for positioning tiles.
     *
     * @param g Graphics to paint the lines onto.
     */
    private void drawGuidelines(Graphics g, final boolean drawNums) {
        g.setColor(Color.WHITE);
        //vertical lines
        for (int x = 0; x < Tuning.SCREEN_WIDTH; x += Tuning.TILE_SIZE) {
            g.drawLine(x, 0, x, Tuning.SCREEN_HEIGHT);
        }

        //horizontal lines
        for (int y = 0; y < Tuning.SCREEN_WIDTH; y += Tuning.TILE_SIZE) {
            g.drawLine(0, y, Tuning.SCREEN_WIDTH, y);
        }
        if (!drawNums) {
            return;
        }
        g.setFont(new Font("Arial", Font.BOLD, 8));
        int counter = 0;
        for (int i = 10; i < Tuning.SCREEN_HEIGHT; i += Tuning.TILE_SIZE) {
            g.drawString("" + counter, 5, i);
            counter++;
        }
        counter = shift; //used to draw the numbers incrementing from left to right
        for (int i = 5; i < Tuning.SCREEN_WIDTH; i += Tuning.TILE_SIZE) {
            g.drawString("" + counter, i, 8);
            counter++;
        }
    }

    /**
     * Draws all current enemies to the screen.
     *
     * @param g Graphics to draw onto.
     * @param alsoDrawPaths True if the valid move positions for the enemies
     * should be drawn.
     */
    private void drawEnemies(Graphics g, final boolean alsoDrawPaths) {
        for (Enemy enemy : game.getEnemies()) {
            switch (enemy.getType()) {
                case Enemy.GOOBLIN:
                    g.drawImage(gooblin, (enemy.getCurrentTile().getX() - shift) * Tuning.TILE_SIZE,
                            enemy.getCurrentTile().getY() * Tuning.TILE_SIZE, null);
                    break;
                case Enemy.TROOL:
                    g.drawImage(trool, (enemy.getCurrentTile().getX() - shift) * Tuning.TILE_SIZE,
                            enemy.getCurrentTile().getY() * Tuning.TILE_SIZE, null);
                    break;
                case Enemy.WEESP:
                    g.drawImage(weesp, (enemy.getCurrentTile().getX() - shift) * Tuning.TILE_SIZE,
                            enemy.getCurrentTile().getY() * Tuning.TILE_SIZE, null);
                    break;
            }
            if (alsoDrawPaths) {
                drawValidMoves(g, enemy.getCurrentTile(), Tuning.ENEMY_MOVE_COLOR);
            }
        }
    }

    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println("Error Sleeping.");
                Logger.logErrorMessage("Error Sleeping Thread.");
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'n') {
            if (!Tuning.DEBUG) {
                if (JOptionPane.showConfirmDialog(null, "Restart the game?", "Restart the game?", JOptionPane.YES_NO_OPTION) == 0) {
                    reset();
                }
            } else {
                reset();
            }
        } else if (e.getKeyChar() == 'd' && Tuning.DEBUG) {
            safeAdjustShift(1);
            System.out.println("Current shift: " + shift);
        } else if (e.getKeyChar() == 'a' && Tuning.DEBUG) {
            safeAdjustShift(-1);
            System.out.println("Current shift: " + shift);
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (x < 0 || x > Tuning.MAP_WIDTH * Tuning.TILE_SIZE || y < 0
                || y > Tuning.MAP_HEIGHT * Tuning.TILE_SIZE) {
            return;
        }

        Tile move = game.convertCoords(x, y);
        Player player = game.getPlayer();

        if (Tuning.PLAYER_FREE_MOVE || player.isValidMove(move)) {
            int pY = player.getCurrentTile().getY();
            int pX = player.getCurrentTile().getX();
            //if the player can move, move them, fixing orientation
            if (move.getX() > pX && move.getY() == pY) {
                player.setOrientation(Player.RIGHT);
            } else if (move.getX() < pX && move.getY() == pY) {
                player.setOrientation(Player.LEFT);
            } else if (move.getY() > pY && move.getX() == pX) {
                player.setOrientation(Player.DOWN);
            } else if (move.getY() < pY && move.getX() == pX) {
                player.setOrientation(Player.UP);
            }
            player.setLastPosition(player.getCurrentTile());
            player.setX(move.getX());
            player.setY(move.getY());

            /*
             * Death is checked. If it succeeds here before enemies move, this
             * means that the player has stepped onto an enemy, which should result
             * in the enemy dying.
             */
            if (game.checkDeath()) {
                Enemy killed = null;
                for (Enemy enemy : game.getEnemies()) {
                    if (enemy.getCurrentTile() == player.getCurrentTile()) {
                        killed = enemy;
                    }
                }
                if (killed != null) { //remove the enemy the player just killed
                    game.getEnemies().remove(killed);
                    player.setKills(player.getKills() + 1);
                    kill.play();
                    game.spawnRandomEnemy(); //regen the killed enemy somewhere else
                }
            }

            //move all enemies
            game.getEnemies().forEach((enemy) -> {
                enemy.doMove(player);
            });

            //check death, if success, the player has died.
            if (game.checkDeath()) {
                death.play();
                Logger.messageWindow("You died, making it " + player.getX()
                        + " tiles and taking down " + player.getKills()
                        + " monsters before one finally got you.");
                reset();
            } else if (game.checkWin()) { //check for a game win
                win.play();
                Logger.messageWindow("You made it to the castle, ridding the world of "
                        + player.getKills() + " monsters.");
                reset();
            } else if (Tuning.AUTO_SHIFT) {
                int playerX = player.getX();
                //UPDATE SHIFT ------------
                if (playerX > 20 || shift > 0) {
                    safeAdjustShift(player.getCurrentTile().getX() - player.getLastPosition().getX());
                }
            }
        }
    }

    /**
     * Safely adjusts shift without throwing NPEs.
     *
     * @param adjustment Amount to adjust by.
     */
    private void safeAdjustShift(int adjustment) {
        if (adjustment == 0 || (shift == 0 && adjustment < 0)) {
            return;
        }
        if (shift + adjustment < Tuning.SHIFT_MAX && shift + adjustment > 0) {
            shift += adjustment;
        } else {
            if (adjustment > 0) {
                shift = Tuning.SHIFT_MAX; //maxes it out
            } else {
                shift = 0; //mins it out
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //unused
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //unused
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //unused
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //unused
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //unused
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //unused
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    public SneakGame getGame() {
        return game;
    }

    public static int getShift() {
        return shift;
    }

    /**
     * Resets the game, remaking the map, and resetting all entities.
     */
    private void reset() {
        System.out.println("Making new game.");
        Logger.logCodeMessage("Making new game.");
        shift = 0;
        game = new SneakGame();
    }

}
