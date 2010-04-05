/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents;

import javax.swing.table.DefaultTableModel;

/**
 * The tablemodel for the series table
 * @author lordovol
 */
public class MySeriesTableModel extends DefaultTableModel {

  private static final long serialVersionUID = 567264312632462L;

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnIndex == 2 || columnIndex == 3) {
      return Boolean.class;
    } else {
      return super.getColumnClass(columnIndex);
    }
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    if (col == 2 || col == 3) {
      return true;
    } else {
      return false;
    }
  }
}
