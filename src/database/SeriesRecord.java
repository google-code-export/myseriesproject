/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import myComponents.MyUsefulFunctions;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author lordovol
 */
public class SeriesRecord extends Record {

  String table = "series";
  private int series_ID = 0;
  private String title = "";
  private int season = 0;
  private String fullTitle = "";
  private int hidden = 0;
  private String link = "";
  private int internetUpdate = 1;
  private int tvrage_ID = 0;
  private String localDir = "";
  private String screenshot = "";

  public SeriesRecord() {
    super();
  }

  public static SeriesRecord getSeriesByID(int series_ID) throws SQLException {
    Vector<SeriesRecord> a = new Vector<SeriesRecord>();
    a = getSeriesBySql("SELECT * FROM series WHERE series_ID = " + series_ID);
    if (a.size() == 1) {
      return a.get(0);
    } else {
      return null;
    }
  }

  public static Vector<SeriesRecord> getAllSeries() throws SQLException {
    return getSeriesBySql("SELECT * FROM series WHERE hidden = 0");

  }

  public static Vector<SeriesRecord> getSeriesBySql(String sql) throws SQLException {
    ResultSet rs = null;
    SeriesRecord s = new SeriesRecord();
    try {
      rs = query(sql);
      Vector<SeriesRecord> a = new Vector<SeriesRecord>();
      while (rs.next()) {
        s = new SeriesRecord();
        s.setSeason(rs.getInt("season"));
        s.setSeries_ID(rs.getInt("series_ID"));
        s.setTitle(rs.getString("title"));
        s.setLink(rs.getString("link"));
        s.setInternetUpdate(rs.getInt("internetUpdate"));
        s.setTvrage_ID(rs.getInt("tvrage_ID"));
        s.setLocalDir(rs.getString("localDir"));
        s.setScreenshot(rs.getString("screenshot"));
        a.add(s);
      }
      rs.close();
      return a;
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
  }

  public int save() throws SQLException {
    int result = 0;
    String sql;
    if (this.series_ID != 0) {
      sql = "UPDATE series SET title = '" + this.title + "', season = " + this.getSeason()
              + ", hidden = " + this.getHidden() + ", link ='" + this.getLink() + "', internetUpdate  ="
              + this.getInternetUpdate() + ", tvrage_ID = " + this.getTvrage_ID()
              + ", localDir = '" + this.localDir + "', screenshot = '" + this.screenshot + "' WHERE series_ID = " + this.getSeries_ID();
    } else {
      sql = "INSERT INTO series (title, season, hidden, link, internetUpdate, tvrage_ID, localDir, screenshot) VALUES('" + this.getTitle() + "', "
              + this.getSeason() + ", " + this.getHidden() + ", '" + this.getLink() + "',"
              + this.getInternetUpdate() + ", " + this.getTvrage_ID() + ", '" + this.getLocalDir() + "','" + this.getScreenshot() + "' )";
    }
    return queryUpdate(sql);
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
  public String getLink() {
    return link;
  }

  /**
   * @param link the link to set
   */
  public void setLink(String link) {
    this.link = link;
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
    return localDir;
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
}
