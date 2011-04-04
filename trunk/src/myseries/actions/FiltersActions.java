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
import javax.swing.JComboBox;
import myComponents.MyMessages;
import myseries.filters.Filters;
import myseries.MySeries;
import myComponents.MyUsefulFunctions;
import tools.MySeriesLogger;
/**
 *
 * @author ssoldatos
 */
public class FiltersActions {
public static void filterSubtitles(JComboBox comboBox_subtitles) {
    try {
      //Filters.setSubtitles(comboBox_subtitles.getSelectedIndex());
      Filters.getFilteredSeries();
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
  }

  public static void filterSeen(JComboBox comboBox_seen) {
    try {
      //Filters.setSeen(comboBox_seen.getSelectedIndex());
      Filters.getFilteredSeries();
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
  }
  public static void filterDownloaded(JComboBox combobox_downloaded) {
    try {
      //Filters.setDownloaded(combobox_downloaded.getSelectedIndex());
      Filters.getFilteredSeries();
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
  }
   public static void saveFilter(MySeries m) {
    String title = "";
    title = String.valueOf(MySeries.combobox_filters.getSelectedItem());
    FilterRecord f;
    if (title.trim().equals("") || title.equals("null")) {
      MyMessages.error("Empty title", "Please specify a save name");
    } else {
      try {
        f = DBHelper.getFilterByTitle(title);
        if (f == null) {
          f = new FilterRecord();
        }
        f.setDownloaded(MySeries.combobox_downloaded.getSelectedIndex());
        f.setSeen(MySeries.comboBox_seen.getSelectedIndex());
        f.setSubtitles(MySeries.comboBox_filterSubtitles.getSelectedIndex());
        f.setTitle(title);
        f.save();
        MyMessages.message("Filter saved", "Filter was saved");
        m.comboBoxModel_filters = new DefaultComboBoxModel(DBHelper.getFiltersTitlesList());
        MySeries.combobox_filters.setModel(m.comboBoxModel_filters);
      } catch (SQLException ex) {
        MySeriesLogger.logger.log(Level.WARNING, "Error while saving filter", ex);
        MyMessages.error("SQL Error", "There was an error when saving the filter");
      }
    }
  }

  public static void deleteFilter(MySeries m) {
    String title = "";
    title = String.valueOf(MySeries.combobox_filters.getSelectedItem());
    FilterRecord f;
    int answ = MyMessages.question("Delete Filter?", "Are you sure that you want to delete the filter?");
    if (answ == 0) {
      try {
        f = DBHelper.getFilterByTitle(title);
        if (f != null) {
          f.delete();
          MyMessages.message("Filter deleted", "Filter was deleted");
        } else {
          MyMessages.error("Error", "Filter could not be deleted");
        }
        m.comboBoxModel_filters = new DefaultComboBoxModel(DBHelper.getFiltersTitlesList());
        MySeries.combobox_filters.setModel(m.comboBoxModel_filters);
      } catch (SQLException ex) {
        MySeriesLogger.logger.log(Level.WARNING, "Error while deleting filter", ex);
        MyMessages.error("SQL Error", "There was an error when deleting the filter");
      }
    }
  }

  public static void applyFilter(MySeries m) {
    try {
      String title = "";
      title = String.valueOf(MySeries.combobox_filters.getSelectedItem());
      FilterRecord f = DBHelper.getFilterByTitle(title);
      if (f != null) {
        MySeries.combobox_downloaded.setSelectedIndex(f.getDownloaded());
        MySeries.comboBox_seen.setSelectedIndex(f.getSeen());
        MySeries.comboBox_filterSubtitles.setSelectedIndex(f.getSubtitles());
      }
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.WARNING, "Error while applying the  filter", ex);
      MyMessages.error("SQL Error", "There was an error when applying the filter");
    }
  }


}
