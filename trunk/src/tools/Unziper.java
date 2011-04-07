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
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import myComponents.MyUsefulFunctions;
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
  public static final String SEVEN_ZIP = "7z";

  public Unziper(String directory, File file, boolean delete, int type) {
    this.directory = directory;
    this.file = file;
    this.delete = delete;
    this.type = type;
    this.ext = MyUsefulFunctions.getExtension(file);
  }

  public boolean unzip() throws Exception {
    boolean res = false;
    if (ext.equals(ZIP)) {
      res = unzipZipFile();
    } else if (ext.equals(RAR)) {
      res = unzipRarFile();
    } else if (ext.equals(SEVEN_ZIP)) {
      res = unzipSevenZipFile();
    }

    if (delete && !unzippedFiles.isEmpty()) {
      if (file.delete()) {
        MySeriesLogger.logger.log(Level.INFO, "Original zipped file {0} deleted", file);
      } else {
        MySeriesLogger.logger.log(Level.WARNING, "Original zipped file {0} could not be deleted", file);
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
     
    } catch (Exception ex) {
      throw ex;
    }
    return true;
  }

  private boolean unzipRarFile() throws Exception {

    try {
      FileInputStream f = new FileInputStream(this.file);
      byte[] b = new byte[4];
      f.read(b);
      String rarHeader = new String(b);
      f.close();
      if (!"Rar!".equals(rarHeader)) {
        throw new IOException();
      }

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
      rarFile.close();

    } catch (Exception ex) {
      throw ex;
    }
    return true;
  }

  private boolean unzipSevenZipFile() throws Exception {
    try {
      //SevenZip.initSevenZipFromPlatformJAR(ext, file);
      ISevenZipInArchive inArchive = null;
      RandomAccessFile randomAccessFile = null;
      randomAccessFile = new RandomAccessFile(file, "r");
      inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));
      inArchive.extract(null, false, new MyExtractCallback(inArchive, file.getParent()));
      if (inArchive != null) {
        inArchive.close();
      }
      if (randomAccessFile != null) {
        randomAccessFile.close();
      }
      return true;
    } catch (Exception ex) {
      throw ex;
    }
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

  public class MyExtractCallback implements IArchiveExtractCallback {

    private final ISevenZipInArchive inArchive;
    private final String extractPath;

    public MyExtractCallback(ISevenZipInArchive inArchive, String extractPath) {
      this.inArchive = inArchive;
      this.extractPath = extractPath;
    }

    @Override
    public ISequentialOutStream getStream(final int index, ExtractAskMode extractAskMode) throws SevenZipException {
      return new ISequentialOutStream() {

        @Override
        public int write(byte[] data) throws SevenZipException {
          String filePath = inArchive.getStringProperty(index, PropID.PATH);

          FileOutputStream fos = null;
          try {
            if (shouldUnzip(filePath)) {
              fos = new FileOutputStream(extractPath + "/" + filePath, true);
              fos.write(data);
              unzippedFiles.add(filePath);
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
