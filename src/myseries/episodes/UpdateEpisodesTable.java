/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.episodes;

import database.EpisodesRecord;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.event.TableModelEvent;
import tools.MySeriesLogger;
import javax.swing.table.TableModel;
import myComponents.MyMessages;
import myseries.MySeries;
import tools.download.subtitles.Subtitle;
import tools.languages.Language;
import myComponents.MyUsefulFunctions;
/**
 * Updates the episodes table
 * @author lordovol
 */
public class UpdateEpisodesTable {

  Object rec[] = new Object[Episodes.NUMBER_OF_COLUMS];
  private TableModel model;

  public UpdateEpisodesTable(TableModelEvent e) {
    if (e.getType() == TableModelEvent.UPDATE) {
      int row = e.getFirstRow();
      model = (TableModel) e.getSource();
      for (int i = 0; i < Episodes.NUMBER_OF_COLUMS; i++) {
        rec[i] = model.getValueAt(row, i);
      }
      updateEpisode(rec);
    }
  }

  private void updateEpisode(int row) {
    for (int i = 0; i < model.getColumnCount(); i++) {
      rec[i] = model.getValueAt(row, i);
    }
    updateEpisode(rec);
  }

  private void updateEpisode(Object[] rec) {
    try {
      EpisodesRecord er = (EpisodesRecord) rec[Episodes.EPISODERECORD_COLUMN];
      er.setEpisode((Integer)rec[Episodes.EPISODE_NUM_COLUMN]);
      er.setTitle(((EpisodesRecord)rec[Episodes.EPISODERECORD_COLUMN]).getTitle());
      if (!rec[Episodes.AIRED_COLUMN].equals("")) {
        er.setAired((String)rec[Episodes.AIRED_COLUMN]);
      }
      er.setDownloaded((Boolean)rec[Episodes.DOWNLOADED_COLUMN] ? EpisodesRecord.DOWNLOADED : EpisodesRecord.NOT_DOWNLOADED);
      er.setSubs((Language) rec[Episodes.SUBS_COLUMN]);
      er.setSeen((Boolean)rec[Episodes.SEEN_COLUMN]? EpisodesRecord.SEEN : EpisodesRecord.NOT_SEEN);
      er.setRate((Double)rec[Episodes.RATE_COLUMN]);
      er.save();
     // NextEpisodes.createNextEpisodes();
     // NextEpisodes.show();
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);


    } catch (NumberFormatException ex) {
      MyMessages.error("Not a number", "The value you entered is not a number");

    }

  }
}
