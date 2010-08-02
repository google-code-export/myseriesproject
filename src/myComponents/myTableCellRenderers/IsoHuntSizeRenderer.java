/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myComponents.myTableCellRenderers;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import myComponents.MyUsefulFunctions;

/**
 *
 * @author ssoldatos
 */
  public class IsoHuntSizeRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 23543643523654L;

    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      long v = (Long)value;
      String strValue = MyUsefulFunctions.createFileSize(v);


      setText(strValue);
      setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
      setHorizontalAlignment(SwingConstants.RIGHT);
      return super.getTableCellRendererComponent(table, strValue, isSelected, hasFocus, row, column);
    }
  }
