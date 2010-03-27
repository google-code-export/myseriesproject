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
    link = link.replaceAll("\\[", "%5B");
    link = link.replaceAll("\\]", "%5D");
    try {
      return new URI(link);
    } catch (URISyntaxException ex) {
      return null;
    }
  }


}
