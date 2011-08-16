/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.options;

import java.awt.Image;
import java.text.DecimalFormat;

/**
 *
 * @author Spyros Soldatos
 */
public interface IOldOptions {

  
  /***********************************************
   * CONSTANTS
   ***********************************************/
  
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
  //public static final String MODAL = "MODAL";
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
   * Warn the first time for version 7
   */
  public static final String WARN_FOR_VERSION = "WARN_FOR_VERSION";
  /**
   * Rename single episode with no confirmation
   */
  public static final String NO_RENAME_CONFIRMATION = "NO_RENAME_CONFIRMATION";
  /**
   * Auto rename subtitles in zips
   */
  public static final String AUTO_RENAME_SUBS = "AUTO RENAME SUBS";
  /**
   * If window should minimize to tray
   */
  public static final String MINIMIZE_TO_TRAY = "MINIMIZE_TO_TRAY";
  /**
   * Use a random look and feel
   */
  public static final String RANDOMIZE_LAF = "RANDOMIZE LAF";
  /**
   * The active episodes filter
   */
  public static final String ACTIVE_FILTER = "ACTIVE_FILTER";
  /**
   * How often memory consumption should be updated
   */
  public static final String MEMORY_CONSUMPTION_UPDATE = "MEMORY_CONSUMTION_UPDATE";
  /**
   * How often the feeds will be updated
   */
  public static final String FEED_UPDATE_FREQUENCY = "FEED_UPDATE_FREQUENCY";
  /**
   * The quality of the screenshots
   */
  public static final String IMAGE_QUALITY = "IMAGE_QUALITY";
  /**
   * When searching for subtitles and primary not found, check for secondary 
   * or not
   */
  public static final String SEARCH_FOR_SECONDARY_SUBTITLE = "SEARCH_FOR_SECONDARY_SUBTITLE";
  /**
   * The default video quality for downloading videos
   */
  public static final String VIDEO_QUALITY = "VIDEO_QUALITY";
  /**
   * The id of the latest news that the user has seen
   */
  public static final String LATEST_NEWS_ID = "LATEST_NEWS_ID";
  /**
   * Show popup messages
   */
  public static final String SHOW_POPUPS = "SHOW_POPUPS";
  /**
   * The type of iamge scaling from fastest(worst quality) to slower (better quality)
   */
  public static final int[] IMAGE_SCALING = {Image.SCALE_REPLICATE,
    Image.SCALE_FAST, Image.SCALE_AREA_AVERAGING, Image.SCALE_SMOOTH};
  /**
   * An array of the options that are selected in combo boxes
   */
  public static final String[] _COMBO_OPTIONS_ = {DATE_FORMAT, DEBUG_MODE, LOOK_AND_FEEL,
    FONT_FACE, SUBTITLE_SITE, PRIMARY_SUB, SECONDARY_SUB};
}
