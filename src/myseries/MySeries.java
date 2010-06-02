/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MySeries.java
 *
 * Created on 30 Οκτ 2008, 7:04:28 μμ
 */
package myseries;

import com.googlecode.starrating.StarTableCellEditor;
import com.googlecode.starrating.StarTableCellRenderer;
import javax.swing.ComboBoxModel;
import myseries.episodes.NextEpisodes;
import myseries.episodes.Episodes;
import myseries.series.Series;
import database.DBConnection;
import database.DBHelper;
import tools.languages.Language;
import tools.options.Options;
import myComponents.MyTableModels.MyEpisodesTableModel;
import javax.swing.event.TableModelEvent;
import database.EpisodesRecord;
import database.SeriesRecord;
import help.CheckUpdate;
import help.Help;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JRootPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import myComponents.MyTableModels.MyFilteredSeriesTableModel;
import myComponents.MyTableModels.MySeriesTableModel;
import myComponents.MyUsefulFunctions;
import myComponents.myGUI.MyImagePanel;
import myComponents.myGUI.MyDisabledGlassPane;
import myComponents.myGUI.MyFont;
import myComponents.myTableCellEditors.MyRateEditor;
import myComponents.myTableCellEditors.MyTitleCellEditor;
import myComponents.myTableCellRenderers.MyJDateChooserCellRenderer;
import myComponents.myTableCellRenderers.MyTitleCellRenderer;
import myseries.actions.Actions;
import myseries.episodes.UpdateEpisodesTable;
import myseries.filters.Filters;
import myseries.filters.UpdateFiltersTable;
import myseries.series.UpdateSeriesTable;
import myseries.statistics.StatEpisodes;
import myseries.statistics.StatSeries;
import tools.DesktopSupport;
import tools.Skin;
import tools.download.subtitles.Subtitle;
import tools.internetUpdate.InternetUpdate;
import tools.languages.LangsList;
import tools.myLogger;

/**
 *
 * @author lordovol
 */
public class MySeries extends javax.swing.JFrame implements TableModelListener {

  /**
   * Shortcuts
   * MySeries
   *    Create Database : Ctrl+C
   *    Load Database : Ctrl+L
   *    Save Database : Ctrl+S
   *    Exit Database : Ctrl+Q
   *
   * Edit
   *    Add Series    : Ctrl - A
   *    Edit Series   : Ctrl - E
   *    Delete Series : Ctrl - D
   *    Add Episode   : Ctrl - P
   *
   * Tools
   *    Export Episodes : Ctrl - X
   *    Import Episodes : Ctrl - I
   *    Download Torrent: Ctrl - T
   *    IU TvRage       : Ctrl - R
   *    IU EpGuides     : Ctrl - G
   *    Options         : Ctrl - O
   *
   * Help
   *    Help            : F1
   *    Check Updates   : F5
   *    View Log File   : F12
   *    About           : F11
   *
   *
   */
  private MySeriesTableModel tableModel_series;
  public MyEpisodesTableModel tableModel_episodes;
  private MyFilteredSeriesTableModel tableModel_filterSeries;
  public ComboBoxModel comboBoxModel_filters;
  public static String version = "1.3(dev)";
  public String date = "2010-05-17";
  public static MyDisabledGlassPane glassPane;
  public static Logger logger;
  public static final long serialVersionUID = 1L;
  public MyImagePanel imagePanel = new MyImagePanel();
  public Image image;
  private Integer[] seriesTableWidths;
  private Integer[] episodesTableWidths;
  private Integer[] filtersTableWidths;
  public static LangsList languages;
  private static int CELL_MARGIN = 3;
  public static int TAB_SERIES = 0;
  public static int TAB_FILTERS = 1;
  public static int TAB_STATISTICS = 2;
  public StatSeries table_stat_series;
  public StatEpisodes table_stat_episodes;

  /**
   *
   * @throws java.lang.ClassNotFoundException
   * @throws java.sql.SQLException
   * @throws java.lang.InstantiationException
   * @throws java.lang.IllegalAccessException
   * @throws javax.swing.UnsupportedLookAndFeelException
   * @throws java.io.IOException
   */
  public MySeries() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IOException {

    //DEBUG DATABASE
    if (Options._USER_DIR_.equals("D:\\JavaProjects\\myseriesproject")) {
      //System.out.println("Copy debug db");
      //MyUsefulFunctions.copyfile("G:\\TV Series\\MySeries\\databases\\Spyros2009.db", "D:\\JavaProjects\\myseriesproject\\databases\\develop.db");
    }

    //Set look and feel
    if (Options.toString(Options.LOOK_AND_FEEL).equals("")) {
    } else {
      //set look and feel
      //LookAndFeels.setLookAndFeel(this, Options.toString(Options.LOOK_AND_FEEL));
    }

    //Get language list
    languages = new LangsList();
    languages.setPrimary(LangsList.getLanguageByName(Options.toString(Options.PRIMARY_SUB)));
    languages.setSecondary(LangsList.getLanguageByName(Options.toString(Options.SECONDARY_SUB)));
    for (Iterator<Language> it = languages.getLangs().iterator(); it.hasNext();) {
      Language language = it.next();
      Subtitle.SUBTITLE_LANG.add(language);
    }

    // Create connection
    MySeries.logger.log(Level.INFO, "Creating database connection");
    DBConnection.createConnection(Options.toString(Options.DB_NAME));
    // Create the GUIs table
    MySeries.logger.log(Level.INFO, "Creating the GUI");
    createGUI();
    table_stat_series = new StatSeries();
    table_stat_episodes = new StatEpisodes();
    panel_stats.add(table_stat_series);
    panel_stats.add(table_stat_episodes);
    panel_stats.validate();
    setIconImage(new javax.swing.ImageIcon(getClass().getResource("/images/subtitles.png")).getImage());
    setSize(Options.toInt(Options.WIDTH), Options.toInt(Options.HEIGHT));
    setExtendedState(Options.toInt(Options.WINDOW_STATE));
    createComboBox_filters();

//    this.getContentPane().setBackground(Options.getColor(Options.SKIN_COLOR));
    if (Options.toBoolean(Options.USE_SKIN)) {
      scrollPane_series.getViewport().setBackground(Skin.getSkinColor());
      panel_episodesList.getViewport().setBackground(Skin.getColor_4());
      panel_allSeriesEpisodes.getViewport().setBackground(Skin.getColor_4());
      panel_nextEpisodes.setBackground(Skin.getColor_4());
      panel_Series.setBackground(Skin.getSkinColor());
      panel_episodes.setBackground(Skin.getSkinColor());
      scrollPane_series.setBackground(Skin.getSkinColor());
      panel_filters.setBackground(Skin.getColor_1());
    } else {
      //scrollPane_series.getViewport().setBackground(Color.white);
      panel_episodesList.getViewport().setBackground(Color.white);
      panel_allSeriesEpisodes.getViewport().setBackground(Color.white);

    }

    setLocationRelativeTo(null);
    setVisible(true);
    // Create the next episodes obj
    MySeries.logger.log(Level.INFO, "Creating Next Episodes Object");
    createNextEpisodes();

    //create the series data
    MySeries.logger.log(Level.INFO, "Creating series data");
    Series.setTableModel_series(tableModel_series);
    Series.getSeries();
    tableModel_series = Series.getTableModel_series();

    //Create image pane
    imageLayerPanel.add(imagePanel);
    Image scrImage = new ImageIcon(getClass().getResource("/images/logo.png")).getImage();
    imagePanel.setImage(scrImage);

    //Create the episodes data
    MySeries.logger.log(Level.INFO, "Creating episodes data");
    Episodes.setTableModel_episodes(tableModel_episodes);
    Episodes.setTabsPanel(tabsPanel);
    Series.selectSeries(0);

    tableEpisodes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    //Create the filteredSeries data
    MySeries.logger.log(Level.INFO, "Creating filters data");
    Filters.setTableModel_filterSeries(tableModel_filterSeries);
    Filters.getFilteredSeries();

    //Set the glass pane
    MySeries.logger.log(Level.INFO, "Creating the glass pane");
    glassPane = new MyDisabledGlassPane();
    JRootPane root = SwingUtilities.getRootPane(this);
    root.setGlassPane(glassPane);
    //Check for updates
    MyUsefulFunctions.initInternetConnection();
    if (Options.toBoolean(Options.CHECK_VERSION)) {
      new CheckUpdate(true);
    }
  }

  public static void createLogger() {
    //Create the JVM logger
    logger = myLogger.createHtmlLogger("MYSERIES", Options._USER_DIR_ + "/MySeriesLogs", 262144, true, 1);
    logger.setLevel(Level.parse(Options.toString(Options.DEBUG_MODE)));
  }

  private void createNextEpisodes() throws SQLException {
    NextEpisodes.createNextEpisodes();
    NextEpisodes.button_first = button_first;
    NextEpisodes.button_last = button_last;
    NextEpisodes.button_previous = button_previous;
    NextEpisodes.button_next = button_next;
    NextEpisodes.label_NextEpisode = label_NextEpisode;
    NextEpisodes.show();
  }

