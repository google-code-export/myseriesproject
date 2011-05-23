/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.subtitles.sonline;

import java.io.FileNotFoundException;
import tools.MySeriesLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import sdialogs.Info;
import tools.download.screenshot.SelectSubtitle;
import tools.download.subtitles.AbstractDownloadSubtitle;
import tools.download.subtitles.Subtitle;
import tools.download.subtitles.SubtitleConstants;
import tools.languages.LangsList;
import tools.languages.Language;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class DownloadSOnline extends AbstractDownloadSubtitle implements Runnable, SubtitleConstants {

  private String sOnlineCode;

  /**
   * Download subtitle from subtitleOnline
   * @param sOnlineCode The series subtitleonline code
   * @param season The series season
   * @param episode The episode to download subtitles for
   * @param form The messages form
   */
  public DownloadSOnline(String sOnlineCode, int season, int episode, SOnlineForm form) {
    MySeriesLogger.logger.log(Level.INFO, "Downloading subtitles from subtitles online :{0} season {1} episode {2}", new Object[]{sOnlineCode, season, episode});
    this.sOnlineCode = sOnlineCode;
    this.season = season;
    this.episode = episode;
    this.form = form;
    this.progress = form.progress;
    this.lang[0] = myseries.MySeries.languages.getPrimary().getName();
    this.lang[1] = myseries.MySeries.languages.getSecondary().getName();
  }

  public void run() {
    progress.setIndeterminate(true);
    progress.setString("Searching for subtitles");
    try {
      getSubtitle();
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.WARNING, "Could not read input stream", ex);
      MyMessages.error("IO error", "Could not read from input stream", true);
    }
    progress.setIndeterminate(false);
    if (subs.isEmpty()) {
      MySeriesLogger.logger.log(Level.INFO, "Subtitle was not found");
      form.dispose();
      if (!cancel) {
        MyMessages.message("Subtitle not found", "The subtitle was not found", Info.WARNING_MESS, true, true);
      }
    } else if (subs.size() == 1) {
      MySeriesLogger.logger.log(Level.FINE, "Found one subtitle.Downloading {0}", subs.get(0).url);
      download(subs.get(0));
      form.dispose();
    } else {
      MySeriesLogger.logger.log(Level.FINE, "Found {0} subtitles", subs.size());
      SelectSubtitle ss = new SelectSubtitle(subs);
      Subtitle sub = ss.subtitle;
      if (sub != null) {
        MySeriesLogger.logger.log(Level.INFO, "Downloadinf subtitle {0}", sub.url);
        download(sub);
        form.dispose();
      } else {
        form.dispose();
      }
    }
    if (!srtFilename.equals("")) {
      MySeriesLogger.logger.log(Level.FINE, "Subtitle downloaded");
      MyMessages.message("Subtitle downloaded", "Subtitle\n" + srtFilename + "\nwas downloaded and extracted succesfully");
    }
  }

  private void getSubtitle() throws FileNotFoundException, IOException {
    if (MyUsefulFunctions.hasInternetConnection(SUBTITLE_ONLINE_URL)) {
      URL url = new URL(SUBTITLE_ONLINE_URL + sOnlineCode + "-season-" + season + "-episode-" + episode + "-subtitles.html");
      MySeriesLogger.logger.log(Level.INFO, "Getting subtitle from url {0}", url);
      parseWebpage(url, true);
    } else {
      MyMessages.internetError(true);
      form.dispose();
    }
  }

  private void parseWebpage(URL wpUrl, boolean primary) throws IOException {
    MySeriesLogger.logger.log(Level.INFO, "Parsing webpage");
    String line = "", dLine = "";
    URL curLink = null;
    String curtitle = "";
    String[] search = new String[NUM_OF_SUBTITLES];
    for (int i = 0; i < NUM_OF_SUBTITLES; i++) {
      search[i] = "<a href=\"/" + sOnlineCode + "-s" + season + "e" + episode + "-" + lang[i] + "-subtitles-download";
    
      BufferedReader in = new BufferedReader(new InputStreamReader(wpUrl.openStream()));
      URL dUrl;
      String s= search[i].toLowerCase();
      while ((line = in.readLine()) != null) {
        if (line.indexOf(s) > -1) {
          line = line.replaceAll("(<a href=\"/)|(\">)", "").trim();
          String[] tokens = line.split("-");
          String code = tokens[tokens.length - 1].replaceAll(".html", "");
          dUrl = new URL(SUBTITLE_ONLINE_URL + line);
          BufferedReader dIn = new BufferedReader(new InputStreamReader(dUrl.openStream()));
          while ((dLine = dIn.readLine()) != null) {
            if (dLine.indexOf(".zip") > -1) {
              dLine = dLine.substring(0, dLine.indexOf(".zip") + 4).replaceAll("<a href=\"/", "");
              String url = SUBTITLE_ONLINE_URL + dLine.trim();
              curLink = new URL(url);
            }
          }
          dIn.close();
          line = in.readLine();
          curtitle = line.replaceAll("</a>", "");
          MySeriesLogger.logger.log(Level.FINE, "Subtitle found :{0}", curtitle);
          subs.add(new Subtitle(curtitle, curLink, 0, 0, LangsList.getLanguageByName(lang[i]).getCode()));
        }
      }
     in.close();
    }
//        if (subs.isEmpty() && primary && Options.toBoolean(Options.SEARCH_FOR_SECONDARY_SUBTITLE)) {
//            MySeriesLogger.logger.log(Level.INFO, "Subtitle not found.Asking for secondary");
//            if (MyMessages.confirm("Download secondary language", "Primary language subs not found.\nSearch for secondary?", true) == JOptionPane.YES_OPTION) {
//                parseWebpage(in, false);
//            } else {
//                MySeriesLogger.logger.log(Level.INFO, "Downloading aborted by the user");
//                cancel = true;
//            }
//        }
  }
}
