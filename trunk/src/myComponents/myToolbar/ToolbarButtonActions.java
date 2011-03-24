/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myToolbar;

import java.awt.Dimension;

/**
 *
 * @author ssoldatos
 */
public interface ToolbarButtonActions {

  public static Dimension BUTTON_DIMENSION = new Dimension(32, 32);
  public static Dimension SEP_DIMENSION = new Dimension(11, 11);
  public static int NONE = -1;
  public static int CREATE_DB = 0;
  public static int LOAD_DB = 1;
  public static int SAVE_DB = 2;
  public static int SEP_DB = 3;
  public static int ADD_SERIES = 4;
  public static int EDIT_SERIES = 5;
  public static int DELETE_SERIES = 6;
  public static int RESTORE_SERIES = 7;
  public static int ADD_EPISODE = 8;
  public static int SEP_EDIT = 9;
  public static int EXPORT_EPISODES = 10;
  public static int IMPORT_EPISODES = 11;
  public static int EZTV_TORRENT = 12;
  public static int ISOHUNT_TORRENT = 13;
  public static int TVRAGE_UPDATE = 14;
  public static int EPGUIDES_UPDATE = 15;
  public static int UPDATE_FILES = 16;
  public static int DELETE_TORRENTS = 17;
  public static int UPDATE_FEEDS = 18;
  public static int OPTIONS = 19;
  public static int SEP_TOOLS = 20;
  public static int HELP = 21;
  public static int CHECK_UPDATES = 22;
  public static int VIEW_LOGS = 23;
  public static int CLEAR_LOGS = 24;
  public static int ABOUT = 25;
  public static int EXIT = 26;


}
