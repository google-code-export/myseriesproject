package tools.importExport;

import database.DBConnection;
import database.DBHelper;
import database.EpisodesRecord;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JTable;
import tools.MySeriesLogger;
import myComponents.myEvents.MyEvent;
import myComponents.myEvents.MyEventHandler;
import myComponents.myEvents.MyEventsClass;
import myseries.episodes.Episodes;
import myseries.*;
import myComponents.MyUsefulFunctions;

/**
 * The task used to import the episodes in the DB
 */
class insertEpisodesInDB implements Runnable {

    private ImportEpisodes im;
    private MyEventsClass evClass;
  private MySeries m;

    /**
     * Import episodes
     * @param im The import episodes form
     */
    insertEpisodesInDB(ImportEpisodes im, MySeries m) {
        this.im = im;
        this.m = m;
        evClass = new MyEventsClass(m);
    }

    public void run() {
        try {
            insert(m.tableEpisodes);
        } catch (SQLException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, null, ex);
        }
    }

    private void insert(JTable episodesTable) throws SQLException, ParseException {
        int total = im.newEpisodes.size();
        Iterator<EpisodesRecord> it = im.newEpisodes.iterator();
        EpisodesRecord e;
        int i = 0;
        int val = 0;
        while (it.hasNext()) {
            i++;
            e = it.next();
            save(e);
            MySeriesLogger.logger.log(Level.FINE, "Saved episode {0}", e.getTitle());
            val = (i * 100) / total;
            im.progress_import.setValue(val);
            im.progress_import.setString(val + "%");
        }
        im.progress_import.setString("Completed");
        evClass.fireMyEvent(new MyEvent(this, MyEventHandler.SERIES_UPDATE));
        Episodes.getCurrentSeriesEpisodes(episodesTable);
        im.dispose();
        MySeries.glassPane.deactivate();
    }

    private void save(EpisodesRecord e) throws SQLException {
        int episode = e.getEpisode();
        int series_ID = e.getSeries_ID();
        Vector<EpisodesRecord> episodes = DBHelper.getEpisodesBySql(
                "SELECT * from episodes WHERE episode = " + episode + " AND series_ID = " + series_ID);
        if (episodes.size() > 0) {
            e.setEpisode_ID(episodes.get(0).getEpisode_ID());
        }
        MySeriesLogger.logger.log(Level.INFO, "Saving episode {0}",e.getTitle());
        e.save(DBConnection.conn.createStatement());
    }
}
