/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myComponents;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lordovol
 */
public class MyRenameEpisodesTableModel extends DefaultTableModel {
 private static final long serialVersionUID = 434532375687L;

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnIndex == 3) {
      return Boolean.class;
    } else {
      return super.getColumnClass(columnIndex);
    }
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    if(col==3){
    if(this.getValueAt(row, 1).equals(this.getValueAt(row, 2))){
      return false;
    }
    return true;
    }
    return false;
  }


}
