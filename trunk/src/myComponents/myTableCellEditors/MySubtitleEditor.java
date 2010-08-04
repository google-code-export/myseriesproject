/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellEditors;

import database.SeriesRecord;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import myComponents.MyUsefulFunctions;
import myComponents.myTableCellRenderers.MySubtitleListRenderer;
import myseries.series.Series;
import tools.languages.LangsList;
import tools.languages.Language;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class MySubtitleEditor extends AbstractCellEditor implements TableCellEditor {

  public static final long serialVersionUID = 234534634646L;
  JComboBox combo = new JComboBox();

  public MySubtitleEditor() {
    Language[] subStatuses = {
      LangsList.NONE,
      myseries.MySeries.languages.getPrimary(),
      myseries.MySeries.languages.getSecondary(),
      LangsList.MULTIPLE};
      combo.setModel(new DefaultComboBoxModel(subStatuses));
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    combo.setSelectedItem(value);
    combo.setRenderer(new MySubtitleListRenderer());
    combo.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        stopCellEditing();
      }
    });
    
    return combo;
  }

  @Override
  public boolean isCellEditable(EventObject e) {
    SeriesRecord ser = Series.getCurrentSerial();
    if(!ser.isValidLocalDir() || !Options.toBoolean(Options.AUTO_FILE_UPDATING)){
      return true;
    }
    return false;
  }



  @Override
   public Language getCellEditorValue() {
    return (Language) combo.getSelectedItem();
  }

  @Override
  public boolean stopCellEditing() {

    return super.stopCellEditing();
  }


}
