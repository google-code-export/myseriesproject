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
import java.util.logging.Level;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import tools.MySeriesLogger;
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
    ArrayList<String> v = new ArrayList<String>();
    try {
      ArrayList<SeriesRecord> s = Series.getSeries(false);
      for (Iterator<SeriesRecord> it = s.iterator(); it.hasNext();) {
        SeriesRecord seriesRecord = it.next();
        v.add(seriesRecord.getTitle());
      }
      seriesModel = new DefaultComboBoxModel(v.toArray());
      qualityModel = new DefaultComboBoxModel(QUALITIES);
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception", ex);
    }
  }

  protected abstract URI createUri();
}
