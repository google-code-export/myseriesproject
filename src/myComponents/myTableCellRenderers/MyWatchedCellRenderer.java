/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import database.EpisodesRecord;
import java.awt.Component;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import myComponents.MyUsefulFunctions;

/**
 *
 * @author ssoldatos
 */
public class MyWatchedCellRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = 7897867559L;
  public static final int IMAGE_WIDTH = 16;
  public static final int IMAGE_HEIGHT = 16;
  private final int watchedColumn;

  public MyWatchedCellRenderer(int watchedColumn) {
    this.watchedColumn = watchedColumn;
  }

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

  private Icon createIcon(Boolean watched) {
    if(!watched){
      return null;
    }
    ImageIcon im = new ImageIcon(getClass().getResource("/images/tick.png"));
    return im;
  }
}
