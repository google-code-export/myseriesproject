/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.feeds;

import database.FeedsRecord;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import tools.options.Options;
import tools.MySeriesLogger;

/**
 *
 * @author lordovol
 */
public class FeedUpdater implements Runnable {

  private ArrayList<FeedsRecord> feeds = new ArrayList<FeedsRecord>();
  private FeedTree tree;
  private boolean readFeeds = true;
  public static boolean updating = false;

  public FeedUpdater(FeedTree tree, FeedsRecord feedRecord) {
    feeds.add(feedRecord);
    this.tree = tree;
  }

  public FeedUpdater(FeedTree tree, ArrayList<FeedsRecord> feeds, boolean readFeeds) {
    this.feeds = feeds;
    this.tree = tree;
    this.readFeeds = readFeeds;
  }

  @Override
  public void run() {
    //myseries.MySeries.tabsPanel.setSelectedIndex(myseries.MySeries.TABS_PANEL_FEEDS);
    //myseries.MySeries.glassPane.activate(null);
    int id = -1;
    updating = true;
    myseries.MySeries.pr_rssUpdating.setVisible(true);
    for (Iterator<FeedsRecord> it = feeds.iterator(); it.hasNext();) {
      FeedsRecord feedRecord = it.next();
      id = feedRecord.getFeed_ID();
      if(MyUsefulFunctions.hasInternetConnection(feedRecord.getUrl())){
      update(feedRecord);
      } else {
        MyMessages.error("Feed update", "Could not connect to " + feedRecord.getUrl());
      }
    }
    updating = false;
    myseries.MySeries.pr_rssUpdating.setVisible(false);
    //myseries.MySeries.glassPane.deactivate();
    
    int[] sel = myseries.MySeries.feedTree.tree.getSelectionRows();
   // myseries.MySeries.feedTree.tree.setSelectionRow(0);
    if (sel == null || sel[0] == 0) {
      myseries.MySeries.feedTree.tree.setSelectionRow(1);
    } else {
      myseries.MySeries.feedTree.tree.setSelectionRow(sel[0]);
    }
  }

  private void update(FeedsRecord feed) {
    try {
      InputStream is = null;
      BufferedOutputStream outStream = null;
      byte[] buf;
      URLConnection uCon = new URL(feed.getUrl()).openConnection();
      is = uCon.getInputStream();
      buf = new byte[1024];
      int ByteRead;
      int ByteWritten = 0;
      String filename = Options._USER_DIR_ + Feed.FEEDS_PATH + feed.getFeed_ID();
      outStream = new BufferedOutputStream(new FileOutputStream(filename));
      while ((ByteRead = is.read(buf)) != -1) {
        outStream.write(buf, 0, ByteRead);
        ByteWritten += ByteRead;
      }
      is.close();
      outStream.close();
    } catch (IOException ex) {
      //MyMessages.error("Feed reader", "Could not read feed from: " + feed.getUrl());
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
  }
}
