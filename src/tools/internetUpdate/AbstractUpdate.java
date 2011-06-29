/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.internetUpdate;

import Exceptions.DatabaseException;
import database.DBHelper;
import database.EpisodesRecord;
import database.SeriesRecord;
import java.sql.SQLException;
import java.util.ArrayList;
import tools.MySeriesLogger;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JTable;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myseries.*;
import tools.options.Options;
import myComponents.MyUsefulFunctions;
/**
 * The series updater abstract class
 * @author ssoldatos
 */
public abstract class AbstractUpdate {

    /**
     * The series to update
     */
    protected Vector<SeriesRecord> serVector = new Vector<SeriesRecord>();
    /**
     * The list of series to update
     */
    protected ArrayList<AbstractSeriesToUpdate> list = new ArrayList<AbstractSeriesToUpdate>();
    /**
     * The internet update form
     */
    protected InternetUpdate iu = null;
    /**
     * The current messages
     */
    protected String currentMessages = "";
    /**
     * The data lines that are parsed
     */
    protected int totalLinesParsed = 0;
    /**
     * Start time in msecs
     */
    protected long start;
    /**
     * End time in msecs
     */
    protected long end;
    /**
     * If connected to internet
     */
    protected boolean isConected = false;
    /**
     * The current updated series
     */
    protected SeriesRecord series;
    /**
     * The site from which data is updated
     */
    protected String site;
    /**
     * The episodes table
     */
    protected JTable episodesTable;

    public void run() {
        start = System.currentTimeMillis();
        MySeriesLogger.logger.log(Level.INFO, "Updating...");
        update(episodesTable);
        if (!isConected) {
            MySeries.glassPane.deactivate();
            iu.dispose();
        }
    }

    /**
     * Calculates the execution time
     * @return The updating time in mm::ss::ms
     */
    protected String calcExecTime() {
        String execTime = "";
        int hours, mins, secs, mill;
        long t, a;
        end = System.currentTimeMillis();
        a = (end - start);
        t = (end - start) / 1000;
        mill = (int) a % 1000;
        secs = (int) t % 60;
        mins = (int) t / 60;
        hours = (int) t / 3600;

        execTime = MyUsefulFunctions.padLeft("" + mins, 2, "0") + ":"
                + MyUsefulFunctions.padLeft("" + secs, 2, "0") + ","
                + MyUsefulFunctions.padLeft("" + mill, 3, "0");
        return execTime;
    }

    /**
     * Appends a message to the current messages
     * @param str The message to append
     * @throws IllegalArgumentException
     */
    protected void append(String str) throws IllegalArgumentException {
        currentMessages += str + "<br />";
        iu.editor_messages.setText(currentMessages);
        int messagesLength = iu.editor_messages.getDocument().getLength();
        if (messagesLength > 0) {
            iu.editor_messages.setCaretPosition(messagesLength - 1);
        }
    }

    /**
     * Starts the updating
     */
    protected void update(JTable episodesTable) {
        try {
            if (iu.getCurrentSeries() == null) {
                serVector = DBHelper.getSeriesBySql(
                        "SELECT * FROM series WHERE internetUpdate = 1 AND "
                        + "deleted = " + SeriesRecord.NOT_DELETED);
                MySeriesLogger.logger.log(Level.INFO, "Updating {0} series",serVector.size());
            } else {
                MySeriesLogger.logger.log(Level.INFO, "Updating series {0}",iu.getCurrentSeries().getFullTitle());
                serVector.add(iu.getCurrentSeries());
            }
            iu.progress_bar.setIndeterminate(true);
            iu.progress_bar.setString("Getting data from " + site);
            append("<span style='font-weight:bold;font-size:12px'>Getting data</span>");
            for (int i = 0; i < serVector.size(); i++) {
                series = serVector.get(i);
                iu.label_update_series.setText("Getting data for \"" + series.getFullTitle() + "\"");
                if (!read(series)) {
                    return;
                }
                iu.label_update_series.setText("");
            }
            if (list.size() > 0) {
                updateEpisodes(episodesTable);
            } else {
                MySeriesLogger.logger.log(Level.INFO, "Nothing to update");
                MyMessages.message("No Update!!!", "Nothing to update");
                MySeries.glassPane.deactivate();
                iu.dispose();
            }

        } catch (SQLException ex) {
            MySeriesLogger.logger.log(Level.WARNING, "Could not get the series from the database", ex);
            MyMessages.error("Updating Series", "Could not get the series from the database", true, true);
        }
    }

    /**
     * Reads from internet the episodes data of series and adds the data to the @list ArrayList
     * @param series
     * @return true if episodes data is read
     */
    protected abstract boolean read(SeriesRecord series);

    /**
     * Updates the database
     * @throws SQLException
     */
    protected abstract void updateEpisodes(JTable episodesTable) throws SQLException;

    protected boolean shouldSaveEpisode(EpisodesRecord episodeRecord, String title, String airDate) {
        MySeriesLogger.logger.log(Level.INFO, "Check if episode {0} should be saved",title);
        String oldTitle = episodeRecord.getTitle().trim().toLowerCase();
        String oldDate = episodeRecord.getAired();
        if (!oldTitle.equals(title.toLowerCase())
                || (!oldDate.equals(airDate) && MyUsefulFunctions.isValidDate(airDate))) {
            MySeriesLogger.logger.log(Level.FINE, "Yes");
            return true;
        }
        MySeriesLogger.logger.log(Level.INFO, "No");
        return false;
    }
}
