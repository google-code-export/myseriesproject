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
      for (int i = 0; i < 7; i++) {
        rec[i] = String.valueOf(model.getValueAt(row, i));
      }
      updateEpisode(rec);
    }
  }

  private void updateEpisode(int row) {
    for (int i = 0; i < 7; i++) {
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
      er.setEpisode(Integer.parseInt(rec[0]));
      er.setTitle(rec[1]);
      if (!rec[2].equals("")) {
        er.setAired(rec[2]);
      }
      er.setDownloaded(rec[3].equals("true") ? EpisodesRecord.DOWNLOADED : EpisodesRecord.NOT_DOWNLOADED);
      er.setSubs(rec[4].equals("None") ? EpisodesRecord.NO_SUBS
              : rec[4].equals("English") ? EpisodesRecord.PRIM_SUB
              : rec[4].equals("Greek") ? EpisodesRecord.SEC_SUB
              : rec[4].equals("Both") ? EpisodesRecord.BOTH_SUBS
              : EpisodesRecord.UNKNOWN_SUB);
      er.setSeen(rec[5].equals("true") ? EpisodesRecord.SEEN : EpisodesRecord.NOT_SEEN);
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
