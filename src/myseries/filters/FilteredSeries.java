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
import myComponents.MyTableModels.MyFilteredSeriesTableModel;

/**
 * Creates the filtered series object used to filter the series according to the
 * user's filters
 * @author lordovol
 */
public class FilteredSeries {

  public static final int FULLTITLE_COLUMN = 0;
  public static final int EPISODE_NUMBER_COLUMN = 1;
  public static final int EPISODERECORD_COLUMN = 2;
  public static final int AIRED_COLUMN = 3;
  public static final int DOWNLOADED_COLUMN = 4;
  public static final int SUBS_COLUMN = 5;
  public static final int SEEN_COLUMN = 6;
  public static final int SUBS_NONE = 0;
  public static final int SUBS_SEC = 1;
  public static final int SUBS_PRIM = 2;
  public static final int SUBS_BOTH = 3;
  public static final int SUBS_PRIM_OR_SEC = 4;
  public static final int SUBS_NOT_PRIM = 5;
  public static final int SUBS_UNAWARE = 6;



  /**
   * The model of the filteredSeries table
   */
  private static MyFilteredSeriesTableModel tableModel_filterSeries;
  /**
   * Seen state of the episode 0:Not seen, 1:Seen , 2:Unaware , default = 0
   */
  private static int seen = EpisodesRecord.NOT_SEEN;
  /**
   * Download state of the episode 0:Not downloaded, 1:downloaded, 2:Unaware
   */
  private static int downloaded = EpisodesRecord.NOT_DOWNLOADED;
  /**
   * Which subtitles are available 0:None, 1:English, 2:Greek, 3:Both, 4:English or Greek,
   * 5: Not Greek , 6:Unaware , defautlt :4
   */
  private static int subtitles = SUBS_PRIM_OR_SEC;
  /**
   * The filtered episodes table
   */
  private static JTable table_FilteredSeriesEpisodesList;

  /**
   * Gets the filtered episodes
   * First empty the table, create the new model and set it tot he filtered series table
   * @throws java.sql.SQLException
   */
  public static void getFilteredSeries() throws SQLException {
    int id, subsInt, series_ID, episode;
    Boolean boolDownloaded, boolSeen;
    String title, aired, subs;

    emptyFilteredSeries();
    String where = "";
    where += getSeen() == EpisodesRecord.NOT_SEEN || getSeen() == EpisodesRecord.SEEN ? " AND seen = " + getSeen() : "";
    where += getDownloaded() == EpisodesRecord.NOT_DOWNLOADED || getDownloaded() == EpisodesRecord.DOWNLOADED ? " AND downloaded = " + getDownloaded() : "";
    where += getSubtitles() < SUBS_PRIM_OR_SEC ? " AND subs =  " + getSubtitles() : getSubtitles() == SUBS_PRIM_OR_SEC ?
      " AND  (subs = "+SUBS_SEC+" OR subs ="+SUBS_PRIM+" )" : getSubtitles() == SUBS_NOT_PRIM ? " AND  (subs = "+SUBS_NONE+" OR subs = "+SUBS_SEC+" )" : "";
    String sql = "SELECT e.* FROM episodes e JOIN series s on e.series_ID = s.series_ID WHERE s.hidden = "+SeriesRecord.NOT_HIDDEN+" " + where + " ORDER BY aired ASC";
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
      subs = subsInt == SUBS_NONE ? "None" : subsInt == SUBS_SEC ? "English" : subsInt == SUBS_PRIM ? "Greek" : "Both";
      boolSeen = rs.getBoolean("seen");
      Vector<SeriesRecord> seriesV = SeriesRecord.getSeriesBySql("SELECT * FROM series WHERE hidden = "+SeriesRecord.NOT_HIDDEN+" AND series_ID = " + series_ID);
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
