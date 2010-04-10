/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.series;

import database.SeriesRecord;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import myseries.MySeries;
import myseries.episodes.NextEpisodes;

/**
 *
 * @author lordovol
 */
public class UpdateSeriesTable {

  private TableModel model;
  private String rec[] = new String[Series.NUMBER_OF_COLUMS];

  public UpdateSeriesTable(TableModelEvent e) {
    if (e.getType() == TableModelEvent.UPDATE) {
      int row = e.getFirstRow();
      model = (TableModel) e.getSource();
      for (int i = 0; i < Series.NUMBER_OF_COLUMS; i++) {
        rec[i] = String.valueOf(model.getValueAt(row, i));
      }
      try {
        updateSeries(rec);
      } catch (SQLException ex) {
        MySeries.logger.log(Level.SEVERE, null, ex);
      }
    } else if (e.getType() == -1) {
      //workaround to update the screenshot position when series are added /deleted
      if (MySeries.splitPane_main.getDividerLocation() % 2 == 0) {
        MySeries.splitPane_main.setDividerLocation(MySeries.splitPane_main.getDividerLocation() + 1);
      } else {
        MySeries.splitPane_main.setDividerLocation(MySeries.splitPane_main.getDividerLocation() - 1);
      }
    }
  }

  private void updateSeries(String[] rec) throws SQLException {
    SeriesRecord ser = SeriesRecord.getSeriesByID(Integer.parseInt(rec[1]));
    ser.setHidden(rec[Series.HIDDEN_COLUMN].equals("true") ? SeriesRecord.HIDDEN : SeriesRecord.NOT_HIDDEN);
    ser.setInternetUpdate(rec[Series.UPDATE_COLUMN].equals("true") ? SeriesRecord.INTERNET_UPDATE : SeriesRecord.NO_INTERNET_UPDATE);
    ser.save();
    NextEpisodes.createNextEpisodes();
    NextEpisodes.show();
  }
}
