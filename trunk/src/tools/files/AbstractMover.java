/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.files;

import database.SeriesRecord;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import myseriesproject.MySeries;
import tools.MySeriesLogger;
import tools.options.MySeriesOptions;

/**
 *
 * @author lordovol
 */
public abstract class AbstractMover {

  protected ArrayList<SeriesRecord> series;
  
  protected FilenameFilter filter;

  public AbstractMover(ArrayList<SeriesRecord> series) {
    this.series = series;

  }

  public void move() {
    for (Iterator<SeriesRecord> it = series.iterator(); it.hasNext();) {
      SeriesRecord ser = it.next();
      if (ser.isValidLocalDir()) {
        MySeriesLogger.logger.log(Level.INFO, "Moving files of series {0}", ser.getFullTitle());
        ArrayList<File> files = getFiles(ser);
        for (Iterator<File> iter = files.iterator(); iter.hasNext();) {
          File sub = iter.next();
          move(sub, ser);
        }
      }
    }
  }

  protected boolean isValid(File file, SeriesRecord ser) {
    if (file.isFile()) {
      // Search for pattern " the big bang theory"
      String name = file.getName().replaceAll("[^A-Za-z0-9]", " ");
      String regex = ser.getTitle() + "\\D*0*" + ser.getSeason() + "\\D";
      // Search for pattern " thebigbangtheory"
      String regex2 = ser.getTitle().replaceAll(" ", "") + "\\D*0*" + ser.getSeason() + "\\D";
      Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
      Pattern pattern2 = Pattern.compile(regex2, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(name);
      Matcher matcher2 = pattern2.matcher(name);
      if (matcher.find() || matcher2.find()) {
        return true;
      }
    }
    return false;
  }

  private void move(File file, SeriesRecord ser) {
    FileMover m = new FileMover(file, ser);
    Thread t = new Thread(m);
    t.start();
  }
  
  protected ArrayList<File> getFiles(SeriesRecord ser) {
    ArrayList<File> files = new ArrayList<File>();
    File mainDir = new File(MySeries.options.getStringOption(MySeriesOptions.MAIN_DIRECTORY));
    File[] allFiles = mainDir.listFiles(filter);
    if (allFiles != null) {
      for (int i = 0; i < allFiles.length; i++) {
        File file = allFiles[i];
        if (isValid(file, ser)) {
          MySeriesLogger.logger.log(Level.FINE, "Found file {0}", file);
          files.add(file);
        }
      }
    }

    return files;
  }
}
