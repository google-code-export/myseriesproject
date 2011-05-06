/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.archive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import net.sf.sevenzipjbinding.ExtractAskMode;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IArchiveExtractCallback;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.ISevenZipInArchive;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

/**
 *
 * @author ssoldatos
 */
class SevenZipFile extends AbstractArchiveFile implements ArchiveConstants {

 
  SevenZipFile(File file) {
    this.archivedFile = file;
  }

  @Override
  void unzip(String directory, int type) throws Exception {
     try {
      //SevenZip.initSevenZipFromPlatformJAR(ext, file);
      ISevenZipInArchive inArchive = null;
      RandomAccessFile randomAccessFile = null;
      randomAccessFile = new RandomAccessFile(archivedFile, "r");
      inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));
      inArchive.extract(null, false, new SevenZipExtractCallback(inArchive, this, directory, type));
      if (inArchive != null) {
        inArchive.close();
      }
      if (randomAccessFile != null) {
        randomAccessFile.close();
      }
      result = true;
    } catch (Exception ex) {
      throw ex;
    }
  }

  @Override
  ArrayList<String> getEntries(int type) {
    ArrayList<String> entries = new ArrayList<String>();
    try {
      //SevenZip.initSevenZipFromPlatformJAR(ext, file);
      ISevenZipInArchive inArchive = null;
      RandomAccessFile randomAccessFile = null;
      randomAccessFile = new RandomAccessFile(archivedFile, "r");
      inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));
      int i = inArchive.getNumberOfItems();
      for (int j = 0; j < i; j++) {
        String name = inArchive.getStringProperty(j, PropID.getPropIDByIndex(3));
        if(isValidType(name, type)){
          entries.add(name);
        }
      }
      if (inArchive != null) {
        inArchive.close();
      }
      if (randomAccessFile != null) {
        randomAccessFile.close();
      }
      return entries;
    } catch (Exception ex) {
      return null;
    }
  }

 
}
