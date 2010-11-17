/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.options;

import Exceptions.OptionFormatException;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.JFrame;
import myComponents.MyUsefulFunctions;
import myseries.*;
import tools.download.subtitles.Subtitle;
import tools.download.subtitles.SubtitleConstants;
import tools.internetUpdate.InternetUpdate;

/**
 *
 * @author lordovol
 */
public class Options {

  /**
   * The options map
   */
  private static Map<String, Object> options;
  /***********************************************
   * CONSTANTS
   ***********************************************/
  /**
   * The install directory
   */
  public static String _USER_DIR_;
  /**
   * The total columns in all tables : 16
   */
  public static final int _TOTAL_COLUMNS_ = 16;
  /**
   * The regex to use for finding episodes
   * " ?((?i)([ xe-]|(ep)|)?|(([ xe-]|(ep))?\\d*([- &])))0*"
   */
  public static String _REGEX_ = " ?((?i)([ xe-]|(ep)|)?|(([ xe-]|(ep))?\\d*([- &])))0*";
  /**
   * The possible toolbar positions
   */
  public static final int _NORTH_ = 0;
  public static final int _EAST_ = 1;
  public static final int _SOUTH_ = 2;
  public static final int _WEST_ = 3;
  public static final int _FLOAT_ = -1;
  /**
   * The decimal number format
   */
  public static final DecimalFormat _DEC_FORMAT_ = new DecimalFormat("#.000");
  /************************************************
   * User Options
   ************************************************/
  /**
   * Database name
   * String default ""
   */
  public static String DB_NAME = "DB_NAME";
  /**
   * Debug mode
   * int default:0
   */
  public static String DEBUG_MODE = "DEBUG_MODE";
  /**
   * If modal windows are used
   * boolean default true
   */
  public static String MODAL = "MODAL";
  /**
   * Date format
   * String default "dd/mm/YYYY"
   */
  public static String DATE_FORMAT = "DATE_FORMAT";
  /**
   * Look and feel to use
   * Not used yet!!!
   */
  public static String LOOK_AND_FEEL = "LOOK_AND_FEEL";
  /**
   * Skin color to use
   * Color default [240,240,240]
   */
  public static String SKIN_COLOR = "BACKGROUND_COLOR";
  /**
   * Use skin color or not
   * boolean default true;
   */
  public static String USE_SKIN = "USE_SKIN";
  /**
   * Use proxy to connect to internet or not
   * boolean default : false
   */
  public static String USE_PROXY = "USE_PROXY";
  /**
   * Proxy to use
   * string default ""
   */
  public static String PROXY_HOST = "PROXY_HOST";
  /**
   * Proxy port
   * String default ""
   */
  public static String PROXY_PORT = "PROXY_PORT";
  /**
   * The split pane divider's location
   * integer default 250
   */
  public static String DIVIDER_LOCATION = "DIVIDER_LOCATION";
  /**
   * The feeds split pane divider's location
   * integer default 250
   */
  public static String FEED_DIVIDER_LOCATION = "FEED_DIVIDER_LOCATION";
  /**
   * the font to use
   * String default Arial
   */
  public static String FONT_FACE = "FONT_FACE";
  /**
   * The font's size
   * integer default 12
   */
  public static String FONT_SIZE = "FONT_SIZE";
  /**
   * The table columns widths
   * ArrayList<integer> default 100
   */
  public static String TABLE_WIDTHS = "TABLE_WIDTHS";
  /**
   * The frame's state
   * integer default JFrame.NORMAL
   */
  public static String WINDOW_STATE = "WINDOW_STATE";
  /**
   * The frame's width
   * integer default 1000
   */
  public static String WIDTH = "WIDTH";
  /**
   * The frame's height
   * integer default 600
   */
  public static String HEIGHT = "HEIGHT";
  /**
   * Check for updates on start up
   * boolean default true
   */
  public static String CHECK_VERSION = "CHECK_VERSION";
  /**
   * the primary subtitles language
   * String default "Greek"
   */
  public static String PRIMARY_SUB = "PRIMARY_SUB";
  /**
   * the secondary subtitles language
   * String default "English"
   */
  public static String SECONDARY_SUB = "SECONDARY_SUB";
  /**
   * The web page used for downloading subtitles
   * String default Options._SUBTITLE_ONLINE_NAME_
   */
  public static String SUBTITLE_SITE = "SUBTITLE_SITE";
  /**
   * Auto updating video and subtitles from series local folder
   * boolean default false;
   */
  public static String AUTO_FILE_UPDATING = "AUTO_FILE_UPDATING";
  /**
   * If show series stats as unified series
   * boolean default true;
   */
  public static String UNIFIED_SERIES = "UNIFIED_SERIES";
  /**
   * The season separator when renaming episodes
   * String default "SE"
   */
  public static String SEASON_SEPARATOR = "SEASON_SEPARATOR";
  /**
   * The Episode separator when renaming episodes
   * String default "x"
   */
  public static String EPISODE_SEPARATOR = "EPISODE_SEPARATOR";
  /**
   * The title separator when renaming episodes
   * String default " - "
   */
  public static String TITLE_SEPARATOR = "TITLE_SEPARATOR";
  /**
   * The position of the toolbar
   */
  public static String TOOLBAR_POSITION = "TOOLBAR_POSITION";
  /**
   * The toolbar buttons
   */
  public static String TOOLBAR_BUTTONS = "TOOLBAR_BUTTONS";
  /**
   * The number of feed columns
   */
  public static String FEED_COLUMNS = "FEED_COLUMNS";
  /**
   * The application used to open video files
   */
  public static String VIDEO_APP = "VIDEO_APP";
  /**
   * An array of the options that are selected in combo boxes
   */
  public static String[] _COMBO_OPTIONS_ = {DATE_FORMAT, DEBUG_MODE, LOOK_AND_FEEL,
    FONT_FACE, SUBTITLE_SITE, PRIMARY_SUB, SECONDARY_SUB};

