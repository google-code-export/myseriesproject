/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myToolbar;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JToolBar;
import tools.options.Options;

/**
 *
 * @author ssoldatos
 */
public abstract class AbstractToolbar extends JToolBar implements ToolbarButtonActions{

  protected Map<Integer, Component> buttons = new HashMap<Integer, Component>();
  protected ToolbarButton createDb;
  protected ToolbarButton loadDb;
  protected ToolbarButton saveDb;
  protected ToolbarSeperator sepDb;
  protected ToolbarButton addSeries;
  protected ToolbarButton editSeries;
  protected ToolbarButton deleteSeries;
  protected ToolbarButton restoreSeries;
  protected ToolbarButton addepisode;
  protected ToolbarSeperator sepEdit;
  protected ToolbarButton exportEpisodes;
  protected ToolbarButton importEpisodes;
  protected ToolbarButton eztvTorrent;
  protected ToolbarButton isohuntTorrent;
  protected ToolbarButton tvrageUpdate;
  protected ToolbarButton epguidesUpdate;
  protected ToolbarButton updateFiles;
  protected ToolbarButton options;
  protected ToolbarSeperator sepTools;
  protected ToolbarButton help;
  protected ToolbarButton checkUpdates;
  protected ToolbarButton viewLogs;
  protected ToolbarButton clearLogs;
  protected ToolbarButton about;
  protected Integer[] visibleButtons;

  public AbstractToolbar() {
    super();
  }


  public AbstractToolbar(Integer[] visibleButtons) {
    super();
     if(visibleButtons==null){
      visibleButtons = Options.getDefaultToolbarButtons();
    }
    this.visibleButtons = visibleButtons;
  }



  public void createButtons() {
      createDb = new ToolbarButton(ToolbarButtonActions.CREATE_DB, "Add Database", "database.png");
      loadDb = new ToolbarButton(ToolbarButtonActions.LOAD_DB, "Load Database", "load_database.png");
      saveDb = new ToolbarButton(ToolbarButtonActions.SAVE_DB, "Save Database As...", "database_save.png");
      sepDb = new ToolbarSeperator(ToolbarButtonActions.SEP_DB, "", "");
      addSeries = new ToolbarButton(ToolbarButtonActions.ADD_SERIES, "Add Series", "add_series.png");
      editSeries = new ToolbarButton(ToolbarButtonActions.EDIT_SERIES, "Edit Series", "edit_series.png");
      deleteSeries = new ToolbarButton(ToolbarButtonActions.DELETE_SERIES, "Delete Series", "delete_series.png");
      restoreSeries = new ToolbarButton(ToolbarButtonActions.RESTORE_SERIES, "Restore Series", "restore.png");
      addepisode = new ToolbarButton(ToolbarButtonActions.ADD_EPISODE, "Add episode", "add_episode.png");
      sepEdit = new ToolbarSeperator(ToolbarButtonActions.SEP_EDIT, "", "");
      exportEpisodes = new ToolbarButton(ToolbarButtonActions.EXPORT_EPISODES, "Export episodes", "export_episodes.png");
      importEpisodes = new ToolbarButton(ToolbarButtonActions.IMPORT_EPISODES, "Import episodes", "import_episodes.png");
      eztvTorrent = new ToolbarButton(ToolbarButtonActions.EZTV_TORRENT, "Download torrent from eztv", "eztv.png");
      isohuntTorrent = new ToolbarButton(ToolbarButtonActions.ISOHUNT_TORRENT, "Download torrent from isohunt", "isohunt.png");
      tvrageUpdate = new ToolbarButton(ToolbarButtonActions.TVRAGE_UPDATE, "Update episodes list from tvrage", "tvrage.png");
      epguidesUpdate = new ToolbarButton(ToolbarButtonActions.EPGUIDES_UPDATE, "Update episodes list from epguides", "epguides.png");
      updateFiles = new ToolbarButton(ToolbarButtonActions.UPDATE_FILES, "Update episodes download and subtitles statuses", "updateFiles.png");
      options = new ToolbarButton(ToolbarButtonActions.OPTIONS, "Options", "options.png");
      sepTools = new ToolbarSeperator(ToolbarButtonActions.SEP_TOOLS, "", "");
      help = new ToolbarButton(ToolbarButtonActions.HELP, "Help", "help.png");
      checkUpdates = new ToolbarButton(ToolbarButtonActions.CHECK_UPDATES, "Check for program updates", "checkUpdates.png");
      viewLogs = new ToolbarButton(ToolbarButtonActions.VIEW_LOGS, "View the log file", "viewLogs.png");
      clearLogs = new ToolbarButton(ToolbarButtonActions.CLEAR_LOGS, "Clear the log files", "clearLogs.png");
      about = new ToolbarButton(ToolbarButtonActions.ABOUT, "About MySeries", "info.png");
  }
  public void addButtons(){
    buttons.put(ToolbarButtonActions.CREATE_DB, createDb);
    buttons.put(ToolbarButtonActions.LOAD_DB, loadDb);
    buttons.put(ToolbarButtonActions.SAVE_DB, saveDb);
    buttons.put(ToolbarButtonActions.SEP_DB, sepDb);
    buttons.put(ToolbarButtonActions.ADD_SERIES, addSeries);
    buttons.put(ToolbarButtonActions.EDIT_SERIES, editSeries);
    buttons.put(ToolbarButtonActions.DELETE_SERIES, deleteSeries);
    buttons.put(ToolbarButtonActions.RESTORE_SERIES, restoreSeries);
    buttons.put(ToolbarButtonActions.ADD_EPISODE, addepisode);
    buttons.put(ToolbarButtonActions.SEP_EDIT, sepEdit);
    buttons.put(ToolbarButtonActions.EXPORT_EPISODES, exportEpisodes);
    buttons.put(ToolbarButtonActions.IMPORT_EPISODES, importEpisodes);
    buttons.put(ToolbarButtonActions.EZTV_TORRENT, eztvTorrent);
    buttons.put(ToolbarButtonActions.ISOHUNT_TORRENT, isohuntTorrent);
    buttons.put(ToolbarButtonActions.TVRAGE_UPDATE, tvrageUpdate);
    buttons.put(ToolbarButtonActions.EPGUIDES_UPDATE, epguidesUpdate);
    buttons.put(ToolbarButtonActions.UPDATE_FILES, updateFiles);
    buttons.put(ToolbarButtonActions.OPTIONS, options);
    buttons.put(ToolbarButtonActions.SEP_TOOLS, sepTools);
    buttons.put(ToolbarButtonActions.HELP, help);
    buttons.put(ToolbarButtonActions.CHECK_UPDATES, checkUpdates);
    buttons.put(ToolbarButtonActions.VIEW_LOGS, viewLogs);
    buttons.put(ToolbarButtonActions.CLEAR_LOGS, clearLogs);
    buttons.put(ToolbarButtonActions.ABOUT, about);

    populateToolbar();
  }

  protected  abstract void populateToolbar() ;
}
