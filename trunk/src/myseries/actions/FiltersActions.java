/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.actions;

import database.DBHelper;
import database.FilterRecord;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import myComponents.MyMessages;
import myseries.filters.Filters;
import myseries.MySeries;
import tools.MySeriesLogger;

/**
 *
 * @author ssoldatos
 */
public class FiltersActions {

  public static void filterSubtitles(MySeries m) {
    try {
      MySeriesLogger.logger.log(Level.INFO, "Filtering subtitles");
      //Filters.setSubtitles(comboBox_subtitles.getSelectedIndex());
      Filters.getFilteredSeries(m.comboBox_seen, m.comboBox_filterSubtitles, m.combobox_downloaded,m.combobox_filters, m.tableFilters);
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
  }

  public static void filterSeen(MySeries m) {
    try {
      MySeriesLogger.logger.log(Level.INFO, "Filtering watch");
      //Filters.setSeen(comboBox_seen.getSelectedIndex());
      Filters.getFilteredSeries(m.comboBox_seen, m.comboBox_filterSubtitles, m.combobox_downloaded,m.combobox_filters, m.tableFilters);
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
  }

  public static void filterDownloaded(MySeries m) {
    try {
      MySeriesLogger.logger.log(Level.INFO, "Filtering downloaded");
      //Filters.setDownloaded(combobox_downloaded.getSelectedIndex());
      Filters.getFilteredSeries(m.comboBox_seen, m.comboBox_filterSubtitles, m.combobox_downloaded,m.combobox_filters, m.tableFilters);
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
  }

  public static void saveFilter(MySeries m) {
    String title = "";
    title = String.valueOf(m.combobox_filters.getSelectedItem());
    FilterRecord f;
    MySeriesLogger.logger.log(Level.INFO, "Saaving filter action");
    if (title.trim().equals("") || title.equals("null")) {
      MySeriesLogger.logger.log(Level.WARNING, "Empty title");
      MyMessages.warning("Empty title", "Please specify a save name", true);
    } else {
      try {
        f = DBHelper.getFilterByTitle(title);
        if (f == null) {
          f = new FilterRecord();
        }
        f.setDownloaded(m.combobox_downloaded.getSelectedIndex());
        f.setSeen(m.comboBox_seen.getSelectedIndex());
        f.setSubtitles(m.comboBox_filterSubtitles.getSelectedIndex());
        f.setTitle(title);
        f.save();
        MyMessages.message("Filter saved", "Filter was saved");
        MySeriesLogger.logger.log(Level.FINE, "Filter {0} was saved", title);
        m.comboBoxModel_filters = new DefaultComboBoxModel(DBHelper.getFiltersTitlesList());
        m.combobox_filters.setModel(m.comboBoxModel_filters);
      } catch (SQLException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "Error while saving filter", ex);
        MyMessages.error("SQL Error", "There was an error when saving the filter", true);
      }
    }
  }

  public static void deleteFilter(MySeries m) {
    String title = "";
    title = String.valueOf(m.combobox_filters.getSelectedItem());
    FilterRecord f;
    MySeriesLogger.logger.log(Level.INFO, "Delete filter {0}", title);
    int answ = MyMessages.confirm("Delete Filter?", "Are you sure that you want to delete the filter?", true);
    if (answ == JOptionPane.YES_OPTION) {
      try {
        f = DBHelper.getFilterByTitle(title);
        if (f != null) {
          f.delete();
          MySeriesLogger.logger.log(Level.FINE, "Filter was deleted");
          MyMessages.message("Filter deleted", "Filter was deleted");
        } else {
          MySeriesLogger.logger.log(Level.WARNING, "Filter not found");
          MyMessages.warning("Error", "Filter not found", true);
        }
        m.comboBoxModel_filters = new DefaultComboBoxModel(DBHelper.getFiltersTitlesList());
        m.combobox_filters.setModel(m.comboBoxModel_filters);
      } catch (SQLException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "Error while deleting filter", ex);
        MyMessages.error("SQL Error", "There was an error when deleting the filter", true);
      }
    } else {
      MySeriesLogger.logger.log(Level.INFO, "Delete filter aborted by user");
    }
  }

  public static void applyFilter(MySeries m) {
    try {
      String title = "";
      title = String.valueOf(m.combobox_filters.getSelectedItem());
      FilterRecord f = DBHelper.getFilterByTitle(title);
      if (f != null) {
        MySeriesLogger.logger.log(Level.INFO, "Applying filter {0}", f.getTitle());
        m.combobox_downloaded.setSelectedIndex(f.getDownloaded());
        m.comboBox_seen.setSelectedIndex(f.getSeen());
        m.comboBox_filterSubtitles.setSelectedIndex(f.getSubtitles());
      }
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Error while applying the  filter", ex);
      MyMessages.error("SQL Error", "There was an error when applying the filter", true);
    }
  }

  private FiltersActions() {
  }
}
