/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.files;

import database.SeriesRecord;
import java.io.File;
import java.util.logging.Level;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myseriesproject.MySeries;
import tools.MySeriesLogger;
import tools.options.MySeriesOptions;

/**
 *
 * @author lordovol
 */
public class FileMover implements Runnable {

  protected File file;
  protected SeriesRecord ser;

  public FileMover(File file, SeriesRecord ser) {
    this.file = file;
    this.ser = ser;

  }

  public void run() {
    move();
  }

  private boolean move() {
    MySeriesLogger.logger.log(Level.INFO, "Move file {0} to local folder", this.file.getName());
    long start = System.currentTimeMillis();
    long time = 0;
    if (!this.file.canWrite()) {
      MyMessages.warning("Cannot move file", "Cannot move file " + this.file.getName() + ". It is probably used by another application (torrent client?)", true);
      MySeriesLogger.logger.log(Level.WARNING, "Cannot move file {0}. It is probably used by another application (torrent client?)", this.file.getName());
      return false;
    }
    if (this.file.renameTo(new File(this.ser.getLocalDir() + "/" + this.file.getName()))) {
      time = System.currentTimeMillis() - start;
      MySeriesLogger.logger.log(Level.INFO, "Moved file {0} to local folder in {1} ms", new Object[]{this.file.getName(), time});
      if (MyUsefulFunctions.isSubtitle(this.file) && MySeries.options.getBooleanOption(MySeriesOptions.AUTO_RENAME_SUBS)) {
        MyUsefulFunctions.renameEpisode(this.ser, this.file.getName());
        return true;
      }
    } else {
      MySeriesLogger.logger.log(Level.WARNING, "Could not move file {0}", this.file.getName());
    }
    return false;
  }
}
