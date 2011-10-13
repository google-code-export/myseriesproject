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

/**
 * Looks and Feels
 * @author lordovol
 */
public class LookAndFeels {
  static Map<String,LookAndFeelInfo> lafMap;

  /**
   * Sets the look and feel for the application form
   * @param m The application form
   * @param strLaf The lookAndFeel
   */
  public static void setLookAndFeel(MySeries m, String strLaf) {
    getLookAndFeels();
    setLookAndFeel(m, lafMap.get(strLaf));
  }

  public LookAndFeels() {
        
        
    }

  /**
   * Gets the supported look and feels info
   * @return a n array of LookAndFeels info
   */
  public static LookAndFeelInfo[] getLookAndFeels(){
    UIManager.LookAndFeelInfo[] lookAndFeelInfos = UIManager.getInstalledLookAndFeels();
    LookAndFeelInfo laf[] = new LookAndFeelInfo[lookAndFeelInfos.length];
    lafMap = new HashMap<String,LookAndFeelInfo>();
        for (int i = 0; i < lookAndFeelInfos.length; i++) {
            UIManager.LookAndFeelInfo lookAndFeelInfo = lookAndFeelInfos[i];
            lafMap.put(lookAndFeelInfo.getName(), lookAndFeelInfo);
            laf[i] = lookAndFeelInfo;
            
        }
   return laf;
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
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (UnsupportedLookAndFeelException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }
  
}
