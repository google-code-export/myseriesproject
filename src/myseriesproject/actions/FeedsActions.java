/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseriesproject.actions;

import database.FeedsRecord;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myseriesproject.MySeries;
import tools.MySeriesLogger;
import tools.feeds.AdminFeed;

/**
 *
 * @author lordovol
 */
public class FeedsActions {

  public static boolean addFeedPanel(int feed_ID, MySeries m) {
    myseriesproject.MySeries.glassPane.activate(null);
    FeedsRecord f;
    try {
      f = FeedsRecord.queryOne(FeedsRecord.C_FEED_ID + "=?",
          new String[]{String.valueOf(feed_ID)}, null);
      if(f==null){
        f = new FeedsRecord();
      }
      MySeriesLogger.logger.log(Level.INFO, "Opening admin feed panel for {0}",
          new String[]{feed_ID == 0 ? f.getTitle() : "new feed"});
      AdminFeed a = new AdminFeed(feed_ID == 0 ? null : f, m);
      myseriesproject.MySeries.glassPane.deactivate();
      return a.isFeedSaved;
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      return false;
    }
  }

  public static void updateFeeds(boolean onStartUp, MySeries m) {
    MySeriesLogger.logger.log(Level.INFO, "Update feeds action");
    if (MyUsefulFunctions.hasInternetConnection(MyUsefulFunctions.GOOGLE)) {
      ArrayList<FeedsRecord> feeds = FeedsRecord.getAll();
      m.feedTree.updateFeeds(feeds, false);
    } else {
      MyMessages.internetError();
    }
  }

  private FeedsActions() {
  }
}
