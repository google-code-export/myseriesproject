/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author lordovol
 */
public class EpisodesHelper {

  /**
   * Gets an episode by the primary key (episode_ID)
   * @param episode_ID The episode id
   * @return The episode record or null if it's not found
   * @throws SQLException
   */
  public static EpisodesRecord getEpisodeByID(int episode_ID) throws SQLException {
    String sql = "SELECT * FROM episodes WHERE episode_ID = " + episode_ID;
    Vector<EpisodesRecord> episodes = getEpisodesBySql(sql);
    return episodes.size() == 1 ? episodes.get(0) : null;
  }

  /**
   * Gets all episodes from the database
   * @return a Vector of episodesRecords
   * @throws SQLException
   */
  public static Vector<EpisodesRecord> getAllEpisodes() throws SQLException {
    String sql = "SELECT * FROM episodes ";
    return getEpisodesBySql(sql);
  }

  /**
   * Gets episodes by executing an sql quey
   * @param sql The query to execute
   * @return a vector of episodesRecords
   * @throws SQLException
   */
  public static Vector<EpisodesRecord> getEpisodesBySql(String sql) throws SQLException {
    EpisodesRecord er = new EpisodesRecord();
    ResultSet rs = DBConnection.stmt.executeQuery(sql);
    Vector<EpisodesRecord> a = new Vector<EpisodesRecord>();
    try {
      if (rs.next()) {
        er.setEpisode_ID(rs.getInt("episode_ID"));
        er.setEpisode(rs.getInt("episode"));
        er.setTitle(rs.getString("title"));
        er.setSeries_ID(rs.getInt("series_ID"));
        er.setAired(rs.getString("aired"));
        er.setDownloaded(rs.getInt("downloaded"));
        er.setSubs(rs.getInt("subs"));
        er.setSeen(rs.getInt("seen"));
        a.add(er);
      }
      rs.close();
      return a;
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
  }
}
