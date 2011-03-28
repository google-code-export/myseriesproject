/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import myComponents.MyUsefulFunctions;

/**
 *
 * @author ssoldatos
 */
public class Unziper {

  public String directory;
  public ArrayList<String> unzippedFiles = new ArrayList<String>();
  private final File file;
  private final boolean delete;
  private final int type;

  public static final int NONE = -1;
  public static final int ALL = 0;
  public static final int SUBTITLES = 1;

  public Unziper(String directory, File file, boolean delete, int type) {
    this.directory = directory;
    this.file = file;
    this.delete = delete;
    this.type = type;
  }

  public boolean unzip() throws Exception {
    try {
      byte[] buf = new byte[1024];
      ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
      ZipEntry zipEntry = zipInputStream.getNextEntry();
      while (zipEntry != null) {
        //for each entry to be extracted
        String entryName = zipEntry.getName();
        if (shouldUnzip(entryName)) {
          int n;
          FileOutputStream fileoutputstream;
          File newFile = new File(entryName);
          String zipdirectory = newFile.getParent();
          if (zipdirectory == null) {
            if (newFile.isDirectory()) {
              break;
            }
          }
          fileoutputstream = new FileOutputStream(directory + "/" + entryName);
          while ((n = zipInputStream.read(buf, 0, 1024)) > -1) {
            fileoutputstream.write(buf, 0, n);
          }
          fileoutputstream.close();
          zipInputStream.closeEntry();
          unzippedFiles.add(entryName);
        }
        zipEntry = zipInputStream.getNextEntry();
      }//while
      zipInputStream.close();
      if (delete && !unzippedFiles.isEmpty()) {
        file.delete();
      }
    } catch (Exception ex) {
      throw new Exception(ex);
    }
    return true;
  }

  private boolean shouldUnzip(String entryName){
    switch (type){
      case NONE:return false;
      case ALL:return true;
      case SUBTITLES:return MyUsefulFunctions.isSubtitle(entryName);
    }
    return false;
  }
}
