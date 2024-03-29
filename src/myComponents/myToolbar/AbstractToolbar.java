/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myToolbar;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.JToolBar;
import myseriesproject.MySeries;
import tools.MySeriesLogger;

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
  protected ToolbarButton recycleBin;
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
  protected ToolbarButton deleteTorrents;
  protected ToolbarButton updateFeeds;
  protected ToolbarButton exit;
  protected ToolbarButton houseKeeping;
  protected ToolbarButton restart;
  protected ToolbarSeperator sepMem;
  protected ToolbarSeperator memory;
  protected ToolbarButton news;
  public Integer[] visibleButtons;

  public AbstractToolbar() {
    super();
  }


  public AbstractToolbar(Integer[] visibleButtons) {
    super();
     if(visibleButtons==null){
      visibleButtons = MySeries.options.getDefaultToolbarButtons();
    }
    this.visibleButtons = visibleButtons;
  }



  public void createButtons() {
      MySeriesLogger.logger.log(Level.INFO, "Creating toolbar buttons");
      createDb = new ToolbarButton(ToolbarButtonActions.CREATE_DB, "Add Database", "database.png");
      loadDb = new ToolbarButton(ToolbarButtonActions.LOAD_DB, "Load Database", "load_database.png");
      saveDb = new ToolbarButton(ToolbarButtonActions.SAVE_DB, "Save Database As...", "database_save.png");
      sepDb = new ToolbarSeperator(ToolbarButtonActions.SEP_DB, "", "");
      addSeries = new ToolbarButton(ToolbarButtonActions.ADD_SERIES, "Add Series", "add_series.png");
      editSeries = new ToolbarButton(ToolbarButtonActions.EDIT_SERIES, "Edit Series", "edit_series.png");
      deleteSeries = new ToolbarButton(ToolbarButtonActions.DELETE_SERIES, "Delete Series", "delete_series.png");
      recycleBin = new ToolbarButton(ToolbarButtonActions.RECYCLE_BIN, "Recycle Bin", "recycle.png");
      addepisode = new ToolbarButton(ToolbarButtonActions.ADD_EPISODE, "Add episode", "add_episode.png");
      sepEdit = new ToolbarSeperator(ToolbarButtonActions.SEP_EDIT, "", "");
      exportEpisodes = new ToolbarButton(ToolbarButtonActions.EXPORT_EPISODES, "Export episodes", "export_episodes.png");
      importEpisodes = new ToolbarButton(ToolbarButtonActions.IMPORT_EPISODES, "Import episodes", "import_episodes.png");
      eztvTorrent = new ToolbarButton(ToolbarButtonActions.EZTV_TORRENT, "Download torrent from eztv", "eztv.png");
      isohuntTorrent = new ToolbarButton(ToolbarButtonActions.ISOHUNT_TORRENT, "Download torrent from isohunt", "isohunt.png");
      tvrageUpdate = new ToolbarButton(ToolbarButtonActions.TVRAGE_UPDATE, "Update episodes list from tvrage", "tvrage.png");
      epguidesUpdate = new ToolbarButton(ToolbarButtonActions.EPGUIDES_UPDATE, "Update episodes list from epguides", "epguides.png");
      updateFiles = new ToolbarButton(ToolbarButtonActions.UPDATE_FILES, "Update episodes download and subtitles statuses", "updateFiles.png");
      deleteTorrents = new ToolbarButton(ToolbarButtonActions.DELETE_TORRENTS, "Delete the downloaded torrents", "deleteTorrents.png");
      updateFeeds = new ToolbarButton(ToolbarButtonActions.UPDATE_FEEDS, "Update all rss feeds", "rss_refresh.png");
      options = new ToolbarButton(ToolbarButtonActions.OPTIONS, "Options", "options.png");
      sepTools = new ToolbarSeperator(ToolbarButtonActions.SEP_TOOLS, "", "");
      help = new ToolbarButton(ToolbarButtonActions.HELP, "Help", "help.png");
      checkUpdates = new ToolbarButton(ToolbarButtonActions.CHECK_UPDATES, "Check for program updates", "checkUpdates.png");
      viewLogs = new ToolbarButton(ToolbarButtonActions.VIEW_LOGS, "View the log file", "viewLogs.png");
      clearLogs = new ToolbarButton(ToolbarButtonActions.CLEAR_LOGS, "Clear the log files", "clearLogs.png");
      about = new ToolbarButton(ToolbarButtonActions.ABOUT, "About MySeries", "about.png");
      exit = new ToolbarButton(ToolbarButtonActions.EXIT, "Quit MySeries", "exit.png");
      houseKeeping = new ToolbarButton(ToolbarButtonActions.HOUSEKEEPING, "HouseKeeping", "housekeeping.png");
      restart = new ToolbarButton(ToolbarButtonActions.RESTART, "Restart", "restart.png");
      sepMem = new ToolbarSeperator(ToolbarButtonActions.SEP_MEM, "", "");
      memory = new ToolbarSeperator(ToolbarButtonActions.MEMORY, "Memory", "ram.png");
      news = new ToolbarButton(ToolbarButtonActions.NEWS, "Latest News", "news.png");
      MySeriesLogger.logger.log(Level.FINE, "All toolbar buttons created");
      //DEPRECATE BUTTONS
      clearLogs.setDeprecated(true);
      deleteTorrents.setDeprecated(true);
  }
  public void addButtons(){
      MySeriesLogger.logger.log(Level.INFO, "Adding buttons to map");
    buttons.put(ToolbarButtonActions.CREATE_DB, createDb);
    buttons.put(ToolbarButtonActions.LOAD_DB, loadDb);
    buttons.put(ToolbarButtonActions.SAVE_DB, saveDb);
    buttons.put(ToolbarButtonActions.SEP_DB, sepDb);
    buttons.put(ToolbarButtonActions.ADD_SERIES, addSeries);
    buttons.put(ToolbarButtonActions.EDIT_SERIES, editSeries);
    buttons.put(ToolbarButtonActions.DELETE_SERIES, deleteSeries);
    buttons.put(ToolbarButtonActions.RECYCLE_BIN, recycleBin);
    buttons.put(ToolbarButtonActions.ADD_EPISODE, addepisode);
    buttons.put(ToolbarButtonActions.SEP_EDIT, sepEdit);
    buttons.put(ToolbarButtonActions.EXPORT_EPISODES, exportEpisodes);
    buttons.put(ToolbarButtonActions.IMPORT_EPISODES, importEpisodes);
    buttons.put(ToolbarButtonActions.EZTV_TORRENT, eztvTorrent);
    buttons.put(ToolbarButtonActions.ISOHUNT_TORRENT, isohuntTorrent);
    buttons.put(ToolbarButtonActions.TVRAGE_UPDATE, tvrageUpdate);
    buttons.put(ToolbarButtonActions.EPGUIDES_UPDATE, epguidesUpdate);
    buttons.put(ToolbarButtonActions.UPDATE_FILES, updateFiles);
    buttons.put(ToolbarButtonActions.DELETE_TORRENTS, deleteTorrents);
    buttons.put(ToolbarButtonActions.UPDATE_FEEDS, updateFeeds);
    buttons.put(ToolbarButtonActions.OPTIONS, options);
    buttons.put(ToolbarButtonActions.SEP_TOOLS, sepTools);
    buttons.put(ToolbarButtonActions.HELP, help);
    buttons.put(ToolbarButtonActions.CHECK_UPDATES, checkUpdates);
    buttons.put(ToolbarButtonActions.VIEW_LOGS, viewLogs);
    buttons.put(ToolbarButtonActions.CLEAR_LOGS, clearLogs);
    buttons.put(ToolbarButtonActions.ABOUT, about);
    buttons.put(ToolbarButtonActions.HOUSEKEEPING, houseKeeping);
    buttons.put(ToolbarButtonActions.RESTART, restart);
    buttons.put(ToolbarButtonActions.EXIT, exit);
    buttons.put(ToolbarButtonActions.SEP_MEM, sepMem);
    buttons.put(ToolbarButtonActions.MEMORY, memory);
    buttons.put(ToolbarButtonActions.NEWS, news);
      MySeriesLogger.logger.log(Level.FINE, "All buttons added");
    populateToolbar();
  }

  protected  abstract void populateToolbar() ;
}
