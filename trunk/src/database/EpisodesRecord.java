/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import myComponents.MyUsefulFunctions;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import myseries.episodes.NextEpisodes;

/**
 * The episode record
 * @author lordovol
 */
public class EpisodesRecord extends Record {

  /**
   * downloaded : 1
   */
  public static final int DOWNLOADED = 1;
  /**
   * Not downloaded : 0
   */
  public static final int NOT_DOWNLOADED = 0;
  /**
   * Seen : 1
   */
  public static final int SEEN = 1;
  /**
   * Not seen : 0
   */
  public static final int NOT_SEEN = 0;
  /**
   * No Subs : 0
   */
  public static final int NO_SUBS = 0;
  /**
   * Primary Sub : 1
   */
  public static final int PRIM_SUB = 1;
  /**
   * Secondary Sub : 2
   */
  public static final int SEC_SUB = 2;
  /**
   * Both subs : 3
   */
  public static final int BOTH_SUBS = 3;
  /**
   * Unknown state of subs , used in EpdateEpisodesTable : 4
   */
  public static final int UNKNOWN_SUB = 4;
   /**
   * MySQL date format : "yyyy-MM-dd"
   */
  public static final String MYSQL_DATE_FORMAT = "yyyy-MM-dd";
 /**
  * The database table
  */
  public static final String table = "episodes";
  /**
   * Default values for episode record attributes
   */
  private int episode_ID = 0;
  private int episode = 0;
  private String title = "";
  private int series_ID = 0;
  private String aired;
  private int downloaded = NOT_DOWNLOADED;
  private int subs = NO_SUBS;
  private int seen = NOT_SEEN;

  /**
   * Default constructor
   */
  public EpisodesRecord() {
    super();
  }

  /**
   * Inserts a record in the database if it doesn't exist, or updates it if it exists
   * @return The id of the inserted record or -1 if it's an update
   * @throws java.sql.SQLException
   */
  public int save() throws SQLException {
    String sql;

    if (this.getEpisode_ID() != 0) {
      sql = "UPDATE episodes SET title = '" + this.title + "', episode = " + this.episode + ", series_ID = " +
              this.series_ID + ", aired = '" + this.aired + "' ," +
              " subs = " + this.getSubs() + ", downloaded = " + this.getDownloaded() + ", " +
              "seen = " + this.getSeen() +
              " WHERE episode_ID = " + this.getEpisode_ID();
    } else {
      sql = "INSERT INTO episodes (episode, title, series_ID,aired, downloaded, subs, seen) " +
              "VALUES(" + this.episode + ", '" + this.title +
              "', " + this.series_ID + ",'" + this.aired + "'," + this.getDownloaded() + "," +
              this.getSubs() + "," + this.getSeen() + ")";
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
    this.title = MyUsefulFunctions.escapeString(title);
  }

  /**
   * @return the episode_ID
   */
  public int getEpisode_ID() {
    return episode_ID;
  }

  /**
   * @param episode_ID the episode_ID to set
   */
  public void setEpisode_ID(int episode_ID) {
    this.episode_ID = episode_ID;
  }

  /**
   * @return the episode
   */
  public int getEpisode() {
    return episode;
  }

  /**
   * @param episode the episode to set
   */
  public void setEpisode(int episode) {
    this.episode = episode;
  }

  /**
   * @return the aired
   */
  public String getAired() {
    return aired;
  }

  /**
   * @param aired the aired to set
   */
  public void setAired(String aired) {
    if(aired.equals("")) {
      aired = "";
    }
    this.aired = MyUsefulFunctions.convertDateForMySQL(aired);
  }

  /**
   * @return the gr_subs
   */
  public int getSubs() {
    return subs;
  }

  /**
   * @param subs 
   */
  public void setSubs(int subs) {
    this.subs = subs;
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

  @Override
  public String toString() {
   return title;
  }


}
