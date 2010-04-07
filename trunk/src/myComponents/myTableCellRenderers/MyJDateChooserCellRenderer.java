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
public class MyJDateChooserCellRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = 23556474572L;

  @Override
  public Component getTableCellRendererComponent(JTable table,
      Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    if (value instanceof String) {
      String strDate = MyUsefulFunctions.convertDateForRendering((String) value);
      this.setText(strDate);
    }

    return this;
  }
}
