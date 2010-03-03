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
    buttons[0] = false;
    buttons[1] = false;
    buttons[2] = false;
    buttons[3] = false;
    if (Options.toBoolean(Options.SHOW_UNSEEN)) {
      where = " OR seen = 0";
    } 
    if (Options.toBoolean(Options.SHOW_UNDOWNLOADED)) {
      where += " OR downloaded = 0";
    }
    String sql = "SELECT e.title AS epTitle, e.aired AS aired, s.title AS serial FROM episodes e " +
        "LEFT JOIN series s ON e.series_ID = s.series_ID WHERE (aired >= current_date " + where +
        ") AND hidden = 0 AND aired != '' AND substr(aired,6) <> '00-00' ORDER BY aired ASC LIMIT " + Options.toInt("NEXT_EPISODES_LIMIT");
    EpisodesRecord e = new EpisodesRecord();
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
      buttons[2] = true;
      buttons[3] = true;
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
    buttons[0] = true;
    buttons[1] = true;
    buttons[2] = true;
    buttons[3] = true;
    if (nextEpisode_id == 0) {
      buttons[0] = false;
      buttons[1] = false;
    }
    if (nextEpisode_id == nextEpisodes.size() - 1) {
      buttons[2] = false;
      buttons[3] = false;
    }
  }

  /**
   * Show the next episode and update the buttons state
   */
  public static void show() {
    if (NextEpisodes.nextEpisodes.size() > 0) {
      label_NextEpisode.setText(NextEpisodes.getCurrentTitle());
      button_first.setEnabled(NextEpisodes.buttons[0]);
      button_previous.setEnabled(NextEpisodes.buttons[1]);
      button_next.setEnabled(NextEpisodes.buttons[2]);
      button_last.setEnabled(NextEpisodes.buttons[3]);
    }
  }

  /**
   * Update the label depending of the button clicked
   * @param ep The button that was clicked
   */
  public static void showNextEpisodes(String ep) {
    if (ep.equals("first")) {
      NextEpisodes.setNextEpisode_id(0);
    } else if (ep.equals("last")) {
      NextEpisodes.setNextEpisode_id(NextEpisodes.nextEpisodes.size() - 1);
    } else if (ep.equals("previous")) {
      NextEpisodes.setNextEpisode_id(NextEpisodes.getNextEpisode_id() - 1);
    } else if (ep.equals("next")) {
      NextEpisodes.setNextEpisode_id(NextEpisodes.getNextEpisode_id() + 1);
    }
    show();
  }

  private NextEpisodes() {
  }
}
