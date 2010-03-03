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
public class EpisodesRecord extends Record {

  public static final String table = "episodes";
  private int episode_ID = 0;
  private int episode = 0;
  private String title = "";
  private int series_ID = 0;
  private String aired;
  private int downloaded = 0;
  private int subs = 0;
  private int seen = 0;

  public EpisodesRecord() {
    super();
  }

  public static EpisodesRecord getEpisodeByID(int episode_ID) throws SQLException {
    String sql = "SELECT * FROM episodes WHERE episode_ID = " + episode_ID;
    return getEpisodesBySql(sql).get(0);
  }

  public static Vector<EpisodesRecord> getAllEpisodes() throws SQLException {
    String sql = "SELECT * FROM episodes ";
    return getEpisodesBySql(sql);
  }

  public static Vector<EpisodesRecord> getEpisodesBySql(String sql) throws SQLException {
    EpisodesRecord er = new EpisodesRecord();
    ResultSet rs = query(sql);
    Vector<EpisodesRecord> a = new Vector<EpisodesRecord>();
    try{
    if (rs.next()) {
      er.episode_ID = rs.getInt("episode_ID");
      er.episode = rs.getInt("episode");
      er.title = rs.getString("title");
      er.series_ID = rs.getInt("series_ID");
      er.aired = rs.getString("aired");
      er.downloaded = rs.getInt("downloaded");
      er.subs = rs.getInt("subs");
      er.seen = rs.getInt("seen");
      a.add(er);
    }
    rs.close();
    return a;
    } finally{
      if (rs != null) {
        rs.close();
      }
    }
  }

  /**
   * Saves a record
   * @return
   * @throws java.sql.SQLException
   */
  public int save() throws SQLException {
    int result = 0;
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
      //SimpleDateFormat f = new SimpleDateFormat(Options._MYSQL_DATE_FORMAT_);
      //aired = f.format(Calendar.getInstance().getTime());
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
}
