/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myComponents;

import database.EpisodesRecord;
import database.SeriesRecord;
import java.awt.Component;
import java.awt.Font;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import myseries.episodes.Episodes;
import myseries.series.Series;

/**
 *
 * @author lordovol
 */
public class MyTitleCellRenderer extends DefaultTableCellRenderer {
private static final long serialVersionUID = 3456463456344572L;

  @Override
  public Component getTableCellRendererComponent(JTable table,
      Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    ArrayList<EpisodesRecord> ep;
    if (value instanceof String) {
     // SeriesRecord s = Series.getCurrentSerial();
      //try {
//        ep =  Episodes.getCurrentSeriesEpisodes();
//        for (Iterator<EpisodesRecord> it = ep.iterator(); it.hasNext();) {
//          EpisodesRecord episodesRecord = it.next();
//          if(episodesRecord.getTitle().equals((String) value) &&
//                  episodesRecord.getDownloaded() == 1 &&
//                  episodesRecord.getSubs() != 0 &&
//                  episodesRecord.getSeen() == 0){
//            this.setFont(this.getFont().deriveFont(Font.BOLD));
//          }
//        }
   //   } catch (SQLException ex) {
    //    Logger.getLogger(MyTitleCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
    //  }
      Boolean d =(Boolean) table.getModel().getValueAt(row, 3);
      Boolean s =(Boolean) table.getModel().getValueAt(row, 5);
      String sub = (String)table.getModel().getValueAt(row, 4);
      if(d && !s && !sub.equals("None")){
        this.setFont(this.getFont().deriveFont(Font.BOLD));
      }
      this.setText((String)value);
    }

    return this;
  }
}
