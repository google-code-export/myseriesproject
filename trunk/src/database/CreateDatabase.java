/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import javax.swing.UnsupportedLookAndFeelException;
import myComponents.MyMessages;
import myseries.StartPanel;
import myseries.actions.ApplicationActions;
import tools.MySeriesLogger;
import tools.options.Options;
import tools.options.Paths;

/**
 *
 * @author lordovol
 */
public class CreateDatabase implements Runnable {

    private StartPanel startPanel;
    public String db;

    /**
     * Constructor for creating a new database
     * @param startPanelForm The form from which the constructor is invoked
     * @param db The database name
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public CreateDatabase(StartPanel startPanelForm, String db) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
        if (!db.endsWith(Database.EXT)) {
            this.db = db + Database.EXT;
        }
        DBConnection conn = new DBConnection(this.db, true);
        this.startPanel = startPanelForm;
    }

    /**
     * Starts the runnable
     */
    @Override
    public void run() {
        try {
            File dbFile = new File(Options._USER_DIR_ + Paths.DATABASES_PATH + DBConnection.db);
            if (dbFile.exists() && dbFile.length() > 1) {
                MyMessages.error("DB Exists!!!", "DB File " + DBConnection.db + " already exists\nAborting...", true);
                MySeriesLogger.logger.log(Level.WARNING, "DB File already exists");
            } else {
                commit();
            }
        } catch (IOException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Could not create the db file", ex);

        } catch (SQLException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "SQL exception", ex);
        } catch (ClassNotFoundException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, null, ex);
        }
    }

    private void commit() throws IOException, SQLException, FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, ParseException {
        createTables();
        if (startPanel != null) {
            startProgram();
        }
    }

    private void startProgram() throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        if (startPanel.m == null) {
            startPanel.startMySeries();
        } else {
            Options.setOption(Options.DB_NAME, startPanel.dbName.trim());
            Options.save();
            MySeriesLogger.logger.log(Level.INFO, "Closing starting panel");
            startPanel.dispose();
            MySeriesLogger.logger.log(Level.INFO, "Closing Main Window");
            startPanel.m.dispose();
            MySeriesLogger.logger.log(Level.INFO, "Restarting MySerieS");
            ApplicationActions.restartApplication(startPanel.m);
        }
    }

    /**
     * Create the database tables
     * @throws SQLException
     * @throws IOException
     */
    public void createTables() throws SQLException, IOException {
        MySeriesLogger.logger.log(Level.INFO, "Creating table episodes");
        DBConnection.conn.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS [episodes] "
                + "([episode_ID] INTEGER NOT NULL PRIMARY KEY UNIQUE,"
                + "  [episode] VARCHAR DEFAULT 0,"
                + " [title] VARCHAR DEFAULT '',"
                + " [series_ID] INTEGER NOT NULL REFERENCES [series]"
                + " ( series_ID ) ON DELETE CASCADE ON UPDATE RESTRICT,"
                + " [aired] VARCHAR DEFAULT '0000-00-00',"
                + " [downloaded] INTEGER DEFAULT 0,"
                + " [subs] INTEGER DEFAULT 0,"
                + " [seen] INTEGER DEFAULT 0,"
                + " [rate] BOOLEAN DEFAULT 0.0)");
        MySeriesLogger.logger.log(Level.FINE, "Episodes table created");
        MySeriesLogger.logger.log(Level.INFO, "Creating table series");
        DBConnection.conn.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS [series] "
                + "([series_ID] INTEGER NOT NULL ON CONFLICT ABORT "
                + "PRIMARY KEY ON CONFLICT ABORT AUTOINCREMENT UNIQUE ON CONFLICT ABORT, "
                + "[title] VARCHAR NOT NULL ON CONFLICT ABORT,"
                + "[season] INTEGER NOT NULL ON CONFLICT ABORT DEFAULT 0,"
                + "[hidden] INTEGER DEFAULT 0,"
                + "[link] VARCHAR,"
                + "[internetUpdate] INTEGER DEFAULT 1,"
                + "[tvrage_ID] INTEGER DEFAULT 0,"
                + "[localDir] VARCHAR DEFAULT '',"
                + "[sonline] VARCHAR DEFAULT '',"
                + "[screenshot] VARCHAR DEFAULT '',"
                + "[deleted]INTEGER DEFAULT 0)");
        MySeriesLogger.logger.log(Level.INFO, "Creating table filters");
        DBConnection.conn.createStatement().executeUpdate("CREATE  TABLE IF NOT EXISTS [filters] "
                + "([filter_ID] INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "
                + "[title] VARCHAR NOT NULL  DEFAULT 'filter', "
                + "[downloaded] INTEGER NOT NULL  DEFAULT 0, "
                + "[seen] INTEGER NOT NULL  DEFAULT 0, "
                + "[subtitles] INTEGER NOT NULL  DEFAULT 0)");
        MySeriesLogger.logger.log(Level.INFO, "Creating table feeds");
        DBConnection.conn.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS  [feeds]"
                + "([feed_ID] INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,"
                + "[title] VARCHAR NOT NULL , "
                + "[url] VARCHAR NOT NULL )");
    }
}
