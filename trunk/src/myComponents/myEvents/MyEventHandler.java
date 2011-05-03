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
import javax.swing.ImageIcon;
import myComponents.myGUI.MyImagePanel;
import myseries.Menus;
import myseries.MySeries;
import myseries.episodes.Episodes;
import myseries.series.Series;
import tools.options.Options;
import tools.MySeriesLogger;
import tools.options.Paths;

/**
 *
 * @author ssoldatos
 */
public class MyEventHandler implements MyEventListener {

    public static final int SERIES_UPDATE = 0;
    public static final int EPISODES_UPDATE = 1;
    public static final int SET_CURRENT_SERIES = 2;
    public static final int SET_CURRENT_EPISODE = 3;
    private MySeries m;

    @Override
    public void myEventOccured(MyEvent evt, MySeries m) {
      this.m = m;
      System.gc();
        try {
            //Add event responses here
            if (evt.getType() == SERIES_UPDATE) {
                MySeriesLogger.logger.log(Level.INFO, "Series update event occured");
                Series.updateSeriesTable(m.tableSeries, false);
            } else if (evt.getType() == SET_CURRENT_SERIES) {
                MySeriesLogger.logger.log(Level.INFO, "Series select event occured");
                SeriesRecord series = evt.getSeries();
                if (series == null) {
                    MySeriesLogger.logger.log(Level.FINE, "New Series event");
                    series = new SeriesRecord();
                }
                Series.setCurrentSerial(series);

                //TABS
                MySeriesLogger.logger.log(Level.INFO, "Updating series tab");
                int epiIndex = m.tabsPanel.getIndexByName(String.valueOf(MySeries.TAB_EPISODES_ID));
                m.tabsPanel.setTitleAt(epiIndex, series.getFullTitle());
                //MySeries.tabsPanel.setTitleAt(0, series.getFullTitle());
                if (evt.isSeriesPanel()) {
                    MySeriesLogger.logger.log(Level.INFO, "Select the tab");
                    m.tabsPanel.setSelectedIndex(epiIndex);
                }
                //IMAGE SCREENSHOT
                MySeriesLogger.logger.log(Level.INFO, "Updating series screenshot");
                if (series.isValidScreenshot()) {
                   String imagePath = Options._USER_DIR_ + Paths.SCREENSHOTS_PATH + "/" + series.getScreenshot();
                    Image image = new ImageIcon(imagePath).getImage();
                    MySeriesLogger.logger.log(Level.FINE, "Screenshot exists. Setting screenshot to {0}", series.getScreenshot());
                    m.imagePanel.setImage(image, false, m);
                } else {
                    MySeriesLogger.logger.log(Level.FINE, "No screenshot. Setting screenshot to default image");
                    Image image = new ImageIcon(getClass().getResource(MyImagePanel.LOGO)).getImage();
                    m.imagePanel.setImage(image, true, m);
                }

                Episodes.updateEpisodesTable(m.tableEpisodes);
                int row = m.getSeriesTableRow(series);
                if (row > -1) {
                    m.tableSeries.setRowSelectionInterval(row, row);
                } else {
                    if (m.tableSeries.getRowCount() > 0) {
                        m.tableSeries.removeRowSelectionInterval(0, m.tableSeries.getRowCount() - 1);
                    }
                }
                Menus.setSeriesMenus(series, m);
                m.imagePanel.relocate(m);
            } else if (evt.getType() == SET_CURRENT_EPISODE) {
                MySeriesLogger.logger.log(Level.INFO, "Episode select event occured");
                if (!evt.isSingleEpisode()) {
                } else {
                    EpisodesRecord episode = evt.getEpisode();
                    if (episode == null) {
                    MySeriesLogger.logger.log(Level.FINE, "New Episode event");
                    episode = new EpisodesRecord();
                }
                    Episodes.setCurrentEpisode(episode.getEpisode());
                }
                Menus.setEpisodesPopup(evt.getSeries(), evt.getEpisode(), evt.isSingleEpisode(), evt.isEpisodesPanel(), m);
            }
        } catch (SQLException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, null, ex);
        }
    }
}
