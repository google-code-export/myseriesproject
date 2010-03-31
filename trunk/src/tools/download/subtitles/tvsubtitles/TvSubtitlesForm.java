/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.download.subtitles.tvsubtitles;

import myComponents.MyUsefulFunctions;
import tools.download.subtitles.DownloadTvSubtitlesForm;

/**
 *
 * @author lordovol
 */
public class TvSubtitlesForm extends DownloadTvSubtitlesForm {
  private static final long serialVersionUID = 2353636L;

  /** Creates new form DownloadSubtitlesForm */
  public TvSubtitlesForm(String link, int season, int episode, String localDir, String title) {
    super.init();
    label_subtitle.setText(title);
    setLocationRelativeTo(null);
    if (link.indexOf("www.tvsubtitles.net/") == -1) {
      MyUsefulFunctions.message("Wrong subtitles webpage", "The subtitles webpage should be in http://www.subtitles.net");
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
