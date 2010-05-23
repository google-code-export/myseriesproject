/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.subtitles.sonline;

import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import tools.download.subtitles.AbstractDownloadSubtitle;
import tools.download.subtitles.Subtitle;
import tools.languages.LangsList;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class DownloadSOnline extends AbstractDownloadSubtitle implements Runnable {

  private String sOnlineCode;

  /**
   * Download subtitle from subtitleOnline
   * @param sOnlineCode The series subtitleonline code
   * @param season The series season
   * @param episode The episode to download subtitles for
   * @param form The messages form
   */
  public DownloadSOnline(String sOnlineCode, int season, int episode, SOnlineForm form) {
    this.sOnlineCode = sOnlineCode;
    this.season = season;
    this.episode = episode;
    this.form = form;
    this.progress = form.progress;
  }

  public void run() {
    progress.setIndeterminate(true);
    progress.setString("Searching for subtitles");
    try {
      getSubtitle();
    } catch (IOException ex) {
      myseries.MySeries.logger.log(Level.WARNING, "Could not read input stream", ex);
      MyMessages.error("IO error", "Could not read from input stream");
    }
    progress.setIndeterminate(false);
    if (subs.size() == 0) {
      form.dispose();
      if (!cancel) {
        MyMessages.message("Subtitle not found", "The subtitle was not found");
      }
    } else if (subs.size() == 1) {
      download(subs.get(0));
      form.dispose();
    } else {
      Subtitle sub = (Subtitle) JOptionPane.showInputDialog(null, "Choose the subtitle to download", "Choose subtitle", JOptionPane.QUESTION_MESSAGE, null, subs.toArray(), 0);
      if (sub != null) {
        download(sub);
        form.dispose();
      }
    }
    if (!srtFilename.equals("")) {
      MyMessages.message("Subtitle downloaded", "Subtitle\n" + srtFilename + "\nwas downloaded and extracted succesfully");
    }
  }

  private void getSubtitle() throws FileNotFoundException, IOException {
    if (MyUsefulFunctions.hasInternetConnection(Subtitle.SUBTITLE_ONLINE_URL)) {
      URL url = new URL(Subtitle.SUBTITLE_ONLINE_URL + sOnlineCode + "-season-" + season + "-episode-" + episode + "-subtitles.html");
      BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
      parseWebpage(in, true);
    } else {
      MyMessages.internetError();
      form.dispose();

    }
  }

  private void parseWebpage(BufferedReader in, boolean primary) throws IOException {
    String line = "";
    URL curLink = null;
    String curtitle = "";
    String lang;
   lang = primary ? myseries.MySeries.languages.getPrimary().getName().toLowerCase() :
     myseries.MySeries.languages.getSecondary().getName().toLowerCase();

    String search = "<a href=\"/" + sOnlineCode + "-s" + season + "e" + episode + "-" + lang + "-subtitles-download";
    while ((line = in.readLine()) != null) {
      if (line.indexOf(search.toLowerCase()) > -1) {
        line = line.replaceAll("(<a href=\"/)|(\">)", "").trim();
        String[] tokens = line.split("-");
        String code = tokens[tokens.length - 1].replaceAll(".html", "");
        String url = Subtitle.SUBTITLE_ONLINE_URL + "download-subtitle-" + code + ".html";
        curLink = new URL(url);
        line = in.readLine();
        curtitle = line.replaceAll("</a>", "");
        subs.add(new Subtitle(curtitle, curLink));
      }
    }
    if (subs.size() == 0 && primary) {
      if (MyMessages.question("Download secondary language", "Primary language subs not found.\nSearch for secondary?") == JOptionPane.YES_OPTION) {
        parseWebpage(in, false);
      } else {
        cancel = true;
      }
    }
  }
}
