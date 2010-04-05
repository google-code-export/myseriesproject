/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.subtitles.sonline;

import java.io.FileNotFoundException;
import tools.download.subtitles.tvsubtitles.*;
import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import tools.download.subtitles.AbstractDownloadSubtitle;
import tools.download.subtitles.Subtitle;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class DownloadSOnline extends AbstractDownloadSubtitle implements Runnable {

  private String sOnlineCode;
  
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
      MyMessages.message("Subtitle not found", "The subtitle was not found");
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
  }

  public void getSubtitle() throws FileNotFoundException, IOException {
    if (MyUsefulFunctions.hasInternetConnection()) {
      URL url = new URL(Options._SUBTITLE_ONLINE_URL_+sOnlineCode+"-season-" + season + "-episode-" + episode + "-subtitles.html");
      BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
      parseWebpage(in);
    } else {
      MyMessages.internetError();
      form.dispose();
      
    }
  }

  private void parseWebpage(BufferedReader in) throws IOException {
    String line = "";
    URL curLink = null;
    String curtitle = "";
    String search = "<a href=\"/" + sOnlineCode + "-s" + season + "e" + episode + "-greek-subtitles-download";
    while ((line = in.readLine()) != null) {
      if (line.indexOf(search.toLowerCase()) > -1) {
        line = line.replaceAll("(<a href=\"/)|(\">)", "").trim();
        String[] tokens = line.split("-");
        String code = tokens[tokens.length-1].replaceAll(".html", "");
        String url = Options._SUBTITLE_ONLINE_URL_+"download-subtitle-"+code+".html";
        curLink = new URL(url);
        line = in.readLine();
        curtitle = line.replaceAll("</a>", "");
        subs.add(new Subtitle(curtitle, curLink));
      }
    }
  }

}
