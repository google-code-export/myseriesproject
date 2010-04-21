/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.torrents;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tools.options.Options;

/**
 * Downloads a torrent from EzTv
 * @author lordovol
 */
public class EzTv implements Runnable {

  private final URI uri;
  private JProgressBar progress;
  private EzTvForm form;

  /**
   * Seacrhes EZTv
   * @param uri
   * @param form
   */
  public EzTv(URI uri, EzTvForm form) {
    this.uri = uri;
    this.form = form;
    this.progress = form.progress;
  }

  public void run() {
    if (MyUsefulFunctions.hasInternetConnection()) {
      progress.setIndeterminate(true);
      progress.setString("Getting rss feeds from eztv");
      getRss();
    } else {
      MyMessages.internetError();
    }
  }

  private void getRss() {
    InputStream in = null;
    try {
      URL rss = uri.toURL();
      in = rss.openStream();
      ArrayList<Torrent> torrents = readXML(in);
      progress.setString(torrents.size() + " torrents found");
      progress.setIndeterminate(false);
      if (torrents.size() == 0) {
        MyMessages.message("No Torrents", "No torrent was found");
      } else if (torrents.size() == 1) {
        getTorrent(torrents.get(0));
      } else {
        Torrent tor = (Torrent) JOptionPane.showInputDialog(null, "Choose the torrent to download", "Choose torrent", JOptionPane.QUESTION_MESSAGE, null, torrents.toArray(), 0);
        if (tor != null) {
          getTorrent(tor);
        }
      }
    } catch (IOException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    } finally {
      myseries.MySeries.glassPane.deactivate();
    }
  }

  private boolean isTorrent(Torrent torrent) throws MalformedURLException, IOException {
    URI u = torrent.getUri();
    if (uri != null) {
      BufferedReader in = new BufferedReader(new InputStreamReader(u.toURL().openStream()));
      String line;
      while ((line = in.readLine()) != null) {
        if (line.indexOf("Follow the Swarm") > -1) {
          MyMessages.error("No Torrents", "Torrent is not available anymore");
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private void downloadTorrent(Torrent torrent) {
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
      String[] t = torrent.link.split("/", -1);
      torrentName = t[t.length - 1];
      String filename = Options._USER_DIR_ + "/" + Torrent.TORRENTS_PATH + torrentName;
      outStream = new BufferedOutputStream(new FileOutputStream(filename));
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

  private void getTorrent(Torrent torrent) throws IOException {
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

  private ArrayList<Torrent> readXML(InputStream in) {
    Element val;
    NodeList t;
    String title = "";
    NodeList l;
    String link = "";
    ArrayList<Torrent> torrents = new ArrayList<Torrent>();
    try {
      progress.setString("Reading the rss data");
      myseries.MySeries.logger.log(Level.INFO, "Parsing XML");
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(in);
      doc.getDocumentElement().normalize();
      NodeList itemNodeList = doc.getElementsByTagName("item");
      if (itemNodeList.getLength() == 0) {
        return torrents;
      }
      for (int s = 0; s < itemNodeList.getLength(); s++) {
        Node itemNode = itemNodeList.item(s);
        val = (Element) itemNode;
        t = val.getElementsByTagName("title");
        title = t.item(0).getFirstChild().getNodeValue();
        l = val.getElementsByTagName("link");
        link = l.item(0).getFirstChild().getNodeValue();
        if (!link.equals("") && !title.equals("")) {
          torrents.add(new Torrent(title, link));
        }
      }
    } catch (SAXException ex) {
      Logger.getLogger(EzTv.class.getName()).log(Level.SEVERE, null, ex);
      return torrents;
    } catch (IOException ex) {
      Logger.getLogger(EzTv.class.getName()).log(Level.SEVERE, null, ex);
      return torrents;
    } catch (ParserConfigurationException ex) {
      Logger.getLogger(EzTv.class.getName()).log(Level.SEVERE, null, ex);
      return torrents;
    }
    return torrents;
  }
}
