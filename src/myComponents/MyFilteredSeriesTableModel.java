/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents;

import javax.swing.table.DefaultTableModel;

/**
 * The tablemodel for filtered series table
 * @author lordovol
 */
public class MyFilteredSeriesTableModel extends DefaultTableModel {
  private static final long serialVersionUID = 325756637473L;

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnIndex == 4 || columnIndex == 6) {

      return Boolean.class;
    } else if (columnIndex == 1) {
      return Integer.class;
    } else {
      return super.getColumnClass(columnIndex);
    }
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    return false;
  }
}
