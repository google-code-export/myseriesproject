/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.actions;

import database.DBConnection;
import database.SeriesRecord;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import tools.MySeriesLogger;
import myComponents.myEvents.MyEvent;
import myComponents.myEvents.MyEventHandler;
import myComponents.myGUI.MyImagePanel;
import myseries.MySeries;
import myseries.episodes.Episodes;
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
      AdminSeries a = new AdminSeries(m, Series.getCurrentSerial());
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }

  }

  public static void addSeries(MySeries m) {
    MySeries.glassPane.activate(null);
    try {
      AdminSeries a = new AdminSeries(m);
      MyEvent evt = new MyEvent(m, MyEventHandler.SET_CURRENT_SERIES);
      evt.setSeries(null);
      m.getEvClass().fireMyEvent(evt);
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }

  }

  public static void deleteSeries(MySeries m) {
    String title = Series.getCurrentSerial().getTitle();
    int season = Series.getCurrentSerial().getSeason();
    int series_ID = Series.getCurrentSerial().getSeries_ID();
    String screenshot = Series.getCurrentSerial().getScreenshot();
    int answ = MyMessages.question("Delete Serial?", "Really delete the series " + title + " season " + season + "?");
    ArrayList<SeriesRecord> s;
    SeriesRecord ser;
    if (answ == 0) {
      try {
        String sql = "UPDATE series SET deleted = " + SeriesRecord.DELETED + " WHERE series_ID = " + series_ID;
        DBConnection.stmt.execute(sql);
        // sql = "DELETE FROM episodes WHERE series_ID = " + series_ID;
        // DBConnection.stmt.execute(sql);
        // File screenshotFile = new File(Options._USER_DIR_ + MyImagePanel.SCREENSHOTS_PATH  + screenshot);
        // if (screenshotFile.isFile()) {
        //   screenshotFile.delete();
        //   Image image = new ImageIcon(MySeries.class.getResource(MyImagePanel.LOGO)).getImage();
        //   m.imagePanel.setImage(image,true);
        // }
        Image image = new ImageIcon(MySeries.class.getResource(MyImagePanel.LOGO)).getImage();
        MySeries.imagePanel.setImage(image, true);
        m.getEvClass().fireMyEvent(new MyEvent(m, MyEventHandler.SERIES_UPDATE));
        if (Series.getSize() > 0) {
          ser = Series.getSeries(false).get(0);
          MyEvent evt = new MyEvent(m, MyEventHandler.SET_CURRENT_SERIES);
          evt.setSeries(ser);
          m.getEvClass().fireMyEvent(evt);
        }
        Episodes.updateEpisodesTable();
      } catch (SQLException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      }
    }
  }

  public static void goToSubtitlePage(MySeries m, String site) {
    try {
      if (!DesktopSupport.isBrowseSupport()) {
        MySeriesLogger.logger.log(Level.WARNING, "Browse is not supported in the current OS");
        MyMessages.error("Browse Error!!!", "Browse is not supported");
        return;
      }
      java.net.URI uri = null;
      if (site.equals(SubtitleConstants.TV_SUBTITLES_NAME)) {
        uri = new java.net.URI("http://www.tvsubtitles.net/tvshow-" + Series.getCurrentSerial().getTvSubtitlesCode() + ".html");
      } else if (site.equals(SubtitleConstants.SUBTITLE_ONLINE_NAME)) {
        uri = new java.net.URI("http://www.subtitleonline.com/" + Series.getCurrentSerial().getSOnlineCode() + "-season-" + Series.getCurrentSerial().getSeason() + "-subtitles.html");
      }
      MyUsefulFunctions.browse(uri);
    } catch (URISyntaxException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
  }

  public static void goToLocalDir() {
    try {
      File f = new File(Series.getCurrentSerial().getLocalDir());
      if (f.isDirectory()) {
        DesktopSupport.getDesktop().open(f);
      } else {
        MySeriesLogger.logger.log(Level.WARNING, f.getCanonicalPath() + " is not a directory");
        MyMessages.error("Directory error", f.getCanonicalPath() + " is not a directory");
        return;
      }
    } catch (Exception ex) {
      MySeriesLogger.logger.log(Level.WARNING, "Browse is not supported in the current OS");
      MyMessages.error("Browse Error!!!", "Browse is not supported");
      return;
    }
  }

  public static void downloadTorrent(String site) {
    if (site.equals(TorrentConstants.EZTV_NAME)) {
      new EzTvForm();
    } else if (site.equals(TorrentConstants.ISOHUNT_NAME)) {
      new IsohuntForm();
    }

  }

  public static void updateFiles(MySeries m) {
    boolean update = Options.toBoolean(Options.AUTO_FILE_UPDATING);
    boolean unzip = Options.toBoolean(Options.AUTO_EXTRACT_ZIPS);
    Options.setOption(Options.AUTO_FILE_UPDATING, true);
    Options.setOption(Options.AUTO_EXTRACT_ZIPS, true);
    //if (!Options.toBoolean(Options.AUTO_FILE_UPDATING)) {
      
      //MyMessages.error("Auto file updating disabled", "Auto file updating is disabled in the options.\n"
      //    + "Enable it and try again");
      //return;
    //}
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
      
      MyMessages.message("Update finished", "Updating of series files finished.");
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    } finally {
      Options.setOption(Options.AUTO_FILE_UPDATING, update);
      Options.setOption(Options.AUTO_EXTRACT_ZIPS, unzip);
    }

  }

  public static void internetUpdate(MySeries m, String site) {
    MySeries.glassPane.activate(null);
    new InternetUpdate(m, site);
  }

  public static void internetUpdateSeries(MySeries m, String site) {
    MySeries.glassPane.activate(null);
    SeriesRecord cSeries = Series.getCurrentSerial();
    if (site.equals(InternetUpdate.TV_RAGE_NAME) && cSeries.getTvrage_ID() == 0) {
      try {
        TrGetId g = new TrGetId(m, cSeries.getSeries_ID(), cSeries.getTitle());
        cSeries.setTvrage_ID(g.tvRageID);
        cSeries.save();
      } catch (SQLException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, null, ex);
      }
    } else {
      InternetUpdate iu = new InternetUpdate(m, cSeries, site);
    }
    MySeries.glassPane.deactivate();
  }

  public static void downloadSeasonSubtitles() {
    SeriesRecord series = Series.getCurrentSerial();
    String code = series.getTvSubtitlesCode().trim();
    String lang = myseries.MySeries.languages.getPrimary().getCode();
    String link = SubtitleConstants.TV_SUBTITLES_URL + "download-"
        + code + "-" + lang + ".html";
    TvSubtitlesForm d = new TvSubtitlesForm(
                link,
                Series.getCurrentSerial().getSeason(),
                Series.getCurrentSerial().getLocalDir()
                );
  }

  private SeriesActions() {
  }
}
