/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.subtitles;

import database.SeriesRecord;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import tools.MySeriesLogger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import myComponents.myFileFilters.SubtitlesFilter;
import myComponents.MyUsefulFunctions;
import myseriesproject.MySeries;
import tools.options.MySeriesOptions;

/**
 *
 * @author ssoldatos
 */
public class SubtitleMover {

  private final ArrayList<SeriesRecord> series;

  public SubtitleMover(ArrayList<SeriesRecord> series) {
    this.series = series;
  }

  public void move() {
    for (Iterator<SeriesRecord> it = series.iterator(); it.hasNext();) {
      SeriesRecord ser = it.next();
      if (ser.isValidLocalDir()) {
        MySeriesLogger.logger.log(Level.INFO, "Moving subtitles of series {0}", ser.getFullTitle());
        ArrayList<File> subs = getSubtitles(ser);
        for (Iterator<File> it1 = subs.iterator(); it1.hasNext();) {
          File sub = it1.next();
          if (move(sub, ser.getLocalDir()) && MySeries.options.getBooleanOption(MySeriesOptions.AUTO_RENAME_SUBS)) {
            MyUsefulFunctions.renameEpisode(ser, sub.getName());
          }
        }
      }
    }
  }

  private ArrayList<File> getSubtitles(SeriesRecord ser) {
    ArrayList<File> subs = new ArrayList<File>();
    File mainDir = new File(MySeries.options.getStringOption(MySeriesOptions.MAIN_DIRECTORY));
    File[] allSubs = mainDir.listFiles(new SubtitlesFilter());
    if (allSubs != null) {
      for (int i = 0; i < allSubs.length; i++) {
        File file = allSubs[i];
        if (isValid(file, ser)) {
          MySeriesLogger.logger.log(Level.FINE, "Found subtitle {0}", file);
          subs.add(file);
        }
      }
    }

    return subs;
  }

  private boolean isValid(File file, SeriesRecord ser) {
    if (file.isFile()) {
      String name = file.getName().replaceAll("[^A-Za-z0-9]", " ");
      String regex = ser.getTitle() + "\\D*0*" + ser.getSeason() + "\\D";
      Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(name);
      if (matcher.find()) {
        return true;
      }
    }
    return false;
  }

  private boolean move(File sub, String localDir) {
    try {
      MySeriesLogger.logger.log(Level.INFO, "Move subtitle {0} to local folder", sub.getName());
      if (MyUsefulFunctions.copyfile(sub.getCanonicalPath(), localDir + "/" + sub.getName())) {
        MySeriesLogger.logger.log(Level.FINE, "Subtitle moved");
        MySeriesLogger.logger.log(Level.INFO, "Deleting original file");
        sub.delete();
        MySeriesLogger.logger.log(Level.FINE, "File deleted");
        return true;
      } else {
        MySeriesLogger.logger.log(Level.INFO, "Could not move subtitle {0}", sub.getName());
      }
    } catch (FileNotFoundException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "File is not found", ex);
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not read from file", ex);
    }
    return false;
  }
}
