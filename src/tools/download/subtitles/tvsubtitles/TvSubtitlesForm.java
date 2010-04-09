/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.download.subtitles.tvsubtitles;

import myComponents.MyMessages;
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
    label_title.setText("Download from TVSubtitles.net");
    label_subtitle.setText(title);
    setLocationRelativeTo(null);
    if (link.indexOf("www.tvsubtitles.net/") == -1) {
      MyMessages.error("Wrong subtitles webpage", "The subtitles webpage should be in http://www.subtitles.net");
      dispose();
    } else {
      DownloadTvSubtitles d = new DownloadTvSubtitles(link, season, episode, this);
      d.setLocalDir(localDir);
      Thread t = new Thread(d);
      t.start();
      setVisible(true);
    }
  }
}
