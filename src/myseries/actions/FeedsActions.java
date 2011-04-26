/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.actions;

import database.FeedsRecord;
import java.util.ArrayList;
import java.util.logging.Level;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myseries.MySeries;
import tools.MySeriesLogger;
import tools.feeds.AdminFeed;

/**
 *
 * @author lordovol
 */
public class FeedsActions {

  public static boolean addFeedPanel(int feed_ID, MySeries m) {
    myseries.MySeries.glassPane.activate(null);
    FeedsRecord f = new FeedsRecord(feed_ID);
    MySeriesLogger.logger.log(Level.INFO, "Opening admin feed panel for {0}",
        new String[]{feed_ID ==0 ? f.getTitle(): "new feed"});
    AdminFeed a = new AdminFeed(feed_ID == 0 ? null : f, m);
    myseries.MySeries.glassPane.deactivate();
    return a.isFeedSaved;
  }

  public static void updateFeeds(boolean onStartUp, MySeries m) {
    MySeriesLogger.logger.log(Level.INFO, "Update feeds action");
    if (!MyUsefulFunctions.hasInternetConnection(MyUsefulFunctions.GOOGLE)) {
      ArrayList<FeedsRecord> feeds = FeedsRecord.getAll();
      m.feedTree.updateFeeds(feeds, false);
    } else {
      MyMessages.internetError(!onStartUp);
    }
  }

  private FeedsActions() {
  }
}
