/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
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
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import myComponents.MyUsefulFunctions;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class DownloadSubtitles implements Runnable {

  private final String link;
  private final int season;
  private final int episode;
  private String localDir;
  public ArrayList<Subtitle> subs = new ArrayList<Subtitle>();
  private final JProgressBar progress;
  private final DownloadSubtitlesForm form;

  public DownloadSubtitles(String link, int season, int episode, DownloadSubtitlesForm form) {
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
      MyUsefulFunctions.message("Subtitle not found", "The subtitle was not found");
    } else if (subs.size() == 1) {
      download(subs.get(0));
       form.dispose();
    } else {
      Subtitle sub = (Subtitle) JOptionPane.showInputDialog(null, "Choose the subtitle to download", "Choose subtitle", JOptionPane.QUESTION_MESSAGE, null, subs.toArray(), 0);
      if (sub != null) {
        String newPath = sub.url.getPath().replace("subtitle", "download");
        try {
          sub.url = new URL("http://www.tvsubtitles.net"  + newPath);
        } catch (MalformedURLException ex) {
          myseries.MySeries.logger.log(Level.SEVERE, null, ex);
        }
        download(sub);
         form.dispose();
      }
    }
  }

  public void getSubtitle() {
    try {
      if (MyUsefulFunctions.hasInternetConnection()) {
        String buff = parseWebPage();
        if (!buff.equals("")) {
          String subsLink = getLink(buff);
          if (subsLink != null) {
            getDownloadLinks(subsLink);
          } else {
             form.dispose();
          }
        }else {
           form.dispose();
        }
      } else {
         form.dispose();
      }
    } catch (MalformedURLException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
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

  private String getLink(String buff) {
    String lang = Options.toString(Options.PRIMARY_SUB).equals("Greek") ? "gr" : "en";
    int pos = buff.indexOf("<img src=\"images/flags/"+lang+".gif\"");
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
    return subLink;
  }

  private void getDownloadLinks(String subsLink) throws MalformedURLException, IOException {
    URL subsUrl = new URL("http://www.tvsubtitles.net/" + subsLink);
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
        curLink = "http://www.tvsubtitles.net/" + field;
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

  private void download(Subtitle sub) {
    if (localDir.equals("")) {
      try {
        try {
          progress.setIndeterminate(false);
          Desktop.getDesktop().browse(new URI(sub.url.toString()));
        } catch (IOException ex) {
          Logger.getLogger(DownloadSubtitles.class.getName()).log(Level.SEVERE, null, ex);
        }
      } catch (URISyntaxException ex) {
        Logger.getLogger(DownloadSubtitles.class.getName()).log(Level.SEVERE, null, ex);
      }
    } else {
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
        String filename = localDir +"/s"+season+"x"+episode+".zip";
        outStream = new BufferedOutputStream(new FileOutputStream(filename));
        while ((ByteRead = is.read(buf)) != -1) {
          outStream.write(buf, 0, ByteRead);
          ByteWritten += ByteRead;
        }
        is.close();
        outStream.close();
        progress.setString("Opening zip File");
        ZipFile zip = new ZipFile(filename);
        if(zip.entries().hasMoreElements()){
          ZipEntry el = zip.entries().nextElement();
          if(el.getName().endsWith(".srt")){
            form.label_message.setText(el.getName());
            MyUsefulFunctions.message("Subtitle downloaded", "The subtitle \n"+ el.getName()+ "\n is downloaded");
          }
        }
        
      } catch (IOException ex) {

        try {
          if(ex.getMessage().indexOf("code: 403") > -1){
            Desktop.getDesktop().browse(new URI(sub.url.toString()));
          }
        } catch (URISyntaxException ex1) {
          Logger.getLogger(DownloadSubtitles.class.getName()).log(Level.SEVERE, null, ex1);
        } catch(IOException ex1){
          Logger.getLogger(DownloadSubtitles.class.getName()).log(Level.SEVERE, null, ex1);
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
}
