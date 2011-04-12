/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools;

import java.awt.Desktop;
import java.util.logging.Level;
/**
 * Desktop support for the OS
 * @author lordovol
 */
public class DesktopSupport {
  private static Desktop desktop;
  private static boolean browseSupport;
  private static boolean desktopSupport;
  private static boolean mailSupport;

  /**
   * Creates the DesktopSupport object
   */
  public DesktopSupport(){
    try {
        MySeriesLogger.logger.log(Level.INFO, "Checking desktop support");
        desktop = Desktop.getDesktop();
        desktopSupport = Desktop.isDesktopSupported();
        browseSupport = desktop.isSupported(Desktop.Action.BROWSE);
        mailSupport = desktop.isSupported(Desktop.Action.MAIL);
      } catch (UnsupportedOperationException ex) {
        MySeriesLogger.logger.log(Level.WARNING, "Desktop is not supported in the current OS");
      }
  }

  /**
   * @return the desktop
   */
  public static Desktop getDesktop() {
    return desktop;
  }
  
  /**
   * @return the browseSupport
   */
  public static boolean isBrowseSupport() {
    return browseSupport;
  }

 
  /**
   * @return the desktopSupport
   */
  public static boolean isDesktopSupport() {
    return desktopSupport;
  }

 
  /**
   * @return the mailSupport
   */
  public static boolean isMailSupport() {
    return mailSupport;
  }

 
}
