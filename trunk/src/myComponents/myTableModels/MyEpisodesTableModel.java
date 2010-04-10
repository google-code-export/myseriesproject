/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.MyTableModels;

import javax.swing.table.DefaultTableModel;
import myseries.episodes.Episodes;

/**
 * The table model for Episodes table
 * @author lordovol
 */
public class MyEpisodesTableModel extends DefaultTableModel {

  private static final long serialVersionUID = 467462375687L;
  
  public MyEpisodesTableModel() {
    addColumn(Episodes.EPISODE_NUM_COLUMN_TITLE);
    addColumn(Episodes.EPISODERECORD_COLUMN_TITLE);
    addColumn(Episodes.AIRED_COLUMN_TITLE);
    addColumn(Episodes.DOWNLOADED_COLUMN_TITLE);
    addColumn(Episodes.SUBS_COLUMN_TITLE);
    addColumn(Episodes.SEEN_COLUMN_TITLE);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnIndex == Episodes.DOWNLOADED_COLUMN || columnIndex == Episodes.SEEN_COLUMN) {
      return Boolean.class;
    } else if (columnIndex == Episodes.EPISODE_NUM_COLUMN) {
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
