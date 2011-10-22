/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents;

import components.MyDraggable;
import java.util.logging.Level;
import myComponents.myGUI.MyDisabledGlassPane;
import myseriesproject.MySeries;
import sdialogs.Ask;
import sdialogs.Confirm;
import sdialogs.Info;
import tools.MySeriesLogger;
import tools.options.MySeriesOptions;

/**
 * The messages helper class
 * @author 
 */
public class MyMessages {

  private static boolean wasGlassPaneActive;
  private static MyLogPanel logPanel;
  public static final int ERROR_MESS = 0;
  public static final int WARNING_MESS = 2;
  public static final int INFO_MESS = 1;

  public static void setLogPanel(MyLogPanel logPanel) {
    MyMessages.logPanel = logPanel;
  }

  private static void checkpane() {
    wasGlassPaneActive = MyDisabledGlassPane.isActivated;
    if (!wasGlassPaneActive) {
      myseriesproject.MySeries.glassPane.activate(null);
    }
  }

  /**
   * Display a message to the user
   * @param title The message title
   * @param message The message text
   */
  public static void message(String title, String message) {
    message(title, message, INFO_MESS, false, true);
  }

  /**
   * Display a message to the user
   * @param title The message title
   * @param type The type of the message
   * @param log Log the message
   * @param message The message text
   */
  public static void message(String title, String message, int type, boolean log) {
    if(log){
      message(title, message, type, false, true);
    } else {
      Info i = new Info(title, message, type);
      i.showDialog();
      hideMessage();
    }
  }

  /**
   * Display a message to the user
   * @param title The message title
   * @param showWindow If a window should be shown anyway
   * @param message The message text
   */
  private static void message(String title, String message, int type, boolean showWindow, boolean setColors) {
    Level level = Level.INFO;
    switch (type) {
      case INFO_MESS:
        level = Level.INFO;
        break;
      case WARNING_MESS:
        level = Level.WARNING;
        break;
      case ERROR_MESS:
        level = Level.SEVERE;
        break;
    }
    MySeriesLogger.logger.log(level, "Displaying info dialog : {0}", message);
    if (showWindow || MySeries.options.getBooleanOption(MySeriesOptions.SHOW_POPUPS)) {
      checkpane();
      Info i = new Info(title, message, type);
      if(setColors){
        setColors(i);
      }
      i.showDialog();
      hideMessage();
    }
    if (type != INFO_MESS || !MySeries.options.getBooleanOption(MySeriesOptions.SHOW_POPUPS)) {
      logToPanel(type, title, message);
    }
  }

  /**
   * Displays an error dialog
   * @param title The title
   * @param message The message
   */
  public static void error(String title, String message, boolean setColors) {
    message(title, message, ERROR_MESS, false, setColors);
  }
  
  /**
   * Displays a warning dialog
   * @param title The title
   * @param message The message
   */
  public static void warning(String title, String message, boolean setColors) {
    message(title, message, WARNING_MESS, false, setColors);
  }
  
  public static void validationError(String title, String message){
    message(title, message, WARNING_MESS, true, true);
  }

  /**
   * Displays an internet connection error
   */
  public static void internetError() {
    message("No Internet Connection!!!",
        "Could not connect to internet.\nIf you are behind a proxy check your proxy settings in options",
        WARNING_MESS, false, true);

  }

  /**
   * Displays a YES NO dialog
   * @param title The title
   * @param message The message
   * @return The users decision
   */
  public static int confirm(String title, String message, boolean setColors) {
    checkpane();
    MySeriesLogger.logger.log(Level.INFO, "Displaying confirm dialog : {0}", message);
    Confirm c = new Confirm(title, message);
     if(setColors){
        setColors(c);
      }
    c.showDialog();
    hideMessage();


    return c.answer;


  }

  public static String ask(String title, String message) {
    checkpane();
    MySeriesLogger.logger.log(Level.INFO, "Show ask dialog : {0}", message);
    String answer = (String) ask(title, message, null, null, null, true);
    hideMessage();
    return answer;


  }

  public static Object ask(String title, String message, String errorMessage, Object[] collection, Object defaultSelection, boolean setColors) {
    checkpane();
    MySeriesLogger.logger.log(Level.INFO, "Show ask dialog : {0}", message);
    Ask a = new Ask(title, message, null, collection, defaultSelection);
     if(setColors){
        setColors(a);
      }
    a.showDialog();
    Object name = a.selection;
    hideMessage();


    return name;


  }

  private static void setColors(MyDraggable drag) {
    if (MySeries.options.getBooleanOption(MySeriesOptions.USE_SKIN)) {
      //drag.setOuterColor(Skin.getOuterColor());
      //drag.setInnerColor(Color.WHITE);
      //drag.setTitleColor(Skin.getTitleColor());
      //drag.setTextColor(Skin.getForeGroundColor(Skin.getInnerColor()));
    }
  }

  private static void hideMessage() {
    if (!wasGlassPaneActive) {
      myseriesproject.MySeries.glassPane.deactivate();
    }
  }

  public static void logToPanel(int type, String title,  String message) {
    if (logPanel != null) {
      logPanel.setVisible(true);
      logPanel.log(type, System.currentTimeMillis(),title, message);

    }
  }

  private MyMessages() {
  }
}
