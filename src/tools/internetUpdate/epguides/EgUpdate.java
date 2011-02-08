/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.internetUpdate.epguides;

import database.DBHelper;
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
  public EgUpdate(InternetUpdate iu) {
    this.iu = iu;
    this.list = new ArrayList<AbstractSeriesToUpdate>();
    this.site = InternetUpdate.EP_GUIDES_NAME;
  }

  protected boolean read(SeriesRecord series) {
    isConected = MyUsefulFunctions.hasInternetConnection(InternetUpdate.EP_GUIDES_URL);
    BufferedReader in = null;
    URL epGuides;
    StringBuffer buf = new StringBuffer();
    if (!isConected) {
      MySeries.logger.log(Level.WARNING, "Could not connect to internet");
      MyMessages.internetError();
      return false;
    }
    try {
      list.add(new EgSeriesToUpdate(series));
      epGuides = new URL(InternetUpdate.EP_GUIDES_URL + series.getTitle().toLowerCase().replaceAll(" ", "") + "/");
      in = new BufferedReader(new InputStreamReader(epGuides.openStream()));
    } catch (IOException ex) {
      append("<span style='color:red'>(" + series.getFullTitle() + ") could not  find webpage: " + InternetUpdate.EP_GUIDES_URL + series.getTitle().replaceAll(" ", "") + "/" + " </span>");
      MySeries.logger.log(Level.WARNING, "(" + series.getTitle() + ") could not " + InternetUpdate.EP_GUIDES_URL + series.getTitle().replaceAll(" ", "") + "/", ex);
      if (series.getTitle().toLowerCase().startsWith("the")) {
        String newTitle = series.getTitle().toLowerCase().replaceFirst(" ", "").replaceAll("((?i)^the)|( )", "");
        try {
          append("<span> Trying for: " + newTitle + "</span>");
          epGuides = new URL(InternetUpdate.EP_GUIDES_URL + newTitle + "/");
          in = new BufferedReader(new InputStreamReader(epGuides.openStream()));
        } catch (IOException ex1) {
          append("<span style='color:red'>(" + series.getFullTitle() + ") could not  find webpage: " + InternetUpdate.EP_GUIDES_URL + newTitle + "/" + " </span>");
          MySeries.logger.log(Level.WARNING, "(" + series.getTitle() + ") could not " + InternetUpdate.EP_GUIDES_URL + newTitle + "/", ex);
          return false;
        } catch (IllegalArgumentException ex1) {
          MySeries.logger.log(Level.SEVERE, "Illegal argument exception.Possibly the update was cancelled", ex);
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
      MySeries.logger.log(Level.WARNING, "(" + series.getTitle() + ") could not " + InternetUpdate.EP_GUIDES_URL + series.getTitle().replaceAll(" ", "") + "/", ex);
    } catch (NullPointerException ex) {
    } catch (IllegalArgumentException ex) {
      MySeries.logger.log(Level.SEVERE, "Illegal argument exception.Possibly the update was cancelled", ex);
      return false;
    }
    return true;
  }

  private boolean isSeasonRight(String inputLine, int season) {
    String[] lineArr;
    int s = -1;

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
        return true;
      }
    } else {
      return rightSeason;
    }
    return false;
  }

  protected void updateEpisodes() throws SQLException {
    append("<span style='font-weight:bold;font-size:12px'>Importing data</span>");
    EgEpisodeOld curData;
    String[] arr;
    String line;
    int linesParsed = 0;
    int perc;
    iu.progress_bar.setIndeterminate(false);
    iu.progress_bar.setString("0%");

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
          MySeries.logger.log(Level.INFO, "Importing episodes of " + curSeries);
          header = false;
        }
        Database.beginTransaction();
        for (int e = 0; e < curSeries.episodes.size(); e++) {
          boolean save = false;
          EgEpisode episode = curSeries.episodes.get(e);
          int number = episode.number;
          String title = episode.title.trim();
          String airDate = episode.airDate;
          Vector<EpisodesRecord> episodes = DBHelper.getEpisodesBySql("SELECT * FROM episodes WHERE series_ID = " + series.getSeries_ID()
                  + " AND episode = " + number + " LIMIT 1");
          EpisodesRecord episodeRecord;
          if (episodes.size() == 0) {
            newEpisodes++;
            save = true;
            episodeRecord = new EpisodesRecord();
            append("<b>&nbsp;&nbsp;&nbsp;&nbsp;New Episode: " + number + ". " + title + " (Inserted)</b>");
          } else {
            episodeRecord = episodes.get(0);
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
        Database.endTransaction();
        if (newEpisodes == 0 && updEpisodes == 0) {
          append("No new or updated episodes");
        } else {
          append(newEpisodes + " new episodes and " + updEpisodes + " updates");
        }
      }

    }
    iu.progress_bar.setValue(100);
    iu.progress_bar.setString("100%");
    Episodes.updateEpisodesTable();
    append("<br><br>Internet update of series completed in " + calcExecTime());
    iu.finished = true;
  }
}
