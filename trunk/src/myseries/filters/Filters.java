/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.filters;

import database.DBConnection;
import database.DBHelper;
import database.EpisodesRecord;
import database.SeriesRecord;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import myComponents.MyTableModels.MyFilteredSeriesTableModel;
import myComponents.MyUsefulFunctions;
import tools.download.subtitles.Subtitle;
import tools.languages.LangsList;
import tools.languages.Language;

/**
 * Creates the filtered series object used to filter the series according to the
 * user's filters
 * @author lordovol
 */
public class Filters {

  /**
   * The number of columns : 7
   */
  public static final int NUMBER_OF_COLUMNS = 7;
  /**
   * The fulltitle field of the table : 0
   */
  public static final int FULLTITLE_COLUMN = 0;
  /**
   * The episode number field of the table : 1
   */
  public static final int EPISODE_NUMBER_COLUMN = 1;
  /**
   * The episode record field of the table : 2
   */
  public static final int EPISODERECORD_COLUMN = 2;
  /**
   * The aired field of the table : 3
   */
  public static final int AIRED_COLUMN = 3;
  /**
   * The downloaded field of the table : 4
   */
  public static final int DOWNLOADED_COLUMN = 4;
  /**
   * The sub status field of the table : 5
   */
  public static final int SUBS_COLUMN = 5;
  /**
   * The seen field of the table : 6
   */
  public static final int SEEN_COLUMN = 6;
  /**
   * The fulltitle field of the table title: Series
   */
  public static final String FULLTITLE_COLUMN_TITLE = "Series";
  /**
   * The episode number field of the table title : Episode
   */
  public static final String EPISODE_NUMBER_COLUMN_TITLE = "Episode";
  /**
   * The episode record field of the table title : Title
   */
  public static final String EPISODERECORD_COLUMN_TITLE = "Title";
  /**
   * The aired field of the table title : Aired
   */
  public static final String AIRED_COLUMN_TITLE = "Aired";
  /**
   * The downloaded field of the table title : Video File
   */
  public static final String DOWNLOADED_COLUMN_TITLE = "Video File";
  /**
   * The sub status field of the table title : Subtitle
   */
  public static final String SUBS_COLUMN_TITLE = "Subtitle";
  /**
   * The seen field of the table title : Seen
   */
  public static final String SEEN_COLUMN_TITLE = "Watched";
  /**
   * The not seen status : 0
   */
  public static final int SEEN_NO = 0;
  /**
   * The seen status : 1
   */
  public static final int SEEN_YES = 1;
  /**
   * The unaware seen status : 2
   */
  public static final int SEEN_UNAWARE = 2;
  /**
   * The not downloaded status : 0
   */
  public static final int DOWNLOADED_NO = 0;
  /**
   * The downloaded status : 1
   */
  public static final int DOWNLOADED_YES = 1;
  /**
   * The unaware downloaded status : 2
   */
  public static final int DOWNLOADED_UNAWARE = 2;
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
   * Which subtitles are available 0:None, 1:Primary, 2:Secondary, 3:Both, 4:Primary or Secondary,
   * 5: Not Primary , 6:Unaware , defautlt :4
   */
  private static int subtitles;
  /**
   * The filtered episodes table
   */
  public static JTable table_filters;

