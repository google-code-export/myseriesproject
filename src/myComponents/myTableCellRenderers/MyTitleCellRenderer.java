/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myComponents;

import database.EpisodesRecord;
import database.SeriesRecord;
import java.awt.Color;
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
import tools.Skin;

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
      Boolean downloaded =(Boolean) table.getModel().getValueAt(row, 3);
      Boolean seen =(Boolean) table.getModel().getValueAt(row, 5);
      String sub = (String)table.getModel().getValueAt(row, 4);
      if(seen){
        this.setFont(this.getFont().deriveFont(Font.ITALIC));
        this.setForeground(Skin.getColor_4());
      }
      if(downloaded && !seen && !sub.equals("None")){
        this.setFont(this.getFont().deriveFont(Font.BOLD));
        this.setForeground(Skin.getColor_5());
      }
      if(isSelected){
        this.setForeground(Skin.getColor_1());
      }
      this.setText((String)value);
    }

    return this;
  }
}
