/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.feeds;

import Exceptions.DatabaseException;
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
import myComponents.MyMessages;
import myseries.MySeries;
import tools.options.Options;
import tools.MySeriesLogger;
import tools.options.Paths;

/**
 *
 * @author lordovol
 */
public class FeedReader {

    private final FeedsRecord feedRecord;
    private Feed feed = new Feed();
    private final FeedTree tree;
  private MySeries m;

    public FeedReader(FeedTree tree, FeedsRecord feedRecord, MySeries m) {
        MySeriesLogger.logger.log(Level.INFO, "Reading feeds from {0}", feedRecord.getTitle());
        this.feedRecord = feedRecord;
        this.tree = tree;
        this.m = m;
        _getFeed();
    }

    private void _getFeed() {
        try {
            File file = new File(Options._USER_DIR_ + Paths.FEEDS_PATH + feedRecord.getFeed_ID());
            if (!file.exists()) {
                MySeriesLogger.logger.log(Level.INFO, "Feed file does not exist.Updating feed");
                FeedUpdater fu = new FeedUpdater(tree, feedRecord, m, false);
                fu.run();
                MySeriesLogger.logger.log(Level.FINE, "Feed updated");
            }
            MySeriesLogger.logger.log(Level.INFO, "Reading feed file");
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feedXml = input.build(new XmlReader(file));
            String title = feedXml.getTitle();
            if (feedRecord.getUrl().indexOf(feedRecord.getTitle()) > -1 && !title.equals("")) {
                MySeriesLogger.logger.log(Level.INFO, "Setting feed title to {0}",title);
                feedRecord.setTitle(title);
                feedRecord.save();
            }
            List entries = feedXml.getEntries();
            feed = new Feed();
            MySeriesLogger.logger.log(Level.INFO, "Getting {0} entries",entries.size());
            for (int i = 0; i < entries.size(); i++) {
                SyndEntryImpl entry = (SyndEntryImpl) entries.get(i);
                getFeed().getEntries().add(entry);
                MySeriesLogger.logger.log(Level.FINE, "Added entry {0}",entry.getTitle());
            }
        } catch (SQLException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Could not save feed to database", ex);
            MyMessages.error("Feed", "Could not save feed to database", true, false);
        } catch (FeedException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Could not read feed from " + feedRecord.getUrl(), ex);
            MyMessages.error("Feed", "Could not read feed from " + feedRecord.getUrl(), true, false);
        } catch (IOException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Could not read feed from " + feedRecord.getUrl(), ex);
            MyMessages.error("Feed", "Could not read feed from " + feedRecord.getUrl(), true, false);
        } catch (IllegalArgumentException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Could not read feed from " + feedRecord.getUrl(), ex);
            MyMessages.error("Feed", "Could not read feed from " + feedRecord.getUrl(), true, false);
        }
    }

    /**
     * @return the feed
     */
    public Feed getFeed() {
        return feed;
    }
}
