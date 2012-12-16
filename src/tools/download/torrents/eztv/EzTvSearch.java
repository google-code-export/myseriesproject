/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.torrents.eztv;

import database.EpisodesRecord;
import database.SeriesRecord;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import tools.MySeriesLogger;
import tools.download.torrents.AbstractTorrent;
import tools.download.torrents.TorrentConstants;

/**
 *
 * @author lordovol
 */
public class EzTvSearch extends EzTv implements Runnable, TorrentConstants {

  private final String title;
  private final String quality;
  private final String season;
  private final String episodeNo;

  public EzTvSearch(URI uri, EzTvForm form) {
    super(uri, form);
    title = String.valueOf(form.combo_series.getSelectedItem());
    quality = String.valueOf(form.combo_quality.getSelectedItem());
    season = String.valueOf(form.spinner_season.getValue());
    episodeNo = String.valueOf(form.spinner_episode.getValue());
  }

  public void run() {
    if (MyUsefulFunctions.hasInternetConnection(EZTV_SEARCH)) {
      progress.setIndeterminate(true);
      progress.setString("Getting rss feeds from " + EZTV_NAME);
      getStream();
    } else {
      MyMessages.internetError();
    }
  }

  @Override
  protected ArrayList<AbstractTorrent> readStream(InputStream inStream) {
    ArrayList<AbstractTorrent> torrents = new ArrayList<AbstractTorrent>();
    String title = "";
    String link = "";
    try {
      String inputLine;
      BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
      while ((inputLine = in.readLine()) != null) {
        String el = "<div id=\"placeholder\">";
        String[] a = inputLine.split(el);
        if (a.length > 1) {
          String regex = "<div id=\"showname\">|<div id=\"mirror\">";
          String[] b = inputLine.split(regex);
          if (b.length > 1) {
            title = MyUsefulFunctions.stripHTML(b[1]);
            String[] c = b[2].split("\" target=\"");
            link = c[0].replaceAll("<a href=\"", "");
            if (isValidTorrent(title)) {
              System.out.println(title);
              EzTvTorrent tor = new EzTvTorrent();
              tor.setTitle(title);
              tor.setLink(link);
              torrents.add(tor);
            }
          }
        }

      }
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
    return torrents;
  }

  private boolean isValidTorrent(String title) {
    String epPart = "";
    String sPart = "";
    if(!this.episodeNo.equals("0")){
      epPart = this.episodeNo;
    } 
    if(!this.season.equals("0")){
      sPart = this.season;
    }
    String epRegex = "(" + sPart + "x(0)*" + epPart + ")|(" + sPart + "E(0)*" + epPart + ")";
    System.out.println(epRegex);
    Pattern pattern = Pattern.compile(epRegex);
    Matcher matcher = pattern.matcher(title);
    if (!matcher.find()) {
      return false;
    }
    if (quality != null) {
      String qRegex = quality;
      pattern = Pattern.compile(qRegex);
      matcher = pattern.matcher(title);
      if (!matcher.find()) {
        return false;
      }
    }
    return true;
  }
}
