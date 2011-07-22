/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.internetUpdate.epguides;

import Exceptions.DatabaseException;
import database.DBConnection;
import database.DBHelper;
import tools.MySeriesLogger;
import database.Database;
import database.EpisodesRecord;
import database.SeriesRecord;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JTable;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myseries.episodes.Episodes;
import myseries.MySeries;
import tools.internetUpdate.AbstractSeriesToUpdate;
import tools.internetUpdate.AbstractUpdate;
import tools.internetUpdate.InternetUpdate;

/**
 * Update series from epGuides
 * @author ssoldatos
 */
public class EgUpdate extends AbstractUpdate implements Runnable {

  private boolean rightSeason;
  private boolean newFormat;

  /**
   * Update series
   * @param iu The update series form
   */
  public EgUpdate(InternetUpdate iu, JTable episodesTable) {
    MySeriesLogger.logger.log(Level.INFO, "Updating series from EpGuide");
    this.iu = iu;
    this.list = new ArrayList<AbstractSeriesToUpdate>();
    this.site = InternetUpdate.EP_GUIDES_NAME;
    this.episodesTable = episodesTable;
  }

  protected boolean read(SeriesRecord series) {
    isConected = MyUsefulFunctions.hasInternetConnection(InternetUpdate.EP_GUIDES_URL);
    BufferedReader in = null;
    URL epGuides;
    StringBuilder buf = new StringBuilder();
    if (!isConected) {
      MyMessages.internetError();
      return false;
    }
    try {
      MySeriesLogger.logger.log(Level.INFO, "Updating episodes for series {0}", series.getFullTitle());
      list.add(new EgSeriesToUpdate(series));
      epGuides = new URL(InternetUpdate.EP_GUIDES_URL + series.getTitle().toLowerCase().replaceAll(" ", "") + "/");
      in = new BufferedReader(new InputStreamReader(epGuides.openStream()));
    } catch (IOException ex) {
      append("<span style='color:red'>(" + series.getFullTitle() + ") could not  find webpage: " + InternetUpdate.EP_GUIDES_URL + series.getTitle().replaceAll(" ", "") + "/" + " </span>");
      MySeriesLogger.logger.log(Level.WARNING, "(" + series.getTitle() + ") could not " + InternetUpdate.EP_GUIDES_URL + series.getTitle().replaceAll(" ", "") + "/", ex);
      if (series.getTitle().toLowerCase().startsWith("the")) {
        String newTitle = series.getTitle().toLowerCase().replaceFirst(" ", "").replaceAll("((?i)^the)|( )", "");
        try {
          append("<span> Trying for: " + newTitle + "</span>");
          epGuides = new URL(InternetUpdate.EP_GUIDES_URL + newTitle + "/");
          in = new BufferedReader(new InputStreamReader(epGuides.openStream()));
        } catch (IOException ex1) {
          append("<span style='color:red'>(" + series.getFullTitle() + ") could not  find webpage: " + InternetUpdate.EP_GUIDES_URL + newTitle + "/" + " </span>");
          MySeriesLogger.logger.log(Level.WARNING, "(" + series.getTitle() + ") could not " + InternetUpdate.EP_GUIDES_URL + newTitle + "/", ex);
          return false;
        } catch (IllegalArgumentException ex1) {
          MySeriesLogger.logger.log(Level.SEVERE, "Illegal argument exception.Possibly the update was cancelled", ex);
          return false;
        }
      }
    }
    String inputLine;
    boolean print = false;
    try {
      EgSeriesToUpdate egs = (EgSeriesToUpdate) list.get(list.size() - 1);
      while ((inputLine = in.readLine()) != null) {
        if (inputLine.indexOf("<div id=\"eplist\">") > -1
                || inputLine.indexOf("Episode #    Prod #    Air Date   Episode Title") > -1) {
          if (inputLine.indexOf("<div id=\"eplist\">") > -1) {
            newFormat = true;
          } else {
            newFormat = false;
          }
          print = true;
        } else if (inputLine.indexOf("</div>") > -1) {
          print = false;
        }
        if (print) {
          rightSeason = isSeasonRight(inputLine, series.getSeason());
          if (rightSeason) {
            totalLinesParsed++;
            egs.update = true;
            buf.append(inputLine = inputLine + "\n");
          }
        }
      }
      String[] lines = buf.toString().split("\n", -1);
      for (int i = 0; i < lines.length; i++) {
        String line = lines[i].trim();

        if (line.replaceAll("\\<.*?>", "").length() > 20) {
          if (MyUsefulFunctions.isNumeric(line.substring(0, 1))) {
            egs.episodes.add(new EgEpisode(line));
          }
        }
      }
      //list.add(new EgEpisodeOld(series, buf.toString()));
      in.close();
      append("<span style='color:green'>(" + series.getFullTitle() + ") - OK</span>");
    } catch (IOException ex) {
      append("<span style='color:red'>(" + series.getFullTitle() + ") could not  find webpage: " + InternetUpdate.EP_GUIDES_URL + series.getTitle().replaceAll(" ", "") + "/" + " </span>");
      MySeriesLogger.logger.log(Level.WARNING, "(" + series.getTitle() + ") could not " + InternetUpdate.EP_GUIDES_URL + series.getTitle().replaceAll(" ", "") + "/", ex);
    } catch (NullPointerException ex) {
    } catch (IllegalArgumentException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Illegal argument exception.Possibly the update was cancelled", ex);
      return false;
    }
    MySeriesLogger.logger.log(Level.FINE, "Series updated");
    return true;
  }

