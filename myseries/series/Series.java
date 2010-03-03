/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.series;

import database.DBConnection;
import database.SeriesRecord;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JTable;
import myComponents.MySeriesTableModel;
import myComponents.MyUsefulFunctions;
import myseries.episodes.Episodes;

/**
 *
 * @author lordovol
 */
public class Series {

  /**
   * The model of the Series table
   */
  private static MySeriesTableModel tableModel_series;
  /**
   * The series table
   */
  private static JTable table_series = new JTable();
  /**
   * The current series record
   */
  private static SeriesRecord currentSeries;

  private Series() {
  }

  /**
   * Gets all the series
   * First empties the series, gets all the series by title and sets the
   * model of the series table.
   * @return an arraylist of all the series records
   * @throws java.sql.SQLException
   */
  public static ArrayList<SeriesRecord> getSeries() throws SQLException {
    emptySeries();
    ArrayList<SeriesRecord> series = new ArrayList<SeriesRecord>();
    String sql = "SELECT * FROM series ORDER BY title , season";
    ResultSet rs = DBConnection.stmt.executeQuery(sql);
    boolean hidden;
    boolean update;
    while (rs.next()) {
      SeriesRecord s = new SeriesRecord();
      s.setTitle(rs.getString("title"));
      s.setSeason(rs.getInt("season"));
      s.setSeries_ID(rs.getInt("series_ID"));
      hidden = rs.getBoolean("hidden");
      s.setHidden(rs.getInt("hidden"));
      update = rs.getBoolean("internetUpdate");
      s.setInternetUpdate(rs.getInt("internetUpdate"));
      String seasonStr = MyUsefulFunctions.padLeft(s.getSeason(), 2, "0");
      Object[] data = {"  " + s.getTitle() + " S" + seasonStr, s.getSeries_ID(), hidden,update};
      getTableModel_series().addRow(data);
      series.add(s);
    }
    rs.close();
    getTable_series().setModel(getTableModel_series());
    return series;

  }

  /**
   * Sets the selected series fulltitle of the series table selected row
   *
   * @param s The selected row of the series table
   * @return The fulltitle of the selected series
   * @throws java.sql.SQLException
   */
  public static String selectSeries(int s) throws SQLException {
    if (table_series.getRowCount() > 0) {
      table_series.setColumnSelectionAllowed(false);
      table_series.setRowSelectionAllowed(true);
      table_series.setRowSelectionInterval(s, s);
      getCurrentSerial(s, true);
      return Series.getCurrentSerial().getFullTitle();
    } else {
      getCurrentSerial(-1, true);
      return "";
    }
  }

  /**
   * Gets the serial of the selected row in the series table
   * Then sets the current serial and updates the episodes list
   * @param s The selected row or -1 for no series
   * @param showEpisodes update episodes or not (deprecated - always update)
   * @throws java.sql.SQLException
   */
   public static void getCurrentSerial(int s, boolean showEpisodes) throws SQLException {
    if (s == -1) {
      currentSeries = new SeriesRecord();
      currentSeries.setSeries_ID(0);
      currentSeries.setSeason(0);
      currentSeries.setTitle("");
      currentSeries.setLink("");
      currentSeries.setInternetUpdate(1);
      Series.setCurrentSerial(currentSeries);
      return;
    }
    int series_ID = Integer.parseInt(String.valueOf(table_series.getModel().getValueAt(s, 1)));
    currentSeries = new SeriesRecord();

    String sql = "SELECT * FROM series WHERE series_ID = " + series_ID;
    ResultSet rs = SeriesRecord.query(sql);
    if (rs.next()) {
      currentSeries.setSeries_ID(rs.getInt("series_ID"));
      currentSeries.setSeason(rs.getInt("season"));
      currentSeries.setTitle(rs.getString("title").trim());
      currentSeries.setLink(rs.getString("link").trim());
      currentSeries.setInternetUpdate(rs.getInt("InternetUpdate"));
      currentSeries.setTvrage_ID(rs.getInt("tvrage_ID"));

      //if (showEpisodes) {
    //}
    } else {
      currentSeries.setSeries_ID(0);
      currentSeries.setSeason(0);
      currentSeries.setTitle("");
      currentSeries.setLink("");
      currentSeries.setInternetUpdate(1);
      currentSeries.setTvrage_ID(0);
    }
    rs.close();
    Series.setCurrentSerial(currentSeries);
    Episodes.updateEpisodesTable();


  }


   /**
    * Empty the series table
    */
  private static void emptySeries() {
    getTableModel_series().setRowCount(0);
  }

  /**
   * @return the tableModel_series
   */
  public static MySeriesTableModel getTableModel_series() {
    return tableModel_series;
  }

  /**
   * @param tableModel_series the tableModel_series to set
   */
  public static void setTableModel_series(MySeriesTableModel tableModel_series) {
    Series.tableModel_series = tableModel_series;
  }

  /**
   * @return the table_series
   */
  public static JTable getTable_series() {
    return table_series;
  }

  /**
   * @param table_series the table_series to set
   */
  public static void setTable_series(JTable table_series) {
    Series.table_series = table_series;
  }

  /**
   * Gets the current series or a new empty series if current series is null
   * @return the currentSerial
   */
  public static SeriesRecord getCurrentSerial() {
    if(currentSeries != null){
    return currentSeries;
    } else {
       return new SeriesRecord();
    }
  }

  /**
   * @param aCurrentSerial the currentSerial to set
   */
  public static void setCurrentSerial(SeriesRecord aCurrentSerial) {
    currentSeries = aCurrentSerial;
  }
}
