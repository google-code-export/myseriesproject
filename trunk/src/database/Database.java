/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import tools.options.Options;

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
    Vector<String> databases = new Vector<String>();
    File dir = new File(Options._USER_DIR_ + Database.PATH);
    File[] files = dir.listFiles(new FilenameFilter() {
      @Override
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

  public static void endTransaction() {
    try {
      database.DBConnection.stmt.execute("END TRANSACTION");
      inTransaction=false;
    } catch (SQLException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }
  public static void beginTransaction() {
    try {
      while (inTransaction){
        
      }
      inTransaction=true;
      database.DBConnection.stmt.execute("BEGIN TRANSACTION");
    } catch (SQLException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }


  private Database() {
  }
}
