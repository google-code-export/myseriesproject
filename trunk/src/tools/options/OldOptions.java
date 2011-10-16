/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.options;

import Exceptions.OptionFormatException;
import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.JFrame;
import myComponents.MyUsefulFunctions;
import tools.MySeriesLogger;
import tools.download.subtitles.SubtitleConstants;

/**
 *
 * @author lordovol
 */
public class OldOptions implements IOldOptions{

  /**
   * The options map
   */
  private static Map<String, Object> options;
 /**
   * The install directory
   */
  public static String _USER_DIR_ = "./";
  /**
   * Loads the options from MySeries.ini or writes a default if <br />
   * it does not exist
   * @throws java.io.FileNotFoundException
   * @throws java.io.IOException
   */
  public static void getOptions() throws FileNotFoundException, IOException {
    //_USER_DIR_ = System.getProperties().getProperty("user.dir");
    //_USER_DIR_ = "./";
    options = new HashMap<String, Object>();
    loadDefaultOptions();
    if (!new File(_USER_DIR_ + "MySeries.ini").isFile()) {
      if (MySeriesLogger.logger != null) {
        MySeriesLogger.logger.log(Level.INFO, "No options file found, creating file with default options");
      }
      save();
    }
    File opFile = new File(_USER_DIR_ + "MySeries.ini");
    if (MySeriesLogger.logger != null) {
      MySeriesLogger.logger.log(Level.INFO, "Reading options file {0}", opFile);
    }
    BufferedReader in = MyUsefulFunctions.createInputStream(opFile);
    String line = "";
    String[] fields;
    Object value = null;
    while ((line = in.readLine()) != null) {
      if (!line.trim().equals("") && !line.startsWith("/")) {
        fields = line.split("=", -1);
        if (fields[1].trim().equals("true") || fields[1].trim().equals("false")) {
          value = Boolean.parseBoolean(fields[1].trim());
        } else if (MyUsefulFunctions.isNumeric(fields[1].trim())) {
          value = Long.parseLong(fields[1].trim());
        } else {
          value = String.valueOf(fields[1]);
        }
        options.put(fields[0].trim(), value);
      }
    }
  }

  public static Integer[] toIntegerArray(String key) {
    MySeriesLogger.logger.log(Level.INFO, "Getting integer array for key {0}", key);
    String w = getStringOption(key).replaceAll("\\[", "").replaceAll("\\]", "");
    if (w.equals("")) {
      MySeriesLogger.logger.log(Level.INFO, "Empty value found");
      return null;
    }
    String[] arr = w.split(",");
    Integer[] intArr = new Integer[arr.length];
    for (int i = 0; i < arr.length; i++) {
      try {
        intArr[i] = Integer.parseInt(arr[i].trim());
      } catch (NumberFormatException ex) {
        MySeriesLogger.logger.log(Level.WARNING, "Not an integer {0} setting it to -1", arr[i]);
        intArr[i] = -1;
      }
    }
    MySeriesLogger.logger.log(Level.FINE, "Return array {0}", intArr);
    return intArr;
  }

  /**
   * Get tables widths
   * @return
   */
  public static ArrayList<Integer> toIntegerArrayList() {
    MySeriesLogger.logger.log(Level.INFO, "Getting table widths");
    ArrayList<Integer> widths = new ArrayList<Integer>();
    if (getStringOption(TABLE_WIDTHS) == null) {
      try {
        throw new OptionFormatException("Table widths is null");
      } catch (OptionFormatException ex) {
        if (MySeriesLogger.logger != null) {
          MySeriesLogger.logger.log(Level.WARNING, ex.getMessage(), ex);
        }
        return getDefaultColumnWidths();
      }
    }
    String w = getStringOption(TABLE_WIDTHS).replaceAll("\\[", "").replaceAll("\\]", "");
    String[] arr = w.split(",");
    try {
      for (int i = 0; i < _TOTAL_COLUMNS_; i++) {
        try {
          widths.add(i, Integer.parseInt(arr[i].trim()));
        } catch (ArrayIndexOutOfBoundsException ex) {
          widths.add(i, 100);
        }
      }
      MySeriesLogger.logger.log(Level.INFO, "Return table widths", widths);
      return widths;
    } catch (NumberFormatException ex) {
      try {
        throw new OptionFormatException("Table width is not an integer");
      } catch (OptionFormatException ex1) {
        if (MySeriesLogger.logger != null) {
          MySeriesLogger.logger.log(Level.WARNING, ex.getMessage(), ex);
        }
        return getDefaultColumnWidths();
      }
    }
  }

