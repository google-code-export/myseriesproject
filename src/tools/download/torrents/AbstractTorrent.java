/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.torrents;

import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author lordovol
 */
public abstract class AbstractTorrent {

  /**
   * The torrents title
   */
  private String title;
  /**
   * The torrents link
   */
  private String link;

  public AbstractTorrent(){
    this.title = "";
    this.link = "";
  }

  public AbstractTorrent(String title, String link) {
    this.title = title;
    this.link = link;
  }

  @Override
  public String toString() {
    return getTitle();
  }

  /**
   * Gets the torrents URI
   * @return The torrents URI or null if there's a syntax error
   */
  public URI getUri() {
    String encodedLink = getLink().replaceAll("\\[", "%5B");
    encodedLink = encodedLink.replaceAll("\\]", "%5D");
    try {
      return new URI(encodedLink);
    } catch (URISyntaxException ex) {
      return null;
    }
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the link
   */
  public String getLink() {
    return link;
  }

  /**
   * @param link the link to set
   */
  public void setLink(String link) {
    this.link = link;
  }
}
