/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.actions;

import database.DBConnection;
import database.SaveDatabase;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.UnsupportedLookAndFeelException;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myseries.MySeries;
import myseries.StartPanel;
import tools.options.Options;
import tools.MySeriesLogger;

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
        try {
            MySeries.glassPane.activate(null);
            String[] filter = {".db"};
            MySeriesLogger.logger.log(Level.INFO, "Loading database");
            String load = MyUsefulFunctions.getSelectedFile(database.Database.PATH, filter, "Load Database", "Select the database to load");
            if (load != null && !load.equals("null") && !load.equals(Options.toString(Options.DB_NAME))) {
              MySeriesLogger.logger.log(Level.INFO, "Database to load : {0}",load);
                if (DBConnection.checkDatabase(load)) {
                    Options.setOption(Options.DB_NAME, load);
                    Options.save();
                    m.dispose();
                    new MySeries();
                } else {
                    MySeriesLogger.logger.log(Level.WARNING, "Selected database is invlid.Not loading...");
                    MyMessages.error("Invalid Database", "The database you selected is invalid");
                    loadDatabase(m);
                }
            } else {
            }
        } catch (ClassNotFoundException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Database class not found", ex);
        } catch (SQLException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
        } catch (InstantiationException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Could not instantiate object", ex);
        } catch (IllegalAccessException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Illegal access exception", ex);
        } catch (UnsupportedLookAndFeelException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Look and feel not supported", ex);
        } catch (IOException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "I/O exception occured", ex);
        } finally {
            MySeries.glassPane.deactivate();
        }
    }

    public static void saveDatase() {
      MySeriesLogger.logger.log(Level.INFO, "Saving database action");
        new SaveDatabase();
    }
}
