/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.archive;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author ssoldatos
 */
class RarFile extends AbstractArchiveFile implements ArchiveConstants {

  RarFile(File file) {
    this.archivedFile = file;
  }

  @Override
  void unzip(String directory, int type) throws Exception {
    try {
      checkHeader();
      Archive rarFile = new Archive(archivedFile);
      List<FileHeader> headers = rarFile.getFileHeaders();
      for (FileHeader fileHeader : headers) {
        String name = fileHeader.getFileNameString();
        if (isValidType(name, type)) {
          FileOutputStream os = new FileOutputStream(directory + "/" + name);
          rarFile.extractFile(fileHeader, os);
          extractedFiles.add(name);
          os.close();
        }
      }

      rarFile.close();

    } catch (Exception ex) {
      throw ex;
    }
    result = true;
  }

  @Override
  ArrayList<String> getEntries(int type) throws Exception {
    ArrayList<String> entries = new ArrayList<String>();
    Archive rarFile =null;
    try {
      checkHeader();
     rarFile = new Archive(archivedFile);
      List<FileHeader> headers = rarFile.getFileHeaders();
      for (FileHeader fileHeader : headers) {
        String name = fileHeader.getFileNameString();
        if (isValidType(name, type)){
        entries.add(name);
        }
      }
      return entries;
    } catch (Exception ex) {
      throw ex;
    } finally {
      rarFile.close();
    }
  }

  private void checkHeader() throws Exception {
    try{
    FileInputStream f = new FileInputStream(archivedFile);
      byte[] b = new byte[4];
      f.read(b);
      String rarHeader = new String(b);
      f.close();
      if (!"Rar!".equals(rarHeader)) {
        throw new IOException();
      }
    } catch(Exception ex){
      throw ex;
    }
  }
}
