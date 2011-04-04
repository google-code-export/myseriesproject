/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.feeds;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import database.FeedsRecord;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import myComponents.MyMessages;
import tools.options.Options;
import myComponents.MyUsefulFunctions;
/**
 *
 * @author lordovol
 */
public class FeedReader {

  private final FeedsRecord feedRecord;
  private Feed feed = new Feed();
  private final FeedTree tree;

  public FeedReader(FeedTree tree, FeedsRecord feedRecord) {
    this.feedRecord = feedRecord;
    this.tree = tree;
    _getFeed();
  }

  private void _getFeed() {
    try {
      File file = new File(Options._USER_DIR_ + Feed.FEEDS_PATH + feedRecord.getFeed_ID());
      if (!file.exists()) {
        FeedUpdater fu = new FeedUpdater(tree, feedRecord);
        fu.run();
      }
      
      SyndFeedInput input = new SyndFeedInput();
      SyndFeed feedXml = input.build(new XmlReader(file));
      String title = feedXml.getTitle();
      if (feedRecord.getUrl().indexOf(feedRecord.getTitle())>-1 && !title.equals("")) {
        feedRecord.setTitle(title);
        feedRecord.save();
      }
      List entries = feedXml.getEntries();
      feed = new Feed();
      for (int i = 0; i < entries.size(); i++) {
        SyndEntryImpl entry = (SyndEntryImpl) entries.get(i);
        getFeed().getEntries().add(entry);
      }
    } catch (SQLException ex) {
      MyUsefulFunctions.log(Level.SEVERE, null, ex);
      MyMessages.error("Feed", "Could not save feed to database");
    } catch (FeedException ex) {
      MyUsefulFunctions.log(Level.SEVERE, null, ex);
      MyMessages.error("Feed", "Could not read feed from "+feedRecord.getUrl());
    } catch (IOException ex) {
      MyUsefulFunctions.log(Level.SEVERE, null, ex);
      MyMessages.error("Feed", "Could not read feed from "+feedRecord.getUrl());
    } catch (IllegalArgumentException ex) {
      MyUsefulFunctions.log(Level.SEVERE, null, ex);
      MyMessages.error("Feed", "Could not read feed from "+feedRecord.getUrl());
    }
  }

  /**
   * @return the feed
   */
  public Feed getFeed() {
    return feed;
  }
}
