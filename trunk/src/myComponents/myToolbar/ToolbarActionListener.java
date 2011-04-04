/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myToolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import javax.accessibility.AccessibleContext;
import myseries.MySeries;
import myseries.actions.ApplicationActions;
import myseries.actions.DatabaseActions;
import myseries.actions.EpisodesActions;
import myseries.actions.FeedsActions;
import myseries.actions.SeriesActions;
import tools.MySeriesLogger;
import tools.download.torrents.TorrentConstants;
import tools.internetUpdate.InternetUpdate;

/**
 *
 * @author ssoldatos
 */
public class ToolbarActionListener implements ActionListener, ToolbarButtonActions {

    @Override
    public void actionPerformed(ActionEvent e) {
        Toolbar toolb;
        AccessibleContext c;
        if (e.getSource() instanceof ToolbarButton) {
            ToolbarButton tb = (ToolbarButton) e.getSource();
            if (Toolbar.customize) {
                return;
            }
            MySeries ms = null;
            if (tb.getTopLevelAncestor() instanceof MySeries) {
                ms = (MySeries) tb.getTopLevelAncestor();
            } else {
                toolb = (Toolbar) tb.getParent();
                ms = toolb.ms;
            }
            MySeriesLogger.logger.log(Level.INFO, "Toolbar button {0} pressed", tb.toString());
            switch (tb.getActionName()) {
                case CREATE_DB:
                    DatabaseActions.createDatabase(ms, true);
                    break;
                case LOAD_DB:
                    DatabaseActions.loadDatabase(ms);
                    break;
                case SAVE_DB:
                    DatabaseActions.saveDatase();
                    break;
                case ADD_SERIES:
                    SeriesActions.addSeries(ms);
                    break;
                case EDIT_SERIES:
                    SeriesActions.editSeries(ms);
                    break;
                case DELETE_SERIES:
                    SeriesActions.deleteSeries(ms);
                    break;
                case RESTORE_SERIES:
                    ApplicationActions.restoreSeries();
                    break;
                case ADD_EPISODE:
                    EpisodesActions.AddEpisode(ms);
                    break;
                case EXPORT_EPISODES:
                    EpisodesActions.exportEpisodes();
                    break;
                case IMPORT_EPISODES:
                    EpisodesActions.importEpisodes(ms);
                    break;
                case EZTV_TORRENT:
                    SeriesActions.downloadTorrent(TorrentConstants.EZTV_NAME);
                    break;
                case ISOHUNT_TORRENT:
                    SeriesActions.downloadTorrent(TorrentConstants.ISOHUNT_NAME);
                    break;
                case TVRAGE_UPDATE:
                    SeriesActions.internetUpdate(ms, InternetUpdate.TV_RAGE_NAME);
                    break;
                case EPGUIDES_UPDATE:
                    SeriesActions.internetUpdate(ms, InternetUpdate.EP_GUIDES_NAME);
                    break;
                case UPDATE_FILES:
                    SeriesActions.updateFiles(ms);
                    break;
                case DELETE_TORRENTS:
                    ApplicationActions.deleteTorrents();
                    break;
                case UPDATE_FEEDS:
                    FeedsActions.updateFeeds();
                    break;
                case OPTIONS:
                    ApplicationActions.showOptions(ms);
                    break;
                case HELP:
                    ApplicationActions.showHelp(ms);
                    break;
                case CHECK_UPDATES:
                    ApplicationActions.checkUpdates();
                    break;
                case VIEW_LOGS:
                    ApplicationActions.viewLog(ms);
                    break;
                case CLEAR_LOGS:
                    ApplicationActions.clearLogFiles();
                    break;
                case ABOUT:
                    ApplicationActions.about(ms);
                    break;
                case EXIT:
                    ApplicationActions.exitApplication(ms);
                    break;
            }
        }
    }
}
