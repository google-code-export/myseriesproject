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
import tools.download.subtitles.Subtitle;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class DownloadSOnline implements Runnable {

  private final String sOnlineCode;
  private final int season;
  private final int episode;
  private String localDir;
  public ArrayList<Subtitle> subs = new ArrayList<Subtitle>();
  private final JProgressBar progress;
  private final SOnlineForm form;

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
    } else {
      //MyMessages.internetError();
      //form.dispose();
      File f = new File(Options._SUBTITLE_ONLINE_URL_+sOnlineCode+"-season-" + season + "-episode-" + episode + "-subtitles.html");
      BufferedReader in = MyUsefulFunctions.createInputStream(f);
      parseWebpage(in);
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

  private void download(Subtitle sub) {
    if (localDir.equals("")) {
      try {
        try {
          progress.setIndeterminate(false);
          MyMessages.error("No local dir", "Local dir for series is not provided.Opening browser");
          Desktop.getDesktop().browse(new URI(sub.url.toString()));
        } catch (IOException ex) {
          myseries.MySeries.logger.log(Level.SEVERE, null, ex);
          MyMessages.error("Error occured!!!", "Could not read input stream");
          form.dispose();
        }
      } catch (URISyntaxException ex) {
        myseries.MySeries.logger.log(Level.SEVERE, null, ex);
        MyMessages.error("Error occured!!!", "Wrong url");
        form.dispose();
      }
    } else {
      System.out.println(sub.url);
      progress.setIndeterminate(true);
      progress.setString("Downloading subtitles");
      form.label_message.setText("Downloading file from " + sub.url);
      InputStream is = null;
      BufferedOutputStream outStream = null;
      try {
        byte[] buf;
        URLConnection uCon = sub.url.openConnection();
        is = uCon.getInputStream();
        buf = new byte[1024];
        int ByteRead, ByteWritten = 0;
        String filename = localDir + "/s" + season + "x" + episode + "_" + MyUsefulFunctions.createRandomString(8) + ".zip";
        outStream = new BufferedOutputStream(new FileOutputStream(filename));
        while ((ByteRead = is.read(buf)) != -1) {
          outStream.write(buf, 0, ByteRead);
          ByteWritten += ByteRead;
        }
        is.close();
        outStream.close();
        progress.setString("Opening zip File");
        openZip(filename);
      } catch (IOException ex) {
        try {
          if (ex.getMessage().indexOf("code: 403") > -1) {
            MyMessages.error("Access denied", "Direct access to subtitle is denied.Opening browser");
            Desktop.getDesktop().browse(new URI(sub.url.toString()));
          }
        } catch (URISyntaxException ex1) {
          myseries.MySeries.logger.log(Level.SEVERE, null, ex1);
          MyMessages.error("Error occured!!!", "Wrong url");
          form.dispose();
        } catch (IOException ex1) {
          myseries.MySeries.logger.log(Level.SEVERE, null, ex1);
          MyMessages.error("Error occured!!!", "Could not read input stream");
          form.dispose();
        }
      }
    }
  }

  /**
   * @return the localDir
   */
  public String getLocalDir() {
    return localDir;
  }

  /**
   * @param localDir the localDir to set
   */
  public void setLocalDir(String localDir) {
    this.localDir = localDir;
  }

  private void openZip(String filename) throws IOException {
    ZipFile zip = new ZipFile(filename);
    ZipInputStream zipinputstream = new ZipInputStream(new FileInputStream(filename));
    byte[] buf = new byte[1024];
    if (zip.entries().hasMoreElements()) {
      ZipEntry el = zip.entries().nextElement();
      if (el.getName().endsWith(".srt")) {
        int n;
        FileOutputStream fileoutputstream;
        File newFile = new File(el.getName());
        String directory = newFile.getParent();
        fileoutputstream = new FileOutputStream(localDir + "/" + el.getName());
        while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
          fileoutputstream.write(buf, 0, n);
        }
        fileoutputstream.close();
        zipinputstream.closeEntry();
      }
      form.label_message.setText(el.getName());
      MyMessages.message("Subtitle downloaded", "The subtitle \n" + el.getName() + "\n is downloaded");
    }
    zipinputstream.close();
    zip.close();
    new File(filename).delete();
  }
}
