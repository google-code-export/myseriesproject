/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.torrents.isohunt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import tools.MySeriesLogger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tools.download.torrents.AbstractTorrentDownload;
import tools.download.torrents.AbstractTorrent;
import tools.download.torrents.TorrentConstants;

/**
 *
 * @author lordovol
 */
public class Isohunt extends AbstractTorrentDownload implements Runnable, TorrentConstants {

  Isohunt(URI uri, IsohuntForm form) {
      MySeriesLogger.logger.log(Level.INFO, "Downloading torrent from isohunt");
    this.uri = uri;
    this.form = form;
    this.progress = form.progress;
  }

  @Override
  public void run() {
    if (MyUsefulFunctions.hasInternetConnection(ISOHUNT_JSON)) {
        MySeriesLogger.logger.log(Level.INFO, "Getting json data from isohunt");
      progress.setIndeterminate(true);
      progress.setString("Getting json data from " + ISOHUNT_NAME);
      getStream();
    } else {
      MyMessages.internetError();
    }
  }

  @Override
  protected boolean isTorrent(AbstractTorrent torrent) throws MalformedURLException, IOException {
    return true;
  }

  @Override
  protected ArrayList<AbstractTorrent> readStream(InputStream in) {
    BufferedReader reader = null;
    ArrayList<AbstractTorrent> torrents = new ArrayList<AbstractTorrent>();
    JSONObject items = null;
    JSONArray list = null;
      MySeriesLogger.logger.log(Level.INFO, "Reading stream from isohunt");

    try {
      String line = "";
      String jsonStr = "";
      reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
      while ((line = reader.readLine()) != null) {
        jsonStr += line;
      }
      JSONObject json = new JSONObject(jsonStr);
      Iterator keys = json.keys();
      while (keys.hasNext()) {
        String key = (String) keys.next();
        if (key.equals("items")) {
          items = json.getJSONObject(key);
        }
      }
      if (items.has("list")) {
        list = items.getJSONArray("list");
      }
      for (int i = 0; i < list.length(); i++) {
        JSONObject tor = list.getJSONObject(i);
        IsohuntTorrent torrent = new IsohuntTorrent();
        torrent.setTitle(tor.has("title") ? MyUsefulFunctions.stripHTML(tor.getString("title")) : "");
        torrent.setLink(tor.has("enclosure_url") ? tor.getString("enclosure_url") : "");
        torrent.setComments(tor.has("comments") ? tor.getInt("comments") : 0);
        torrent.setDownloads(tor.has("downloads") ? tor.getInt("downloads") : 0);
        torrent.setFiles(tor.has("files") ? tor.getInt("files") : 0);
        torrent.setHash(tor.has("hash") ? tor.getString("hash") : "");
        torrent.setLeechers(tor.has("leechers") ? tor.getInt("leechers") : 0);
        torrent.setLength(tor.has("length") ? tor.getLong("length") : 0);
        torrent.setOriginalLink(tor.has("original_link") ? tor.getString("original_link") : "");
        torrent.setPublishedDate(tor.has("pubDate") ? tor.getString("pubDate") : "");
        torrent.setSeeds(tor.has("Seeds") ? tor.getInt("Seeds") : 0);
        torrent.setSize(tor.has("size") ? tor.getString("size") : "");
        torrent.setSummaryLink(tor.has("link") ? tor.getString("link") : "");
        torrent.setTracker(tor.has("tracker") ? tor.getString("tracker") : "");
        torrent.setVotes(tor.has("votes") ? tor.getInt("votes") : 0);
        if(!torrent.getTitle().equals("")){
            MySeriesLogger.logger.log(Level.FINE, "Found torrent {0}",torrent.getTitle());
          torrents.add(torrent);
        }
      }
      return torrents;
    } catch (JSONException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "JSON exception", ex);
      return torrents;
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "I/O error", ex);
      return torrents;
    }

  }

  @Override
  protected AbstractTorrent getSelectedTorrent(ArrayList<AbstractTorrent> torrents) {
    IsohuntResults isoRes = new IsohuntResults(torrents);
      MySeriesLogger.logger.log(Level.INFO, "{0} torrents found.Select the one to download",torrents.size());
    return isoRes.getSelectedTorrent();
  }
}
