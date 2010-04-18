/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.subtitles.tvsubtitles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import tools.download.subtitles.AbstractDownloadSubtitle;
import tools.download.subtitles.Subtitle;
import tools.options.Options;

/**
 * Download from tvSubtitles
 * @author lordovol
 */
public class DownloadTvSubtitles extends AbstractDownloadSubtitle implements Runnable {

  private final String link;

  /**
   * Download the subtitles from tvSubtitles
   * @param link The link to the subtitles page
   * @param season The series season
   * @param episode The episode number
   * @param form The messages form
   */
  public DownloadTvSubtitles(String link, int season, int episode, TvSubtitlesForm form) {
    this.link = link;
    this.season = season;
    this.episode = episode;
    this.form = form;
    this.progress = form.progress;
  }

  public void run() {
    progress.setIndeterminate(true);
    progress.setString("Searching for subtitles");
    getSubtitle();
    progress.setIndeterminate(false);
    if (subs.size() == 0) {
      form.dispose();
      MyMessages.error("Subtitle not found", "The subtitle was not found");
    } else if (subs.size() == 1) {
      download(subs.get(0));
      form.dispose();
    } else {
      Subtitle sub = (Subtitle) JOptionPane.showInputDialog(null, "Choose the subtitle to download", "Choose subtitle", JOptionPane.QUESTION_MESSAGE, null, subs.toArray(), 0);
      if (sub != null) {
        String newPath = sub.url.getPath().replace("/subtitle", "download");
        try {
          sub.url = new URL(Subtitle.TV_SUBTITLES_URL + newPath);
        } catch (MalformedURLException ex) {
          MyMessages.error("Error occured!!!", "Wrong url : " + sub.url);
          myseries.MySeries.logger.log(Level.SEVERE, null, ex);
          form.dispose();
        }
        download(sub);
        form.dispose();
      }
    }
    if (!srtFilename.equals("")) {
      MyMessages.message("Subtitle downloaded", "Subtitle\n" + srtFilename + "\nwas downloaded and extracted succesfully");
    }
  }

  private void getSubtitle() {
    try {
      MyUsefulFunctions.initInternetConnection();
      if (MyUsefulFunctions.hasInternetConnection()) {
        String buff = parseWebPage();
        if (!buff.equals("")) {
          String subsLink = getLink(buff, true);
          if (subsLink != null) {
            getDownloadLinks(subsLink);
          } else {
            form.dispose();
          }
        } else {
          form.dispose();
        }
      } else {
        MyMessages.internetError();
        form.dispose();
      }
    } catch (MalformedURLException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
      MyMessages.error("Error occured!!!", "Wrong url");
      form.dispose();
    } catch (IOException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
      MyMessages.error("Error occured!!!", "Could not read input stream");
      form.dispose();
    }
  }

  private String parseWebPage() throws MalformedURLException, IOException {
    URL subsUrl = new URL(link);
    BufferedReader in = new BufferedReader(new InputStreamReader(subsUrl.openStream()));
    String inputLine;
    boolean copy = false;
    String buff = "";
    while ((inputLine = in.readLine()) != null) {
      String regex = "<td>0*" + season + "x0*" + episode + "</td>";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(inputLine);
      if (matcher.find()) {
        copy = true;
      }
      if (copy && inputLine.indexOf("</tr>") > -1) {
        copy = false;
      }
      if (copy) {
        buff += inputLine;
      }
    }
    return buff;
  }

  private String getLink(String buff, boolean getPrimarySub) {
    String lang = "";
    if (getPrimarySub) {
      lang = Options.toString(Options.PRIMARY_SUB).equals("Greek") ? "gr" : "en";
    } else {
      lang = Options.toString(Options.PRIMARY_SUB).equals("Greek") ? "en" : "gr";
    }
    int pos = buff.indexOf("<img src=\"images/flags/" + lang + ".gif\"");
    int i = pos;
    String subLink = null;
    while (i > 0) {
      String character = buff.substring(i - 1, i);
      if (character.equals("<")) {
        subLink = buff.substring(i, pos).replace("a href=\"", "").replaceFirst("\">", "");
        i = 0;
      }
      i--;
    }
    if (subLink != null) {
      return subLink;
    } else {
      if (!getPrimarySub) {
        return null;
      }
      if (MyMessages.question("Download secondary language", "Primary language subs not found.\nDownload secondary?") == JOptionPane.YES_OPTION) {
        return getLink(buff, false);
      } else {
        return null;
      }

    }
  }

  private void getDownloadLinks(String subsLink) throws MalformedURLException, IOException {
    URL subsUrl = new URL(Subtitle.TV_SUBTITLES_URL + subsLink);
    BufferedReader in = new BufferedReader(new InputStreamReader(subsUrl.openStream()));
    String inputLine, line = "";
    while ((inputLine = in.readLine()) != null) {
      String regex = "(<a href=\"/subtitle-)|(<a href=\"download)|(</h5>)";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(inputLine);
      if (matcher.find()) {
        line += inputLine + "\n";
      }
    }
    String[] fields = line.split("(<a href=\"/)|(\"><div href=\"/)|(align=absmiddle>)|(<a href=\")|(\">)", -1);
    String curLink = "";
    String curTitle = "";
    for (int i = 0; i < fields.length; i++) {
      String field = fields[i];
      if ((field.startsWith("subtitle") || (field.startsWith("download"))) && field.endsWith("html")) {
        curLink = Subtitle.TV_SUBTITLES_URL + URLEncoder.encode(field, "UTF-8");
        if (field.startsWith("download")) {
          curTitle = "dummy";
        }
      } else if (fields[i].indexOf("</h5>") > -1) {
        curTitle = field.substring(0, fields[i].indexOf("</h5>"));
      }
      if (!curTitle.equals("") && !curLink.equals("")) {
        subs.add(new Subtitle(curTitle, new URL(curLink)));
        curTitle = "";
        curLink = "";

      }
    }
  }
}
