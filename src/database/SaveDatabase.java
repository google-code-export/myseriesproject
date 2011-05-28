/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import tools.options.Options;
import tools.MySeriesLogger;
import tools.options.Paths;

/**
 * Save database class
 * @author lordovol
 */
public class SaveDatabase {

    private String name;
    public boolean backUp;

    /**
     * Brings up the panel for saving a database
     */
    public SaveDatabase() {
        try {
            myseries.MySeries.glassPane.activate(null);
            MySeriesLogger.logger.log(Level.INFO, "Showing save database panel");
            showSavePane();
        } catch (FileNotFoundException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Could not save database", ex);
            MyMessages.error("Database not saved!!!", "The database could not be saved (" + ex.getMessage() + ")", true, true);
        } catch (IOException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Could not save database", ex);
            MyMessages.error("Database not saved!!!", "The database could not be saved (" + ex.getMessage() + ")", true, true);
        } finally {
            myseries.MySeries.glassPane.deactivate();
        }
    }

    /**
     * Saves a database
     * @param dbName The database name
     */
    public SaveDatabase(String dbName) {
        try {
            String source = Options._USER_DIR_ + Paths.DATABASES_PATH  + dbName;
            String dest = Options._USER_DIR_ + Paths.DATABASES_PATH  + dbName + Database.BACK_UP_EXT;
            MySeriesLogger.logger.log(Level.INFO, "Taking a backup of the database");
            if (MyUsefulFunctions.copyfile(source, dest)) {
                MySeriesLogger.logger.log(Level.INFO, "Database backed up!!!");
                MyMessages.message("Database backed up", "A back up of the older database was taken");
                backUp = true;
                return;
            } else {
            }
        } catch (FileNotFoundException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Database file was not found", ex);
        } catch (IOException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Could not read/write to database", ex);
        }
    }

    private boolean overwriteDatabase() {
        File db = new File(Options._USER_DIR_ + Paths.DATABASES_PATH  + name + Database.EXT);
        if (db.isFile()) {
          MySeriesLogger.logger.log(Level.INFO, "Overwrite database?");
            return MyMessages.confirm("File Exists",
                    "File already exists.\nOverwrite it?", true) == JOptionPane.NO_OPTION ? true : false;
        } else {
            return false;
        }
    }

    private void commitSave() {
        try {
            String source = Options._USER_DIR_ + Paths.DATABASES_PATH  + Options.toString(Options.DB_NAME);
            String dest = Options._USER_DIR_ + Paths.DATABASES_PATH  + name + Database.EXT;
            MySeriesLogger.logger.log(Level.INFO, "Saving database");
            if (MyUsefulFunctions.copyfile(source, dest)) {
                MySeriesLogger.logger.log(Level.INFO, "Database saved");
                MyMessages.message("Database saved", "The database was saved with the name " + name + Database.EXT);
                return;
            } else {
            }
        } catch (FileNotFoundException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "The database could not be saved", ex);
            MyMessages.error("Database not saved!!!", "The database could not be saved (" + ex.getMessage() + ")", true, true);
        } catch (IOException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "The database could not be saved", ex);
            MyMessages.error("Database not saved!!!", "The database could not be saved (" + ex.getMessage() + ")", true, true);
        }

    }

    private void showSavePane() throws FileNotFoundException, IOException {
        name = MyUsefulFunctions.getInput("Save Database", "Save Database As ");
        MySeriesLogger.logger.log(Level.INFO, "Saving database {0}",name);
        if (name == null) {
            MyMessages.error("Save Database", "Save Database aborted", true, true);
            MySeriesLogger.logger.log(Level.INFO, "Save Database aborted");
        } else {
            if (!overwriteDatabase()) {
                if ((name).equals(Options.toString(Options.DB_NAME).replace(Database.EXT, ""))) {
                    MyMessages.error("Error", "Cannot save the database on itself!!!", true, true);
                    MySeriesLogger.logger.log(Level.WARNING, "Cannot save the database on itself!!!");
                } else {
                    commitSave();
                }
            } else {
            }
        }
    }
}
