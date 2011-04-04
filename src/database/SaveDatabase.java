/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myseries.MySeries;
import tools.options.Options;
import tools.MySeriesLogger;

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
            showSavePane();
        } catch (FileNotFoundException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Could not save database", ex);
            MyMessages.error("Database not saved!!!", "The database could not be saved (" + ex.getMessage() + ")");
        } catch (IOException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Could not save database", ex);
            MyMessages.error("Database not saved!!!", "The database could not be saved (" + ex.getMessage() + ")");
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
            String source = Options._USER_DIR_ + Database.PATH + dbName;
            String dest = Options._USER_DIR_ + Database.PATH + dbName + Database.BACK_UP_EXT;
            if (MyUsefulFunctions.copyfile(source, dest)) {
                MySeriesLogger.logger.log(Level.INFO, "Database backed up!!!");
                MyMessages.message("Database backed up", "A back up of the older database was taken");
                backUp = true;
                return;
            } else {
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SaveDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SaveDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean overwriteDatabase() {
        File db = new File(Options._USER_DIR_ + Database.PATH + name + Database.EXT);
        if (db.isFile()) {
            return MyMessages.question("File Exists",
                    "File already exists.\nOverwrite it?") == JOptionPane.NO_OPTION ? true : false;
        } else {
            return false;
        }
    }

    private void commitSave() {
        try {
            String source = Options._USER_DIR_ + Database.PATH + Options.toString(Options.DB_NAME);
            String dest = Options._USER_DIR_ + Database.PATH + name + Database.EXT;
            if (MyUsefulFunctions.copyfile(source, dest)) {
                MySeriesLogger.logger.log(Level.INFO, "Database saved");
                MyMessages.message("Database saved", "The database was saved with the name " + name + Database.EXT);
                return;
            } else {
            }
        } catch (FileNotFoundException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "The database could not be saved", ex);
            MyMessages.error("Database not saved!!!", "The database could not be saved (" + ex.getMessage() + ")");
        } catch (IOException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "The database could not be saved", ex);
            MyMessages.error("Database not saved!!!", "The database could not be saved (" + ex.getMessage() + ")");
        }

    }

    private void showSavePane() throws FileNotFoundException, IOException {
        name = MyUsefulFunctions.getInput("Save Database", "Save Database As ");
        if (name == null) {
            MyMessages.error("Save Database", "Save Database aborted");
            MySeriesLogger.logger.log(Level.INFO, "Save Database aborted");
        } else {
            if (!overwriteDatabase()) {
                if ((name).equals(Options.toString(Options.DB_NAME).replace(Database.EXT, ""))) {
                    MyMessages.error("Error", "Cannot save the database on itself!!!");
                    MySeriesLogger.logger.log(Level.WARNING, "Cannot save the database on itself!!!");
                } else {
                    commitSave();
                }
            } else {
            }
        }
    }
}
