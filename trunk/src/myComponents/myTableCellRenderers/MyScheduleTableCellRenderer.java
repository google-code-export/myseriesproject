/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import com.googlecode.scheduler.ScheduleDay;
import com.googlecode.scheduler.SchedulerCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Calendar;
import java.util.Date;
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
    private Color selBack = Skin.getColor_5();
    private Color selFore = Skin.getColor_1();
    private Color plainBack = Skin.getColor_1();
    private Color plainFore = Skin.getColor_5();
    private Color nowBack = Skin.getSkinColor();
    private Color nowFore = Skin.getColor_5();
    private Color weekendBack = Skin.getColor_4();
    private Color weekendFore = Skin.getColor_2();

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
                        dayLabel.setForeground(Skin.getColor_5());
                        dayLabel.setFont(dayLabel.getFont().deriveFont(Font.BOLD,dayLabel.getFont().getSize()+2));
                    } else {
                        panel.setBackground(plainBack);
                        dayLabel.setForeground(plainFore);
                    }
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
