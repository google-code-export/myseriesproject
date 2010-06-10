/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myGUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import myseries.MySeries;

/**
 * The screenshot panel
 * @author ssoldatos
 */
public class MyImagePanel extends JPanel {

   /**
   * The screenshots path : "images/"
   */
  public static final String SCREENSHOTS_PATH ="images/";

  private static final long serialVersionUID = 356475743574387L;

  private Image image;

  {
  setOpaque(false);
  }

  /**
   * Creates the default screenshot of the application's logo
   */
  public MyImagePanel() {
    this.image = new ImageIcon(getClass().getResource("/images/logo.png")).getImage();
    this.setBorder(BorderFactory.createLineBorder(Color.black));
    
  }

  /**
   * Sets a screenshot
   * @param image The screenshot image
   */
  public MyImagePanel(Image image) {
    this.image = image;
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
  public void setImage(Image image, int width, int height) {
    this.image = image;
    this.setSize(width, height);
    repaint();
  }

   public void relocate(MySeries m) {
     try {
      Thread.sleep(100);
      try {
        int width = MySeries.splitPane_main.getDividerLocation() - 26;
        int height = (int) (image.getHeight(this) * ((double) width / (double) image.getWidth(this)));

        int yPos = (int) MySeries.tableSeries.getPreferredSize().getHeight() + 20;
        if(yPos ==20) {
          yPos = 40;
        }
        //imageLayerPanel.setBounds(0, yPos, width, height);
        m.imagePanel.setBounds(0, yPos, width, height);
        m.imagePanel.setImage(image, width, height);
      } catch (NullPointerException ex) {
      }
    } catch (InterruptedException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }

    public void setImage(Image image) {
    this.image = image;
    int width = MySeries.splitPane_main.getDividerLocation() - 26;
    int height = (int) (image.getHeight(this) * ((double) width / (double) image.getWidth(this)));
    setBounds(0, (int) MySeries.tableSeries.getPreferredSize().getHeight() + 20,
            width, height);
    setImage(image, width, height);
  }
}