  private static ArrayList<Integer> getDefaultColumnWidths() {
    if (MySeriesLogger.logger != null) {
      MySeriesLogger.logger.log(Level.INFO, "Getting default column widths");
    }
    ArrayList<Integer> widths = new ArrayList<Integer>();
    for (int i = 0; i < _TOTAL_COLUMNS_; i++) {
      widths.add(i, 100);
    }
    if (MySeriesLogger.logger != null) {
      MySeriesLogger.logger.log(Level.INFO, "Return table widths", widths);
    }
    return widths;
  }

  /**
   * Get an Option as Color
   * @param value
   * @return
   */
  public static Color getColorOption(String value) {
    MySeriesLogger.logger.log(Level.INFO, "Getting Color option for key {0}", value);
    Color c = new Color(240, 240, 240);
    String strColor = getStringOption(value.trim());
    String colors[] = strColor.split(",", -1);
    if (colors.length != 3) {
      try {
        throw new OptionFormatException("Wrong color format for " + value + "- R, G and B not present : '" + strColor + "'");
      } catch (OptionFormatException ex) {
        if (MySeriesLogger.logger != null) {
          MySeriesLogger.logger.log(Level.WARNING, ex.getMessage(), ex);
        }
        setOption(SKIN_COLOR, "240,240,240");
        save();
        MySeriesLogger.logger.log(Level.INFO, "Returning default color {0}", c);
        return c;
      }
    }
    try {
      c = new Color(Integer.parseInt(colors[0].trim()), Integer.parseInt(colors[1].trim()), Integer.parseInt(colors[2].trim()));
    } catch (NumberFormatException ex) {
      try {
        throw new OptionFormatException("Wrong color format for " + value + "  - R,G or B not an integer: '" + strColor + "'");
      } catch (OptionFormatException ex1) {
        if (MySeriesLogger.logger != null) {
          MySeriesLogger.logger.log(Level.WARNING, ex1.getMessage(), ex1);
        }
        setOption(SKIN_COLOR, "240,240,240");
        save();
        MySeriesLogger.logger.log(Level.INFO, "Returning default color {0}", c);
        return c;
      }
    }
    MySeriesLogger.logger.log(Level.INFO, "Returning color {0}", c);
    return c;
  }

  /**
   * Get an option of an integer type
   * @param key The option to get
   * @return
   */
  public static int getIntegerOption(String key) {
    int val = 0;
    String s;
    MySeriesLogger.logger.log(Level.INFO, "Getting integer option for key {0}", key);
    try {
      s = String.valueOf(options.get(key)).trim();
      if (s != null) {
        val = Integer.parseInt(s);
      } else {
        MySeriesLogger.logger.log(Level.INFO, "Value is null setting to 0");
        val = 0;
      }
    } catch (NumberFormatException ex) {
      try {
        throw new OptionFormatException("value " + String.valueOf(options.get(key)).trim() + " of " + key + " is not an integer, setting it to 0");
      } catch (OptionFormatException ex1) {
        if (MySeriesLogger.logger != null) {
          MySeriesLogger.logger.log(Level.WARNING, ex1.getMessage(), ex1);
        }
        setOption(key, 0);
        save();
        MySeriesLogger.logger.log(Level.INFO, "Parse exception setting to 0");
        return 0;
      }
    }
    MySeriesLogger.logger.log(Level.INFO, "Return integer {0}", val);
    return val;
  }

