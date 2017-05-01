package libraries;

import java.io.*;
import javax.swing.JOptionPane;

/**
 * This is a logging class written to log basic events within my code, for debug purposes. Everything
 * should theoretically work, with a few tweaks here and there.
 *
 * @author Noah Morton
 * @version 0.2
 */
@SuppressWarnings("ALL")
public class Logger {

    private static File f;

    public Logger() {
        f = new File("programLog.log");
        Logger.logOtherMessage("New Run", "----------------------\n");
    }

    /**
     * logs a code message, such as a creation success.
     *
     * @param message The message to be written.
     */
    public static void logCodeMessage(String message) {
        try {
            try (FileWriter fw = new FileWriter(f, true) //the true will append the new data
                    ) {
                fw.write("\n[Code] " + message);
            }
        } catch (IOException e) {
            System.err.println("Error with writing logging file. " + e.getMessage());
        }
    }

    /**
     * logs input of the user in the console/gui, for recreation of bugs.
     *
     * @param message The message to be written.
     */
    public static void logUserMessage(String message) {
        try {
            try (FileWriter fw = new FileWriter(f, true) //the true will append the new data
                    ) {
                fw.write("\n[User] " + message);
            }
        } catch (IOException e) {
            System.err.println("Error with writing logging file. " + e.getMessage());
        }

    }

    /**
     * logs error and failure messages.
     *
     * @param message Message to be logged after [Error]
     */
    public static void logErrorMessage(String message) {
        try {
            try (FileWriter fw = new FileWriter(f, true) //the true will append the new data
                    ) {
                fw.write("\n[Error] " + message);
            }
        } catch (IOException e) {
            System.err.println("Error with writing logging file. " + e.getMessage());

        }
    }

    /**
     * logs other messages, that don't fit into a category.
     *
     * @param type The message to be displayed inside brackets, eg. [Window]
     * @param message The message to be written after the brackets.
     */
    public static void logOtherMessage(String type, String message) {

        try {
            try (FileWriter fw = new FileWriter(f, true) //the true will append the new data
                    ) {
                fw.write("\n[" + type + "] " + message);
            }
        } catch (IOException e) {
            System.err.println("Error with writing logging file. " + e.getMessage());
        }

    }

    /**
     * create a message window, for GUI based notices.
     *
     * @param message The message to be displayed in the window.
     */
    public static void messageWindow(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Displays a error window.
     *
     * @param title The title of the error window.
     * @param message The error message to be displayed.
     */
    public static void errorWindow(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
