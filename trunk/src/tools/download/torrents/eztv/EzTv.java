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
import tools.download.torrents.TorrentConstants;
import tools.MySeriesLogger;

/**
 * Downloads a torrent from EzTv
 * @author lordovol
 */
public class EzTv extends AbstractTorrentDownload implements Runnable, TorrentConstants {

  /**
   * Seacrhes EZTv
   * @param uri
   * @param form
   */
  public EzTv(URI uri, EzTvForm form) {
      MySeriesLogger.logger.log(Level.INFO, "Downloading from eztv");
    this.uri = uri;
    this.form = form;
    this.progress = form.progress;
  }

  public void run() {
    if (MyUsefulFunctions.hasInternetConnection(EZTV_RSS)) {
      progress.setIndeterminate(true);
      progress.setString("Getting rss feeds from " + EZTV_NAME);
      getStream();
    } else {
      MyMessages.internetError(true);
    }
  }

  protected boolean isTorrent(AbstractTorrent torrent) throws MalformedURLException, IOException {
    URI u = torrent.getUri();
    if (uri != null) {
      BufferedReader in = new BufferedReader(new InputStreamReader(u.toURL().openStream()));
      String line;
      while ((line = in.readLine()) != null) {
        if (line.indexOf("Follow the Swarm") > -1) {
            MySeriesLogger.logger.log(Level.WARNING, "Torrent is not available any more");
          MyMessages.warning("No Torrents", "Torrent is not available anymore", true);
          return false;
        }
      }
        MySeriesLogger.logger.log(Level.FINE, "The url is a valid torrent");
      return true;
    }
    return false;
  }

  @Override
  protected ArrayList<AbstractTorrent> readStream(InputStream in) {
    Element val;
    NodeList t;
    String title = "";
    NodeList l;
    String link = "";
    ArrayList<AbstractTorrent> torrents = new ArrayList<AbstractTorrent>();
    try {
        MySeriesLogger.logger.log(Level.INFO, "Reading the rss data");
      progress.setString("Reading the rss data");
      MySeriesLogger.logger.log(Level.INFO, "Parsing XML");
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(in);
      doc.getDocumentElement().normalize();
      NodeList itemNodeList = doc.getElementsByTagName("item");
      if (itemNodeList.getLength() == 0) {
          MySeriesLogger.logger.log(Level.INFO, "No torrents found");
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
            MySeriesLogger.logger.log(Level.FINE, "Torrent found {0}",title);
        }
      }
    } catch (SAXException ex) {
      Logger.getLogger(EzTv.class.getName()).log(Level.SEVERE, "SAX exception occured", ex);
      return torrents;
    } catch (IOException ex) {
      Logger.getLogger(EzTv.class.getName()).log(Level.SEVERE, "I/O exception occured", ex);
      return torrents;
    } catch (ParserConfigurationException ex) {
      Logger.getLogger(EzTv.class.getName()).log(Level.SEVERE, "Parser error", ex);
      return torrents;
    }
    return torrents;
  }

  @Override
  protected AbstractTorrent getSelectedTorrent(ArrayList<AbstractTorrent> torrents) {
      MySeriesLogger.logger.log(Level.INFO, "{0} torrents found.Select the one to download",torrents.size());
      return (AbstractTorrent) MyMessages.ask( "Choose torrent", "Choose the torrent to download", null, torrents.toArray(),null, true);

    
  }
}
