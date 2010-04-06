/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import database.EpisodesRecord;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
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
      Boolean downloaded = (Boolean) table.getModel().getValueAt(row, 3);
      Boolean seen = (Boolean) table.getModel().getValueAt(row, 5);
      String sub = (String) table.getModel().getValueAt(row, 4);
      if (seen) {
        this.setFont(this.getFont().deriveFont(Font.ITALIC));
      }
      if (downloaded && !seen && !sub.equals("None")) {
        this.setFont(this.getFont().deriveFont(Font.BOLD));
      }
      if (isSelected) {
        this.setForeground(Skin.getColor_1());
      } else {
        if (seen) {
          this.setForeground(Skin.getColor_4());
        }
        if (downloaded && !seen && !sub.equals("None")) {
          this.setForeground(Skin.getColor_5());
        }
      }
      this.setText((String) value);
    }

    return this;
  }
}
