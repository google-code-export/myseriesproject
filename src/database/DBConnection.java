/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import Exceptions.DatabaseException;
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

    public static String db;
    public static Connection conn;
    private static Statement stmt;
    private static boolean inTransaction;
    public static boolean isConnected;

    public DBConnection(String db, boolean newDatabase) {
        MySeriesLogger.logger.log(Level.INFO, "Creating connection");
        DBConnection.db = db;
        try {
            createConnection(newDatabase);
        } catch (DatabaseException ex) {
            isConnected = false;
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Check if database needs updating
     * @param db The database to check
     * @return true if update is done or does not need update. False if update is
     * needed but an error occured
     */
    public static boolean checkDatabase() {
        MySeriesLogger.logger.log(Level.INFO, "Checking database tables");
        String sqlSeries = "PRAGMA table_info(series)";
        String sqlEpisodes = "PRAGMA table_info(episodes)";
        String sqlFeeds = "SELECT name FROM sqlite_master WHERE name='feeds'";

        ResultSet rsSeries = null;
        ResultSet rsEpisodes = null;
        ResultSet rsFeeds = null;
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
            rsSeries = conn.createStatement().executeQuery(sqlSeries);
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
                    "Database is of an older version and needs update\nA back Up is taken first!!", Info.WARNING_MESS, true, true);
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
                // Options.setOption(Options.DB_NAME, db);
                // Options.save();
                checkDatabase();
                return true;
            } else {
                Options.setOption(Options.DB_NAME, "");
                Options.save();
                MyMessages.error("No Update", "Could not update the database", true);
                MySeriesLogger.logger.log(Level.SEVERE, "Could not update the database.Exiting...");
                isConnected = false;
                // System.exit(1);
                return false;
            }
        } catch (Exception ex1) {
            MySeriesLogger.logger.log(Level.SEVERE, "SQL Error.Exiting...", ex1);
            // Options.setOption(Options.DB_NAME, "");
            // Options.save();
            isConnected = false;
            //System.exit(1);
            return false;
        }
    }

    /**
     * Creates a connection to the database
     * @param db The database name
     */
    private static void createConnection(boolean newDatabase) throws DatabaseException {
        try {
            MySeriesLogger.logger.log(Level.INFO, "Creating database connection with {0}", db);
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + Options._USER_DIR_ + Paths.DATABASES_PATH + db);
            stmt = conn.createStatement();
            if (!newDatabase) {
                if (checkDatabase()) {
                    Options.setOption(Options.DB_NAME, db);
                    Options.save();
                    MySeriesLogger.logger.log(Level.FINE, "Database connection established");
                    isConnected = true;
                } else {
                    isConnected = false;
                }
            }
        } catch (SQLException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Could not connect to the SQLite database", ex);
            isConnected = false;
        } catch (ClassNotFoundException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Could not find SQLite class", ex);
            isConnected = false;
        }
    }

    /**
     * Checks if database exists
     * @param dbName The database name
     * @return
     */
    public static boolean databaseExists(String dbName) {
        if (new File(Options._USER_DIR_ + Paths.DATABASES_PATH + dbName).isFile()) {
            MySeriesLogger.logger.log(Level.FINE, "Database {0} exists", dbName);
            return true;
        }
        MySeriesLogger.logger.log(Level.WARNING, "Database {0} does not exist", dbName);
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

    public static void endTransaction() {
        try {
            MySeriesLogger.logger.log(Level.INFO, "Ending transaction");
            stmt.execute("END TRANSACTION");
            inTransaction = false;
        } catch (SQLException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, null, ex);
        }
    }

    public static void beginTransaction() {
        try {
            while (inTransaction) {
            }
            inTransaction = true;
            MySeriesLogger.logger.log(Level.INFO, "Beggining transaction");
            stmt.execute("BEGIN TRANSACTION");
        } catch (SQLException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, null, ex);
        }
    }
}
