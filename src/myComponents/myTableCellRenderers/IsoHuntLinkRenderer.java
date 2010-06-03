/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author ssoldatos
 */
public class IsoHuntLinkRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = 23543643523654L;
  private final DecimalFormat formatter = new DecimalFormat("#.00");

  @Override
  public Component getTableCellRendererComponent(
          JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    setIcon(new ImageIcon(getClass().getResource("/images/download.png")));
    setText("");
    setBorder(BorderFactory.createLineBorder(Color.GRAY));
    setCursor(new Cursor(Cursor.HAND_CURSOR));
    setHorizontalAlignment(SwingConstants.CENTER);
    setVerticalAlignment(SwingConstants.CENTER);
    return super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
  }
}
