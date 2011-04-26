/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.episodes;

import java.util.logging.Level;
import myseries.series.Series;
import tools.MySeriesLogger;
import database.DBConnection;
import database.Database;
import database.EpisodesRecord;
import database.SeriesRecord;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import myComponents.MyTableModels.MyEpisodesTableModel;
import myComponents.MyUsefulFunctions;
import myComponents.myFileFilters.ZipFilter;
import tools.Unziper;
import tools.download.subtitles.SubtitleMover;
import tools.languages.LangsList;
import tools.languages.Language;
import tools.options.Options;

/**
 * The episodes table class
 * @author ssoldatos
 */
public class Episodes {

  /** The number of columns   */
  public static final int NUMBER_OF_COLUMS = 7;
  /** The episodes number table column : 0   */
  public static final int EPISODE_NUM_COLUMN = 0;
  /** The episodes title table column : 1   */
  public static final int EPISODERECORD_COLUMN = 1;
  /** The episodes aired table column : 2   */
  public static final int AIRED_COLUMN = 2;
  /** The episodes downloaded table column : 3   */
  public static final int DOWNLOADED_COLUMN = 3;
  /** The episodes sub status table column : 4   */
  public static final int SUBS_COLUMN = 4;
  /** The episodes seen table column : 5   */
  public static final int SEEN_COLUMN = 5;
  /** The episodes rate table column : 6   */
  public static final int RATE_COLUMN = 6;
  /** The Episodes number column title : Episode Number   */
  public static final String EPISODE_NUM_COLUMN_TITLE = "Episode";
  /** The episodes title table column title : Title   */
  public static final String EPISODERECORD_COLUMN_TITLE = "Title";
  /** The episodes aired table column title : Aired   */
  public static final String AIRED_COLUMN_TITLE = "Aired";
  /** The episodes downloaded table column  title: Video File   */
  public static final String DOWNLOADED_COLUMN_TITLE = "Video File";
  /** The episodes sub status table column title : Subtitle   */
  public static final String SUBS_COLUMN_TITLE = "Subtitle";
  /** The episodes seen table column title : Seen   */
  public static final String SEEN_COLUMN_TITLE = "Watched";
  /** The episodes rate table column title : rate   */
  public static final String RATE_COLUMN_TITLE = "Rate";
  /** The episodes table model   */
  //private static MyEpisodesTableModel tableModel_episodes;
  /** The episodes table   */
  //private static JTable table_episodesList = new JTable();
  /** The current episode   */
  private static EpisodesRecord currentEpisode;

//  /** @return the tableModel_episodes   */
//  public static MyEpisodesTableModel getTableModel_episodes() {
//    return tableModel_episodes;
//  }
//
//  /**
//   * Sets the model for the episodes table
//   * @param aTableModel_episodes the tableModel_episodes to set
//   */
//  public static void setTableModel_episodes(MyEpisodesTableModel aTableModel_episodes) {
//    tableModel_episodes = aTableModel_episodes;
//  }

  public static void setTableWidths(JTable table, Integer[] EpisodesTableWidths) {
    TableColumnModel model = table.getColumnModel();
    for (int i = 0; i < EpisodesTableWidths.length; i++) {
      Integer width = EpisodesTableWidths[i];
      model.getColumn(i).setPreferredWidth(width);
    }
  }

  private static void unzipSubtitleFiles(SeriesRecord series) {
    if (!series.isValidLocalDir()) {
      return;
    }
    File dir = new File(series.getLocalDir());
    File[] subs = dir.listFiles(new ZipFilter());
    for (int i = 0; i < subs.length; i++) {
      File file = subs[i];
      if (file.isFile()) {
        Unziper u = new Unziper(series.getLocalDir(), file, true, Unziper.SUBTITLES);
        try {
          if (u.unzip()) {
            if (!u.unzippedFiles.isEmpty()) {
              MySeriesLogger.logger.log(Level.INFO, "Unzipped {0}", u.unzippedFiles);
              for (Iterator<String> it = u.unzippedFiles.iterator(); it.hasNext();) {
                String filename = it.next();
                if(Options.toBoolean(Options.AUTO_RENAME_SUBS) && MyUsefulFunctions.renameEpisode(series,filename)){
                  MySeriesLogger.logger.log(Level.INFO, "Subtitle renamed");
                }
              }

            }
          }
        } catch (Exception ex) {
          MySeriesLogger.logger.log(Level.SEVERE, "Could not unzip " + file, ex);
        }
      }
    }
  }

