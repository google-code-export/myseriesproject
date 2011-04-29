/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myGUI;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import javax.swing.JFrame;
import tools.MySeriesLogger;

/**
 *
 * @author ssoldatos
 */
public class MyTrayIcon {

  private TrayIcon trayIcon;
  private Image image;
  private String tooltip;
  private SystemTray tray;
  private JFrame frame;
  private TrayIconMouseListener mouseListener;
  private PopupMenu popup;
  private static final int RESTORE = 0;
  private static final int EXIT = 1;

  public MyTrayIcon(JFrame frame, Image im, String tooltip) {
    this.image = im;
    this.frame = frame;
    this.tooltip = tooltip;
    create();
  }

  private void create() {
    if (SystemTray.isSupported()) {

      tray = SystemTray.getSystemTray();
      popup = new PopupMenu();
      MenuItem restore = new MenuItem("Restore MySeries");
      MenuItem exit = new MenuItem("Exit MySeries");
      restore.addActionListener(new PopUpListener(RESTORE));
      exit.addActionListener(new PopUpListener(EXIT));
      popup.add(restore);
      popup.add(exit);


      trayIcon = new TrayIcon(image, tooltip, popup);

      mouseListener = new TrayIconMouseListener();
      trayIcon.addMouseListener(mouseListener);


      trayIcon.setImageAutoSize(true);

    } else {
    }
  }

  /**
   * @return the icon
   */
  public TrayIcon getTrayIcon() {
    return trayIcon;
  }

  /**
   * @param icon the icon to set
   */
  public void setTrayIcon(TrayIcon icon) {
    this.trayIcon = icon;
  }

  /**
   * @return the image
   */
  public Image getImage() {
    return image;
  }

  /**
   * @param image the image to set
   */
  public void setImage(Image image) {
    this.image = image;
  }

  /**
   * @return the tooltip
   */
  public String getTooltip() {
    return tooltip;
  }

  /**
   * @param tooltip the tooltip to set
   */
  public void setTooltip(String tooltip) {
    this.tooltip = tooltip;
  }

  public void removeIconFromTray() {
    tray.remove(trayIcon);
  }

  public void addIconToTray() {
    try {
      tray.add(trayIcon);
    } catch (AWTException e) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not add icon to tray");
    }
  }

  private class TrayIconMouseListener extends MouseAdapter {

    @Override
    public void mouseReleased(MouseEvent e) {
      if (e.getClickCount() > 1) {
        frame.setVisible(true);
        frame.setExtendedState(Frame.NORMAL);
        removeIconFromTray();
      }
    }
  }

  private class PopUpListener implements ActionListener {

    private final int action;

    public PopUpListener(int action) {
      this.action = action;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
      switch (action) {
        case RESTORE:
          frame.setVisible(true);
          frame.setExtendedState(Frame.NORMAL);
          removeIconFromTray();
          break;
        case EXIT:
          removeIconFromTray();
          frame.setExtendedState(Frame.ICONIFIED);
          frame.dispose();
          break;
      }
    }
  }
}