  static {
    InternetUpdate.DB_UPDATERS.add(InternetUpdate.EP_GUIDES_NAME);
    InternetUpdate.DB_UPDATERS.add(InternetUpdate.TV_RAGE_NAME);
    SubtitleConstants.SUBTITLE_SITES.add(SubtitleConstants.TV_SUBTITLES_NAME);
    SubtitleConstants.SUBTITLE_SITES.add(SubtitleConstants.SUBTITLE_ONLINE_NAME);
  }

  /**
   * Loads the options from MySeries.ini or writes a default if <br />
   * it does not exist
   * @throws java.io.FileNotFoundException
   * @throws java.io.IOException
   */
  public static void getOptions() throws FileNotFoundException, IOException {
    //Options._USER_DIR_ = System.getProperties().getProperty("user.dir");
    Options._USER_DIR_ = "./";
    options = new HashMap<String, Object>();
    Options.loadDefaultOptions();
    if (!new File(Options._USER_DIR_ + "MySeries.ini").isFile()) {
      Options.save();
    }
    BufferedReader in = MyUsefulFunctions.createInputStream(new File(Options._USER_DIR_ + "MySeries.ini"));
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
    String w = Options.toString(key).replaceAll("\\[", "").replaceAll("\\]", "");
    if (w.equals("")) {
      return null;
    }
    String[] arr = w.split(",");
    Integer[] intArr = new Integer[arr.length];
    for (int i = 0; i < arr.length; i++) {
      try {
        intArr[i] = Integer.parseInt(arr[i].trim());
      } catch (NumberFormatException ex) {
        intArr[i] = -1;
      }
    }
    return intArr;
  }

  public static ArrayList<Integer> toIntegerArrayList(String TABLE_WIDTHS) {
    ArrayList<Integer> widths = new ArrayList<Integer>();
    if (Options.toString(Options.TABLE_WIDTHS) == null) {
      try {
        throw new OptionFormatException("Table widths is null");
      } catch (OptionFormatException ex) {
        MySeries.logger.log(Level.WARNING, ex.getMessage(), ex);
        return getDefaultColumnWidths();
      }
    }
    String w = Options.toString(Options.TABLE_WIDTHS).replaceAll("\\[", "").replaceAll("\\]", "");
    String[] arr = w.split(",");
    try {
      for (int i = 0; i < Options._TOTAL_COLUMNS_; i++) {
        try {
          widths.add(i, Integer.parseInt(arr[i].trim()));
        } catch (ArrayIndexOutOfBoundsException ex) {
          widths.add(i, 100);
        }
      }
      return widths;
    } catch (NumberFormatException ex) {
      try {
        throw new OptionFormatException("Table width is not an integer");
      } catch (OptionFormatException ex1) {
        MySeries.logger.log(Level.WARNING, ex.getMessage(), ex);
        return getDefaultColumnWidths();
      }
    }
  }

  private static ArrayList<Integer> getDefaultColumnWidths() {
    ArrayList<Integer> widths = new ArrayList<Integer>();
    for (int i = 0; i < Options._TOTAL_COLUMNS_; i++) {
      widths.add(i, 100);
    }
    return widths;
  }

