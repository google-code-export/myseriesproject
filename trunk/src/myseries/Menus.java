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
       String seriesTitle = series != null ? series.getFullTitle() : "";
       String episodeTitle = episode != null ? episode.getTitle() : "";
       String localDir = series != null ? series.getLocalDir() : "";
      //ADD EPISODE
      MySeries.PopUpItem_AddEpisodeInEpisodes.setEnabled(singleEpisode);
      MySeries.PopUpItem_AddEpisodeInEpisodes.setText("Add new episode to " + seriesTitle);

      //DELETE EPISODE
      MySeries.popUpItem_deleteEpisode.setEnabled(episodesPanel);
      MySeries.popUpItem_deleteEpisode.setText(singleEpisode ? 
        "Delete episode " + episodeTitle :
        "Delete selected episodes");

      //VIEW EPISODE
      MySeries.popUpItem_viewEpisode.setEnabled(!localDir.equals("")&&singleEpisode);
      MySeries.popUpItem_viewEpisode.setText("View episode " + episodeTitle);

      //RENAME EPISODE
      MySeries.popUpItem_renameEpisode.setEnabled(!localDir.equals("")&&singleEpisode);
      MySeries.popUpItem_renameEpisode.setText("Rename episode " + episodeTitle);

      //DOWNLOAD SUBS
      MySeries.popUpMenu_downloadSubtitles.setEnabled(singleEpisode);
      MySeries.popUpMenu_downloadSubtitles.setText("Download subtitles for " + episodeTitle);
      MySeries.popUpItem_downloadSubsTvSubs.setEnabled(singleEpisode);
      MySeries.popUpItem_downloadSubsSubOn.setEnabled(singleEpisode);

      //DOWNLOAD TORRENT
      MySeries.popUpMenu_downloadTorrent.setEnabled(singleEpisode);
      MySeries.popUpMenu_downloadTorrent.setText("Download torrent for " + episodeTitle);
      MySeries.popUpItem_downloadEzTv.setEnabled(singleEpisode);
      MySeries.popUpItem_downloadIsohunt.setEnabled(singleEpisode);
      

    

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
