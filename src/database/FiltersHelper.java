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
public class FiltersHelper {

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
    FilterRecord s = null;
    try {
      s = new FilterRecord();
      rs = DBConnection.stmt.executeQuery(sql);
      Vector<FilterRecord> a = new Vector<FilterRecord>();
      while (rs.next()) {
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
}
