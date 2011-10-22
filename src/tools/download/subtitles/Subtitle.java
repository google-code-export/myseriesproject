/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.subtitles;

import java.awt.Color;
import java.net.URL;
import javax.swing.ImageIcon;
import myComponents.MyUsefulFunctions;

/**
 *
 * @author lordovol
 */
public class Subtitle  {

  public String title;
  public URL url;
  public int love = 0;
  public int hate = 0;
  public int percent = 0;
  public String language;
  private Color bgColor;

  /**
   *
   * @param title
   * @param url
   */
  public Subtitle(String title, URL url, int love, int hate, String language) {
    this.title = MyUsefulFunctions.deleteDoubleSpaces(title);
    this.url = url;
    this.love = love;
    this.hate = hate;
    this.language = language;
    if(this.love + this.hate > 0){
      percent = this.love * 100 / (this.love + this.hate);
    }
    
  }

  public ImageIcon getIcon(){
    return new ImageIcon(getClass().getResource("/images/langs/" + language + ".png"));
  }

  @Override
  public String toString() {
    if (percent == 0) {
      return title;
    }
    return percent + "% "  + title + "(" + love + "/" + hate + ")";
  }

  /**
   * @return the bgColor
   */
  public Color getBgColor() {
    if (percent == 0){
      return Color.WHITE;
    }
    if(percent < 30){
      return new Color(255,64,64);
    } else if (percent < 60){
      return new Color(255,218,185);
    } else if (percent < 80){
      return new Color(219,219,112);
    }else {
      return new Color(152,251,152);
    }
  }
}
