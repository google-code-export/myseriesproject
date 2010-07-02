/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries;

import database.EpisodesRecord;
import database.SeriesRecord;
import tools.DesktopSupport;

/**
 *
 * @author ssoldatos
 */
public class Menus {

  public static void setSeriesMenus(SeriesRecord s) {
    MySeries.menuItem_editSeries.setEnabled(s != null);
    MySeries.menuItem_deleteSeries.setEnabled(s != null);
    MySeries.menuItem_editEpisode.setEnabled(s != null);
    MySeries.menuItem_exportEpisodes.setEnabled(s != null);
    MySeries.menuItem_editSeries.setText("Edit series " + s);
    MySeries.menuItem_deleteSeries.setText("Delete series " + s);
    MySeries.menuItem_editEpisode.setText("Add episode to " + s);

  }

  public static void setEpisodesPopup(SeriesRecord series, EpisodesRecord episode, boolean singleEpisode, boolean episodesPanel) {
    initEpisodesPopUp();
    if (!singleEpisode) {
      MySeries.popUpItem_deleteEpisode.setText("Delete selected episodes");
      MySeries.popUpItem_deleteEpisode.setEnabled(true);
    } else {
      MySeries.PopUpItem_AddEpisodeInEpisodes.setEnabled(true);
      MySeries.PopUpItem_AddEpisodeInEpisodes.setVisible(true);
      MySeries.popUpItem_deleteEpisode.setVisible(true);
      MySeries.popUpItem_viewEpisode.setEnabled(!series.getLocalDir().equals(""));
      MySeries.popUpItem_renameEpisode.setEnabled(!series.getLocalDir().equals(""));
      MySeries.popUpItem_renameEpisode.setText("Rename episode " + episode.getTitle());
      MySeries.popUpItem_viewEpisode.setText("View episode " + episode.getTitle());
      MySeries.PopUpItem_AddEpisodeInEpisodes.setText("Add new episode");
      if (episodesPanel) {
        MySeries.popUpItem_deleteEpisode.setEnabled(episode != null);
        MySeries.popUpItem_deleteEpisode.setText("Delete episode " + episode.getTitle());
      }
      MySeries.popUpMenu_downloadSubtitles.setEnabled(true);
      MySeries.popUpMenu_downloadSubtitles.setText("Download subtitles for " + episode.getTitle());
      MySeries.popUpItem_downloadSubsTvSubs.setEnabled(true);
      MySeries.popUpItem_downloadSubsSubOn.setEnabled(true);
      MySeries.popUpMenu_downloadTorrent.setText("Download torrent for " + episode.getTitle());
      MySeries.popUpItem_downloadEzTv.setEnabled(true);
      MySeries.popUpItem_downloadIsohunt.setEnabled(true);
      MySeries.popUpItem_viewEpisode.validate();
    }

  }

  private static void initEpisodesPopUp() {
    MySeries.PopUpItem_AddEpisodeInEpisodes.setText("Add episode");
    MySeries.PopUpItem_AddEpisodeInEpisodes.setEnabled(false);
    MySeries.popUpItem_deleteEpisode.setText("Delete episode");
    MySeries.popUpItem_deleteEpisode.setEnabled(false);
    MySeries.popUpItem_viewEpisode.setText("View episode");
    MySeries.popUpItem_viewEpisode.setEnabled(false);
    MySeries.popUpItem_renameEpisode.setText("Rename episode");
    MySeries.popUpItem_renameEpisode.setEnabled(false);
    MySeries.popUpMenu_downloadSubtitles.setText("Download subtitles");
    MySeries.popUpMenu_downloadSubtitles.setEnabled(false);
    MySeries.popUpItem_downloadSubsTvSubs.setText("Download subtitles from TvSubtitles");
    MySeries.popUpItem_downloadSubsTvSubs.setEnabled(false);
    MySeries.popUpItem_downloadSubsSubOn.setText("Download subtitles from SubtitleOnline");
    MySeries.popUpItem_downloadSubsSubOn.setEnabled(false);
    MySeries.popUpItem_downloadEzTv.setText("Download torrent from EzTv");
    MySeries.popUpItem_downloadIsohunt.setText("Download torrent from Isohunt");
  }

