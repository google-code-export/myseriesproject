/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
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

  public FeedsRecord() {
    super();
  }

  public FeedsRecord(int feed_ID) {
    super();
    if(feed_ID > 0){
      try {
        MySeriesLogger.logger.log(Level.INFO, "Getting feed: {0}",feed_ID);
        String sql = "SELECT * FROM feeds WHERE feed_ID = " + feed_ID;
        DBConnection conn = new DBConnection();
        ResultSet rs = query(conn.stmt, sql);
        while (rs.next()) {
          this.feed_ID = feed_ID;
          this.url = rs.getString("url");
          this.title = rs.getString("title");
          MySeriesLogger.logger.log(Level.FINE, "Feed found : {0}",title);
        }
      } catch (SQLException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      }

    }
  }

  public static boolean deleteById(int id) {
    if(id > 0){
      MySeriesLogger.logger.log(Level.INFO, "Deleting feed : {0}",id);
      String sql = "DELETE FROM feeds WHERE feed_ID = "+id;
      try {
        File file = new File(Options._USER_DIR_+Paths.FEEDS_PATH+id);
        if(file.exists()){
          MySeriesLogger.logger.log(Level.INFO, "Deleting feed file : {0}" , file.getName());
          file.delete();
          MySeriesLogger.logger.log(Level.FINE, "Feed file deleted");
        }
        DBConnection conn = new DBConnection();
        queryUpdate(conn.stmt, sql);
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
    try {
      MySeriesLogger.logger.log(Level.INFO, "Getting all feeds");
      String sql = "SELECT * FROM feeds ORDER BY title";
      DBConnection conn = new DBConnection();
      ResultSet rs = query(conn.stmt, sql);
      while(rs.next()){
        FeedsRecord f = new FeedsRecord();
        f.feed_ID = rs.getInt("feed_ID");
        f.title = rs.getString("title");
        f.url = rs.getString("url");
        feeds.add(f);
        MySeriesLogger.logger.log(Level.FINE, "Feed found : {0}",f.title);
      }
      return feeds;
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      return null;
    }
  }

  /**
   * Saves a feed Record
   * @return The id of the record or -1 if it's an update
   * @throws SQLException
   */
  public int save() throws SQLException {
    String sql;
    DBConnection conn = new DBConnection();
    MySeriesLogger.logger.log(Level.INFO, "Saving feed");
    if (this.feed_ID != 0) {
      sql = "UPDATE feeds SET title = '" + MyUsefulFunctions.escapeString(this.getTitle()) + "', url = '" + this.getUrl()
              + "' WHERE feed_ID = " + this.feed_ID;
    } else {
      sql = "INSERT INTO feeds (title, url) "
              + "VALUES('" + MyUsefulFunctions.escapeString(this.getTitle()) + "','" + this.getUrl() + "')";
    }
    return queryUpdate(conn.stmt, sql);
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
