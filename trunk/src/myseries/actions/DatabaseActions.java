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
      String[] filter = {".db"};
      String load = MyUsefulFunctions.getSelectedFile(database.Database.PATH, filter, "Load Database", "Select the database to load");
      if (!load.equals("null") && !load.equals(Options.toString(Options.DB_NAME))) {
        if (DBConnection.checkDatabase(load)) {
          Options.setOption(Options.DB_NAME, load);
          Options.save();
          m.dispose();
          new MySeries();
        } else {
          MySeries.logger.log(Level.WARNING, "Selected database is invlid.Not loading...");
          MyMessages.error("Invalid Database", "The database you selected is invalid");
          loadDatabase(m);
        }
      } else {
      }
    } catch (ClassNotFoundException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (UnsupportedLookAndFeelException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }

  public static void saveDatase() {
    new SaveDatabase();
  }
}
