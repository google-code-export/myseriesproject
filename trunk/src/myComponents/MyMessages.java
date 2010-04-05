/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents;

import javax.swing.JOptionPane;

/**
 *
 * @author ΔΙΟΝΥΣΗΣ
 */
public class MyMessages {

  /**
   * Display a message to the user
   * @param title The message title
   * @param message The message text
   */
  public static void message(String title, String message) {
    JOptionPane.showMessageDialog(null,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Displays an error dialog
   * @param title The title
   * @param message The message
   */
  public static void error(String title, String message) {
    JOptionPane.showMessageDialog(null,
            message,
            title,
            JOptionPane.ERROR_MESSAGE);
  }

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
    return JOptionPane.showConfirmDialog(null,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
  }
}
