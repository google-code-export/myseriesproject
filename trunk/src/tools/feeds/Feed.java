/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.feeds;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import java.util.ArrayList;

/**
 *
 * @author lordovol
 */
public class Feed {

  public static final String FEEDS_PATH = "feeds/";
  private ArrayList<SyndEntryImpl> entries = new ArrayList<SyndEntryImpl>();

  /**
   * @return the entries
   */
  public ArrayList<SyndEntryImpl> getEntries() {
    return entries;
  }

  /**
   * @param entries the entries to set
   */
  public void setEntries(ArrayList<SyndEntryImpl> entries) {
    this.entries = entries;
  }
}
