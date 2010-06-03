/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myComponents.myTableCellRenderers;

import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author ssoldatos
 */
  public class IsoHuntSizeRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 23543643523654L;

    private final DecimalFormat formatter = new DecimalFormat("#.00");

    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      long v = (Long)value;
      String strValue = String.valueOf(value);
      if(v<1024){
        strValue = strValue + " bytes";
      } else if (v < Math.pow(1024l,2)){
        strValue = formatter.format(v/1024) + " KB";
      } else if (v < Math.pow(1024l,3)){
        strValue = formatter.format(v/Math.pow(1024,2)) + " MB";
      } else if (v < Math.pow(1024l,4)){
        strValue = formatter.format(v/Math.pow(1024,3)) + " GB";
      } else if (v < Math.pow(1024l,5)){
        strValue = formatter.format(v/Math.pow(1024,4)) + " TB";
      }

      setText(strValue);
      setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
      setHorizontalAlignment(SwingConstants.RIGHT);
      return this;
    }
  }
