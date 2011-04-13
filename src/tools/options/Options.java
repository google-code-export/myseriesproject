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
import tools.MySeriesLogger;
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
    public static final String _REGEX_ = " ?((?i)([ xe-]|(ep)|)?|(([ xe-]|(ep))?\\d*([- &])))0*";
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
    public static final String DB_NAME = "DB_NAME";
    /**
     * Debug mode
     * int default:0
     */
    public static final String DEBUG_MODE = "DEBUG_MODE";
    /**
     * If modal windows are used
     * boolean default true
     */
    public static final String MODAL = "MODAL";
    /**
     * Date format
     * String default "dd/mm/YYYY"
     */
    public static final String DATE_FORMAT = "DATE_FORMAT";
    /**
     * Look and feel to use
     * Not used yet!!!
     */
    public static final String LOOK_AND_FEEL = "LOOK_AND_FEEL";
    /**
     * Skin color to use
     * Color default [240,240,240]
     */
    public static final String SKIN_COLOR = "BACKGROUND_COLOR";
    /**
     * Use skin color or not
     * boolean default true;
     */
    public static final String USE_SKIN = "USE_SKIN";
    /**
     * Use proxy to connect to internet or not
     * boolean default : false
     */
    public static final String USE_PROXY = "USE_PROXY";
    /**
     * Proxy to use
     * string default ""
     */
    public static final String PROXY_HOST = "PROXY_HOST";
    /**
     * Proxy port
     * String default ""
     */
    public static final String PROXY_PORT = "PROXY_PORT";
    /**
     * The split pane divider's location
     * integer default 250
     */
    public static final String DIVIDER_LOCATION = "DIVIDER_LOCATION";
    /**
     * The feeds split pane divider's location
     * integer default 250
     */
    public static final String FEED_DIVIDER_LOCATION = "FEED_DIVIDER_LOCATION";
    /**
     * the font to use
     * String default Arial
     */
    public static final String FONT_FACE = "FONT_FACE";
    /**
     * The font's size
     * integer default 12
     */
    public static final String FONT_SIZE = "FONT_SIZE";
    /**
     * The table columns widths
     * ArrayList<integer> default 100
     */
    public static final String TABLE_WIDTHS = "TABLE_WIDTHS";
    /**
     * The frame's state
     * integer default JFrame.NORMAL
     */
    public static final String WINDOW_STATE = "WINDOW_STATE";
    /**
     * The frame's width
     * integer default 1000
     */
    public static final String WIDTH = "WIDTH";
    /**
     * The frame's height
     * integer default 600
     */
    public static final String HEIGHT = "HEIGHT";
    /**
     * Check for updates on start up
     * boolean default true
     */
    public static final String CHECK_VERSION = "CHECK_VERSION";
    /**
     * the primary subtitles language
     * String default "Greek"
     */
    public static final String PRIMARY_SUB = "PRIMARY_SUB";
    /**
     * the secondary subtitles language
     * String default "English"
     */
    public static final String SECONDARY_SUB = "SECONDARY_SUB";
    /**
     * The web page used for downloading subtitles
     * String default Options._SUBTITLE_ONLINE_NAME_
     */
    public static final String SUBTITLE_SITE = "SUBTITLE_SITE";
    /**
     * Auto updating video and subtitles from series local folder
     * boolean default false;
     */
    public static final String AUTO_FILE_UPDATING = "AUTO_FILE_UPDATING";
    /**
     * If show series stats as unified series
     * boolean default true;
     */
    public static final String UNIFIED_SERIES = "UNIFIED_SERIES";
    /**
     * The season separator when renaming episodes
     * String default "SE"
     */
    public static final String SEASON_SEPARATOR = "SEASON_SEPARATOR";
    /**
     * The Episode separator when renaming episodes
     * String default "x"
     */
    public static final String EPISODE_SEPARATOR = "EPISODE_SEPARATOR";
    /**
     * The title separator when renaming episodes
     * String default " - "
     */
    public static final String TITLE_SEPARATOR = "TITLE_SEPARATOR";
    /**
     * The position of the toolbar
     */
    public static final String TOOLBAR_POSITION = "TOOLBAR_POSITION";
    /**
     * The toolbar buttons
     */
    public static final String TOOLBAR_BUTTONS = "TOOLBAR_BUTTONS";
    /**
     * The number of feed columns
     */
    public static final String FEED_COLUMNS = "FEED_COLUMNS";
    /**
     * The application used to open video files
     */
    public static final String VIDEO_APP = "VIDEO_APP";
    /**
     * Auto extract zip files
     */
    public static final String AUTO_EXTRACT_ZIPS = "AUTO_EXTRACT_ZIPS";
    /**
     * Update feeds on startup
     */
    public static final String UPDATE_FEEDS = "UPDATE_FEEDS";
    /**
     * The order of the tabs
     */
    public static final String TABS_ORDER = "TABS_ORDER";
    /**
     * The main series directory
     */
    public static final String MAIN_DIRECTORY = "MAIN_DIRECTORY";
    /**
     * Warn the first time LOG is in INFO Level
     */
    public static final String WARN_FOR_LOG_USE = "WARN_FOR_LOG_USE";
    /**
     * An array of the options that are selected in combo boxes
     */
    public static final String[] _COMBO_OPTIONS_ = {DATE_FORMAT, DEBUG_MODE, LOOK_AND_FEEL,
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
            if (MySeriesLogger.logger != null) {
                MySeriesLogger.logger.log(Level.INFO, "No options file found, creating file with default options");
            }
            Options.save();
        }
        File opFile = new File(Options._USER_DIR_ + "MySeries.ini");
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
        String w = Options.toString(key).replaceAll("\\[", "").replaceAll("\\]", "");
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
        if (Options.toString(Options.TABLE_WIDTHS) == null) {
            try {
                throw new OptionFormatException("Table widths is null");
            } catch (OptionFormatException ex) {
                if (MySeriesLogger.logger != null) {
                    MySeriesLogger.logger.log(Level.WARNING, ex.getMessage(), ex);
                }
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
        for (int i = 0; i < Options._TOTAL_COLUMNS_; i++) {
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
    public static Color toColor(String value) {
        MySeriesLogger.logger.log(Level.INFO, "Getting Color option for key {0}", value);
        Color c = new Color(240, 240, 240);
        String strColor = Options.toString(value.trim());
        String colors[] = strColor.split(",", -1);
        if (colors.length != 3) {
            try {
                throw new OptionFormatException("Wrong color format for " + value + "- R, G and B not present : '" + strColor + "'");
            } catch (OptionFormatException ex) {
                if (MySeriesLogger.logger != null) {
                    MySeriesLogger.logger.log(Level.WARNING, ex.getMessage(), ex);
                }
                Options.setOption(Options.SKIN_COLOR, "240,240,240");
                Options.save();
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
                Options.setOption(Options.SKIN_COLOR, "240,240,240");
                Options.save();
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
    public static int toInt(String key) {
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
                Options.setOption(key, 0);
                Options.save();
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
    public static float toFloat(String key) {
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
                Options.setOption(key, 0.0F);
                Options.save();
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
    public static Boolean toBoolean(String key) {
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
                Options.setOption(key, "false");
                Options.save();
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
        out.println(Options.AUTO_EXTRACT_ZIPS + "=false");
        out.println(Options.UPDATE_FEEDS + "=false");
        out.println(Options.TABS_ORDER + "=" + getDefaultTabsOrder());
        out.println(Options.MAIN_DIRECTORY + "=" + getDefaultMainDirectory());
        out.println(Options.WARN_FOR_LOG_USE + "=true");

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

    public static Integer[] getDefaultToolbarButtons() {
        return new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
    }

    private static Integer[] getDefaultTabsOrder() {
        return new Integer[]{0, 1, 2, 3, 4};
    }

    private static void loadDefaultOptions() {
        if (MySeriesLogger.logger != null) {
            MySeriesLogger.logger.log(Level.INFO, "Loading default options");
        }
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
        options.put(Options.AUTO_EXTRACT_ZIPS, false);
        options.put(Options.UPDATE_FEEDS, false);
        options.put(Options.TABS_ORDER, getDefaultTabsOrder());
        options.put(Options.MAIN_DIRECTORY, getDefaultMainDirectory());
        options.put(Options.WARN_FOR_LOG_USE, true);
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

    private Options() {
    }
}
