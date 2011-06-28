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
  public static final String FEED_ID = "feed_ID";
  public static final String TITLE = "title";
  public static final String URL = "url";

  public FeedsRecord() {
    super();
  }

  public FeedsRecord(int feed_ID) {
    super();
    Statement stmt = null;
    if (feed_ID > 0) {
      ResultSet rs = null;
      try {
        MySeriesLogger.logger.log(Level.INFO, "Getting feed: {0}", feed_ID);
        String sql = "SELECT * FROM " + TABLE + " WHERE " + FEED_ID + " = " + feed_ID;
        stmt = DBConnection.conn.createStatement();
        rs = query(stmt, sql);
        while (rs.next()) {
          this.feed_ID = feed_ID;
          this.url = rs.getString("url");
          this.title = rs.getString("title");
          MySeriesLogger.logger.log(Level.FINE, "Feed found : {0}", title);
        }
      } catch (SQLException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      } finally {
        if (rs != null) {
          try {
            rs.close();

          } catch (SQLException ex) {
           MySeriesLogger.logger.log(Level.SEVERE, null, ex);
          }
          if (stmt != null) {
            try {
              stmt.close();
            } catch (SQLException ex) {
             MySeriesLogger.logger.log(Level.SEVERE, null, ex);
            }
          }
        }
      }

    }
  }
  
  /**
   * Saves a feed Record
   * @return The id of the record or -1 if it's an update
   * @throws SQLException
   */
  public int save() throws SQLException, DatabaseException {
    String sql;
    Statement stmt = DBConnection.conn.createStatement();
    MySeriesLogger.logger.log(Level.INFO, "Saving feed");
    if (this.feed_ID != 0) {
      return save(TABLE, new String[]{TITLE, URL},
              new String[]{MyUsefulFunctions.escapeString(this.getTitle()), this.getUrl()},
              FEED_ID + " = ?", new String[]{String.valueOf(this.feed_ID)});
    } else {
      return save(TABLE, new String[]{TITLE, URL}, new String[]{MyUsefulFunctions.escapeString(this.getTitle()), this.getUrl()}, null, null);
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
        delete(TABLE, FEED_ID + " = ?", new String[]{String.valueOf(this.feed_ID)});
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
      Statement stmt = DBConnection.conn.createStatement();
      rs = query(stmt, sql);
      while (rs.next()) {
        FeedsRecord f = new FeedsRecord();
        f.feed_ID = rs.getInt("feed_ID");
        f.title = rs.getString("title");
        f.url = rs.getString("url");
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
