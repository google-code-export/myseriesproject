/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.archive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
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
      inArchive.extract(null, false, new MyExtractCallback(inArchive, directory, type));
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
  
  public class MyExtractCallback implements IArchiveExtractCallback {

    private final ISevenZipInArchive inArchive;
    private final String extractPath;
    private int type;

    public MyExtractCallback(ISevenZipInArchive inArchive, String extractPath, int type) {
      this.inArchive = inArchive;
      this.extractPath = extractPath;
      this.type = type;
    }

    @Override
    public ISequentialOutStream getStream(final int index, ExtractAskMode extractAskMode) throws SevenZipException {
      return new ISequentialOutStream() {

        @Override
        public int write(byte[] data) throws SevenZipException {
          String filePath = inArchive.getStringProperty(index, PropID.PATH);

          FileOutputStream fos = null;
          try {
            if (shouldUnzip(filePath, type)) {
              fos = new FileOutputStream(extractPath + "/" + filePath, true);
              fos.write(data);
              extractedFiles.add(filePath);
            }
          } catch (IOException e) {
          } finally {
            try {
              if (fos != null) {
                fos.flush();
                fos.close();
              }
            } catch (IOException e) {
            }
          }
          return data.length;
        }
      };
    }

    @Override
    public void prepareOperation(ExtractAskMode extractAskMode) throws SevenZipException {
    }

    @Override
    public void setOperationResult(ExtractOperationResult extractOperationResult) throws SevenZipException {
    }

    @Override
    public void setCompleted(long completeValue) throws SevenZipException {
    }

    @Override
    public void setTotal(long total) throws SevenZipException {
    }
  }
}
