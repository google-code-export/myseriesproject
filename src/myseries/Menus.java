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

    public static void setSeriesMenus(SeriesRecord s, MySeries m) {
        MySeriesLogger.logger.log(Level.INFO, "Enabling series menu");
        m.menuItem_editSeries.setEnabled(s.getSeries_ID() != 0);
        m.menuItem_deleteSeries.setEnabled(s.getSeries_ID() != 0);
        m.menuItem_editEpisode.setEnabled(s.getSeries_ID() != 0);
        m.menuItem_exportEpisodes.setEnabled(s.getSeries_ID() != 0);
        m.menuItem_editSeries.setText("Edit series " + s);
        m.menuItem_deleteSeries.setText("Delete series " + s);
        m.menuItem_editEpisode.setText("Add episode to " + s);
        setSeriesPopup(s, m);
        setSeriesToolbar(s, m);
    }

    public static void setEpisodesPopup(SeriesRecord series, EpisodesRecord episode,
        boolean singleEpisode, boolean episodesPanel, MySeries m) {
        MySeriesLogger.logger.log(Level.INFO, "Enabling episodes popup");
        String seriesTitle = series != null ? series.getFullTitle() : "";
        String episodeTitle = episode != null ? episode.getTitle() : "";
        String localDir = series != null ? series.getLocalDir() : "";
        boolean hasBeenAired = false;
        if (singleEpisode) {
            hasBeenAired = episode!=null ? MyUsefulFunctions.hasBeenAired(episode.getAired(), true):false;
        }
        //ADD EPISODE
        m.PopUpItem_AddEpisodeInEpisodes.setEnabled(singleEpisode);
        m.PopUpItem_AddEpisodeInEpisodes.setText("Add new episode to " + seriesTitle);

        //DELETE EPISODE
        m.popUpItem_deleteEpisode.setEnabled(episodesPanel && episode!=null);
        m.popUpItem_deleteEpisode.setText(singleEpisode
                ? "Delete episode " + episodeTitle
                : "Delete selected episodes");

        //VIEW EPISODE
        m.popUpItem_viewEpisode.setEnabled(episode!=null && !localDir.equals("") && singleEpisode && Episodes.checkDownloads(series, episode));
        m.popUpItem_viewEpisode.setText("View episode " + episodeTitle);

        //RENAME EPISODE
        m.popUpItem_renameEpisode.setEnabled(episode!=null && !localDir.equals("") && singleEpisode && Episodes.checkDownloads(series, episode));
        m.popUpItem_renameEpisode.setText("Rename episode " + episodeTitle);

        //DOWNLOAD SUBS
        m.popUpMenu_downloadSubtitles.setEnabled(singleEpisode && hasBeenAired);
        m.popUpMenu_downloadSubtitles.setText("Download subtitles for " + episodeTitle);
        m.popUpItem_downloadSubsTvSubs.setEnabled(singleEpisode && hasBeenAired);
        m.popUpItem_downloadSubsSubOn.setEnabled(singleEpisode && hasBeenAired);

        //DOWNLOAD TORRENT
        m.popUpMenu_downloadTorrent.setEnabled(singleEpisode && hasBeenAired);
        m.popUpMenu_downloadTorrent.setText("Download torrent for " + episodeTitle);
        m.popUpItem_downloadEzTv.setEnabled(singleEpisode && hasBeenAired);
        m.popUpItem_downloadIsohunt.setEnabled(singleEpisode && hasBeenAired);
    }

    private static void setSeriesPopup(SeriesRecord series, MySeries m) {
        MySeriesLogger.logger.log(Level.INFO, "Enabling series popup");
        String seriesTitle = series != null ? series.getFullTitle() : "";
        String localDir = series != null ? series.getLocalDir() : "";

        m.PopUpItem_AddEpisode.setEnabled(series.getSeries_ID() != 0);
        m.PopUpItem_DeleteSerial.setEnabled(series.getSeries_ID() != 0);
        m.PopUpItem_EditSerial.setEnabled(series.getSeries_ID() != 0);
        m.popUpItem_GoToTvSubs.setEnabled(series.getSeries_ID() != 0 && !series.getTvSubtitlesCode().equals("") && DesktopSupport.isBrowseSupport());
        m.popUpItem_GoToSubOn.setEnabled(series.getSeries_ID() != 0 && !series.getSOnlineCode().equals("") && DesktopSupport.isBrowseSupport());
        m.popUpMenu_GoToSubtitles.setEnabled(series.getSeries_ID() != 0);
        m.popUpItem_GoToLocalDir.setEnabled(series.getSeries_ID() != 0 && !series.getLocalDir().equals("") && DesktopSupport.isDesktopSupport());
        m.popUpItem_renameEpisodes.setEnabled(series.getSeries_ID() != 0 && !series.getLocalDir().equals(""));
        m.popUpItem_exportEpisodes.setEnabled(series.getSeries_ID() != 0);
        m.popUpItem_IUTvrage.setEnabled(series.getSeries_ID() != 0 && series.getTvrage_ID() > 0);
        m.popUpMenu_internetUpdate.setEnabled(series.getSeries_ID() != 0);


        m.PopUpItem_AddEpisode.setText("Add new episode to " + seriesTitle);
        m.PopUpItem_DeleteSerial.setText("Delete series " + seriesTitle);
        m.PopUpItem_EditSerial.setText("Edit series " + seriesTitle);
        m.popUpMenu_GoToSubtitles.setText("Go to the subtitles page of  " + seriesTitle);
        m.popUpItem_GoToTvSubs.setText("Go to TvSubtitle");
        m.popUpItem_GoToSubOn.setText("Go to SubtitleOnline");
        m.popUpItem_GoToLocalDir.setText("Open " + seriesTitle + " directory");
        m.popUpItem_renameEpisodes.setText("Rename " + seriesTitle + " episodes");
        m.popUpItem_exportEpisodes.setText("Export episodes of " + seriesTitle);
        m.popUpMenu_internetUpdate.setText("Update " + seriesTitle + " episodes list");

    }

    private static void setSeriesToolbar(SeriesRecord s, MySeries m) {

        if (m.myToolbar == null) {
            return;
        } else {
            MySeriesLogger.logger.log(Level.INFO, "Enabling toolbar menu");
            Toolbar toolbar = m.myToolbar;
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
