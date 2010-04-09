/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.logging.Level;
import javax.swing.UnsupportedLookAndFeelException;
import myComponents.MyMessages;
import myseries.MySeries;
import myseries.StartPanel;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class CreateDatabase implements Runnable {

  private Statement stmt;
  private StartPanel startPanel;
  private boolean createNewDb = false;

  /**
   * Constructor for creating a new database
   * @param startPanelForm The form from which the constructor is invoked
   * @param db The database name
   * @param createNewDB Create a new db or ovewrite it if exists
   * @throws ClassNotFoundException
   * @throws SQLException
   * @throws FileNotFoundException
   * @throws IOException
   */
  public CreateDatabase(StartPanel startPanelForm, String db, boolean createNewDB) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
    if (!db.endsWith(Database.EXT)) {
      DBConnection.db = db + Database.EXT;
    } else {
      DBConnection.db = db;
    }
    DBConnection.createConnection(db);
    this.stmt = DBConnection.stmt;
    this.startPanel = startPanelForm;
    this.createNewDb = createNewDB;
  }

  /**
   * Starts the runnable
   */
  public void run() {
    try {
      File dbFile = new File(Options._USER_DIR_ + "/" + Database.PATH + DBConnection.db);
      if(dbFile.exists() && dbFile.length() >0 && createNewDb){
        MyMessages.error("DB Exists!!!", "DB File " + DBConnection.db + " already exists\nAborting...");
        MySeries.logger.log(Level.WARNING, "DB File already exists");
      } else {
      commit();
      }
    } catch (IOException ex) {
      MySeries.logger.log(Level.SEVERE, "Could not create the db file", ex);
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (ClassNotFoundException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (UnsupportedLookAndFeelException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (ParseException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }

  private void commit() throws IOException, SQLException, FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, ParseException {

    MySeries.logger.log(Level.INFO, "Loading the Database");
    createTables();
    if (startPanel.m == null) {
      startPanel.startMySeries();
    } else {
      MySeries.logger.log(Level.INFO, "Setting database");
      Options.setOption(Options.DB_NAME, startPanel.textbox_name.getText().trim());
      MySeries.logger.log(Level.INFO, "Saving options");
      Options.save();
      startPanel.dispose();
      startPanel.m.dispose();
      MySeries.logger.log(Level.INFO, "Loading MySerieS");
      new MySeries();
    }
  }

  /**
   * Create the database tables
   * @throws SQLException
   * @throws IOException
   */
  public void createTables() throws SQLException, IOException {
    MySeries.logger.log(Level.INFO, "Creating table episodes");
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS [episodes] " +
            "([episode_ID] INTEGER NOT NULL PRIMARY KEY UNIQUE," +
            "  [episode] VARCHAR DEFAULT 0," +
            " [title] VARCHAR DEFAULT ''," +
            " [series_ID] INTEGER NOT NULL REFERENCES [series]" +
            " ( series_ID ) ON DELETE CASCADE ON UPDATE RESTRICT," +
            " [aired] VARCHAR DEFAULT '0000-00-00'," +
            " [downloaded] INTEGER DEFAULT 0," +
            " [subs] INTEGER DEFAULT 0," +
            " [seen] INTEGER DEFAULT 0)");
    MySeries.logger.log(Level.INFO, "Creating table series");
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS [series] " +
            "([series_ID] INTEGER NOT NULL ON CONFLICT ABORT " +
            "PRIMARY KEY ON CONFLICT ABORT AUTOINCREMENT UNIQUE ON CONFLICT ABORT, " +
            "[title] VARCHAR NOT NULL ON CONFLICT ABORT," +
            "[season] INTEGER NOT NULL ON CONFLICT ABORT DEFAULT 0," +
            "[hidden] INTEGER DEFAULT 0," +
            "[link] VARCHAR," +
            "[internetUpdate] INTEGER DEFAULT 1," +
            "[tvrage_ID] INTEGER DEFAULT 0," +
            "[localDir] VARCHAR DEFAULT ''," +
            "[sonline] VARCHAR DEFAULT ''," +
            "[screenshot] VARCHAR DEFAULT '')");
    MySeries.logger.log(Level.INFO, "Creating table filters");
    stmt.executeUpdate("CREATE  TABLE IF NOT EXISTS [filters] " +
            "([filter_ID] INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " +
            "[title] VARCHAR NOT NULL  DEFAULT 'filter', " +
            "[downloaded] INTEGER NOT NULL  DEFAULT 0, " +
            "[seen] INTEGER NOT NULL  DEFAULT 0, " +
            "[subtitles] INTEGER NOT NULL  DEFAULT 0)");
  }
}