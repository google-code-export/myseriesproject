/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;
import java.util.logging.Level;
import tools.MySeriesLogger;
import tools.options.MySeriesOptions;
import tools.options.Paths;
/**
 * Database helper class
 * @author ssoldatos
 */
public class Database implements DatabaseConstants {

  public static boolean inTransaction;

  /**
   * Get All Databases
   * @return A vector of all the databases
   */
  public static Vector<String> getDatabases() {
    MySeriesLogger.logger.log(Level.INFO, "Getting databases");
    Vector<String> databases = new Vector<String>();
    File dir = new File(MySeriesOptions._USER_DIR_ + Paths.DATABASES_PATH);
    File[] files = dir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(EXT);
      }
    });
    databases.addElement("");
    for (int i = 0; i < files.length; i++) {
      databases.addElement(files[i].getName().replace(EXT, ""));
      MySeriesLogger.logger.log(Level.FINE, "Found database: {0}" , files[i]);
    }
    return databases;
  }


  private Database() {
  }
}
