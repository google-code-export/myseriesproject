/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellEditors;

import com.toedter.calendar.JDateChooser;
import database.EpisodesRecord;
import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import tools.MySeriesLogger;
import tools.options.Options;

/**
 * The cell editor for the datechooser
 * @author lordovol
 */
public class MyJDateChooserCellEditor extends AbstractCellEditor implements TableCellEditor {

    private static final long serialVersionUID = 917881575221755609L;
    /**
     * The date chooser
     */
    private JDateChooser dateChooser = new JDateChooser();
    /**
     * The date format
     */
    private SimpleDateFormat f = new SimpleDateFormat(Options.toString(Options.DATE_FORMAT));

    /**
     * Get the cell component
     * @param table The cells table
     * @param value The cells value
     * @param isSelected If the cell is selected
     * @param row The row number
     * @param column The column number
     * @return The date Chooser object
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {

        Date date = null;
        if (value instanceof Date) {
            date = (Date) value;
        } else if (value instanceof String) {
            SimpleDateFormat sdf = new SimpleDateFormat(EpisodesRecord.MYSQL_DATE_FORMAT);
            try {
                date = sdf.parse((String) value);
            } catch (ParseException ex) {
                sdf = new SimpleDateFormat(Options.toString(Options.DATE_FORMAT));
                try {
                    date = sdf.parse((String) value);
                } catch (ParseException ex1) {
                    date = null;
                }
            }
        }

        dateChooser.setDate(date);
        dateChooser.setDateFormatString(Options.toString(Options.DATE_FORMAT));
        return dateChooser;
    }

    /**
     * Get the value of the cell
     * @return The cells value
     */
    public String getCellEditorValue() {
        try {
            return f.format(dateChooser.getDate());
        } catch (NullPointerException ex) {
            MySeriesLogger.logger.log(Level.WARNING, "Null date chooser");
            return "";
        }
    }
}
