/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package help;

import java.util.HashMap;
import java.util.Map;

/**
 * A map of Help textt => Help links for opening the help html files by clicking on
 * the text
 * @author ssoldatos
 */
public class Links {
  static Map<String,String> links;

  static void createLinksMap() {
    links = new HashMap<String,String>();
    links.put("MySeries Help", "MySerieS Help");
    links.put("Application", "Start Application");
    links.put("Main Window","Main Window");
    links.put("The Series panel:","Series Panel");
    links.put("episodes panel","Episodes Panel");
    links.put("The Next Episodes panel:","Next Episodes Panel");
    links.put("The Episodes panel:","Episodes Panel");
    links.put("The Filters panel:","Filters Panel");
    links.put("The Schedule panel:","Schedule Panel");
    links.put("Create","Create database");
    links.put("Load","Load Database");
    links.put("Save","Save Database");
    links.put("Series Administration pop up menu","series_popup");
    links.put("Add/Edit Series Panel","series_administration");
    links.put("Add new Series Panel","series_administration");
    links.put("Add new episode Panel","episodes_administration");
    links.put("Renaming episodes Panel","episodes_renaming");
    links.put("Internet","internet_update");
    links.put("Episodes pop up menu","episodes_popup");
    links.put("The Feeds panel:","feeds_panel");
  }

  private Links() {
  }
}
