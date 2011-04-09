/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.subtitles.tvsubtitles;

import java.util.logging.Level;
import tools.MySeriesLogger;
import tools.download.subtitles.AbstractDownloadForm;

/**
 * The tvSubtitles message form
 * @author lordovol
 */
public class TvSubtitlesForm extends AbstractDownloadForm {

  private static final long serialVersionUID = 2353636L;

  /** Creates new form DownloadSubtitlesForm
   * @param link The link to the subtitle
   * @param season The series season
   * @param episode The episode number
   * @param localDir The series local directory
   * @param title The episode's title
   */
  public TvSubtitlesForm(String link, int season, int episode, String localDir, String title) {
    super.init();
      MySeriesLogger.logger.log(Level.INFO, "Showing download subtitles from Tv Subtitles form for episode {0}",title);
    label_title.setText("Download from TVSubtitles.net");
    label_subtitle.setText(title);
    setLocationRelativeTo(null);
    DownloadTvSubtitles d = new DownloadTvSubtitles(link, season, episode, this);
    d.setLocalDir(localDir);
    Thread t = new Thread(d);
    t.start();
    setVisible(true);
  }

  public TvSubtitlesForm(String link, int season,  String localDir) {
      MySeriesLogger.logger.log(Level.INFO, "Showing download whole season subtitles from Tv Subtitles form");
    super.init();
    label_title.setText("Download whole season subtitles from TVSubtitles.net");
    label_subtitle.setText("Whole season");
    setLocationRelativeTo(null);
    DownloadTvSubtitles d = new DownloadTvSubtitles(link, season,-1 , this);
    d.setLocalDir(localDir);
    Thread t = new Thread(d);
    t.start();
    setVisible(true);
  }
}
