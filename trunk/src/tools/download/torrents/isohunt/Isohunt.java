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
      while(keys.hasNext()){
       String key =(String) keys.next();
       if(key.equals("items")){
         items = json.getJSONObject(key);
       }
      }
      if(items.has("list")){
       list = items.getJSONArray("list");
      }
      for (int i = 0; i < list.length(); i++) {
        JSONObject tor = list.getJSONObject(i);
        Torrent t = new Torrent((String)tor.get("title"),(String) tor.get("enclosure_url"));
        torrents.add(t);
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
