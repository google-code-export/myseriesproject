/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myComponents.myFileFilters;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Screenshot filter
 * @author lordovol
 */
public class ScreenshotFilter extends FileFilter {

  public static final String[] EXTENSIONS = {"jpg", "png", "gif", "bmp"};

  @Override
  public boolean accept(File f) {
    if(f.isDirectory()){
      return true;
    }
     for (int i = 0; i < EXTENSIONS.length; i++) {
      String ext = EXTENSIONS[i];
      if (f.getName().endsWith(ext)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String getDescription() {
    return "Image Files (*.jpg, *.png, *.gif, *.bmp)";
  }

}
