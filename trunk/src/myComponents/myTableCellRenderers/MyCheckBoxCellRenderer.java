/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author ssoldatos
 */
public class MyCheckBoxCellRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = 34342424243L;
  public static final int IMAGE_WIDTH = 16;
  public static final int IMAGE_HEIGHT = 16;

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    if (value instanceof Boolean) {
      Boolean val = (Boolean) value;
      setText("");
      setIcon(createIcon(val));
    }
    //setHorizontalAlignment(SwingConstants.CENTER);
    setHorizontalAlignment(SwingConstants.CENTER);
    return this;
  }

  private Icon createIcon(Boolean checked) {
    if (!checked) {
      ImageIcon im = new ImageIcon(getClass().getResource("/images/notick.png"));
      return im;
    }
    ImageIcon im = new ImageIcon(getClass().getResource("/images/tick.png"));
    return im;
  }
}
