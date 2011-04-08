/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.actions;

import database.DBConnection;
import database.EpisodesRecord;
import database.SeriesRecord;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myseries.MySeries;
import myseries.episodes.AdminEpisodes;
import myseries.episodes.*;
import myseries.episodes.Video;
import myseries.filters.Filters;
import myseries.series.Series;
import tools.download.subtitles.SubtitleConstants;
import tools.download.subtitles.sonline.GetSubtitleOnlineCode;
import tools.download.subtitles.sonline.SOnlineForm;
import tools.download.subtitles.tvsubtitles.GetTvSubtitlesCode;
import tools.download.subtitles.tvsubtitles.TvSubtitlesForm;
import tools.download.torrents.TorrentConstants;
import tools.download.torrents.eztv.EzTvForm;
import tools.download.torrents.isohunt.IsohuntForm;
import tools.importExport.ExportEpisodes;
import tools.importExport.ImportEpisodes;
import tools.options.Options;
import tools.renaming.RenameEpisodes;
import tools.MySeriesLogger;

/**
 *
 * @author ssoldatos
 */
public class EpisodesActions {

  public static void AddEpisode(MySeries m) {
    try {
      MySeries.glassPane.activate(null);
      MySeriesLogger.logger.log(Level.INFO, "Showing admin episodes panel");
      AdminEpisodes e = new AdminEpisodes(m, Series.getCurrentSerial(), null);
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "IO exception occured", ex);
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
    }
  }

  public static void AddEpisodeInEpisodes(MySeries m) {
    try {
      MySeries.glassPane.activate(null);
      MySeriesLogger.logger.log(Level.INFO, "Showing admin episodes panel");
      AdminEpisodes e = new AdminEpisodes(m, Series.getCurrentSerial(), null);
     } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "IO exception occured", ex);
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
    }
  }

  public static void exportEpisodes() {
    MySeriesLogger.logger.log(Level.INFO, "Showing export episodes panel");
    new ExportEpisodes();
  }

  public static void importEpisodes(MySeries m) {
    try {
      MySeries.glassPane.activate(null);
      MySeriesLogger.logger.log(Level.INFO, "Showing import episodes panel");
      new ImportEpisodes(m);
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.WARNING, "Error while importing the episodes", ex);
      MyMessages.error("SQL Error", "There was an error when importing the episodes");
    }
  }

  public static void viewEpisode() {
    File localDir = new File(Series.getCurrentSerial().getLocalDir().trim());
    int season = Series.getCurrentSerial().getSeason();
    int episode = Episodes.getCurrentEpisode().getEpisode();
    MySeriesLogger.logger.log(Level.INFO, "Viewing episode {0} of series {1}",
        new String[]{Series.getCurrentSerial().getFullTitle(),Episodes.getCurrentEpisode().getTitle()});
    String regex = MyUsefulFunctions.createRegex(season, episode);
    String regexFake = MyUsefulFunctions.createRegex(season,season*10+ episode);
    Video.getVideos(localDir, regex, regexFake);
  }

  public static void deleteEpisode() {
    String title = Episodes.getCurrentEpisode().getTitle();
    int episode_ID = Episodes.getCurrentEpisode().getEpisode_ID();
    MySeriesLogger.logger.log(Level.INFO, "Deleting episode {0}",title);
    int answ = MyMessages.question("Delete Episode?", "Really delete the episode " + title + "?");
    if (answ == JOptionPane.YES_OPTION) {
      try {
        String sql = "DELETE FROM episodes WHERE episode_ID = " + episode_ID;
        DBConnection.stmt.execute(sql);
        MySeriesLogger.logger.log(Level.FINE, "Episode deleted");
        Episodes.updateEpisodesTable();
      } catch (SQLException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "Could not delete episode", ex);
      }
    } else {
      MySeriesLogger.logger.log(Level.INFO, "Action aborted by the user");
    }
  }

  public static void deleteEpisodes(ArrayList<EpisodesRecord> episodes) {
    MySeriesLogger.logger.log(Level.INFO, "Deleting {0} episodes ",episodes.size());
    int answ = MyMessages.question("Delete Episode?", "Really delete the selected episodes ?");
    if (answ == JOptionPane.YES_OPTION) {
      for (Iterator<EpisodesRecord> it = episodes.iterator(); it.hasNext();) {
        EpisodesRecord e = it.next();
        MySeriesLogger.logger.log(Level.INFO, "Deleting episode {0}" + e.getTitle());
        String sql = "DELETE FROM episodes WHERE episode_ID = " + e.getEpisode_ID();
        try {
          DBConnection.stmt.execute(sql);
          MySeriesLogger.logger.log(Level.FINE, "Episode deleted");
        } catch (SQLException ex) {
          MySeriesLogger.logger.log(Level.SEVERE, "Could not delete episode " + e.getTitle(), ex);
        }
      }
      try {
        Episodes.updateEpisodesTable();
      } catch (SQLException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
      }
    }else {
      MySeriesLogger.logger.log(Level.INFO, "Action aborted by the user");
    }
  }

  public static void renameEpisode() {
    ArrayList<File> oldNames = new ArrayList<File>();
    ArrayList<EpisodesRecord> newNames = new ArrayList<EpisodesRecord>();
    SeriesRecord series = Series.getCurrentSerial();
    int season = series.getSeason();
    EpisodesRecord episodeRecord = Episodes.getCurrentEpisode();
    MySeriesLogger.logger.log(Level.INFO, "Renaming episode {1} of {0}",
        new String[] {series.getFullTitle(),episodeRecord.getTitle()});
    File dir = new File(series.getLocalDir());
    File[] files = dir.listFiles();
    String path;
    int episode = episodeRecord.getEpisode();
    String regex = MyUsefulFunctions.createRegex(season, episode);
    String regexFake = MyUsefulFunctions.createRegex(season,season*10+ episode);
    Pattern pattern = Pattern.compile(regex);
    Pattern patternFake = Pattern.compile(regexFake);
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      if (file != null && file.isFile()) {
        path = file.getParent();
        String name = file.getName();
        Matcher matcher = pattern.matcher(name);
        Matcher matcherFake = patternFake.matcher(name);
        if (matcher.find() &&!matcherFake.find()) {
          String[] tokens = name.split("\\.", -1);
          String ext = tokens[tokens.length - 1];
          if (MyUsefulFunctions.isSubtitle(name)) {
            if (tokens[tokens.length - 2].equals("gr") || tokens[tokens.length - 2].equals("en")) {
              ext = tokens[tokens.length - 2] + "." + tokens[tokens.length - 1];
            }
          }
          String sample = "";
          if (name.indexOf("sample") > -1) {
            sample = "_sample";
          }
          String newFilename = series.getTitle()
                  + Options.toString(Options.SEASON_SEPARATOR, false) + MyUsefulFunctions.padLeft(series.getSeason(), 2, "0")
                  + Options.toString(Options.EPISODE_SEPARATOR, false) + MyUsefulFunctions.padLeft(episodeRecord.getEpisode(), 2, "0")
                  + Options.toString(Options.TITLE_SEPARATOR, false) + episodeRecord.getTitle();

          String newName = path + "/" + newFilename + sample + "." + ext;
          File newFile = new File(newName);
          oldNames.add(files[i]);
          newNames.add(episodeRecord);
          files[i] = null;
        }
      }
    }
    if (oldNames.size() > 0) {
      MySeriesLogger.logger.log(Level.INFO, "{0} files to rename",newNames.size());
      MySeriesLogger.logger.log(Level.INFO, "Showing Rename panel");
      RenameEpisodes r = new RenameEpisodes(oldNames, newNames, series);
    } else {
      MySeriesLogger.logger.log(Level.INFO, "No files to rename");
      MyMessages.message("No files to rename", "There are no available files to rename");
    }
  }

  public static void renameEpisodes() {
    try {
      ArrayList<File> oldNames = new ArrayList<File>();
      ArrayList<EpisodesRecord> newNames = new ArrayList<EpisodesRecord>();
      SeriesRecord series = Series.getCurrentSerial();
      int season = series.getSeason();
      File dir = new File(series.getLocalDir());
      MySeriesLogger.logger.log(Level.INFO, "Renaming all episodes of series {0}",series.getFullTitle());
      if (!dir.isDirectory()) {
        myseries.MySeries.glassPane.activate(null);
        MySeriesLogger.logger.log(Level.WARNING, "No local directory for series {0}",series.getFullTitle());
        MyMessages.error("Not a directory", "The local directory is not found");
        myseries.MySeries.glassPane.deactivate();
        return;
      }
      File[] files = dir.listFiles();
      String path;

      ArrayList<EpisodesRecord> episodes = Episodes.getCurrentSeriesEpisodes();
      for (Iterator<EpisodesRecord> it = episodes.iterator(); it.hasNext();) {
        EpisodesRecord episodesRecord = it.next();
        int episode = episodesRecord.getEpisode();
        String regex = MyUsefulFunctions.createRegex(season, episode);
        String regexFake = MyUsefulFunctions.createRegex(season,season*10+ episode);
       Pattern pattern = Pattern.compile(regex);
       Pattern patternFake = Pattern.compile(regexFake);
        for (int i = 0; i < files.length; i++) {
          File file = files[i];
          if (file != null && file.isFile()) {
            path = file.getParent();
            String name = file.getName();
            Matcher matcher = pattern.matcher(name);
            Matcher matcherFake = patternFake.matcher(name);
            if (matcher.find() &&!matcherFake.find()) {
              String[] tokens = name.split("\\.", -1);
              String ext = tokens[tokens.length - 1];
              if (MyUsefulFunctions.isSubtitle(name)) {
                if (tokens[tokens.length - 2].equals("gr") || tokens[tokens.length - 2].equals("en")) {
                  ext = tokens[tokens.length - 2] + "." + tokens[tokens.length - 1];
                }
              }

              String sample = "";
              if (name.indexOf("sample") > -1) {
                sample = "_sample";
              }

              String newFilename = series.getTitle()
                      + Options.toString(Options.SEASON_SEPARATOR, false) + MyUsefulFunctions.padLeft(series.getSeason(), 2, "0")
                      + Options.toString(Options.EPISODE_SEPARATOR, false) + MyUsefulFunctions.padLeft(episodesRecord.getEpisode(), 2, "0")
                      + Options.toString(Options.TITLE_SEPARATOR, false) + episodesRecord.getTitle();

              String newName = path + "/" + newFilename + sample + "." + ext;
              File newFile = new File(newName);
              oldNames.add(files[i]);
              newNames.add(episodesRecord);
              files[i] = null;
            }
          }
        }
      }
      if (oldNames.size() > 0) {
        MySeriesLogger.logger.log(Level.FINE, "{0} files to rename",newNames.size());
        MySeriesLogger.logger.log(Level.INFO, "Showing rename panel");
        RenameEpisodes r = new RenameEpisodes(oldNames, newNames, series);
      } else {
        myseries.MySeries.glassPane.activate(null);
        MyMessages.message("No files to rename", "There are no available files to rename");
        myseries.MySeries.glassPane.deactivate();
      }
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
    }
  }

  public static void downloadSubtitles(String site) {
    MySeriesLogger.logger.log(Level.INFO, "Downloading subtitles from {0}",site);
    if (site.equals(SubtitleConstants.TV_SUBTITLES_NAME)) {
      SeriesRecord series = Series.getCurrentSerial();
      String link = series.getTvSubtitlesCode().trim();
      boolean updateLink = false;
      if (link.startsWith(SubtitleConstants.TV_SUBTITLES_URL)) {
        link = link.replaceAll("(" + SubtitleConstants.TV_SUBTITLES_URL + "/tvshow-)|(.html)", "");
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
        MySeriesLogger.logger.log(Level.INFO, "Updating link interface database to {0}",link);
        series.setTvSubtitlesCode(link);
        try {
          series.save();
        } catch (SQLException ex) {
          MyMessages.error("SQL Error", "Could not update series link");
          MySeriesLogger.logger.log(Level.WARNING, "Could not update series link", ex);
        }
      }
      if (link != null && !link.equals("")) {
        MySeriesLogger.logger.log(Level.INFO, "Showing Download subtitles from tvsubtitles panel");
        TvSubtitlesForm d = new TvSubtitlesForm(
                SubtitleConstants.TV_SUBTITLES_URL + "tvshow-" + link + ".html",
                Series.getCurrentSerial().getSeason(),
                Episodes.getCurrentEpisode().getEpisode(),
                Series.getCurrentSerial().getLocalDir(),
                Episodes.getCurrentEpisode().getTitle());
      }
    } else if (site.equals(SubtitleConstants.SUBTITLE_ONLINE_NAME)) {
      String sOnlineCode = Series.getCurrentSerial().getSOnlineCode().trim();
      if (sOnlineCode.equals("")) {
        MySeriesLogger.logger.log(Level.INFO, "Subtitles online code not found");
        MySeriesLogger.logger.log(Level.INFO, "Showing Get Subtitles online code panel");
        GetSubtitleOnlineCode s = new GetSubtitleOnlineCode(Series.getCurrentSerial());
        sOnlineCode = s.subtitleOnlineCode;
        if (!sOnlineCode.equals("")) {
          MySeriesLogger.logger.log(Level.FINE, "Sonline code found {0}",sOnlineCode);
          SeriesRecord ser = Series.getCurrentSerial();
          ser.setSOnlineCode(sOnlineCode);
          try {
            ser.save();
          } catch (SQLException ex) {
            MySeriesLogger.logger.log(Level.WARNING, "Could not save sOnlineCode", ex);
          }
          getSOnlineSubtitle(sOnlineCode);
        }
      } else {
        getSOnlineSubtitle(sOnlineCode);
      }
    }
    try {
      Episodes.updateEpisodesTable();
      Filters.getFilteredSeries();
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
    }
  }

  private static void getSOnlineSubtitle(String sOnlineCode) {
    MySeriesLogger.logger.log(Level.INFO, "Showing download from Subtitles online code panel");
    SOnlineForm d = new SOnlineForm(
            sOnlineCode,
            Series.getCurrentSerial().getSeason(),
            Episodes.getCurrentEpisode().getEpisode(),
            Series.getCurrentSerial().getLocalDir(),
            Episodes.getCurrentEpisode().getTitle());
  }

  public static void downloadEpisodesTorrent(String site) {
    SeriesRecord series = Series.getCurrentSerial();
    EpisodesRecord episode = Episodes.getCurrentEpisode();
    MySeriesLogger.logger.log(Level.INFO, "Downloading torrent from {0} for series {1} and episode {2}",
        new String[]{site,series.getFullTitle(),episode.getTitle()});
    if (site.equals(TorrentConstants.EZTV_NAME)) {
      MySeriesLogger.logger.log(Level.INFO, "Showing EzTv form");
      new EzTvForm(series, episode);
    } else if (site.equals(TorrentConstants.ISOHUNT_NAME)) {
      MySeriesLogger.logger.log(Level.INFO, "Showing Isohunt form");
      new IsohuntForm(series, episode);
    }
  }

  private EpisodesActions() {
  }
}