  /**
   * Get an Option as Color
   * @param value
   * @return
   */
  public static Color toColor(String value) {
    Color c = new Color(240, 240, 240);
    String strColor = Options.toString(value.trim());
    String colors[] = strColor.split(",", -1);
    if (colors.length != 3) {
      try {
        throw new OptionFormatException("Wrong color format for " + value + "- R, G and B not present : '" + strColor + "'");
      } catch (OptionFormatException ex) {
        MySeries.logger.log(Level.WARNING, ex.getMessage(), ex);
        Options.setOption(Options.SKIN_COLOR, "240,240,240");
        Options.save();
        return c;
      }
    }
    try {
      c = new Color(Integer.parseInt(colors[0].trim()), Integer.parseInt(colors[1].trim()), Integer.parseInt(colors[2].trim()));
    } catch (NumberFormatException ex) {
      try {
        throw new OptionFormatException("Wrong color format for " + value + "  - R,G or B not an integer: '" + strColor + "'");
      } catch (OptionFormatException ex1) {
        MySeries.logger.log(Level.WARNING, ex1.getMessage(), ex1);
        Options.setOption(Options.SKIN_COLOR, "240,240,240");
        Options.save();
        return c;
      }
    }
    return c;
  }

  /**
   * Get an option of an integer type
   * @param key The option to get
   * @return
   */
  public static int toInt(String key) {
    int val = 0;
    String s;
    try {
      s = String.valueOf(options.get(key)).trim();
      if (s != null) {
        val = Integer.parseInt(s);
      } else {
        val = 0;
      }
    } catch (NumberFormatException ex) {
      try {
        throw new OptionFormatException("value " + String.valueOf(options.get(key)).trim() + " of " + key + " is not an integer, setting it to 0");
      } catch (OptionFormatException ex1) {
        MySeries.logger.log(Level.WARNING, ex1.getMessage(), ex1);
        Options.setOption(key, 0);
        Options.save();
        return 0;
      }
    }
    return val;
  }

  /**
   * Get an option of a float type
   * @param key The option to get
   * @return
   */
  public static float toFloat(String key) {
    float val = 0.0F;
    String s;
    try {
      s = String.valueOf(options.get(key)).trim();
      if (s != null) {
        val = Float.parseFloat(s);
      } else {
        val = 0;
      }
    } catch (NumberFormatException ex) {
      try {
        throw new OptionFormatException("value " + String.valueOf(options.get(key)).trim() + " of " + key + " is not a float, setting it to 0.0");
      } catch (OptionFormatException ex1) {
        MySeries.logger.log(Level.WARNING, ex1.getMessage(), ex1);
        Options.setOption(key, 0.0F);
        Options.save();
        return 0.0F;
      }
    }
    return val;
  }

  /**
   * Get an option of a boolean type
   * @param key The option to get
   * @return
   */
  public static Boolean toBoolean(String key) {
    Boolean val = false;
    String value = String.valueOf(options.get(key)).trim();
    if (value.trim().equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
      val = Boolean.parseBoolean(String.valueOf(options.get(key)));
    } else {
      try {
        throw new OptionFormatException(key + " is not a boolean, setting it to false '" + value + "'");
      } catch (OptionFormatException ex1) {
        MySeries.logger.log(Level.WARNING, ex1.getMessage(), ex1);
        Options.setOption(key, "false");
        Options.save();
        return false;
      }
    }
    return val;
  }

  /**
   * Get an option of a string type
   * @param key The option to get
   * @return
   */
  public static String toString(String key) {
    return toString(key, true);
  }

  /**
   * Get an option of a string type
   * @param key The option to get
   * @param trim Trim value or not
   * @return
   */
  public static String toString(String key, boolean trim) {
    String val = trim ? String.valueOf(options.get(key)).trim() : String.valueOf(options.get(key));
    return val != null && !val.equals("null") ? val : "";
  }

  /**
   * Sets an option
   * @param key The option to set
   * @param value The value to set
   */
  @SuppressWarnings("unchecked")
  public static void setOption(String key, Object value) {
    try {
      options.put(key, value);
    } catch (NullPointerException ex) {
      MySeries.logger.log(Level.WARNING, "Null pointer exception", ex);
    }

  }

