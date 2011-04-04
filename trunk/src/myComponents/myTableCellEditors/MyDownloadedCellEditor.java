/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellEditors;

import database.EpisodesRecord;
import database.SeriesRecord;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import myComponents.MyUsefulFunctions;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class MyDownloadedCellEditor extends DefaultCellEditor implements TableCellEditor {

    public static final long serialVersionUID = 2352352525L;
    private JCheckBox cb = new JCheckBox();
    private final int episodeColumn;

    public MyDownloadedCellEditor(int episodeColumn) {
        super(new JCheckBox());
        this.episodeColumn = episodeColumn;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        MouseEvent me;
        if (!(e instanceof MouseEvent)) {
            return false;
        } else {
            me = (MouseEvent) e;
        }
        SeriesRecord series = new SeriesRecord();
        EpisodesRecord ep = new EpisodesRecord();
        if (e.getSource() instanceof JTable) {
            JTable table = (JTable) e.getSource();
            int row = table.rowAtPoint(((MouseEvent) e).getPoint());
            ep = (EpisodesRecord) table.getValueAt(row, episodeColumn);
            int sid = ep.getSeries_ID();
            try {
                series = database.DBHelper.getSeriesByID(sid);
            } catch (SQLException ex) {
                return true;
            }
        }
        if (series.isValidLocalDir() && Options.toBoolean(Options.AUTO_FILE_UPDATING) && !MyUsefulFunctions.isNetworkPath(new File(series.getLocalDir()))) {
            return false;
        }
        if (!MyUsefulFunctions.hasBeenAired(ep.getAired(), true)) {
            return false;
        }
        return true;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        super.getTableCellEditorComponent(table, value, isSelected, row, column);
        ((JCheckBox) editorComponent).setHorizontalAlignment(SwingConstants.CENTER);
        return editorComponent;
    }
}
