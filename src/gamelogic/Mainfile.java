package gamelogic;

import gamewindow.SneakFrame;
import javax.swing.JOptionPane;
import libraries.Logger;

/**
 * The main file of the game
 *
 * @author Noah Morton
 *
 * Date created: May 1, 2017
 *
 * Part of project: ScrollingGame
 */
public class Mainfile {

    public static void main(String[] args) {
        System.out.println("Init");
        new Logger();
        Logger.logCodeMessage("-------------Start Game ---------");
        new SneakFrame();
        if (!Tuning.DEBUG) {
            Logger.messageWindow("Welcome to SneakGame!");
            if (JOptionPane.showConfirmDialog(null, "Would you like more detailed instructions?", "Instructions", JOptionPane.YES_NO_OPTION) == 0) {
                Logger.messageWindow("To play, click somewhere within the white squares around your character.\n"
                        + "Depending on the tile that you're standing on, you can move different ranges.\n"
                        + "Enemies are also bound by this rule. Rougher tiles, such as mud, will slow you down,\n"
                        + "while smoother tiles like ice will speed you up. Enemies will kill you by moving onto your square\n"
                        + "after you move, indicated by black transparent squares around them. However, you can kill enemies\n"
                        + "by moving onto them on purpose. To win, make it to the castle at the right end of the field.\n"
                        + "To restart, press the N key.");
            }
        }
    }
}
