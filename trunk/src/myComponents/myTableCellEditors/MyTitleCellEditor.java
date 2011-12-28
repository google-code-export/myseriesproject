/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellEditors;

import database.EpisodesRecord;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author lordovol
 */
public class MyTitleCellEditor extends AbstractCellEditor implements TableCellEditor {

  public static final long serialVersionUID = 234534634646L;
  private EpisodesRecord episode = new EpisodesRecord();
  private JTextField title = new JTextField();

  public EpisodesRecord getCellEditorValue() {
    return episode;
  }

  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      this.episode = (EpisodesRecord) value;
      title.setText(episode.getTitle());
      title.setBackground(Color.WHITE);
      return title;
  }

  @Override
  public boolean stopCellEditing() {
    episode.setTitle(title.getText());
    return super.stopCellEditing();
  }

  @Override
  public boolean isCellEditable(EventObject e) {
    if (e instanceof MouseEvent){
      return false ; //((MouseEvent)e).getClickCount() >= 2;
    }
    return true;
  }



 }
