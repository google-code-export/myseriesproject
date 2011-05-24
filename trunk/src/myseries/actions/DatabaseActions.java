/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.actions;

import database.CreateDatabase;
import database.DBConnection;
import database.Database;
import database.SaveDatabase;
import database.SeriesRecord;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myComponents.myEvents.MyEvent;
import myComponents.myEvents.MyEventHandler;
import myComponents.myEvents.MyEventsClass;
import myseries.MySeries;
import myseries.series.Series;
import tools.options.Options;
import tools.MySeriesLogger;
import tools.options.Paths;

/**
 *
 * @author ssoldatos
 */
public class DatabaseActions {

    public static void createDatabase(MySeries m, boolean createNewDb) {
        try {
            String oldDb = Options.toString(Options.DB_NAME);
            MySeries.glassPane.activate(null);
            MySeriesLogger.logger.log(Level.INFO, "Showing createDatabase panel");
            String db = null;
            db = MyMessages.ask("Create Database", "Type the name of the database");
            if (db != null) {
                if (db.endsWith(Database.EXT)) {
                    db = db.substring(0, db.length() - 3);
                }
            }
            CreateDatabase c = new CreateDatabase(null, db);
            c.run();
            doLoad(m, c.db, oldDb, true);
            MySeries.glassPane.deactivate();
        } catch (Exception ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Error while creating DB", ex);
        }
    }

    public static void loadDatabase(MySeries m) {
        String oldDb = Options.toString(Options.DB_NAME);
        try {
            MySeries.glassPane.activate(null);
            String[] filter = {".db"};
            MySeriesLogger.logger.log(Level.INFO, "Loading database");
            String load = MyUsefulFunctions.getSelectedFile(Paths.DATABASES_PATH, filter, "Load Database", "Select the database to load");
            doLoad(m, load, oldDb, false);
        }  catch (Exception ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Error while loading DB", ex);
        }   finally {
            MySeries.glassPane.deactivate();

        }
    }

    private static void doLoad(MySeries m, String load, String oldDb, boolean newDatabase) throws SQLException {
        MyEvent ev;
        DBConnection conn;
        ArrayList<SeriesRecord> series;
        if (load != null && !load.equals("null") && !load.equals(Options.toString(Options.DB_NAME))) {
            MySeriesLogger.logger.log(Level.INFO, "Database to load : {0}", load);
            conn = new DBConnection(load, newDatabase);
            if (DBConnection.checkDatabase()) {
                MyEventsClass evClass = new MyEventsClass(m);
                ev = new MyEvent(m, MyEventHandler.SERIES_UPDATE);
                evClass.fireMyEvent(ev);
                ev.setType(MyEventHandler.SET_CURRENT_SERIES);
                series = Series.getSeries(false);
                ev.setSeries(!series.isEmpty()? series.get(0):null);
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
