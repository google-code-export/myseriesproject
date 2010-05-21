/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.episodes;

import database.EpisodesRecord;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import myComponents.MyMessages;
import myseries.MySeries;
import tools.download.subtitles.Subtitle;
import tools.languages.LangsList;

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
    try {
      //Episodes.updateEpisodesTable();
      NextEpisodes.createNextEpisodes();
      NextEpisodes.show();
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }
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
      er.setSubs(rec[Episodes.SUBS_COLUMN].equals(Subtitle.NONE) ? EpisodesRecord.NO_SUBS
              : rec[Episodes.SUBS_COLUMN].equals(myseries.MySeries.languages.getSecondary().getName()) ? EpisodesRecord.SEC_SUB
              : rec[Episodes.SUBS_COLUMN].equals(myseries.MySeries.languages.getPrimary().getName()) ? EpisodesRecord.PRIM_SUB
              : rec[Episodes.SUBS_COLUMN].equals(Subtitle.BOTH) ? EpisodesRecord.BOTH_SUBS
              : EpisodesRecord.UNKNOWN_SUB);
      er.setSeen((Boolean)rec[Episodes.SEEN_COLUMN]? EpisodesRecord.SEEN : EpisodesRecord.NOT_SEEN);
      er.save();
     // NextEpisodes.createNextEpisodes();
     // NextEpisodes.show();
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);


    } catch (NumberFormatException ex) {
      MyMessages.error("Not a number", "The value you entered is not a number");

    }

  }
}
