/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myComponents.myGUI;

import java.awt.Image;

/**
 *
 * @author lordovol
 */
public class MyScaledImage {
  private  Image image;
  private  int width;
  private  int height;

  public MyScaledImage(Image image) {
    this.image = image;
  }

  public void fitImageIn(int fitWidth, int fitHeight) {
    int imWidth = image.getWidth(null);
    int imHeight = image.getHeight(null);
    if(imWidth > imHeight){
      this.setWidth(fitWidth);
      this.setHeight((this.getWidth() * imHeight) / imWidth);
    } else {
      this.setHeight(fitHeight);
      this.setWidth((this.getHeight() * imWidth) / imHeight);
    }
    this.setImage(getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));
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
   * @return the width
   */
  public int getWidth() {
    return width;
  }

  /**
   * @param width the width to set
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * @return the height
   */
  public int getHeight() {
    return height;
  }

  /**
   * @param height the height to set
   */
  public void setHeight(int height) {
    this.height = height;
  }


}
