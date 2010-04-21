/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.internetUpdate.tvrage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import myseries.MySeries;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tools.internetUpdate.InternetUpdate;



/**
 *
 * @author ssoldatos
 */
class SearchTvRage implements Runnable {

    private String title;
    Vector<TvRageSeries> results = new Vector<TvRageSeries>();
     private TrGetId tr;

    public SearchTvRage(String title, TrGetId tr) {
      MySeries.logger.log(Level.INFO, "Search");
      this.title = title;
      this.tr = tr;
      // run();
    }

    public void run() {
      try {
        tr.progress.setString("Searching for " + this.title);
        tr.progress.setIndeterminate(true);
        search();
        tr.progress.setIndeterminate(false);
        tr.progress.setString("Found  " + results.size() + " results");
      } catch (SAXException ex) {
        MySeries.logger.log(Level.WARNING, "Could not parse XML file", ex);
      } catch (ParserConfigurationException ex) {
        MySeries.logger.log(Level.WARNING, "Could not parse XML file", ex);
      } catch (IOException ex) {
        MySeries.logger.log(Level.WARNING, "Could not find the url for " + title, ex);
      }
    }

    private void search() throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
      InputStream in = null;
      MySeries.logger.log(Level.INFO, "Getting the url");
      String url = InternetUpdate.TV_RAGE_SEARCH_SHOW_URL;
      url = url + URLEncoder.encode(title, "UTF-8");
      MySeries.logger.log(Level.INFO, "Reading URL: " + url);
      URL tvRage = new URL(url);
      in = tvRage.openStream();
      readXML(in);

    }

    private void readXML(InputStream in) throws ParserConfigurationException, SAXException, IOException {

      MySeries.logger.log(Level.INFO, "Parsing XML");
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(in);
      doc.getDocumentElement().normalize();
      NodeList nodeLst = doc.getElementsByTagName("show");
      if (nodeLst.getLength() == 0) {
        return;
      }
      for (int s = 0; s < nodeLst.getLength(); s++) {
        Node node = nodeLst.item(s);
        Element el = (Element) node;
        String id = el.getElementsByTagName("showid").item(0).getFirstChild().getNodeValue();
        String name = el.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
        String year = el.getElementsByTagName("started").item(0).getFirstChild().getNodeValue();
        results.add(new TvRageSeries(id, name, year));
      }
      if (results.size() > 0) {
        tr.resultsModel = new DefaultComboBoxModel(results);
      }
      tr.combo_results.setModel(tr.resultsModel);
      tr.validate();
    }

    class TvRageSeries {

      final String id;
      final String name;
      final String year;

      private TvRageSeries(String id, String name, String year) {
        this.id = id;
        this.name = name;
        this.year = year;
      }

      @Override
      public String toString() {
        return name + " (" + year + ")";
      }
    }
  }
