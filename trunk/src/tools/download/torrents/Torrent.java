/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.download.torrents;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lordovol
 */
public class Torrent {
  /**
   * The torrents path : "torrents/"
   */
  public static final String TORRENTS_PATH = "torrents/";
  /**
   * The eztv rss link
   */
  public static String EZTV_RSS = "http://ezrss.it/search/index.php?";
  public String title;
  public String link;

  Torrent(String title, String link) {
    this.title = title;
    this.link = link;
  }

  @Override
  public String toString() {
    return title;
  }

  public URI getUri() {
   String encodedLink = link.replaceAll("\\[", "%5B");
    encodedLink = encodedLink.replaceAll("\\]", "%5D");
    try {
      return new URI(encodedLink);
    } catch (URISyntaxException ex) {
      return null;
    }
  }


}
