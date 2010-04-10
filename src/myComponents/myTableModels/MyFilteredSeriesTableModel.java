/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.MyTableModels;

import javax.swing.table.DefaultTableModel;
import myseries.filters.Filters;

/**
 * The tablemodel for filtered series table
 * @author lordovol
 */
public class MyFilteredSeriesTableModel extends DefaultTableModel {

  private static final long serialVersionUID = 325756637473L;

  public MyFilteredSeriesTableModel() {
    addColumn(Filters.FULLTITLE_COLUMN_TITLE);
    addColumn(Filters.EPISODE_NUMBER_COLUMN_TITLE);
    addColumn(Filters.EPISODERECORD_COLUMN_TITLE);
    addColumn(Filters.AIRED_COLUMN_TITLE);
    addColumn(Filters.DOWNLOADED_COLUMN_TITLE);
    addColumn(Filters.SUBS_COLUMN_TITLE);
    addColumn(Filters.SEEN_COLUMN_TITLE);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnIndex == Filters.DOWNLOADED_COLUMN || columnIndex == Filters.SEEN_COLUMN) {
      return Boolean.class;
    } else if (columnIndex == Filters.EPISODE_NUMBER_COLUMN) {
      return Integer.class;
    } else {
      return super.getColumnClass(columnIndex);
    }
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    if (col == Filters.DOWNLOADED_COLUMN || col == Filters.SEEN_COLUMN || col == Filters.SUBS_COLUMN) {
      return true;
    }
    return false;
  }
}
