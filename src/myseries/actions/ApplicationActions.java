/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.actions;

import database.SeriesRecord;
import help.About;
import help.CheckUpdate;
import help.Help;
import java.awt.Desktop;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableColumnModel;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myseries.MySeries;
import myseries.filters.Filters;
import myseries.series.RestoreSeries;
import myseries.series.Series;
import tools.download.torrents.TorrentConstants;
import tools.internetUpdate.tvrage.TrGetId;
import tools.options.Options;
import tools.options.OptionsPanel;
import myComponents.MyUsefulFunctions;
import tools.MySeriesLogger;

/**
 *
 * @author ssoldatos
 */
public class ApplicationActions {

  public static void showOptions(MySeries m) {
    MySeries.glassPane.activate(null);
    MySeriesLogger.logger.log(Level.INFO, "Showing options panel");
    OptionsPanel a = new OptionsPanel(m);
  }

  public static void exitApplication(MySeries m) {
    MySeriesLogger.logger.log(Level.INFO, "Performing exiting application actions");
    int divLocation = MySeries.splitPane_main.getDividerLocation();
    Options.setOption(Options.DIVIDER_LOCATION, divLocation);
    int feedDivLocation = MySeries.feedSplitPanel.getDividerLocation();
    Options.setOption(Options.FEED_DIVIDER_LOCATION, feedDivLocation);
    Options.setOption(Options.WINDOW_STATE, m.getExtendedState());
    Options.setOption(Options.WIDTH, m.getWidth());
    Options.setOption(Options.HEIGHT, m.getHeight());
    Options.setOption(Options.TOOLBAR_POSITION, m.getToolbarPosition());
    ArrayList<Integer> w = getTablesWidths(m);
    Options.setOption(Options.TABLE_WIDTHS, w);
    Options.save();
    deleteJVMFiles();
    MySeriesLogger.logger.log(Level.INFO, "Application exiting...");
    System.exit(0);
  }

  private static void deleteJVMFiles() {
    MySeriesLogger.logger.log(Level.INFO, "Delete JVM log files");
    File[] logs = (new File(Options._USER_DIR_)).listFiles(new FileFilter() {

      @Override
      public boolean accept(File pathname) {
        if (pathname.getName().startsWith("hs_err")) {
          return true;
        }
        return false;
      }
    });
    for (int i = 0; i < logs.length; i++) {
      File file = logs[i];
      file.delete();
    }
    MySeriesLogger.logger.log(Level.INFO, "JVM log files deleted");
  }

  private static ArrayList<Integer> getTablesWidths(MySeries m) {
    ArrayList<Integer> widths = new ArrayList<Integer>();
    TableColumnModel ts = MySeries.tableSeries.getColumnModel();
    for (int i = 0; i < ts.getColumnCount(); i++) {
      widths.add(ts.getColumn(i).getWidth());
    }
    ts = MySeries.tableEpisodes.getColumnModel();
    for (int i = 0; i < ts.getColumnCount(); i++) {
      widths.add(ts.getColumn(i).getWidth());
    }
    ts = MySeries.tableFilters.getColumnModel();
    for (int i = 0; i < ts.getColumnCount(); i++) {
      widths.add(ts.getColumn(i).getWidth());
    }

    return widths;
  }

  public static void about(MySeries m) {
    MySeries.glassPane.activate(null);
    MySeriesLogger.logger.log(Level.INFO, "Showing about panel");
    About a = new About(m);
  }

  public static void checkUpdates() {
    MySeries.glassPane.activate(null);
    MySeriesLogger.logger.log(Level.INFO, "Showing check update panel");
    new CheckUpdate(false);
  }

  public static void clearLogFiles() {
    MySeriesLogger.logger.log(Level.INFO, "Clearing log files");
    File dir = new File(Options._USER_DIR_);
    File[] files = dir.listFiles(new FilenameFilter() {

      @Override
      public boolean accept(File dir, String name) {
        if (name.matches("MySeriesLogs_([1-9]\\.html$|[0-9]\\.html\\.\\d+)")) {
          return true;
        }
        return false;
      }
    });
    if (files.length == 0) {
      MySeriesLogger.logger.log(Level.INFO, "No old files");
      MyMessages.message("Delete log files", "There are no old log files to delete");
      return;
    }
    int ans = MyMessages.question("Delete log files",
        "Realy delete the following " + files.length + " log files:\n"
        + MyUsefulFunctions.listArray(null, true));
    if (ans == JOptionPane.YES_OPTION) {
      for (int i = 0; i < files.length; i++) {
        File file = files[i];
        if(file.delete()){
          MySeriesLogger.logger.log(Level.FINE, "Deleted file {0}",file.getName());
        }
      }
    }
  }

