/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.actions;

import database.SeriesRecord;
import help.About;
import help.CheckUpdate;
import help.ContactWay;
import help.Help;
import java.awt.Desktop;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableColumnModel;
import myComponents.MyMessages;
import myseries.MySeries;
import myseries.filters.Filters;
import myseries.series.RestoreSeries;
import myseries.series.Series;
import tools.internetUpdate.tvrage.TrGetId;
import tools.options.Options;
import myComponents.MyUsefulFunctions;
import myComponents.myToolbar.ToolbarCustomize;
import myseries.MySeriesConstants;
import sdialogs.Info;
import tools.MySeriesLogger;
import tools.misc.housekeeping.HouseKeeping;
import tools.misc.latestNews.LatestNews;
import tools.options.OptionsPanel;
import tools.options.Paths;

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
    int divLocation = m.splitPane_main.getDividerLocation();
    Options.setOption(Options.DIVIDER_LOCATION, divLocation);
    int feedDivLocation = m.feedSplitPanel.getDividerLocation();
    String filter = m.combobox_filters.getSelectedItem() != null ? (String) m.combobox_filters.getSelectedItem() : "";
    Options.setOption(Options.ACTIVE_FILTER, filter);
    Options.setOption(Options.FEED_DIVIDER_LOCATION, feedDivLocation);
    if (m.getExtendedState() != JFrame.ICONIFIED) {
      Options.setOption(Options.WINDOW_STATE, m.getExtendedState());
    }
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
    TableColumnModel ts = m.tableSeries.getColumnModel();
    for (int i = 0; i < ts.getColumnCount(); i++) {
      widths.add(ts.getColumn(i).getWidth());
    }
    ts = m.tableEpisodes.getColumnModel();
    for (int i = 0; i < ts.getColumnCount(); i++) {
      widths.add(ts.getColumn(i).getWidth());
    }
    ts = m.tableFilters.getColumnModel();
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
    int ans = MyMessages.confirm("Delete log files",
        "Realy delete the following " + files.length + " log files:\n"
        + MyUsefulFunctions.listArray(null, true), true);
    if (ans == JOptionPane.YES_OPTION) {
      for (int i = 0; i < files.length; i++) {
        File file = files[i];
        if (file.delete()) {
          MySeriesLogger.logger.log(Level.FINE, "Deleted file {0}", file.getName());
        }
      }
    }
  }

  public static void viewLog(MySeries m) {
    Desktop d = Desktop.getDesktop();
    if (!Desktop.isDesktopSupported()) {
      MySeriesLogger.logger.log(Level.SEVERE, "Desktop is not supported");
      MyMessages.error("Sorry!!!", "Your OS does not support this function", true, true);
    } else {
      if (!d.isSupported(Desktop.Action.OPEN)) {
        MySeriesLogger.logger.log(Level.SEVERE, "Open file is not supported");
        MyMessages.error("Sorry!!!", "Your OS does not support this function", true, true);
      } else {
        try {
          d.open(new File(Paths.LOGS_PATH + "MySeriesLog_0.html"));
          MySeriesLogger.logger.log(Level.FINE, "Log file opened for viewing");
        } catch (Exception ex) {
          MySeriesLogger.logger.log(Level.SEVERE, "Could not read the log file", ex);
          MyMessages.error("View Log", "Couldn't find the log file", true, true);
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
    MySeriesLogger.logger.log(Level.INFO, "Changing tab to {0}", name);
    try {
      if (name == MySeries.TAB_EPISODES_ID) {
      } else if (name == MySeries.TAB_FILTERS_ID) {
        Filters.getFilteredSeries(m.comboBox_seen, m.comboBox_filterSubtitles, m.combobox_downloaded,m.combobox_filters, m.tableFilters);
      } else if (name == MySeries.TAB_RATINGS_ID) {
        m.statSeries.refresh(Options.toBoolean(Options.UNIFIED_SERIES));
        m.statEpisodes.refresh();
      } else if (name == MySeries.TAB_SCHEDULE_ID) {
        //MySeries.scheduler.refreshCalendar(MySeries.scheduler.getRealMonth(),MySeries.scheduler.getRealYear());
      }

    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
    }
  }

  public static void downloadScreenshot() {
    SeriesRecord series = Series.getCurrentSerial();
    MySeriesLogger.logger.log(Level.INFO, "Downloading screenshot for series {0}", series.getFullTitle());
    if (series.getTvrage_ID() == 0) {
      TrGetId tr = new TrGetId(null, series.getSeries_ID(), series.getFullTitle(), true);
    }
  }

  public static void restoreSeries(MySeries m) {
    try {
      MySeriesLogger.logger.log(Level.INFO, "Restoring series");
      ArrayList<SeriesRecord> series = Series.getSeries(true);
      if (series.isEmpty()) {
        MySeriesLogger.logger.log(Level.INFO, "No deleted series to restore");
        MyMessages.message("Restore Series", "There are no deleted series to restore");
      } else {
        MySeriesLogger.logger.log(Level.INFO, "Opening restoring series panel");
        new RestoreSeries(series, m);
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
    if (MyMessages.confirm("Delete torrents", "Do you really want to clear the torrents directory?", true) == JOptionPane.OK_OPTION) {
      File dir = new File(Options._USER_DIR_ + Paths.TORRENTS_PATH);
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
        if (notDel > 0) {
          mess += "and " + notDel + "  torrents could not be deleted.";
        } else {
          mess += ".";
        }
        MySeriesLogger.logger.log(Level.FINE, mess);
        MyMessages.message("Delete torrents", mess);
      } else {
        MySeriesLogger.logger.log(Level.SEVERE, "The torrents directory does not exist");
        MyMessages.error("Delete torrents", "The torrents directory does not exist", true, true);
      }
    }
  }

  public static void warnForLogLevel() {
    MySeriesLogger.logger.log(Level.INFO, "Warn for log using");
    if (Options.toString(Options.DEBUG_MODE).equals("ALL")) {
      if (Options.toBoolean(Options.WARN_FOR_LOG_USE)) {
        int a = MyMessages.confirm("Logging level", "Logging level is set to ALL.\n"
            + "That's for debugging reasons and may slow down the application.\n"
            + "Do you want to turn logging off?", true);
        if (a == JOptionPane.OK_OPTION) {
          Options.setOption(Options.DEBUG_MODE, "OFF");
          Options.setOption(Options.WARN_FOR_LOG_USE, false);
          Options.save();
        }
      }
      MyMessages.logToPanel(Info.WARNING_MESS, "Logging level", "Logging level is set to ALL\n"
          + "That's for debugging reasons and may slow down the application\n");
    }
  }

  public static void houseKeeping() {
    MySeriesLogger.logger.log(Level.INFO, "Housekeeping action");
    HouseKeeping h = new HouseKeeping();
  }

  public static void customizeToolbar(MySeries m) {
    ToolbarCustomize tc = new ToolbarCustomize(m.myToolbar.getVisibleButtons());
    m.myToolbar.visibleButtons = tc.newVisibleButtons;
    Options.setOption(Options.TOOLBAR_BUTTONS, m.myToolbar.visibleButtons);
    Options.save();
    m.myToolbar.populateToolbar();
    MyUsefulFunctions.createMemoryCons(m);
  }

  public static void googleCodePage() {
    MyUsefulFunctions.browse(MySeries.GOOGLE_CODE_URL);
  }

  public static void contactByMail() {
    MySeriesLogger.logger.log(Level.INFO, "Contacting by email");
    try {
      String subject = ("?subject=MySeries v." + MySeries.version).replaceAll("\\s", "%20");
      URI email = new URI("mailto:" + MySeries.EMAIL + subject);
      MyUsefulFunctions.sendMail(email);
    } catch (Exception ex) {
      MyMessages.warning("Sending mail", "Could not open your email client.\n"
          + "Please send it manually to " + MySeries.EMAIL, true, true);
      MySeriesLogger.logger.log(Level.WARNING, "Could not send mail", ex);
    }
  }

  public static void paypalDonation() {
    MySeriesLogger.logger.log(Level.INFO, "Showing donation message");
    int a = MyMessages.confirm("Paypal Donation", "Thanks for your interest in donating for MySeries developement."
        + "Even 1$ or 1€ is a real help.You can make your donation via paypal to " + MySeries.EMAIL + ".\nClick OK"
        + " to go to the donation page", true);
    if (a == JOptionPane.YES_OPTION) {
      MySeriesLogger.logger.log(Level.INFO, "Go to donation page");
      MyUsefulFunctions.browse(MySeries.DONATION_URL);
    }
  }

  public static void chooseContactWay() {
    MySeriesLogger.logger.log(Level.INFO, "Choosing contact way");
    ContactWay cw = new ContactWay();
    if (cw.way == ContactWay.EMAIL) {
      contactByMail();
    } else if (cw.way == ContactWay.GOOGLE_CODE) {
      contactByGoogleCode();
    } else {
    }
  }

  private static void contactByGoogleCode() {
    MySeriesLogger.logger.log(Level.INFO, "Going to google code report");
    MyUsefulFunctions.browse(MySeries.GOOGLE_CODE_TRACKER);
  }

  public static void warnForJREVersion() {
    MySeriesLogger.logger.log(Level.INFO, "Checking java version");
    String version = System.getProperty("java.runtime.version");
    MySeriesLogger.logger.log(Level.INFO, "Java version is {0}", version);
    if (version != null && version.startsWith("1.7")) {
      if (Options.toBoolean(Options.WARN_FOR_VERSION)) {
        MyMessages.warning("Java version " + version, "The application is written for java 6.\n"
            + "It seems that you are using java 7.\nThere might be some incopatibility issues",
            true, true);
        Options.setOption(Options.WARN_FOR_VERSION, false);
        Options.save();
      } else {
        MyMessages.logToPanel(Info.WARNING_MESS,"Java version", "The application is written for java 6.\n"
            + "It seems that you are using java 7.\nThere might be some incopatibility issues");
      }
    }
  }

  public static void restartApplication(MySeries m) {
    String os = System.getProperty("os.name").toLowerCase();
    if (os.startsWith("windows")) {
      restartWindows(m);
    } else if (os.startsWith("linux")) {
      restartLinux(m);
    } else {
      restartDefault(m);
    }
  }

  private static void restartWindows(MySeries m) {
    File exec = new File(MySeriesConstants.WINDOWS_EXECUTABLE);
    restart(m, exec);
  }

  private static void restartLinux(MySeries m) {
    File exec = new File(MySeriesConstants.LINUX_EXECUTABLE);
    restart(m, exec);
  }

  private static void restartDefault(MySeries m) {
    File exec = new File(MySeriesConstants.APPLICATION_JAR);
    restartDefault(m, exec);
  }

  private static void restart(MySeries m, File exec) {
    try {
      String pathToApp = exec.getAbsolutePath();
      File startingDir = exec.getAbsoluteFile().getParentFile();
      if (exec.exists()) {
        MyUsefulFunctions.runExternalProgram(new String[]{pathToApp}, startingDir);
        ApplicationActions.exitApplication(m);
      } else {
        MySeriesLogger.logger.log(Level.SEVERE, "Could not restart application");
        MyMessages.error("Restart Application", "Could not restart the application", false, true);
      }
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not restart application", ex);
      MyMessages.error("Restart Application", "Could not restart the application", false, true);
    }
  }

  private static void restartDefault(MySeries m, File exec) {
    try {
      String pathToApp = exec.getAbsolutePath();
      File startingDir = exec.getAbsoluteFile().getParentFile();
      if (exec.exists()) {
        MyUsefulFunctions.runExternalProgram(new String[]{"java", "-jar", pathToApp}, startingDir);
        ApplicationActions.exitApplication(m);
      } else {
        MySeriesLogger.logger.log(Level.SEVERE, "Could not restart application");
        MyMessages.error("Restart Application", "Could not restart the application", false, true);
      }
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not restart application", ex);
      MyMessages.error("Restart Application", "Could not restart the application", false, true);
    }
  }

  public static void latestNews(MySeries m,boolean start) {
    LatestNews news = new LatestNews(m,start);
  }

    private ApplicationActions() {
  }
}
