/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.torrents;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.MySeriesLogger;
import tools.download.torrents.uriSchemeHandler.CouldNotOpenUriSchemeHandler;
import tools.download.torrents.uriSchemeHandler.URISchemeHandler;

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
  
  /**
   * If this is a magnet link
   */
  private boolean isMagnetLink = false;

  public AbstractTorrent() {
    this.title = "";
    this.link = "";
  }

  public AbstractTorrent(String title, String link) {
    this.title = title.trim();
    this.link = link.trim();
  }

  @Override
  public String toString() {
    return getTitle();
  }

  /**
   * Gets the torrents URI
   *
   * @return The torrents URI or null if there's a syntax error
   */
  public URI getUri() {
    String link = getLink();
    String encodedLink = null;
    encodedLink = getLink().replaceAll("\\[", "%5B").replaceAll("\\]", "%5D");
    if(link.startsWith("magnet")){
      encodedLink = encodedLink.replaceAll(" ", "%20");
      try {
        setIsMagnetLink(true);
        return new URI(encodedLink);
      } catch (URISyntaxException ex1) {
        MySeriesLogger.logger.log(Level.SEVERE, "Wrong syntax for magnet link URI",ex1);
      }
    } else {
      try {    
        return new URI(encodedLink);
      } catch (URISyntaxException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "URI syntax exception",ex);
      }
      
    }
      return null;
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
    this.title = title.trim();
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
    this.link = link.trim();
  }

  /**
   * @return the isMagnetLink
   */
  public boolean isIsMagnetLink() {
    return isMagnetLink;
  }

  /**
   * @param isMagnetLink the isMagnetLink to set
   */
  public void setIsMagnetLink(boolean isMagnetLink) {
    this.isMagnetLink = isMagnetLink;
  }
}
