/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import com.googlecode.scheduler.ScheduleDay;
import com.googlecode.scheduler.SchedulerCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import myseriesproject.schedule.ScheduleDayPanel;
import tools.Skin;

/**
 *
 * @author ssoldatos
 */
public class MyScheduleTableCellRenderer extends SchedulerCellRenderer {

    public ScheduleDayPanel panel;
    private int cellHeight;
    private int cellWidth;
    private Color disabledBack = UIManager.getColor("Panel.background");
    private Color selBack = UIManager.getColor("TableHeader.background");
    private Color selFore = UIManager.getColor("TableHeader.foreground");
    private Color plainBack = null;
    private Color plainFore = null;
    private Color nowBack = UIManager.getColor("Table.focusCellBackground");
    private Color nowFore = UIManager.getColor("Table.focusCellForeground");
    private Color weekendBack = new ColorUIResource(Color.DARK_GRAY);
    private Color weekendFore =new ColorUIResource(Color.WHITE);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel dayLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        dayLabel.setOpaque(false);
        dayLabel.setBorder(BorderFactory.createEmptyBorder());
        dayLabel.setHorizontalTextPosition(LEFT);
        this.cellHeight = table.getRowHeight();
        this.cellWidth = table.getColumnModel().getColumn(column).getWidth();
        panel = new ScheduleDayPanel(value, dayLabel, cellHeight, cellWidth);
        panel.setBorder(BorderFactory.createEmptyBorder());

        //panel.setBackground(plainBack);
        //dayLabel.setForeground(plainFore);
        if (value instanceof ScheduleDay) {
            ScheduleDay s = (ScheduleDay) value;
            Date date = s.getDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int day = cal.get(Calendar.DAY_OF_WEEK);

            if (isSelected) {
                panel.setBackground(selBack);
                dayLabel.setForeground(selFore);
            } else {
                if (isToday(s)) {
                    //dayLabel.setForeground(nowFore);
                    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    panel.setBackground(nowBack);
                } else {
                    if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
                        dayLabel.setForeground(Color.RED);
                        dayLabel.setFont(dayLabel.getFont().deriveFont(Font.ITALIC,dayLabel.getFont().getSize()+2));
                    } else {
                        panel.setBackground(plainBack);
                        dayLabel.setForeground(plainFore);
                    }
                }
            }
            panel.setOpaque(true);
        } else {
            //panel.setBackground(disabledBack);
            panel.setOpaque(false);
        }
        
        return panel;
    }

    private boolean isToday(ScheduleDay s) {
        return Calendar.getInstance().get(Calendar.DATE) == s.getDay()
                && Calendar.getInstance().get(Calendar.MONTH) + 1 == s.getMonth()
                && Calendar.getInstance().get(Calendar.YEAR) == s.getYear();
    }
}
