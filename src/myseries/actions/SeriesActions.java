/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.actions;

import database.DBConnection;
import database.SeriesRecord;
import java.awt.Image;
import java.io.File;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import tools.MySeriesLogger;
import myComponents.myEvents.MyEvent;
import myComponents.myEvents.MyEventHandler;
import myComponents.myGUI.MyImagePanel;
import myseries.MySeries;
import myseries.episodes.Episodes;
import myseries.filters.Filters;
import myseries.series.AdminSeries;
import myseries.series.Series;
import tools.DesktopSupport;
import tools.download.subtitles.SubtitleConstants;
import tools.download.subtitles.tvsubtitles.TvSubtitlesForm;
import tools.download.torrents.TorrentConstants;
import tools.download.torrents.eztv.EzTvForm;
import tools.download.torrents.isohunt.IsohuntForm;
import tools.internetUpdate.InternetUpdate;
import tools.internetUpdate.tvrage.TrGetId;
import tools.options.Options;

/**
 *
 * @author ssoldatos
 */
public class SeriesActions {

  public static void editSeries(MySeries m) {
    try {
      MySeries.glassPane.activate(null);
      MySeriesLogger.logger.log(Level.INFO, "Showing edit series panel");
      AdminSeries a = new AdminSeries(m, Series.getCurrentSerial());
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
    }

  }

  public static void addSeries(MySeries m) {
    MySeries.glassPane.activate(null);
    try {
      MySeriesLogger.logger.log(Level.INFO, "Showing add series panel");
      AdminSeries a = new AdminSeries(m);
      MyEvent evt = new MyEvent(m, MyEventHandler.SET_CURRENT_SERIES);
      evt.setSeries(null);
      m.getEvClass().fireMyEvent(evt);
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
    }

  }

  public static void deleteSeries(MySeries m) {
    String title = Series.getCurrentSerial().getTitle();
    SeriesRecord series = Series.getCurrentSerial();
    int season = series.getSeason();
    int series_ID = series.getSeries_ID();
    String screenshot = series.getScreenshot();
    MySeriesLogger.logger.log(Level.INFO, "Deleting series {0}", series.getFullTitle());
    int answ = MyMessages.confirm("Delete Serial?", "Really delete the series " + title + " season " + season + "?", true);
    ArrayList<SeriesRecord> s;
    SeriesRecord ser;
    if (answ == JOptionPane.YES_OPTION) {
      try {
        String sql = "UPDATE series SET deleted = " + SeriesRecord.DELETED + " WHERE series_ID = " + series_ID;
        DBConnection.conn.createStatement().execute(sql);
        MySeriesLogger.logger.log(Level.INFO, "Series deleted");
        m.getEvClass().fireMyEvent(new MyEvent(m, MyEventHandler.SERIES_UPDATE));
        if (Series.getSize() > 0) {
          ser = Series.getSeries(false).get(0);
          MyEvent evt = new MyEvent(m, MyEventHandler.SET_CURRENT_SERIES);
          evt.setSeries(ser);
          m.getEvClass().fireMyEvent(evt);
        }
        Episodes.updateEpisodesTable(m.tableEpisodes);

      } catch (SQLException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
      } 
    } else {
      MySeriesLogger.logger.log(Level.INFO, "Delete series aborted by user");
    }
  }

  public static void goToSubtitlePage(MySeries m, String site) {
    try {
      if (!DesktopSupport.isBrowseSupport()) {
        MySeriesLogger.logger.log(Level.WARNING, "Browse is not supported in the current OS");
        MyMessages.error("Browse Error!!!", "Browse is not supported", true);
        return;
      }
      SeriesRecord series = Series.getCurrentSerial();
      MySeriesLogger.logger.log(Level.INFO, "Go to the subtitle page of series {0}", series);
      java.net.URI uri = null;
      if (site.equals(SubtitleConstants.TV_SUBTITLES_NAME)) {
        uri = new java.net.URI("http://www.tvsubtitles.net/tvshow-" + series.getTvSubtitlesCode() + ".html");
      } else if (site.equals(SubtitleConstants.SUBTITLE_ONLINE_NAME)) {
        uri = new java.net.URI("http://www.subtitleonline.com/" + series.getSOnlineCode() + "-season-" + Series.getCurrentSerial().getSeason() + "-subtitles.html");
      }
      MyUsefulFunctions.browse(uri);
    } catch (URISyntaxException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
    }
  }

