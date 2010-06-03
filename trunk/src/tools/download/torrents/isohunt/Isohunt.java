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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tools.download.torrents.AbstractTorrentDownload;
import tools.download.torrents.Torrent;

/**
 *
 * @author lordovol
 */
public class Isohunt extends AbstractTorrentDownload implements Runnable {

  Isohunt(URI uri, IsohuntForm form) {
    this.uri = uri;
    this.form = form;
    this.progress = form.progress;
  }

  public void run() {
    if (MyUsefulFunctions.hasInternetConnection(Torrent.ISOHUNT_JSON)) {
      progress.setIndeterminate(true);
      progress.setString("Getting json data from " + Torrent.ISOHUNT_NAME);
      getStream();
    } else {
      MyMessages.internetError();
    }
  }

  @Override
  protected boolean isTorrent(Torrent torrent) throws MalformedURLException, IOException {
    return true;
  }

  @Override
  protected ArrayList<Torrent> readStream(InputStream in) {
    BufferedReader reader = null;
    ArrayList<Torrent> torrents = new ArrayList<Torrent>();
    JSONObject items = null;
    JSONArray list = null;


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
        //Print all fields
//         Iterator it = tor.keys();
//        while(it.hasNext()){
//          String key =(String) it.next();
//          String value = (String)tor.get(key);
//          System.out.println(key + ": :" + value);
//        }

        IsohuntTorrent torrent = new IsohuntTorrent();
        torrent.setTitle(tor.has("title")? tor.getString("title"): "");
        torrent.setLink(tor.has("enclosure_url")? tor.getString("enclosure_url"): "");
        torrent.setComments(tor.has("comments")? tor.getInt("comments"): 0);
        torrent.setDownloads(tor.has("downloads")? tor.getInt("downloads"): 0);
        torrent.setFiles(tor.has("files")? tor.getInt("files"): 0);
        torrent.setHash(tor.has("hash")? tor.getString("hash"): "");
        torrent.setLeechers(tor.has("leechers")? tor.getInt("leechers"): 0);
        torrent.setLength(tor.has("length")? tor.getLong("length"): 0);
        torrent.setOriginalLink(tor.has("original_link")? tor.getString("original_link"): "");
        torrent.setPublishedDate(tor.has("pubDate")? tor.getString("pubDate"): "");
        torrent.setSeeds(tor.has("Seeds")? tor.getInt("Seeds"): 0);
        torrent.setSize(tor.has("size")? tor.getString("size"): "");
        torrent.setSummaryLink(tor.has("link")? tor.getString("link"): "");
        torrent.setTracker(tor.has("tracker")? tor.getString("tracker"): "");
        torrent.setVotes(tor.has("votes")? tor.getInt("votes"): 0);
        torrents.add(torrent);

      }
      return torrents;
    } catch (JSONException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
      return torrents;
    } catch (IOException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
      return torrents;
    }

  }
}
