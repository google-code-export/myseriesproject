/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myFileFilters;

import java.io.File;
import java.io.FilenameFilter;

/**
 * The video filter
 * @author lordovol
 */
public class VideoFilter implements FilenameFilter {
  private boolean acceptDirs = true;

  public VideoFilter(boolean dirs) {
    this.acceptDirs = dirs;
    
  }
    

  public static final String[] EXTENSIONS = {"avi", "mkv","mpg","mp4","wmv"};


  public boolean accept(File dir, String name) {
    if(new File(dir + "/" + name).isDirectory()){
      return acceptDirs;
    }
    for (int i = 0; i < EXTENSIONS.length; i++) {
      String ext = EXTENSIONS[i];
      if (name.toLowerCase().endsWith(ext)) {
        return true;
      }
    }
    return false;
  }
}
