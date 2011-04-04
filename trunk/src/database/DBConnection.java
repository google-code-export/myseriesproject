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
import tools.MySeriesLogger;
import tools.options.Options;

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
  public static boolean checkDatabase(String db) {
    //try {
    createConnection(db);
    String sqlSeries = "PRAGMA table_info(series)";
    String sqlEpisodes = "PRAGMA table_info(episodes)";
    String sqlFeeds = "SELECT name FROM sqlite_master WHERE name='feeds'";

    ResultSet rsSeries;
    ResultSet rsEpisodes;
    ResultSet rsFeeds;
    boolean internetUpdate = false;
    boolean tvRageID = false;
    boolean localDir = false;
    boolean screenshot = false;
    boolean sonline = false;
    boolean newSubs = false;
    boolean rate = false;
    boolean deleted = false;
    boolean feeds = false;

    try {
      rsSeries = stmt.executeQuery(sqlSeries);
      while (rsSeries.next()) {
        if (rsSeries.getString(2).equals("internetUpdate")) {
          internetUpdate = true;
        }
        if (rsSeries.getString(2).equals("tvrage_ID")) {
          tvRageID = true;
        }
        if (rsSeries.getString(2).equals("localDir")) {
          localDir = true;
        }
        if (rsSeries.getString(2).equals("screenshot")) {
          screenshot = true;
        }
        if (rsSeries.getString(2).equals("sonline")) {
          sonline = true;
        }
        if (rsSeries.getString(2).equals("deleted")) {
          deleted = true;
        }
      }

      rsEpisodes = stmt.executeQuery(sqlEpisodes);
      while (rsEpisodes.next()) {
        if (rsEpisodes.getString(2).equals("rate")) {
          rate = true;
        }
      }

      rsFeeds = stmt.executeQuery(sqlFeeds);
      while (rsFeeds.next()) {
        feeds = true;
      }

      if (tvRageID && internetUpdate && localDir && screenshot && sonline && rate && deleted && feeds) {
        return true;
      }
      MyMessages.message("Old Database version", "Database is of an older version and needs update\nA back Up is taken first!!");
      SaveDatabase s = new SaveDatabase(db);
      if (s.backUp) {
        if (!internetUpdate) {
          sqlSeries = "ALTER TABLE series ADD COLUMN internetUpdate INTEGER DEFAULT 1";
          stmt.execute(sqlSeries);
        }
        if (!tvRageID) {
          sqlSeries = "ALTER TABLE series ADD COLUMN tvrage_ID INTEGER DEFAULT 0";
          stmt.execute(sqlSeries);
        }
        if (!localDir) {
          sqlSeries = "ALTER TABLE series ADD COLUMN localDir VARCHAR DEFAULT ''";
          stmt.execute(sqlSeries);
        }
        if (!screenshot) {
          sqlSeries = "ALTER TABLE series ADD COLUMN screenshot VARCHAR DEFAULT ''";
          stmt.execute(sqlSeries);
        }
        if (!sonline) {
          sqlSeries = "ALTER TABLE series ADD COLUMN sonline VARCHAR DEFAULT ''";
          stmt.execute(sqlSeries);
        }
        if (!deleted) {
          sqlSeries = "ALTER TABLE series ADD COLUMN deleted INTEGER DEFAULT 0";
          stmt.execute(sqlSeries);
        }
        if (!rate) {
          sqlEpisodes = "ALTER TABLE episodes ADD COLUMN rate DOUBLE DEFAULT 0.0";
          stmt.execute(sqlEpisodes);
        }
        if (!feeds) {
          MySeriesLogger.logger.log(Level.INFO, "Creating table feeds");
          stmt.executeUpdate("CREATE TABLE IF NOT EXISTS  [feeds]"
                  + "([feed_ID] INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,"
                  + "[title] VARCHAR NOT NULL , "
                  + "[url] VARCHAR NOT NULL )");
        }
        MyMessages.message("Database Update", "Database Update done!!!");
        checkDatabase(db);
        return true;
      } else {
        MyMessages.error("No Update", "Could not update the database");
        MySeriesLogger.logger.log(Level.SEVERE, "Could not update the database.Exiting...");
        System.exit(1);
        return false;
      }
    } catch (SQLException ex1) {
      MySeriesLogger.logger.log(Level.SEVERE, "SQL Error.Exiting...", ex1);
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
      MySeriesLogger.logger.log(Level.INFO, "Creating database connection");
      if (!db.endsWith(Database.EXT)) {
        DBConnection.db = db + Database.EXT;
      } else {
        DBConnection.db = db;
      }
      Class.forName("org.sqlite.JDBC");
      conn = DriverManager.getConnection("jdbc:sqlite:" + Options._USER_DIR_ + Database.PATH + DBConnection.db);
      stmt = conn.createStatement();
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not connect to the SQLite database", ex);
    } catch (ClassNotFoundException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not find SQLite class", ex);
    }
  }

  /**
   * Checks if database exists
   * @param dbName The database name
   * @return
   */
  public static boolean databaseExists(String dbName) {
    if (new File(Options._USER_DIR_ + Database.PATH + dbName).isFile()) {
      return true;
    }
    return false;
  }

  private DBConnection() {
  }
}
