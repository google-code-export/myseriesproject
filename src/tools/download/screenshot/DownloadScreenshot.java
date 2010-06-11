/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.screenshot;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import myComponents.myGUI.MyImagePanel;
import tools.internetUpdate.InternetUpdate;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class DownloadScreenshot {

  private final int tvRageID;
  private String filename;
  private boolean success;

  public DownloadScreenshot(int tvRageID) {
    this.tvRageID = tvRageID;
    get();
    
  }

  private void get() {
    InputStream in;
    URLConnection con;

    {
      try {
        for (int i = 0; i < 20; i++) {
          String url = InternetUpdate.TV_RAGE_IMAGES_URL + "shows/" + i + "/" + tvRageID + ".jpg";
          URL u = new URL(url);
          con = u.openConnection();
          if (con.getHeaderField("Content-type").equals("image/jpeg")) {
            in = u.openStream();
            download(in);
            return;
          }


        }
      } catch (IOException ex) {
        myseries.MySeries.logger.log(Level.SEVERE, null, ex);
      }
    }
  }

  private void download(InputStream in) {
    try {
      byte[] buf = new byte[1024];
      int ByteRead;
      int ByteWritten = 0;
      setFilename(Options._USER_DIR_ + MyImagePanel.SCREENSHOTS_PATH + tvRageID + ".jpg");
      BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(getFilename()));
      while ((ByteRead = in.read(buf)) > -1) {
        outStream.write(buf, 0, ByteRead);
        ByteWritten += ByteRead;
      }
      in.close();
      outStream.close();
      setSuccess(true);
    } catch (IOException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    } 
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
