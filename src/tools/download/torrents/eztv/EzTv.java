/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.torrents.eztv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import tools.download.torrents.AbstractTorrentDownload;
import tools.download.torrents.AbstractTorrent;

/**
 * Downloads a torrent from EzTv
 * @author lordovol
 */
public class EzTv extends AbstractTorrentDownload implements Runnable {

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
    if (MyUsefulFunctions.hasInternetConnection(AbstractTorrent.EZTV_RSS)) {
      progress.setIndeterminate(true);
      progress.setString("Getting rss feeds from "+AbstractTorrent.EZTV_NAME);
      getStream();
    } else {
      MyMessages.internetError();
    }
  }

  
  protected boolean isTorrent(AbstractTorrent torrent) throws MalformedURLException, IOException {
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

  protected ArrayList<AbstractTorrent> readStream(InputStream in) {
    Element val;
    NodeList t;
    String title = "";
    NodeList l;
    String link = "";
    ArrayList<AbstractTorrent> torrents = new ArrayList<AbstractTorrent>();
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
          EzTvTorrent torrent = new EzTvTorrent();
          torrent.setTitle(title);
          torrent.setLink(link);
          torrents.add(torrent);
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
