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
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultTreeCellRenderer;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class FeedUpdater implements Runnable {

  private ArrayList<FeedsRecord> feeds = new ArrayList<FeedsRecord>();
  private FeedTree tree;

  public FeedUpdater(FeedTree tree, FeedsRecord feedRecord) {
    feeds.add(feedRecord);
    this.tree = tree;
  }

  public FeedUpdater(FeedTree tree, ArrayList<FeedsRecord> feeds) {
    this.feeds = feeds;
    this.tree = tree;
  }

  public void run() {
    for (Iterator<FeedsRecord> it = feeds.iterator(); it.hasNext();) {
      FeedsRecord feedRecord = it.next();
      DefaultTreeCellRenderer r = (DefaultTreeCellRenderer) tree.getCellRenderer();
      r.setOpenIcon(new ImageIcon(getClass().getResource("/images/rss_opened.png")));
      tree.tree.revalidate();
      tree.tree.repaint();
      update(feedRecord);
      r.setLeafIcon(new ImageIcon(getClass().getResource("/images/rss.png")));
      tree.revalidate();
      tree.repaint();
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
      String filename = Options._USER_DIR_ +Feed.FEEDS_PATH + feed.getFeed_ID();
      outStream = new BufferedOutputStream(new FileOutputStream( filename));
      while ((ByteRead = is.read(buf)) != -1) {
        outStream.write(buf, 0, ByteRead);
        ByteWritten += ByteRead;
      }
      is.close();
      outStream.close();
    } catch (IOException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }
}