  /**
   * Get an option of a float type
   * @param key The option to get
   * @return
   */
  public static float getFloatOption(String key) {
    MySeriesLogger.logger.log(Level.INFO, "GEtting float option for key {0}", key);
    float val = 0.0F;
    String s;
    try {
      s = String.valueOf(options.get(key)).trim();
      if (s != null) {
        val = Float.parseFloat(s);
      } else {
        MySeriesLogger.logger.log(Level.INFO, "Value is null returning 0F");
        val = 0F;
      }
    } catch (NumberFormatException ex) {
      try {
        throw new OptionFormatException("value " + String.valueOf(options.get(key)).trim() + " of " + key + " is not a float, setting it to 0.0");
      } catch (OptionFormatException ex1) {
        if (MySeriesLogger.logger != null) {
          MySeriesLogger.logger.log(Level.WARNING, ex1.getMessage(), ex1);
        }
        MySeriesLogger.logger.log(Level.INFO, "Parse exception returning 0.0F");
        setOption(key, 0.0F);
        save();
        return 0.0F;
      }
    }
    MySeriesLogger.logger.log(Level.INFO, "Returning value {0}", val);
    return val;
  }

  /**
   * Get an option of a boolean type
   * @param key The option to get
   * @return
   */
  public static Boolean getBooleanOption(String key) {
    MySeriesLogger.logger.log(Level.INFO, "Getting boolean option for key {0}", key);
    Boolean val = false;
    String value = String.valueOf(options.get(key)).trim();
    if (value.trim().equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
      val = Boolean.parseBoolean(String.valueOf(options.get(key)));
    } else {
      try {
        throw new OptionFormatException(key + " is not a boolean, setting it to false '" + value + "'");
      } catch (OptionFormatException ex1) {
        if (MySeriesLogger.logger != null) {
          MySeriesLogger.logger.log(Level.WARNING, ex1.getMessage(), ex1);
        }
        setOption(key, "false");
        save();
        MySeriesLogger.logger.log(Level.INFO, "Parse exception returning false");
        return false;
      }
    }
    MySeriesLogger.logger.log(Level.INFO, "Returning boolean value {0}", val);
    return val;
  }

  /**
   * Get an option of a string type
   * @param key The option to get
   * @return
   */
  public static String getStringOption(String key) {
    return getStringOption(key, true);
  }

  /**
   * Get an option of a string type
   * @param key The option to get
   * @param trim Trim value or not
   * @return
   */
  public static String getStringOption(String key, boolean trim) {
    MySeriesLogger.logger.log(Level.INFO, "Getting " + (trim ? "trimmed" : "") + " string value for key {0}", key);
    String val = trim ? String.valueOf(options.get(key)).trim() : String.valueOf(options.get(key));
    String v = val != null && !val.equals("null") ? val : "";
    MySeriesLogger.logger.log(Level.INFO, "Returning value {0}", v);
    return v;
  }

  /**
   * Sets an option
   * @param key The option to set
   * @param value The value to set
   */
  @SuppressWarnings("unchecked")
  public static void setOption(String key, Object value) {
    try {
      if (MySeriesLogger.logger != null) {
        MySeriesLogger.logger.log(Level.INFO, "Setting option {0} to value {1}", new Object[]{key, value});
      }
      options.put(key, value);
      if (MySeriesLogger.logger != null) {
        MySeriesLogger.logger.log(Level.FINE, "Option set");
      }
    } catch (NullPointerException ex) {
      if (MySeriesLogger.logger != null) {
        MySeriesLogger.logger.log(Level.WARNING, "Null pointer exception", ex);
      }
    }

  }

