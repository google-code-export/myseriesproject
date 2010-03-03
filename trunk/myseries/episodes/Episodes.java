/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.episodes;

import myseries.series.Series;
import database.DBConnection;
import database.EpisodesRecord;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import myComponents.MyEpisodesTableModel;

/**
 *
 * @author ssoldatos
 */
public class Episodes {

  /**
   * The episodes table model
   */
  private static MyEpisodesTableModel tableModel_episodes;
  /**
   * The episodes table
   */
  private static JTable table_episodesList = new JTable();
  /**
   * The tabbed pane
   */
  private static JTabbedPane tabsPanel = new JTabbedPane();
  /**
   * The current episode
   */
  private static EpisodesRecord currentEpisode;

  
  /**
   * @return the tableModel_episodes
   */
  public static MyEpisodesTableModel getTableModel_episodes() {
    return tableModel_episodes;
  }

  /**
   * @param aTableModel_episodes the tableModel_episodes to set
   */
  public static void setTableModel_episodes(MyEpisodesTableModel aTableModel_episodes) {
    tableModel_episodes = aTableModel_episodes;
  }

  /**
   * @return the table_episodes
   */
  public static JTable getTable_episodes() {
    return table_episodesList;
  }

  /**
   * @param aTable_episodes the table_episodes to set
   */
  public static void setTable_episodes(JTable aTable_episodes) {
    table_episodesList = aTable_episodes;
  }


  private Episodes() {
  }

  /**
   * Sets the current episode
   * @param episode The episode id
   * @throws java.sql.SQLException
   */
  public static void setCurrentEpisode(int episode) throws SQLException {
      String sql = "SELECT * FROM episodes " +
              "WHERE series_ID = "+ Series.getCurrentSerial().getSeries_ID() +" AND episode = " + episode;
      ResultSet rs = EpisodesRecord.query(sql);
      if (rs.next()) {
        currentEpisode = new EpisodesRecord();
        getCurrentEpisode().setEpisode_ID(rs.getInt("episode_ID"));
        getCurrentEpisode().setEpisode(rs.getInt("episode"));
        getCurrentEpisode().setTitle(rs.getString("title").trim());
        getCurrentEpisode().setAired(rs.getString("aired").trim());
        getCurrentEpisode().setDownloaded(rs.getInt("downloaded"));
        getCurrentEpisode().setSubs(rs.getInt("subs"));
        getCurrentEpisode().setSeen(rs.getInt("seen"));

      }
      rs.close();
    }

  /**
   * Gets all the episodes of the current series
   * First empty episodes, create the model and apply it to the episodes table
   * Then prints the series fulltitle in the tabbed panel
   * @return An arraylist of all the episodes records
   * @throws java.sql.SQLException
   */
  public static ArrayList<EpisodesRecord> getCurrentSeriesEpisodes() throws SQLException {
    ArrayList<EpisodesRecord> eps = new ArrayList<EpisodesRecord>();
    int id, subsInt, episode;
    Boolean download, seen;
    String title, aired, subs;

    emptyEpisodes();
    if(Series.getCurrentSerial() == null){

    }
    String sql = "SELECT * FROM episodes WHERE series_ID = " + Series.getCurrentSerial().getSeries_ID() + 
            " ORDER BY CAST(episode AS UNSIGNED) ASC";
    ResultSet rs = DBConnection.stmt.executeQuery(sql);
    while (rs.next()) {
      EpisodesRecord e = new EpisodesRecord();
      e.setEpisode_ID(rs.getInt("episode_ID"));
      e.setTitle(rs.getString("title"));
      aired = rs.getString("aired");
      e.setAired(rs.getString("aired"));
      episode = rs.getInt("episode");
      e.setEpisode(rs.getInt("episode"));
      download = rs.getBoolean("downloaded");
      e.setDownloaded(rs.getInt("downloaded"));
      e.setSubs(rs.getInt("subs"));
      subs = e.getSubs() == 0 ? "None" : e.getSubs() == 1 ? "English" : e.getSubs() == 2 ? "Greek" : "Both";
      seen = rs.getBoolean("seen");
      Object[] data = {episode, e.getTitle(), e.getAired(), download, subs, seen, e.getEpisode_ID()};
      getTableModel_episodes().addRow(data);
      eps.add(e);
    }
    rs.close();

    table_episodesList.setModel(getTableModel_episodes());
    getTabsPanel().setTitleAt(0, Series.getCurrentSerial().getFullTitle());
    return eps;
  }

  /**
   * Updates the episodes table with the current series episodes
   * @throws java.sql.SQLException
   */
  public static void updateEpisodesTable() throws SQLException {
    Episodes.setTableModel_episodes(tableModel_episodes);
    Episodes.setTabsPanel(tabsPanel);
    Episodes.getCurrentSeriesEpisodes();
    tableModel_episodes = Episodes.getTableModel_episodes();
    tabsPanel = Episodes.getTabsPanel();
  }

  /**
   * Empty the episodes table and sets the tabbed pane title to empty string
   */
  public static void emptyEpisodes() {
    getTableModel_episodes().setRowCount(0);
    getTabsPanel().setTitleAt(0, "");

  }

  /**
   * @return the tabsPanel
   */
  public static JTabbedPane getTabsPanel() {
    return tabsPanel;
  }

  /**
   * @param tabsPanel the tabsPanel to set
   */
  public static void setTabsPanel(JTabbedPane tabsPanel) {
    Episodes.tabsPanel = tabsPanel;
  }

  /**
   * @return the currentEpisode
   */
  public static EpisodesRecord getCurrentEpisode() {
    return currentEpisode;
  }
}
