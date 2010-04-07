/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.MyTableModels;

import javax.swing.table.DefaultTableModel;
import tools.renaming.RenameEpisodes;

/**
 *
 * @author lordovol
 */
public class MyRenameEpisodesTableModel extends DefaultTableModel {

  private static final long serialVersionUID = 434532375687L;

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnIndex == RenameEpisodes.EDIT_COLUMN) {
      return Boolean.class;
    } else {
      return super.getColumnClass(columnIndex);
    }
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    if (col == RenameEpisodes.EDIT_COLUMN) {
      if (this.getValueAt(row, RenameEpisodes.ORIGINAL_NAME_COLUMN).equals(this.getValueAt(row, RenameEpisodes.NEW_NAME_COLUMN))) {
        return false;
      }
      return true;
    }
    return false;
  }
}
