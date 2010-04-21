/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.episodes;

import javax.swing.JButton;
import javax.swing.JLabel;
import tools.options.Options;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import database.EpisodesRecord;
import database.SeriesRecord;

/**
 * Cretates the next episodes object
 * @author lordovol
 */
public class NextEpisodes {

  /**
   * An arraylist containing all the next episodes
   */
  public static ArrayList<String> nextEpisodes = new ArrayList<String>();
  /**
   * The current next episode ID
   */
  private static int currentEpisode_id = -1;
  /**
   * The state of the first,previous,next,last buttons
   */
  public static Boolean[] buttons = new Boolean[4];
  /**
   * The first episode button : 0
   */
  public static final int FIRST_BUTTON  = 0;
  /**
   * The previous episode button : 1
   */
  public static final int PREVIOUS_BUTTON  = 1;
  /**
   * The next episode button : 2
   */
  public static final int NEXT_BUTTON  = 2;
  /**
   * The last episode button : 3
   */
  public static final int LAST_BUTTON  = 3;
  /**
   * The current next episode title
   */
  public static String currentepisodeTitle = "";
  /**
   * The next episodes label
   */
  public static JLabel label_NextEpisode;
  /**
   * The first,previous,next,last buttons
   */
  public static JButton button_first,  button_last,  button_previous,  button_next;

  /**
   * Creates the next episodes
   * @throws java.sql.SQLException
   */
  public static void createNextEpisodes() throws SQLException {
    String where = "";
    nextEpisodes.clear();
    buttons[FIRST_BUTTON] = false;
    buttons[PREVIOUS_BUTTON] = false;
    buttons[NEXT_BUTTON] = false;
    buttons[LAST_BUTTON] = false;
    if (Options.toBoolean(Options.SHOW_UNSEEN)) {
      where = " OR seen = "+EpisodesRecord.NOT_SEEN;
    } 
    if (Options.toBoolean(Options.SHOW_UNDOWNLOADED)) {
      where += " OR (downloaded = "+EpisodesRecord.NOT_DOWNLOADED +" AND seen = " + EpisodesRecord.NOT_SEEN + ")";
    }
    String sql = "SELECT e.title AS epTitle, e.aired AS aired, s.title AS serial FROM episodes e " +
        "LEFT JOIN series s ON e.series_ID = s.series_ID WHERE (aired >= current_date " + where +
        ") AND downloaded = "+EpisodesRecord.NOT_DOWNLOADED+" AND hidden = "+SeriesRecord.NOT_HIDDEN+" AND aired <> '' AND substr(aired,6) <> '00-00' ORDER BY aired ASC LIMIT " + Options.toInt("NEXT_EPISODES_LIMIT");
    ResultSet rs = EpisodesRecord.query(sql);
    while (rs.next()) {
      String aired = myComponents.MyUsefulFunctions.convertDateForRendering(rs.getString("aired"));
      nextEpisodes.add(aired + " " + rs.getString("epTitle") + " (" + rs.getString("serial") + ")");
    }
    if (nextEpisodes.size() > 0) {
      currentEpisode_id = 0;
    }
    rs.close();
    if (nextEpisodes.size() > 1) {
      buttons[NEXT_BUTTON] = true;
      buttons[LAST_BUTTON] = true;
    }
  }

  /**
   * Get the title of the current episode
   * @return
   */
  public static String getCurrentTitle() {
    return nextEpisodes.get(currentEpisode_id);
  }

  /**
   * @return the nextEpisode_id
   */
  public static int getNextEpisode_id() {
    return currentEpisode_id;
  }

  /**
   * Sets the next episode and the buttons state
   * @param nextEpisode_id the nextEpisode_id to set
   */
  public static void setNextEpisode_id(int nextEpisode_id) {
    currentEpisode_id = nextEpisode_id;
    buttons[FIRST_BUTTON] = true;
    buttons[PREVIOUS_BUTTON] = true;
    buttons[NEXT_BUTTON] = true;
    buttons[LAST_BUTTON] = true;
    if (nextEpisode_id == 0) {
      buttons[FIRST_BUTTON] = false;
      buttons[PREVIOUS_BUTTON] = false;
    }
    if (nextEpisode_id == nextEpisodes.size() - 1) {
      buttons[NEXT_BUTTON] = false;
      buttons[LAST_BUTTON] = false;
    }
  }

  /**
   * Show the next episode and update the buttons state
   */
  public static void show() {
    if (NextEpisodes.nextEpisodes.size() > 0) {
      label_NextEpisode.setText(NextEpisodes.getCurrentTitle());
      button_first.setEnabled(NextEpisodes.buttons[FIRST_BUTTON]);
      button_previous.setEnabled(NextEpisodes.buttons[PREVIOUS_BUTTON]);
      button_next.setEnabled(NextEpisodes.buttons[NEXT_BUTTON]);
      button_last.setEnabled(NextEpisodes.buttons[LAST_BUTTON]);
    }
  }

  /**
   * Update the label depending of the button clicked
   * @param button The button clicked
   */
  public static void showNextEpisodes(int button) {
    if (button == FIRST_BUTTON) {
      NextEpisodes.setNextEpisode_id(0);
    } else if (button == LAST_BUTTON) {
      NextEpisodes.setNextEpisode_id(NextEpisodes.nextEpisodes.size() - 1);
    } else if (button == PREVIOUS_BUTTON) {
      NextEpisodes.setNextEpisode_id(NextEpisodes.getNextEpisode_id() - 1);
    } else if (button == NEXT_BUTTON) {
      NextEpisodes.setNextEpisode_id(NextEpisodes.getNextEpisode_id() + 1);
    }
    show();
  }

  public static void update() throws SQLException {
    createNextEpisodes();
    show();
  }

  private NextEpisodes() {
  }
}
