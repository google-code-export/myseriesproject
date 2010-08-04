/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myComponents.myTableCellEditors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import myseries.series.Series;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class MyDownloadedCellEditor extends AbstractCellEditor implements TableCellEditor {
  public static final long serialVersionUID = 2352352525L;
  private JCheckBox cb = new JCheckBox();

  public MyDownloadedCellEditor() {
    cb.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        stopCellEditing();
      }
    });
  }



   @Override
  public boolean isCellEditable(EventObject e) {
   if(Series.getCurrentSerial().isValidLocalDir() && Options.toBoolean(Options.AUTO_FILE_UPDATING)){
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
