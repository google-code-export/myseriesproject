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
import java.util.logging.Logger;
import myComponents.MyMessages;
import sdialogs.Info;
import tools.MySeriesLogger;
import tools.options.Options;
import tools.options.Paths;

/**
 *
 * @author lordovol
 */
public class DBConnection {

  public String db;
  public Connection conn;
  public Statement stmt;
  private boolean inTransaction;

  public DBConnection(String db) {
    MySeriesLogger.logger.log(Level.INFO, "Creating connection");
    this.db = db;
    createConnection();
  }

  public DBConnection() {
    MySeriesLogger.logger.log(Level.INFO, "Creating connection");
    this.db = Options.toString(Options.DB_NAME);
    createConnection();
  }

  /**
   * Check if database needs updating
   * @param db The database to check
   * @return true if update is done or does not need update. False if update is
   * needed but an error occured
   */
  public boolean checkDatabase() {
    createConnection();
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
      MyMessages.message("Old Database version", 
          "Database is of an older version and needs update\nA back Up is taken first!!",Info.WARNING_MESS, true);
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
        checkDatabase();
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
  private void createConnection() {
    try {
      if (!db.endsWith(Database.EXT)) {
        db = db + Database.EXT;
      }
      MySeriesLogger.logger.log(Level.INFO, "Creating database connection with {0}", db);
      Class.forName("org.sqlite.JDBC");
      conn = DriverManager.getConnection("jdbc:sqlite:" + Options._USER_DIR_ + Paths.DATABASES_PATH + db);
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
  public boolean databaseExists() {
    if (new File(Options._USER_DIR_ + Paths.DATABASES_PATH + db).isFile()) {
      MySeriesLogger.logger.log(Level.FINE, "Database exists");
      return true;
    }
    MySeriesLogger.logger.log(Level.WARNING, "Database does not exist");
    return false;
  }

  public boolean close() {
    try {
      MySeriesLogger.logger.log(Level.INFO, "Closing connection");
      conn.close();
      return true;
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not close connection", ex);
      return false;
    }
  }

  public void endTransaction() {
    try {
      MySeriesLogger.logger.log(Level.INFO, "Ending transaction");
      stmt.execute("END TRANSACTION");
      inTransaction=false;
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
  }
  public void beginTransaction() {
    try {
      while (inTransaction){

      }
      inTransaction=true;
      MySeriesLogger.logger.log(Level.INFO, "Beggining transaction");
      stmt.execute("BEGIN TRANSACTION");
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
  }
}
