/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import com.googlecode.scheduler.ScheduleDay;
import com.googlecode.scheduler.SchedulerCellRenderer;
import java.awt.Color;
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
  private Color disabledBack = Color.LIGHT_GRAY;
  private Color selBack = Skin.getSkinColor();
  private Color selFore = Skin.getColor_5();
  private Color plainBack = Skin.getColor_1();
  private Color plainFore = Skin.getColor_5();
  private Color nowBack = Skin.getColor_5();
  private Color nowFore = Skin.getColor_1();

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    JLabel dayLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    dayLabel.setOpaque(false);
    dayLabel.setBorder(BorderFactory.createEmptyBorder());
    this.cellHeight = table.getRowHeight();
    this.cellWidth = table.getColumnModel().getColumn(column).getWidth();
    panel = new ScheduleDayPanel(value, dayLabel, cellHeight, cellWidth);
    panel.setBorder(BorderFactory.createEmptyBorder());
    //panel.setBackground(plainBack);
    //dayLabel.setForeground(plainFore);
    if (value instanceof ScheduleDay) {
      ScheduleDay s = (ScheduleDay) value;
      if (isToday(s)) {
        panel.setBackground(nowBack);
        dayLabel.setForeground(nowFore);
      } else {
        if (isSelected) {
          panel.setBackground(selBack);
          dayLabel.setForeground(selFore);
        } else {
          panel.setBackground(plainBack);
          dayLabel.setForeground(plainFore);
        }
      }

    } else {
      panel.setBackground(disabledBack);
    }

    return panel;
  }

  private boolean isToday(ScheduleDay s) {
    return Calendar.getInstance().get(Calendar.DATE) == s.getDay()
            && Calendar.getInstance().get(Calendar.MONTH) + 1 == s.getMonth()
            && Calendar.getInstance().get(Calendar.YEAR) == s.getYear();
  }
}
