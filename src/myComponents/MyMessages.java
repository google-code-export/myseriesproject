/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents;

import javax.swing.JOptionPane;
import myComponents.myGUI.buttons.MyButtonCancel;
import myComponents.myGUI.buttons.MyButtonOk;
import sdialogs.Ask;
import sdialogs.Confirm;
import sdialogs.Info;

/**
 * The messages helper class
 * @author 
 */
public class MyMessages {

    /**
     * Display a message to the user
     * @param title The message title
     * @param message The message text
     */
    public static void message(String title, String message) {
        //    JOptionPane.showMessageDialog(null,
        //            message,
        //            title,
        //            JOptionPane.INFORMATION_MESSAGE);
        Info info = new Info(title, message, Info.INFO_MESS);
    }

    /**
     * Displays an error dialog
     * @param title The title
     * @param message The message
     */
    public static void error(String title, String message) {
//    JOptionPane.showMessageDialog(null,
//            message,
//            title,
//            JOptionPane.ERROR_MESSAGE);
        new Info(title, message, Info.ERROR_MESS);
    }

    /**
     * Displays an internet connection error
     */
    public static void internetError() {
        error("No Internet Connection!!!", "Could not connect to internet\nIf you are behind a proxy check your proxy settings in options");
    }

    /**
     * Displays a YES NO dialog
     * @param title The title
     * @param message The message
     * @return The users decision
     */
    public static int question(String title, String message) {
//    return JOptionPane.showConfirmDialog(null,
//            message,
//            title,
//            JOptionPane.YES_NO_OPTION,
//            JOptionPane.QUESTION_MESSAGE);
        Confirm c = new Confirm(title, message);
        return c.answer;
    }

    /**
     * Displays a form validation error
     */
    public static void validationError() {
        error("Validation failed", "The form contains errors");
    }

    public static String ask(String title, String message) {
        return (String) ask(title, message, null, null, null);
    }

    public static Object ask(String title, String message, String errorMessage, Object[] collection, Object defaultSelection) {
        Ask a = new Ask(title, message, null, collection,defaultSelection);
        a.showDialog();
        Object name = a.selection;
        return name;
    }

    private MyMessages() {
    }
}
