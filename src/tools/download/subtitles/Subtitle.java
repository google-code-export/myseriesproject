/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.subtitles;

import database.SeriesRecord;
import java.awt.Color;
import java.net.URL;
import javax.swing.ImageIcon;
import myComponents.MyUsefulFunctions;
import myseriesproject.series.Quality;
import myseriesproject.series.Series;

/**
 *
 * @author lordovol
 */
public class Subtitle implements Comparable<Subtitle> {

  public String title;
  public URL url;
  public int love = 0;
  public int hate = 0;
  public int score = 0;
  public String language;
  private Color bgColor;
  private Quality quality;

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
    if (this.love + this.hate > 0) {
      score = this.love * 100 / (this.love + this.hate);
    }

  }

  public ImageIcon getIcon() {
    return new ImageIcon(getClass().getResource("/images/langs/" + language + ".png"));
  }

  @Override
  public String toString() {
    if (score == 0) {
      return title;
    }
    return score + "% " + title + "(" + love + "/" + hate + ")";
  }

  /**
   * @return the bgColor
   */
  public Color getBgColor() {
    if (score == 0) {
      return Color.WHITE;
    }
    if (score < 30) {
      return new Color(255, 64, 64);
    } else if (score < 60) {
      return new Color(255, 218, 185);
    } else if (score < 80) {
      return new Color(219, 219, 112);
    } else {
      return new Color(152, 251, 152);
    }
  }

  @Override
  public int compareTo(Subtitle o) {
    int q = (Series.getCurrentSerial()).getQuality();
    int subQ = getQuality().getValue();
    int oQ = o.getQuality().getValue();
    if (q == Quality.ALL_QUALITIES || subQ == oQ) {
      return o.score - score;
    } else {
      return subQ == q ? -1 : 1;
    }
    
  }

  /**
   * @return the quality
   */
  public Quality getQuality() {
    return title.indexOf("720") > -1 ? new Quality(Quality.HIGH_QUALITY) : new Quality(Quality.LOW_QUALITY);
  }
}
