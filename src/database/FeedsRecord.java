/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import Exceptions.DatabaseException;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import myComponents.MyUsefulFunctions;
import tools.options.Options;
import tools.MySeriesLogger;
import tools.options.Paths;

/**
 *
 * @author lordovol
 */
public class FeedsRecord extends Record {

  /**
   * The default values for the record attributes
   */
  private int feed_ID = 0;
  private String title = "";
  private String url = "";
  public static final String TABLE = "feeds";
  public static final String C_FEED_ID = "feed_ID";
  public static final String C_TITLE = "title";
  public static final String C_URL = "url";

  public FeedsRecord() {
    super();
  }
  
   private static FeedsRecord create(ResultSet rs) throws SQLException{
    FeedsRecord f = new FeedsRecord();
      f.setFeed_ID(rs.getInt(C_FEED_ID));
      f.setTitle(rs.getString(C_TITLE));
      f.setUrl(rs.getString(C_URL));
      return f;
  }
   
   public static FeedsRecord queryOne(String whereClause, String[] whereValues, String order) throws SQLException {
    ResultSet rs = query(TABLE, null, whereClause, whereValues, null, null, order, "1");
    if (rs.next()) {
      return create(rs);
    }
    return null;
  }

  public static Vector<FeedsRecord> queryAll( String whereClause, String[] whereValues, String order, String limit) throws SQLException {
    ResultSet rs = query(TABLE, null, whereClause, whereValues, null, null, order, limit);
    Vector<FeedsRecord> a = new Vector<FeedsRecord>();
    while (rs.next()) {
      a.add(create(rs));
    }
    return a;
  }
   
  /**
   * Saves a feed Record
   * @return The id of the record or -1 if it's an update
   * @throws SQLException
   */
  public int save() throws SQLException, DatabaseException {
    MySeriesLogger.logger.log(Level.INFO, "Saving feed");
    if (this.feed_ID != 0) {
      return save(TABLE, new String[]{C_TITLE, C_URL},
              new String[]{MyUsefulFunctions.escapeString(this.getTitle()), this.getUrl()},
              C_FEED_ID + " = ?", new String[]{String.valueOf(this.feed_ID)});
    } else {
      return save(TABLE, new String[]{C_TITLE, C_URL}, new String[]{MyUsefulFunctions.escapeString(this.getTitle()), this.getUrl()}, null, null);
    }

  }


  public boolean delete() {
    if (this.feed_ID > 0) {
      ResultSet rs = null;
      MySeriesLogger.logger.log(Level.INFO, "Deleting feed : {0}", feed_ID);
      try {
        File file = new File(Options._USER_DIR_ + Paths.FEEDS_PATH + feed_ID);
        if (file.exists()) {
          MySeriesLogger.logger.log(Level.INFO, "Deleting feed file : {0}", file.getName());
          file.delete();
          MySeriesLogger.logger.log(Level.FINE, "Feed file deleted");
        }
        delete(TABLE, C_FEED_ID + " = ?", new String[]{String.valueOf(this.feed_ID)});
        return true;
      } catch (SQLException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
        return false;
      }
    }
    return false;
  }

  public static ArrayList<FeedsRecord> getAll() {
    ArrayList<FeedsRecord> feeds = new ArrayList<FeedsRecord>();
    ResultSet rs = null;
    try {
      MySeriesLogger.logger.log(Level.INFO, "Getting all feeds");
      String sql = "SELECT * FROM feeds ORDER BY title";
      rs = query(sql);
      while (rs.next()) {
        FeedsRecord f = new FeedsRecord();
        f.feed_ID = rs.getInt(C_FEED_ID);
        f.title = rs.getString(C_TITLE);
        f.url = rs.getString(C_URL);
        feeds.add(f);
        MySeriesLogger.logger.log(Level.FINE, "Feed found : {0}", f.title);
      }
      return feeds;
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      return null;
    } finally {
      try {
        rs.close();
      } catch (SQLException ex) {
        Logger.getLogger(FeedsRecord.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  
  /**
   * @return the feed_ID
   */
  public int getFeed_ID() {
    return feed_ID;
  }

  /**
   * @param feed_ID the feed_ID to set
   */
  public void setFeed_ID(int feed_ID) {
    this.feed_ID = feed_ID;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * @param url the url to set
   */
  public void setUrl(String url) {
    this.url = url;
  }
}
