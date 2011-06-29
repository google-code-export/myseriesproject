/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import Exceptions.DatabaseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import myComponents.MyMessages;
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
  public static final String TABLE = "filters";
  public static final String C_FILTER_ID = "filter_ID";
  public static final String C_TITLE = "title";
  public static final String C_DOWNLOADED = "downloaded";
  public static final String C_SEEN = "seen";
  public static final String C_SUBTITLES = "subtitles";
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
  
  public static Vector<FilterRecord> query(String[] columns, String whereClause, String[] whereValues, String group,String having, String order, String limit) throws SQLException {
    ResultSet rs = query(TABLE, columns, whereClause, whereValues, group, having, order, limit);
    Vector<FilterRecord> a = new Vector<FilterRecord>();
      while (rs.next()) {
        FilterRecord s = new FilterRecord();
        s.setFilter_ID(rs.getInt(C_FILTER_ID));
        s.setDownloaded(rs.getInt(C_DOWNLOADED));
        s.setSeen(rs.getInt(C_SEEN));
        s.setSubtitles(rs.getInt(C_SUBTITLES));
        s.setTitle(rs.getString(C_TITLE));
        MySeriesLogger.logger.log(Level.FINE, "Found filter: {0}", s);
        a.add(s);
      }
      rs.close();
      return a;
  }

  /**
   * Delete a filter
   * @return The row count of the deleted records or -1
   * @throws SQLException
   */
  public int delete() {
    try {
      if (this.getFilter_ID() != 0) {
        MySeriesLogger.logger.log(Level.INFO, "Deleting filter: {0} ", getTitle());
        delete(TABLE, C_FILTER_ID + " = ? ", new String[]{String.valueOf(this.getFilter_ID())});
        return 1;
      }
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.INFO, "Could not delete filter: {0} ", getTitle());
      MyMessages.error("Deleting filter", "Could not delete filter", true, true);
    }
    return -1;
  }

  /**
   * Inserts a filter if its new or updates it if it exists
   * @return The id of the saved filter or -1 if it's an update
   * @throws SQLException
   */
  public int save() throws SQLException, DatabaseException {
    MySeriesLogger.logger.log(Level.INFO, "Saving filter : {0}", getTitle());
    if (this.filter_ID == 0) {
      return save(TABLE, new String[]{C_TITLE, C_DOWNLOADED, C_SEEN, C_SUBTITLES},
          new String[]{this.getTitle(), String.valueOf(this.getDownloaded()),
            String.valueOf(this.getSeen()), String.valueOf(this.getSubtitles())}, null, null);
    } else {
      return save(TABLE, new String[]{C_TITLE, C_DOWNLOADED, C_SEEN, C_SUBTITLES},
          new String[]{this.getTitle(), String.valueOf(this.getDownloaded()),
            String.valueOf(this.getSeen()), String.valueOf(this.getSubtitles())}, C_FILTER_ID+ " = ? ", 
            new String[] {String.valueOf(this.getFilter_ID())});
    }

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
