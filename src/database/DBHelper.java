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
import java.util.logging.Logger;
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
    MySeriesLogger.logger.log(Level.INFO, "Getting episode {0}", episode_ID);
    Vector<EpisodesRecord> episodes = getEpisodesBySql(sql);
    return episodes.size() == 1 ? episodes.get(0) : null;
  }

  /**
   * Gets all episodes from the database
   * @return a Vector of episodesRecords
   * @throws SQLException
   */
  public static Vector<EpisodesRecord> getAllEpisodes() throws SQLException {
    MySeriesLogger.logger.log(Level.INFO, "Getting all episodes");
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
    DBConnection conn = new DBConnection();
    ResultSet rs = conn.stmt.executeQuery(sql);
    Vector<EpisodesRecord> a = new Vector<EpisodesRecord>();
    MySeriesLogger.logger.log(Level.INFO, "Getting episodes by sql");
    MySeriesLogger.logger.log(Level.INFO, sql);
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
        MySeriesLogger.logger.log(Level.FINE, "Found episode : {0}", er);
      }
      rs.close();
      return a;
    } finally {
      conn.close();
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
    MySeriesLogger.logger.log(Level.INFO, "Getting all filters");
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
    MySeriesLogger.logger.log(Level.INFO, "Get filter : {0}", title);
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
    MySeriesLogger.logger.log(Level.INFO, "Geting filter record by sql");
    MySeriesLogger.logger.log(Level.INFO, sql);
    DBConnection conn = new DBConnection();
    try {
      rs = conn.stmt.executeQuery(sql);
      Vector<FilterRecord> a = new Vector<FilterRecord>();
      while (rs.next()) {
        FilterRecord s = new FilterRecord();
        s.setFilter_ID(rs.getInt("filter_ID"));
        s.setDownloaded(rs.getInt("downloaded"));
        s.setSeen(rs.getInt("seen"));
        s.setSubtitles(rs.getInt("subtitles"));
        s.setTitle(rs.getString("title"));
        MySeriesLogger.logger.log(Level.FINE, "Found filter: {0}", s);
        a.add(s);
      }
      rs.close();
      return a;
    } finally {
      conn.close();
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
    MySeriesLogger.logger.log(Level.INFO, "Getting titles of saved filters");
    Vector<FilterRecord> sfr = getSavedFilterRecords();
    String[] filters = new String[sfr.size()];
    for (int i = 0; i < sfr.size(); i++) {
      filters[i] = sfr.get(i).getTitle();
      MySeriesLogger.logger.log(Level.FINE, "Found filter title {0}", filters[i]);
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
    MySeriesLogger.logger.log(Level.INFO, "Getting series by sql");
    MySeriesLogger.logger.log(Level.INFO, sql);
      DBConnection conn = new DBConnection();
      try {
      rs = conn.stmt.executeQuery(sql);
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
        MySeriesLogger.logger.log(Level.FINE, "Found series: {0}", s);
        a.add(s);
      }
      rs.close();
      return a;
    } finally {
      conn.close();
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
    MySeriesLogger.logger.log(Level.INFO, "Getting series : {0}", series_ID);
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
    MySeriesLogger.logger.log(Level.INFO, "Getting all series");
    return getSeriesBySql("SELECT * FROM series WHERE hidden = "
        + SeriesRecord.NOT_HIDDEN + " AND deleted = " + SeriesRecord.NOT_DELETED);

  }

  public static Vector<EpisodesRecord> getSeriesEpisodesByRate(int series_ID) {
    MySeriesLogger.logger.log(Level.INFO, "Getting series {0} episodes by rating", series_ID);
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
     ResultSet rs = null;
    MySeriesLogger.logger.log(Level.INFO, "Getting day events for date : {0}", date);
    String sql = "SELECT series.screenshot as image, episodes.episode AS ep, episodes.title AS title , "
        + "series.title AS series, episodes.downloaded AS downloaded, episodes.seen AS seen FROM "
        + "episodes JOIN series on episodes.series_ID = series.series_ID WHERE "
        + "aired = '" + date + "' AND deleted = 0";
     DBConnection conn = new DBConnection();
      try {
     rs = conn.stmt.executeQuery(sql);
      while (rs.next()) {
        ScheduleEvent ev = new ScheduleEvent();
        ev.setSeries(rs.getString("series"));
        ev.setEpisodeNumber(rs.getInt("ep"));
        ev.setEpisode(rs.getString("title"));
        ev.setImage(rs.getString("image"));
        ev.setDownloaded(rs.getInt("downloaded"));
        ev.setSeen(rs.getInt("seen"));
        MySeriesLogger.logger.log(Level.FINE, "Found event : {0}", ev);
        events.add(ev);
      }
      return events;
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
      return events;
    } finally {
      conn.close();
      if(rs!=null){
        try {
          rs.close();
        } catch (SQLException ex) {
          MySeriesLogger.logger.log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  public static Vector<EpisodesRecord> getSeriesEpisodesByRate(String seriesTitle) {
    MySeriesLogger.logger.log(Level.INFO, "Getting series {0} episodes by rate", seriesTitle);
    String sql = "SELECT episodes.* FROM episodes  JOIN series ON "
        + "series.series_ID = episodes.series_ID "
        + "WHERE series.title = '" + seriesTitle + "' AND rate > 0 ORDER BY rate DESC LIMIT " + LIMIT;
    try {
      return getEpisodesBySql(sql);
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      return null;
    }
  }

  public static int getSeasonByEpisodeId(int episode_ID) {
    MySeriesLogger.logger.log(Level.INFO, "Getting season by episode id: {0}", episode_ID);
    DBConnection conn = new DBConnection();
    ResultSet rs = null;
    try {
      String sql = "SELECT series.season FROM series join episodes ON " + "series.series_ID=episodes.series_ID WHERE episodes.episode_ID=" + episode_ID;
      
      rs = conn.stmt.executeQuery(sql);
      return rs.getInt("season");
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      return 0;
    } finally{
      conn.close();
      if(rs!=null){
        try {
          rs.close();
        } catch (SQLException ex) {
          MySeriesLogger.logger.log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  private DBHelper() {
  }
}
