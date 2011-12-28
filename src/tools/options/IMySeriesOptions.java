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
public interface IMySeriesOptions {
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
  public static final String DB_NAME = "database";
  /**
   * Debug mode
   * int default:0
   */
  public static final String DEBUG_MODE = "debugMode";
  /**
   * If modal windows are used
   * boolean default true
   */
  //public static final String MODAL = "MODAL";
  /**
   * Date format
   * String default "dd/mm/YYYY"
   */
  public static final String DATE_FORMAT = "dateFormat";
  /**
   * Look and feel to use
   * Not used yet!!!
   */
  public static final String LOOK_AND_FEEL = "lookAndFeel";
  /**
   * Skin color to use
   * Color default [240,240,240]
   */
  public static final String SKIN_COLOR = "bgColor";
  /**
   * Use skin color or not
   * boolean default true;
   */
  public static final String USE_SKIN = "useSkin";
  /**
   * Use proxy to connect to internet or not
   * boolean default : false
   */
  public static final String USE_PROXY = "useProxy";
  /**
   * Proxy to use
   * string default ""
   */
  public static final String PROXY_HOST = "proxyHost";
  /**
   * Proxy port
   * String default ""
   */
  public static final String PROXY_PORT = "proxyPort";
  /**
   * The split pane divider's location
   * integer default 250
   */
  public static final String DIVIDER_LOCATION = "dividerLocation";
  /**
   * The feeds split pane divider's location
   * integer default 250
   */
  public static final String FEED_DIVIDER_LOCATION = "feedDividerLocation";
  /**
   * the font to use
   * String default Arial
   */
  public static final String FONT_FACE = "fontface";
  /**
   * The font's size
   * integer default 12
   */
  public static final String FONT_SIZE = "fontSize";
  /**
   * The table columns widths
   * ArrayList<integer> default 100
   */
  public static final String TABLE_WIDTHS = "tablewidths";
  /**
   * The frame's state
   * integer default JFrame.NORMAL
   */
  public static final String WINDOW_STATE = "windowState";
  /**
   * The frame's width
   * integer default 1000
   */
  public static final String WIDTH = "width";
  /**
   * The frame's height
   * integer default 600
   */
  public static final String HEIGHT = "height";
  /**
   * Check for updates on start up
   * boolean default true
   */
  public static final String CHECK_VERSION = "checkNewVersion";
  /**
   * the primary subtitles language
   * String default "Greek"
   */
  public static final String PRIMARY_SUB = "primarySubtitle";
  /**
   * the secondary subtitles language
   * String default "English"
   */
  public static final String SECONDARY_SUB = "secondarySubtitle";
  /**
   * The web page used for downloading subtitles
   * String default Options._SUBTITLE_ONLINE_NAME_
   */
  public static final String SUBTITLE_SITE = "subtitleSite";
  /**
   * Auto updating video and subtitles from series local folder
   * boolean default false;
   */
  public static final String AUTO_FILE_UPDATING = "autoFileUpdating";
  /**
   * If show series stats as unified series
   * boolean default true;
   */
  public static final String UNIFIED_SERIES = "unifiedSeries";
  /**
   * The season separator when renaming episodes
   * String default "SE"
   */
  public static final String SEASON_SEPARATOR = "seasonSeparator";
  /**
   * The Episode separator when renaming episodes
   * String default "x"
   */
  public static final String EPISODE_SEPARATOR = "episodeSeparator";
  /**
   * The title separator when renaming episodes
   * String default " - "
   */
  public static final String TITLE_SEPARATOR = "titleSeparator";
  /**
   * The position of the toolbar
   */
  public static final String TOOLBAR_POSITION = "toolbarPosition";
  /**
   * The toolbar buttons
   */
  public static final String TOOLBAR_BUTTONS = "toolbarButtons";
  /**
   * The number of feed columns
   */
  public static final String FEED_COLUMNS = "feedColumns";
  /**
   * The application used to open video files
   */
  public static final String VIDEO_APP = "videoApp";
  /**
   * Arguments to pass to the video application
   */
  public static final String VIDEO_APP_ARGS = "videoAppArgs";
  /**
   * Auto extract zip files
   */
  public static final String AUTO_EXTRACT_ZIPS = "autoExtractArchives";
  /**
   * Update feeds on startup
   */
  public static final String UPDATE_FEEDS = "updateFeeds";
  /**
   * The order of the tabs
   */
  public static final String TABS_ORDER = "tabsOrder";
  /**
   * The main series directory
   */
  public static final String MAIN_DIRECTORY = "mainDirectory";
  /**
   * Warn the first time LOG is in INFO Level
   */
  public static final String WARN_FOR_LOG_USE = "warnForLogUse";
  /**
   * Warn the first time for version 7
   */
  public static final String WARN_FOR_VERSION = "warnForJreVersion";
  /**
   * Rename single episode with no confirmation
   */
  public static final String NO_RENAME_CONFIRMATION = "noRenameConfirmation";
  /**
   * Auto rename subtitles in zips
   */
  public static final String AUTO_RENAME_SUBS = "autoRenameSubtitles";
  /**
   * If window should minimize to tray
   */
  public static final String MINIMIZE_TO_TRAY = "minimizeTotray";
  /**
   * Use a random look and feel
   */
  public static final String RANDOMIZE_LAF = "randomizeLaf";
  /**
   * The active episodes filter
   */
  public static final String ACTIVE_FILTER = "activeFilter";
  /**
   * How often memory consumption should be updated
   */
  public static final String MEMORY_CONSUMPTION_UPDATE = "memoryConsumptionFrequency";
  /**
   * How often the feeds will be updated
   */
  public static final String FEED_UPDATE_FREQUENCY = "feedUpdateFrequency";
  /**
   * The quality of the screenshots
   */
  public static final String IMAGE_QUALITY = "imageQuality";
  /**
   * When searching for subtitles and primary not found, check for secondary 
   * or not
   */
  public static final String SEARCH_FOR_SECONDARY_SUBTITLE = "searchforSecondarySubtitle";
  /**
   * The default video quality for downloading videos
   */
  public static final String VIDEO_QUALITY = "videoQuality";
  /**
   * The id of the latest news that the user has seen
   */
  public static final String LATEST_NEWS_ID = "latestNewsId";
  /**
   * Show popup messages
   */
  public static final String SHOW_POPUPS = "showPopups";
  /**
   * Show toolbar
   */
  public static final String SHOW_TOOLBAR = "showToolbar";
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