  /**
   * Gets the filtered episodes
   * First empty the table, create the new model and set it tot he filtered series table
   * @throws java.sql.SQLException
   */
  public static void getFilteredSeries(JComboBox cbSeen, JComboBox cbSubs, JComboBox cbDownload) throws SQLException {
    int id, subsInt, series_ID, episode;
    Boolean boolDownloaded, boolSeen;
    String title, aired;
    Language subs;
    emptyFilteredSeries();
    String where = "";
    where += getSeen(cbSeen) == EpisodesRecord.NOT_SEEN || getSeen(cbSeen) == EpisodesRecord.SEEN ? " AND seen = " + getSeen(cbSeen) : "";
    where += getDownloaded(cbDownload) == EpisodesRecord.NOT_DOWNLOADED || getDownloaded(cbDownload) == EpisodesRecord.DOWNLOADED ? " AND downloaded = " + getDownloaded(cbDownload) : "";
    where += getSubtitles(cbSubs);
    DBConnection conn = new DBConnection();
    String sql = "SELECT e.*, s.title AS sTitle, s.season FROM episodes e JOIN series s on "
        + "e.series_ID = s.series_ID WHERE s.hidden = "
        + SeriesRecord.NOT_HIDDEN + " AND s.deleted = "
        + SeriesRecord.NOT_DELETED + " AND aired < date( julianday(date('now'))) AND aired <> '0000-00-00' "
        + where + " ORDER BY aired ASC";
    ResultSet rs = conn.stmt.executeQuery(sql);
    SeriesRecord ser;
    while (rs.next()) {
      id = rs.getInt("episode_ID");
      series_ID = rs.getInt("series_ID");
      title = rs.getString("title");
      aired = rs.getString("aired");
      episode = rs.getInt("episode");
      boolDownloaded = rs.getBoolean("downloaded");
      subsInt = rs.getInt("subs");
      subs = LangsList.getLanguageById(subsInt);
      boolSeen = rs.getBoolean("seen");
      String fulltitle = rs.getString("sTitle") + " S" + myComponents.MyUsefulFunctions.padLeft(rs.getInt("season"), 2, "0");
      EpisodesRecord epRecord = new EpisodesRecord();
      epRecord.setEpisode_ID(id);
      epRecord.setEpisode(episode);
      epRecord.setAired(aired);
      epRecord.setSeries_ID(series_ID);
      epRecord.setDownloaded(boolDownloaded ? 1 :0);
      epRecord.setSeen(boolSeen ? 1 : 0);
      epRecord.setSubs(subs);
      epRecord.setTitle(title);
      


//      Vector<SeriesRecord> seriesV = DBHelper.getSeriesBySql(
//          "SELECT * FROM series WHERE hidden = " + SeriesRecord.NOT_HIDDEN
//          + " AND deleted = " + SeriesRecord.NOT_DELETED
//          + " AND series_ID = " + series_ID);
//      ser = seriesV.get(0);
      Object[] data = {fulltitle, episode, epRecord, aired, boolDownloaded, subs, boolSeen};
      if (getTableModel_filterSeries() != null) {
        getTableModel_filterSeries().addRow(data);
      }
    }
    rs.close();
    conn.close();
    if (getTableModel_filterSeries() != null && table_filters != null) {
      table_filters.setModel(getTableModel_filterSeries());
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
    Filters.tableModel_filterSeries = tableModel_filterSeries;
  }

  /**
   * @return the seen
   */
  public static int getSeen(JComboBox cb) {
    return cb.getSelectedIndex();
  }

  /**
   * @param seen the seen to set
   */
  public static void setSeen(int seen) {
    Filters.seen = seen;
  }

  /**
   * @return the downloaded
   */
  public static int getDownloaded(JComboBox cb) {
    return cb.getSelectedIndex();
  }

  /**
   * @param downloaded the downloaded to set
   */
  public static void setDownloaded(int downloaded) {
    Filters.downloaded = downloaded;
  }

  /**
   * @return the subtitles
   */
  public static String getSubtitles(JComboBox cb) {
    int sel = cb.getSelectedIndex();
    switch (sel) {
      case 0:
        return " AND subs = 0 ";
      case 1:
        return " AND subs = " + myseries.MySeries.languages.getPrimary().getId();
      case 2:
        return " AND subs = " + myseries.MySeries.languages.getSecondary().getId();
      case 3:
        return " AND subs = 3";
      case 4:
        return " AND (subs = " + myseries.MySeries.languages.getPrimary().getId() + " OR subs = " + myseries.MySeries.languages.getSecondary().getId() + ")";
      case 5:
        return " AND subs <> " + myseries.MySeries.languages.getPrimary().getId();
      case 6:
        return "";
    }
    return "";

  }

  /**
   * @param subtitles the subtitles to set
   */
  public static void setSubtitles(int subtitles) {
    Filters.subtitles = subtitles;
  }

  public static void setTableFilters(JTable table) {
    Filters.table_filters = table;
  }

  public static void setTableWidths(Integer[] filtersTableWidths) {
    TableColumnModel model = Filters.table_filters.getColumnModel();
    for (int i = 0; i < filtersTableWidths.length; i++) {
      Integer width = filtersTableWidths[i];
      model.getColumn(i).setPreferredWidth(width);
    }
  }

  private Filters() {
  }
}
