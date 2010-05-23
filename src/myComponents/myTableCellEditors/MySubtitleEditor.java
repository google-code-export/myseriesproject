/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellEditors;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import tools.languages.Language;

/**
 *
 * @author lordovol
 */
public class MySubtitleEditor extends AbstractCellEditor implements TableCellEditor {

  public static final long serialVersionUID = 234534634646L;
  JComboBox combo = new JComboBox();

  public MySubtitleEditor(JComboBox subs) {
    this.combo = subs;
  }

  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    DefaultComboBoxModel model = (DefaultComboBoxModel) combo.getModel();
    if(model.getIndexOf(value) == -1){
     model.addElement(value); 
    }
    combo.setSelectedItem(value);
    return combo;
  }

   public Language getCellEditorValue() {
    return (Language) combo.getSelectedItem();
  }
}
