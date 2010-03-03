/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents;

import javax.swing.table.DefaultTableModel;

/**
 * The table model for Episodes table
 * @author lordovol
 */
public class MyEpisodesTableModel extends DefaultTableModel {
  private static final long serialVersionUID = 467462375687L;

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnIndex == 3 || columnIndex == 5) {
      return Boolean.class;
    } else if (columnIndex == 0) {
      return Integer.class;
    } else {
      return super.getColumnClass(columnIndex);
    }
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    return true;
  }
}
