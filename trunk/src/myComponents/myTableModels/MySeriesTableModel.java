/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.MyTableModels;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import myseriesproject.series.Series;

/**
 * The tablemodel for the series table
 * @author lordovol
 */
public class MySeriesTableModel extends DefaultTableModel {

  private static final long serialVersionUID = 567264312632462L;

  public MySeriesTableModel() {
    addColumn(Series.SERIES_RECORD_COLUMN_TITLE);
    addColumn(Series.HIDDEN_COLUMN_TITLE);
    addColumn(Series.UPDATE_COLUMN_TITLE);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnIndex == Series.HIDDEN_COLUMN || columnIndex == Series.UPDATE_COLUMN) {
      return Boolean.class;
    } else {
      return super.getColumnClass(columnIndex);
    }
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    return  col == Series.HIDDEN_COLUMN || col == Series.UPDATE_COLUMN;
  }
}
