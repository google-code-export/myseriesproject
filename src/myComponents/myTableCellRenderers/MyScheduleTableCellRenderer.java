/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import com.googlecode.scheduler.SchedulerCellRenderer;
import com.googlecode.scheduler.SchedulerMouseListener;
import java.awt.Component;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JTable;
import myseries.schedule.ScheduleDayPanel;

/**
 *
 * @author ssoldatos
 */
public class MyScheduleTableCellRenderer extends SchedulerCellRenderer {

  public ScheduleDayPanel panel;
  private int cellHeight;
  private int cellWidth;

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    JLabel dayLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    this.cellHeight = table.getRowHeight();
    this.cellWidth = table.getColumnModel().getColumn(column).getWidth();
    panel = new ScheduleDayPanel(value, dayLabel, cellHeight, cellWidth);
    return panel;
  }

  

}
