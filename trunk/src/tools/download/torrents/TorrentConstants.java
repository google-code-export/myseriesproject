/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.torrents;

/**
 *
 * @author ssoldatos
 */
public interface TorrentConstants {

  /**
   * The torrents path : "torrents/"
   */
  public static final String TORRENTS_PATH = "torrents/";
  /**
   * The EzTv name : EzTv
   */
  public static String EZTV_NAME = "EzTv";
  /**
   * The eztv rss link "http://ezrss.it/search/index.php?"
   */
  public static String EZTV_RSS = "http://ezrss.it/search/index.php?";
  /**
   * The Isohunt name : Isohunt
   */
  public static String ISOHUNT_NAME = "Isohunt";
  /**
   * The eztv rss link "http://ezrss.it/search/index.php?"
   */
  public static String ISOHUNT_JSON = "http://isohunt.com/js/json.php?";
  /**
   * List of qualities
   */
  public static String[] QUALITIES = {"", "HDTV", "720p", "1080i", "1080p", "DSRip", "DVBRip", "DVDR", "DVDRip", "DVDScr", "HR.HDTV", "HR.PDTV", "PDTV", "SatRip", "SVCD", "TVRip", "WebRip"};
  /**
   * List of sorting options
   */
  public static String[] SORT_OPTIONS = {"", "Seeds", "Age", "Size"};
  /**
   * List of sorting orer
   */
  public static String[] SORT_ORDER = {"Desc", "Asc"};
}
