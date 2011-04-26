/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myGUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import myseries.MySeries;
import myseries.actions.ApplicationActions;
import myComponents.MyUsefulFunctions;
import tools.MySeriesLogger;

/**
 * The screenshot panel
 * @author ssoldatos
 */
public class MyImagePanel extends JPanel {

  public static final String LOGO = "/images/logo.png";
  private static final long serialVersionUID = 356475743574387L;
  private Image image;

  {
    setOpaque(false);
  }
  private boolean defaultImage;

  /**
   * Creates the default screenshot of the application's logo
   */
  public MyImagePanel() {
    this.image = new ImageIcon(getClass().getResource(MyImagePanel.LOGO)).getImage();
    this.defaultImage = true;
    this.setBorder(BorderFactory.createLineBorder(Color.black));
  }

  /**
   * Sets a screenshot
   * @param image The screenshot image
   */
  public MyImagePanel(Image image) {
    this.image = image;
    this.defaultImage = false;
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
  public void changeSize(Image image, int width, int height) {
      MySeriesLogger.logger.log(Level.INFO, "Resizing screenshot to {1} x {2}",new Object[]{width, height});
    this.defaultImage = false;
    if (image == null) {
      this.image = new ImageIcon(getClass().getResource(MyImagePanel.LOGO)).getImage();
    } else {
      this.image = image;
    }
    this.setSize(width, height);
    repaint();
  }

  public void relocate(MySeries m) {
    try {
      Thread.sleep(100);
      try {

        int width = m.splitPane_main.getDividerLocation() - 26;
        int height = (int) (image.getHeight(this) * ((double) width / (double) image.getWidth(this)));

        int yPos = calcYpos(m);
        if (yPos == 30) {
          yPos = 50;
        }
        //imageLayerPanel.setBounds(0, yPos, width, height);
        MySeriesLogger.logger.log(Level.INFO, "Relocating screenshot to {0},{1}", new Object[] {0,yPos});
        setBounds(0, yPos, width, height);
        changeSize(image, width, height);
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
    int width = m.splitPane_main.getDividerLocation() - 26;
    int height = (int) (this.image.getHeight(this) * ((double) width / (double) this.image.getWidth(this)));
    int yPos = calcYpos(m);
    setBounds(0, yPos,
            width, height);
    changeSize(this.image, width, height);

  }

  // TODO fix position
  private int calcYpos(MySeries m) {
    int y = m.tableSeries.getHeight() > m.scrollPane_series.getHeight() ?
          m.scrollPane_series.getHeight() + 30 : m.tableSeries.getHeight() + 30;
   return y==30 ? 50 : y;
  }
}
