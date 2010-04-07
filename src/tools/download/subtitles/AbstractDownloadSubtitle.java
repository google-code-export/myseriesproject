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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JProgressBar;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;

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

  protected void download(Subtitle sub) {
    if (localDir.equals("")) {
      progress.setIndeterminate(false);
      MyMessages.error("No local dir", "Local dir for series is not provided.Opening browser");
      openInBrowser(sub);
    } else {
      //System.out.println(sub.url);
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
        if (is.available() > 200) {
          while ((ByteRead = is.read(buf)) > -1) {
            outStream.write(buf, 0, ByteRead);
            ByteWritten += ByteRead;
          }
        } else {
          is.close();
          outStream.close();
          MyMessages.error("Access denied", "Direct access to subtitle is denied.Opening browser");
          openInBrowser(sub);
        }
        is.close();
        outStream.close();
        progress.setString("Opening zip File");
        openZip(filename);
      } catch (IOException ex) {
        MyMessages.error("Access denied", "Direct access to subtitle is denied.Opening browser");
        openInBrowser(sub);
      }
    }
  }

  private void openInBrowser(Subtitle sub) {
    try {
      Desktop.getDesktop().browse(new URI(sub.url.toString()));
      form.dispose();
    } catch (IOException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
      MyMessages.error("Error occured!!!", "Could not read input stream");
      form.dispose();
    } catch (URISyntaxException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
      MyMessages.error("Error occured!!!", "Wrong url");
      form.dispose();
    } finally {
      myseries.MySeries.glassPane.deactivate();
    }
  }

  private void openZip(String filename) throws IOException {
    try {
      byte[] buf = new byte[1024];
      ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(filename));
      ZipEntry zipEntry = zipInputStream.getNextEntry();
      while (zipEntry != null) {
        //for each entry to be extracted
        String entryName = zipEntry.getName();
        if (entryName.endsWith(".srt")) {
          int n;
          FileOutputStream fileoutputstream;
          File newFile = new File(entryName);
          String directory = newFile.getParent();
          if (directory == null) {
            if (newFile.isDirectory()) {
              break;
            }
          }
          fileoutputstream = new FileOutputStream(getLocalDir() + "/" + entryName);
          while ((n = zipInputStream.read(buf, 0, 1024)) > -1) {
            fileoutputstream.write(buf, 0, n);
          }
          fileoutputstream.close();
          zipInputStream.closeEntry();
          srtFilename = entryName;
          form.label_message.setText("Subtitle downloaded and extracted");
        }
        zipEntry = zipInputStream.getNextEntry();
      }//while
      zipInputStream.close();
      new File(filename).delete();
    } catch (Exception ex) {
      myseries.MySeries.logger.log(Level.WARNING, "Could not extract srt file", ex);
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
