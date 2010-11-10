/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.schedule;

import com.googlecode.scheduler.ScheduleDay;
import database.DBHelper;
import database.EpisodesRecord;
import database.SeriesRecord;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import myComponents.MyUsefulFunctions;
import tools.download.torrents.eztv.EzTvForm;

/**
 *
 * @author ssoldatos
 */
public class ScheduleMouseListener extends MouseAdapter {

  JPopupMenu pop = new JPopupMenu();

  @Override
  public void mouseClicked(MouseEvent e) {
    Vector<EpisodesRecord> episodes;
    if (e.getButton() == MouseEvent.BUTTON3) {
      JTable table = (JTable) e.getSource();
      Point p = e.getPoint();
      int row = table.rowAtPoint(p);
      int col = table.columnAtPoint(p);
      ScheduleDay val = (ScheduleDay) table.getModel().getValueAt(row, col);
      String date = val.getYear() + "-"
          + MyUsefulFunctions.padLeft(val.getMonth(), 2, "0") + "-"
          + MyUsefulFunctions.padLeft(val.getDay(), 2, "0");

      try {
        episodes = DBHelper.getEpisodesBySql("SELECT * FROM episodes WHERE aired ='" + date + "' AND downloaded = " + EpisodesRecord.NOT_DOWNLOADED);
        if (!episodes.isEmpty()) {
          for (Iterator<EpisodesRecord> it = episodes.iterator(); it.hasNext();) {
            EpisodesRecord ep = it.next();
            SeriesRecord ser = DBHelper.getSeriesByID(ep.getSeries_ID());
            pop.add(new ScheduleMenuItem(ser,ep));
           // new EzTvForm(ser, ep);
          }
          
          pop.show(table, p.x, p.y);
        }
      } catch (SQLException ex) {
        myseries.MySeries.logger.log(Level.SEVERE, null, ex);
      }
    }
  }
}
