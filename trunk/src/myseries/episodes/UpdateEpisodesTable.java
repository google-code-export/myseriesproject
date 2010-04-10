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

/**
 * Updates the episodes table
 * @author lordovol
 */
public class UpdateEpisodesTable {

  String rec[] = new String[Episodes.NUMBER_OF_COLUMS];
  private TableModel model;

  public UpdateEpisodesTable(TableModelEvent e) {
    if (e.getType() == TableModelEvent.UPDATE) {
      int row = e.getFirstRow();
      model = (TableModel) e.getSource();
      for (int i = 0; i < Episodes.NUMBER_OF_COLUMS; i++) {
        rec[i] = String.valueOf(model.getValueAt(row, i));
      }
      updateEpisode(rec);
    }
  }

  private void updateEpisode(int row) {
    for (int i = 0; i < model.getColumnCount(); i++) {
      rec[i] = String.valueOf(model.getValueAt(row, i));
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

  private void updateEpisode(String[] rec) {
    try {
      EpisodesRecord er = EpisodesRecord.getEpisodeByID(Integer.parseInt(rec[6]));
      er.setEpisode(Integer.parseInt(rec[Episodes.EPISODE_NUM_COLUMN]));
      er.setTitle(rec[Episodes.TITLE_COLUMN]);
      if (!rec[Episodes.AIRED_COLUMN].equals("")) {
        er.setAired(rec[Episodes.AIRED_COLUMN]);
      }
      er.setDownloaded(rec[Episodes.DOWNLOADED_COLUMN].equals("true") ? EpisodesRecord.DOWNLOADED : EpisodesRecord.NOT_DOWNLOADED);
      er.setSubs(rec[Episodes.SUBS_COLUMN].equals("None") ? EpisodesRecord.NO_SUBS
              : rec[Episodes.SUBS_COLUMN].equals("English") ? EpisodesRecord.PRIM_SUB
              : rec[Episodes.SUBS_COLUMN].equals("Greek") ? EpisodesRecord.SEC_SUB
              : rec[Episodes.SUBS_COLUMN].equals("Both") ? EpisodesRecord.BOTH_SUBS
              : EpisodesRecord.UNKNOWN_SUB);
      er.setSeen(rec[Episodes.SEEN_COLUMN].equals("true") ? EpisodesRecord.SEEN : EpisodesRecord.NOT_SEEN);
      er.save();
      NextEpisodes.createNextEpisodes();
      NextEpisodes.show();
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);


    } catch (NumberFormatException ex) {
      MyMessages.error("Not a number", "The value you entered is not a number");

    }

  }
}
