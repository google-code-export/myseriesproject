/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.download.subtitles;

import java.util.Vector;
import tools.languages.Language;

/**
 *
 * @author lordovol
 */
public interface SubtitleConstants {
  /**
   * List of web subtitle sites
   */
  public static Vector<String> SUBTITLE_SITES = new Vector<String>();
  /**
   * List of available languages
   */
  public static Vector<Language> SUBTITLE_LANG = new Vector<Language>();

   /**
   * The greek subtitles language : "Greek"
   */
  //public static final String GREEK = "Greek";
  /**
   * The english subtitles language : "English"
   */
  //public static final String ENGLISH = "English";
  /**
   * The Subtitle none
   */
  public static final String NONE = "None";
  /**
   * The subtitle both
   */
  public static final String BOTH = "Both";
  /**
   * The subtitle unaware state
   */
  public static String UNAWARE = "";
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

  /**
   * The subtitle extensions (srt, sub)
   */
  public static final String[] EXTENSIONS = {"srt", "sub","zip","rar","7z"};
/**
 * Ziped subtitles extensions (zip, rar)
 */
  public static final String[] ZIP_EXT = new String[]{"zip","rar","7z"};
  

}
