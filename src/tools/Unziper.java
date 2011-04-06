/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
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
  private String ext;
  public static final int NONE = -1;
  public static final int ALL = 0;
  public static final int SUBTITLES = 1;
  public static final String ZIP = "zip";
  public static final String RAR = "rar";

  public Unziper(String directory, File file, boolean delete, int type) {
    this.directory = directory;
    this.file = file;
    this.delete = delete;
    this.type = type;
    this.ext = MyUsefulFunctions.getExtension(file);
  }

  public boolean unzip() throws Exception {
    boolean res = ext.equals(ZIP) ? unzipZipFile() : unzipRarFile();
    if (delete && !unzippedFiles.isEmpty()) {
      if (file.delete()) {
        MySeriesLogger.logger.log(Level.INFO, "Original zipped file {0} deleted",file);
      } else {
        MySeriesLogger.logger.log(Level.WARNING, "Original zipped file {0} could not be deleted",file);
      }
    }
    return res;
  }

  private boolean unzipZipFile() throws Exception {
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

  private boolean unzipRarFile() {

    try {
      FileInputStream f = new FileInputStream(this.file);
      byte[] b = new byte[4];
      f.read(b);
      String rarHeader = new String(b);
      f.close();
      if (!"Rar!".equals(rarHeader)) {
        throw new IOException();
      }
      try {
        Archive rarFile = new Archive(file);
        List<FileHeader> headers = rarFile.getFileHeaders();
        for (FileHeader fileHeader : headers) {
          String name = fileHeader.getFileNameString();
          if (shouldUnzip(name)) {
            FileOutputStream os = new FileOutputStream(directory + "/" + name);
            rarFile.extractFile(fileHeader, os);
            unzippedFiles.add(name);
          }
        }

      } catch (Exception e) {
        throw new IOException();
      }
    } catch (Exception ex) {
    }
    return true;
  }

  private boolean shouldUnzip(String entryName) {
    switch (type) {
      case NONE:
        return false;
      case ALL:
        return true;
      case SUBTITLES:
        return MyUsefulFunctions.isSubtitle(entryName);
    }
    return false;
  }
}