  private boolean isSeasonRight(String inputLine, int season) {
    String[] lineArr;
    int s = -1;
    MySeriesLogger.logger.log(Level.INFO, "Checking if season is right");
    if (inputLine.indexOf("Season") > -1 || inputLine.indexOf("Series") > -1) {
      lineArr = inputLine.split("Season", -1);
      if (lineArr.length == 1) {
        lineArr = inputLine.split("Series", -1);
      }
      try {
        s = Integer.parseInt(lineArr[1].trim());
      } catch (NumberFormatException ex) {
        try {
          s = Integer.parseInt(lineArr[0].trim().replaceAll("[a-z]", ""));
        } catch (NumberFormatException ex1) {
        }
      }
      if (season == s) {
        MySeriesLogger.logger.log(Level.FINE, "Right season {0}", s);
        return true;
      }
    } else {
      return rightSeason;
    }
    return false;
  }

  protected void updateEpisodes(JTable episodesTable) throws SQLException {
    append("<span style='font-weight:bold;font-size:12px'>Importing data</span>");
    EgEpisodeOld curData;
    String[] arr;
    String line;
    int linesParsed = 0;
    int perc;
    iu.progress_bar.setIndeterminate(false);
    iu.progress_bar.setString("0%");
    MySeriesLogger.logger.log(Level.INFO, "Updating all episodes");
    for (int i = 0; i < list.size(); i++) {
      int newEpisodes = 0;
      int updEpisodes = 0;
      boolean header = true;
      EgSeriesToUpdate curSeries = (EgSeriesToUpdate) list.get(i);
      if (curSeries.update) {
        series = curSeries.series;
        if (header) {
          iu.label_update_series.setText("Importing episodes of " + curSeries);
          append("<span><b>Importing episodes of " + curSeries + "</b></span>");
          MySeriesLogger.logger.log(Level.INFO, "Importing episodes of {0}", curSeries);
          header = false;
        }
        DBConnection.beginTransaction();
        try {
          for (int e = 0; e < curSeries.episodes.size(); e++) {
            boolean save = false;
            EgEpisode episode = curSeries.episodes.get(e);
            int number = episode.number;
            String title = episode.title.replaceAll("\\[.+?\\]", "").trim();
            String airDate = episode.airDate;
            EpisodesRecord episodeRecord = EpisodesRecord.queryOne(null,
                  EpisodesRecord.C_SERIES_ID +" = ? AND " + EpisodesRecord.C_EPISODE + " = ?",
                  new String[] {
                  String.valueOf(series.getSeries_ID()),
                  String.valueOf(number),
                  }
                  , null,null,null);
            
            if (episodeRecord == null) {
              newEpisodes++;
              save = true;
              episodeRecord = new EpisodesRecord();
              append("<b>&nbsp;&nbsp;&nbsp;&nbsp;New Episode: " + number + ". " + title + " (Inserted)</b>");
            } else {
              if (shouldSaveEpisode(episodeRecord, title, airDate)) {
                updEpisodes++;
                save = true;
                append("&nbsp;&nbsp;&nbsp;&nbsp;Episode: " + number + ". " + title + " (Updated)");
              }
            }
            if (save) {
              episodeRecord.setSeries_ID(series.getSeries_ID());
              episodeRecord.setEpisode(number);
              episodeRecord.setTitle(title);
              if (!airDate.trim().equals("")) {
                episodeRecord.setAired(airDate);
              }
              episodeRecord.save();
            }
          }
        } catch (SQLException ex) {
          throw ex;
        } finally {
          DBConnection.endTransaction();
        }
        if (newEpisodes == 0 && updEpisodes == 0) {
          MySeriesLogger.logger.log(Level.INFO, "No new or updated episodes");
          append("No new or updated episodes");
        } else {
          MySeriesLogger.logger.log(Level.INFO, "{0} new episodes and {1} updates", new Object[]{newEpisodes, updEpisodes});
          append(newEpisodes + " new episodes and " + updEpisodes + " updates");
        }
      }

    }
    iu.progress_bar.setValue(100);
    iu.progress_bar.setString("100%");
    Episodes.updateEpisodesTable(episodesTable);
    append("<br><br>Internet update of series completed in " + calcExecTime());
    MySeriesLogger.logger.log(Level.INFO, "<br><br>Internet update of series completed in {0}", calcExecTime());
    iu.finished = true;
  }
}
