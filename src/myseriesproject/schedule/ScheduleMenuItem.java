/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseriesproject.schedule;

import database.EpisodesRecord;
import database.SeriesRecord;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import tools.download.torrents.eztv.EzTvForm;

/**
 *
 * @author ssoldatos
 */
class ScheduleMenuItem extends JMenuItem {

  private static final long serialVersionUID = 34647567567L;
  private SeriesRecord ser;
  private EpisodesRecord ep;

  public ScheduleMenuItem(final SeriesRecord ser, final EpisodesRecord ep) {
    setText("Download torrent for "+ep);
    setIcon(new ImageIcon(getClass().getResource("/images/eztv.png")));
    this.ser = ser;
    this.ep = ep;
    addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        new EzTvForm(ser, ep);
      }
    });
    
  }
}
