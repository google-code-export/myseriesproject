/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents;

import components.MyDraggable;
import java.awt.Color;
import java.util.logging.Level;
import javax.swing.JPanel;
import myComponents.myGUI.MyDisabledGlassPane;
import sdialogs.Ask;
import sdialogs.Confirm;
import sdialogs.Info;
import tools.MySeriesLogger;
import tools.Skin;
import tools.options.Options;

/**
 * The messages helper class
 * @author 
 */
public class MyMessages {

  private static boolean glassPaneActive;
  private static MyLogPanel logPanel;

  public static void setLogPanel(MyLogPanel logPanel) {
    MyMessages.logPanel = logPanel;
  }

  private static void checkpane() {
    glassPaneActive = MyDisabledGlassPane.isActivated;
    if (!glassPaneActive) {
      myseries.MySeries.glassPane.activate(null);
    }
  }

  /**
   * Display a message to the user
   * @param title The message title
   * @param message The message text
   */
  public static void message(String title, String message) {
    message(title, message, Info.INFO_MESS, true);
  }

  /**
   * Display a message to the user
   * @param title The message title
   * @param message The message text
   */
  public static void message(String title, String message, int type, boolean showWindow) {
    Level level = Level.INFO;
    switch (type) {
      case Info.INFO_MESS:
        level = Level.INFO;
        break;
      case Info.WARNING_MESS:
        level = Level.WARNING;
        break;
      case Info.ERROR_MESS:
        level = Level.SEVERE;
        break;
    }
    MySeriesLogger.logger.log(level, "Displaying info dialog : {0}", message);
    if (showWindow) {
      checkpane();
      Info i = new Info(title, message, type);
      setColors(i);
      i.showDialog();
      hideMessage();
    }
    if (type != Info.INFO_MESS) {
      logToPanel(type, message);
    }
  }

  /**
   * Displays an error dialog
   * @param title The title
   * @param message The message
   */
  public static void error(String title, String message) {
    message(title, message, Info.ERROR_MESS, true);
  }

  /**
   * Displays an internet connection error
   */
  public static void internetError(boolean showMessage) {
    message("No Internet Connection!!!",
        "Could not connect to internet.\nIf you are behind a proxy check your proxy settings in options",
        Info.WARNING_MESS, showMessage);

  }

  /**
   * Displays a YES NO dialog
   * @param title The title
   * @param message The message
   * @return The users decision
   */
  public static int confirm(String title, String message) {
    checkpane();
    MySeriesLogger.logger.log(Level.INFO, "Displaying confirm dialog : {0}", message);
    Confirm c = new Confirm(title, message);
    setColors(
        c);
    c.showDialog();
    hideMessage();


    return c.answer;


  }

  public static String ask(String title, String message) {
    checkpane();
    MySeriesLogger.logger.log(Level.INFO, "Show ask dialog : {0}", message);
    String answer = (String) ask(title, message, null, null, null);
    hideMessage();


    return answer;


  }

  public static Object ask(String title, String message, String errorMessage, Object[] collection, Object defaultSelection) {
    checkpane();
    MySeriesLogger.logger.log(Level.INFO, "Show ask dialog : {0}", message);
    Ask a = new Ask(title, message, null, collection, defaultSelection);
    setColors(
        a);
    a.showDialog();
    Object name = a.selection;
    hideMessage();


    return name;


  }

  private static void setColors(MyDraggable drag) {
    if (Options.toBoolean(Options.USE_SKIN)) {
      drag.setOuterColor(Skin.getOuterColor());
      drag.setInnerColor(Color.WHITE);
      drag.setTitleColor(Skin.getTitleColor());
      drag.setTextColor(Skin.getForeGroundColor(Skin.getInnerColor()));


    }
  }

  private static void hideMessage() {
    if (!glassPaneActive) {
      myseries.MySeries.glassPane.deactivate();


    }
  }

  public static void logToPanel(int type, String message) {
    if (logPanel != null) {
      logPanel.setVisible(true);
      logPanel.log(type, System.currentTimeMillis(), message);

    }
  }

  private MyMessages() {
  }
}
