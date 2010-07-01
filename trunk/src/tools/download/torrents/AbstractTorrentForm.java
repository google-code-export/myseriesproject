/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.torrents;

import database.EpisodesRecord;
import database.SeriesRecord;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import myComponents.myGUI.MyDraggable;
import myseries.series.Series;

/**
 *
 * @author lordovol
 */
public abstract class AbstractTorrentForm extends MyDraggable implements TorrentConstants {

  protected SeriesRecord series = null;
  protected EpisodesRecord episode = null;
  protected ComboBoxModel seriesModel = new DefaultComboBoxModel();
  protected ComboBoxModel qualityModel = new DefaultComboBoxModel();

  protected void createModels() {
    Vector<String> v = new Vector<String>();
    try {
      ArrayList<SeriesRecord> s = Series.getSeries(false);
      for (Iterator<SeriesRecord> it = s.iterator(); it.hasNext();) {
        SeriesRecord seriesRecord = it.next();
        v.add(seriesRecord.getTitle());
      }
      seriesModel = new DefaultComboBoxModel(v);
      qualityModel = new DefaultComboBoxModel(QUALITIES);
    } catch (SQLException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }

  protected abstract URI createUri();
}
