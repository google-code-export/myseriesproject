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
public class SeriesHelper {

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
      rs = DBConnection.stmt.executeQuery(sql);
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