  private Episodes() {
  }

  /**
   * Sets the current episode
   * @param episode The episode id
   * @throws java.sql.SQLException
   */
  public static void setCurrentEpisode(int episode) throws SQLException {
    MySeriesLogger.logger.log(Level.INFO, "Setting the current episode");
    String sql = "SELECT * FROM episodes "
        + "WHERE series_ID = " + Series.getCurrentSerial().getSeries_ID() + " AND episode = " + episode;
    ResultSet rs = EpisodesRecord.query(new DBConnection().stmt, sql);
    if (rs.next()) {
      currentEpisode = new EpisodesRecord();
      getCurrentEpisode().setEpisode_ID(rs.getInt("episode_ID"));
      getCurrentEpisode().setSeries_ID(rs.getInt("series_ID"));
      getCurrentEpisode().setEpisode(rs.getInt("episode"));
      getCurrentEpisode().setTitle(rs.getString("title").trim());
      getCurrentEpisode().setAired(rs.getString("aired").trim());
      getCurrentEpisode().setDownloaded(rs.getInt("downloaded"));
      getCurrentEpisode().setSubs(LangsList.getLanguageById(rs.getInt("subs")));
      getCurrentEpisode().setSeen(rs.getInt("seen"));
      getCurrentEpisode().setRate(rs.getDouble("rate"));
      MySeriesLogger.logger.log(Level.FINE, "Current episode set to {0}",getCurrentEpisode().getTitle());
    }
    rs.close();
  }

