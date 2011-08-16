/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.options;

import com.googlecode.soptions.IOption;
import com.googlecode.soptions.Option;
import com.googlecode.soptions.SOptions;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import tools.MySeriesLogger;
import tools.download.subtitles.SubtitleConstants;

/**
 *
 * @author Spyros Soldatos
 */
public class MySeriesOptions extends SOptions implements IMySeriesOptions {

  /**
   * The install directory
   */
  public static String _USER_DIR_ = "./";
  public static String FILENAME = "MySeriesOptions.xml";

  public MySeriesOptions() {
    super(_USER_DIR_ + FILENAME);
  }

  @Override
  protected Option[] createDefaultOptionsArray() {
    return new Option[]{
          new Option(ACTIVE_FILTER, STRING_CLASS, ""),
          new Option(AUTO_EXTRACT_ZIPS, BOOLEAN_CLASS, false),
          new Option(AUTO_FILE_UPDATING, BOOLEAN_CLASS, false),
          new Option(AUTO_RENAME_SUBS, BOOLEAN_CLASS, false),
          new Option(CHECK_VERSION, BOOLEAN_CLASS, true),
          new Option(DATE_FORMAT, STRING_CLASS, "dd/MM/yyyy"),
          new Option(DB_NAME, STRING_CLASS, ""),
          new Option(DEBUG_MODE, STRING_CLASS, "OFF"),
          new Option(DIVIDER_LOCATION, INTEGER_CLASS, 250),
          new Option(EPISODE_SEPARATOR, STRING_CLASS, "x"),
          new Option(FEED_COLUMNS, INTEGER_CLASS, 1),
          new Option(FEED_DIVIDER_LOCATION, INTEGER_CLASS, 250),
          new Option(FEED_UPDATE_FREQUENCY, INTEGER_CLASS, 0),
          new Option(FONT_FACE, STRING_CLASS, "Arial"),
          new Option(FONT_SIZE, INTEGER_CLASS, 12),
          new Option(HEIGHT, INTEGER_CLASS, 600),
          new Option(IMAGE_QUALITY, INTEGER_CLASS, Image.SCALE_DEFAULT),
          new Option(LATEST_NEWS_ID, INTEGER_CLASS, 0),
          new Option(LOOK_AND_FEEL, STRING_CLASS, "EaSynth"),
          new Option(MAIN_DIRECTORY, STRING_CLASS, getDefaultMainDirectory()),
          new Option(MEMORY_CONSUMPTION_UPDATE, INTEGER_CLASS, 1000),
          new Option(MINIMIZE_TO_TRAY, BOOLEAN_CLASS, false),
          new Option(NO_RENAME_CONFIRMATION, BOOLEAN_CLASS, false),
          new Option(PRIMARY_SUB, STRING_CLASS, "Greek"),
          new Option(PROXY_HOST, STRING_CLASS, ""),
          new Option(PROXY_PORT, STRING_CLASS, ""),
          new Option(RANDOMIZE_LAF, BOOLEAN_CLASS, false),
          new Option(SEARCH_FOR_SECONDARY_SUBTITLE, BOOLEAN_CLASS, true),
          new Option(SEASON_SEPARATOR, STRING_CLASS, "SE"),
          new Option(SECONDARY_SUB, STRING_CLASS, "English"),
          new Option(SHOW_POPUPS, BOOLEAN_CLASS, true),
          new Option(SHOW_TOOLBAR, BOOLEAN_CLASS, true),
          new Option(SKIN_COLOR, COLOR_CLASS, Color.GRAY),
          new Option(SUBTITLE_SITE, STRING_CLASS, SubtitleConstants.SUBTITLE_ONLINE_NAME),
          new Option(TABLE_WIDTHS, ARRAY_CLASS, getDefaultColumnWidths()),
          new Option(TABS_ORDER, ARRAY_CLASS, getDefaultTabsOrder()),
          new Option(TITLE_SEPARATOR, STRING_CLASS, " - "),
          new Option(TOOLBAR_BUTTONS, ARRAY_CLASS, getDefaultToolbarButtons()),
          new Option(TOOLBAR_POSITION, INTEGER_CLASS, JToolBar.HORIZONTAL),
          new Option(UNIFIED_SERIES, BOOLEAN_CLASS, true),
          new Option(UPDATE_FEEDS, BOOLEAN_CLASS, false),
          new Option(USE_PROXY, BOOLEAN_CLASS, false),
          new Option(USE_SKIN, BOOLEAN_CLASS, false),
          new Option(VIDEO_APP, STRING_CLASS, ""),
          new Option(VIDEO_QUALITY, STRING_CLASS, ""),
          new Option(WARN_FOR_LOG_USE, BOOLEAN_CLASS, true),
          new Option(WARN_FOR_VERSION, BOOLEAN_CLASS, true),
          new Option(WIDTH, INTEGER_CLASS, 100),
          new Option(WINDOW_STATE, INTEGER_CLASS, JFrame.NORMAL)
        };
  }

