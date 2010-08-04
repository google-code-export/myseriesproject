/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellEditors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import myComponents.myTableCellRenderers.MySubtitleListRenderer;
import tools.languages.LangsList;
import tools.languages.Language;

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
    DefaultComboBoxModel model = (DefaultComboBoxModel) combo.getModel();
    if(model.getIndexOf(value) == -1){
     model.addElement(value); 
    }
    combo.setSelectedItem(value);
    combo.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        stopCellEditing();
      }
    });
    combo.setRenderer(new MySubtitleListRenderer());
    return combo;
  }

  @Override
   public Language getCellEditorValue() {
    return (Language) combo.getSelectedItem();
  }
}
