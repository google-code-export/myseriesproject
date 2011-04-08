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
import javax.swing.JOptionPane;
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
      MySeriesLogger.logger.log(Level.INFO, "Filtering subtitles");
      //Filters.setSubtitles(comboBox_subtitles.getSelectedIndex());
      Filters.getFilteredSeries();
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
  }

  public static void filterSeen(JComboBox comboBox_seen) {
    try {
      MySeriesLogger.logger.log(Level.INFO, "Filtering watch");
      //Filters.setSeen(comboBox_seen.getSelectedIndex());
      Filters.getFilteredSeries();
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
  }
  public static void filterDownloaded(JComboBox combobox_downloaded) {
    try {
      MySeriesLogger.logger.log(Level.INFO, "Filtering downloaded");
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
     MySeriesLogger.logger.log(Level.INFO, "Saaving filter action");
    if (title.trim().equals("") || title.equals("null")) {
      MySeriesLogger.logger.log(Level.WARNING, "Empty title");
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
        MySeriesLogger.logger.log(Level.FINE, "Filter {0} was saved",title);
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
    MySeriesLogger.logger.log(Level.INFO, "Delete filter {0}",title);
    int answ = MyMessages.question("Delete Filter?", "Are you sure that you want to delete the filter?");
    if (answ == JOptionPane.YES_OPTION) {
      try {
        f = DBHelper.getFilterByTitle(title);
        if (f != null) {
          f.delete();
          MySeriesLogger.logger.log(Level.FINE, "Filter was deleted");
          MyMessages.message("Filter deleted", "Filter was deleted");
        } else {
          MySeriesLogger.logger.log(Level.WARNING, "Filter not found");
          MyMessages.error("Error", "Filter not found");
        }
        m.comboBoxModel_filters = new DefaultComboBoxModel(DBHelper.getFiltersTitlesList());
        MySeries.combobox_filters.setModel(m.comboBoxModel_filters);
      } catch (SQLException ex) {
        MySeriesLogger.logger.log(Level.WARNING, "Error while deleting filter", ex);
        MyMessages.error("SQL Error", "There was an error when deleting the filter");
      }
    } else {
      MySeriesLogger.logger.log(Level.INFO, "Delete filter aborted by user");
    }
  }

  public static void applyFilter(MySeries m) {
    try {
      String title = "";
      title = String.valueOf(MySeries.combobox_filters.getSelectedItem());
      FilterRecord f = DBHelper.getFilterByTitle(title);
      if (f != null) {
        MySeriesLogger.logger.log(Level.INFO, "Applying filter {0}",f.getTitle());
        MySeries.combobox_downloaded.setSelectedIndex(f.getDownloaded());
        MySeries.comboBox_seen.setSelectedIndex(f.getSeen());
        MySeries.comboBox_filterSubtitles.setSelectedIndex(f.getSubtitles());
      }
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.WARNING, "Error while applying the  filter", ex);
      MyMessages.error("SQL Error", "There was an error when applying the filter");
    }
  }

  private FiltersActions() {
  }


}
