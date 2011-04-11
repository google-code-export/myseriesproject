/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.languages;

import java.util.logging.Level;
import javax.swing.ImageIcon;
import myComponents.myTableCellRenderers.MySubtitlesCellRenderer;
import tools.MySeriesLogger;

/**
 * Subtitle language object
 * @author ssoldatos
 */
public final class Language {

  /** The primary lang constant **/
  public static int PRIMARY = 0;
  /** The secondary lang constant **/
  public static int SECONDARY = 1;
  /** The no priority lang constant **/
  public static int NO_PRIORITY = 2;
  /** The multiple icon name **/
  private String MULTIPLE = "multiple";
  /** If the lang is the primary one **/
  private boolean isPrimary = false;
  /** If the lang is the secondary one **/
  private boolean isSecondary = false;
  /** The name of the lang **/
  private String name;
  /** The lang code in subtitle files **/
  private String code;
  /** The language's id in the database */
  private int id;
  /** The languages icon   */
  private ImageIcon icon;


  Language(String name, String code, int priority, int id) {
    this.name = name;
    this.code = code;
    this.id = id;
    String p = priority==PRIMARY ? "primary" : priority == SECONDARY ? "secondary" : "no priority";
    MySeriesLogger.logger.log(Level.INFO, "Creating new language [{0},{1},{2}] as {3}",
        new Object[] {name, code, id,p});
    if (priority == PRIMARY) {
      setIsPrimary(true);
      setIsSecondary(false);
    } else if (priority == SECONDARY) {
      setIsSecondary(true);
      setIsPrimary(false);
    } else if (priority == NO_PRIORITY) {
      setIsSecondary(false);
      setIsPrimary(false);
    }
  }

  /**
   * @return the isPrimary
   */
  public boolean isIsPrimary() {
    return isPrimary;
  }

  /**
   * @param isPrimary the isPrimary to set
   */
  public void setIsPrimary(boolean isPrimary) {
    this.isPrimary = isPrimary;
  }

  /**
   * @return the isSecondary
   */
  public boolean isIsSecondary() {
    return isSecondary;
  }

  /**
   * @param isSecondary the isSecondary to set
   */
  public void setIsSecondary(boolean isSecondary) {
    this.isSecondary = isSecondary;
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

  /**
   * @return the code
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code the code to set
   */
  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return name;
  }

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the icon
   */
  public ImageIcon getIcon() {
    try {
      MySeriesLogger.logger.log(Level.INFO, "Getting icon for language {0}",getName());
      if (this.equals(LangsList.NONE)) {
        MySeriesLogger.logger.log(Level.INFO, "Language is none , no icon");
        return null;
      } else if (this.equals(LangsList.MULTIPLE)) {
        MySeriesLogger.logger.log(Level.INFO, "Language is multiple, multiple icon");
        return new ImageIcon(getClass().getResource("/images/langs/" + MULTIPLE + ".png"));
      }
      ImageIcon im =  new ImageIcon(getClass().getResource("/images/langs/" + getCode() + ".png"));
      MySeriesLogger.logger.log(Level.FINE, "Image icon found");
      return im;
    } catch (Exception e) {
       MySeriesLogger.logger.log(Level.WARNING, "Image icon error");
    }
    return null;
  }
}
