/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.series;

import database.DBConnection;
import database.DBHelper;
import database.SeriesRecord;
import java.io.File;
import java.io.FilenameFilter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import myComponents.MyTableModels.MySeriesTableModel;
import myComponents.MyUsefulFunctions;
import myComponents.myEvents.MyEvent;
import myComponents.myEvents.MyEventHandler;
import myComponents.myFileFilters.SubtitlesFilter;
import myComponents.myFileFilters.VideoFilter;
import myseries.MySeries;
import tools.MySeriesLogger;
import tools.options.Options;

/**
 * The series table
 * @author lordovol
 */
public class Series {

    /**  The number of columns in Series table : 3 */
    public static final int NUMBER_OF_COLUMS = 3;
    /**  The fulltitle field : 0  */
    public static final int SERIESRECORD_COLUMN = 0;
    /** The hidden field : 1   */
    public static final int HIDDEN_COLUMN = 1;
    /** The update field : 2  */
    public static final int UPDATE_COLUMN = 2;
    /** The fulltitle field title : Title   */
    public static final String SERIES_RECORD_COLUMN_TITLE = "Title";
    /** The hidden field title : Hidden   */
    public static final String HIDDEN_COLUMN_TITLE = "Hidden";
    /** The update field title : Update   */
    public static final String UPDATE_COLUMN_TITLE = "Update";
    /** The default season : 1   */
    public static final int DEFAULT_SEASON = 1;
    /** The minimum season : 1   */
    public static final int MINIMUM_SEASON = 1;
    /** The maximum season : 100   */
    public static final int MAXIMUM_SEASON = 100;
    /** The season step : 1   */
    public static final int SEASON_STEP = 1;
    /** The model of the Series table   */
    private static MySeriesTableModel tableModel_series;
    /** The series table   */
    private static JTable table_series = new JTable();
    /** The current series record   */
    private static SeriesRecord currentSeries;
    /** The series subtitle files */
    private static File[] subtitleFiles;
    /** The series video files */
    private static File[] videoFiles;

    private Series() {
    }

    /**
     * Gets all the series
     * First empties the series, gets all the series by title and sets the
     * model of the series table.
     * @param deleted Show deleted series or not
     * @return an arraylist of all the series records
     * @throws java.sql.SQLException
     */
    public static void updateSeriesTable(boolean deleted) throws SQLException {
        MySeriesLogger.logger.log(Level.INFO, "Updating series table");
        emptySeries();
        ArrayList<SeriesRecord> series = getSeries(false);
        for (Iterator<SeriesRecord> it = series.iterator(); it.hasNext();) {
            SeriesRecord s = it.next();
            Object[] data = {s, s.getHidden() == 1 ? true : false, s.getInternetUpdate() == 1 ? true : false};
            getTableModel_series().addRow(data);
        }
        getTable_series().setModel(getTableModel_series());
    }

    public static int getSize() {
        try {
            return getSeries(false).size();
        } catch (SQLException ex) {
            return 0;
        }
    }

    /**
     * Gets all the series
     * First empties the series, gets all the series by title and sets the
     * model of the series table.
     * @param deleted If should search for deleted series
     * @return an arraylist of all the series records
     * @throws java.sql.SQLException
     */
    public static ArrayList<SeriesRecord> getSeries(boolean deleted) throws SQLException {
        int d = deleted ? 1 : 0;
        MySeriesLogger.logger.log(Level.INFO, "Getting all the {0} series", deleted ? "deleted" : "");
        ArrayList<SeriesRecord> series = new ArrayList<SeriesRecord>();
        String sql = "SELECT * FROM series  WHERE deleted = " + d + " ORDER BY title , season";
        ResultSet rs = DBConnection.stmt.executeQuery(sql);
        boolean hidden;
        boolean update;
        while (rs.next()) {
            SeriesRecord s = new SeriesRecord();
            s.setTitle(rs.getString("title"));
            s.setSeason(rs.getInt("season"));
            s.setSeries_ID(rs.getInt("series_ID"));
            s.setTvrage_ID(rs.getInt("tvrage_ID"));
            s.setLocalDir(rs.getString("localDir"));
            s.setScreenshot(rs.getString("screenshot"));
            s.setSOnlineCode(rs.getString("sonline"));
            s.setTvSubtitlesCode(rs.getString("link"));
            hidden = rs.getBoolean("hidden");
            s.setHidden(rs.getInt("hidden"));
            update = rs.getBoolean("internetUpdate");
            s.setInternetUpdate(rs.getInt("internetUpdate"));
            deleted = rs.getBoolean("deleted");
            s.setDeleted(rs.getInt("deleted"));
            series.add(s);
            MySeriesLogger.logger.log(Level.FINE, "Found series {0}", s.getFullTitle());
        }
        rs.close();
        return series;
    }

