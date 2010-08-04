/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellEditors;

import database.EpisodesRecord;
import database.SeriesRecord;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import myComponents.MyUsefulFunctions;
import myseries.series.Series;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class MyWatchedCellEditor extends AbstractCellEditor implements TableCellEditor {

  public static final long serialVersionUID = 2352352525L;
  private JCheckBox cb = new JCheckBox();
  private final int episodeColumn;

  public MyWatchedCellEditor(int episodeColumn) {
    this.episodeColumn = episodeColumn;
     cb.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseExited(MouseEvent e) {
        stopCellEditing();
      }

    });
  }

  @Override
  public boolean isCellEditable(EventObject e) {
    MouseEvent me;
    if (!(e instanceof MouseEvent)) {
      return false;
    } else {
      me = (MouseEvent) e;
    }
    EpisodesRecord ep = new EpisodesRecord();
    if (e.getSource() instanceof JTable) {
      JTable table = (JTable) e.getSource();
      int row = table.rowAtPoint(((MouseEvent) e).getPoint());
      ep = (EpisodesRecord) table.getValueAt(row, episodeColumn);
    }
    if (!MyUsefulFunctions.hasBeenAired(ep.getAired())) {
      return false;
    }
    return true;
  }

  public Object getCellEditorValue() {
    return (Boolean) cb.isSelected();
  }

  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    cb.setHorizontalAlignment(SwingConstants.CENTER);
    return cb;
  }
}
