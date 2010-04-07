/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import myComponents.MyUsefulFunctions;

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
    if (value instanceof String) {
      Boolean downloaded = (Boolean) table.getModel().getValueAt(row, 3);
      Boolean seen = (Boolean) table.getModel().getValueAt(row, 5);
      String sub = (String) table.getModel().getValueAt(row, 4);
      this.setFont(MyUsefulFunctions.getCellFont(this.getFont(),downloaded,seen,sub));
      this.setForeground(MyUsefulFunctions.getCellColor(isSelected,seen));
      this.setText((String) value);
    }

    return this;
  }
}