  /**
   * Gets all the episodes of the current series
   * First empty episodes, create the model and apply it to the episodes table
   * Then prints the series fulltitle in the tabbed panel
   * @return An arraylist of all the episodes records
   * @throws java.sql.SQLException
   */
  public static ArrayList<EpisodesRecord> getCurrentSeriesEpisodes(JTable episodesTable) throws SQLException {
    ArrayList<EpisodesRecord> eps = new ArrayList<EpisodesRecord>();
    ArrayList<EpisodesRecord> updated = new ArrayList<EpisodesRecord>();
    File[] subtitleFiles = null;
    File[] videoFiles = null;
    int episode;
    Boolean download, seen;
    String title, aired;
    Language subs;
    emptyEpisodes(episodesTable);
    SeriesRecord series = Series.getCurrentSerial();
    DefaultTableModel model = (DefaultTableModel) episodesTable.getModel();
    MySeriesLogger.logger.log(Level.INFO, "Getting episodes of series {0}",series.getFullTitle());
    DBConnection conn = new DBConnection();
    if (Options.toBoolean(Options.AUTO_FILE_UPDATING) && series.isValidLocalDir()) {
      MySeriesLogger.logger.log(Level.INFO, "File auto updating is active");
      ArrayList<SeriesRecord> list = new ArrayList<SeriesRecord>();
      list.add(series);
      SubtitleMover sm = new SubtitleMover(list);
      sm.move();
      if (Options.toBoolean(Options.AUTO_EXTRACT_ZIPS)) {
        MySeriesLogger.logger.log(Level.INFO, "Auto extracting subtitles is active");
        unzipSubtitleFiles(series);
      }
      subtitleFiles = Series.getSubtitleFiles(series);
      videoFiles = Series.getVideoFiles(series);
    }
    if (Series.getCurrentSerial() == null) {
    }
    String sql = "SELECT * FROM episodes WHERE series_ID = " + Series.getCurrentSerial().getSeries_ID()
        + " ORDER BY CAST(episode AS UNSIGNED) ASC";
    Statement stmt = conn.stmt;
    ResultSet rs = stmt.executeQuery(sql);
    while (rs.next()) {
      EpisodesRecord e = new EpisodesRecord();
      e.setSeries_ID(rs.getInt("series_ID"));
      e.setEpisode_ID(rs.getInt("episode_ID"));
      e.setTitle(rs.getString("title"));
      aired = rs.getString("aired");
      e.setAired(rs.getString("aired"));
      episode = rs.getInt("episode");
      e.setEpisode(rs.getInt("episode"));
      download = rs.getBoolean("downloaded");
      e.setDownloaded(rs.getInt("downloaded"));
      e.setSeen(rs.getInt("seen"));
      e.setRate(rs.getDouble("rate"));
      e.setSubs(LangsList.getLanguageById(rs.getInt("subs")));
      boolean newDownloadedStatus = download;
      Language cSubs = e.getSubs();
      if (MyUsefulFunctions.hasBeenAired(e.getAired(), true)) {
        seen = rs.getBoolean("seen");
        //Video files
        if (videoFiles != null) {
          newDownloadedStatus = checkDownloads(Series.getCurrentSerial().getSeason(), e.getEpisode(), videoFiles);

        }
        // Subs fuiles
        if (subtitleFiles != null) {
          cSubs = checkSubs(Series.getCurrentSerial().getSeason(), e.getEpisode(), subtitleFiles);
        }
      } else {
        newDownloadedStatus = false;
        cSubs = LangsList.NONE;
        seen = false;
      }
      if (download != newDownloadedStatus) {
        e.setDownloaded(newDownloadedStatus ? EpisodesRecord.DOWNLOADED : EpisodesRecord.NOT_DOWNLOADED);
        updated.add(e);
        download = newDownloadedStatus;
      }
      if (cSubs != e.getSubs()) {
        e.setSubs(cSubs);
        updated.add(e);
      }
      subs = e.getSubs();
      Object[] data = {episode, e, e.getAired(), download, e.getSubs(), seen, e.getRate()};
      model.addRow(data);
      eps.add(e);
    }
    rs.close();
    MySeriesLogger.logger.log(Level.FINE, "Found {0} episodes",eps.size());
    if (!updated.isEmpty()) {
      MySeriesLogger.logger.log(Level.INFO, "Updating episodes");
      conn.beginTransaction();
      //System.out.println(System.currentTimeMillis());
      for (Iterator<EpisodesRecord> it = updated.iterator(); it.hasNext();) {
        EpisodesRecord episodesRecord = it.next();
        episodesRecord.save(stmt);
        MySeriesLogger.logger.log(Level.FINE, "Updating {0}",episodesRecord.getTitle());
      }
      conn.endTransaction();
       //System.out.println(System.currentTimeMillis());
      MySeriesLogger.logger.log(Level.FINE, "Updating finished");
    }
    episodesTable.setModel(model);
    conn.close();
    return eps;
  }

  public static boolean checkDownloads(SeriesRecord series, EpisodesRecord e) {
    int season = series.getSeason();
    int episode = e.getEpisode();
    MySeriesLogger.logger.log(Level.INFO, "Checking downloaded of series {0} episode {1}",
        new String[] {series.getFullTitle(),e.getTitle()});
    File[] videoFiles = Series.getVideoFiles(series);
    try {
      return checkDownloads(season, episode, videoFiles);
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
      return false;
    }
  }

  private static boolean checkDownloads(int season, int episode, File[] videoFiles) throws SQLException {
    if (videoFiles == null) {
      MySeriesLogger.logger.log(Level.WARNING, "No video files found");
      return false;
    }
    String regex = MyUsefulFunctions.createRegex(season, episode);
    String regexFake = MyUsefulFunctions.createRegex(season, season * 10 + episode);
    Pattern pattern = Pattern.compile(regex);
    Pattern patternFake = Pattern.compile(regexFake);
    MySeriesLogger.logger.log(Level.INFO, "Getting video files  of season {0} episode {1}",
        new int[] {season,episode});
    for (int j = 0; j < videoFiles.length; j++) {
      File file = videoFiles[j];
      Matcher matcher = pattern.matcher(file.getName());
      Matcher matcherFake = patternFake.matcher(file.getName());
      if (matcher.find() && !matcherFake.find()) {
        MySeriesLogger.logger.log(Level.FINE, "Video file  found {0}",file.getName());
        return true;
      }
    }
    return false;
  }

