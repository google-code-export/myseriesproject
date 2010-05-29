/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author lordovol
 */
public class MyDecimalFormatRenderer extends DefaultTableCellRenderer {

  public static final long serialVersionUID = 14232533456L;
  private static final DecimalFormat formatter = new DecimalFormat("#.00");

  @Override
  public Component getTableCellRendererComponent(
          JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    value = formatter.format(value);
    setHorizontalAlignment(SwingConstants.RIGHT);
    return super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);
  }
}
