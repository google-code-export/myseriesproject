/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import com.googlecode.scheduler.Database;
import com.googlecode.scheduler.ScheduleDay;
import com.googlecode.scheduler.SchedulerCellRenderer;
import database.DBHelper;
import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import myseries.schedule.ScheduleEvent;
import tools.options.Options;

/**
 *
 * @author ssoldatos
 */
public class MyScheduleTableCellRenderer extends SchedulerCellRenderer {

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
  }

  @Override
  public String getEventsList(ScheduleDay sDay) {
    ArrayList<ScheduleEvent> events = DBHelper.getDayEvents(sDay);
    if (events.size() == 0) {
      return null;
    }
    Image orIm;
    if (events.size() == 1) {
      if (events.get(0).getImage().equals("")) {
        setImage(getDefaultImage());
      } else {
        File sc = new File("./images/" + events.get(0).getImage());
        orIm = new ImageIcon(sc.getAbsolutePath()).getImage();
        setImage(getScaledImageIcon(orIm));
      }
    } else {
      setImage(getDefaultImage());
    }
    return createTooltip(events);
  }

  private ImageIcon getScaledImageIcon(Image image) {
    int w = image.getWidth(this);
    int h = image.getHeight(this);
    int newW=0, newH=0;
    if(w>h){
      newW= 30;
      newH = (int) (30 * ((double) h / w));
    } else {
      newH =30;
      newW =(int) (30 * ((double) w / h));
    }
    ImageIcon im = new ImageIcon(image.getScaledInstance(newW, newH, Image.SCALE_SMOOTH));
    return im;
  }

  private String createTooltip(ArrayList<ScheduleEvent> events) {
    String tip = "<html><table>";
    for (Iterator<ScheduleEvent> it = events.iterator(); it.hasNext();) {
      ScheduleEvent event = it.next();
      //File f = new File("images/"+ event.getImage());
      File sc = new File("images/" + event.getImage());
      String source = sc.getAbsolutePath();

      tip += "<tr><td rowspan='2'><img src='" + source + "'></td><th>" + event.getSeries() + "</th></tr>"
          + "<tr><td>" + event.getEpisodeNumber() + "." + event.getEpisode() + "</td></tr>"
          + "<tr><td colspan='2'><hr></td></tr>";
    }
    tip += "</table></html>";
    return tip;
  }

  @Override
  public ImageIcon getDefaultImage() {
    Image im = new ImageIcon(getClass().getResource("/images/logo.png")).getImage();
    return getScaledImageIcon(im);
  }
}
