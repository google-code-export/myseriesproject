/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.archive;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import myComponents.MyUsefulFunctions;
import tools.MySeriesLogger;

/**
 *
 * @author ssoldatos
 */
public class ArchiveFile implements ArchiveConstants {

  private File file;
  private String ext;
  public boolean res = false;
  public ArrayList<String> extractedFiles = new ArrayList<String>();

  public ArchiveFile(File file) {
    MySeriesLogger.logger.log(Level.INFO, "Unziping file {0}", file);
    this.file = file;
    this.ext = MyUsefulFunctions.getExtension(file);
  }

  public boolean unzip(String directory, boolean delete, int type) throws Exception {
    if (ext.toLowerCase().equals(ZIP)) {
      MySeriesLogger.logger.log(Level.INFO, "Unziping file {0} (zip compression)", file);
      ZipFile zip = new ZipFile(file);
      zip.unzip(directory, type);
      res = zip.result;
      extractedFiles = zip.extractedFiles;
    } else if (ext.toLowerCase().equals(RAR)) {
      MySeriesLogger.logger.log(Level.INFO, "Unziping file {0} (rar compression)", file);
      RarFile rar = new RarFile(file);
      rar.unzip(directory, type);
      res = rar.result;
      extractedFiles = rar.extractedFiles;
    } else {
      MySeriesLogger.logger.log(Level.INFO, "Unziping file {0} (7zip compression)", file);
      SevenZipFile sev = new SevenZipFile(file);
      sev.unzip(directory, type);
      res = sev.result;
      extractedFiles = sev.extractedFiles;
    }
    if (delete && !extractedFiles.isEmpty()) {
      if (file.delete()) {
        MySeriesLogger.logger.log(Level.INFO, "Original zipped file {0} deleted", file);
      } else {
        MySeriesLogger.logger.log(Level.WARNING, "Original zipped file {0} could not be deleted", file);
      }
    }
    return res;
  }

  public ArrayList<String> getEntries(int type) throws Exception {
    if (ext.toLowerCase().equals(ZIP)) {
      MySeriesLogger.logger.log(Level.INFO, "Getting entries of file {0} (zip compression)", file);
      ZipFile zip = new ZipFile(file);
      return zip.getEntries(type);
    } else if (ext.toLowerCase().equals(RAR)) {
      MySeriesLogger.logger.log(Level.INFO, "Getting entries of file {0} (rar compression)", file);
      RarFile rar = new RarFile(file);
      return rar.getEntries(type);
    } else {
      MySeriesLogger.logger.log(Level.INFO, "Getting entries of file {0} (7zip compression)", file);
      SevenZipFile sev = new SevenZipFile(file);
      return sev.getEntries(type);
    }


  }
}
