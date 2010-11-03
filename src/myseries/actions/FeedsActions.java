/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myseries.actions;

import database.FeedsRecord;
import tools.feeds.AdminFeed;

/**
 *
 * @author lordovol
 */
public class FeedsActions {

  public static boolean addFeedPanel(int feed_ID){
    myseries.MySeries.glassPane.activate(null);
    FeedsRecord f = new FeedsRecord(feed_ID);
    AdminFeed a = new AdminFeed(feed_ID==0 ? null :f);
    myseries.MySeries.glassPane.deactivate();
    return a.isFeedSaved;
  }

  private FeedsActions() {
  }

}
