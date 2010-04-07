/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.MyTableModels;

import javax.swing.table.DefaultTableModel;
import myseries.filters.FilteredSeries;

/**
 * The tablemodel for filtered series table
 * @author lordovol
 */
public class MyFilteredSeriesTableModel extends DefaultTableModel {

  private static final long serialVersionUID = 325756637473L;

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnIndex == FilteredSeries.DOWNLOADED_COLUMN || columnIndex == FilteredSeries.SEEN_COLUMN) {
      return Boolean.class;
    } else if (columnIndex == FilteredSeries.EPISODE_NUMBER_COLUMN) {
      return Integer.class;
    } else {
      return super.getColumnClass(columnIndex);
    }
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    if (col == FilteredSeries.DOWNLOADED_COLUMN || col == FilteredSeries.SEEN_COLUMN || col == FilteredSeries.SUBS_COLUMN) {
      return true;
    }
    return false;
  }
}
