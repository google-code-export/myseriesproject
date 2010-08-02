/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import database.EpisodesRecord;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import myComponents.MyUsefulFunctions;
import myseries.episodes.Episodes;
import tools.languages.Language;

/**
 * The cell renderer for the tile field
 * @author lordovol
 */
public class MyTitleCellRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = 3456463456344572L;

  @Override
  public Component getTableCellRendererComponent(JTable table,
          Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    if (value instanceof EpisodesRecord) {
      Boolean downloaded = (Boolean) table.getModel().getValueAt(row, Episodes.DOWNLOADED_COLUMN);
      Boolean seen = (Boolean) table.getModel().getValueAt(row, Episodes.SEEN_COLUMN);
      Language sub = (Language) table.getModel().getValueAt(row, Episodes.SUBS_COLUMN);
      this.setFont(MyUsefulFunctions.getCellFont(this.getFont(),downloaded,seen,sub));
      this.setForeground(MyUsefulFunctions.getCellColor(isSelected,seen));
      //String size = MyUsefulFunctions.getVideoFileSize((EpisodesRecord)value);
      String size = "";
      this.setText(value.toString() + size);
    }

    return this;
  }
}
