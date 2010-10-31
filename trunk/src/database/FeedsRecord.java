/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        String sql = "SELECT * FROM feeds WHERE feed_ID = " + feed_ID;
        ResultSet rs = query(sql);
        while (rs.next()) {
          this.feed_ID = feed_ID;
          this.url = rs.getString("url");
          this.title = rs.getString("title");
        }
      } catch (SQLException ex) {
        myseries.MySeries.logger.log(Level.SEVERE, null, ex);
      }

    }
  }

  public static boolean deleteById(int id) {
    if(id > 0){
      String sql = "DELETE FROM feeds WHERE feed_ID = "+id;
      try {
        queryUpdate(sql);
        return true;
      } catch (SQLException ex) {
        myseries.MySeries.logger.log(Level.SEVERE, null, ex);
        return false;
      }
    }
    return false;
  }

  public static ResultSet getAll() {
    try {
      String sql = "SELECT * FROM feeds ORDER BY title";
      return query(sql);
    } catch (SQLException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
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
    if (this.feed_ID != 0) {
      sql = "UPDATE feeds SET title = '" + this.getTitle() + "', url = '" + this.getUrl()
              + "' WHERE feed_ID = " + this.feed_ID;
    } else {
      sql = "INSERT INTO feeds (title, url) "
              + "VALUES('" + this.getTitle() + "','" + this.getUrl() + "')";
    }
    return queryUpdate(sql);
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
