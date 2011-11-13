/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseriesproject.series;

import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author lordovol
 */
public class Quality {

  /** All video qualities  **/
  public static final int ALL_QUALITIES = 0;
  /** Low quality  **/
  public static final int LOW_QUALITY = 1;
  /** High quality  **/
  public static final int HIGH_QUALITY = 2;
  /** All video qualities  **/
  public static final String ALL_QUALITIES_NAME = "All Qualities";
  /** Low quality  **/
  public static final String LOW_QUALITY_NAME = "Low Quality";
  /** High quality  **/
  public static final String HIGH_QUALITY_NAME = "High Quality";
  /**
   * Qualities Values Array
   */
  public static final Integer[] QUALITIES = {ALL_QUALITIES, LOW_QUALITY, HIGH_QUALITY};
  /**
   * Qualities Names Array
   */
  public static final String[] QUALITIES_NAMES = {ALL_QUALITIES_NAME, LOW_QUALITY_NAME, HIGH_QUALITY_NAME};
  private int value;
  private String name;

  public Quality(int q) {
    if (q > QUALITIES.length) {
      q = 0;
    }
    value = q;
    name = QUALITIES_NAMES[q];
  }

  /**
   * @return the value
   */
  public int getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(int value) {
    this.value = value;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  public static ArrayList<Quality> getAllQualities() {
    ArrayList<Quality> qualities = new ArrayList<Quality>();
    for (int i = 0; i < QUALITIES.length; i++) {
      qualities.add(new Quality(QUALITIES[i]));

    }
    return qualities;
  }

  /**
   * Gets the quality icon
   * @return The quality icon
   */
  public Icon getIcon() {
    return new ImageIcon(getClass().getResource("/images/qualities/" + getValue() + ".png"));
  }
}
