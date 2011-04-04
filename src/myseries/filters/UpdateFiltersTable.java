/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.filters;

import database.EpisodesRecord;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import tools.languages.Language;
import myComponents.MyUsefulFunctions;
/**
 *
 * @author lordovol
 */
public class UpdateFiltersTable {

  private TableModel model;

  public UpdateFiltersTable(TableModelEvent e) {
    if (e.getType() == TableModelEvent.UPDATE) {
      int row = e.getFirstRow();
      model = (TableModel) e.getSource();
      if (model.getRowCount() > row) {
        EpisodesRecord ep = (EpisodesRecord) model.getValueAt(row, Filters.EPISODERECORD_COLUMN);
        Boolean downloaded = (Boolean) model.getValueAt(row, Filters.DOWNLOADED_COLUMN);
        Language subs = (Language) model.getValueAt(row, Filters.SUBS_COLUMN);
        Boolean seen = (Boolean) model.getValueAt(row, Filters.SEEN_COLUMN);
        try {
          ep.setDownloaded(downloaded ? EpisodesRecord.DOWNLOADED : EpisodesRecord.NOT_DOWNLOADED);
          ep.setSeen(seen ? EpisodesRecord.SEEN : EpisodesRecord.NOT_SEEN);
          ep.setSubs(subs);
          ep.save();
         // NextEpisodes.createNextEpisodes();
         // NextEpisodes.show();
          Thread.sleep(100);
          Filters.getFilteredSeries();
        } catch (SQLException ex) {
          MyUsefulFunctions.log(Level.WARNING, null, ex);
        } catch (InterruptedException ex) {
          MyUsefulFunctions.log(Level.SEVERE, null, ex);
        }
      }
    }
  }
}
