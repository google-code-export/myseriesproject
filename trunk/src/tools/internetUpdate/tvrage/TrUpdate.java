/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.internetUpdate.tvrage;

import database.EpisodesRecord;
import database.SeriesRecord;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
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
  public TrUpdate(InternetUpdate iu) {
    this.iu = iu;
    this.list = new ArrayList<AbstractSeriesToUpdate>();
  }

  @Override
  @SuppressWarnings("unchecked")
  protected boolean read(SeriesRecord series) {
    try {
      MyUsefulFunctions.initInternetConnection();
      isConected = MyUsefulFunctions.hasInternetConnection();
      if(!isConected){
        MyMessages.error("No Internet Connection", "Could not connect to the Internet");
        return false;
      }
      this.series = series;
      list.add(new TrSeriesToUpdate(series));
      InputStream in = null;
      MySeries.logger.log(Level.INFO, "Getting the url");
      String url = "http://services.tvrage.com/feeds/episode_list.php?sid=" + series.getTvrage_ID();
      MySeries.logger.log(Level.INFO, "Reading URL: " + url);
      URL tvRage = new URL(url);
      in = tvRage.openStream();
      readXML(in);
      in.close();
      append("<span style='color:green'>(" + series.getFullTitle() + ") - OK</span>");
      return true;
    } catch (ParserConfigurationException ex) {
      MySeries.logger.log(Level.SEVERE, "Could not parse XML", ex);
      append("<span style='color:red'>(" + series.getFullTitle() + ") could not  get episodes </span>");
      return true;
    } catch (SAXException ex) {
      MySeries.logger.log(Level.SEVERE, "Could not parse XML", ex);
      append("<span style='color:red'>(" + series.getFullTitle() + ") could not  get episodes </span>");
      return true;
    } catch (IOException ex) {
      MySeries.logger.log(Level.SEVERE, "Could not read from tvrage", ex);
      append("<span style='color:red'>(" + series.getFullTitle() + ") could not  get episodes </span>");
      return true;
    } catch (IllegalArgumentException ex){
      MySeries.logger.log(Level.SEVERE, "Illegal argument exception.Possibly the update was cancelled", ex);
      return false;
    }
  }

  @Override
  protected void updateEpisodes() throws SQLException {
    append("<span style='font-weight:bold;font-size:12px'>Step 2 : Importing data</span>");
    iu.progress_bar.setIndeterminate(false);
    iu.progress_bar.setString("0%");

    for (int i = 0; i < list.size(); i++) {
      int newEpisodes = 0;
      int updEpisodes = 0;
      boolean header = true;
      TrSeriesToUpdate curSeries = (TrSeriesToUpdate) list.get(i);
      if (curSeries.update) {
        if (header) {
          iu.label_update_series.setText("Importing episodes of " + curSeries);
          append("<span><b>Importing episodes of " + curSeries + "</b></span>");
          MySeries.logger.log(Level.INFO, "Importing episodes of " + curSeries);
          header = false;
        }
        for (int e = 0; e < curSeries.episodes.size(); e++) {
          boolean save = false;
          TrEpisode episode = curSeries.episodes.get(e);
          int number = episode.number;
          String title = episode.title.trim();
          String airDate = episode.airDate;
          Vector<EpisodesRecord> episodes = EpisodesRecord.getEpisodesBySql("SELECT * FROM episodes WHERE series_ID = " + series.getSeries_ID()
                  + " AND episode = " + number + " LIMIT 1");
          EpisodesRecord episodeRecord;
          if (episodes.size() == 0) {
            newEpisodes++;
            save = true;
            episodeRecord = new EpisodesRecord();
            append("<b>&nbsp;&nbsp;&nbsp;&nbsp;New Episode: " + number + ". " + title + " (Inserted)</b>");
          } else {
            episodeRecord = episodes.get(0);
            if (!episodeRecord.getTitle().trim().equals(title) || (!episodeRecord.getAired().equals(airDate)
                    && !airDate.trim().equals(""))) {
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
            episodeRecord.save();
          }
        }
         if(newEpisodes == 0 && updEpisodes ==0){
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

  @SuppressWarnings("unchecked")
  private void readXML(InputStream in) throws ParserConfigurationException, SAXException, IOException {
    MySeries.logger.log(Level.INFO, "Parsing XML");
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
