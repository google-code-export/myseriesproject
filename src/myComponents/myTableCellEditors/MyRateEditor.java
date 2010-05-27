/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellEditors;

import com.googlecode.starrating.StarTableCellEditor;
import database.EpisodesRecord;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.JTable;
import myseries.episodes.Episodes;

/**
 *
 * @author lordovol
 */
public class MyRateEditor extends StarTableCellEditor {

  private Boolean seen;

  public MyRateEditor(boolean b) {
    super(b);
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    rating.setRate(Double.parseDouble(String.valueOf(value)));
    seen = (Boolean) table.getModel().getValueAt(row, Episodes.SEEN_COLUMN);
    //if (!seen) {
    //  rating.setRatingEnabled(false);
    // }
    return rating;
  }

  @Override
  public boolean isCellEditable(EventObject e) {
    seen = false;
    if (e instanceof MouseEvent) {
      MouseEvent event = (MouseEvent) e;
      Point point = event.getPoint();
      JTable table = (JTable) e.getSource();
      int row = table.rowAtPoint(point);
      seen = (Boolean) table.getModel().getValueAt(row, Episodes.SEEN_COLUMN);
    }

    if (!seen) {
      return false;
    } else {
      return super.isCellEditable(e);
    }
  }
}
