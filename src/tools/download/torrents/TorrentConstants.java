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
   * The EzTv name : EzTv
   */
  public static String EZTV_NAME = "EzTv";
  /**
   * The eztv rss link "http://ezrss.it/search/index.php?"
   */
  public static String EZTV_RSS = "http://ezrss.it/search/index.php?";
  /**
   * The date EZTV RSS stoped working
   */
  public static String EZTV_OFF = "2012-09-22";
  /**
   * The eztv search link "http://eztv.it/search.php?"
   */
  public static String EZTV_SEARCH = "http://eztv.it/search.php?";
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
  /**
   * Isohunt results column title : 0
   */
  public static int ISOHUNT_RESULTS_TITLE = 0;
  /**
   * Isohunt results column files : 1
   */
  public static int ISOHUNT_RESULTS_FILES = 1;
  /**
   * Isohunt results column length : 2
   */
  public static int ISOHUNT_RESULTS_LENGTH = 2;
  /**
   * Isohunt results column seeds : 3
   */
  public static int ISOHUNT_RESULTS_SEEDS = 3;
  /**
   * Isohunt results column leechers : 4
   */
  public static int ISOHUNT_RESULTS_LEECHERS = 4;
  /**
   * Isohunt results column download : 5
   */
  public static int ISOHUNT_RESULTS_DOWNLOAD = 5;
  
}
