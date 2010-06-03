/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.download.torrents;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import myComponents.MyMessages;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public abstract class AbstractTorrentDownload {

  protected AbstractTorrentForm form;
  protected URI uri;
  protected JProgressBar progress;

  protected void getTorrent(AbstractTorrent torrent) throws IOException {
    if (true) {
      downloadTorrent(torrent);
      return;
    }
    URI u = torrent.getUri();
    if (u != null) {
      if (isTorrent(torrent)) {
        Desktop.getDesktop().browse(uri);
        form.dispose();
        myseries.MySeries.glassPane.deactivate();
      }
    }
  }

  protected void getStream() {
     InputStream in = null;
    try {
      URL rss = uri.toURL();
      in = rss.openStream();
      ArrayList<AbstractTorrent> torrents = readStream(in);
      progress.setString(torrents.size() + " torrents found");
      progress.setIndeterminate(false);
      if (torrents.size() == 0) {
        MyMessages.message("No Torrents", "No torrent was found");
      } else if (torrents.size() == 1) {
        getTorrent(torrents.get(0));
      } else {
        AbstractTorrent tor = (AbstractTorrent) JOptionPane.showInputDialog(null, "Choose the torrent to download", "Choose torrent", JOptionPane.QUESTION_MESSAGE, null, torrents.toArray(), 0);
        if (tor != null) {
          getTorrent(tor);
        }
      }
    } catch (IOException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    } finally {
      //myseries.MySeries.glassPane.deactivate();
    }
  }

  protected abstract ArrayList<AbstractTorrent> readStream(InputStream in);

   private void downloadTorrent(AbstractTorrent torrent) {
    try {
      if (!isTorrent(torrent)) {
        return;
      }
      InputStream is = null;
      BufferedOutputStream outStream = null;
      byte[] buf;
      URLConnection uCon = torrent.getUri().toURL().openConnection();
      is = uCon.getInputStream();
      buf = new byte[1024];
      int ByteRead;
      int ByteWritten = 0;
      String torrentName;
      String[] t = torrent.getLink().split("/", -1);
      torrentName = t[t.length - 1];
      String filename = AbstractTorrent.TORRENTS_PATH + torrentName;
      outStream = new BufferedOutputStream(new FileOutputStream(Options._USER_DIR_ + filename));
      while ((ByteRead = is.read(buf)) != -1) {
        outStream.write(buf, 0, ByteRead);
        ByteWritten += ByteRead;
      }
      is.close();
      outStream.close();
      Desktop.getDesktop().open(new File(filename));
      form.dispose();
    } catch (IOException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    } finally {
      myseries.MySeries.glassPane.deactivate();
    }
  }

   protected abstract boolean isTorrent(AbstractTorrent torrent) throws MalformedURLException, IOException;
}
