/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.torrents;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import myComponents.MyUsefulFunctions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author lordovol
 */
public class EzTv implements Runnable {

  private final URI uri;
  private JProgressBar progress;

  EzTv(URI uri, JProgressBar progress) {
    this.uri = uri;
    this.progress = progress;
  }

  public void run() {
    if (MyUsefulFunctions.hasInternetConnection()) {
      progress.setIndeterminate(true);
      progress.setString("Getting rss feeds from eztv");
      getRss();
    }
  }

  private void getRss() {
    InputStream in = null;
    try {
      URL rss = uri.toURL();
      in = rss.openStream();
      ArrayList<Torrent> torrents = readXML(in);
      progress.setString(torrents.size()+ " torrents found");
      progress.setIndeterminate(false);
      if (torrents.size() == 0) {
        MyUsefulFunctions.message("No Torrents", "No torrent was found");
      } else if (torrents.size() == 1) {
        URI uri = torrents.get(0).getUri();
        if (uri != null) {
          Desktop.getDesktop().browse(uri);
        }
      } else {
        Torrent tor = (Torrent) JOptionPane.showInputDialog(null, "Choose the torrent to download", "Choose torrent", JOptionPane.QUESTION_MESSAGE, null, torrents.toArray(), 0);
        if (tor != null) {
          URI uri = tor.getUri();
          if (uri != null) {
            Desktop.getDesktop().browse(uri);
          }
        }
      }
    } catch (IOException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
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
