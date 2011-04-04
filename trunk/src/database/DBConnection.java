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
    createConnection(db);
    MySeriesLogger.logger.log(Level.INFO, "Checking database tables");
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
      MySeriesLogger.logger.log(Level.INFO, "Checking series table");
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
      MySeriesLogger.logger.log(Level.INFO, "Checking episodes table");
      rsEpisodes = stmt.executeQuery(sqlEpisodes);
      while (rsEpisodes.next()) {
        if (rsEpisodes.getString(2).equals("rate")) {
          rate = true;
        }
      }
      MySeriesLogger.logger.log(Level.INFO, "Checking feeds table");
      rsFeeds = stmt.executeQuery(sqlFeeds);
      while (rsFeeds.next()) {
        feeds = true;
      }

      if (tvRageID && internetUpdate && localDir && screenshot && sonline && rate && deleted && feeds) {
        MySeriesLogger.logger.log(Level.INFO, "Database does not need updating");
        return true;
      }
      MySeriesLogger.logger.log(Level.INFO, "Database is of an older version and needs update");
      MyMessages.message("Old Database version", "Database is of an older version and needs update\nA back Up is taken first!!");
      MySeriesLogger.logger.log(Level.INFO, "Database backup");
      SaveDatabase s = new SaveDatabase(db);
      if (s.backUp) {
        MySeriesLogger.logger.log(Level.FINE, "Database back up was succesful");

        if (!internetUpdate) {
          MySeriesLogger.logger.log(Level.INFO, "Adding internet update column");
          sqlSeries = "ALTER TABLE series ADD COLUMN internetUpdate INTEGER DEFAULT 1";
          stmt.execute(sqlSeries);
        }
        if (!tvRageID) {
          MySeriesLogger.logger.log(Level.INFO, "Adding tvrage id column");
          sqlSeries = "ALTER TABLE series ADD COLUMN tvrage_ID INTEGER DEFAULT 0";
          stmt.execute(sqlSeries);
        }
        if (!localDir) {
          MySeriesLogger.logger.log(Level.INFO, "Adding local directory column");
          sqlSeries = "ALTER TABLE series ADD COLUMN localDir VARCHAR DEFAULT ''";
          stmt.execute(sqlSeries);
        }
        if (!screenshot) {
          MySeriesLogger.logger.log(Level.INFO, "Adding screenshot column");
          sqlSeries = "ALTER TABLE series ADD COLUMN screenshot VARCHAR DEFAULT ''";
          stmt.execute(sqlSeries);
        }
        if (!sonline) {
          MySeriesLogger.logger.log(Level.INFO, "Adding sonline id column");
          sqlSeries = "ALTER TABLE series ADD COLUMN sonline VARCHAR DEFAULT ''";
          stmt.execute(sqlSeries);
        }
        if (!deleted) {
          MySeriesLogger.logger.log(Level.INFO, "Adding deleted column");
          sqlSeries = "ALTER TABLE series ADD COLUMN deleted INTEGER DEFAULT 0";
          stmt.execute(sqlSeries);
        }
        if (!rate) {
          MySeriesLogger.logger.log(Level.INFO, "Adding rate column");
          sqlEpisodes = "ALTER TABLE episodes ADD COLUMN rate DOUBLE DEFAULT 0.0";
          stmt.execute(sqlEpisodes);
        }
        if (!feeds) {
          MySeriesLogger.logger.log(Level.INFO, "Adding feeds table");
          MySeriesLogger.logger.log(Level.INFO, "Creating table feeds");
          stmt.executeUpdate("CREATE TABLE IF NOT EXISTS  [feeds]"
              + "([feed_ID] INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,"
              + "[title] VARCHAR NOT NULL , "
              + "[url] VARCHAR NOT NULL )");
        }
        MySeriesLogger.logger.log(Level.FINE, "Database was updated");
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
      if (!db.endsWith(Database.EXT)) {
        DBConnection.db = db + Database.EXT;
      } else {
        DBConnection.db = db;
      }
      MySeriesLogger.logger.log(Level.INFO, "Creating database connection with {0}", db);
      Class.forName("org.sqlite.JDBC");
      conn = DriverManager.getConnection("jdbc:sqlite:" + Options._USER_DIR_ + Database.PATH + DBConnection.db);
      stmt = conn.createStatement();
      MySeriesLogger.logger.log(Level.FINE, "Database connection established");
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
      MySeriesLogger.logger.log(Level.FINE, "Database exists");
      return true;
    }
    MySeriesLogger.logger.log(Level.WARNING, "Database does not exist");
    return false;
  }

  private DBConnection() {
  }
}
