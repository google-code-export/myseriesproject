/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellEditors;

import database.EpisodesRecord;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import myComponents.MyUsefulFunctions;

/**
 *
 * @author lordovol
 */
public class MyWatchedCellEditor extends DefaultCellEditor implements TableCellEditor {

  public static final long serialVersionUID = 2352352525L;
  private final int episodeColumn;

  public MyWatchedCellEditor(int episodeColumn) {
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
    EpisodesRecord ep = new EpisodesRecord();
    if (e.getSource() instanceof JTable) {
      JTable table = (JTable) e.getSource();
      int row = table.rowAtPoint(((MouseEvent) e).getPoint());
      ep = (EpisodesRecord) table.getValueAt(row, episodeColumn);
    }
    if (!MyUsefulFunctions.hasBeenAired(ep.getAired(),true)) {
      return false;
    }
    return true;
  }

  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    super.getTableCellEditorComponent(table,value,isSelected,row,column);
    ((JCheckBox)editorComponent).setHorizontalAlignment(SwingConstants.CENTER);
    return editorComponent;
  }
}
