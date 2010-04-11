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

/**
 *
 * @author lordovol
 */
public class UpdateSeriesTable {

  private TableModel model;
  private Object rec[] = new Object[Series.NUMBER_OF_COLUMS];

  public UpdateSeriesTable(TableModelEvent e) {
    if (e.getType() == TableModelEvent.UPDATE) {
      int row = e.getFirstRow();
      model = (TableModel) e.getSource();
      for (int i = 0; i < Series.NUMBER_OF_COLUMS; i++) {
        rec[i] = model.getValueAt(row, i);
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

  private void updateSeries(Object[] rec) throws SQLException {
    SeriesRecord ser = (SeriesRecord) rec[Series.SERIESRECORD_COLUMN];
    ser.setHidden((Boolean)rec[Series.HIDDEN_COLUMN] ? SeriesRecord.HIDDEN : SeriesRecord.NOT_HIDDEN);
    ser.setInternetUpdate((Boolean)rec[Series.UPDATE_COLUMN] ? SeriesRecord.INTERNET_UPDATE : SeriesRecord.NO_INTERNET_UPDATE);
    ser.save();
    //NextEpisodes.createNextEpisodes();
    //NextEpisodes.show();
  }
}
