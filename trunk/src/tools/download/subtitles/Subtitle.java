/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.download.subtitles;

import java.net.URL;
import java.util.Vector;
import myComponents.MyUsefulFunctions;

/**
 *
 * @author lordovol
 */
public class Subtitle {

  /**
   * List of web subtitle sites
   */
  public static Vector<String> SUBTITLE_SITES = new Vector<String>();
  /**
   * List of available languages
   */
  public static Vector<String> SUBTITLE_LANG = new Vector<String>();

   /**
   * The greek subtitles language : "Greek"
   */
  public static final String GREEK = "Greek";
  /**
   * The english subtitles language : "English"
   */
  public static final String ENGLISH = "English";
  /**
   * The Subtitle none
   */
  public static final String NONE = "None";
  /**
   * The subtitle both
   */
  public static final String BOTH = "Both";
  /**
   * The tv subtitles name : "Tvsubtitles.net"
   */
  public static final String TV_SUBTITLES_NAME = "TvSubtitles.net";
  /**
   * The tv subtitles url : "http://www.tvsubtitles.net/"
   */
  public static final String TV_SUBTITLES_URL = "http://www.tvsubtitles.net/";
  /**
   * The subtitle online name : "SubtitleOnline.com"
   */
  public static final String SUBTITLE_ONLINE_NAME = "SubtitleOnline.com";
  /**
   * The subtitle online url : "http://www.subtitleonline.com/"
   */
  public static String SUBTITLE_ONLINE_URL = "http://www.subtitleonline.com/";

  public String title;
  public URL url;

  /**
   *
   * @param title
   * @param url
   */
  public Subtitle(String title, URL url) {
    this.title = MyUsefulFunctions.deleteDoubleSpaces(title);
    this.url = url;
  }



  @Override
  public String toString() {
    return title;
  }



}
