package tools.importExport;

import database.DBHelper;
import database.EpisodesRecord;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import myComponents.myEvents.MyEvent;
import myComponents.myEvents.MyEventHandler;
import myseries.episodes.Episodes;
import myseries.series.Series;
import myseries.*;

/**
 * The task used to import the episodes in the DB
 */
class insertEpisodesInDB implements Runnable {

  private ImportEpisodes im;
  private MySeries m;

  /**
   * Import episodes
   * @param im The import episodes form
   */
  insertEpisodesInDB(MySeries m, ImportEpisodes im) {
    this.im = im;
    this.m = m;
  }

  public void run() {
    try {
      insert();
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (ParseException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }

  private void insert() throws SQLException, ParseException {
    int total = im.newEpisodes.size();
    Iterator<EpisodesRecord> it = im.newEpisodes.iterator();
    EpisodesRecord e;
    int i = 0;
    int val = 0;
    while (it.hasNext()) {
      i++;
      e = it.next();
      save(e);
      val = (i * 100) / total;
      im.progress_import.setValue(val);
      im.progress_import.setString(val + "%");
    }
    im.progress_import.setString("Completed");
    m.fireMyEvent(new MyEvent(m, MyEventHandler.SERIES_UPDATE));
    Episodes.getCurrentSeriesEpisodes();
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
    e.save();
  }
}