  private void createGUI() throws SQLException {
    Language[] subStatuses = {
      LangsList.NONE,
      languages.getPrimary(),
      languages.getSecondary(),
      LangsList.MULTIPLE};
    JComboBox subs = new JComboBox(subStatuses);

    // Set column widths
    ArrayList<Integer> widths = Options.toIntegerArrayList(Options.TABLE_WIDTHS);
    Integer widthsArr[] = new Integer[widths.size()];
    widthsArr = widths.toArray(widthsArr);
    seriesTableWidths = Arrays.copyOfRange(widthsArr, 0, 3);
    episodesTableWidths = Arrays.copyOfRange(widthsArr, 3, 10);
    filtersTableWidths = Arrays.copyOfRange(widthsArr, 10, 16);
    //Create tablemodels
    tableModel_episodes = new MyEpisodesTableModel();
    tableModel_filterSeries = new MyFilteredSeriesTableModel();
    tableModel_series = new MySeriesTableModel();
    // Get saved filters
    comboBoxModel_filters = new DefaultComboBoxModel(DBHelper.getFiltersTitlesList());
    //Init gui components
    initComponents();
    int fontHeight = getFontMetrics(MyFont.myFont).getHeight();
    tabsPanel.setSelectedComponent(tabpanel_FilteredSeries);

    //EPISODES TABLE
    //table_episodesList.removeColumn(table_episodesList.getColumnModel().getColumn(6));
    tableEpisodes.getModel().addTableModelListener(this);
    tableEpisodes.getTableHeader().setReorderingAllowed(false);
    tableEpisodes.getColumn(Episodes.SUBS_COLUMN_TITLE).setCellEditor(new myComponents.myTableCellEditors.MySubtitleEditor(subs));
    tableEpisodes.getColumn(Episodes.AIRED_COLUMN_TITLE).setCellEditor(new myComponents.myTableCellEditors.MyJDateChooserCellEditor());
    tableEpisodes.getColumn(Episodes.AIRED_COLUMN_TITLE).setCellRenderer(new MyJDateChooserCellRenderer());
    tableEpisodes.getColumn(Episodes.EPISODERECORD_COLUMN_TITLE).setCellRenderer(new MyTitleCellRenderer());
    tableEpisodes.getColumn(Episodes.EPISODERECORD_COLUMN_TITLE).setCellEditor(new MyTitleCellEditor());
    tableEpisodes.getColumn(Episodes.RATE_COLUMN_TITLE).setCellRenderer(new StarTableCellRenderer(true, false));
    tableEpisodes.getColumn(Episodes.RATE_COLUMN_TITLE).setCellEditor(new MyRateEditor(true));
    Episodes.setTable_episodes(tableEpisodes);
    Episodes.setTableWidths(episodesTableWidths);
    tableEpisodes.setRowHeight(fontHeight + CELL_MARGIN);


    //FILTERS TABLE
    tableFilters.getModel().addTableModelListener(this);
    tableFilters.getTableHeader().setReorderingAllowed(false);
    tableSeries.getTableHeader().setCursor(Cursor.getDefaultCursor());
    tableFilters.getColumn(Filters.SUBS_COLUMN_TITLE).setCellEditor(new myComponents.myTableCellEditors.MySubtitleEditor(subs));
    tableFilters.getColumn(Filters.AIRED_COLUMN_TITLE).setCellEditor(new myComponents.myTableCellEditors.MyJDateChooserCellEditor());
    tableFilters.getColumn(Filters.AIRED_COLUMN_TITLE).setCellRenderer(new MyJDateChooserCellRenderer());
    Filters.setTableFilters(tableFilters);
    Filters.setTableWidths(filtersTableWidths);
    tableFilters.setRowHeight(fontHeight + CELL_MARGIN);

    //SERIES TABLE
    tableSeries.getModel().addTableModelListener(this);
    tableSeries.getTableHeader().setReorderingAllowed(false);
    tableSeries.getTableHeader().setCursor(Cursor.getDefaultCursor());
    tableSeries.setRowHeight(fontHeight + CELL_MARGIN);
    Series.setTable_series(tableSeries);
    Series.setTableWidths(seriesTableWidths);


    setLocationRelativeTo(null);
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

    seriesPopUp = new javax.swing.JPopupMenu();
    PopUpItem_AddSeries = new javax.swing.JMenuItem();
    popUpItem_GoToLocalDir = new javax.swing.JMenuItem();
    popUpItem_renameEpisodes = new javax.swing.JMenuItem();
    popUpMenu_GoToSubtitles = new javax.swing.JMenu();
    popUpItem_GoToTvSubs = new javax.swing.JMenuItem();
    popUpItem_GoToSubOn = new javax.swing.JMenuItem();
    popUpMenu_internetUpdate = new javax.swing.JMenu();
    popUpItem_IUTvrage = new javax.swing.JMenuItem();
    popUpItem_IUEpguides = new javax.swing.JMenuItem();
    jSeparator1 = new javax.swing.JSeparator();
    PopUpItem_AddEpisode = new javax.swing.JMenuItem();
    PopUpItem_DeleteSerial = new javax.swing.JMenuItem();
    PopUpItem_EditSerial = new javax.swing.JMenuItem();
    popUpItem_exportEpisodes = new javax.swing.JMenuItem();
    episodesPopUp = new javax.swing.JPopupMenu();
    PopUpItem_AddEpisodeInEpisodes = new javax.swing.JMenuItem();
    popUpItem_deleteEpisode = new javax.swing.JMenuItem();
    popUpItem_viewEpisode = new javax.swing.JMenuItem();
    popUpItem_renameEpisode = new javax.swing.JMenuItem();
    popUpMenu_downloadSubtitles = new javax.swing.JMenu();
    popUpItem_downloadSubsTvSubs = new javax.swing.JMenuItem();
    popUpItem_downloadSubsSubOn = new javax.swing.JMenuItem();
    popUpItem_downloadTorrent = new javax.swing.JMenuItem();
    splitPane_main = new javax.swing.JSplitPane();
    panel_Series = new javax.swing.JPanel();
    scrollPane_series = new javax.swing.JScrollPane();
    tableSeries = new javax.swing.JTable();
    imageLayerPanel = new javax.swing.JLayeredPane();
    panel_episodes = new javax.swing.JPanel();
    panel_nextEpisodes = new javax.swing.JPanel();
    label_NextEpisodeTitle = new javax.swing.JLabel();
    label_NextEpisode = new javax.swing.JLabel();
    panel_NextEpisodesbuttons = new javax.swing.JPanel();
    button_last = new javax.swing.JButton();
    button_previous = new javax.swing.JButton();
    button_next = new javax.swing.JButton();
    button_first = new javax.swing.JButton();
    tabsPanel = new javax.swing.JTabbedPane();
    tabpanel_episodesList = new javax.swing.JPanel();
    panel_episodesList = new javax.swing.JScrollPane();
    tableEpisodes = new javax.swing.JTable();
    tabpanel_FilteredSeries = new javax.swing.JPanel();
    panel_allSeriesEpisodes = new javax.swing.JScrollPane();
    tableFilters = new javax.swing.JTable();
    panel_filters = new javax.swing.JPanel();
    combobox_filters = new javax.swing.JComboBox();
    button_saveFilter = new javax.swing.JButton();
    button_deleteFilter = new javax.swing.JButton();
    comboBox_filterSubtitles = new javax.swing.JComboBox();
    combobox_downloaded = new javax.swing.JComboBox();
    comboBox_seen = new javax.swing.JComboBox();
    tabpanel_statistics = new javax.swing.JPanel();
    panel_stats = new javax.swing.JPanel();
    menuBar = new javax.swing.JMenuBar();
    menu_MySeries = new javax.swing.JMenu();
    menuItem_createDB = new javax.swing.JMenuItem();
    menuItem_loadDatabase = new javax.swing.JMenuItem();
    menuItem_saveDatabaseAs = new javax.swing.JMenuItem();
    menuItem_exit = new javax.swing.JMenuItem();
    menu_Edit = new javax.swing.JMenu();
    menuItem_addSeries = new javax.swing.JMenuItem();
    menuItem_editSeries = new javax.swing.JMenuItem();
    menuItem_deleteSeries = new javax.swing.JMenuItem();
    menuItem_editEpisode = new javax.swing.JMenuItem();
    menu_Tools = new javax.swing.JMenu();
    menuItem_exportEpisodes = new javax.swing.JMenuItem();
    menuItem_importEpisodes = new javax.swing.JMenuItem();
    jMenu1 = new javax.swing.JMenu();
    menuItem_downloadTorrent = new javax.swing.JMenuItem();
    jMenuItem2 = new javax.swing.JMenuItem();
    menu_InternetUpdate = new javax.swing.JMenu();
    menuItem_IUTvrage = new javax.swing.JMenuItem();
    menuItem_IUEpguides = new javax.swing.JMenuItem();
    jSeparator2 = new javax.swing.JSeparator();
    menuItem_options = new javax.swing.JMenuItem();
    menu_Help = new javax.swing.JMenu();
    menuItem_help = new javax.swing.JMenuItem();
    menuItem_checkUpdate = new javax.swing.JMenuItem();
    menuItem_viewLogs = new javax.swing.JMenuItem();
    jMenuItem1 = new javax.swing.JMenuItem();
    jSeparator3 = new javax.swing.JSeparator();
    menuItem_About = new javax.swing.JMenuItem();

    PopUpItem_AddSeries.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_series.png"))); // NOI18N
    PopUpItem_AddSeries.setText("Add a new series");
    PopUpItem_AddSeries.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        PopUpItem_AddSeriesActionPerformed(evt);
      }
    });
    seriesPopUp.add(PopUpItem_AddSeries);

    popUpItem_GoToLocalDir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/localDir.png"))); // NOI18N
    popUpItem_GoToLocalDir.setText("Open Directory");
    popUpItem_GoToLocalDir.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_GoToLocalDirActionPerformed(evt);
      }
    });
    seriesPopUp.add(popUpItem_GoToLocalDir);

    popUpItem_renameEpisodes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/rename.png"))); // NOI18N
    popUpItem_renameEpisodes.setText("Rename Episodes");
    popUpItem_renameEpisodes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_renameEpisodesActionPerformed(evt);
      }
    });
    seriesPopUp.add(popUpItem_renameEpisodes);

    popUpMenu_GoToSubtitles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/subtitles.png"))); // NOI18N
    popUpMenu_GoToSubtitles.setText("Go to Subtitles");

    popUpItem_GoToTvSubs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tvsubtitles.png"))); // NOI18N
    popUpItem_GoToTvSubs.setText("Go to the subtitles page");
    popUpItem_GoToTvSubs.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_GoToTvSubsActionPerformed(evt);
      }
    });
    popUpMenu_GoToSubtitles.add(popUpItem_GoToTvSubs);

    popUpItem_GoToSubOn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/subtitleonline.png"))); // NOI18N
    popUpItem_GoToSubOn.setText("Go to the subtitles page");
    popUpItem_GoToSubOn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_GoToSubOnActionPerformed(evt);
      }
    });
    popUpMenu_GoToSubtitles.add(popUpItem_GoToSubOn);

    seriesPopUp.add(popUpMenu_GoToSubtitles);

    popUpMenu_internetUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/internet_update.png"))); // NOI18N
    popUpMenu_internetUpdate.setText("Update " + Series.getCurrentSerial() + " episodes list");
    popUpMenu_internetUpdate.setFont(popUpMenu_internetUpdate.getFont());

    popUpItem_IUTvrage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tvrage.png"))); // NOI18N
    popUpItem_IUTvrage.setText("Update from " + InternetUpdate.TV_RAGE_NAME);
    popUpItem_IUTvrage.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_IUTvrageActionPerformed(evt);
      }
    });
    popUpMenu_internetUpdate.add(popUpItem_IUTvrage);

    popUpItem_IUEpguides.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/epguides.png"))); // NOI18N
    popUpItem_IUEpguides.setText("Update from " + InternetUpdate.EP_GUIDES_NAME);
    popUpItem_IUEpguides.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_IUEpguidesActionPerformed(evt);
      }
    });
    popUpMenu_internetUpdate.add(popUpItem_IUEpguides);

    seriesPopUp.add(popUpMenu_internetUpdate);
    seriesPopUp.add(jSeparator1);

    PopUpItem_AddEpisode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_episode.png"))); // NOI18N
    PopUpItem_AddEpisode.setText("Add episode");
    PopUpItem_AddEpisode.setEnabled(false);
    PopUpItem_AddEpisode.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        PopUpItem_AddEpisodeActionPerformed(evt);
      }
    });
    seriesPopUp.add(PopUpItem_AddEpisode);

    PopUpItem_DeleteSerial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_series.png"))); // NOI18N
    PopUpItem_DeleteSerial.setText("Delete Series");
    PopUpItem_DeleteSerial.setEnabled(false);
    PopUpItem_DeleteSerial.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        PopUpItem_DeleteSerialActionPerformed(evt);
      }
    });
    seriesPopUp.add(PopUpItem_DeleteSerial);

    PopUpItem_EditSerial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit_series.png"))); // NOI18N
    PopUpItem_EditSerial.setText("Edit Series");
    PopUpItem_EditSerial.setEnabled(false);
    PopUpItem_EditSerial.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        PopUpItem_EditSerialActionPerformed(evt);
      }
    });
    seriesPopUp.add(PopUpItem_EditSerial);

    popUpItem_exportEpisodes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/export_episodes.png"))); // NOI18N
    popUpItem_exportEpisodes.setText("Export episodes");
    popUpItem_exportEpisodes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_exportEpisodesActionPerformed(evt);
      }
    });
    seriesPopUp.add(popUpItem_exportEpisodes);

    PopUpItem_AddEpisodeInEpisodes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_episode.png"))); // NOI18N
    PopUpItem_AddEpisodeInEpisodes.setText("Add episode");
    PopUpItem_AddEpisodeInEpisodes.setEnabled(false);
    PopUpItem_AddEpisodeInEpisodes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        PopUpItem_AddEpisodeInEpisodesActionPerformed(evt);
      }
    });
    episodesPopUp.add(PopUpItem_AddEpisodeInEpisodes);

    popUpItem_deleteEpisode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_episode.png"))); // NOI18N
    popUpItem_deleteEpisode.setText("Delete Episode");
    popUpItem_deleteEpisode.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_deleteEpisodeActionPerformed(evt);
      }
    });
    episodesPopUp.add(popUpItem_deleteEpisode);

    popUpItem_viewEpisode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/series.png"))); // NOI18N
    popUpItem_viewEpisode.setText("View Episode");
    popUpItem_viewEpisode.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_viewEpisodeActionPerformed(evt);
      }
    });
    episodesPopUp.add(popUpItem_viewEpisode);

    popUpItem_renameEpisode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/rename.png"))); // NOI18N
    popUpItem_renameEpisode.setText("Rename Episode");
    popUpItem_renameEpisode.setToolTipText("Rename the episode");
    popUpItem_renameEpisode.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_renameEpisodeActionPerformed(evt);
      }
    });
    episodesPopUp.add(popUpItem_renameEpisode);

    popUpMenu_downloadSubtitles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/subtitles.png"))); // NOI18N
    popUpMenu_downloadSubtitles.setText("Download Subtitles");

    popUpItem_downloadSubsTvSubs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tvsubtitles.png"))); // NOI18N
    popUpItem_downloadSubsTvSubs.setText("DownloadSubtitles");
    popUpItem_downloadSubsTvSubs.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_downloadSubsTvSubsActionPerformed(evt);
      }
    });
    popUpMenu_downloadSubtitles.add(popUpItem_downloadSubsTvSubs);

    popUpItem_downloadSubsSubOn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/subtitleonline.png"))); // NOI18N
    popUpItem_downloadSubsSubOn.setText("DownloadSubtitles");
    popUpItem_downloadSubsSubOn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_downloadSubsSubOnActionPerformed(evt);
      }
    });
    popUpMenu_downloadSubtitles.add(popUpItem_downloadSubsSubOn);

    episodesPopUp.add(popUpMenu_downloadSubtitles);

    popUpItem_downloadTorrent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/torrent.png"))); // NOI18N
    popUpItem_downloadTorrent.setText("Download torrent");
    popUpItem_downloadTorrent.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_downloadTorrentActionPerformed(evt);
      }
    });
    episodesPopUp.add(popUpItem_downloadTorrent);

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("MySerieS v" + version+ " - Database: " + Options.toString(Options.DB_NAME).replace(".db", ""));
    setIconImages(null);
    setMinimumSize(new java.awt.Dimension(1000, 500));
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        formWindowClosing(evt);
      }
    });

    splitPane_main.setDividerLocation(Options.toInt(Options.DIVIDER_LOCATION) == 0 ? 250 : Options.toInt(Options.DIVIDER_LOCATION));

    panel_Series.setMaximumSize(new java.awt.Dimension(216, 32767));
    panel_Series.setPreferredSize(new java.awt.Dimension(216, 584));
    panel_Series.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(java.awt.event.ComponentEvent evt) {
        panel_SeriesComponentResized(evt);
      }
    });

    scrollPane_series.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    scrollPane_series.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane_series.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    scrollPane_series.setMaximumSize(new java.awt.Dimension(30000, 30000));
    scrollPane_series.setMinimumSize(new java.awt.Dimension(200, 400));
    scrollPane_series.setPreferredSize(new java.awt.Dimension(200, 400));
    scrollPane_series.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        scrollPane_seriesMouseReleased(evt);
      }
    });

    tableSeries.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    tableSeries.setModel(tableModel_series);
    tableSeries.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
    tableSeries.setOpaque(false);
    tableSeries.setSelectionBackground(tableSeries.getSelectionBackground());
    tableSeries.setSelectionForeground(tableSeries.getSelectionForeground());
    tableSeries.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tableSeriesMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        tableSeriesMouseReleased(evt);
      }
    });
    scrollPane_series.setViewportView(tableSeries);

    javax.swing.GroupLayout panel_SeriesLayout = new javax.swing.GroupLayout(panel_Series);
    panel_Series.setLayout(panel_SeriesLayout);
    panel_SeriesLayout.setHorizontalGroup(
      panel_SeriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_SeriesLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(imageLayerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
        .addContainerGap())
      .addGroup(panel_SeriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(panel_SeriesLayout.createSequentialGroup()
          .addContainerGap()
          .addComponent(scrollPane_series, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
          .addContainerGap()))
    );
    panel_SeriesLayout.setVerticalGroup(
      panel_SeriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_SeriesLayout.createSequentialGroup()
        .addGap(33, 33, 33)
        .addComponent(imageLayerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
        .addGap(53, 53, 53))
      .addGroup(panel_SeriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(panel_SeriesLayout.createSequentialGroup()
          .addContainerGap()
          .addComponent(scrollPane_series, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGap(157, 157, 157)))
    );

    splitPane_main.setLeftComponent(panel_Series);

    panel_episodes.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    panel_episodes.setMaximumSize(new java.awt.Dimension(35000, 30000));
    panel_episodes.setPreferredSize(new java.awt.Dimension(812, 584));

    panel_nextEpisodes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    label_NextEpisodeTitle.setFont(label_NextEpisodeTitle.getFont().deriveFont(label_NextEpisodeTitle.getFont().getStyle() | java.awt.Font.BOLD));
    label_NextEpisodeTitle.setText("Next Episodes:");

    label_NextEpisode.setFont(label_NextEpisode.getFont().deriveFont(label_NextEpisode.getFont().getStyle() | java.awt.Font.BOLD, label_NextEpisode.getFont().getSize()+1));
    label_NextEpisode.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

    panel_NextEpisodesbuttons.setOpaque(false);

    button_last.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/last.png"))); // NOI18N
    button_last.setToolTipText("First");
    button_last.setEnabled(false);
    button_last.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_lastActionPerformed(evt);
      }
    });

    button_previous.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/previous.png"))); // NOI18N
    button_previous.setToolTipText("First");
    button_previous.setEnabled(false);
    button_previous.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_previousActionPerformed(evt);
      }
    });

    button_next.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/next.png"))); // NOI18N
    button_next.setToolTipText("First");
    button_next.setEnabled(false);
    button_next.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_nextActionPerformed(evt);
      }
    });

    button_first.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/first.png"))); // NOI18N
    button_first.setToolTipText("First");
    button_first.setEnabled(false);
    button_first.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_firstActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout panel_NextEpisodesbuttonsLayout = new javax.swing.GroupLayout(panel_NextEpisodesbuttons);
    panel_NextEpisodesbuttons.setLayout(panel_NextEpisodesbuttonsLayout);
    panel_NextEpisodesbuttonsLayout.setHorizontalGroup(
      panel_NextEpisodesbuttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_NextEpisodesbuttonsLayout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(button_first, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(button_previous, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(button_next, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(button_last, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    panel_NextEpisodesbuttonsLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {button_first, button_last, button_next, button_previous});

    panel_NextEpisodesbuttonsLayout.setVerticalGroup(
      panel_NextEpisodesbuttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(button_last, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(button_next, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(button_previous, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(button_first, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    javax.swing.GroupLayout panel_nextEpisodesLayout = new javax.swing.GroupLayout(panel_nextEpisodes);
    panel_nextEpisodes.setLayout(panel_nextEpisodesLayout);
    panel_nextEpisodesLayout.setHorizontalGroup(
      panel_nextEpisodesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_nextEpisodesLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(label_NextEpisodeTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(label_NextEpisode, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
        .addGap(18, 18, 18)
        .addComponent(panel_NextEpisodesbuttons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );
    panel_nextEpisodesLayout.setVerticalGroup(
      panel_nextEpisodesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_nextEpisodesLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_nextEpisodesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(panel_NextEpisodesbuttons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(label_NextEpisode, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
          .addComponent(label_NextEpisodeTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
        .addContainerGap())
    );

    tabsPanel.setToolTipText("");
    tabsPanel.setMinimumSize(new java.awt.Dimension(120, 460));
    tabsPanel.setPreferredSize(new java.awt.Dimension(400, 463));
    tabsPanel.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        tabsPanelStateChanged(evt);
      }
    });

    tabpanel_episodesList.setBackground(new java.awt.Color(255, 255, 255));
    tabpanel_episodesList.setToolTipText("List of episodes");
    tabpanel_episodesList.setOpaque(false);
    tabpanel_episodesList.setPreferredSize(new java.awt.Dimension(460, 460));

    panel_episodesList.setBackground(new java.awt.Color(255, 255, 255));
    panel_episodesList.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    panel_episodesList.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    panel_episodesList.setAutoscrolls(true);
    panel_episodesList.setOpaque(false);
    panel_episodesList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        panel_episodesListMouseReleased(evt);
      }
    });

    tableEpisodes.setAutoCreateRowSorter(true);
    tableEpisodes.setBackground(tableSeries.getBackground());
    tableEpisodes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    tableEpisodes.setModel(tableModel_episodes);
    tableEpisodes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
    tableEpisodes.setSelectionBackground(tableSeries.getSelectionBackground());
    tableEpisodes.setSelectionForeground(tableSeries.getSelectionForeground());
    tableEpisodes.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        tableEpisodesMouseReleased(evt);
      }
    });
    panel_episodesList.setViewportView(tableEpisodes);

    javax.swing.GroupLayout tabpanel_episodesListLayout = new javax.swing.GroupLayout(tabpanel_episodesList);
    tabpanel_episodesList.setLayout(tabpanel_episodesListLayout);
    tabpanel_episodesListLayout.setHorizontalGroup(
      tabpanel_episodesListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 814, Short.MAX_VALUE)
      .addGroup(tabpanel_episodesListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(tabpanel_episodesListLayout.createSequentialGroup()
          .addContainerGap()
          .addComponent(panel_episodesList, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
          .addGap(14, 14, 14)))
    );
    tabpanel_episodesListLayout.setVerticalGroup(
      tabpanel_episodesListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 405, Short.MAX_VALUE)
      .addGroup(tabpanel_episodesListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(tabpanel_episodesListLayout.createSequentialGroup()
          .addContainerGap()
          .addComponent(panel_episodesList, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
          .addGap(31, 31, 31)))
    );

    tabsPanel.addTab("                          ", new javax.swing.ImageIcon(getClass().getResource("/images/series.png")), tabpanel_episodesList); // NOI18N

    tabpanel_FilteredSeries.setBackground(new java.awt.Color(255, 255, 255));
    tabpanel_FilteredSeries.setToolTipText("Filter series episodes");
    tabpanel_FilteredSeries.setOpaque(false);
    tabpanel_FilteredSeries.setPreferredSize(new java.awt.Dimension(460, 464));

    panel_allSeriesEpisodes.setBackground(new java.awt.Color(255, 255, 255));
    panel_allSeriesEpisodes.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    panel_allSeriesEpisodes.setEnabled(false);
    panel_allSeriesEpisodes.setOpaque(false);

    tableFilters.setAutoCreateRowSorter(true);
    tableFilters.setBackground(tableSeries.getBackground());
    tableFilters.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    tableFilters.setModel(tableModel_filterSeries);
    tableFilters.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
    tableFilters.setOpaque(false);
    tableFilters.setSelectionBackground(tableSeries.getSelectionBackground());
    tableFilters.setSelectionForeground(tableSeries.getSelectionForeground());
    tableFilters.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        tableFiltersMouseReleased(evt);
      }
    });
    panel_allSeriesEpisodes.setViewportView(tableFilters);

    panel_filters.setBackground(new java.awt.Color(255, 255, 255));
    panel_filters.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    combobox_filters.setEditable(true);
    combobox_filters.setModel(comboBoxModel_filters);
    combobox_filters.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        combobox_filtersActionPerformed(evt);
      }
    });

    button_saveFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
    button_saveFilter.setToolTipText("Save Filter");
    button_saveFilter.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_saveFilterActionPerformed(evt);
      }
    });

    button_deleteFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete.png"))); // NOI18N
    button_deleteFilter.setToolTipText("Delete filter");
    button_deleteFilter.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_deleteFilterActionPerformed(evt);
      }
    });

    comboBox_filterSubtitles.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        comboBox_filterSubtitlesActionPerformed(evt);
      }
    });

    combobox_downloaded.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Not downloaded", "Downloaded", "" }));
    combobox_downloaded.setSelectedIndex(1);
    combobox_downloaded.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        combobox_downloadedActionPerformed(evt);
      }
    });

    comboBox_seen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Not Seen", "Seen", "" }));
    comboBox_seen.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        comboBox_seenActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout panel_filtersLayout = new javax.swing.GroupLayout(panel_filters);
    panel_filters.setLayout(panel_filtersLayout);
    panel_filtersLayout.setHorizontalGroup(
      panel_filtersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_filtersLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(combobox_downloaded, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(comboBox_seen, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(comboBox_filterSubtitles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(combobox_filters, 0, 396, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(button_saveFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(button_deleteFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );
    panel_filtersLayout.setVerticalGroup(
      panel_filtersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_filtersLayout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(panel_filtersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_filtersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(combobox_downloaded, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(comboBox_seen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(comboBox_filterSubtitles, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(combobox_filters, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(button_deleteFilter, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(button_saveFilter, javax.swing.GroupLayout.Alignment.TRAILING))
        .addContainerGap())
    );

    panel_filtersLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {button_deleteFilter, button_saveFilter, comboBox_filterSubtitles, comboBox_seen, combobox_downloaded, combobox_filters});

    javax.swing.GroupLayout tabpanel_FilteredSeriesLayout = new javax.swing.GroupLayout(tabpanel_FilteredSeries);
    tabpanel_FilteredSeries.setLayout(tabpanel_FilteredSeriesLayout);
    tabpanel_FilteredSeriesLayout.setHorizontalGroup(
      tabpanel_FilteredSeriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(tabpanel_FilteredSeriesLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(tabpanel_FilteredSeriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(panel_allSeriesEpisodes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
          .addComponent(panel_filters, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    tabpanel_FilteredSeriesLayout.setVerticalGroup(
      tabpanel_FilteredSeriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(tabpanel_FilteredSeriesLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(panel_filters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(panel_allSeriesEpisodes, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
        .addContainerGap())
    );

    tabsPanel.addTab("Filter Series", new javax.swing.ImageIcon(getClass().getResource("/images/filter.png")), tabpanel_FilteredSeries); // NOI18N

    panel_stats.setLayout(new java.awt.GridLayout(2, 1, 0, 10));

    javax.swing.GroupLayout tabpanel_statisticsLayout = new javax.swing.GroupLayout(tabpanel_statistics);
    tabpanel_statistics.setLayout(tabpanel_statisticsLayout);
    tabpanel_statisticsLayout.setHorizontalGroup(
      tabpanel_statisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(tabpanel_statisticsLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(panel_stats, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(260, Short.MAX_VALUE))
    );
    tabpanel_statisticsLayout.setVerticalGroup(
      tabpanel_statisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(tabpanel_statisticsLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(panel_stats, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
        .addGap(26, 26, 26))
    );

    tabsPanel.addTab("Statistics", new javax.swing.ImageIcon(getClass().getResource("/images/star.png")), tabpanel_statistics); // NOI18N

    javax.swing.GroupLayout panel_episodesLayout = new javax.swing.GroupLayout(panel_episodes);
    panel_episodes.setLayout(panel_episodesLayout);
    panel_episodesLayout.setHorizontalGroup(
      panel_episodesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_episodesLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_episodesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(tabsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 819, Short.MAX_VALUE)
          .addComponent(panel_nextEpisodes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    panel_episodesLayout.setVerticalGroup(
      panel_episodesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_episodesLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(panel_nextEpisodes, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addComponent(tabsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 441, Short.MAX_VALUE)
        .addGap(10, 10, 10))
    );

    tabsPanel.getAccessibleContext().setAccessibleName("");

    splitPane_main.setRightComponent(panel_episodes);

    org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, tableSeries, org.jdesktop.beansbinding.ELProperty.create("${background}"), menuBar, org.jdesktop.beansbinding.BeanProperty.create("foreground"));
    bindingGroup.addBinding(binding);

    menu_MySeries.setText("MySerieS");

    menuItem_createDB.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_createDB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/database.png"))); // NOI18N
    menuItem_createDB.setText("Create Database");
    menuItem_createDB.setToolTipText("Create a new database");
    menuItem_createDB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_createDBActionPerformed(evt);
      }
    });
    menu_MySeries.add(menuItem_createDB);

    menuItem_loadDatabase.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_loadDatabase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/load_database.png"))); // NOI18N
    menuItem_loadDatabase.setText("Load Database");
    menuItem_loadDatabase.setToolTipText("Load a database");
    menuItem_loadDatabase.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_loadDatabaseActionPerformed(evt);
      }
    });
    menu_MySeries.add(menuItem_loadDatabase);

    menuItem_saveDatabaseAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_saveDatabaseAs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/database_save.png"))); // NOI18N
    menuItem_saveDatabaseAs.setText("Save Database As...");
    menuItem_saveDatabaseAs.setToolTipText("Save the current database");
    menuItem_saveDatabaseAs.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_saveDatabaseAsActionPerformed(evt);
      }
    });
    menu_MySeries.add(menuItem_saveDatabaseAs);

    menuItem_exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exit.png"))); // NOI18N
    menuItem_exit.setText("Exit");
    menuItem_exit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_exitActionPerformed(evt);
      }
    });
    menu_MySeries.add(menuItem_exit);

    menuBar.add(menu_MySeries);

    menu_Edit.setText("Edit");

    menuItem_addSeries.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_addSeries.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_series.png"))); // NOI18N
    menuItem_addSeries.setText("Add Series");
    menuItem_addSeries.setToolTipText("Add a new series");
    menuItem_addSeries.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_addSeriesActionPerformed(evt);
      }
    });
    menu_Edit.add(menuItem_addSeries);

    menuItem_editSeries.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_editSeries.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit_series.png"))); // NOI18N
    menuItem_editSeries.setToolTipText("Edit current series");

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, PopUpItem_EditSerial, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), menuItem_editSeries, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
    bindingGroup.addBinding(binding);
    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, PopUpItem_EditSerial, org.jdesktop.beansbinding.ELProperty.create("${text}"), menuItem_editSeries, org.jdesktop.beansbinding.BeanProperty.create("text"));
    bindingGroup.addBinding(binding);

    menuItem_editSeries.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_editSeriesActionPerformed(evt);
      }
    });
    menu_Edit.add(menuItem_editSeries);

    menuItem_deleteSeries.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_deleteSeries.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_series.png"))); // NOI18N
    menuItem_deleteSeries.setToolTipText("Delete current series");

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, PopUpItem_DeleteSerial, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), menuItem_deleteSeries, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
    bindingGroup.addBinding(binding);
    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, PopUpItem_DeleteSerial, org.jdesktop.beansbinding.ELProperty.create("${text}"), menuItem_deleteSeries, org.jdesktop.beansbinding.BeanProperty.create("text"));
    bindingGroup.addBinding(binding);

    menuItem_deleteSeries.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_deleteSeriesActionPerformed(evt);
      }
    });
    menu_Edit.add(menuItem_deleteSeries);

    menuItem_editEpisode.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_editEpisode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_episode.png"))); // NOI18N
    menuItem_editEpisode.setToolTipText("Add a new episode to the current series");

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, PopUpItem_AddEpisode, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), menuItem_editEpisode, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
    bindingGroup.addBinding(binding);
    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, PopUpItem_AddEpisode, org.jdesktop.beansbinding.ELProperty.create("${text}"), menuItem_editEpisode, org.jdesktop.beansbinding.BeanProperty.create("text"));
    bindingGroup.addBinding(binding);

    menuItem_editEpisode.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_editEpisodeActionPerformed(evt);
      }
    });
    menu_Edit.add(menuItem_editEpisode);

    menuBar.add(menu_Edit);

    menu_Tools.setText("Tools");

    menuItem_exportEpisodes.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_exportEpisodes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/export_episodes.png"))); // NOI18N
    menuItem_exportEpisodes.setText("Export Episodes");
    menuItem_exportEpisodes.setToolTipText("Export current series episodes");
    menuItem_exportEpisodes.setEnabled(false);
    menuItem_exportEpisodes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_exportEpisodesActionPerformed(evt);
      }
    });
    menu_Tools.add(menuItem_exportEpisodes);

    menuItem_importEpisodes.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_importEpisodes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/import_episodes.png"))); // NOI18N
    menuItem_importEpisodes.setText("Import Episodes");
    menuItem_importEpisodes.setToolTipText("Import a file with a series episodes");
    menuItem_importEpisodes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_importEpisodesActionPerformed(evt);
      }
    });
    menu_Tools.add(menuItem_importEpisodes);

    jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/torrent.png"))); // NOI18N
    jMenu1.setText("Download Torrent");

    menuItem_downloadTorrent.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_downloadTorrent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/eztv.png"))); // NOI18N
    menuItem_downloadTorrent.setText("From Eztv");
    menuItem_downloadTorrent.setToolTipText("Download an episode's torrent");
    menuItem_downloadTorrent.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_downloadTorrentActionPerformed(evt);
      }
    });
    jMenu1.add(menuItem_downloadTorrent);

    jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/isohunt.png"))); // NOI18N
    jMenuItem2.setText("From Isohunt");
    jMenu1.add(jMenuItem2);

    menu_Tools.add(jMenu1);

    menu_InternetUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/internet_update.png"))); // NOI18N
    menu_InternetUpdate.setText("Internet Update");
    menu_InternetUpdate.setToolTipText("Update all series episodes list");

    menuItem_IUTvrage.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_IUTvrage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tvrage.png"))); // NOI18N
    menuItem_IUTvrage.setText("Update from " + InternetUpdate.TV_RAGE_NAME);
    menuItem_IUTvrage.setToolTipText("Update all series episodes list from TvRage");
    menuItem_IUTvrage.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_IUTvrageActionPerformed(evt);
      }
    });
    menu_InternetUpdate.add(menuItem_IUTvrage);

    menuItem_IUEpguides.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_IUEpguides.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/epguides.png"))); // NOI18N
    menuItem_IUEpguides.setText("Update from " + InternetUpdate.EP_GUIDES_NAME);
    menuItem_IUEpguides.setToolTipText("Update all series episodes list from EpGuides");
    menuItem_IUEpguides.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_IUEpguidesActionPerformed(evt);
      }
    });
    menu_InternetUpdate.add(menuItem_IUEpguides);

    menu_Tools.add(menu_InternetUpdate);
    menu_Tools.add(jSeparator2);

    menuItem_options.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_options.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/options.png"))); // NOI18N
    menuItem_options.setText("Options");
    menuItem_options.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_optionsActionPerformed(evt);
      }
    });
    menu_Tools.add(menuItem_options);

    menuBar.add(menu_Tools);

    menu_Help.setText("Help");

    menuItem_help.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
    menuItem_help.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
    menuItem_help.setText("Help");
    menuItem_help.setToolTipText("Display Help");
    menuItem_help.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_helpActionPerformed(evt);
      }
    });
    menu_Help.add(menuItem_help);

    menuItem_checkUpdate.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
    menuItem_checkUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/checkUpdates.png"))); // NOI18N
    menuItem_checkUpdate.setText("Check For Updates");
    menuItem_checkUpdate.setToolTipText("Check for MySerieS updates");
    menuItem_checkUpdate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_checkUpdateActionPerformed(evt);
      }
    });
    menu_Help.add(menuItem_checkUpdate);

    menuItem_viewLogs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, 0));
    menuItem_viewLogs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/viewLogs.png"))); // NOI18N
    menuItem_viewLogs.setText("View Log File");
    menuItem_viewLogs.setToolTipText("View the log file");
    menuItem_viewLogs.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_viewLogsActionPerformed(evt);
      }
    });
    menu_Help.add(menuItem_viewLogs);

    jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
    jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exit.png"))); // NOI18N
    jMenuItem1.setText("Clear Log Files");
    jMenuItem1.setToolTipText("Clear older log files");
    jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem1ActionPerformed(evt);
      }
    });
    menu_Help.add(jMenuItem1);
    menu_Help.add(jSeparator3);

    menuItem_About.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
    menuItem_About.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/info.png"))); // NOI18N
    menuItem_About.setText("About");
    menuItem_About.setToolTipText("About MySerieS");
    menuItem_About.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_AboutActionPerformed(evt);
      }
    });
    menu_Help.add(menuItem_About);

    menuBar.add(menu_Help);

    setJMenuBar(menuBar);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(splitPane_main, javax.swing.GroupLayout.DEFAULT_SIZE, 1064, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(splitPane_main, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
    );

    bindingGroup.bind();

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void tableSeriesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableSeriesMouseClicked
    try {
      seriesMouseClicked();
    } catch (IOException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }
}//GEN-LAST:event_tableSeriesMouseClicked

  private void seriesMouseClicked() throws IOException {
    int selectedRow = tableSeries.getSelectedRow();
    if (selectedRow > -1) {
      Series.setCurrentSerial((SeriesRecord) tableSeries.getValueAt(selectedRow, Series.SERIESRECORD_COLUMN));
      tabsPanel.setTitleAt(0, Series.getCurrentSerial().getFullTitle());
      String imagePath = Options._USER_DIR_ + "/" + MyImagePanel.PATH + "/" + Series.getCurrentSerial().getScreenshot();
      if (new File(imagePath).isFile()) {
        Image im = new ImageIcon(imagePath).getImage();
        imagePanel.setImage(im);
      } else {
        Image image = new ImageIcon(getClass().getResource("/images/logo.png")).getImage();
        imagePanel.setImage(image);
      }
      seriesPopUpItemsState(Series.getCurrentSerial().getFullTitle(), true);
      tabsPanel.setSelectedComponent(tabpanel_episodesList);
    } else {
      seriesPopUpItemsState(null, false);
    }
  }

  private void seriesPopUpItemsState(String ser, boolean state) throws IOException {
    PopUpItem_AddEpisode.setEnabled(state);
    PopUpItem_DeleteSerial.setEnabled(state);
    PopUpItem_EditSerial.setEnabled(state);
    popUpItem_GoToTvSubs.setEnabled(state);
    popUpItem_GoToSubOn.setEnabled(state);
    popUpMenu_GoToSubtitles.setEnabled(state);
    popUpItem_GoToLocalDir.setEnabled(state);
    popUpItem_renameEpisodes.setEnabled(state);
    menuItem_exportEpisodes.setEnabled(state);
    popUpItem_exportEpisodes.setEnabled(state);
    popUpItem_IUTvrage.setEnabled(state);
    popUpMenu_internetUpdate.setEnabled(state);
    if (ser != null) {
      PopUpItem_AddEpisode.setText("Add new episode to " + ser);
      PopUpItem_DeleteSerial.setText("Delete series " + ser);
      PopUpItem_EditSerial.setText("Edit series " + ser);
      popUpMenu_GoToSubtitles.setText("Go to the subtitles page of  " + ser);
      popUpItem_GoToTvSubs.setText("Go to TvSubtitle");
      popUpItem_GoToSubOn.setText("Go to SubtitleOnline");
      popUpItem_GoToLocalDir.setText("Open " + ser + " directory");
      popUpItem_renameEpisodes.setText("Rename " + ser + " directory");
      menuItem_exportEpisodes.setText("Export episodes of " + ser);
      popUpItem_exportEpisodes.setText("Export episodes of " + ser);
      popUpMenu_internetUpdate.setText("Update " + ser + " episodes list");
      if (!DesktopSupport.isDesktopSupport() || !DesktopSupport.isBrowseSupport()) {
        popUpItem_GoToTvSubs.setEnabled(false);
      } else {
        if(Series.getCurrentSerial().getTvSubtitlesCode().equals("")){
          popUpItem_GoToTvSubs.setEnabled(false);
        } if(Series.getCurrentSerial().getSOnlineCode().equals("")){
          popUpItem_GoToSubOn.setEnabled(false);
        }
      }
      if (Series.getCurrentSerial().getLocalDir().equals("") || !DesktopSupport.isDesktopSupport()) {
        popUpItem_GoToLocalDir.setEnabled(false);
      }
      if (Series.getCurrentSerial().getLocalDir().equals("")) {
        popUpItem_renameEpisodes.setEnabled(false);
      }
      if (Series.getCurrentSerial().getInternetUpdate() == 0) {
        popUpItem_IUTvrage.setEnabled(false);
      }

    } else {
      PopUpItem_AddEpisode.setText("Add new episode");
      PopUpItem_DeleteSerial.setText("Delete series");
      PopUpItem_EditSerial.setText("Edit series");
      popUpItem_GoToTvSubs.setText("Go to the subtitles page");
      popUpItem_GoToLocalDir.setText("Open Directory");
      popUpItem_renameEpisodes.setText("Rename episodes");
      popUpItem_exportEpisodes.setText("Export episodes");
      popUpItem_IUTvrage.setText("Update episodes list");
    }
  }

  private void tableSeriesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableSeriesMouseReleased
    try {
      seriesMouseReleased(evt);
    } catch (IOException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }
}//GEN-LAST:event_tableSeriesMouseReleased

  private void seriesMouseReleased(java.awt.event.MouseEvent evt) throws IOException {
    Point p = evt.getPoint();
    int rowSelected = tableSeries.rowAtPoint(p);
    if (rowSelected > -1) {
      String ser = "";
      try {
        ser = Series.selectSeries(rowSelected);
      } catch (SQLException ex) {
        MySeries.logger.log(Level.SEVERE, null, ex);
      }

      if (evt.getButton() == MouseEvent.BUTTON3) {
        seriesPopUpItemsState(ser, true);
        seriesPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
      }

    } else {
      seriesPopUpItemsState(null, false);
      if (evt.getButton() == MouseEvent.BUTTON1) {
        try {
          tableSeries.removeRowSelectionInterval(0, tableSeries.getRowCount() - 1);
          //Series.setCurrentSerial(null);
          //Episodes.emptyEpisodes();
        } catch (IllegalArgumentException ex) {
        }
      } else if (evt.getButton() == MouseEvent.BUTTON3) {
        seriesPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
      }
    }
  }

  private void PopUpItem_AddSeriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PopUpItem_AddSeriesActionPerformed
    Actions.addSeries(this);
}//GEN-LAST:event_PopUpItem_AddSeriesActionPerformed

  private void PopUpItem_EditSerialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PopUpItem_EditSerialActionPerformed
    Actions.editSeries(this);

}//GEN-LAST:event_PopUpItem_EditSerialActionPerformed

  private void PopUpItem_DeleteSerialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PopUpItem_DeleteSerialActionPerformed
    Actions.deleteSeries(this);
}//GEN-LAST:event_PopUpItem_DeleteSerialActionPerformed

  private void scrollPane_seriesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPane_seriesMouseReleased
    tableSeriesMouseReleased(evt);
}//GEN-LAST:event_scrollPane_seriesMouseReleased

  private void PopUpItem_AddEpisodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PopUpItem_AddEpisodeActionPerformed
    Actions.AddEpisode(this);
}//GEN-LAST:event_PopUpItem_AddEpisodeActionPerformed

  private void button_firstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_firstActionPerformed
    NextEpisodes.showNextEpisodes(NextEpisodes.FIRST_BUTTON);
  }//GEN-LAST:event_button_firstActionPerformed

  private void button_previousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_previousActionPerformed
    NextEpisodes.showNextEpisodes(NextEpisodes.PREVIOUS_BUTTON);
  }//GEN-LAST:event_button_previousActionPerformed

  private void button_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_nextActionPerformed
    NextEpisodes.showNextEpisodes(NextEpisodes.NEXT_BUTTON);
  }//GEN-LAST:event_button_nextActionPerformed

  private void button_lastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_lastActionPerformed
    NextEpisodes.showNextEpisodes(NextEpisodes.LAST_BUTTON);
  }//GEN-LAST:event_button_lastActionPerformed

  private void menuItem_addSeriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_addSeriesActionPerformed
    PopUpItem_AddSeriesActionPerformed(evt);
}//GEN-LAST:event_menuItem_addSeriesActionPerformed

  private void menuItem_deleteSeriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_deleteSeriesActionPerformed
    PopUpItem_DeleteSerialActionPerformed(evt);
}//GEN-LAST:event_menuItem_deleteSeriesActionPerformed

  private void menuItem_editSeriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_editSeriesActionPerformed
    PopUpItem_EditSerialActionPerformed(evt);
  }//GEN-LAST:event_menuItem_editSeriesActionPerformed

  private void menuItem_editEpisodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_editEpisodeActionPerformed
    PopUpItem_AddEpisodeActionPerformed(evt);
}//GEN-LAST:event_menuItem_editEpisodeActionPerformed

  private void comboBox_filterSubtitlesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBox_filterSubtitlesActionPerformed
    Actions.filterSubtitles(comboBox_filterSubtitles);
  }//GEN-LAST:event_comboBox_filterSubtitlesActionPerformed

  private void comboBox_seenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBox_seenActionPerformed
    Actions.filterSeen(comboBox_seen);
}//GEN-LAST:event_comboBox_seenActionPerformed

  private void menuItem_createDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_createDBActionPerformed
    Actions.createDatabase(this, true);
}//GEN-LAST:event_menuItem_createDBActionPerformed

  private void menuItem_loadDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_loadDatabaseActionPerformed
    Actions.loadDatabase(this);
}//GEN-LAST:event_menuItem_loadDatabaseActionPerformed

  private void combobox_downloadedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combobox_downloadedActionPerformed
    Actions.filterDownloaded(combobox_downloaded);
  }//GEN-LAST:event_combobox_downloadedActionPerformed

  private void menuItem_optionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_optionsActionPerformed
    Actions.showOptions(this);
  }//GEN-LAST:event_menuItem_optionsActionPerformed

  private void menuItem_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_exitActionPerformed
    Actions.exitApplication(this);
}//GEN-LAST:event_menuItem_exitActionPerformed

  private void tabsPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabsPanelStateChanged
    Actions.changeTab(this, evt);
  }//GEN-LAST:event_tabsPanelStateChanged

  private void menuItem_AboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_AboutActionPerformed
    Actions.about(this);
  }//GEN-LAST:event_menuItem_AboutActionPerformed

  private void button_saveFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_saveFilterActionPerformed
    Actions.saveFilter(this);
}//GEN-LAST:event_button_saveFilterActionPerformed

  private void button_deleteFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_deleteFilterActionPerformed
    Actions.deleteFilter(this);
}//GEN-LAST:event_button_deleteFilterActionPerformed

  private void combobox_filtersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combobox_filtersActionPerformed
    Actions.applyFilter(this);
  }//GEN-LAST:event_combobox_filtersActionPerformed

  private void PopUpItem_AddEpisodeInEpisodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PopUpItem_AddEpisodeInEpisodesActionPerformed
    Actions.AddEpisodeInEpisodes(this);

}//GEN-LAST:event_PopUpItem_AddEpisodeInEpisodesActionPerformed

  private void initEpisodesPopUp() {
    PopUpItem_AddEpisodeInEpisodes.setText("Add episode");
    PopUpItem_AddEpisodeInEpisodes.setEnabled(false);
    popUpItem_deleteEpisode.setText("Delete episode");
    popUpItem_deleteEpisode.setEnabled(false);
    popUpItem_viewEpisode.setText("View episode");
    popUpItem_viewEpisode.setEnabled(false);
    popUpItem_renameEpisode.setText("Rename episode");
    popUpItem_renameEpisode.setEnabled(false);
    popUpMenu_downloadSubtitles.setText("Download subtitles");
    popUpMenu_downloadSubtitles.setEnabled(false);
    popUpItem_downloadSubsTvSubs.setText("Download subtitles from TvSubtitles");
    popUpItem_downloadSubsTvSubs.setEnabled(false);
    popUpItem_downloadSubsSubOn.setText("Download subtitles from SubtitleOnline");
    popUpItem_downloadSubsSubOn.setEnabled(false);
    popUpItem_downloadTorrent.setText("Download torrent");
    popUpItem_downloadTorrent.setEnabled(false);
  }

  private void tableEpisodesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableEpisodesMouseReleased

    if (evt.getButton() == MouseEvent.BUTTON3) {
      if (tableEpisodes.getSelectedRowCount() > 1) {
        initEpisodesPopUp();
        popUpItem_deleteEpisode.setText("Delete selected episodes");
        popUpItem_deleteEpisode.setEnabled(true);
        episodesPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
      } else {
        Point p = evt.getPoint();
        int rowSelected = tableEpisodes.rowAtPoint(p);
        //init menus
        initEpisodesPopUp();
        try {
          tableEpisodes.setRowSelectionInterval(rowSelected, rowSelected);
          int s = Integer.parseInt(String.valueOf(tableEpisodes.getValueAt(rowSelected, 0)));
          Episodes.setCurrentEpisode(s);
          int series_ID = Episodes.getCurrentEpisode().getSeries_ID();
          Series.setCurrentSerial(DBHelper.getSeriesByID(series_ID));
          setEpisodePopUpMenu(rowSelected, true);
          episodesPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
        } catch (SQLException ex) {
          MySeries.logger.log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
          if (Series.getCurrentSerial().getSeries_ID() > 0) {
            PopUpItem_AddEpisodeInEpisodes.setEnabled(true);
          } else {
            PopUpItem_AddEpisodeInEpisodes.setEnabled(false);
          }
          episodesPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
        }
      }
    }
  }//GEN-LAST:event_tableEpisodesMouseReleased

  private void popUpItem_deleteEpisodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_deleteEpisodeActionPerformed
    if (tableEpisodes.getSelectedRowCount() == 1) {
      Actions.deleteEpisode();
    } else {
      ArrayList<EpisodesRecord> episodes = new ArrayList<EpisodesRecord>();
      int[] selRows = tableEpisodes.getSelectedRows();
      for (int i = 0; i < selRows.length; i++) {
        EpisodesRecord ep = (EpisodesRecord) tableEpisodes.getValueAt(selRows[i], 1);
        episodes.add(ep);
      }
      Actions.deleteEpisodes(episodes);
    }
  }//GEN-LAST:event_popUpItem_deleteEpisodeActionPerformed

  private void panel_episodesListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_episodesListMouseReleased
    tableEpisodesMouseReleased(evt);
}//GEN-LAST:event_panel_episodesListMouseReleased

  private void popUpItem_GoToTvSubsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_GoToTvSubsActionPerformed
    Actions.goToSubtitlePage(this,Subtitle.TV_SUBTITLES_NAME);
  }//GEN-LAST:event_popUpItem_GoToTvSubsActionPerformed

  private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    Actions.exitApplication(this);
  }//GEN-LAST:event_formWindowClosing

  private void menuItem_exportEpisodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_exportEpisodesActionPerformed
    Actions.exportEpisodes();
  }//GEN-LAST:event_menuItem_exportEpisodesActionPerformed

  private void popUpItem_exportEpisodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_exportEpisodesActionPerformed
    Actions.exportEpisodes();
  }//GEN-LAST:event_popUpItem_exportEpisodesActionPerformed

  private void menuItem_importEpisodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_importEpisodesActionPerformed
    Actions.importEpisodes(this);
  }//GEN-LAST:event_menuItem_importEpisodesActionPerformed

  private void menuItem_helpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_helpActionPerformed
    new Help(this);
}//GEN-LAST:event_menuItem_helpActionPerformed

  private void menuItem_saveDatabaseAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_saveDatabaseAsActionPerformed
    Actions.saveDatase();
  }//GEN-LAST:event_menuItem_saveDatabaseAsActionPerformed

  private void menuItem_IUTvrageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_IUTvrageActionPerformed
    Actions.internetUpdate(this, InternetUpdate.TV_RAGE_NAME);
}//GEN-LAST:event_menuItem_IUTvrageActionPerformed

  private void popUpItem_IUTvrageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_IUTvrageActionPerformed
    Actions.internetUpdateSeries(this, InternetUpdate.TV_RAGE_NAME);
  }//GEN-LAST:event_popUpItem_IUTvrageActionPerformed

  private void menuItem_checkUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_checkUpdateActionPerformed
    Actions.checkUpdates();
  }//GEN-LAST:event_menuItem_checkUpdateActionPerformed

  private void menuItem_viewLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_viewLogsActionPerformed
    Actions.viewLog(this);
  }//GEN-LAST:event_menuItem_viewLogsActionPerformed

  private void popUpItem_GoToLocalDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_GoToLocalDirActionPerformed
    Actions.goToLocalDir();
  }//GEN-LAST:event_popUpItem_GoToLocalDirActionPerformed

  private void popUpItem_viewEpisodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_viewEpisodeActionPerformed
    Actions.viewEpisode();
  }//GEN-LAST:event_popUpItem_viewEpisodeActionPerformed

  private void panel_SeriesComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_panel_SeriesComponentResized
    imagePanel.relocate(this);
  }//GEN-LAST:event_panel_SeriesComponentResized

  private void setEpisodePopUpMenu(int rowSelected, boolean episodePanel) {
    PopUpItem_AddEpisodeInEpisodes.setEnabled(true);
    PopUpItem_AddEpisodeInEpisodes.setVisible(true);
    popUpItem_deleteEpisode.setVisible(true);
    popUpItem_viewEpisode.setEnabled(!Series.getCurrentSerial().getLocalDir().equals(""));
    popUpItem_renameEpisode.setEnabled(!Series.getCurrentSerial().getLocalDir().equals(""));
    popUpItem_renameEpisode.setText("Rename episode " + Episodes.getCurrentEpisode().getTitle());
    popUpItem_viewEpisode.setText("View episode " + Episodes.getCurrentEpisode().getTitle());
    PopUpItem_AddEpisodeInEpisodes.setText("Add new episode");
    if (episodePanel) {
      popUpItem_deleteEpisode.setEnabled(rowSelected > -1);
      popUpItem_deleteEpisode.setText("Delete episode " + Episodes.getCurrentEpisode().getTitle());
    }
    popUpMenu_downloadSubtitles.setEnabled(true);
    popUpMenu_downloadSubtitles.setText("Download subtitles for " + Episodes.getCurrentEpisode().getTitle());
    popUpItem_downloadSubsTvSubs.setEnabled(true);
    popUpItem_downloadSubsSubOn.setEnabled(true);
    popUpItem_downloadTorrent.setText("Download torrent for " + Episodes.getCurrentEpisode().getTitle());
    popUpItem_downloadTorrent.setEnabled(true);
    popUpItem_viewEpisode.validate();
  }

  private void popUpItem_renameEpisodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_renameEpisodesActionPerformed
    Actions.renameEpisodes();
  }//GEN-LAST:event_popUpItem_renameEpisodesActionPerformed

  private void popUpItem_downloadSubsTvSubsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_downloadSubsTvSubsActionPerformed
    Actions.downloadSubtitles(Subtitle.TV_SUBTITLES_NAME);
  }//GEN-LAST:event_popUpItem_downloadSubsTvSubsActionPerformed

  private void popUpItem_downloadTorrentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_downloadTorrentActionPerformed
    Actions.downloadEpisodesTorrent();
  }//GEN-LAST:event_popUpItem_downloadTorrentActionPerformed

  private void tableFiltersMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableFiltersMouseReleased
    if (evt.getButton() == MouseEvent.BUTTON3) {
      Point p = evt.getPoint();
      int rowSelected = tableFilters.rowAtPoint(p);
      try {
        EpisodesRecord ep = (EpisodesRecord) tableFilters.getValueAt(rowSelected, 2);
        SeriesRecord seriesRec = DBHelper.getSeriesByID(ep.getSeries_ID());
        Series.setCurrentSerial(seriesRec);
        Episodes.setCurrentEpisode(ep.getEpisode());
        initEpisodesPopUp();
        setEpisodePopUpMenu(rowSelected, false);
//        if (seriesRec != null) {
//          popUpItem_viewEpisode.setEnabled(!seriesRec.getLocalDir().equals(""));
//          popUpItem_viewEpisode.setText("View episode " + Episodes.getCurrentEpisode().getTitle());
//          popUpItem_downloadSubtitles.setEnabled(!seriesRec.getLink().equals(""));
//          popUpItem_downloadSubtitles.setText("Downlod subtitles for " + Episodes.getCurrentEpisode().getTitle());
//          popUpItem_downloadTorrent.setEnabled(true);
//          popUpItem_downloadTorrent.setText("Download torrent for " + Episodes.getCurrentEpisode().getTitle());
//        }
        episodesPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
      } catch (SQLException ex) {
        MySeries.logger.log(Level.SEVERE, null, ex);
      } catch (IndexOutOfBoundsException ex) {
      }

    } else {
    }

  }//GEN-LAST:event_tableFiltersMouseReleased

  private void menuItem_downloadTorrentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_downloadTorrentActionPerformed
    Actions.downloadTorrent();
  }//GEN-LAST:event_menuItem_downloadTorrentActionPerformed

  private void popUpItem_IUEpguidesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_IUEpguidesActionPerformed
    Actions.internetUpdateSeries(this, InternetUpdate.EP_GUIDES_NAME);
  }//GEN-LAST:event_popUpItem_IUEpguidesActionPerformed

  private void menuItem_IUEpguidesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_IUEpguidesActionPerformed
    Actions.internetUpdate(this, InternetUpdate.EP_GUIDES_NAME);
  }//GEN-LAST:event_menuItem_IUEpguidesActionPerformed

  private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
    Actions.clearLogFiles();
  }//GEN-LAST:event_jMenuItem1ActionPerformed

  private void popUpItem_downloadSubsSubOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_downloadSubsSubOnActionPerformed
    Actions.downloadSubtitles(Subtitle.SUBTITLE_ONLINE_NAME);
  }//GEN-LAST:event_popUpItem_downloadSubsSubOnActionPerformed

  private void popUpItem_GoToSubOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_GoToSubOnActionPerformed
    Actions.goToSubtitlePage(this, Subtitle.SUBTITLE_ONLINE_NAME);
  }//GEN-LAST:event_popUpItem_GoToSubOnActionPerformed

  private void popUpItem_renameEpisodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_renameEpisodeActionPerformed
    Actions.renameEpisode();
  }//GEN-LAST:event_popUpItem_renameEpisodeActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  public static javax.swing.JMenuItem PopUpItem_AddEpisode;
  public static javax.swing.JMenuItem PopUpItem_AddEpisodeInEpisodes;
  public static javax.swing.JMenuItem PopUpItem_AddSeries;
  public static javax.swing.JMenuItem PopUpItem_DeleteSerial;
  public static javax.swing.JMenuItem PopUpItem_EditSerial;
  public static javax.swing.JButton button_deleteFilter;
  public static javax.swing.JButton button_first;
  public static javax.swing.JButton button_last;
  public static javax.swing.JButton button_next;
  public static javax.swing.JButton button_previous;
  public static javax.swing.JButton button_saveFilter;
  public static javax.swing.JComboBox comboBox_filterSubtitles;
  public static javax.swing.JComboBox comboBox_seen;
  public static javax.swing.JComboBox combobox_downloaded;
  public static javax.swing.JComboBox combobox_filters;
  public static javax.swing.JPopupMenu episodesPopUp;
  public static javax.swing.JLayeredPane imageLayerPanel;
  public static javax.swing.JMenu jMenu1;
  public static javax.swing.JMenuItem jMenuItem1;
  public static javax.swing.JMenuItem jMenuItem2;
  public static javax.swing.JSeparator jSeparator1;
  public static javax.swing.JSeparator jSeparator2;
  public static javax.swing.JSeparator jSeparator3;
  public static javax.swing.JLabel label_NextEpisode;
  public static javax.swing.JLabel label_NextEpisodeTitle;
  public static javax.swing.JMenuBar menuBar;
  public static javax.swing.JMenuItem menuItem_About;
  public static javax.swing.JMenuItem menuItem_IUEpguides;
  public static javax.swing.JMenuItem menuItem_IUTvrage;
  public static javax.swing.JMenuItem menuItem_addSeries;
  public static javax.swing.JMenuItem menuItem_checkUpdate;
  public static javax.swing.JMenuItem menuItem_createDB;
  public static javax.swing.JMenuItem menuItem_deleteSeries;
  public static javax.swing.JMenuItem menuItem_downloadTorrent;
  public static javax.swing.JMenuItem menuItem_editEpisode;
  public static javax.swing.JMenuItem menuItem_editSeries;
  public static javax.swing.JMenuItem menuItem_exit;
  public static javax.swing.JMenuItem menuItem_exportEpisodes;
  public static javax.swing.JMenuItem menuItem_help;
  public static javax.swing.JMenuItem menuItem_importEpisodes;
  public static javax.swing.JMenuItem menuItem_loadDatabase;
  public static javax.swing.JMenuItem menuItem_options;
  public static javax.swing.JMenuItem menuItem_saveDatabaseAs;
  public static javax.swing.JMenuItem menuItem_viewLogs;
  public static javax.swing.JMenu menu_Edit;
  public static javax.swing.JMenu menu_Help;
  public static javax.swing.JMenu menu_InternetUpdate;
  public static javax.swing.JMenu menu_MySeries;
  public static javax.swing.JMenu menu_Tools;
  public static javax.swing.JPanel panel_NextEpisodesbuttons;
  public static javax.swing.JPanel panel_Series;
  public static javax.swing.JScrollPane panel_allSeriesEpisodes;
  public static javax.swing.JPanel panel_episodes;
  public static javax.swing.JScrollPane panel_episodesList;
  public static javax.swing.JPanel panel_filters;
  public static javax.swing.JPanel panel_nextEpisodes;
  public javax.swing.JPanel panel_stats;
  public static javax.swing.JMenuItem popUpItem_GoToLocalDir;
  public static javax.swing.JMenuItem popUpItem_GoToSubOn;
  public static javax.swing.JMenuItem popUpItem_GoToTvSubs;
  public static javax.swing.JMenuItem popUpItem_IUEpguides;
  public static javax.swing.JMenuItem popUpItem_IUTvrage;
  public static javax.swing.JMenuItem popUpItem_deleteEpisode;
  public static javax.swing.JMenuItem popUpItem_downloadSubsSubOn;
  public static javax.swing.JMenuItem popUpItem_downloadSubsTvSubs;
  public static javax.swing.JMenuItem popUpItem_downloadTorrent;
  public static javax.swing.JMenuItem popUpItem_exportEpisodes;
  public static javax.swing.JMenuItem popUpItem_renameEpisode;
  public static javax.swing.JMenuItem popUpItem_renameEpisodes;
  public static javax.swing.JMenuItem popUpItem_viewEpisode;
  public static javax.swing.JMenu popUpMenu_GoToSubtitles;
  public static javax.swing.JMenu popUpMenu_downloadSubtitles;
  public static javax.swing.JMenu popUpMenu_internetUpdate;
  public static javax.swing.JScrollPane scrollPane_series;
  public static javax.swing.JPopupMenu seriesPopUp;
  public static javax.swing.JSplitPane splitPane_main;
  public static javax.swing.JTable tableEpisodes;
  public static javax.swing.JTable tableFilters;
  public static javax.swing.JTable tableSeries;
  public static javax.swing.JPanel tabpanel_FilteredSeries;
  public static javax.swing.JPanel tabpanel_episodesList;
  public static javax.swing.JPanel tabpanel_statistics;
  public static javax.swing.JTabbedPane tabsPanel;
  private org.jdesktop.beansbinding.BindingGroup bindingGroup;
  // End of variables declaration//GEN-END:variables

  @Override
  public void tableChanged(TableModelEvent e) {
    if (e.getSource() instanceof MyEpisodesTableModel) {
      new UpdateEpisodesTable(e);
    } else if (e.getSource() instanceof MySeriesTableModel) {
      new UpdateSeriesTable(e);
    } else if (e.getSource() instanceof MyFilteredSeriesTableModel) {
      new UpdateFiltersTable(e);
    }

  }

  public void createComboBox_filters() {
    comboBox_filterSubtitles.setModel(new DefaultComboBoxModel(
            new String[]{
              Subtitle.NONE,
              languages.getPrimary().getName(),
              languages.getSecondary().getName(),
              Subtitle.BOTH,
              languages.getPrimary().getName() + " or " + languages.getSecondary().getName(),
              "Not " + languages.getPrimary().getName()
            }));
  }
}
