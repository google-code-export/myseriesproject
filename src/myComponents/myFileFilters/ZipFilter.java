/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myFileFilters;

import java.io.File;
import java.io.FilenameFilter;
import myComponents.MyUsefulFunctions;
import tools.download.subtitles.SubtitleConstants;

/**
 * The subtitles filter
 * @author lordovol
 */
public class ZipFilter implements FilenameFilter, SubtitleConstants {


  @Override
  public boolean accept(File dir, String name) {
    String ext = MyUsefulFunctions.getExtension(name);
    return MyUsefulFunctions.isInArray(ZIP_EXT, ext.toLowerCase());
  }
}
