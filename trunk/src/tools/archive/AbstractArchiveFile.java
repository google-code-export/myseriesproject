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
public abstract class AbstractArchiveFile implements ArchiveConstants {

  ArrayList<String> extractedFiles = new ArrayList<String>();
  File archivedFile;
  boolean result = false;

  boolean shouldUnzip(String entryName, int type) {
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

  abstract void unzip(String directory, int type) throws Exception;
}
