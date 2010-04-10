/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.actions;

import database.DBConnection;
import database.Database;
import database.EpisodesRecord;
import database.FilterRecord;
import database.SaveDatabase;
import database.SeriesRecord;
import help.About;
import help.CheckUpdate;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableColumnModel;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myComponents.myFileFilters.VideoFilter;
import myseries.series.AdminSeries;
import myseries.series.Series;
import myseries.MySeries;
import myseries.StartPanel;
import myseries.episodes.AdminEpisodes;
import myseries.episodes.Episodes;
import myseries.episodes.NextEpisodes;
import myseries.episodes.Video;
import myseries.filters.Filters;
import tools.DesktopSupport;
import tools.download.subtitles.Subtitle;
import tools.download.subtitles.sonline.GetSOnlineCode;
import tools.download.subtitles.sonline.SOnlineForm;
import tools.download.subtitles.tvsubtitles.GetTvSubtitlesCode;
import tools.download.subtitles.tvsubtitles.TvSubtitlesForm;
import tools.download.torrents.EzTvForm;
import tools.importExport.ExportEpisodes;
import tools.importExport.ImportEpisodes;
import tools.internetUpdate.InternetUpdate;
import tools.internetUpdate.tvrage.TrGetId;
import tools.options.Options;
import tools.options.OptionsPanel;
import tools.renaming.RenameEpisodes;

/**
 *
 * @author lordovol
 */
public class Actions {

  public static void editSeries(MySeries m) {
    try {
      MySeries.glassPane.activate(null);
      AdminSeries a = new AdminSeries(m, Series.getCurrentSerial());
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }

  }

  public static void addSeries(MySeries m) {
    Series.setCurrentSerial(null);
    MySeries.glassPane.activate(null);
    //if (!addSeriesPanel) {
    try {
      AdminSeries a = new AdminSeries(m, null);
      Series.setCurrentSerial(null);
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }

  }

  public static void deleteSeries(MySeries m) {
    String title = Series.getCurrentSerial().getTitle();
    int season = Series.getCurrentSerial().getSeason();
    int series_ID = Series.getCurrentSerial().getSeries_ID();
    int answ = MyMessages.question("Delete Serial?", "Really delete the series " + title + " season " + season + "?");
    if (answ == 0) {
      try {
        String sql = "DELETE FROM series WHERE series_ID = " + series_ID;
        DBConnection.stmt.execute(sql);
        sql = "DELETE FROM episodes WHERE series_ID = " + series_ID;
        DBConnection.stmt.execute(sql);
        Series.getSeries();
        Series.setCurrentSerial(null);
        Episodes.updateEpisodesTable();
        NextEpisodes.createNextEpisodes();
        NextEpisodes.show();
      } catch (SQLException ex) {
        MySeries.logger.log(Level.SEVERE, null, ex);
      }
    }
  }