  public void updateOptions() {
    setOption(new Option(DB_NAME, STRING_CLASS, OldOptions.getStringOption(IOldOptions.DB_NAME)));
    setOption(new Option(DEBUG_MODE, STRING_CLASS, OldOptions.getStringOption(IOldOptions.DEBUG_MODE)));
    setOption(new Option(DATE_FORMAT, STRING_CLASS, OldOptions.getStringOption(IOldOptions.DATE_FORMAT)));
    setOption(new Option(LOOK_AND_FEEL, STRING_CLASS, OldOptions.getStringOption(IOldOptions.LOOK_AND_FEEL)));
    setOption(new Option(SKIN_COLOR, COLOR_CLASS, OldOptions.getColorOption(IOldOptions.SKIN_COLOR)));
    setOption(new Option(USE_SKIN, BOOLEAN_CLASS, OldOptions.getBooleanOption(IOldOptions.USE_SKIN)));
    setOption(new Option(USE_PROXY, BOOLEAN_CLASS, OldOptions.getBooleanOption(IOldOptions.USE_PROXY)));
    setOption(new Option(UNIFIED_SERIES, BOOLEAN_CLASS, OldOptions.getBooleanOption(IOldOptions.UNIFIED_SERIES)));
    setOption(new Option(PROXY_HOST, STRING_CLASS, OldOptions.getStringOption(IOldOptions.PROXY_HOST)));
    setOption(new Option(PROXY_PORT, STRING_CLASS, OldOptions.getStringOption(IOldOptions.PROXY_PORT)));
    setOption(new Option(DIVIDER_LOCATION, INTEGER_CLASS, OldOptions.getIntegerOption(IOldOptions.DIVIDER_LOCATION)));
    setOption(new Option(FEED_DIVIDER_LOCATION, INTEGER_CLASS, OldOptions.getIntegerOption(IOldOptions.FEED_DIVIDER_LOCATION)));
    setOption(new Option(FONT_FACE, STRING_CLASS, OldOptions.getStringOption(IOldOptions.FONT_FACE)));
    setOption(new Option(FONT_SIZE, FLOAT_CLASS, OldOptions.getFloatOption(IOldOptions.FONT_SIZE)));
    setOption(new Option(TABLE_WIDTHS, ARRAY_CLASS, OldOptions.toIntegerArray(IOldOptions.TABLE_WIDTHS)));
    setOption(new Option(WINDOW_STATE, INTEGER_CLASS, OldOptions.getIntegerOption(IOldOptions.WINDOW_STATE)));
    setOption(new Option(WIDTH, INTEGER_CLASS, OldOptions.getIntegerOption(IOldOptions.WIDTH)));
    setOption(new Option(HEIGHT, INTEGER_CLASS, OldOptions.getIntegerOption(IOldOptions.HEIGHT)));
    setOption(new Option(CHECK_VERSION, BOOLEAN_CLASS, OldOptions.getBooleanOption(IOldOptions.CHECK_VERSION)));
    setOption(new Option(PRIMARY_SUB, STRING_CLASS, OldOptions.getStringOption(IOldOptions.PRIMARY_SUB)));
    setOption(new Option(SECONDARY_SUB, STRING_CLASS, OldOptions.getStringOption(IOldOptions.SECONDARY_SUB)));
    setOption(new Option(SUBTITLE_SITE, STRING_CLASS, OldOptions.getStringOption(IOldOptions.SUBTITLE_SITE)));
    setOption(new Option(AUTO_FILE_UPDATING, BOOLEAN_CLASS, OldOptions.getBooleanOption(IOldOptions.AUTO_FILE_UPDATING)));
    setOption(new Option(SEASON_SEPARATOR, STRING_CLASS, OldOptions.getStringOption(IOldOptions.SEASON_SEPARATOR)));
    setOption(new Option(EPISODE_SEPARATOR, STRING_CLASS, OldOptions.getStringOption(IOldOptions.EPISODE_SEPARATOR)));
    setOption(new Option(TITLE_SEPARATOR, STRING_CLASS, OldOptions.getStringOption(IOldOptions.TITLE_SEPARATOR, false)));
    setOption(new Option(TOOLBAR_POSITION, INTEGER_CLASS, OldOptions.getIntegerOption(IOldOptions.TOOLBAR_POSITION)));
    setOption(new Option(TOOLBAR_BUTTONS, ARRAY_CLASS, OldOptions.toIntegerArray(IOldOptions.TOOLBAR_BUTTONS)));
    setOption(new Option(FEED_COLUMNS, INTEGER_CLASS, OldOptions.getIntegerOption(IOldOptions.FEED_COLUMNS)));
    setOption(new Option(VIDEO_APP, STRING_CLASS, OldOptions.getStringOption(IOldOptions.VIDEO_APP)));
    setOption(new Option(AUTO_EXTRACT_ZIPS, BOOLEAN_CLASS, OldOptions.getBooleanOption(IOldOptions.AUTO_EXTRACT_ZIPS)));
    setOption(new Option(UPDATE_FEEDS, BOOLEAN_CLASS, OldOptions.getBooleanOption(IOldOptions.UPDATE_FEEDS)));
    setOption(new Option(TABS_ORDER, ARRAY_CLASS, OldOptions.toIntegerArray(IOldOptions.TABS_ORDER)));
    setOption(new Option(MAIN_DIRECTORY, STRING_CLASS, OldOptions.getStringOption(IOldOptions.MAIN_DIRECTORY)));
    setOption(new Option(WARN_FOR_LOG_USE, BOOLEAN_CLASS, OldOptions.getBooleanOption(IOldOptions.WARN_FOR_LOG_USE)));
    setOption(new Option(WARN_FOR_VERSION, BOOLEAN_CLASS, OldOptions.getBooleanOption(IOldOptions.WARN_FOR_VERSION)));
    setOption(new Option(NO_RENAME_CONFIRMATION, BOOLEAN_CLASS, OldOptions.getBooleanOption(IOldOptions.NO_RENAME_CONFIRMATION)));
    setOption(new Option(AUTO_RENAME_SUBS, BOOLEAN_CLASS, OldOptions.getBooleanOption(IOldOptions.AUTO_RENAME_SUBS)));
    setOption(new Option(MINIMIZE_TO_TRAY, BOOLEAN_CLASS, OldOptions.getBooleanOption(IOldOptions.MINIMIZE_TO_TRAY)));
    setOption(new Option(RANDOMIZE_LAF, BOOLEAN_CLASS, OldOptions.getBooleanOption(IOldOptions.RANDOMIZE_LAF)));
    setOption(new Option(ACTIVE_FILTER, STRING_CLASS, OldOptions.getStringOption(IOldOptions.VIDEO_APP)));
    setOption(new Option(MEMORY_CONSUMPTION_UPDATE, INTEGER_CLASS, OldOptions.getIntegerOption(IOldOptions.MEMORY_CONSUMPTION_UPDATE)));
    setOption(new Option(FEED_UPDATE_FREQUENCY, INTEGER_CLASS, OldOptions.getIntegerOption(IOldOptions.FEED_UPDATE_FREQUENCY)));
    setOption(new Option(IMAGE_QUALITY, INTEGER_CLASS, OldOptions.getIntegerOption(IOldOptions.IMAGE_QUALITY)));
    setOption(new Option(SEARCH_FOR_SECONDARY_SUBTITLE, BOOLEAN_CLASS, OldOptions.getBooleanOption(IOldOptions.SEARCH_FOR_SECONDARY_SUBTITLE)));
    setOption(new Option(VIDEO_QUALITY, STRING_CLASS, OldOptions.getStringOption(IOldOptions.VIDEO_QUALITY)));
    setOption(new Option(LATEST_NEWS_ID, INTEGER_CLASS, OldOptions.getIntegerOption(IOldOptions.LATEST_NEWS_ID)));
    setOption(new Option(SHOW_POPUPS, BOOLEAN_CLASS, OldOptions.getBooleanOption(IOldOptions.SHOW_POPUPS)));
    setOption(new Option(SHOW_TOOLBAR, BOOLEAN_CLASS,true));
    try {
      save();
    } catch (ParserConfigurationException ex) {
      if (MySeriesLogger.logger != null) {
        MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      }
    } catch (IOException ex) {
      if (MySeriesLogger.logger != null) {
        MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      }
    } catch (SAXException ex) {
      if (MySeriesLogger.logger != null) {
        MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      }
    }
  }

  @Override
  public boolean setOption(Option option, boolean save) {
    MySeriesLogger.logger.log(Level.INFO, "Setting option : {0}", option);
    return super.setOption(option, save);
  }

  private static Integer[] getDefaultColumnWidths() {
    if (MySeriesLogger.logger != null) {
      MySeriesLogger.logger.log(Level.INFO, "Getting default column widths");
    }
    Integer[] widths = new Integer[_TOTAL_COLUMNS_];
    for (int i = 0; i < _TOTAL_COLUMNS_; i++) {
      widths[i] = 100;
    }
    if (MySeriesLogger.logger != null) {
      MySeriesLogger.logger.log(Level.INFO, "Return table widths", widths);
    }
    return widths;
  }

  public static Integer[] getDefaultToolbarButtons() {
    return new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
          12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
  }

  private static Integer[] getDefaultTabsOrder() {
    return new Integer[]{0, 1, 2, 3, 4};
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

  @Override
  protected String getVersion() {
    return "1.0";
  }
}
