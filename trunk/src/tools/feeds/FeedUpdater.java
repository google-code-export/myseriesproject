/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.feeds;

import database.FeedsRecord;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myseries.MySeries;
import tools.options.Options;
import tools.MySeriesLogger;
import tools.options.Paths;

/**
 *
 * @author lordovol
 */
public class FeedUpdater implements Runnable {

  private ArrayList<FeedsRecord> feeds = new ArrayList<FeedsRecord>();
  private FeedTree tree;
  private boolean readFeeds = true;
  private MySeries m;
  public static boolean updating = false;
  private Icon DEFAULT_ICON = new ImageIcon(getClass().getResource("/images/rss_tab.png"));
  private Icon ANIMATION_0 = new ImageIcon(getClass().getResource("/images/rss_0.png"));
  private Icon ANIMATION_1 = new ImageIcon(getClass().getResource("/images/rss_1.png"));
  private Icon ANIMATION_2 = new ImageIcon(getClass().getResource("/images/rss_2.png"));
  private Icon ANIMATION_3 = new ImageIcon(getClass().getResource("/images/rss_3.png"));
  private static int frame = -1;

  public FeedUpdater(FeedTree tree, FeedsRecord feedRecord, MySeries m) {
    MySeriesLogger.logger.log(Level.INFO, "Updating feed {0}", feedRecord.getTitle());
    feeds.add(feedRecord);
    this.m = m;
    this.tree = tree;

  }

  public FeedUpdater(FeedTree tree, ArrayList<FeedsRecord> feeds, boolean readFeeds, MySeries m) {
    this.feeds = feeds;
    this.m = m;
    this.tree = tree;
    this.readFeeds = readFeeds;
  }

  @Override
  public void run() {
    int id = -1;
    updating = true;
    m.pr_rssUpdating.setVisible(true);
    Timer animation = new Timer(400, new AnimationListener());
    animation.start();
    for (Iterator<FeedsRecord> it = feeds.iterator(); it.hasNext();) {
      FeedsRecord feedRecord = it.next();
      id = feedRecord.getFeed_ID();
      if (MyUsefulFunctions.hasInternetConnection(feedRecord.getUrl())) {
        update(feedRecord);
        MySeriesLogger.logger.log(Level.FINE, "Feed updated");
      } else {
        MySeriesLogger.logger.log(Level.WARNING, "Could not connect to {0}", feedRecord.getUrl());
        MyMessages.warning("Feed update", "Could not connect to " + feedRecord.getUrl(), true);
      }
    }
    updating = false;
    int ind = m.tabsPanel.getIndexByName(String.valueOf(myseries.MySeries.TAB_FEEDS_ID));
    m.tabsPanel.setIconAt(ind, DEFAULT_ICON);
    m.pr_rssUpdating.setVisible(false);
    animation.stop();

    int[] sel = m.feedTree.tree.getSelectionRows();
    // myseries.MySeries.feedTree.tree.setSelectionRow(0);
    if (sel == null || sel[0] == 0) {
      m.feedTree.tree.setSelectionRow(1);
    } else {
      m.feedTree.tree.setSelectionRow(sel[0]);
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
      String filename = Options._USER_DIR_ + Paths.FEEDS_PATH + feed.getFeed_ID();
      outStream = new BufferedOutputStream(new FileOutputStream(filename));
      while ((ByteRead = is.read(buf)) != -1) {
        outStream.write(buf, 0, ByteRead);
        ByteWritten += ByteRead;
      }
      is.close();
      outStream.close();
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not read from input stream", ex);
    }
  }

  public class AnimationListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      int ind = m.tabsPanel.getIndexByName(String.valueOf(myseries.MySeries.TAB_FEEDS_ID));
      frame++;
      switch (frame) {
        case 0:
          m.tabsPanel.setIconAt(ind, ANIMATION_0);
          break;
        case 1:
          m.tabsPanel.setIconAt(ind, ANIMATION_1);
          break;
        case 2:
          m.tabsPanel.setIconAt(ind, ANIMATION_2);
          break;
        case 3:
          m.tabsPanel.setIconAt(ind, ANIMATION_3);
          frame = -1;
          break;
        default:
          m.tabsPanel.setIconAt(ind, ANIMATION_3);
          frame = -1;
          break;
      }
    }
  }
}
