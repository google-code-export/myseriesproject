/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries;

import database.EpisodesRecord;
import database.SeriesRecord;
import java.awt.Component;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import myComponents.MyUsefulFunctions;
import myComponents.myToolbar.Toolbar;
import myComponents.myToolbar.ToolbarButton;
import myseries.episodes.Episodes;
import tools.DesktopSupport;
import tools.MySeriesLogger;

/**
 *
 * @author ssoldatos
 */
public class Menus {

    public static void setSeriesMenus(SeriesRecord s) {
        MySeriesLogger.logger.log(Level.INFO, "Enabling series menu");
        MySeries.menuItem_editSeries.setEnabled(s.getSeries_ID() != 0);
        MySeries.menuItem_deleteSeries.setEnabled(s.getSeries_ID() != 0);
        MySeries.menuItem_editEpisode.setEnabled(s.getSeries_ID() != 0);
        MySeries.menuItem_exportEpisodes.setEnabled(s.getSeries_ID() != 0);
        MySeries.menuItem_editSeries.setText("Edit series " + s);
        MySeries.menuItem_deleteSeries.setText("Delete series " + s);
        MySeries.menuItem_editEpisode.setText("Add episode to " + s);
        setSeriesPopup(s);
        setSeriesToolbar(s);
    }

    public static void setEpisodesPopup(SeriesRecord series, EpisodesRecord episode, boolean singleEpisode, boolean episodesPanel) {
        MySeriesLogger.logger.log(Level.INFO, "Enabling episodes popup");
        String seriesTitle = series != null ? series.getFullTitle() : "";
        String episodeTitle = episode != null ? episode.getTitle() : "";
        String localDir = series != null ? series.getLocalDir() : "";
        boolean hasBeenAired = false;
        if (singleEpisode) {
            hasBeenAired = MyUsefulFunctions.hasBeenAired(episode.getAired(), true);
        }
        //ADD EPISODE
        MySeries.PopUpItem_AddEpisodeInEpisodes.setEnabled(singleEpisode);
        MySeries.PopUpItem_AddEpisodeInEpisodes.setText("Add new episode to " + seriesTitle);

        //DELETE EPISODE
        MySeries.popUpItem_deleteEpisode.setEnabled(episodesPanel);
        MySeries.popUpItem_deleteEpisode.setText(singleEpisode
                ? "Delete episode " + episodeTitle
                : "Delete selected episodes");

        //VIEW EPISODE
        MySeries.popUpItem_viewEpisode.setEnabled(!localDir.equals("") && singleEpisode && Episodes.checkDownloads(series, episode));
        MySeries.popUpItem_viewEpisode.setText("View episode " + episodeTitle);

        //RENAME EPISODE
        MySeries.popUpItem_renameEpisode.setEnabled(!localDir.equals("") && singleEpisode && Episodes.checkDownloads(series, episode));
        MySeries.popUpItem_renameEpisode.setText("Rename episode " + episodeTitle);

        //DOWNLOAD SUBS
        MySeries.popUpMenu_downloadSubtitles.setEnabled(singleEpisode && hasBeenAired);
        MySeries.popUpMenu_downloadSubtitles.setText("Download subtitles for " + episodeTitle);
        MySeries.popUpItem_downloadSubsTvSubs.setEnabled(singleEpisode && hasBeenAired);
        MySeries.popUpItem_downloadSubsSubOn.setEnabled(singleEpisode && hasBeenAired);

        //DOWNLOAD TORRENT
        MySeries.popUpMenu_downloadTorrent.setEnabled(singleEpisode && hasBeenAired);
        MySeries.popUpMenu_downloadTorrent.setText("Download torrent for " + episodeTitle);
        MySeries.popUpItem_downloadEzTv.setEnabled(singleEpisode && hasBeenAired);
        MySeries.popUpItem_downloadIsohunt.setEnabled(singleEpisode && hasBeenAired);
    }

