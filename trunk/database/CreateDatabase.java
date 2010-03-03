/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.logging.Level;
import javax.swing.UnsupportedLookAndFeelException;
import myComponents.MyUsefulFunctions;
import myseries.MySeries;
import myseries.StartPanel;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class CreateDatabase implements Runnable {

  private Connection conn;
  private Statement stmt;
  private boolean demo;
  private StartPanel s;
  private boolean createNewDb = false;

  public void run() {
    try {
      File dbFile = new File(Options._USER_DIR_ + "/" + Options._DB_PATH_ + DBConnection.db);
      if(dbFile.exists() && dbFile.length() >0 && createNewDb){
        MyUsefulFunctions.error("DB Exists!!!", "DB File " + DBConnection.db + " already exists\nAborting...");
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
    loadDatabases();
    if (demo) {
      s.progress.setVisible(true);
      MySeries.logger.log(Level.INFO, "Loading demo data");
      loadDemoData();
    }
    if (s.m == null) {
      s.startMySeries();
    } else {
      MySeries.logger.log(Level.INFO, "Setting database");
      Options.setOption(Options.DB_NAME, s.textbox_name.getText().trim());
      MySeries.logger.log(Level.INFO, "Saving options");
      Options.save();
      s.dispose();
      s.m.dispose();
      MySeries.logger.log(Level.INFO, "Loading MySerieS");
      MySeries mNew = new MySeries();
    }
  }

  public CreateDatabase(StartPanel s, String db, boolean demo, boolean createNewDB) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
    if (!db.endsWith(".db")) {
      DBConnection.db = db + ".db";
    } else {
      DBConnection.db = db;
    }
    DBConnection.createConnection(db);
    this.stmt = DBConnection.stmt;
    this.demo = demo;
    this.s = s;
    this.createNewDb = createNewDB;
  }

  private void loadDemoData() throws FileNotFoundException, IOException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
    MySeries.logger.log(Level.INFO, "Importing demo data");
    //File data = new File("database/demoData.sql");
    //BufferedReader in = MyUsefulFunctions.createInputStream(data);
    InputStream is = CreateDatabase.class.getResourceAsStream("demoData.sql");
    BufferedReader in = new BufferedReader(new InputStreamReader(is));

    
    long length = 6432;
    long curLength = 0;
    String line;

    while ((line = in.readLine()) != null) {
      curLength += line.length() + 2;
      stmt.executeUpdate(line.trim());
      int perc = (int) ((curLength * 100) / length);
      s.progress.setValue(perc);
    }
    s.progress.setValue(0);
    in.close();
    MySeries.logger.log(Level.INFO, "Demo data was imported");
  }

  public void loadDatabases() throws SQLException, IOException {
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
            "[tvrage_ID] INTEGER DEFAULT 0)");
    MySeries.logger.log(Level.INFO, "Creating table filters");
    stmt.executeUpdate("CREATE  TABLE IF NOT EXISTS [filters] " +
            "([filter_ID] INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " +
            "[title] VARCHAR NOT NULL  DEFAULT 'filter', " +
            "[downloaded] INTEGER NOT NULL  DEFAULT 0, " +
            "[seen] INTEGER NOT NULL  DEFAULT 0, " +
            "[subtitles] INTEGER NOT NULL  DEFAULT 0)");
  }
}
