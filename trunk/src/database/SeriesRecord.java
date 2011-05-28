/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.File;
import myComponents.MyUsefulFunctions;
import java.sql.SQLException;
import java.util.logging.Level;
import tools.MySeriesLogger;
import tools.options.Options;

/**
 * The series database record
 * @author lordovol
 */
public class SeriesRecord extends Record implements Comparable<SeriesRecord> {

  /** No internet update : 0   */
  public static final int NO_INTERNET_UPDATE = 0;
  /** Internet update : 1   */
  public static final int INTERNET_UPDATE = 1;
  /** Not hidden : 0   */
  public static final int NOT_HIDDEN = 0;
  /* Hidden : 1  */
  public static final int HIDDEN = 1;
  /** Not deleted : 0  */
  public static final int NOT_DELETED = 0;
  /** Deleted : 1 **/
  public static final int DELETED = 1;
  /**
   * The table name
   */
  String table = "series";
  /**
   * The default values for the record attributes
   */
  private int series_ID = 0;
  private String title = "";
  private int season = 0;
  private int hidden = NOT_HIDDEN;
  private String tvSubtitlesCode = "";
  private int internetUpdate = INTERNET_UPDATE;
  private int tvrage_ID = 0;
  private String localDir = "";
  private String screenshot = "";
  private String sOnlineCode = "";
  private int deleted = NOT_DELETED;

  /**
   * The default constructor
   */
  public SeriesRecord() {
    super();
  }

  /**
   * Inserts a new series record or updates an existing one
   * @return The id of the new record or -1 if it's an update
   * @throws SQLException
   */
  public int save() throws SQLException {
    String sql;
    MySeriesLogger.logger.log(Level.INFO, "Saving series : {0}",getFullTitle());
    if (this.series_ID != 0) {
      sql = "UPDATE series SET title = '" + this.title + "', season = " + this.getSeason()
          + ", hidden = " + this.getHidden() + ", link ='" + this.getTvSubtitlesCode() + "', internetUpdate  ="
          + this.getInternetUpdate() + ", tvrage_ID = " + this.getTvrage_ID()
          + ", localDir = '" + this.localDir + "', screenshot = '"
          + this.screenshot + "', sonline = '" + this.sOnlineCode
          + "', deleted = " + this.deleted
          + " WHERE series_ID = " + this.getSeries_ID();
    } else {
      sql = "INSERT INTO series (title, season, hidden, link, internetUpdate, "
          + "tvrage_ID, localDir, screenshot, sonline, deleted) "
          + "VALUES('" + this.title + "', " + this.getSeason() + ", "
          + this.getHidden() + ", '" + this.getTvSubtitlesCode() + "',"
          + this.getInternetUpdate() + ", " + this.getTvrage_ID() + ", '"
          + this.getLocalDir() + "','" + this.getScreenshot()
          + "','" + this.sOnlineCode + "'," + this.deleted + ")";
    }
    return queryUpdate(DBConnection.conn.createStatement(), sql);
  }

  /**
   * @return the series_ID
   */
  public int getSeries_ID() {
    return series_ID;
  }

  /**
   * @param series_ID the series_ID to set
   */
  public void setSeries_ID(int series_ID) {
    this.series_ID = series_ID;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return MyUsefulFunctions.unescapeString(title);
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {

    title = MyUsefulFunctions.escapeString(title);
    this.title = title.trim();
  }

  /**
   * @return the season
   */
  public int getSeason() {
    return season;
  }

  /**
   * @param season the season to set
   */
  public void setSeason(int season) {
    this.season = season;
  }

  /**
   * @return the fullTitle
   */
  public String getFullTitle() {
    if (this.series_ID > 0) {
      return this.getTitle() + " S" + myComponents.MyUsefulFunctions.padLeft(this.getSeason(), 2, "0");
    } else {
      return "";
    }
  }

  /**
   * @return the hidden
   */
  public int getHidden() {
    return hidden;
  }

  /**
   * @param hidden the hidden to set
   */
  public void setHidden(int hidden) {
    this.hidden = hidden;
  }

  /**
   * @return the link
   */
  public String getTvSubtitlesCode() {
    if(tvSubtitlesCode==null || tvSubtitlesCode.equals("null")){
      return "";
    }
    return tvSubtitlesCode;
  }

  /**
   * @param link the link to set
   */
  public void setTvSubtitlesCode(String link) {
    this.tvSubtitlesCode = link;
  }

  /**
   * @return the update
   */
  public int getInternetUpdate() {
    return internetUpdate;
  }

  /**
   * @param update the update to set
   */
  public void setInternetUpdate(int update) {
    this.internetUpdate = update;
  }

  /**
   * @return the tvrage_ID
   */
  public int getTvrage_ID() {
    return tvrage_ID;
  }

  /**
   * @param tvrage_ID the tvrage_ID to set
   */
  public void setTvrage_ID(int tvrage_ID) {
    this.tvrage_ID = tvrage_ID;
  }

  @Override
  public String toString() {
    return this.getFullTitle();
  }

  /**
   * @return the localDir
   */
  public String getLocalDir() {
    if(isValidLocalDir()){
    return localDir;
    } else {
      return "";
    }
  }

  /**
   * @param localDir the localDir to set
   */
  public void setLocalDir(String localDir) {
    this.localDir = localDir;
  }

  /**
   * @return the screenshot
   */
  public String getScreenshot() {
    return screenshot;
  }

  /**
   * @param screenshot the screenshot to set
   */
  public void setScreenshot(String screenshot) {
    this.screenshot = screenshot;
  }

  /**
   * @return the sonline
   */
  public String getSOnlineCode() {
    return sOnlineCode;
  }

  /**
   * @param sonline the sonline to set
   */
  public void setSOnlineCode(String sonline) {
    this.sOnlineCode = sonline;
  }

  /**
   * @return the deleted
   */
  public int getDeleted() {
    return deleted;
  }

  /**
   * @param deleted the deleted to set
   */
  public void setDeleted(int deleted) {
    this.deleted = deleted;
  }

  public boolean isValidLocalDir() {
    if(!this.localDir.equals("") && (new File(this.localDir).isDirectory())){
      return true;
    }
    return false;
  }

  public boolean isValidScreenshot() {
    return new File(Options._USER_DIR_+"images/"+getScreenshot()).isFile();
  }

  public int compareTo(SeriesRecord o) {
    Integer i = new Integer(series_ID);
    Integer oI = new Integer(o.series_ID);
    return i.compareTo(oI);
  }
}
