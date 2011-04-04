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

/**
 *
 * @author ssoldatos
 */
public class DatabaseActions {

    public static void createDatabase(MySeries m, boolean createNewDb) {
        MySeries.glassPane.activate(null);
        new StartPanel(m, createNewDb);
    }

    public static void loadDatabase(MySeries m) {
        try {
            MySeries.glassPane.activate(null);
            String[] filter = {".db"};
            String load = MyUsefulFunctions.getSelectedFile(database.Database.PATH, filter, "Load Database", "Select the database to load");
            if (load != null && !load.equals("null") && !load.equals(Options.toString(Options.DB_NAME))) {
                if (DBConnection.checkDatabase(load)) {
                    Options.setOption(Options.DB_NAME, load);
                    Options.save();
                    m.dispose();
                    new MySeries();
                } else {
                    MyUsefulFunctions.log(Level.WARNING, "Selected database is invlid.Not loading...");
                    MyMessages.error("Invalid Database", "The database you selected is invalid");
                    loadDatabase(m);
                }
            } else {
            }
        } catch (ClassNotFoundException ex) {
            MyUsefulFunctions.log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            MyUsefulFunctions.log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            MyUsefulFunctions.log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            MyUsefulFunctions.log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            MyUsefulFunctions.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            MyUsefulFunctions.log(Level.SEVERE, null, ex);
        } finally {
            MySeries.glassPane.deactivate();
        }
    }

    public static void saveDatase() {
        new SaveDatabase();
    }
}
