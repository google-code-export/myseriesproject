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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JFrame;
import myComponents.MyUsefulFunctions;
import myseries.*;

/**
 *
 * @author lordovol
 */
public class Options {

  /**
   * The install directory
   */
  public static String _USER_DIR_;
  /**
   * The databases path with trailing /
   */
  public static final String _DB_PATH_ = "databases/";
  /**
   * MySQL date format
   */
  public static final String _MYSQL_DATE_FORMAT_ = "yyyy-MM-dd";
  /**
   * The total columns in all tables
   */
  public static final int _TOTAL_COLUMNS_ = 16;
  /**
   * The regex to use for finding episodes
   */
  public static String _REGEX_ = "(([ xeEX]|(ep)|(EP))|(([ xeEX]|(ep)|(EP))\\d*([- &])))0*";

   /**
   * The options map
   */
  private static Map<String,Object> options;

  
  public static String SEASON_SEPARATOR = " - ";
  public static String TITLE_SEPARATOR = " - ";
  public static String EPISODE_SEPARATOR = "x";

  /**
   * The screenshots path
   */
  public static final String _SCREENSHOTS_PATH_ = "images/";

  public static String TV_RAGE = "TvRage";
  public static String EP_GUIDES = "EpGuides";
  public static String GREEK = "Greek";
  public static String ENGLISH = "English";

  public static Vector<String> DB_UPDATERS = new Vector<String>();
  public static Vector<String> SUBTITLES = new Vector<String>();
  public static String[] QUALITIES = {"All","HDTV","720p","1080i","1080p","DSRip","DVBRip","DVDR","DVDRip","DVDScr","HR.HDTV","HR.PDTV","PDTV","SatRip","SVCD","TVRip","WebRip"};

  public static String NEXT_EPISODES_LIMIT = "NEXT_EPISODES_LIMIT";
  public static String SHOW_UNSEEN = "SHOW_UNSEEN";
  public static String SHOW_UNDOWNLOADED = "SHOW_UNDOWNLOADED";
  public static String DB_NAME = "DB_NAME";
  public static String DEBUG_MODE = "DEBUG_MODE";
  public static String MODAL = "MODAL";
  public static String DATE_FORMAT = "DATE_FORMAT";
  public static String LOOK_AND_FEEL = "LOOK_AND_FEEL";
  public static String SKIN_COLOR = "BACKGROUND_COLOR";
  public static String USE_SKIN = "USE_SKIN";
  public static String USE_PROXY = "USE_PROXY";
  public static String PROXY_HOST = "PROXY_HOST";
  public static String PROXY_PORT = "PROXY_PORT";
  public static String DIVIDER_LOCATION = "DIVIDER_LOCATION";
  public static String FONT_FACE = "FONT_FACE";
  public static String FONT_SIZE = "FONT_SIZE";
  public static String TABLE_WIDTHS = "TABLE_WIDTHS";
  public static String WINDOW_STATE = "WINDOW_STATE";
  public static String WIDTH = "WIDTH";
  public static String HEIGHT = "HEIGHT";
  public static String INTERNET_UPDATE_DB = "INTERNET_UPDATE_DB";
  public static String CHECK_VERSION = "CHECK_VERSION";
  public static String PRIMARY_SUB = "PRIMARY_SUB";


  static {
    DB_UPDATERS.add(Options.EP_GUIDES);
    DB_UPDATERS.add(Options.TV_RAGE);
    SUBTITLES.add(Options.GREEK);
    SUBTITLES.add(Options.ENGLISH);
  }

  /**
   * Loads the options from MySeries.ini or writes a default if <br />
   * it does not exist
   * @throws java.io.FileNotFoundException
   * @throws java.io.IOException
   */
  public static void getOptions() throws FileNotFoundException, IOException {
    Options._USER_DIR_ = System.getProperties().getProperty("user.dir");
    System.out.println(_USER_DIR_);
    options = new HashMap<String,Object>();
    if (!new File(Options._USER_DIR_ + "/MySeries.ini").isFile()) {
      writeDefaultIniFile();
    }
    BufferedReader in = MyUsefulFunctions.createInputStream(new File(Options._USER_DIR_ + "/MySeries.ini"));
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
          value = String.valueOf(fields[1].trim());
        }
        options.put(fields[0].trim(), value);
      }
    }
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
        widths.add(i,Integer.parseInt(arr[i].trim()));
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
    ArrayList<Integer>  widths = new ArrayList<Integer>();
    for (int i = 0; i < Options._TOTAL_COLUMNS_; i++) {
      widths.add(i,100);
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

    try {
      String s = String.valueOf(options.get(key)).trim();
      if (s != null) {
        val = Integer.parseInt(s);
      } else {
        val = 0;
      }
    } catch (NumberFormatException ex) {
      try {
        throw new OptionFormatException(key + " is not an integer, setting it to 0");
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
    String val = "";

    val = String.valueOf(options.get(key)).trim();
    return val;
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
    PrintWriter out = MyUsefulFunctions.createOutputStream(new File(Options._USER_DIR_ + "/MySeries.ini"), false);
    out.println(Options.NEXT_EPISODES_LIMIT + "= 10");
    out.println(Options.SHOW_UNDOWNLOADED + "= false");
    out.println(Options.SHOW_UNSEEN + "= false");
    out.println(Options.DB_NAME + "=");
    out.println(Options.DEBUG_MODE + "=0");
    out.println(Options.MODAL + "= true");
    out.println(Options.DATE_FORMAT + "= dd/MM/yyyy");
    out.println(Options.LOOK_AND_FEEL + "=");
    out.println(Options.SKIN_COLOR + " = 240,240,240");
    out.println(Options.USE_SKIN + " = true");
    out.println(Options.USE_PROXY + " = false");
    out.println(Options.PROXY_HOST + " = ");
    out.println(Options.PROXY_PORT + " = ");
    out.println(Options.DIVIDER_LOCATION + " = 250");
    out.println(Options.FONT_FACE + " = Arial");
    out.println(Options.FONT_SIZE + " = 12");
    out.println(Options.TABLE_WIDTHS + " = " + getDefaultColumnWidths());
    out.println(Options.WINDOW_STATE + " = " + JFrame.NORMAL);
    out.println(Options.WIDTH + " = 1000");
    out.println(Options.HEIGHT + " = 600");
    out.println(Options.INTERNET_UPDATE_DB + "= TvRage");
    out.println(Options.CHECK_VERSION + " = true");
    out.println(Options.PRIMARY_SUB + " = gr");
    out.close();
  }

  /**
   * Saves the options file
   */
  public static void save() {
    PrintWriter out = null;
    ArrayList<String> arr;
    try {
      out = MyUsefulFunctions.createOutputStream(new File(Options._USER_DIR_ + "/MySeries.ini"), false);
      Iterator<String> it = options.keySet().iterator();
      while (it.hasNext()) {
        String key = String.valueOf(it.next());
        String value = String.valueOf(options.get(key));
        // Check DB extension
        if (key.equals(Options.DB_NAME)) {
          if (!value.endsWith(".db")) {
            value = value + ".db";
          }
        }
        out.println(key + " = " + value);
      }
      out.close();
    } catch (IOException ex) {
      MySeries.logger.log(Level.SEVERE, "Cannot write the ini file", ex);
    } finally {
      out.close();
    }
  }

  private Options() {
  }
}
