/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myEvents;

import database.EpisodesRecord;
import database.SeriesRecord;
import java.awt.Image;
import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import myComponents.myGUI.MyImagePanel;
import myseries.Menus;
import myseries.MySeries;
import myseries.episodes.Episodes;
import myseries.series.Series;
import tools.options.Options;

/**
 *
 * @author ssoldatos
 */
public class MyEventHandler implements MyEventListener {

  public static final int SERIES_UPDATE = 0;
  public static final int EPISODES_UPDATE = 1;
  public static final int SET_CURRENT_SERIES = 2;
  public static final int SET_CURRENT_EPISODE = 3;

  @Override
  public void myEventOccured(MyEvent evt) {
    try {
      //Add event responses here
      if (evt.getType() == SERIES_UPDATE) {
        Series.updateSeriesTable(false);
      } else if (evt.getType() == SET_CURRENT_SERIES) {
        SeriesRecord series = evt.getSeries();
        if (series == null) {
          series = new SeriesRecord();
        }
        Series.setCurrentSerial(series);

        //TABS
        MySeries.tabsPanel.setTitleAt(MySeries.tabsPanel.getIndexByName(MySeries.TABS_PANEL_EPISODES), series.getFullTitle());
        if (evt.isSeriesPanel()) {
          MySeries.tabsPanel.setSelectedIndex(0);
        }
        //IMAGE SCREENSHOT
        String imagePath = Options._USER_DIR_ + MyImagePanel.SCREENSHOTS_PATH + "/" + series.getScreenshot();
        if (new File(imagePath).isFile()) {
          Image im = new ImageIcon(imagePath).getImage();
          MySeries.imagePanel.setImage(im, false);
        } else {
          Image image = new ImageIcon(getClass().getResource(MyImagePanel.LOGO)).getImage();
          MySeries.imagePanel.setImage(image, true);
        }

        Episodes.updateEpisodesTable();
        int row = MySeries.getSeriesTableRow(series);
        if (row > -1) {
          MySeries.tableSeries.setRowSelectionInterval(row, row);
        } else {
          if(MySeries.tableSeries.getRowCount()>0){
          MySeries.tableSeries.removeRowSelectionInterval(0, MySeries.tableSeries.getRowCount() - 1);
          }
        }
        Menus.setSeriesMenus(series);
      } else if (evt.getType() == SET_CURRENT_EPISODE) {
        if (!evt.isSingleEpisode()) {
        } else {
          EpisodesRecord episode = evt.getEpisode();
          Episodes.setCurrentEpisode(episode.getEpisode());
        }
        Menus.setEpisodesPopup(evt.getSeries(), evt.getEpisode(), evt.isSingleEpisode(), evt.isEpisodesPanel());
      }
    } catch (SQLException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }
}
