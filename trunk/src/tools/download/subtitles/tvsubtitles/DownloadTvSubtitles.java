/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.subtitles.tvsubtitles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import tools.MySeriesLogger;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import tools.download.subtitles.SelectSubtitle;
import tools.download.subtitles.AbstractDownloadSubtitle;
import tools.download.subtitles.Subtitle;
import tools.download.subtitles.SubtitleConstants;

/**
 * Download from tvSubtitles
 * @author lordovol
 */
public class DownloadTvSubtitles extends AbstractDownloadSubtitle implements Runnable, SubtitleConstants {

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
    this.lang[0] = myseriesproject.MySeries.languages.getPrimary().getCode();
    this.lang[1] = myseriesproject.MySeries.languages.getSecondary().getCode();
  }

  public void run() {
    progress.setIndeterminate(true);
    progress.setString("Searching for subtitles");
    if (episode != -1) {
      MySeriesLogger.logger.log(Level.INFO, "Downloading from tv subtitles series {0} seadon {1} episode {2}", new Object[]{link, season, episode});
      getSubtitle();
    } else {
      try {
        MySeriesLogger.logger.log(Level.INFO, "Downloading whole season subtitles for series {0} seadon {1}", new Object[]{link, season});
        subs.add(new Subtitle("Whole season subtitles", new URL(link), 0, 0, ""));
      } catch (MalformedURLException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "Malformed url: {0}", link);
        MyMessages.error("Download whole season subtitles", "Malformed url: " + link, true);
      }
    }
    progress.setIndeterminate(false);
    if (subs.isEmpty()) {
      MySeriesLogger.logger.log(Level.INFO, "Subtitle not found");
      form.dispose();
      if (!cancel) {
        MyMessages.warning("Subtitle not found", "The subtitle was not found", true);
      }
    } else if (subs.size() == 1) {
      if(subs.get(0).language.equals(lang[0]) || MyMessages.confirm("Download subtitle", "Only secondary language found.Download?",true) == JOptionPane.OK_OPTION){
        MySeriesLogger.logger.log(Level.FINE, "Found one subtitle.Downloading {0}", subs.get(0).url);
        download(subs.get(0));
      }
      form.dispose();
    } else {
      // Subtitle sub = (Subtitle) JOptionPane.showInputDialog(null, "Choose the subtitle to download", "Choose subtitle", JOptionPane.QUESTION_MESSAGE, null, subs.toArray(), 0);
      MySeriesLogger.logger.log(Level.FINE, "Found {0} subtitles", subs.size());
      SelectSubtitle ss = new SelectSubtitle(subs);
      Subtitle sub = ss.subtitle;
      //Subtitle sub = (Subtitle) MyMessages.ask("Choose subtitle", "Choose the subtitle to download", null, subLabels, null, true);
      if (sub != null) {
        MySeriesLogger.logger.log(Level.INFO, "Downloading subtitle {0}", sub.title);
        String newPath = sub.url.getPath().replace("/subtitle", "download");
        try {
          sub.url = new URL(TV_SUBTITLES_URL + newPath);
        } catch (MalformedURLException ex) {
          MyMessages.error("Error occured!!!", "Wrong url : " + sub.url, true);
          MySeriesLogger.logger.log(Level.SEVERE, null, ex);
          form.dispose();
        }
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

  private void getSubtitle() {
    try {
      if (MyUsefulFunctions.hasInternetConnection(TV_SUBTITLES_URL)) {
        MySeriesLogger.logger.log(Level.INFO, "Getting the subtitle links");
        String buff = parseWebPage();
        if (!buff.equals("")) {
          String[] subsLink = getLink(buff, true);
          if (!MyUsefulFunctions.isAllArrayElementsNull(subsLink)) {
            MySeriesLogger.logger.log(Level.FINE, "Subtitle page found");
            for (int i = 0; i < subsLink.length; i++) {
              if (subsLink[i] != null) {
                getDownloadLinks(subsLink[i], lang[i]);
              }
            }

          } else {
            MySeriesLogger.logger.log(Level.INFO, "No links found");
            form.dispose();
          }
        } else {
          MySeriesLogger.logger.log(Level.INFO, "Could not parse page");
          form.dispose();
        }
      } else {
        MyMessages.internetError();
        form.dispose();
      }
    } catch (MalformedURLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      MyMessages.error("Error occured!!!", "Wrong url", true);
      form.dispose();
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      MyMessages.error("Error occured!!!", "Could not read input stream", true);
      form.dispose();
    }
  }

  private String parseWebPage() throws MalformedURLException, IOException {
    MySeriesLogger.logger.log(Level.INFO, "Parsing webpage {0}", link);
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

  private String[] getLink(String buff, boolean primary) {
    MySeriesLogger.logger.log(Level.INFO, "Getting the subtitle link");

    String[] subLink = new String[NUM_OF_SUBTITLES];
    int[] pos = new int[NUM_OF_SUBTITLES];
    int[] i = new int[NUM_OF_SUBTITLES];

    for (int j = 0; j < NUM_OF_SUBTITLES; j++) {
      pos[j] = buff.indexOf("<img src=\"images/flags/" + lang[j] + ".gif\"");
      i[j] = pos[j];
      while (i[j] > 0) {
        String character = buff.substring(i[j] - 1, i[j]);
        if (character.equals("<")) {
          subLink[j] = buff.substring(i[j], pos[j]).replace("a href=\"", "").replaceFirst("\">", "");
          i[j] = 0;
        }
        i[j]--;
      }
    }
    if (!MyUsefulFunctions.isAllArrayElementsNull(subLink)) {
      MySeriesLogger.logger.log(Level.FINE, "Subtitle link found");
    }
    return subLink;
  }

  private void getDownloadLinks(String subsLink, String language) throws MalformedURLException, IOException {
    MySeriesLogger.logger.log(Level.INFO, "Getting the download links from {0}", subsLink);
    URL subsUrl = new URL(TV_SUBTITLES_URL + subsLink);
    BufferedReader in = new BufferedReader(new InputStreamReader(subsUrl.openStream()));
    String inputLine, line = "";
    while ((inputLine = in.readLine()) != null) {

      String regex = "(<a href=\"/subtitle-)|"
              + "(<a href=\"download)|"
              + "(<b id=\"hate\")|"
              + "(<b id=\"love\")|"
              + "(span style=\"color:red\")|"
              + "(span style=\"color:green\")|"
              + "(</h5>)";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(inputLine);
      if (matcher.find()) {
        line += inputLine + "\n";
      }
    }
    String[] fields = line.split("(<a href=\"/)|(\"><div href=\"/)|(align=absmiddle>)|(<a href=\")|(\">)|"
            + "(<b id=\")|"
            + "(span style=\")|"
            + "(<b id=\")|"
            + "(<b id=\")", -1);
    String curLink = "";
    String curTitle = "";
    int love = 0;
    int hate = 0;
    for (int i = 0; i < fields.length; i++) {
      String field = fields[i];
      if ((field.startsWith("subtitle") || (field.startsWith("download"))) && field.endsWith("html")) {
        curLink = TV_SUBTITLES_URL + URLEncoder.encode(field, "UTF-8");
        if (field.startsWith("download")) {
          curTitle = "Season " + season + " Episode " + episode;
        }
      } else if (fields[i].indexOf("</h5>") > -1) {
        curTitle = field.substring(0, fields[i].indexOf("</h5>"));
      }
      if (fields[i].startsWith("hate") && (!fields[i + 1].startsWith("Rate") && !fields[i + 1].startsWith("Bad"))) {
        hate = getLoveHate(fields[i + 1]);
      } else if (fields[i].startsWith("love") && !fields[i + 1].startsWith("Good")) {
        love = getLoveHate(fields[i + 1]);
      } else if (fields[i].startsWith("color:red")) {
        hate = getLoveHate(fields[i + 1]);
      } else if (fields[i].startsWith("color:green")) {
        love = getLoveHate(fields[i + 1]);
      }
      if (!curTitle.equals("") && !curLink.equals("")) {
        MySeriesLogger.logger.log(Level.FINE, "Subtitle found {0}", curTitle);
        subs.add(new Subtitle(curTitle, new URL(curLink), love, hate, language));
        curTitle = "";
        curLink = "";
        love = 0;
        hate = 0;
      }
    }
  }

  private int getLoveHate(String str) {
    String s = "";
    char[] c = str.toCharArray();
    for (int i = 0; i < c.length; i++) {

      if (MyUsefulFunctions.isNumeric(String.valueOf(c[i]))) {
        s += c[i];
      } else {
        if (MyUsefulFunctions.isNumeric(s)) {
          return Integer.parseInt(s);
        }
        return 0;
      }

    }
    if (MyUsefulFunctions.isNumeric(s)) {
      return Integer.parseInt(s);
    }
    return 0;


  }
}
