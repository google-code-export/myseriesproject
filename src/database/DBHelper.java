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
public class DBHelper {

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
    ResultSet rs = er.query(sql);
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
    /**
   * Gets all the filters
   * @return a vector of filtersRecords
   * @throws SQLException
   */
  public static Vector<FilterRecord> getSavedFilterRecords() throws SQLException {
    Vector<FilterRecord> recs = getFilterRecordBySql("SELECT * FROM filters ORDER BY title");
    return recs;
  }

  /**
   * Gets a filter by the name
   * @param title The name of the filter
   * @return The filter record if found or null
   * @throws SQLException
   */
  public static FilterRecord getFilterByTitle(String title) throws SQLException {
    Vector<FilterRecord> a = getFilterRecordBySql("SELECT * FROM filters WHERE title = '" + title + "' LIMIT 1");
    if (a.size() == 1) {
      return a.get(0);
    } else {
      return null;
    }
  }

  /**
   * Gets filter records by executing a query
   * @param sql The query to execute
   * @return a vector of filterRecords
   * @throws SQLException
   */
  public static Vector<FilterRecord> getFilterRecordBySql(String sql) throws SQLException {
    ResultSet rs = null;
    
    try {
      rs = database.DBConnection.stmt.executeQuery(sql);
      Vector<FilterRecord> a = new Vector<FilterRecord>();
      while (rs.next()) {
        FilterRecord s = new FilterRecord();
        s.setFilter_ID(rs.getInt("filter_ID"));
        s.setDownloaded(rs.getInt("downloaded"));
        s.setSeen(rs.getInt("seen"));
        s.setSubtitles(rs.getInt("subtitles"));
        s.setTitle(rs.getString("title"));
        a.add(s);
      }
      rs.close();
      return a;
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
  }

  /**
   * Gets an array of the filters titles
   * @return an array of filter titles
   * @throws SQLException
   */
  public static String[] getFiltersTitlesList() throws SQLException {
    Vector<FilterRecord> sfr = getSavedFilterRecords();
    String[] filters = new String[sfr.size()];
    for (int i = 0; i < sfr.size(); i++) {
      filters[i] = sfr.get(i).getTitle();
    }
    return filters;
  }

    /**
   * Gets series by executing a query
   * @param sql The query to execute
   * @return a vector of series records
   * @throws SQLException
   */
  public static Vector<SeriesRecord> getSeriesBySql(String sql) throws SQLException {
    ResultSet rs = null;
    SeriesRecord s = new SeriesRecord();
    try {
      rs = s.query(sql);
      Vector<SeriesRecord> a = new Vector<SeriesRecord>();
      while (rs.next()) {
        s.setSeason(rs.getInt("season"));
        s.setSeries_ID(rs.getInt("series_ID"));
        s.setTitle(rs.getString("title"));
        s.setTvSubtitlesCode(rs.getString("link"));
        s.setInternetUpdate(rs.getInt("internetUpdate"));
        s.setTvrage_ID(rs.getInt("tvrage_ID"));
        s.setLocalDir(rs.getString("localDir"));
        s.setScreenshot(rs.getString("screenshot"));
        s.setSOnlineCode(rs.getString("sonline"));
        a.add(s);
      }
      rs.close();
      return a;
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
  }

  /**
   * Gets a series by its id
   * @param series_ID The id
   * @return The series or null if it's not found
   * @throws SQLException
   */
  public static SeriesRecord getSeriesByID(int series_ID) throws SQLException {
    Vector<SeriesRecord> series = new Vector<SeriesRecord>();
    series = getSeriesBySql("SELECT * FROM series WHERE series_ID = " + series_ID);
    return series.size() == 1 ? series.get(0) : null;
  }

  /**
   * Gets all series
   * @return a vector of series records
   * @throws SQLException
   */
  public static Vector<SeriesRecord> getAllSeries() throws SQLException {
    return getSeriesBySql("SELECT * FROM series WHERE hidden = " + SeriesRecord.NOT_HIDDEN);

  }
}
