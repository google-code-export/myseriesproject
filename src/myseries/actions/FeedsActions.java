/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.actions;

import database.FeedsRecord;
import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import myseries.MySeriesConstants;
import tools.feeds.AdminFeed;
import tools.feeds.FeedLeaf;
import tools.feeds.FeedReader;
import tools.feeds.FeedTree;

/**
 *
 * @author lordovol
 */
public class FeedsActions {

  public static boolean addFeedPanel(int feed_ID) {
    myseries.MySeries.glassPane.activate(null);
    FeedsRecord f = new FeedsRecord(feed_ID);
    AdminFeed a = new AdminFeed(feed_ID == 0 ? null : f);
    myseries.MySeries.glassPane.deactivate();
    return a.isFeedSaved;
  }

  public static void updateFeeds() {
    ArrayList<FeedsRecord> feeds = FeedsRecord.getAll();
    myseries.MySeries.feedTree.updateFeeds(feeds);
  }

  private FeedsActions() {
  }
}
