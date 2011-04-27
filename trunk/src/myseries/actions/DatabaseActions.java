/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.actions;

import database.DBConnection;
import database.SaveDatabase;
import java.util.logging.Level;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myseries.MySeries;
import myseries.StartPanel;
import tools.options.Options;
import tools.MySeriesLogger;
import tools.options.Paths;

/**
 *
 * @author ssoldatos
 */
public class DatabaseActions {

  public static void createDatabase(MySeries m, boolean createNewDb) {
    MySeries.glassPane.activate(null);
    MySeriesLogger.logger.log(Level.INFO, "Showing createDatabase panel");
    new StartPanel(m, createNewDb);
  }

  public static void loadDatabase(MySeries m) {
    DBConnection conn = null;
    try {
      MySeries.glassPane.activate(null);
      String[] filter = {".db"};
      MySeriesLogger.logger.log(Level.INFO, "Loading database");
      String load = MyUsefulFunctions.getSelectedFile(Paths.DATABASES_PATH, filter, "Load Database", "Select the database to load");
      if (load != null && !load.equals("null") && !load.equals(Options.toString(Options.DB_NAME))) {
        MySeriesLogger.logger.log(Level.INFO, "Database to load : {0}", load);
        conn = new DBConnection(load);
        if (DBConnection.checkDatabase()) {
          
          ApplicationActions.restartApplication(m);
        } else {
          MySeriesLogger.logger.log(Level.WARNING, "Selected database is invlid.Not loading...");
          MyMessages.error("Invalid Database", "The database you selected is invalid");
          loadDatabase(m);
        }
      } else {
      }
    } finally {
      MySeries.glassPane.deactivate();
     
    }
  }

  public static void saveDatase() {
    MySeriesLogger.logger.log(Level.INFO, "Saving database action");
    new SaveDatabase();
  }
}
