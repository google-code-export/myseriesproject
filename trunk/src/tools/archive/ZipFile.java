/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author ssoldatos
 */
class ZipFile extends AbstractArchiveFile implements ArchiveConstants {

  ZipFile(File file) {
    this.archivedFile = file;
  }

  @Override
  void unzip(String directory, int type) throws Exception {
    try {
      byte[] buf = new byte[1024];
      ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(archivedFile));
      ZipEntry zipEntry = zipInputStream.getNextEntry();
      while (zipEntry != null) {
        //for each entry to be extracted
        String entryName = zipEntry.getName();
        if (isValidType(entryName, type)) {
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
          extractedFiles.add(entryName);
        }
        zipEntry = zipInputStream.getNextEntry();
      }//while
      zipInputStream.close();
    } catch (Exception ex) {
      throw ex;
    }
    result = true;
  }

  @Override
  ArrayList<String> getEntries(int type) throws Exception {
    ArrayList<String> entries = new ArrayList<String>();
    ZipInputStream zipInputStream = null;
    try {
      byte[] buf = new byte[1024];
      zipInputStream = new ZipInputStream(new FileInputStream(archivedFile));
      ZipEntry zipEntry = zipInputStream.getNextEntry();
      while (zipEntry != null) {
        //for each entry to be extracted
        String entryName = zipEntry.getName();
        if(isValidType(entryName, type)){
          entries.add(entryName);
        }
        zipEntry = zipInputStream.getNextEntry();
      }
      return entries;
    } catch (Exception ex) {
      throw ex;
    } finally {
      zipInputStream.close();
    }
  }
}
