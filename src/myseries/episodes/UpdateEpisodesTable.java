/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.episodes;

import Exceptions.DatabaseException;
import database.DBConnection;
import database.EpisodesRecord;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import javax.swing.event.TableModelEvent;
import tools.MySeriesLogger;
import javax.swing.table.TableModel;
import myComponents.MyMessages;
import tools.languages.Language;

/**
 * Updates the episodes table
 * @author lordovol
 */
public class UpdateEpisodesTable {

  Object rec[] = new Object[Episodes.NUMBER_OF_COLUMS];
  private TableModel model;

  public UpdateEpisodesTable(TableModelEvent e) {
    if (e.getType() == TableModelEvent.UPDATE) {
      MySeriesLogger.logger.log(Level.INFO, "Updating episodes table");
      int row = e.getFirstRow();
      model = (TableModel) e.getSource();
      for (int i = 0; i < Episodes.NUMBER_OF_COLUMS; i++) {
        rec[i] = model.getValueAt(row, i);
      }
      updateEpisode(rec);
    }
  }

  private void updateEpisode(int row) {
    MySeriesLogger.logger.log(Level.INFO, "Update episode interface row {0}", row);
    for (int i = 0; i < model.getColumnCount(); i++) {
      rec[i] = model.getValueAt(row, i);
    }
    updateEpisode(rec);
  }

  private void updateEpisode(Object[] rec) {
    try {
      EpisodesRecord er = (EpisodesRecord) rec[Episodes.EPISODERECORD_COLUMN];
      er.setEpisode((Integer) rec[Episodes.EPISODE_NUM_COLUMN]);
      er.setTitle(((EpisodesRecord) rec[Episodes.EPISODERECORD_COLUMN]).getTitle());
      if (!rec[Episodes.AIRED_COLUMN].equals("")) {
        er.setAired((String) rec[Episodes.AIRED_COLUMN]);
      }
      er.setDownloaded((Boolean) rec[Episodes.DOWNLOADED_COLUMN] ? EpisodesRecord.DOWNLOADED : EpisodesRecord.NOT_DOWNLOADED);
      er.setSubs((Language) rec[Episodes.SUBS_COLUMN]);
      er.setSeen((Boolean) rec[Episodes.SEEN_COLUMN] ? EpisodesRecord.SEEN : EpisodesRecord.NOT_SEEN);
      er.setRate((Double) rec[Episodes.RATE_COLUMN]);
      MySeriesLogger.logger.log(Level.INFO, "Update episode {0}", er.getTitle());
      er.save();
      MySeriesLogger.logger.log(Level.FINE, "Episode updated");
      // NextEpisodes.createNextEpisodes();
      // NextEpisodes.show();
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
    }  catch (NumberFormatException ex) {
      MySeriesLogger.logger.log(Level.WARNING, "Wrong episode number entered");
      MyMessages.warning("Not a number", "The value you entered is not a number", true, true);
    }
  }
}
