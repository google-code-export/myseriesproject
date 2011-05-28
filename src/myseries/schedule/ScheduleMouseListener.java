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
import tools.MySeriesLogger;
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
  Vector<EpisodesRecord> episodes = new Vector<EpisodesRecord>();

  @Override
  public void mouseClicked(MouseEvent e) {

    if (e.getButton() == MouseEvent.BUTTON3) {
      JTable table = (JTable) e.getSource();
      Point p = e.getPoint();
      int row = table.rowAtPoint(p);
      int col = table.columnAtPoint(p);
      ScheduleDay val = (ScheduleDay) table.getModel().getValueAt(row, col);
      String date = val.getYear() + "-"
              + MyUsefulFunctions.padLeft(val.getMonth(), 2, "0") + "-"
              + MyUsefulFunctions.padLeft(val.getDay(), 2, "0");
      if (MyUsefulFunctions.hasBeenAired(date, true)) {
        try {
          episodes.clear();
          pop.removeAll();
          episodes = DBHelper.getEpisodesBySql("SELECT e.* FROM episodes e JOIN series s ON e.series_ID = s.series_ID "
                  + "WHERE s.deleted = "+SeriesRecord.NOT_DELETED+" AND aired ='" + date + "' AND downloaded = " + EpisodesRecord.NOT_DOWNLOADED);
          if (!episodes.isEmpty()) {
            for (Iterator<EpisodesRecord> it = episodes.iterator(); it.hasNext();) {
              EpisodesRecord ep = it.next();
              SeriesRecord ser = DBHelper.getSeriesByID(ep.getSeries_ID());
              pop.add(new ScheduleMenuItem(ser, ep));
              // new EzTvForm(ser, ep);
            }
            MySeriesLogger.logger.log(Level.INFO, "Showing pop up for downloading torrent");
            pop.show(table, p.x, p.y);
          }
        } catch (SQLException ex) {
          MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
        }
      }
    }
  }
}
