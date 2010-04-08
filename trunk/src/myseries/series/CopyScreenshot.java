/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.series;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myComponents.myGUI.Screenshot;
import tools.options.Options;
import myseries.MySeries;

/**
 *
 * @author lordovol
 */
public class CopyScreenshot implements Runnable {

  private final String screenshot;
  private final String destination;

  CopyScreenshot(String screenshot) {
    this.screenshot = screenshot;
    destination = Options._USER_DIR_ + "/" + Screenshot.PATH;
  }

  public void run() {
    try {
      start();
    } catch (FileNotFoundException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
      MyMessages.error("I/O error", "Screenshot could not be found");
    } catch (IOException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
      MyMessages.error("I/O error", "Screenshot could not be read or written");
    }
  }

  private void start() throws FileNotFoundException, IOException {
    File inputFile = new File(screenshot);
    File outputFile = new File(destination + inputFile.getName());
    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile), 4096);

    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile), 4096);

    int theChar;
    while ((theChar = bis.read()) != -1) {
      bos.write(theChar);
    }
    bos.close();
    bis.close();

  }
}
