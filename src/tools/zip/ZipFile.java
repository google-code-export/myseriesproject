/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.zip;

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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import myComponents.MyUsefulFunctions;
import net.sf.sevenzipjbinding.ArchiveFormat;
import net.sf.sevenzipjbinding.ExtractAskMode;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IArchiveExtractCallback;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.ISevenZipInArchive;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import tools.MySeriesLogger;

/**
 *
 * @author ssoldatos
 */
public class ZipFile {

  public ArrayList<String> unzippedFiles = new ArrayList<String>();
  private File file;
  private String ext;
  public static final int NONE = -1;
  public static final int ALL = 0;
  public static final int SUBTITLES = 1;
  public static final String ZIP = "zip";
  public static final String RAR = "rar";
  public static final String SEVEN_ZIP = "7z";

  public ZipFile(File file) {
    MySeriesLogger.logger.log(Level.INFO, "Unziping file {0}", file);
    this.file = file;
    this.ext = MyUsefulFunctions.getExtension(file);
  }

  public boolean unzip(String directory, boolean delete, int type) throws Exception {
    boolean res = false;
    if (ext.toLowerCase().equals(ZIP)) {
      MySeriesLogger.logger.log(Level.INFO, "Unziping file {0} (zip compression)", file);
      res = unzipZipFile(directory, type);
    } else if (ext.toLowerCase().equals(RAR)) {
      MySeriesLogger.logger.log(Level.INFO, "Unziping file {0} (rar compression)", file);
      res = unzipRarFile(directory, type);
    } else {
      MySeriesLogger.logger.log(Level.INFO, "Unziping file {0} (7zip compression)", file);
      res = unzipSevenZipFile(directory, type);
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

  private boolean unzipZipFile(String directory, int type) throws Exception {
    try {
      byte[] buf = new byte[1024];
      ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
      ZipEntry zipEntry = zipInputStream.getNextEntry();
      while (zipEntry != null) {
        //for each entry to be extracted
        String entryName = zipEntry.getName();
        if (shouldUnzip(entryName, type)) {
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

  private boolean unzipRarFile(String directory, int type) throws Exception {

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
        if (shouldUnzip(name, type)) {
          FileOutputStream os = new FileOutputStream(directory + "/" + name);
          rarFile.extractFile(fileHeader, os);
          unzippedFiles.add(name);
          os.close();
        }
      }

      rarFile.close();

    } catch (Exception ex) {
      throw ex;
    }
    return true;
  }

  private boolean unzipSevenZipFile(String directory, int type) throws Exception {
    try {
      //SevenZip.initSevenZipFromPlatformJAR(ext, file);
      ISevenZipInArchive inArchive = null;
      RandomAccessFile randomAccessFile = null;
      randomAccessFile = new RandomAccessFile(file, "r");
      inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));
      inArchive.extract(null, false, new MyExtractCallback(inArchive, directory, type));
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

  private boolean shouldUnzip(String entryName, int type) {
    MySeriesLogger.logger.log(Level.INFO, "Check if {0} should unzip", entryName);
    switch (type) {
      case NONE:
        MySeriesLogger.logger.log(Level.INFO, "Unzipping none");
        return false;
      case ALL:
        MySeriesLogger.logger.log(Level.INFO, "Unzipping all");
        return true;
      case SUBTITLES:
        MySeriesLogger.logger.log(Level.INFO, "Checking if subtitle");
        return MyUsefulFunctions.isSubtitle(entryName);
    }
    return false;
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