    private static void setSeriesPopup(SeriesRecord series) {
        MySeriesLogger.logger.log(Level.INFO, "Enabling series popup");
        String seriesTitle = series != null ? series.getFullTitle() : "";
        String localDir = series != null ? series.getLocalDir() : "";

        MySeries.PopUpItem_AddEpisode.setEnabled(series.getSeries_ID() != 0);
        MySeries.PopUpItem_DeleteSerial.setEnabled(series.getSeries_ID() != 0);
        MySeries.PopUpItem_EditSerial.setEnabled(series.getSeries_ID() != 0);
        MySeries.popUpItem_GoToTvSubs.setEnabled(series.getSeries_ID() != 0 && !series.getTvSubtitlesCode().equals("") && DesktopSupport.isBrowseSupport());
        MySeries.popUpItem_GoToSubOn.setEnabled(series.getSeries_ID() != 0 && !series.getSOnlineCode().equals("") && DesktopSupport.isBrowseSupport());
        MySeries.popUpMenu_GoToSubtitles.setEnabled(series.getSeries_ID() != 0);
        MySeries.popUpItem_GoToLocalDir.setEnabled(series.getSeries_ID() != 0 && !series.getLocalDir().equals("") && DesktopSupport.isDesktopSupport());
        MySeries.popUpItem_renameEpisodes.setEnabled(series.getSeries_ID() != 0 && !series.getLocalDir().equals(""));
        MySeries.popUpItem_exportEpisodes.setEnabled(series.getSeries_ID() != 0);
        MySeries.popUpItem_IUTvrage.setEnabled(series.getSeries_ID() != 0 && series.getTvrage_ID() > 0);
        MySeries.popUpMenu_internetUpdate.setEnabled(series.getSeries_ID() != 0);


        MySeries.PopUpItem_AddEpisode.setText("Add new episode to " + seriesTitle);
        MySeries.PopUpItem_DeleteSerial.setText("Delete series " + seriesTitle);
        MySeries.PopUpItem_EditSerial.setText("Edit series " + seriesTitle);
        MySeries.popUpMenu_GoToSubtitles.setText("Go to the subtitles page of  " + seriesTitle);
        MySeries.popUpItem_GoToTvSubs.setText("Go to TvSubtitle");
        MySeries.popUpItem_GoToSubOn.setText("Go to SubtitleOnline");
        MySeries.popUpItem_GoToLocalDir.setText("Open " + seriesTitle + " directory");
        MySeries.popUpItem_renameEpisodes.setText("Rename " + seriesTitle + " episodes");
        MySeries.popUpItem_exportEpisodes.setText("Export episodes of " + seriesTitle);
        MySeries.popUpMenu_internetUpdate.setText("Update " + seriesTitle + " episodes list");

    }

    private static void setSeriesToolbar(SeriesRecord s) {

        if (MySeries.myToolbar == null) {
            return;
        } else {
            MySeriesLogger.logger.log(Level.INFO, "Enabling toolbar menu");
            Toolbar toolbar = MySeries.myToolbar;
            Map<Integer, Component> buttons = toolbar.getButtons();
            Set<Integer> keys = buttons.keySet();
            for (Iterator<Integer> it = keys.iterator(); it.hasNext();) {
                Integer key = it.next();
                Component comp = buttons.get(key);
                if (comp instanceof ToolbarButton) {
                    ToolbarButton bt = (ToolbarButton) comp;
                    int action = bt.getActionName();
                    switch (action) {
                        case ToolbarButton.DELETE_SERIES:
                            bt.setEnabled(s.getSeries_ID() > 0);
                            break;
                        case ToolbarButton.EDIT_SERIES:
                            bt.setEnabled(s.getSeries_ID() > 0);
                            break;
                        case ToolbarButton.ADD_EPISODE:
                            bt.setEnabled(s.getSeries_ID() > 0);
                            break;
                        case ToolbarButton.EXPORT_EPISODES:
                            bt.setEnabled(s.getSeries_ID() > 0);
                            break;
                    }
                }
            }
        }
    }

    private Menus() {
    }
}
