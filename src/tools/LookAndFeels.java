/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import myseries.MySeries;
import myComponents.MyUsefulFunctions;
/**
 * Looks and Feels
 * @author lordovol
 */
public class LookAndFeels {

  public static Map<String, LookAndFeelInfo> lafMap;

  {
    getLookAndFeels();
  }

  /**
   * Sets the look and feel for the application form
   * @param m The application form
   * @param strLaf The lookAndFeel
   */
  public static void setLookAndFeel(MySeries m, String strLaf) {

    setLookAndFeel(m, lafMap.get(strLaf));
  }

  public LookAndFeels() {
  }

  /**
   * Gets the supported look and feels info
   * @return a n array of LookAndFeels info
   */
  public static LookAndFeelInfo[] getLookAndFeels() {
    UIManager.LookAndFeelInfo[] lookAndFeelInfos = UIManager.getInstalledLookAndFeels();
    LookAndFeelInfo laf[] = new LookAndFeelInfo[lookAndFeelInfos.length];
    lafMap = new HashMap<String, LookAndFeelInfo>();
    for (int i = 0; i < lookAndFeelInfos.length; i++) {
      UIManager.LookAndFeelInfo lookAndFeelInfo = lookAndFeelInfos[i];
      lafMap.put(lookAndFeelInfo.getName(), lookAndFeelInfo);
      laf[i] = lookAndFeelInfo;

    }
    return laf;
  }

  public static String getClassName(String laf) {
    UIManager.LookAndFeelInfo[] lookAndFeelInfos = UIManager.getInstalledLookAndFeels();
    lafMap = new HashMap<String, LookAndFeelInfo>();
    for (int i = 0; i < lookAndFeelInfos.length; i++) {
      UIManager.LookAndFeelInfo lookAndFeelInfo = lookAndFeelInfos[i];
      if (lookAndFeelInfo.getName().equals(laf)) {
        return lookAndFeelInfo.getClassName();
      }
    }
    return null;
  }

  /**
   * Sets the look and feel for the form
   * @param m The form
   * @param laf The lookandfeel info
   */
  public static void setLookAndFeel(MySeries m, LookAndFeelInfo laf) {
    try {
      UIManager.setLookAndFeel(laf.getClassName());
      SwingUtilities.updateComponentTreeUI(m);
      m.pack();
    } catch (ClassNotFoundException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    } catch (UnsupportedLookAndFeelException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
  }
}
