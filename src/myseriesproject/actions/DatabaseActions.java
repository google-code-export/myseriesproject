/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseriesproject.actions;

import database.CreateDatabase;
import database.DBConnection;
import database.Database;
import database.SaveDatabase;
import database.SeriesRecord;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myComponents.myEvents.MyEvent;
import myComponents.myEvents.MyEventHandler;
import myComponents.myEvents.MyEventsClass;
import myseriesproject.MySeries;
import myseriesproject.series.Series;
import tools.MySeriesLogger;
import tools.options.MySeriesOptions;
import tools.options.Paths;

/**
 *
 * @author ssoldatos
 */
public class DatabaseActions {

  public static void createDatabase(MySeries m, boolean createNewDb) {
    try {
      String oldDb = MySeries.options.getStringOption(MySeriesOptions.DB_NAME);
      MySeries.glassPane.activate(null);
      MySeriesLogger.logger.log(Level.INFO, "Showing createDatabase panel");
      String db = null;
      db = MyMessages.ask("Create Database", "Type the name of the database");
      if (db != null) {
        if (db.endsWith(Database.EXT)) {
          db = db.substring(0, db.length() - 3);
        }
        CreateDatabase c = new CreateDatabase(null, db);
        c.run();
        doLoad(m, c.db, oldDb, true);
      }
      MySeries.glassPane.deactivate();
    } catch (Exception ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Error while creating DB", ex);
    }
  }

  public static void loadDatabase(MySeries m) {
    String oldDb = MySeries.options.getStringOption(MySeriesOptions.DB_NAME);
    try {
      MySeries.glassPane.activate(null);
      String[] filter = {".db"};
      MySeriesLogger.logger.log(Level.INFO, "Loading database");
      String load = MyUsefulFunctions.getSelectedFile(Paths.DATABASES_PATH, filter, "Load Database", "Select the database to load");
      if (load != null) {
        doLoad(m, load, oldDb, false);
      }
      MySeries.glassPane.deactivate();
    } catch (Exception ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Error while loading DB", ex);
    }
  }

  private static void doLoad(MySeries m, String load, String oldDb, boolean newDatabase) throws SQLException {
    MyEvent ev;
    DBConnection conn;
    ArrayList<SeriesRecord> series;
    if (load != null && !load.equals("null") && !load.equals(MySeries.options.getStringOption(MySeriesOptions.DB_NAME))) {
      MySeriesLogger.logger.log(Level.INFO, "Database to load : {0}", load);
      conn = new DBConnection(load, newDatabase);
      if (DBConnection.checkDatabase()) {
        m.setTitle("MySerieS v" + MySeries.version + " - Database: " + MySeries.options.getStringOption(MySeriesOptions.DB_NAME).replace(".db", ""));
        MyEventsClass evClass = new MyEventsClass(m);
        ev = new MyEvent(m, MyEventHandler.SERIES_UPDATE);
        evClass.fireMyEvent(ev);
        ev.setType(MyEventHandler.SET_CURRENT_SERIES);
        series = Series.getSeries(false);
        ev.setSeries(!series.isEmpty() ? series.get(0) : null);
        m.getEvClass().fireMyEvent(ev);
      } else {
        MySeriesLogger.logger.log(Level.WARNING, "Selected database is invlid.Not loading...");
        MyMessages.warning("Invalid Database", "The database you selected is invalid", true);
        loadDatabase(m);
      }
    } else {
      conn = new DBConnection(oldDb, newDatabase);
    }


  }

  public static void saveDatase() {
    MySeriesLogger.logger.log(Level.INFO, "Saving database action");
    new SaveDatabase();
  }
}
