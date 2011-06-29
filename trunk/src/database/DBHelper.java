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
   * Gets episodes by executing an sql quey
   * @param sql The query to execute
   * @return a vector of episodesRecords
   * @throws SQLException
   */
  public static Vector<EpisodesRecord> getEpisodesBySql(String sql) throws SQLException {

    ResultSet rs = EpisodesRecord.query(sql);
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
      if (rs != null) {
        rs.close();
      }
    }
  }

  /**
   * Gets a filter by the name
   * @param title The name of the filter
   * @return The filter record if found or null
   * @throws SQLException
   */
  public static FilterRecord getFilterByTitle(String title) throws SQLException {
    MySeriesLogger.logger.log(Level.INFO, "Get filter : {0}", title);
    return FilterRecord.queryOne("title = ?", new String[]{title}, null);
  }

  /**
   * Gets an array of the filters titles
   * @return an array of filter titles
   * @throws SQLException
   */
  public static String[] getFiltersTitlesList() throws SQLException {
    MySeriesLogger.logger.log(Level.INFO, "Getting titles of saved filters");
    Vector<FilterRecord> sfr = FilterRecord.queryAll(null, null, FilterRecord.C_TITLE + " ASC", null);
    String[] filters = new String[sfr.size()];
    for (int i = 0; i < sfr.size(); i++) {
      filters[i] = sfr.get(i).getTitle();
      MySeriesLogger.logger.log(Level.FINE, "Found filter title {0}", filters[i]);
    }
    return filters;
  }

   public static Vector<EpisodesRecord> getSeriesEpisodesByRate(int series_ID) {
    MySeriesLogger.logger.log(Level.INFO, "Getting series {0} episodes by rating", series_ID);
    try {
      return EpisodesRecord.queryAll(null, EpisodesRecord.C_SERIES_ID + " = ? AND " + EpisodesRecord.C_RATE + " > ?",
              new String[]{String.valueOf(series_ID), "0"}, null, null, "rate DESC", null);
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
    try {
      rs = DBConnection.conn.createStatement().executeQuery(sql);
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
      if (rs != null) {
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
    ResultSet rs = null;
    try {
      String sql = "SELECT series.season FROM series join episodes ON " + "series.series_ID=episodes.series_ID WHERE episodes.episode_ID=" + episode_ID;

      rs = DBConnection.conn.createStatement().executeQuery(sql);
      return rs.getInt("season");
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      return 0;
    } finally {
      if (rs != null) {
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
