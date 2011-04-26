/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.misc.housekeeping;

import java.io.File;

/**
 *
 * @author ssoldatos
 */
public class HouseKeepFile {
  public static final int TORRENT = 0;
  public static final int LOG = 1;
  public static final int SCREENSHOT = 2;
  public int type;
  public File file;

  public HouseKeepFile(int type, File file) {
    this.type = type;
    this.file = file;
  }
  
  


}
