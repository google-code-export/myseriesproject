/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import myComponents.MyUsefulFunctions;
import tools.MySeriesLogger;

/**
 * A filters record
 * @author lordovol
 */
public class FilterRecord extends Record {

  /**
   * The filters table
   */
  String table = "filters";
  private int filter_ID = 0;
  private String title = "";
  private int downloaded = EpisodesRecord.NOT_DOWNLOADED;
  private int seen = EpisodesRecord.NOT_SEEN;
  private int subtitles = EpisodesRecord.NO_SUBS;

  /**
   * Default constructor
   */
  public FilterRecord() {
    super();
  }

   /**
   * Delete a filter
   * @return The row count of the deleted records or -1
   * @throws SQLException
   */
  public int delete() throws SQLException {
    String sql = "";
    if (this.getFilter_ID() != 0) {
      MySeriesLogger.logger.log(Level.INFO, "Deleting filter: {0} ",getTitle());
      sql = "DELETE FROM filters WHERE filter_ID = " + this.getFilter_ID();
      return queryUpdate(DBConnection.conn.createStatement(), sql);
    }
    return -1;
  }

  /**
   * Inserts a filter if its new or updates it if it exists
   * @return The id of the saved filter or -1 if it's an update
   * @throws SQLException
   */
  public int save() throws SQLException {
    int result = 0;
    String sql;
    MySeriesLogger.logger.log(Level.INFO, "Saving filter : {0}",getTitle());
    if (this.getFilter_ID() != 0) {
      sql = "UPDATE filters SET title = '" + this.getTitle() + "', downloaded = " + this.getDownloaded()
              + ", seen = " + this.getSeen() + ", subtitles = " + this.getSubtitles() + " WHERE filter_ID = " + this.getFilter_ID();
    } else {
      sql = "INSERT INTO filters (title, downloaded, seen, subtitles) VALUES('" + this.getTitle() + "', "
              + this.getDownloaded() + ", " + this.getSeen() + ", " + this.getSubtitles() + ")";
    }
    return queryUpdate(DBConnection.conn.createStatement(),sql);
  }

  /**
   * @return the filter_ID
   */
  public int getFilter_ID() {
    return filter_ID;
  }

  /**
   * @param filter_ID the filter_ID to set
   */
  public void setFilter_ID(int filter_ID) {
    this.filter_ID = filter_ID;
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
    this.title = MyUsefulFunctions.escapeString(title);
  }

  /**
   * @return the downloaded
   */
  public int getDownloaded() {
    return downloaded;
  }

  /**
   * @param downloaded the downloaded to set
   */
  public void setDownloaded(int downloaded) {
    this.downloaded = downloaded;
  }

  /**
   * @return the seen
   */
  public int getSeen() {
    return seen;
  }

  /**
   * @param seen the seen to set
   */
  public void setSeen(int seen) {
    this.seen = seen;
  }

  /**
   * @return the subtitles
   */
  public int getSubtitles() {
    return subtitles;
  }

  /**
   * @param subtitles the subtitles to set
   */
  public void setSubtitles(int subtitles) {
    this.subtitles = subtitles;
  }
}