  /**
   * Writes the default ini file
   * @throws java.io.IOException
   */
  private static void writeDefaultIniFile() throws IOException {
    MySeriesLogger.logger.log(Level.INFO, "Writting default ini file");
    PrintWriter out = MyUsefulFunctions.createOutputStream(new File(_USER_DIR_ + "MySeries.ini"), false);
    out.println(DB_NAME + "=");
    out.println(DEBUG_MODE + "=0");
    //out.println(MODAL + "=true");
    out.println(DATE_FORMAT + "=dd/MM/yyyy");
    out.println(LOOK_AND_FEEL + "=EaSynth");
    out.println(SKIN_COLOR + " =240,240,240");
    out.println(USE_SKIN + " =false");
    out.println(USE_PROXY + " =false");
    out.println(UNIFIED_SERIES + " =true");
    out.println(PROXY_HOST + " =");
    out.println(PROXY_PORT + " =");
    out.println(DIVIDER_LOCATION + " =250");
    out.println(FEED_DIVIDER_LOCATION + " =250");
    out.println(FONT_FACE + " =Arial");
    out.println(FONT_SIZE + " =12");
    out.println(TABLE_WIDTHS + " =" + getDefaultColumnWidths());
    out.println(WINDOW_STATE + " =" + JFrame.NORMAL);
    out.println(WIDTH + " =1000");
    out.println(HEIGHT + " =600");
    out.println(CHECK_VERSION + " =true");
    out.println(PRIMARY_SUB + " =Greek");
    out.println(SECONDARY_SUB + " =English");
    out.println(SUBTITLE_SITE + " =" + SubtitleConstants.SUBTITLE_ONLINE_NAME);
    out.println(AUTO_FILE_UPDATING + " =false");
    out.println(SEASON_SEPARATOR + " =SE");
    out.println(TITLE_SEPARATOR + " = - ");
    out.println(EPISODE_SEPARATOR + " =x");
    out.println(TOOLBAR_POSITION + " =1");
    out.println(TOOLBAR_BUTTONS + "=" + getDefaultToolbarButtons());
    out.println(FEED_COLUMNS + "=" + 1);
    out.println(VIDEO_APP + "=");
    out.println(AUTO_EXTRACT_ZIPS + "=false");
    out.println(UPDATE_FEEDS + "=false");
    out.println(TABS_ORDER + "=" + getDefaultTabsOrder());
    out.println(MAIN_DIRECTORY + "=" + getDefaultMainDirectory());
    out.println(WARN_FOR_LOG_USE + "=true");
    out.println(WARN_FOR_VERSION + "=true");
    out.println(NO_RENAME_CONFIRMATION + "=false");
    out.println(AUTO_RENAME_SUBS + "=false");
    out.println(MINIMIZE_TO_TRAY + "=false");
    out.println(RANDOMIZE_LAF + "=false");
    out.println(ACTIVE_FILTER + "=");
    out.println(MEMORY_CONSUMPTION_UPDATE + "=1000");
    out.println(FEED_UPDATE_FREQUENCY + "=0");
    out.println(IMAGE_QUALITY + "=3");
    out.println(SEARCH_FOR_SECONDARY_SUBTITLE + "=true");
    out.println(VIDEO_QUALITY+ "=");
    out.println(LATEST_NEWS_ID+ "=0");
    out.println(SHOW_POPUPS+ "=true");

    out.close();
  }

  /**
   * Saves the options file
   */
  public static void save() {
    PrintWriter out = null;
    ArrayList<String> arr;
    if (MySeriesLogger.logger != null) {
      MySeriesLogger.logger.log(Level.INFO, "Saving options");
    }
    Object[] keys = options.keySet().toArray();
    Arrays.sort(keys);
    try {
      out = MyUsefulFunctions.createOutputStream(new File(_USER_DIR_ + "MySeries.ini"), false);

      for (int i = 0; i < keys.length; i++) {
        String key = String.valueOf(keys[i]);
        String value = "";
        if (options.get(key) instanceof Object[]) {
          Object[] obj = (Object[]) options.get(key);
          value = Arrays.asList(obj).toString();
        } else {
          value = String.valueOf(options.get(key));
        }
        // Check DB extension
        if (key.equals(DB_NAME)) {
          if (!value.endsWith(".db")) {
            value = value + ".db";
          }
        }
        out.println(key + "=" + value);
      }
      out.close();
      if (MySeriesLogger.logger != null) {
        MySeriesLogger.logger.log(Level.FINE, "Options saved");
      }
    } catch (IOException ex) {
      if (MySeriesLogger.logger != null) {
        MySeriesLogger.logger.log(Level.SEVERE, "Cannot write the ini file", ex);
      }
    } finally {
      out.close();
    }
  }