  public static void goToLocalDir() {
    try {
      SeriesRecord series = Series.getCurrentSerial();
      File f = new File(series.getLocalDir());
      if (f.isDirectory()) {
        MySeriesLogger.logger.log(Level.INFO, "Opening local directory of series {0}", series.getFullTitle());
        DesktopSupport.getDesktop().open(f);
      } else {
        MySeriesLogger.logger.log(Level.WARNING, "{0} is not a directory", f.getCanonicalPath());
        MyMessages.error("Directory error", f.getCanonicalPath() + " is not a directory", true);
        return;
      }
    } catch (Exception ex) {
      MySeriesLogger.logger.log(Level.WARNING, "Browse is not supported in the current OS");
      MyMessages.error("Browse Error!!!", "Browse is not supported", true);
      return;
    }
  }

  public static void downloadTorrent(String site) {

    if (site.equals(TorrentConstants.EZTV_NAME)) {
      MySeriesLogger.logger.log(Level.INFO, "Showing download EzTv torrent panel");
      new EzTvForm();
    } else if (site.equals(TorrentConstants.ISOHUNT_NAME)) {
      MySeriesLogger.logger.log(Level.INFO, "Showing download Isohunt torrent panel");
      new IsohuntForm();
    }

  }

  public static void updateFiles(MySeries m) {
    MySeriesLogger.logger.log(Level.INFO, "Updating files");
    boolean update = Options.toBoolean(Options.AUTO_FILE_UPDATING);
    boolean unzip = Options.toBoolean(Options.AUTO_EXTRACT_ZIPS);
    Options.setOption(Options.AUTO_FILE_UPDATING, true);
    Options.setOption(Options.AUTO_EXTRACT_ZIPS, true);
    try {
      SeriesRecord origSeries = Series.getCurrentSerial();
      ArrayList<SeriesRecord> series = Series.getSeries(false);
      for (Iterator<SeriesRecord> it = series.iterator(); it.hasNext();) {
        SeriesRecord ser = it.next();
        if (ser.getSeries_ID() != origSeries.getSeries_ID()) {
          MyEvent evt = new MyEvent(m, MyEventHandler.SET_CURRENT_SERIES);
          evt.setSeries(ser);
          m.getEvClass().fireMyEvent(evt);
        }
      }
      MyEvent evt = new MyEvent(m, MyEventHandler.SET_CURRENT_SERIES);
      evt.setSeries(origSeries);
      m.getEvClass().fireMyEvent(evt);
      Filters.getFilteredSeries(m.comboBox_seen, m.comboBox_filterSubtitles, m.combobox_downloaded, m.tableFilters);
      MySeriesLogger.logger.log(Level.FINE, "Updating finished");
      MyMessages.message("Update finished", "Updating of series files finished.");

    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
    } finally {
      Options.setOption(Options.AUTO_FILE_UPDATING, update);
      Options.setOption(Options.AUTO_EXTRACT_ZIPS, unzip);
    }

  }

  public static void internetUpdate(MySeries m, String site) {
    MySeries.glassPane.activate(null);
    MySeriesLogger.logger.log(Level.INFO, "Showing internet update panel");
    new InternetUpdate(m, site);
  }

  public static void internetUpdateSeries(MySeries m, String site) {
    MySeries.glassPane.activate(null);
    MySeriesLogger.logger.log(Level.INFO, "Updating series from internet");
    SeriesRecord cSeries = Series.getCurrentSerial();
    if (site.equals(InternetUpdate.TV_RAGE_NAME) && cSeries.getTvrage_ID() == 0) {
      try {
        MySeriesLogger.logger.log(Level.INFO, "Showing get tvrage id panel");
        TrGetId g = new TrGetId(m, cSeries.getSeries_ID(), cSeries.getTitle(), true);
        cSeries.setTvrage_ID(g.tvRageID);
        cSeries.save();
      } catch (SQLException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
      }
    } else {
      MySeriesLogger.logger.log(Level.INFO, "Showing internet update panel");
      InternetUpdate iu = new InternetUpdate(m, cSeries, site);
    }
    MySeries.glassPane.deactivate();
  }

  public static void downloadSeasonSubtitles() {
    SeriesRecord series = Series.getCurrentSerial();
    MySeriesLogger.logger.log(Level.INFO, "Downloading whole season subtitles of {0}", series.getFullTitle());
    String code = series.getTvSubtitlesCode().trim();
    String lang = myseries.MySeries.languages.getPrimary().getCode();
    String link = SubtitleConstants.TV_SUBTITLES_URL + "download-"
        + code + "-" + lang + ".html";
    MySeriesLogger.logger.log(Level.INFO, "Showing Tvsubtitles panel");
    TvSubtitlesForm d = new TvSubtitlesForm(
        link,
        Series.getCurrentSerial().getSeason(),
        Series.getCurrentSerial().getLocalDir());
  }

  private SeriesActions() {
  }
}
