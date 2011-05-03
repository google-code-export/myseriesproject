/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myGUI;

import database.SeriesRecord;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import myComponents.MyMessages;
import myseries.MySeries;
import myseries.actions.ApplicationActions;
import myComponents.MyUsefulFunctions;
import myComponents.myEvents.MyEvent;
import myComponents.myEvents.MyEventHandler;
import myseries.series.Series;
import tools.MySeriesLogger;
import tools.download.screenshot.DownloadScreenshot;
import tools.internetUpdate.tvrage.TrGetId;

/**
 * The screenshot panel
 * @author ssoldatos
 */
public class MyImagePanel extends JPanel {

  public static final String LOGO = "/images/logo.png";
  private static final long serialVersionUID = 356475743574387L;
  private Image image;
  DownloadScreenshotListener list;

  {
    setOpaque(false);
  }
  private boolean defaultImage;
  private final boolean addListener;

  /**
   * Creates the default screenshot of the application's logo
   */
  public MyImagePanel(boolean addListener) {
    MySeriesLogger.logger.log(Level.INFO, "Creating image panel");
    this.image = new ImageIcon(getClass().getResource(MyImagePanel.LOGO)).getImage();
    this.defaultImage = true;
    this.addListener = addListener;
    this.setBorder(BorderFactory.createLineBorder(Color.black));
    if (addListener) {
      MySeriesLogger.logger.log(Level.INFO, "Adding mouse listener for downloading screenshot");
      if (getMouseListeners().length == 0) {
        list = new DownloadScreenshotListener(this);
        addMouseListener(list);
      }
    }

  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(getImage(), 0, 0, getWidth(), getHeight(), this);
  }

  /**
   * @return the image
   */
  public Image getImage() {
    return image;
  }

  /**
   * Sets an image and it's size
   * @param image the image to set
   * @param width
   * @param height 
   */
  public void changeSize(int width, int height) {
    MySeriesLogger.logger.log(Level.INFO, "Resizing screenshot to {1} x {2}", new Object[]{width, height});
    this.defaultImage = false;
    this.setSize(width, height);
    revalidate();
    repaint();
  }

  public void relocate(MySeries m) {
    try {
      Thread.sleep(100);
      try {
        int maxWidth = m.splitPane_main.getDividerLocation() - 26;
        int width = image.getWidth(this);
        int maxHeight = m.splitPane_main.getHeight() - m.scrollPane_series.getHeight();
        int height = image.getHeight(this);
        int yPos = calcYpos(m);
        double ratio = (double) height / (double) width;
        while (height < maxHeight && width < maxWidth) {
          height = (int) ((double) height + (5 * ratio));
          width = width + 5;

        }
        while (height > maxHeight || width > maxWidth) {
          height = (int) ((double) height - (5 * ratio));
          width = width - 5;
        }
        int xPos = (maxWidth - width) / 2;
        setBounds(xPos, yPos, width, height);
        MySeriesLogger.logger.log(Level.INFO, "Relocating screenshot to {0},{1} with size {2}x{3}", 
            new Object[]{xPos, yPos,width,height});
        
        changeSize(width, height);
      } catch (NullPointerException ex) {
        MySeriesLogger.logger.log(Level.WARNING, "Null Screenshot");
      }
    } catch (InterruptedException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
  }

  public void setImage(Image image, boolean defaultImage, MySeries m) {
    MySeriesLogger.logger.log(Level.INFO, "Setting the screnshot to {0}", (defaultImage ? " the default image" : " series screenshot"));
    this.defaultImage = defaultImage;
    if (image == null) {
      this.image = new ImageIcon(getClass().getResource(LOGO)).getImage();
    } else {
      this.image = image;
    }
    if (defaultImage && addListener) {
      MySeriesLogger.logger.log(Level.INFO, "Adding mouse listener for downloading screenshot");
      if (getMouseListeners().length == 0) {
        list = new DownloadScreenshotListener(this);
        addMouseListener(list);
      }
    } else {
      MySeriesLogger.logger.log(Level.INFO, "Removing mouse listener for downloading screenshot");
      removeMouseListener(list);
    }
    int width = m.splitPane_main.getDividerLocation() - 26;
    int height = (int) (this.image.getHeight(this) * ((double) width / (double) this.image.getWidth(this)));
    int yPos = calcYpos(m);
    setBounds(0, yPos, width, height);
    changeSize(width, height);

  }

  // TODO fix position
  private int calcYpos(MySeries m) {
    int y = m.tableSeries.getHeight() > m.scrollPane_series.getHeight()
        ? m.scrollPane_series.getHeight() + 30 : m.tableSeries.getHeight() + 30;
    return y == 30 ? 50 : y;
  }

  private class DownloadScreenshotListener extends MouseAdapter {

    private final MyImagePanel imagePanel;
    private int tvRageId = 0;
    private String title = "";
    private SeriesRecord series = null;

    private DownloadScreenshotListener(MyImagePanel imagePanel) {
      this.imagePanel = imagePanel;
      series = Series.getCurrentSerial();
      tvRageId = series.getTvrage_ID();
      title = series.getFullTitle();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      if (tvRageId > 0) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setToolTipText("Click to download screenshot");
      }
    }

    @Override
    public void mouseExited(MouseEvent e) {
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      setToolTipText("");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      if (tvRageId > 0 && !series.isValidScreenshot()) {
        DownloadScreenshot scrDownload = new DownloadScreenshot(tvRageId, title);
        if (scrDownload.isSuccess()) {
          File sc = new File(scrDownload.getFilename());
          series.setScreenshot(sc.getName());
          try {
            series.save();
            MySeries m = (MySeries) imagePanel.getTopLevelAncestor();
            m.getEvClass().fireMyEvent(new MyEvent(m, MyEventHandler.SERIES_UPDATE));
            MyEvent evt = new MyEvent(m, MyEventHandler.SET_CURRENT_SERIES);
            evt.setSeries(series);
            m.getEvClass().fireMyEvent(evt);
            //Image im = new ImageIcon(sc.getAbsolutePath()).getImage();
            //imagePanel.setImage(im, false, m);
            MySeriesLogger.logger.log(Level.FINE, "Screenshot downloaded");
            MyMessages.message("Downloading screenshot", "The screenshot was saved in the images folder");
          } catch (SQLException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Could not save series record", ex);
            MyMessages.error("Downloading screenshot", "SQL error while saving series");
          }
        } else {
          MySeriesLogger.logger.log(Level.WARNING, "Screenshot not found");
          MyMessages.error("Downloading screenshot", "No screenshot was found");
        }
      }
    }
  }
}
