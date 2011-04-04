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
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import myComponents.MyUsefulFunctions;
import myComponents.myFileFilters.SubtitlesFilter;
import tools.options.Options;

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
        ArrayList<File> subs = getSubtitles(ser);
        for (Iterator<File> it1 = subs.iterator(); it1.hasNext();) {
          File sub = it1.next();
          if(move(sub,ser.getLocalDir())){

          }
        }
      }
    }
  }

  private ArrayList<File> getSubtitles(SeriesRecord ser) {
    ArrayList<File> subs = new ArrayList<File>();
    File mainDir = new File(Options.toString(Options.MAIN_DIRECTORY));
    File[] allSubs = mainDir.listFiles(new SubtitlesFilter());
    for (int i = 0; i < allSubs.length; i++) {
      File file = allSubs[i];
      if (isValid(file, ser)) {
        subs.add(file);
      }
    }

    return subs;
  }

  private boolean isValid(File file, SeriesRecord ser) {
    if (file.isFile()) {
      String name = file.getName();
      String regex = ser.getTitle() + "\\D*0*" + ser.getSeason() + "\\D";
      Pattern pattern = Pattern.compile(regex);
       Matcher matcher = pattern.matcher(name);
       if (matcher.find()){
          return true;
       }
    }
    return false;
  }

  private boolean move(File sub, String localDir) {
    try {
      if (MyUsefulFunctions.copyfile(sub.getCanonicalPath(), localDir + "/" + sub.getName())) {
        myseries.MySeries.logger.log(Level.INFO, "Move subtitle {0} to local folder", sub.getName());
        sub.delete();
        return true;
      } else {
        myseries.MySeries.logger.log(Level.WARNING, "Could not move subtitle {0}", sub.getName());
      }
    } catch (FileNotFoundException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    }
    return false;
  }

}