  private static Language checkSubs(int season, int episode, File[] subtitleFiles) throws SQLException {
    boolean hasPrimary = false, hasSecondary = false, hasOther = false;
    int subsFound = 0, totalSubs = 0;
    Language other = LangsList.NONE;
    String regex = MyUsefulFunctions.createRegex(season, episode);
    String regexFake = MyUsefulFunctions.createRegex(season, season * 10 + episode);
    Pattern pattern = Pattern.compile(regex);
    Pattern patternFake = Pattern.compile(regexFake);
    MySeriesLogger.logger.log(Level.INFO, "Getting subtitle files  of season {0} episode {1}",
        new int[] {season,episode});
    for (int j = 0; j < subtitleFiles.length; j++) {
      File file = subtitleFiles[j];
      Matcher matcher = pattern.matcher(file.getName());
      Matcher matcherFake = patternFake.matcher(file.getName());
      if (matcher.find() && file.isFile() && !matcherFake.find()) {
        totalSubs++;
        for (Iterator it = myseries.MySeries.languages.getLangs().iterator(); it.hasNext();) {
          Language lang = (Language) it.next();
          if (file.getName().indexOf("." + lang.getCode() + ".") > 0) {
            if (lang.isIsPrimary()) {
              subsFound++;
              hasPrimary = true;
            } else if (lang.isIsSecondary()) {
              hasSecondary = true;
              subsFound++;
            } else {
              hasOther = true;
              other = lang;
              subsFound++;
            }
          }
        }
      }
    }
    if (subsFound < totalSubs) {
      hasPrimary = true;
    }
    if (hasPrimary && hasSecondary) {
      MySeriesLogger.logger.log(Level.INFO, "Found multiple subtitles");
      return LangsList.MULTIPLE;
    } else if (hasPrimary) {
      MySeriesLogger.logger.log(Level.INFO, "Found primary subtitle");
      return myseries.MySeries.languages.getPrimary();
    } else if (hasSecondary) {
      MySeriesLogger.logger.log(Level.INFO, "Found secondary subtitle");
      return myseries.MySeries.languages.getSecondary();
    } else if (hasOther) {
      MySeriesLogger.logger.log(Level.INFO, "Found other subtitle");
      return other;
    }
    return LangsList.NONE;
  }

  /**
   * Updates the episodes table with the current series episodes
   * @throws java.sql.SQLException
   */
  public static void updateEpisodesTable(JTable table) throws SQLException {
    MySeriesLogger.logger.log(Level.INFO, "Updating episodes table");
    Episodes.getCurrentSeriesEpisodes(table);
  }

  /**
   * Empty the episodes table and sets the tabbed pane title to empty string
   */
  public static void emptyEpisodes(JTable episodesTable) {
    MySeriesLogger.logger.log(Level.INFO, "Emptying episodes table");
    ((DefaultTableModel)episodesTable.getModel()).setRowCount(0);
    //getTabsPanel().setTitleAt(0, "");
  }

//  /**
//   * @return the tabsPanel
//   */
//  public static JTabbedPane getTabsPanel() {
//    return myseries.MySeries.tabsPanel;
//  }

//  /**
//   * @param tabsPanel the tabsPanel to set
//   */
//  public static void setTabsPanel(JTabbedPane tabsPanel) {
//    Episodes.tabsPanel = tabsPanel;
//  }
  /**
   * @return the currentEpisode
   */
  public static EpisodesRecord getCurrentEpisode() {
    return currentEpisode;
  }
}
