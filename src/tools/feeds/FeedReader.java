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
import java.util.List;
import java.util.logging.Level;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class FeedReader {
  private final FeedsRecord feedRecord;
  private Feed feed;
  private final FeedTree tree;

  public FeedReader(FeedTree tree, FeedsRecord feedRecord) {
    this.feedRecord = feedRecord;
    this.tree = tree;
    _getFeed();
  }

  private void _getFeed(){
    try {
      File file = new File( Options._USER_DIR_ +Feed.FEEDS_PATH + feedRecord.getFeed_ID());
      if(!file.exists()){
        FeedUpdater fu = new FeedUpdater(tree, feedRecord);
        fu.run();
      }
      SyndFeedInput input = new SyndFeedInput();
      SyndFeed feedXml = input.build(new XmlReader(file));
      List entries = feedXml.getEntries();
      feed = new Feed();
      for (int i = 0; i < entries.size(); i++) {
       SyndEntryImpl entry = (SyndEntryImpl) entries.get(i);
        getFeed().getEntries().add(entry);
      }
    } catch (FeedException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (IllegalArgumentException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }

  /**
   * @return the feed
   */
  public Feed getFeed() {
    return feed;
  }

}
