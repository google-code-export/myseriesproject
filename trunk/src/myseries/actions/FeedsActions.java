/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myseries.actions;

import database.FeedsRecord;

/**
 *
 * @author lordovol
 */
public class FeedsActions {

  public static boolean addFeedPanel(FeedsRecord f){
    myseries.MySeries.glassPane.activate(null);
    AdminFeed a = new AdminFeed(f);
    myseries.MySeries.glassPane.deactivate();
    return a.isFeedSaved;
  }

  private FeedsActions() {
  }

}
