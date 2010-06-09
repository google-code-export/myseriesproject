/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import com.googlecode.scheduler.ScheduleDay;
import com.googlecode.scheduler.SchedulerCellRenderer;
import database.DBHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import myseries.schedule.ScheduleEvent;

/**
 *
 * @author ssoldatos
 */
public class MyScheduleTableCellRenderer extends SchedulerCellRenderer {

  public JPanel panel = new JPanel();
  private int cellHeight;
  JPanel panel_number;
  JPanel panel_icons;

  public MyScheduleTableCellRenderer() {
    
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    panel.removeAll();
    panel_number = new JPanel();
    panel_icons = new JPanel();
    JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    //panel_number.setBorder(BorderFactory.createLineBorder(Color.red));
    //panel_icons.setBorder(BorderFactory.createLineBorder(Color.blue));
    panel_icons.removeAll();
    panel_number.removeAll();
    this.cellHeight = table.getRowHeight();

    l.setHorizontalAlignment(SwingConstants.LEFT);
    l.setHorizontalTextPosition(SwingConstants.LEFT);
    panel.setBackground(l.getBackground());

    //If not a day;
    if (!(value instanceof ScheduleDay)) {
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      panel.add(l);
      panel.setToolTipText("");
      return panel;
    } else {
      ArrayList<ScheduleEvent> events = DBHelper.getDayEvents((ScheduleDay) value);
      //If no events
      if (events.size() == 0) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(l);
        panel.setToolTipText("");
        return panel;
      } else {
        int rows = events.size();
        panel.setLayout(new BorderLayout(0, 4));
        cellHeight = cellHeight / rows;
        //BoxLayout iconsLayout = new BoxLayout(panel_icons, BoxLayout.Y_AXIS);
        //panel_icons.setLayout(iconsLayout);
        panel_number.add(l);
        panel_number.setMaximumSize(new Dimension(20, 1000));
        panel.add(panel_number, BorderLayout.WEST);
        panel.add(panel_icons, BorderLayout.CENTER);
        ImageIcon orIm;
        String text = "<html>";
        for (Iterator<ScheduleEvent> it = events.iterator(); it.hasNext();) {
          ScheduleEvent ev = it.next();
          JPanel evPanel = new JPanel();
          text += "<b>" + ev.getSeries() + "</b>"  + "<br />" + ev.getEpisodeNumber() + "." + ev.getEpisode()+"<br />";;
          JLabel eventLabel = new JLabel();
          eventLabel.setToolTipText(text);
          if (ev.getImage().equals("")) {
            eventLabel.setIcon(getScaledImageIcon(getDefaultImage().getImage()));
          } else {
            File sc = new File("./images/" + ev.getImage());
            orIm = getScaledImageIcon(new ImageIcon(sc.getAbsolutePath()).getImage());
            eventLabel.setIcon(orIm);
          }
         
          evPanel.add(eventLabel);
          panel_icons.add(evPanel);
        }
         text+="</html>";
        panel.setToolTipText(text);
        return panel;
      }
    }
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
    //setImage(getDefaultImage());
    return createTooltip(events);
  }

  private ImageIcon getScaledImageIcon(Image image) {
    int w = image.getWidth(this);
    int h = image.getHeight(this);
    int newW = 0, newH = 0;
    if (w > h) {
      newW = cellHeight;
      newH = (int) (cellHeight * ((double) h / w));
    } else {
      newH = cellHeight;
      newW = (int) (cellHeight * ((double) w / h));
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
    return null;
  }

  @Override
  public ImageIcon getDefaultImage() {
    ImageIcon im = new ImageIcon(getClass().getResource("/images/logo.png"));
    return im;
  }
}
