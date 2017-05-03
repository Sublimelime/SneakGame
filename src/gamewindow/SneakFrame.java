package gamewindow;

import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import libraries.Logger;

/**
 * The frame of the game.
 *
 * @author Noah Morton
 *
 * Date created: May 3, 2017
 *
 * Part of project: ScrollingGame
 */
public class SneakFrame extends JFrame {

    public SneakFrame() {
        super("Sneak - The top down stealh game");

        // Sets the close button to exit the program
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // makes the window not able to be resized
        setResizable(false);
        // creates the window
        pack();
        // creates the panel
        SneakPanel p = new SneakPanel();
        // gets the frames insets
        Insets frameInsets = getInsets();
        // calculates panel size
        int frameWidth = p.getWidth()
                + (frameInsets.left + frameInsets.right);
        int frameHeight = p.getHeight()
                + (frameInsets.top + frameInsets.bottom);
        // sets the frame's size
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        // turns off the layout options
        setLayout(null);
        // adds the panel to the frame
        add(p);
        // adjusts the window to meet its new preferred size
        pack();
        // shows the frame
        setVisible(true);

        Logger.logOtherMessage("Window", "Window Created.");

    }
}
