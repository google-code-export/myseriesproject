/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.download.torrents.isohunt;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
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
      progress.setString("Getting rss feeds from " + Torrent.ISOHUNT_NAME);
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
    ArrayList<Torrent> torrents = new ArrayList<Torrent>();

    return torrents;
  }

  

}
