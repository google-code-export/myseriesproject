/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myFileFilters;

import java.io.File;
import java.io.FilenameFilter;
import tools.download.subtitles.SubtitleConstants;

/**
 * The subtitles filter
 * @author lordovol
 */
public class SubtitlesFilter implements FilenameFilter, SubtitleConstants {


  public boolean accept(File dir, String name) {
    if(new File(dir + "/" + name).isDirectory()){
      return true;
    }
    for (int i = 0; i < EXTENSIONS.length; i++) {
      String ext = EXTENSIONS[i];
      if (name.endsWith(ext)) {
        return true;
      }
    }
    return false;
  }
}