  /**
   * Writes the default ini file
   * @throws java.io.IOException
   */
  private static void writeDefaultIniFile() throws IOException {
    PrintWriter out = MyUsefulFunctions.createOutputStream(new File(Options._USER_DIR_ + "MySeries.ini"), false);
    out.println(Options.DB_NAME + "=");
    out.println(Options.DEBUG_MODE + "=0");
    out.println(Options.MODAL + "=true");
    out.println(Options.DATE_FORMAT + "=dd/MM/yyyy");
    out.println(Options.LOOK_AND_FEEL + "=");
    out.println(Options.SKIN_COLOR + " =240,240,240");
    out.println(Options.USE_SKIN + " =true");
    out.println(Options.USE_PROXY + " =false");
    out.println(Options.UNIFIED_SERIES + " =true");
    out.println(Options.PROXY_HOST + " =");
    out.println(Options.PROXY_PORT + " =");
    out.println(Options.DIVIDER_LOCATION + " =250");
    out.println(Options.FEED_DIVIDER_LOCATION + " =250");
    out.println(Options.FONT_FACE + " =Arial");
    out.println(Options.FONT_SIZE + " =12");
    out.println(Options.TABLE_WIDTHS + " =" + getDefaultColumnWidths());
    out.println(Options.WINDOW_STATE + " =" + JFrame.NORMAL);
    out.println(Options.WIDTH + " =1000");
    out.println(Options.HEIGHT + " =600");
    out.println(Options.CHECK_VERSION + " =true");
    out.println(Options.PRIMARY_SUB + " =Greek");
    out.println(Options.SECONDARY_SUB + " =English");
    out.println(Options.SUBTITLE_SITE + " =" + SubtitleConstants.SUBTITLE_ONLINE_NAME);
    out.println(Options.AUTO_FILE_UPDATING + " =false");
    out.println(Options.SEASON_SEPARATOR + " =SE");
    out.println(Options.TITLE_SEPARATOR + " = - ");
    out.println(Options.EPISODE_SEPARATOR + " =x");
    out.println(Options.TOOLBAR_POSITION + " =1");
    out.println(Options.TOOLBAR_BUTTONS + "=" + getDefaultToolbarButtons());
    out.println(Options.FEED_COLUMNS + "=" + 1);
    out.println(Options.VIDEO_APP + "=");

    out.close();
  }

  /**
   * Saves the options file
   */
  public static void save() {
    PrintWriter out = null;
    ArrayList<String> arr;

    try {
      out = MyUsefulFunctions.createOutputStream(new File(Options._USER_DIR_ + "MySeries.ini"), false);
      Iterator<String> it = options.keySet().iterator();
      while (it.hasNext()) {
        String key = String.valueOf(it.next());
        String value = "";
        if (options.get(key) instanceof Object[]) {
          Object[] obj = (Object[]) options.get(key);
          value = Arrays.asList(obj).toString();
        } else {
          value = String.valueOf(options.get(key));
        }
        // Check DB extension
        if (key.equals(Options.DB_NAME)) {
          if (!value.endsWith(".db")) {
            value = value + ".db";
          }
        }
        out.println(key + "=" + value);
      }
      out.close();
    } catch (IOException ex) {
      MySeries.logger.log(Level.SEVERE, "Cannot write the ini file", ex);
    } finally {
      out.close();
    }
  }

  public static Integer[] getDefaultToolbarButtons() {
    return new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
  }

  private static void loadDefaultOptions() {
    options.put(Options.DB_NAME, "");
    options.put(Options.DEBUG_MODE, new Integer(0));
    options.put(Options.MODAL, true);
    options.put(Options.DATE_FORMAT, "dd/MM/yyyy");
    options.put(Options.LOOK_AND_FEEL, "");
    options.put(Options.SKIN_COLOR, "240,240,240");
    options.put(Options.USE_SKIN, true);
    options.put(Options.USE_PROXY, false);
    options.put(Options.PROXY_HOST, "");
    options.put(Options.PROXY_PORT, "");
    options.put(Options.DIVIDER_LOCATION, new Integer(250));
    options.put(Options.FONT_FACE, "Arial");
    options.put(Options.FONT_SIZE, new Float(12F));
    options.put(Options.TABLE_WIDTHS, getDefaultColumnWidths());
    options.put(Options.WINDOW_STATE, JFrame.NORMAL);
    options.put(Options.WIDTH, new Integer(1000));
    options.put(Options.HEIGHT, new Integer(600));
    options.put(Options.CHECK_VERSION, true);
    options.put(Options.PRIMARY_SUB, "Greek");
    options.put(Options.SECONDARY_SUB, "English");
    options.put(Options.SUBTITLE_SITE, SubtitleConstants.SUBTITLE_ONLINE_NAME);
    options.put(Options.AUTO_FILE_UPDATING, true);
    options.put(Options.SEASON_SEPARATOR, " SE");
    options.put(Options.TITLE_SEPARATOR, "  - ");
    options.put(Options.EPISODE_SEPARATOR, " x");
    options.put(Options.TOOLBAR_POSITION, new Integer(0));
    options.put(Options.TOOLBAR_BUTTONS, getDefaultToolbarButtons());
    options.put(Options.FEED_DIVIDER_LOCATION, 250);
    options.put(Options.FEED_COLUMNS, 1);
    options.put(Options.UNIFIED_SERIES, false);
    options.put(Options.VIDEO_APP, "");
  }

  private Options() {
  }
}
