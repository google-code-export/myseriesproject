/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.filters;

import database.DBConnection;
import database.EpisodesRecord;
import database.SeriesRecord;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JTable;
import myComponents.MyFilteredSeriesTableModel;

/**
 * Creates the filtered series object used to filter the series according to the
 * user's filters
 * @author lordovol
 */
public class FilteredSeries {

  /**
   * The model of the filteredSeries table
   */
  private static MyFilteredSeriesTableModel tableModel_filterSeries;
  /**
   * Seen state of the episode 0:Not seen, 1:Seen , 2:Unaware , default = 0
   */
  private static int seen = 0;
  /**
   * Download state of the episode 0:Not downloaded, 1:downloaded, 2:Unaware
   */
  private static int downloaded = 1;
  /**
   * Which subtitles are available 0:None, 1:English, 2:Greek, 3:Both, 4:English or Greek,
   * 5: Not Greek , 6:Unaware , defautlt :4
   */
  private static int subtitles = 4;
  /**
   * The filtered episodes table
   */
  private static JTable table_FilteredSeriesEpisodesList;

  /**
   * Gets the filtered episodes
   * First empty the table, create the new model and set it tot he filtered series table
   * @throws java.sql.SQLException
   * @throws java.io.IOException
   */
  public static void getFilteredSeries() throws SQLException {
    int id, subsInt, series_ID, episode;
    Boolean boolDownloaded, boolSeen;
    String title, aired, subs;

    emptyFilteredSeries();
    String where = "";
    where += getSeen() == 0 || getSeen() == 1 ? " AND seen = " + getSeen() : "";
    where += getDownloaded() == 0 || getDownloaded() == 1 ? " AND downloaded = " + getDownloaded() : "";
    where += getSubtitles() < 4 ? " AND subs =  " + getSubtitles() : getSubtitles() == 4 ? " AND  (subs = 1 OR subs =2 )" : getSubtitles() == 5 ? " AND  (subs = 0 OR subs = 1 )" : "";
    String sql = "SELECT e.* FROM episodes e JOIN series s on e.series_ID = s.series_ID WHERE s.hidden = 0 " + where + " ORDER BY aired ASC";
    ResultSet rs = DBConnection.stmt.executeQuery(sql);
    SeriesRecord ser;
    while (rs.next()) {
      id = rs.getInt("episode_ID");
      series_ID = rs.getInt("series_ID");
      title = rs.getString("title");
      aired = rs.getString("aired");
      episode = rs.getInt("episode");
      boolDownloaded = rs.getBoolean("downloaded");
      subsInt = rs.getInt("subs");
      subs = subsInt == 0 ? "None" : subsInt == 1 ? "English" : subsInt == 2 ? "Greek" : "Both";
      boolSeen = rs.getBoolean("seen");
      Vector<SeriesRecord> seriesV = SeriesRecord.getSeriesBySql("SELECT * FROM series WHERE hidden = 0 AND series_ID = " + series_ID);
      ser = seriesV.get(0);

      Object[] data = {ser.getFullTitle(), episode, EpisodesRecord.getEpisodeByID(id), aired, boolDownloaded, subs, boolSeen};
      if (getTableModel_filterSeries() != null) {
        getTableModel_filterSeries().addRow(data);
      }
    }
    rs.close();

    if (getTableModel_filterSeries() != null && table_FilteredSeriesEpisodesList != null) {
      table_FilteredSeriesEpisodesList.setModel(getTableModel_filterSeries());
    }
  }

  /**
   * Empty the filtered series table
   * @throws java.io.IOException
   */
  private static void emptyFilteredSeries() {
    if (getTableModel_filterSeries() != null) {
      getTableModel_filterSeries().setRowCount(0);
    }
  }

  /**
   * @return the tableModel_filterSeries
   */
  public static MyFilteredSeriesTableModel getTableModel_filterSeries() {
    return tableModel_filterSeries;
  }

  /**
   * @param tableModel_filterSeries the tableModel_filterSeries to set
   */
  public static void setTableModel_filterSeries(MyFilteredSeriesTableModel tableModel_filterSeries) {
    FilteredSeries.tableModel_filterSeries = tableModel_filterSeries;
  }

  /**
   * @return the seen
   */
  public static int getSeen() {
    return seen;
  }

  /**
   * @param seen the seen to set
   */
  public static void setSeen(int seen) {
    FilteredSeries.seen = seen;
  }

  /**
   * @return the downloaded
   */
  public static int getDownloaded() {
    return downloaded;
  }

  /**
   * @param downloaded the downloaded to set
   */
  public static void setDownloaded(int downloaded) {
    FilteredSeries.downloaded = downloaded;
  }

  /**
   * @return the subtitles
   */
  public static int getSubtitles() {
    return subtitles;
  }

  /**
   * @param subtitles the subtitles to set
   */
  public static void setSubtitles(int subtitles) {
    FilteredSeries.subtitles = subtitles;
  }

  private FilteredSeries() {
  }
}
