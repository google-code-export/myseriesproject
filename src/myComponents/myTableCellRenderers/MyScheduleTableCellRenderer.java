/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import com.googlecode.scheduler.ScheduleDay;
import com.googlecode.scheduler.SchedulerCellRenderer;
import java.awt.Component;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import myseries.schedule.ScheduleDayPanel;
import tools.Skin;

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
    dayLabel.setOpaque(false);
    dayLabel.setBorder(BorderFactory.createEmptyBorder());
    this.cellHeight = table.getRowHeight();
    this.cellWidth = table.getColumnModel().getColumn(column).getWidth();
    panel = new ScheduleDayPanel(value, dayLabel, cellHeight, cellWidth);
    panel.setBorder(BorderFactory.createEmptyBorder());
    if (isSelected) {
      panel.setBackground(Skin.getSkinColor());
      dayLabel.setForeground(Skin.getColor_5());
    } else {
      panel.setBackground(Skin.getColor_1());
      dayLabel.setForeground(dayLabel.getForeground());
    }
    if (value instanceof ScheduleDay) {
      ScheduleDay s = (ScheduleDay) value;
      if (Calendar.getInstance().get(Calendar.DATE) == s.getDay()) {
        panel.setBackground(Skin.getColor_5());
        dayLabel.setForeground(Skin.getColor_1());
      }
    }

    return panel;
  }
}
