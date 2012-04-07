/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.files;

import database.SeriesRecord;
import java.io.File;
import java.util.logging.Level;
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
      long time= 0;
      if (this.file.renameTo(new File(this.ser.getLocalDir() + "/" + this.file.getName()))) {
        time =  System.currentTimeMillis() - start;
        MySeriesLogger.logger.log(Level.INFO, "Moved file {0} to local folder in {1} ms", new Object[]{this.file.getName(), time});
        System.out.println("File:" + this.file.getName() + " in " + time);
        if (MyUsefulFunctions.isSubtitle(this.file) &&  MySeries.options.getBooleanOption(MySeriesOptions.AUTO_RENAME_SUBS)) {
          MyUsefulFunctions.renameEpisode(this.ser, this.file.getName());
          return true;
        }
      } else {
        MySeriesLogger.logger.log(Level.WARNING, "Could not move file {0}", this.file.getName());
      }
      return false;
    }
  }
