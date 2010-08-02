/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries;

import database.EpisodesRecord;
import database.SeriesRecord;
import myComponents.myToolbar.Toolbar;
import myComponents.myToolbar.ToolbarButtonActions;
import tools.DesktopSupport;

/**
 *
 * @author ssoldatos
 */
public class Menus {

  public static void setSeriesMenus(SeriesRecord s) {
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
    String seriesTitle = series != null ? series.getFullTitle() : "";
    String episodeTitle = episode != null ? episode.getTitle() : "";
    String localDir = series != null ? series.getLocalDir() : "";
    //ADD EPISODE
    MySeries.PopUpItem_AddEpisodeInEpisodes.setEnabled(singleEpisode);
    MySeries.PopUpItem_AddEpisodeInEpisodes.setText("Add new episode to " + seriesTitle);

    //DELETE EPISODE
    MySeries.popUpItem_deleteEpisode.setEnabled(episodesPanel);
    MySeries.popUpItem_deleteEpisode.setText(singleEpisode
            ? "Delete episode " + episodeTitle
            : "Delete selected episodes");

    //VIEW EPISODE
    MySeries.popUpItem_viewEpisode.setEnabled(!localDir.equals("") && singleEpisode);
    MySeries.popUpItem_viewEpisode.setText("View episode " + episodeTitle);

    //RENAME EPISODE
    MySeries.popUpItem_renameEpisode.setEnabled(!localDir.equals("") && singleEpisode);
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

  private static void setSeriesPopup(SeriesRecord series) {
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
    if(MySeries.myToolbar==null){
      return;
    }
    if(MySeries.myToolbar.getComponentCount()>0){
    MySeries.myToolbar.getButtons().get(ToolbarButtonActions.EDIT_SERIES).setEnabled(s.getSeries_ID() != 0);
    MySeries.myToolbar.getButtons().get(ToolbarButtonActions.DELETE_SERIES).setEnabled(s.getSeries_ID() != 0);
    MySeries.myToolbar.getButtons().get(ToolbarButtonActions.ADD_EPISODE).setEnabled(s.getSeries_ID() != 0);
    MySeries.myToolbar.getButtons().get(ToolbarButtonActions.EXPORT_EPISODES).setEnabled(s.getSeries_ID() != 0);
    }
  }

  private Menus() {
  }
}