    /**
     * Sets the selected series fulltitle of the series table selected row
     *
     * @param s The selected row of the series table
     * @return The fulltitle of the selected series
     * @throws java.sql.SQLException
     */
    public static void selectSeries(MySeries m, int s) throws SQLException {
        SeriesRecord series = null;
        MyEvent evt = new MyEvent(m, MyEventHandler.SET_CURRENT_SERIES);
        if (table_series.getRowCount() > 0 && s > -1) {

            table_series.setColumnSelectionAllowed(false);
            table_series.setRowSelectionAllowed(true);
            table_series.setRowSelectionInterval(s, s);
            series = getCurrentSerial(m, s, true);
            MySeriesLogger.logger.log(Level.INFO, "Select series {0}", series.getFullTitle());
            evt.setSeries(series);
            m.getEvClass().fireMyEvent(evt);
        } else {
            MySeriesLogger.logger.log(Level.INFO, "No series selected");
            series = getCurrentSerial(m, -1, true);
            evt.setSeries(series);
            m.getEvClass().fireMyEvent(evt);
        }
    }

    public static void setTableWidths(Integer[] seriesTableWidths) {
        TableColumnModel model = Series.table_series.getColumnModel();
        for (int i = 0; i < seriesTableWidths.length; i++) {
            Integer width = seriesTableWidths[i];
            model.getColumn(i).setPreferredWidth(width);
        }
    }

    /**
     * Gets the serial of the selected row in the series table
     * Then sets the current serial and updates the episodes list
     * @param s The selected row or -1 for no series
     * @param showEpisodes update episodes or not (deprecated - always update)
     * @throws java.sql.SQLException
     */
    public static SeriesRecord getCurrentSerial(MySeries m, int s, boolean showEpisodes) throws SQLException {
        MySeriesLogger.logger.log(Level.INFO, "Get series at row {0}", s);
        if (s == -1) {
            currentSeries = null;
        } else {
            currentSeries = (SeriesRecord) table_series.getModel().getValueAt(s, Series.SERIESRECORD_COLUMN);
            MySeriesLogger.logger.log(Level.FINE, "Found series {0}", currentSeries.getFullTitle());
        }
        MyEvent evt = new MyEvent(m, MyEventHandler.SET_CURRENT_SERIES);
        evt.setSeries(currentSeries);
        m.getEvClass().fireMyEvent(evt);
        return currentSeries;
    }

    /**
     * Empty the series table
     */
    private static void emptySeries() {
        MySeriesLogger.logger.log(Level.INFO, "Emptying series table");
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
        if (currentSeries != null) {
            return currentSeries;
        } else {
            return new SeriesRecord();
        }
    }

    /**
     * @param series the currentSerial to set
     */
    public static void setCurrentSerial(SeriesRecord series) {
        try {
            MySeriesLogger.logger.log(Level.INFO, "Setting the current serial");
            //Update data
            // Image image = new ImageIcon(MySeries.class.getResource(MyImagePanel.LOGO)).getImage();
            if (series != null) {
                int series_id = series.getSeries_ID();
                currentSeries = DBHelper.getSeriesByID(series_id);
                MySeriesLogger.logger.log(Level.INFO, "Current serial set to {0}", currentSeries != null ? currentSeries.getFullTitle() : "none");
            } else {
                currentSeries = null;
                return;
            }
        } catch (SQLException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
            currentSeries = null;
        }
    }

    /**
     * @return the subtitleFiles
     */
    public static File[] getSubtitleFiles(SeriesRecord series) {
        if (subtitleFiles == null) {
            return getFiles(series, new SubtitlesFilter());
        }
        return subtitleFiles;
    }

    /**
     * @return the videoFiles
     */
    public static File[] getVideoFiles(SeriesRecord series) {
        if (videoFiles == null) {
            return getFiles(series, new VideoFilter());
        }
        return videoFiles;
    }

    private static File[] getFiles(SeriesRecord series, FilenameFilter filter) {
        File directory = new File(series.getLocalDir());
        if (!series.isValidLocalDir() || !Options.toBoolean(Options.AUTO_FILE_UPDATING) || MyUsefulFunctions.isNetworkPath(directory)) {
            return null;
        }
        MySeriesLogger.logger.log(Level.INFO, "Getting files interface dir {0}", directory);
        File[] files = directory.listFiles(filter);
        return files;
    }
}