  public static ArrayList<Integer> getDefaultToolbarButtons() {
    Integer[] integ =  new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
          12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
    return new ArrayList<Integer>(Arrays.asList(integ));
  }

  private static  ArrayList<Integer> getDefaultTabsOrder() {
    Integer[] integ = new Integer[]{0, 1, 2, 3, 4};
    return new ArrayList<Integer>(Arrays.asList(integ));
  }

  private static void loadDefaultOptions() {
    if (MySeriesLogger.logger != null) {
      MySeriesLogger.logger.log(Level.INFO, "Loading default options");
    }
    options.put(DB_NAME, "");
    options.put(DEBUG_MODE, new Integer(0));
    //options.put(MODAL, true);
    options.put(DATE_FORMAT, "dd/MM/yyyy");
    options.put(LOOK_AND_FEEL, "EaSynth");
    options.put(SKIN_COLOR, "240,240,240");
    options.put(USE_SKIN, false);
    options.put(USE_PROXY, false);
    options.put(PROXY_HOST, "");
    options.put(PROXY_PORT, "");
    options.put(DIVIDER_LOCATION, new Integer(250));
    options.put(FONT_FACE, "Arial");
    options.put(FONT_SIZE, new Float(12F));
    options.put(TABLE_WIDTHS, getDefaultColumnWidths());
    options.put(WINDOW_STATE, JFrame.NORMAL);
    options.put(WIDTH, new Integer(1000));
    options.put(HEIGHT, new Integer(600));
    options.put(CHECK_VERSION, true);
    options.put(PRIMARY_SUB, "Greek");
    options.put(SECONDARY_SUB, "English");
    options.put(SUBTITLE_SITE, SubtitleConstants.SUBTITLE_ONLINE_NAME);
    options.put(AUTO_FILE_UPDATING, true);
    options.put(SEASON_SEPARATOR, " SE");
    options.put(TITLE_SEPARATOR, "  - ");
    options.put(EPISODE_SEPARATOR, " x");
    options.put(TOOLBAR_POSITION, new Integer(0));
    options.put(TOOLBAR_BUTTONS, getDefaultToolbarButtons());
    options.put(FEED_DIVIDER_LOCATION, 250);
    options.put(FEED_COLUMNS, 1);
    options.put(UNIFIED_SERIES, false);
    options.put(VIDEO_APP, "");
    options.put(AUTO_EXTRACT_ZIPS, false);
    options.put(UPDATE_FEEDS, false);
    options.put(TABS_ORDER, getDefaultTabsOrder());
    options.put(MAIN_DIRECTORY, getDefaultMainDirectory());
    options.put(WARN_FOR_LOG_USE, true);
    options.put(WARN_FOR_VERSION, true);
    options.put(NO_RENAME_CONFIRMATION, false);
    options.put(AUTO_RENAME_SUBS, false);
    options.put(MINIMIZE_TO_TRAY, false);
    options.put(RANDOMIZE_LAF, false);
    options.put(ACTIVE_FILTER, "");
    options.put(MEMORY_CONSUMPTION_UPDATE, 1000);
    options.put(FEED_UPDATE_FREQUENCY, 0);
    options.put(IMAGE_QUALITY, 3);
    options.put(SEARCH_FOR_SECONDARY_SUBTITLE, true);
    options.put(VIDEO_QUALITY, "");
    options.put(LATEST_NEWS_ID, 0);
    options.put(SHOW_POPUPS, true);
  }

  private static Object getDefaultMainDirectory() {
    if (MySeriesLogger.logger != null) {
      MySeriesLogger.logger.log(Level.INFO, "Getting default main directory");
    }
    File d = new File(_USER_DIR_);
    try {
      if (MySeriesLogger.logger != null) {
        MySeriesLogger.logger.log(Level.FINE, "Default main directory :{0}", d.getCanonicalPath());
      }
      return d.getCanonicalPath();
    } catch (IOException ex) {
      if (MySeriesLogger.logger != null) {
        MySeriesLogger.logger.log(Level.INFO, "Return blank");
      }
      return "";
    }
  }

  private OldOptions() {
  }
}