  public static void AddEpisode(MySeries m) {
    try {
      MySeries.glassPane.activate(null);
      AdminEpisodes e = new AdminEpisodes(m, Series.getCurrentSerial(), null);
    } catch (IOException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }

  public static void filterSubtitles(JComboBox comboBox_subtitles) {
    try {
      Filters.setSubtitles(comboBox_subtitles.getSelectedIndex());
      Filters.getFilteredSeries();
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }

  public static void filterSeen(JComboBox comboBox_seen) {
    try {
      Filters.setSeen(comboBox_seen.getSelectedIndex());
      Filters.getFilteredSeries();
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }

  public static void createDatabase(MySeries m, boolean createNewDb) {
    MySeries.glassPane.activate(null);
    new StartPanel(m, createNewDb);
  }

  public static void loadDatabase(MySeries m) {
    try {
      String[] filter = {".db"};
      String load = MyUsefulFunctions.getSelectedFile(Database.PATH, filter, "Load Database", "Select the database to load");
      if (!load.equals("null")) {
        if (DBConnection.CheckDatabase(load)) {
          Options.setOption(Options.DB_NAME, load);
          Options.save();
          m.dispose();
          new MySeries();
        } else {
          MySeries.logger.log(Level.WARNING, "Selected database is invlid.Not loading...");
          MyMessages.error("Invalid Database", "The database you selected is invalid");
          loadDatabase(m);
        }
      } else {
      }
    } catch (ClassNotFoundException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (UnsupportedLookAndFeelException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }

  public static void filterDownloaded(JComboBox combobox_downloaded) {
    try {
      Filters.setDownloaded(combobox_downloaded.getSelectedIndex());
      Filters.getFilteredSeries();
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }

  public static void showOptions(MySeries m) {
    MySeries.glassPane.activate(null);
    OptionsPanel a = new OptionsPanel(m);
  }

  public static void exitApplication(MySeries m) {
    int divLocation = MySeries.splitPane_main.getDividerLocation();
    Options.setOption(Options.DIVIDER_LOCATION, divLocation);
    Options.setOption(Options.WINDOW_STATE, m.getExtendedState());
    Options.setOption(Options.WIDTH, m.getWidth());
    Options.setOption(Options.HEIGHT, m.getHeight());
    ArrayList<Integer> w = getTablesWidths(m);
    Options.setOption(Options.TABLE_WIDTHS, w);
    MySeries.logger.log(Level.INFO, "Saving options");
    Options.save();
    MySeries.logger.log(Level.INFO, "Application exiting...");
    System.exit(0);
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
    ts = MySeries.tableFiltels.getColumnModel();
    for (int i = 0; i < ts.getColumnCount(); i++) {
      widths.add(ts.getColumn(i).getWidth());
    }

    return widths;
  }

  public static void about(MySeries m) {
    MySeries.glassPane.activate(null);
    About a = new About(m);
  }

  public static void saveFilter(MySeries m) {
    String title = "";
    title = String.valueOf(MySeries.combobox_filters.getSelectedItem());
    FilterRecord f;
    if (title.trim().equals("") || title.equals("null")) {
      MyMessages.error("Empty title", "Please specify a save name");
    } else {
      try {
        f = FilterRecord.getFilterByTitle(title);
        if (f == null) {
          f = new FilterRecord();
        }
        f.setDownloaded(MySeries.combobox_downloaded.getSelectedIndex());
        f.setSeen(MySeries.comboBox_seen.getSelectedIndex());
        f.setSubtitles(MySeries.comboBox_subtitles.getSelectedIndex());
        f.setTitle(title);
        f.save();
        MyMessages.message("Filter saved", "Filter was saved");
        m.comboBoxModel_filters = new DefaultComboBoxModel(FilterRecord.getFiltersTitlesList());
        MySeries.combobox_filters.setModel(m.comboBoxModel_filters);
      } catch (SQLException ex) {
        MySeries.logger.log(Level.WARNING, "Error while saving filter", ex);
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
        f = FilterRecord.getFilterByTitle(title);
        if (f != null) {
          f.delete();
          MyMessages.message("Filter deleted", "Filter was deleted");
        } else {
          MyMessages.error("Error", "Filter could not be deleted");
        }
        m.comboBoxModel_filters = new DefaultComboBoxModel(FilterRecord.getFiltersTitlesList());
        MySeries.combobox_filters.setModel(m.comboBoxModel_filters);
      } catch (SQLException ex) {
        MySeries.logger.log(Level.WARNING, "Error while deleting filter", ex);
        MyMessages.error("SQL Error", "There was an error when deleting the filter");
      }
    }
  }

  public static void applyFilter(MySeries m) {
    try {
      String title = "";
      title = String.valueOf(MySeries.combobox_filters.getSelectedItem());
      FilterRecord f = FilterRecord.getFilterByTitle(title);
      if (f != null) {
        MySeries.combobox_downloaded.setSelectedIndex(f.getDownloaded());
        MySeries.comboBox_seen.setSelectedIndex(f.getSeen());
        MySeries.comboBox_subtitles.setSelectedIndex(f.getSubtitles());
      }
    } catch (SQLException ex) {
      MySeries.logger.log(Level.WARNING, "Error while applying the  filter", ex);
      MyMessages.error("SQL Error", "There was an error when applying the filter");
    }
  }

  public static void AddEpisodeInEpisodes(MySeries m) {
    try {
      MySeries.glassPane.activate(null);
      AdminEpisodes e = new AdminEpisodes(m, Series.getCurrentSerial(), null);
    } catch (IOException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }

  public static void downloadSub(MySeries m) {
    try {
      if (!DesktopSupport.isBrowseSupport()) {
        MySeries.logger.log(Level.WARNING, "Browse is not supported in the current OS");
        MyMessages.error("Browse Error!!!", "Browse is not supported");
        return;
      }
      java.net.URI uri = new java.net.URI(Series.getCurrentSerial().getLink());
      DesktopSupport.getDesktop().browse(uri);
    } catch (IOException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (URISyntaxException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }

  public static void exportEpisodes() {
    new ExportEpisodes();
  }

  public static void importEpisodes(MySeries m) {
    try {
      MySeries.glassPane.activate(null);
      new ImportEpisodes(m);
    } catch (SQLException ex) {
      MySeries.logger.log(Level.WARNING, "Error while importing the episodes", ex);
      MyMessages.error("SQL Error", "There was an error when importing the episodes");
    }
  }

  public static void saveDatase() {
    new SaveDatabase();
  }

  public static void internetUpdate(MySeries m) {
    MySeries.glassPane.activate(null);
    new InternetUpdate(m);
  }

  public static void internetUpdateSeries(MySeries m) {
    MySeries.glassPane.activate(null);
    SeriesRecord cSeries = Series.getCurrentSerial();
    if (Options.toString(Options.INTERNET_UPDATE_DB).equals(InternetUpdate.TV_RAGE_NAME) && cSeries.getTvrage_ID() == 0) {
      try {
        TrGetId g = new TrGetId(m, cSeries.getSeries_ID(), cSeries.getTitle());
        cSeries.setTvrage_ID(g.tvRageID);
        cSeries.save();
      } catch (SQLException ex) {
        MySeries.logger.log(Level.SEVERE, null, ex);
      }
    } else {
      InternetUpdate iu = new InternetUpdate(m, Series.getCurrentSerial());
    }
    MySeries.glassPane.deactivate();
  }

  public static void checkUpdates() {
    MySeries.glassPane.activate(null);
    new CheckUpdate(false);
  }

  public static void viewLog(MySeries m) {
    Desktop d = Desktop.getDesktop();
    if (!Desktop.isDesktopSupported()) {
      MyMessages.error("Sorry!!!", "Your OS does not support this function");
    } else {
      if (!d.isSupported(Desktop.Action.OPEN)) {
        MyMessages.error("Sorry!!!", "Your OS does not support this function");
      } else {
        try {
          d.open(new File(Options._USER_DIR_ + "/MySeriesLogs_0.html"));
        } catch (IOException ex) {
          MySeries.logger.log(Level.SEVERE, "Could not read the log file", ex);
        }
      }
    }
  }

  public static void goToLocalDir() {
    try {
      File f = new File(Series.getCurrentSerial().getLocalDir());
      if (f.isDirectory()) {
        DesktopSupport.getDesktop().open(f);
      } else {
        MySeries.logger.log(Level.WARNING, f.getCanonicalPath() + " is not a directory");
        MyMessages.error("Directory error", f.getCanonicalPath() + " is not a directory");
        return;
      }
    } catch (Exception ex) {
      MySeries.logger.log(Level.WARNING, "Browse is not supported in the current OS");
      MyMessages.error("Browse Error!!!", "Browse is not supported");
      return;
    }
  }

  public static void viewEpisode() {
    File localDir = new File(Series.getCurrentSerial().getLocalDir().trim());
    int season = Series.getCurrentSerial().getSeason();
    int episode = Episodes.getCurrentEpisode().getEpisode();
    String regex = MyUsefulFunctions.createRegex(season, episode);
    Video.getVideos(localDir, regex);
  }

  public static void changeTab() {
    try {
      Filters.getFilteredSeries();
      String title = MySeries.tabsPanel.getTitleAt(0).substring(0, MySeries.tabsPanel.getTitleAt(0).length() - 3).trim();
      Vector<SeriesRecord> series = SeriesRecord.getSeriesBySql("SELECT * FROM series WHERE title = '" + title + "'");
      if (series.size() > 0) {
        Series.setCurrentSerial(series.get(0));
        Episodes.updateEpisodesTable();
      } else {
      }
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }

  public static void deleteEpisode() {
    String title = Episodes.getCurrentEpisode().getTitle();
    int episode_ID = Episodes.getCurrentEpisode().getEpisode_ID();
    int answ = MyMessages.question("Delete Episode?", "Really delete the episode " + title + "?");
    if (answ == 0) {
      try {
        String sql = "DELETE FROM episodes WHERE episode_ID = " + episode_ID;
        DBConnection.stmt.execute(sql);
        Episodes.updateEpisodesTable();
      } catch (SQLException ex) {
        MySeries.logger.log(Level.SEVERE, null, ex);
      }
    } else {
    }

  }

  public static void renameEpisodes() {
    try {
      ArrayList<File> oldNames = new ArrayList<File>();
      ArrayList<EpisodesRecord> newNames = new ArrayList<EpisodesRecord>();
      SeriesRecord series = Series.getCurrentSerial();
      int season = series.getSeason();
      File dir = new File(series.getLocalDir());
      File[] files = dir.listFiles();
      String path;

      ArrayList<EpisodesRecord> episodes = Episodes.getCurrentSeriesEpisodes();
      for (Iterator<EpisodesRecord> it = episodes.iterator(); it.hasNext();) {
        EpisodesRecord episodesRecord = it.next();
        int episode = episodesRecord.getEpisode();
        String regex = MyUsefulFunctions.createRegex(season, episode);
        Pattern pattern = Pattern.compile(regex);
        for (int i = 0; i < files.length; i++) {
          File file = files[i];
          if (file != null && file.isFile()) {
            path = file.getParent();
            String name = file.getName();
            Matcher matcher = pattern.matcher(name);
            if (matcher.find()) {
              String[] tokens = name.split("\\.", -1);
              String ext = tokens[tokens.length - 1];
              if (ext.equals("srt") || ext.equals("sub")) {
                if (tokens[tokens.length - 2].equals("gr") || tokens[tokens.length - 2].equals("en")) {
                  ext = tokens[tokens.length - 2] + "." + tokens[tokens.length - 1];
                }
              }

              String newFilename = series.getTitle()
                      + RenameEpisodes.SEASON_SEPARATOR + MyUsefulFunctions.padLeft(series.getSeason(), 2, "0")
                      + RenameEpisodes.EPISODE_SEPARATOR + MyUsefulFunctions.padLeft(episodesRecord.getEpisode(), 2, "0")
                      + RenameEpisodes.TITLE_SEPARATOR + episodesRecord.getTitle();

              String newName = path + "/" + newFilename + "." + ext;
              File newFile = new File(newName);
              oldNames.add(files[i]);
              newNames.add(episodesRecord);
              files[i] = null;
            }
          }
        }
      }
      RenameEpisodes r = new RenameEpisodes(oldNames, newNames, series);
    } catch (SQLException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }

  public static void downloadSubtitles(MySeries aThis) {
    if (Options.toString(Options.SUBTITLE_SITE).equals(Subtitle.TV_SUBTITLES_NAME)) {
      SeriesRecord series = Series.getCurrentSerial();
      String link = series.getLink().trim();
      boolean updateLink = false;
      if (link.startsWith("http://www.tvsubtitles.net")) {
        link = link.replaceAll("(http://www.tvsubtitles.net/tvshow-)|(.html)", "");
        updateLink = true;
      }
      if (MyUsefulFunctions.isNumeric(link)) {
        link = link + "-" + series.getSeason();
        updateLink = true;
      }
      if (!MyUsefulFunctions.isNumeric(link.replace("-", ""))) {
        link = "";
        updateLink = true;
      }
      if (link.equals("")) {
        GetTvSubtitlesCode s = new GetTvSubtitlesCode(Series.getCurrentSerial());
        link = s.tSubCode;
        updateLink = true;
      }
      if (updateLink) {
        series.setLink(link);
        try {
          series.save();
        } catch (SQLException ex) {
          MyMessages.error("SQL Error", "Could not update series link");
          myseries.MySeries.logger.log(Level.WARNING, "Could not update series link", ex);
        }
      }
      if (link != null && !link.equals("")) {
        TvSubtitlesForm d = new TvSubtitlesForm(
                "http://www.tvsubtitles.net/tvshow-" + link + ".html",
                Series.getCurrentSerial().getSeason(),
                Episodes.getCurrentEpisode().getEpisode(),
                Series.getCurrentSerial().getLocalDir(),
                Episodes.getCurrentEpisode().getTitle());
      }
    } else if (Options.toString(Options.SUBTITLE_SITE).equals(Subtitle.SUBTITLE_ONLINE_NAME)) {
      String sOnlineCode = Series.getCurrentSerial().getSOnline().trim();
      if (sOnlineCode.equals("")) {
        GetSOnlineCode s = new GetSOnlineCode(Series.getCurrentSerial());
        sOnlineCode = s.sOnlineCode;
        if (!sOnlineCode.equals("")) {
          SeriesRecord ser = Series.getCurrentSerial();
          ser.setSOnline(sOnlineCode);
          try {
            ser.save();
          } catch (SQLException ex) {
            myseries.MySeries.logger.log(Level.WARNING, "Could not save sOnlineCode", ex);
          }
          getSOnlineSubtitle(sOnlineCode);
        }
      } else {
        getSOnlineSubtitle(sOnlineCode);
      }
    }
  }

  private static void getSOnlineSubtitle(String sOnlineCode) {
    SOnlineForm d = new SOnlineForm(
            sOnlineCode,
            Series.getCurrentSerial().getSeason(),
            Episodes.getCurrentEpisode().getEpisode(),
            Series.getCurrentSerial().getLocalDir(),
            Episodes.getCurrentEpisode().getTitle());
  }

  public static void downloadEpisodesTorrent() {
    SeriesRecord series = Series.getCurrentSerial();
    EpisodesRecord episode = Episodes.getCurrentEpisode();
    new EzTvForm(series, episode);
  }

  public static void downloadTorrent() {
    new EzTvForm();
  }

  public void start(final Runnable r) {
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {
        Thread t = new Thread(r);
        t.start();
      }
    });
  }
}
