/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.download.subtitles;

import java.util.ArrayList;
import myseriesproject.MySeries;
import tools.languages.Language;
import tools.options.MySeriesOptions;

/**
 *
 * @author lordovol
 */
public interface SubtitleConstants {
 
  /**
   * List of available languages
   */
  public static ArrayList<Language> SUBTITLE_LANG = new ArrayList<Language>();

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
  //public static final String[] EXTENSIONS = {"srt", "sub","ssa", "zip","rar","7z","gz","tar"};
  public static final String[] EXTENSIONS = {"srt", "sub","ssa"};
/**
 * Ziped subtitles extensions (zip, rar)
 */
  public static final String[] ZIP_EXT = new String[]{"zip","rar","7z","gz","tar"};
  /**
   * The number of subs to search for : 2 , primary and secondary
   */
  public static int NUM_OF_SUBTITLES = MySeries.options.getBooleanOption(MySeriesOptions.SEARCH_FOR_SECONDARY_SUBTITLE) ? 2 : 1;

}
