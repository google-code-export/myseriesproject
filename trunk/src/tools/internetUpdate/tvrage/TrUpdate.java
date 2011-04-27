/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.internetUpdate.tvrage;

import database.DBConnection;
import database.DBHelper;
import database.Database;
import database.EpisodesRecord;
import database.SeriesRecord;
import java.io.IOException;
import tools.MySeriesLogger;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JTable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myseries.*;
import myseries.episodes.Episodes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tools.internetUpdate.AbstractSeriesToUpdate;
import tools.internetUpdate.AbstractUpdate;
import tools.internetUpdate.InternetUpdate;

/**
 *
 * @author ssoldatos
 */
public class TrUpdate extends AbstractUpdate implements Runnable {

  /**
   * Construct a tr sereis updater
   * @param iu
   */
  public TrUpdate(InternetUpdate iu, JTable episodesTable) {
    this.iu = iu;
    this.list = new ArrayList<AbstractSeriesToUpdate>();
    this.site = InternetUpdate.TV_RAGE_NAME;
    this.episodesTable = episodesTable;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected boolean read(SeriesRecord series) {
    try {
      isConected = MyUsefulFunctions.hasInternetConnection(InternetUpdate.TV_RAGE_URL);
      if(!isConected){
        return false;
      }
        MySeriesLogger.logger.log(Level.INFO, "Updating series {0}",series.getFullTitle());
      this.series = series;
      list.add(new TrSeriesToUpdate(series));
      InputStream in = null;
      MySeriesLogger.logger.log(Level.INFO, "Getting the url");
      String url = InternetUpdate.TV_RAGE_EPISODE_LIST_URL + series.getTvrage_ID();
      MySeriesLogger.logger.log(Level.INFO, "Reading URL: {0}", url);
      URL tvRage = new URL(url);
      in = tvRage.openStream();
      readXML(in);
      in.close();
      append("<span style='color:green'>(" + series.getFullTitle() + ") - OK</span>");
      return true;
    } catch (ParserConfigurationException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not parse XML", ex);
      append("<span style='color:red'>(" + series.getFullTitle() + ") could not  get episodes </span>");
      return true;
    } catch (SAXException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not parse XML", ex);
      append("<span style='color:red'>(" + series.getFullTitle() + ") could not  get episodes </span>");
      return true;
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not read from tvrage", ex);
      append("<span style='color:red'>(" + series.getFullTitle() + ") could not  get episodes </span>");
      return true;
    } catch (IllegalArgumentException ex){
      MySeriesLogger.logger.log(Level.SEVERE, "Illegal argument exception.Possibly the update was cancelled", ex);
      return false;
    }
  }

  @Override
  protected void updateEpisodes(JTable episodesTable) throws SQLException {
    append("<span style='font-weight:bold;font-size:12px'>Importing data</span>");
    iu.progress_bar.setIndeterminate(false);
    iu.progress_bar.setString("0%");

    for (int i = 0; i < list.size(); i++) {
      int newEpisodes = 0;
      int updEpisodes = 0;
      boolean header = true;
      TrSeriesToUpdate curSeries = (TrSeriesToUpdate) list.get(i);
      if (curSeries.update) {
        series = curSeries.series;
        if (header) {
          iu.label_update_series.setText("Importing episodes of " + curSeries);
          append("<span><b>Importing episodes of " + curSeries + "</b></span>");
          MySeriesLogger.logger.log(Level.INFO, "Importing episodes of {0}", curSeries);
          header = false;
        }
        for (int e = 0; e < curSeries.episodes.size(); e++) {
          boolean save = false;
          TrEpisode episode = curSeries.episodes.get(e);
          int number = episode.number;
          String title = episode.title.trim();
          String airDate = episode.airDate;
          Vector<EpisodesRecord> episodes = DBHelper.getEpisodesBySql("SELECT * FROM episodes WHERE series_ID = " + series.getSeries_ID()
                  + " AND episode = " + number + " LIMIT 1");
          EpisodesRecord episodeRecord;
          if (episodes.isEmpty()) {
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
          if(save){
            episodeRecord.setSeries_ID(series.getSeries_ID());
            episodeRecord.setEpisode(number);
            episodeRecord.setTitle(title);
            if (!airDate.trim().equals("")) {
              episodeRecord.setAired(airDate);
            }
            episodeRecord.save(DBConnection.conn.createStatement());
          }
        }
         if(newEpisodes == 0 && updEpisodes ==0){
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

  @SuppressWarnings("unchecked")
  private void readXML(InputStream in) throws ParserConfigurationException, SAXException, IOException {
    MySeriesLogger.logger.log(Level.INFO, "Parsing XML");
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(in);
    doc.getDocumentElement().normalize();
    NodeList seasonNodeList = doc.getElementsByTagName("Season");
    if (seasonNodeList.getLength() == 0) {
      return;
    }
    TrSeriesToUpdate trs = (TrSeriesToUpdate) list.get(list.size() - 1);
    trs.update = true;
    for (int s = 0; s < seasonNodeList.getLength(); s++) {
      Node seasonNode = seasonNodeList.item(s);
      Element seasonElement = (Element) seasonNode;
      String val = seasonElement.getAttribute("no");
      try {
        if (Integer.parseInt(val) == series.getSeason()) {
          NodeList episodesNodeList = seasonElement.getElementsByTagName("episode");
          if (episodesNodeList.getLength() > 0) {
            for (int i = 0; i < episodesNodeList.getLength(); i++) {
              Node epNode = episodesNodeList.item(i);
              trs.episodes.add(new TrEpisode(epNode));
            }
          }
        }
      } catch (NumberFormatException ex) {
        return;
      }
    }

  }
}
