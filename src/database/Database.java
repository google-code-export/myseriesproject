/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import tools.options.Options;

/**
 * Database helper class
 * @author ssoldatos
 */
public class Database {

  /**
   * The database extension : .db
   */
  public static final String EXT = ".db";
  /**
   * The back up extension : .bk
   */
  public static final String BACK_UP_EXT = ".bk";
  /**
   * The databases path : "databases/"
   */
  public static final String PATH = "databases/";


  /**
   * Get All Databases
   * @return A vector of all the databases
   */
  public static Vector<String> getDatabases() {
    Vector<String> databases = new Vector<String>();
    File dir = new File(Options._USER_DIR_ + "/" + Database.PATH);
    File[] files = dir.listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith(EXT);
      }
    });
    databases.addElement("");
    for (int i = 0; i < files.length; i++) {
      databases.addElement(files[i].getName().replace(EXT, ""));
    }
    return databases;
  }


  private Database() {
  }
}
