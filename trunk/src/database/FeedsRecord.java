/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

import java.sql.SQLException;

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


  /**
   * Saves a feed Record
   * @return The id of the record or -1 if it's an update
   * @throws SQLException
   */

  public int save() throws SQLException{
    String sql;
    if (this.feed_ID != 0) {
      sql = "UPDATE feeds SET title = '" + this.getTitle() + "', url = '" + this.getUrl()
          + "' WHERE feed_ID = " + this.feed_ID;
    } else {
      sql = "INSERT INTO feeds (title, url) "
          + "VALUES('" + this.getTitle() + "','" + this.getUrl()+ "')";
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
