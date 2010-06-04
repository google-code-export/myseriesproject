/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.actions;

import database.DBConnection;
import database.DBHelper;
import database.Database;
import database.EpisodesRecord;
import database.FilterRecord;
import database.SaveDatabase;
import database.SeriesRecord;
import help.About;
import help.CheckUpdate;
import java.awt.Desktop;
import java.awt.Image;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableColumnModel;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myseries.series.AdminSeries;
import myseries.series.Series;
import myseries.MySeries;
import myseries.StartPanel;
import myseries.episodes.AdminEpisodes;
import myseries.episodes.Episodes;
import myseries.episodes.NextEpisodes;
import myseries.episodes.Video;
import myseries.filters.Filters;
import tools.DesktopSupport;
import tools.download.subtitles.SubtitleConstants;
import tools.download.subtitles.sonline.GetSOnlineCode;
import tools.download.subtitles.sonline.SOnlineForm;
import tools.download.subtitles.tvsubtitles.GetTvSubtitlesCode;
import tools.download.subtitles.tvsubtitles.TvSubtitlesForm;
import tools.download.torrents.TorrentConstants;
import tools.download.torrents.eztv.EzTvForm;
import tools.download.torrents.isohunt.IsohuntForm;
import tools.importExport.ExportEpisodes;
import tools.importExport.ImportEpisodes;
import tools.internetUpdate.InternetUpdate;
import tools.internetUpdate.tvrage.TrGetId;
import tools.options.Options;
import tools.options.OptionsPanel;
import tools.renaming.RenameEpisodes;

/**
 *
 * @author lordovol
 */
public class Actions {

}
