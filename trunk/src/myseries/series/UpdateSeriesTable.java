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
import myComponents.MyUsefulFunctions;
import tools.MySeriesLogger;

/**
 *
 * @author lordovol
 */
public class UpdateSeriesTable {

    private TableModel model;
    private Object rec[] = new Object[Series.NUMBER_OF_COLUMS];

    public UpdateSeriesTable(TableModelEvent e, MySeries m) {
        if (e.getType() == TableModelEvent.UPDATE) {
            MySeriesLogger.logger.log(Level.INFO, "Update series table event");
            int row = e.getFirstRow();
            model = (TableModel) e.getSource();
            for (int i = 0; i < Series.NUMBER_OF_COLUMS; i++) {
                rec[i] = model.getValueAt(row, i);
            }
            try {
                updateSeries(rec);
            } catch (SQLException ex) {
                MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
            }
        } else if (e.getType() == -1) {
            //workaround to update the screenshot position when series are added /deleted
            if (m.splitPane_main.getDividerLocation() % 2 == 0) {
                m.splitPane_main.setDividerLocation(m.splitPane_main.getDividerLocation() + 1);
            } else {
                m.splitPane_main.setDividerLocation(m.splitPane_main.getDividerLocation() - 1);
            }
        }
    }

    private void updateSeries(Object[] rec) throws SQLException {
        SeriesRecord ser = (SeriesRecord) rec[Series.SERIESRECORD_COLUMN];
        ser.setHidden((Boolean) rec[Series.HIDDEN_COLUMN] ? SeriesRecord.HIDDEN : SeriesRecord.NOT_HIDDEN);
        ser.setInternetUpdate((Boolean) rec[Series.UPDATE_COLUMN] ? SeriesRecord.INTERNET_UPDATE : SeriesRecord.NO_INTERNET_UPDATE);
        MySeriesLogger.logger.log(Level.INFO, "Updating series {0}", ser.getFullTitle());
        ser.save();
        MySeriesLogger.logger.log(Level.FINE, "Series updated");
        //NextEpisodes.createNextEpisodes();

        //NextEpisodes.show();
    }
}
