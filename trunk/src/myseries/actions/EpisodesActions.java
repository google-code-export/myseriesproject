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
import tools.download.subtitles.sonline.GetSOnlineCode;
import tools.download.subtitles.sonline.SOnlineForm;
import tools.download.subtitles.tvsubtitles.GetTvSubtitlesCode;
import tools.download.subtitles.tvsubtitles.TvSubtitlesForm;
import tools.download.torrents.eztv.EzTvForm;
import tools.importExport.ExportEpisodes;
import tools.importExport.ImportEpisodes;
import tools.options.Options;
import tools.renaming.RenameEpisodes;

/**
 *
 * @author ssoldatos
 */
public class EpisodesActions {

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

  public static void viewEpisode() {
    File localDir = new File(Series.getCurrentSerial().getLocalDir().trim());
    int season = Series.getCurrentSerial().getSeason();
    int episode = Episodes.getCurrentEpisode().getEpisode();
    String regex = MyUsefulFunctions.createRegex(season, episode);
    Video.getVideos(localDir, regex);
  }

  public static void deleteEpisode() {
    String title = Episodes.getCurrentEpisode().getTitle();
    int episode_ID = Episodes.getCurrentEpisode().getEpisode_ID();
    int answ = MyMessages.question("Delete Episode?", "Really delete the episode " + title + "?");
    if (answ == JOptionPane.YES_OPTION) {
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

  public static void deleteEpisodes(ArrayList<EpisodesRecord> episodes) {
    int answ = MyMessages.question("Delete Episode?", "Really delete the selected episodes ?");
    if (answ == JOptionPane.YES_OPTION) {
      for (Iterator<EpisodesRecord> it = episodes.iterator(); it.hasNext();) {
        EpisodesRecord e = it.next();
        String sql = "DELETE FROM episodes WHERE episode_ID = " + e.getEpisode_ID();
        try {
          DBConnection.stmt.execute(sql);
        } catch (SQLException ex) {
          MySeries.logger.log(Level.SEVERE, null, ex);
        }
      }
      try {
        Episodes.updateEpisodesTable();
      } catch (SQLException ex) {
        MySeries.logger.log(Level.SEVERE, null, ex);
      }
    }
  }

  public static void renameEpisode() {
    ArrayList<File> oldNames = new ArrayList<File>();
    ArrayList<EpisodesRecord> newNames = new ArrayList<EpisodesRecord>();
    SeriesRecord series = Series.getCurrentSerial();
    int season = series.getSeason();
    EpisodesRecord episodeRecord = Episodes.getCurrentEpisode();
    File dir = new File(series.getLocalDir());
    File[] files = dir.listFiles();
    String path;
    int episode = episodeRecord.getEpisode();
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
              + Options.toString(Options.SEASON_SEPARATOR, false) + MyUsefulFunctions.padLeft(series.getSeason(), 2, "0")
              + Options.toString(Options.EPISODE_SEPARATOR, false) + MyUsefulFunctions.padLeft(episodeRecord.getEpisode(), 2, "0")
              + Options.toString(Options.TITLE_SEPARATOR, false) + episodeRecord.getTitle();

          String newName = path + "/" + newFilename + "." + ext;
          File newFile = new File(newName);
          oldNames.add(files[i]);
          newNames.add(episodeRecord);
          files[i] = null;
        }
      }
    }
    if (oldNames.size() > 0) {
      RenameEpisodes r = new RenameEpisodes(oldNames, newNames, series);
    } else {
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
                  + Options.toString(Options.SEASON_SEPARATOR, false) + MyUsefulFunctions.padLeft(series.getSeason(), 2, "0")
                  + Options.toString(Options.EPISODE_SEPARATOR, false) + MyUsefulFunctions.padLeft(episodesRecord.getEpisode(), 2, "0")
                  + Options.toString(Options.TITLE_SEPARATOR, false) + episodesRecord.getTitle();

              String newName = path + "/" + newFilename + "." + ext;
              File newFile = new File(newName);
              oldNames.add(files[i]);
              newNames.add(episodesRecord);
              files[i] = null;
            }
          }
        }
      }
      if (oldNames.size() > 0) {
        RenameEpisodes r = new RenameEpisodes(oldNames, newNames, series);
      } else {
        MyMessages.message("No files to rename", "There are no available files to rename");
      }
    } catch (SQLException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }

  public static void downloadSubtitles(String site) {
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
        series.setTvSubtitlesCode(link);
        try {
          series.save();
        } catch (SQLException ex) {
          MyMessages.error("SQL Error", "Could not update series link");
          myseries.MySeries.logger.log(Level.WARNING, "Could not update series link", ex);
        }
      }
      if (link != null && !link.equals("")) {
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
        GetSOnlineCode s = new GetSOnlineCode(Series.getCurrentSerial());
        sOnlineCode = s.sOnlineCode;
        if (!sOnlineCode.equals("")) {
          SeriesRecord ser = Series.getCurrentSerial();
          ser.setSOnlineCode(sOnlineCode);
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
    try {
      Episodes.updateEpisodesTable();
      Filters.getFilteredSeries();
    } catch (SQLException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
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
}
