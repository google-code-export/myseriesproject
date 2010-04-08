/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import myComponents.MyMessages;
import tools.options.Options;
import myseries.MySeries;

/**
 *
 * @author lordovol
 */
public class DBConnection {

  public static String db;
  public static Connection conn;
  public static Statement stmt;

/**
 * Check if database needs updating
 * @param db The database to check
 * @return true if update is done or does not need update. False if update is
 * needed but an error occured
 */
  public static boolean CheckDatabase(String db) {
    //try {
    createConnection(db);
    String sql = "PRAGMA table_info(series)";
    ResultSet rs;
    boolean internetUpdate = false;
    boolean tvRageID = false;
    boolean localDir = false;
    boolean screenshot = false;
    boolean sonline = false;

    try {
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        if (rs.getString(2).equals("internetUpdate")) {
          internetUpdate = true;
        }
        if (rs.getString(2).equals("tvrage_ID")) {
          tvRageID = true;
        }
        if (rs.getString(2).equals("localDir")) {
          localDir = true;
        }
        if (rs.getString(2).equals("screenshot")) {
          screenshot = true;
        }
        if (rs.getString(2).equals("sonline")) {
          sonline = true;
        }
      }
      if(tvRageID && internetUpdate && localDir && screenshot && sonline){
        return true;
      }
      MyMessages.message("Old Database version", "Database is of an older version and needs update\nA back Up is taken first!!");
      SaveDatabase s = new SaveDatabase(db);
      if (s.backUp) {
        if(!internetUpdate){
        sql = "ALTER TABLE series ADD COLUMN internetUpdate INTEGER DEFAULT 1";
        stmt.execute(sql);
        }
        if(!tvRageID){
        sql = "ALTER TABLE series ADD COLUMN tvrage_ID INTEGER DEFAULT 0";
        stmt.execute(sql);
        }
        if(!localDir){
        sql = "ALTER TABLE series ADD COLUMN localDir VARCHAR DEFAULT ''";
        stmt.execute(sql);
        }
        if(!screenshot){
        sql = "ALTER TABLE series ADD COLUMN screenshot VARCHAR DEFAULT ''";
        stmt.execute(sql);
        }
        if(!sonline){
        sql = "ALTER TABLE series ADD COLUMN sonline VARCHAR DEFAULT ''";
        stmt.execute(sql);
        }
        MyMessages.message("Database Update", "Database Update done!!!");
        CheckDatabase(db);
        return true;
      } else {
        MyMessages.error("No Update", "Could not update the database");
        MySeries.logger.log(Level.SEVERE, "Could not update the database.Exiting...");
        System.exit(1);
        return false;
      }
    } catch (SQLException ex1) {
      MySeries.logger.log(Level.SEVERE, "SQL Error.Exiting...", ex1);
      System.exit(1);
      return true;
    }
  }

  /**
   * Creates a connection to the database
   * @param db The database name
   */
  public static void createConnection(String db) {
    try {
      MySeries.logger.log(Level.INFO, "Creating database connection");
      if (!db.endsWith(Database.EXT)) {
        DBConnection.db = db + Database.EXT;
      } else {
        DBConnection.db = db;
      }
      Class.forName("org.sqlite.JDBC");
      conn = DriverManager.getConnection("jdbc:sqlite:" + Options._USER_DIR_ + "/" + Database.PATH + DBConnection.db);
      stmt = conn.createStatement();
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, "Could not connect to the SQLite database", ex);
    } catch (ClassNotFoundException ex) {
      MySeries.logger.log(Level.SEVERE, "Could not find SQLite class", ex);
    }
  }

  /**
   * Checks if database exists
   * @param dbName The database name
   * @return
   */
  public static boolean databaseExists(String dbName) {
    if (new File(Options._USER_DIR_ + "/" + Database.PATH + dbName).isFile()) {
      return true;
    }
    return false;
  }

  private DBConnection() {
  }
}