  public static void viewLog(MySeries m) {
    Desktop d = Desktop.getDesktop();
    if (!Desktop.isDesktopSupported()) {
      MySeriesLogger.logger.log(Level.WARNING, "Desktop is not supported");
      MyMessages.error("Sorry!!!", "Your OS does not support this function");
    } else {
      if (!d.isSupported(Desktop.Action.OPEN)) {
        MySeriesLogger.logger.log(Level.WARNING, "Open file is not supported");
        MyMessages.error("Sorry!!!", "Your OS does not support this function");
      } else {
        try {
          d.open(new File("MySeriesLogs_0.html"));
          MySeriesLogger.logger.log(Level.FINE, "Log file opened for viewing");
        } catch (IOException ex) {
          MySeriesLogger.logger.log(Level.SEVERE, "Could not read the log file", ex);
          MyMessages.error("View Log", "Couldn't find the log file");
        }
      }
    }
  }

  public static void changeTab(MySeries m, ChangeEvent evt) {
    Vector<SeriesRecord> series = null;
    JTabbedPane tabs = (JTabbedPane) evt.getSource();
    if (tabs.getComponentCount() == 0 || tabs.getSelectedComponent() == null) {
      return;
    }
    int name = Integer.parseInt(tabs.getSelectedComponent().getName());
    MySeriesLogger.logger.log(Level.INFO, "Changing tab to {0}",name);
    try {
      if (name == MySeries.TAB_EPISODES_ID) {
      } else if (name == MySeries.TAB_FILTERS_ID) {
        Filters.getFilteredSeries();
      } else if (name == MySeries.TAB_RATINGS_ID) {
        MySeries.statSeries.refresh(Options.toBoolean(Options.UNIFIED_SERIES));
        MySeries.statEpisodes.refresh();
      } else if (name == MySeries.TAB_SCHEDULE_ID) {
        //MySeries.scheduler.refreshCalendar(MySeries.scheduler.getRealMonth(),MySeries.scheduler.getRealYear());
      }

    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
    }
  }

  public static void downloadScreenshot() {
    SeriesRecord series = Series.getCurrentSerial();
    MySeriesLogger.logger.log(Level.INFO, "Downloading screenshot for series {0}",series.getFullTitle());
    if (series.getTvrage_ID() == 0) {
      TrGetId tr = new TrGetId(null, series.getSeries_ID(), series.getFullTitle());
    }
  }

  public static void restoreSeries() {
    try {
      MySeriesLogger.logger.log(Level.INFO, "Restoring series");
      ArrayList<SeriesRecord> series = Series.getSeries(true);
      if (series.isEmpty()) {
        MySeriesLogger.logger.log(Level.INFO, "No deleted series to restore");
        MyMessages.message("Restore Series", "There are no deleted series to restore");
      } else {
        MySeriesLogger.logger.log(Level.INFO, "Opening restoring series panel");
        new RestoreSeries(series);
      }
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
    }
  }

  public static void showHelp(MySeries m) {
    if (!MySeries.isHelp) {
      MySeriesLogger.logger.log(Level.INFO, "Showing help panel");
      new Help(m);
    }
  }

  public static void deleteTorrents() {
    MySeriesLogger.logger.log(Level.INFO, "Deleting torrents");
    if (MyMessages.question("Delete torrents", "Do you really want to clear the torrents directory?") == JOptionPane.OK_OPTION) {
      File dir = new File(Options._USER_DIR_ + TorrentConstants.TORRENTS_PATH);
      if (dir.isDirectory()) {
        File[] torrents = dir.listFiles();
        int del = 0, notDel = 0;
        if (torrents.length == 0) {
          MySeriesLogger.logger.log(Level.INFO, "No torrents to delete");
          MyMessages.message("Delete torrents", "There are no torrents to delete");
          return;
        }
        for (int i = 0; i < torrents.length; i++) {
          File tor = torrents[i];
          MySeriesLogger.logger.log(Level.INFO, "Deleting torrent {0}", tor.getName());
          if (tor.delete()) {
            del++;
            MySeriesLogger.logger.log(Level.FINE, "Torrent {0} deleted", tor.getName());
          } else {
            notDel++;
            MySeriesLogger.logger.log(Level.WARNING, "Torrent {0}  could not be deleted", tor.getName());
          }
        }
        String mess = del + " torrents deleted";
        if(notDel >0){
          mess += "and " + notDel+ "  torrents could not be deleted.";
        } else {
          mess += ".";
        }
        MySeriesLogger.logger.log(Level.FINE, mess);
        MyMessages.message("Delete torrents", mess);
      } else {
        MySeriesLogger.logger.log(Level.WARNING, "The torrents directory does not exist");
        MyMessages.error("Delete torrents", "The torrents directory does not exist");
      }
    }
  }

  private ApplicationActions() {
  }
}
