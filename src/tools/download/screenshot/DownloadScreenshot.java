/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.screenshot;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import tools.internetUpdate.InternetUpdate;
import tools.MySeriesLogger;
import tools.options.MySeriesOptions;
import tools.options.Paths;

/**
 *
 * @author lordovol
 */
public class DownloadScreenshot {

  private final int tvRageID;
  private String filename;
  private boolean success = false;
  private final String series;
  public String message = "";

  public DownloadScreenshot(int tvRageID, String series) {
    MySeriesLogger.logger.log(Level.INFO, "Downloading screenshot for series {0} with tvrage id {1}", new Object[]{series, tvRageID});
    this.tvRageID = tvRageID;
    this.series = series;
    if (MyUsefulFunctions.hasInternetConnection(InternetUpdate.TV_RAGE_URL)) {
      get();
    } else {
      MyMessages.internetError();
    }
  }

  private void get() {
    InputStream in;
    URLConnection con;
    int i = (tvRageID / 1000) + 1;

    try {
      String url = InternetUpdate.TV_RAGE_IMAGES_URL + "shows/" + i + "/" + tvRageID + ".jpg";
      URL u = new URL(url);
      con = u.openConnection();
      if (con.getHeaderField("Content-type").equals("image/jpeg")) {
        in = u.openStream();
        download(in);
      } else {
        this.message = "Could not find a screenshot for series " + series;
      }
    } catch (IOException ex) {
      this.message = "Could not find a screenshot for series " + series;
      MySeriesLogger.logger.log(Level.SEVERE, "I/O exception occured", ex);
    }

  }

  private void download(InputStream in) {
    try {
      byte[] buf = new byte[1024];
      int ByteRead;
      int ByteWritten = 0;
      setFilename(MySeriesOptions._USER_DIR_ + Paths.SCREENSHOTS_PATH + tvRageID + ".jpg");
      BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(getFilename()));
      while ((ByteRead = in.read(buf)) > -1) {
        outStream.write(buf, 0, ByteRead);
        ByteWritten += ByteRead;
      }
      in.close();
      outStream.close();
      if (checkImage()) {
        setSuccess(true);
        MySeriesLogger.logger.log(Level.FINE, "Screenshot downloaded");
      } else {
        new File(filename).delete();
        this.message = "Screenshot for series " + series + " was not a valid jpg file";
        MySeriesLogger.logger.log(Level.WARNING, "Screenshot downloaded but was not a valid jpg");
      }
      
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not read from input stream", ex);
    }
  }

  private boolean checkImage() {
    ImageIcon im = new ImageIcon(filename);
    return im.getIconHeight() > -1 && im.getIconWidth() > -1;
  }

  /**
   * @return the filename
   */
  public String getFilename() {
    return filename;
  }

  /**
   * @param filename the filename to set
   */
  public void setFilename(String filename) {
    this.filename = filename;
  }

  /**
   * @return the success
   */
  public boolean isSuccess() {
    return success;
  }

  /**
   * @param success the success to set
   */
  public void setSuccess(boolean success) {
    this.success = success;
  }
}
