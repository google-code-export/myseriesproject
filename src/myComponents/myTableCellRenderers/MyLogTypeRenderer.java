/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import sdialogs.Info;

/**
 *
 * @author ssoldatos
 */
public class MyLogTypeRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = 23543643523654L;
  public static final String WARNING_ICON = "warning_small";
  public static final String ERROR_ICON = "error_small";
  public static final String INFO_ICON = "info_small";

  @Override
  public Component getTableCellRendererComponent(
          JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
    String image=null;
    if(value instanceof Integer){
      int val =(Integer) value;
      switch(val){
        case Info.WARNING_MESS:
          image = WARNING_ICON;
          break;
        case Info.ERROR_MESS:
          image = ERROR_ICON;
          break;
        case Info.INFO_MESS:
          image = INFO_ICON;
          break;
      }
      setIcon(new ImageIcon(getClass().getResource("/images/"+image+".png")));
    
    }
    setText("");
    setHorizontalAlignment(SwingConstants.CENTER);
    setVerticalAlignment(SwingConstants.CENTER);
    return this;
  }
}
