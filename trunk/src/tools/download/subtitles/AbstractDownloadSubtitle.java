/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.subtitles;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import tools.MySeriesLogger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JProgressBar;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myseries.series.Series;
import tools.Unziper;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public abstract class AbstractDownloadSubtitle {

  protected int season;
  protected int episode;
  protected String localDir;
  public ArrayList<Subtitle> subs = new ArrayList<Subtitle>();
  protected JProgressBar progress;
  protected AbstractDownloadForm form;
  protected String srtFilename = "";
  protected boolean cancel = false;

  protected void download(Subtitle sub) {
    if (localDir.equals("")) {
      progress.setIndeterminate(false);
      MySeriesLogger.logger.log(Level.INFO, "No local dir for series, opening browser");
      MyMessages.error("No local dir", "Local dir for series is not provided.Opening browser");
      openInBrowser(sub);
    } else {
      //System.out.println(sub.url);
      MySeriesLogger.logger.log(Level.INFO, "Downloading subtitle to local dir");
      progress.setIndeterminate(true);
      progress.setString("Downloading subtitles");
      form.label_message.setText("Downloading Started");
      InputStream is = null;
      BufferedOutputStream outStream = null;
      try {
        byte[] buf;
        HttpURLConnection uCon = (HttpURLConnection) sub.url.openConnection();
        int resp = uCon.getResponseCode();
        String header = uCon.getHeaderField(0);
        if (!header.equals("HTTP/1.1 200 OK")) {
          MySeriesLogger.logger.log(Level.INFO, "Direct access to subtitle is denied.Opening browser");
          MyMessages.error("Access denied", "Direct access to subtitle is denied.Opening browser");
          openInBrowser(sub);
        } else {
          MySeriesLogger.logger.log(Level.FINE, "Access is granted , downloading the subtitle");
          is = uCon.getInputStream();
          buf = new byte[1024];
          int ByteRead, ByteWritten = 0;
          String filename = localDir + "/s" + season + "x" + episode + "_" + MyUsefulFunctions.createRandomString(8) + ".zip";
          outStream = new BufferedOutputStream(new FileOutputStream(filename));
          while ((ByteRead = is.read(buf)) > -1) {
            outStream.write(buf, 0, ByteRead);
            ByteWritten += ByteRead;
          }
          is.close();
          outStream.close();
          progress.setString("Opening zip File");
          Unziper z = new Unziper(getLocalDir(), new File(filename), true, Unziper.SUBTITLES);
          try {
            if (z.unzip()) {
              for (Iterator<String> it = z.unzippedFiles.iterator(); it.hasNext();) {
                String name = it.next();
                if (Options.toBoolean(Options.AUTO_RENAME_SUBS) && MyUsefulFunctions.renameEpisode(Series.getCurrentSerial(), name)) {
                  MySeriesLogger.logger.log(Level.INFO, "Subtitle renamed");
                }
              }
            }

            MySeriesLogger.logger.log(Level.FINE, "Subtitle downloaded and extracted");
            form.label_message.setText("Subtitle downloaded and extracted");
          } catch (Exception ex) {
            MySeriesLogger.logger.log(Level.WARNING, "Could not extract subtitle file", ex);
          }
          //openZip(filename);
        }
      } catch (IOException ex) {
        MySeriesLogger.logger.log(Level.INFO, "Direct access to subtitle is denied.Opening browser");
        MyMessages.error("Access denied", "Direct access to subtitle is denied.Opening browser");
        openInBrowser(sub);
      }
    }
  }

  private void openInBrowser(Subtitle sub) {
    try {
      MyUsefulFunctions.browse(sub.url.toString());
      form.dispose();
    } finally {
      myseries.MySeries.glassPane.deactivate();
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
