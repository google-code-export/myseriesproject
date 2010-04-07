/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.subtitles.sonline;

import tools.download.subtitles.AbstractDownloadForm;

/**
 *
 * @author lordovol
 */
public class SOnlineForm extends AbstractDownloadForm {

  private static final long serialVersionUID = 2353636L;

  /** Creates new form DownloadSubtitlesForm
   * @param sOnlineCode 
   * @param episode
   * @param season
   * @param localDir
   * @param title
   */
  public SOnlineForm(String sOnlineCode, int season, int episode, String localDir, String title) {
    super.init();
    label_title.setText("Download from SubtitleOnline.com");
    label_subtitle.setText(title);
    setLocationRelativeTo(null);
    DownloadSOnline d = new DownloadSOnline(sOnlineCode, season, episode, this);
    d.setLocalDir(localDir);
    Thread t = new Thread(d);
    t.start();
    setVisible(true);

  }
}
