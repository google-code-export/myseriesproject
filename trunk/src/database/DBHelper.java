/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import com.googlecode.scheduler.ScheduleDay;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import myseries.schedule.ScheduleEvent;
import tools.languages.LangsList;
import myComponents.MyUsefulFunctions;
import tools.MySeriesLogger;
/**
 *
 * @author lordovol
 */
public class DBHelper {
  /**
   * The limit of the episodes to fetch 
   */
  public static final int LIMIT = 20;

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
    ResultSet rs = DBConnection.conn.createStatement().executeQuery(sql);
    Vector<EpisodesRecord> a = new Vector<EpisodesRecord>();
    try {
      while (rs.next()) {
        EpisodesRecord er = new EpisodesRecord();
        er.setEpisode_ID(rs.getInt("episode_ID"));
        er.setEpisode(rs.getInt("episode"));
        er.setTitle(rs.getString("title"));
        er.setSeries_ID(rs.getInt("series_ID"));
        er.setAired(rs.getString("aired"));
        er.setDownloaded(rs.getInt("downloaded"));
        er.setSubs(LangsList.getLanguageById(rs.getInt("subs")));
        er.setSeen(rs.getInt("seen"));
        er.setRate(rs.getDouble("rate"));
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
      rs = database.DBConnection.conn.createStatement().executeQuery(sql);
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

    try {
      rs = DBConnection.conn.createStatement().executeQuery(sql);
      Vector<SeriesRecord> a = new Vector<SeriesRecord>();
      while (rs.next()) {
        SeriesRecord s = new SeriesRecord();
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
    return getSeriesBySql("SELECT * FROM series WHERE hidden = " + 
        SeriesRecord.NOT_HIDDEN + " AND deleted = " + SeriesRecord.NOT_DELETED);

  }

  public static Vector<EpisodesRecord> getSeriesEpisodesByRate(int series_ID) {
    String sql = "SELECT * FROM episodes WHERE series_ID = " + series_ID + " AND rate > 0 ORDER BY rate DESC";
    try {
      return getEpisodesBySql(sql);
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      return null;
    }

  }

  public static ArrayList<ScheduleEvent> getDayEvents(ScheduleDay sDay) {
    ArrayList<ScheduleEvent> events = new ArrayList<ScheduleEvent>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String date = sdf.format(sDay.getDate());
    String sql = "SELECT series.screenshot as image, episodes.episode AS ep, episodes.title AS title , "
        + "series.title AS series, episodes.downloaded AS downloaded, episodes.seen AS seen FROM "
        + "episodes JOIN series on episodes.series_ID = series.series_ID WHERE "
        + "aired = '" + date + "' AND deleted = 0";
    try {
      ResultSet rs = DBConnection.conn.createStatement().executeQuery(sql);
      while (rs.next()) {
        ScheduleEvent ev = new ScheduleEvent();
        ev.setSeries(rs.getString("series"));
        ev.setEpisodeNumber(rs.getInt("ep"));
        ev.setEpisode(rs.getString("title"));
        ev.setImage(rs.getString("image"));
        ev.setDownloaded(rs.getInt("downloaded"));
        ev.setSeen(rs.getInt("seen"));
        events.add(ev);
      }
      return events;
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      return events;
    }
  }

  public static Vector<EpisodesRecord> getSeriesEpisodesByRate(String seriesTitle) {

       String sql = "SELECT episodes.* FROM episodes  JOIN series ON "
           + "series.series_ID = episodes.series_ID "
           + "WHERE series.title = '" + seriesTitle + "' AND rate > 0 ORDER BY rate DESC LIMIT "+LIMIT;
    try {
      return getEpisodesBySql(sql);
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      return null;
    }
  }

  public static int getSeasonByEpisodeId(int episode_ID) {
    try {
      String sql = "SELECT series.season FROM series join episodes ON " + "series.series_ID=episodes.series_ID WHERE episodes.episode_ID=" + episode_ID;
      ResultSet rs = DBConnection.conn.createStatement().executeQuery(sql);
      return rs.getInt("season");
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      return 0;
    }
  }

  private DBHelper() {
  }
}
