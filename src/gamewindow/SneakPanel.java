package gamewindow;

import entities.Player;
import gamelogic.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
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
    BufferedImage grass, grass2, grass3, grass4, ice, mud, sand, stone, stoneBricks, water, wood, voidTile;
    //entities
    BufferedImage playerRight, playerUp, playerDown, playerLeft;

    private SneakGame game;

    public SneakPanel() {
        setSize(Tuning.SCREEN_WIDTH, Tuning.SCREEN_HEIGHT);
        buffer = new BufferedImage(Tuning.SCREEN_WIDTH, Tuning.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        //load all images
        grass = ImageTools.load("resources/grass.png");
        grass2 = ImageTools.rotate(grass, 90);
        grass3 = ImageTools.rotate(grass, 180);
        grass4 = ImageTools.rotate(grass, -90);

        ice = ImageTools.load("resources/ice.png");
        mud = ImageTools.load("resources/mud.png");
        sand = ImageTools.load("resources/sand.png");
        stone = ImageTools.load("resources/stone.png");
        stoneBricks = ImageTools.load("resources/stone-bricks.png");
        water = ImageTools.load("resources/water.png");
        wood = ImageTools.load("resources/wood.png");
        voidTile = ImageTools.load("resources/void-tile.png");

        //player
        playerRight = ImageTools.load("resources/player.png");
        playerDown = ImageTools.rotate(playerRight, 90);
        playerUp = ImageTools.rotate(playerRight, -90);
        playerLeft = ImageTools.rotate(playerRight, 180);

        game = new SneakGame();

        addMouseListener(this);
        addKeyListener(this);

        Logger.logCodeMessage("Initialized panel.");

        Thread t = new Thread(this);
        t.start();

    }

    public void paint(Graphics g) {
        Graphics bg = buffer.getGraphics();
        drawMap(bg);
        if (Tuning.DEBUG) {
            drawGuidelines(bg, true);
        }

        switch (game.getPlayer().getOrientation()) {
            case 0:
                bg.drawImage(playerUp, game.getPlayer().getCurrentTile().getX(), game.getPlayer().getCurrentTile().getY(), null);
                break;
            case 1:
                bg.drawImage(playerLeft, game.getPlayer().getCurrentTile().getX(), game.getPlayer().getCurrentTile().getY(), null);
                break;
            case 2:
                bg.drawImage(playerDown, game.getPlayer().getCurrentTile().getX(), game.getPlayer().getCurrentTile().getY(), null);
                break;
            case 3:
                bg.drawImage(playerRight, game.getPlayer().getCurrentTile().getX(), game.getPlayer().getCurrentTile().getY(), null);
                break;

        }
        drawValidMoves(bg, game.getPlayer().getCurrentTile());

        g.drawImage(buffer, 0, 0, null);
    }

    /**
     * Draws 4 rectangles out from the current tile, displaying valid move positions for that tile.
     *
     * @param g Graphics to draw to.
     * @param currentTile Current tile of reference.
     */
    private void drawValidMoves(Graphics g, Tile currentTile) {
        g.setColor(new Color(255, 255, 255, 100));
        //up from tile
        for (int i = 1; i <= currentTile.getMovementRange(); i++) {
            int y = currentTile.getY() - (i * Tuning.TILE_SIZE);
            int x = currentTile.getX();
            //stop drawing out if we hit an impassible tile
            if (y > Tuning.SCREEN_HEIGHT || x > Tuning.SCREEN_WIDTH || y < 0 || x < 0 || !game.convertCoords(x, y).isPassable()) {
                break;
            }
            g.fillRect(x, y, Tuning.TILE_SIZE, Tuning.TILE_SIZE);
        }
        //down from tile
        for (int i = 1; i <= currentTile.getMovementRange(); i++) {
            int y = currentTile.getY() + (i * Tuning.TILE_SIZE);
            int x = currentTile.getX();
            //stop drawing out if we hit an impassible tile
            if (y > Tuning.SCREEN_HEIGHT || x > Tuning.SCREEN_WIDTH || y < 0 || x < 0 || !game.convertCoords(x, y).isPassable()) {
                break;
            }
            g.fillRect(x, y, Tuning.TILE_SIZE, Tuning.TILE_SIZE);
        }
        //right from tile
        for (int i = 1; i <= currentTile.getMovementRange(); i++) {
            int y = currentTile.getY();
            int x = currentTile.getX() + (i * Tuning.TILE_SIZE);
            //stop drawing out if we hit an impassible tile
            if (y > Tuning.SCREEN_HEIGHT || x > Tuning.SCREEN_WIDTH || y < 0 || x < 0 || !game.convertCoords(x, y).isPassable()) {
                break;
            }
            g.fillRect(x, y, Tuning.TILE_SIZE, Tuning.TILE_SIZE);
        }
        //left from tile
        for (int i = 1; i <= currentTile.getMovementRange(); i++) {
            int y = currentTile.getY();
            int x = currentTile.getX() - (i * Tuning.TILE_SIZE);
            //stop drawing out if we hit an impassible tile
            if (y > Tuning.SCREEN_HEIGHT || x > Tuning.SCREEN_WIDTH || y < 0 || x < 0 || !game.convertCoords(x, y).isPassable()) {
                break;
            }
            g.fillRect(x, y, Tuning.TILE_SIZE, Tuning.TILE_SIZE);
        }
    }

    /**
     * Draws the map onto the screen
     *
     * @param g Graphics to draw onto.
     */
    private void drawMap(Graphics g) {
        for (int x = 0; x < Tuning.MAP_WIDTH * Tuning.TILE_SIZE; x += Tuning.TILE_SIZE) {
            for (int y = 0; y < Tuning.MAP_HEIGHT * Tuning.TILE_SIZE; y += Tuning.TILE_SIZE) {
                Tile currentTile = game.getGrid()[y / Tuning.TILE_SIZE][x / Tuning.TILE_SIZE];
                if (currentTile.getX() > Tuning.SCREEN_WIDTH || currentTile.getX() < 0) { //don't render offscreen tiles
                    continue;
                }
                switch (currentTile.getType()) { //draw based on type
                    case 0: //grass
                        switch (currentTile.getGrassType()) {
                            case 0:
                                g.drawImage(grass, x, y, null);
                                break;
                            case 1:
                                g.drawImage(grass2, x, y, null);
                                break;
                            case 2:
                                g.drawImage(grass3, x, y, null);
                                break;
                            case 3:
                                g.drawImage(grass4, x, y, null);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 1: //ice
                        g.drawImage(ice, x, y, null);
                        break;
                    case 2: //mud
                        g.drawImage(mud, x, y, null);
                        break;
                    case 3: //sand
                        g.drawImage(sand, x, y, null);
                        break;
                    case 4: //stone
                        g.drawImage(stone, x, y, null);
                        break;
                    case 5: //stone bricks
                        g.drawImage(stoneBricks, x, y, null);
                        break;
                    case 6: //water
                        g.drawImage(water, x, y, null);
                        break;
                    case 7: //wood
                        g.drawImage(wood, x, y, null);
                        break;
                    case 8: //void
                        g.drawImage(voidTile, x, y, null);
                        break;
                    default:
                        System.err.println("Cannot determine image to draw from tile type: " + currentTile.getType());
                        break;
                }
            }
        }
    }

    /**
     * Draws guidelines based on tile size and screen size. Used for positioning tiles.
     *
     * @param g Graphics to paint the lines onto.
     */
    private void drawGuidelines(Graphics g, final boolean drawNums) {
        g.setColor(Color.WHITE);
        for (int x = 0; x < Tuning.SCREEN_WIDTH; x += Tuning.TILE_SIZE) {
            g.drawLine(x, 0, x, Tuning.SCREEN_HEIGHT);
        }
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
        counter = 0;
        for (int i = 5; i < Tuning.SCREEN_WIDTH; i += Tuning.TILE_SIZE) {
            g.drawString("" + counter, i, 8);
            counter++;
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
            System.out.println("Making new game.");
            Logger.logCodeMessage("Making new game.");
            game = new SneakGame();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Tile move = game.convertCoords(x, y);

        if (game.getPlayer().isValidMove(move)) { //if the player can move, move them
            if (move.getX() > game.getPlayer().getX()) {
                game.getPlayer().setOrientation(Player.RIGHT);
            }

            game.getPlayer().setX(x);
            game.getPlayer().setY(y);
        }
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
    public void mouseReleased(MouseEvent e) {
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

}