  private void setSeriesPopup(SeriesRecord series, boolean state) {
    String ser = series.getFullTitle();
    MySeries.PopUpItem_AddEpisode.setEnabled(state);
    MySeries.PopUpItem_DeleteSerial.setEnabled(state);
    MySeries.PopUpItem_EditSerial.setEnabled(state);
    MySeries.popUpItem_GoToTvSubs.setEnabled(state);
    MySeries.popUpItem_GoToSubOn.setEnabled(state);
    MySeries.popUpMenu_GoToSubtitles.setEnabled(state);
    MySeries.popUpItem_GoToLocalDir.setEnabled(state);
    MySeries.popUpItem_renameEpisodes.setEnabled(state);
    MySeries.menuItem_exportEpisodes.setEnabled(state);
    MySeries.popUpItem_exportEpisodes.setEnabled(state);
    MySeries.popUpItem_IUTvrage.setEnabled(state);
    MySeries.popUpMenu_internetUpdate.setEnabled(state);
    if (ser != null) {
      MySeries.PopUpItem_AddEpisode.setText("Add new episode to " + ser);
      MySeries.PopUpItem_DeleteSerial.setText("Delete series " + ser);
      MySeries.PopUpItem_EditSerial.setText("Edit series " + ser);
      MySeries.popUpMenu_GoToSubtitles.setText("Go to the subtitles page of  " + ser);
      MySeries.popUpItem_GoToTvSubs.setText("Go to TvSubtitle");
      MySeries.popUpItem_GoToSubOn.setText("Go to SubtitleOnline");
      MySeries.popUpItem_GoToLocalDir.setText("Open " + ser + " directory");
      MySeries.popUpItem_renameEpisodes.setText("Rename " + ser + " directory");
      MySeries.menuItem_exportEpisodes.setText("Export episodes of " + ser);
      MySeries.popUpItem_exportEpisodes.setText("Export episodes of " + ser);
      MySeries.popUpMenu_internetUpdate.setText("Update " + ser + " episodes list");
      if (!DesktopSupport.isDesktopSupport() || !DesktopSupport.isBrowseSupport()) {
        MySeries.popUpItem_GoToTvSubs.setEnabled(false);
      } else {
        if (series.getTvSubtitlesCode().equals("")) {
          MySeries.popUpItem_GoToTvSubs.setEnabled(false);
        }
        if (series.getSOnlineCode().equals("")) {
          MySeries.popUpItem_GoToSubOn.setEnabled(false);
        }
      }
      if (series.getLocalDir().equals("") || !DesktopSupport.isDesktopSupport()) {
        MySeries.popUpItem_GoToLocalDir.setEnabled(false);
      }
      if (series.getLocalDir().equals("")) {
        MySeries.popUpItem_renameEpisodes.setEnabled(false);
      }
      if (series.getInternetUpdate() == 0) {
        MySeries.popUpItem_IUTvrage.setEnabled(false);
      }

    } else {
      MySeries.PopUpItem_AddEpisode.setText("Add new episode");
      MySeries.PopUpItem_DeleteSerial.setText("Delete series");
      MySeries.PopUpItem_EditSerial.setText("Edit series");
      MySeries.popUpItem_GoToTvSubs.setText("Go to the subtitles page");
      MySeries.popUpItem_GoToLocalDir.setText("Open Directory");
      MySeries.popUpItem_renameEpisodes.setText("Rename episodes");
      MySeries.popUpItem_exportEpisodes.setText("Export episodes");
      MySeries.popUpItem_IUTvrage.setText("Update episodes list");
    }
  }

  private Menus() {
  }
}
