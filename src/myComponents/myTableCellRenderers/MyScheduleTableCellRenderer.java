/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import com.googlecode.scheduler.ScheduleDay;
import com.googlecode.scheduler.SchedulerCellRenderer;
import database.DBHelper;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import myseries.schedule.ScheduleEvent;
import myseries.schedule.scheduleDayPanel;

/**
 *
 * @author ssoldatos
 */
public class MyScheduleTableCellRenderer extends SchedulerCellRenderer {

  public scheduleDayPanel panel;
  private int cellHeight;
  private int cellWidth;

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    JLabel dayLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    this.cellHeight = table.getRowHeight();
    this.cellWidth = table.getColumnModel().getColumn(column).getWidth();
    panel = new scheduleDayPanel(value, dayLabel, cellHeight, cellWidth);
    return panel;
  }
}
