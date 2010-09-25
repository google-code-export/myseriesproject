/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.languages;

import javax.swing.ImageIcon;
import myComponents.myTableCellRenderers.MySubtitlesCellRenderer;

/**
 * Subtitle language object
 * @author ssoldatos
 */
public class Language {

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
      if (this.equals(LangsList.NONE)) {
        return null;
      } else if (this.equals(LangsList.MULTIPLE)) {
        return new ImageIcon(getClass().getResource("/images/langs/" + MULTIPLE + ".png"));
      }
      return new ImageIcon(getClass().getResource("/images/langs/" + getCode() + ".png"));
    } catch (Exception e) {
     // System.out.println(this);
    }
    return null;
  }
}
