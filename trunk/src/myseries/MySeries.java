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

import com.googlecode.starrating.StarTableCellRenderer;
import java.awt.Component;
import java.awt.event.MouseListener;
import javax.swing.ComboBoxModel;
import javax.swing.Timer;
import javax.swing.table.TableModel;
import myComponents.myEvents.MyEvent;
import myseries.episodes.Episodes;
import myseries.series.Series;
import database.DBHelper;
import tools.languages.Language;
import tools.options.Options;
import myComponents.MyTableModels.MyEpisodesTableModel;
import javax.swing.event.TableModelEvent;
import database.EpisodesRecord;
import database.SeriesRecord;
import help.CheckUpdate;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicToolBarUI;
import myComponents.MyMessages;
import myComponents.MyTableModels.MyFilteredSeriesTableModel;
import myComponents.MyTableModels.MySeriesTableModel;
import myComponents.MyUsefulFunctions;
import myComponents.myEvents.MyEventHandler;
import myComponents.myEvents.MyEventsClass;
import myComponents.myGUI.MyImagePanel;
import myComponents.myGUI.MyDisabledGlassPane;
import myComponents.myGUI.MyFont;
import myComponents.myGUI.MyTrayIcon;
import myComponents.myGUI.buttons.MyAbstractButton;
import myComponents.myTableCellEditors.MyDownloadedCellEditor;
import myComponents.myTableCellEditors.MyRateEditor;
import myComponents.myTableCellEditors.MyTitleCellEditor;
import myComponents.myTableCellEditors.MyWatchedCellEditor;
import myComponents.myTableCellRenderers.MyJDateChooserCellRenderer;
import myComponents.myTableCellRenderers.MyScheduleTableCellRenderer;
import myComponents.myTableCellRenderers.MyTitleCellRenderer;
import myComponents.myTableCellRenderers.MyDownloadedCellRenderer;
import myComponents.myTableCellRenderers.MySeriesBooleanCellRenderer;
import myComponents.myTableCellRenderers.MySubtitlesCellRenderer;
import myComponents.myTableCellRenderers.MyWatchedCellRenderer;
import myComponents.myTreeCellRenderers.FeedTreeCellRenderer;
import myseries.actions.ApplicationActions;
import myseries.actions.DatabaseActions;
import myseries.actions.EpisodesActions;
import myseries.actions.FeedsActions;
import myseries.actions.FiltersActions;
import myseries.actions.SeriesActions;
import myseries.episodes.UpdateEpisodesTable;
import myseries.filters.Filters;
import myseries.filters.UpdateFiltersTable;
import myseries.schedule.ScheduleMouseListener;
import myseries.series.UpdateSeriesTable;
import myseries.statistics.StatEpisodes;
import myseries.statistics.StatSeries;
import tools.Skin;
import tools.download.subtitles.SubtitleConstants;
import tools.download.torrents.TorrentConstants;
import tools.internetUpdate.InternetUpdate;
import tools.languages.LangsList;
import tools.MySeriesLogger;
import tools.options.Paths;

/**
 *
 * @author lordovol
 */
public class MySeries extends javax.swing.JFrame implements TableModelListener, MySeriesConstants {

  /**
   * Shortcuts
   * MySeries
   *    Create Database:Ctrl - C
   *    Load Database : Ctrl - L
   *    Save Database : Ctrl - S
   *    Restart       : Ctrl - T
   *    Exit          : Ctrl - Q
   *
   * Edit
   *    Add Series    : Ctrl - A
   *    Edit Series   : Ctrl - E
   *    Delete Series : Ctrl - D
   *    Restore Series: Ctrl - R
   *    Add Episode   : Ctrl - P
   *
   * Tools
   *    Export Episodes : Ctrl - X
   *    Import Episodes : Ctrl - I
   *    Download Torrent:
   *        EzTv        : Ctrl - Shift - E
   *        Isohunt     : Ctrl - Shift - I
   *    Internet Update :
   *        TvRage      : Ctrl - Shift - T
   *        EpGuides    : Ctrl - Shift - G
   *    Update Downloads: Ctrl - U
   *    Housekeeping    : Ctrl - H
   *    Customize Toolb.: Ctrl - Z
   *    Options         : Ctrl - O
   *
   * MySerieS Online
   *    Google Code   : F10
   *    Contact       : F6
   *    Check Updates : F5
   *    Latest News   : F2
   *    Donate        : F3
   *
   * Help
   *    Help            : F1
   *    View Log File   : F12
   *    Show errors pan.: F4
   *    About           : F11
   *
   *
   */
  private MySeriesTableModel tableModel_series;
  public MyEpisodesTableModel tableModel_episodes;
  private MyFilteredSeriesTableModel tableModel_filterSeries;
  public ComboBoxModel comboBoxModel_filters;
  public static String version = "1.5(r810)";
  public String date = "2011-05-26";
  public static MyDisabledGlassPane glassPane = new MyDisabledGlassPane();
  public static final long serialVersionUID = 234563636363L;
  public MyImagePanel imagePanel = new MyImagePanel(true);
  public Image image;
  private Integer[] seriesTableWidths;
  private Integer[] episodesTableWidths;
  private Integer[] filtersTableWidths;
  public static LangsList languages;
  private static int CELL_MARGIN = 3;
  public StatSeries table_stat_series;
  public StatEpisodes table_stat_episodes;
  public static boolean isHelp = false;
  private EventListenerList listenerList = new EventListenerList();
  private MyEventsClass evClass = new MyEventsClass(this);
  private Integer[] visibleButtons;
  public TrayIcon trayIcon;
  //public static Toolbar myToolbar;
  private Image appIcon;
  private MyTrayIcon myTrayIcon;
  public Timer memoryTimer = new Timer(Integer.MAX_VALUE, null);
  public Timer feedsTimer = new Timer(Integer.MAX_VALUE, null);

  // TODO delete multiple episodes
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
    appIcon = new javax.swing.ImageIcon(getClass().getResource(APPLICATION_ICON)).getImage();
    myTrayIcon = new MyTrayIcon(this, appIcon, "MySeries");
    if (version.indexOf("dev") > -1) {
      date = MyUsefulFunctions.getToday("dd/MM/yyyy");
    }

    //Get language list
    languages = new LangsList();
    MySeriesLogger.logger.log(Level.INFO, "Setting primary language to {0}", Options.toString(Options.PRIMARY_SUB));
    languages.setPrimary(LangsList.getLanguageByName(Options.toString(Options.PRIMARY_SUB)));
    MySeriesLogger.logger.log(Level.INFO, "Setting secontary langyage to {0}", Options.toString(Options.SECONDARY_SUB));
    languages.setSecondary(LangsList.getLanguageByName(Options.toString(Options.SECONDARY_SUB)));
    for (Iterator<Language> it = languages.getLangs().iterator(); it.hasNext();) {
      Language language = it.next();
      MySeriesLogger.logger.log(Level.INFO, "Adding language {0} to language map", language);
      SubtitleConstants.SUBTITLE_LANG.add(language);
    }

    visibleButtons = Options.toIntegerArray(Options.TOOLBAR_BUTTONS);
    if (visibleButtons == null) {
      visibleButtons = Options.getDefaultToolbarButtons();
    }
    MySeriesLogger.logger.log(Level.INFO, "Creating the GUI");
    // Create the GUIs table
    createGUI();
    MySeriesLogger.logger.log(Level.INFO, "Creating series stats");
    table_stat_series = new StatSeries();
    MySeriesLogger.logger.log(Level.INFO, "Creating episodes stats");
    table_stat_episodes = new StatEpisodes();
    MySeriesLogger.logger.log(Level.INFO, "Creating application icon");
    setIconImage(appIcon);
    MySeriesLogger.logger.log(Level.INFO, "Setting window size to {0}x{1}", new int[]{Options.toInt(Options.WIDTH), Options.toInt(Options.HEIGHT)});
    setSize(Options.toInt(Options.WIDTH), Options.toInt(Options.HEIGHT));
    setExtendedState(Options.toInt(Options.WINDOW_STATE));
    createComboBox_filters();

    //SCHEDULE
    //scheduler.setDatabase(Options._USER_DIR_ +Database.PATH + DBConnection.db);
    MySeriesLogger.logger.log(Level.INFO, "Creating the schedule table");
    scheduler.getSchedule().setDefaultRenderer(new MyScheduleTableCellRenderer());


    //Hide viewports
    panel_episodesList.getViewport().setOpaque(false);
    scrollPane_series.getViewport().setOpaque(false);
    panel_allSeriesEpisodes.getViewport().setOpaque(false);
    //Hide viewport borders
    panel_episodesList.setViewportBorder(BorderFactory.createEmptyBorder());
    scrollPane_series.setViewportBorder(BorderFactory.createEmptyBorder());
    panel_allSeriesEpisodes.setViewportBorder(BorderFactory.createEmptyBorder());
    //Show table borders
    tableEpisodes.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    tableFilters.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    tableSeries.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    //Show header borders
    tableEpisodes.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.GRAY));
    tableFilters.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.GRAY));
    tableSeries.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.GRAY));

    //create the series data
    MySeriesLogger.logger.log(Level.INFO, "Creating series data");
    //Series.setTableModel_series(tableModel_series);
    Series.updateSeriesTable(tableSeries, false);
//        tableModel_series = (MySeriesTableModel) tableSeries.getModel();

    //Create image pane
    MySeriesLogger.logger.log(Level.INFO, "Creating the screenshot panel");
    imageLayerPanel.add(imagePanel);

    //Create the episodes data
    MySeriesLogger.logger.log(Level.INFO, "Creating episodes data");
    //Episodes.setTableModel_episodes(tableModel_episodes);
    //Episodes.setTabsPanel(tabsPanel);
    MySeriesLogger.logger.log(Level.INFO, "Setting current series to the first one");
    Series.selectSeries(this, 0);


    tableEpisodes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    //Create the filteredSeries data
    MySeriesLogger.logger.log(Level.INFO, "Creating filters data");
    Filters.setTableModel_filterSeries(tableModel_filterSeries);
    //    
    //    Filters.getFilteredSeries(comboBox_seen, comboBox_filterSubtitles, combobox_downloaded);
    FiltersActions.applyFilter(this);
    MySeriesLogger.logger.log(Level.INFO, "Creating toolbar");
    switch (Options.toInt(Options.TOOLBAR_POSITION)) {
      case Options._NORTH_:
        myToolbar.setOrientation(SwingConstants.HORIZONTAL);
        getContentPane().add(myToolbar, BorderLayout.NORTH);
        MySeriesLogger.logger.log(Level.INFO, "Position tollbar to the North");
        break;
      case Options._EAST_:
        myToolbar.setOrientation(SwingConstants.VERTICAL);
        getContentPane().add(myToolbar, BorderLayout.EAST);
        MySeriesLogger.logger.log(Level.INFO, "Position tollbar to the East");
        break;
      case Options._SOUTH_:
        myToolbar.setOrientation(SwingConstants.HORIZONTAL);
        getContentPane().add(myToolbar, BorderLayout.SOUTH);
        MySeriesLogger.logger.log(Level.INFO, "Position tollbar to the South");
        break;
      case Options._WEST_:
        myToolbar.setOrientation(SwingConstants.VERTICAL);
        getContentPane().add(myToolbar, BorderLayout.WEST);
        MySeriesLogger.logger.log(Level.INFO, "Position tollbar to the West");
        break;
      case Options._FLOAT_:
        getContentPane().add(myToolbar, BorderLayout.WEST);
        ((BasicToolBarUI) myToolbar.getUI()).setFloating(true, new Point(100, 100));
        myToolbar.setLocation(200, 200);
        MySeriesLogger.logger.log(Level.INFO, "Add floating toolbar");
        break;
    }

    MySeriesLogger.logger.log(Level.INFO, "Setting main split panel divider location");
    splitPane_main.setDividerLocation(Options.toInt(Options.DIVIDER_LOCATION) == 0 ? 250 : Options.toInt(Options.DIVIDER_LOCATION));
    MySeriesLogger.logger.log(Level.INFO, "Setting feeds split panel divider location");
    feedSplitPanel.setDividerLocation(Options.toInt(Options.FEED_DIVIDER_LOCATION) == 0 ? 250 : Options.toInt(Options.FEED_DIVIDER_LOCATION));
    setGlassPane();
    //Seting the order of the tabs
    MySeriesLogger.logger.log(Level.INFO, "Setting the tabs order");
    Integer[] order = Options.toIntegerArray(Options.TABS_ORDER);
    tabsPanel.setOrder(order);
    setLocationRelativeTo(null);
    MySeriesLogger.logger.log(Level.INFO, "Showing GUI");
    setVisible(true);
    MySeriesLogger.logger.log(Level.INFO, "Getting mouse listeners from schedule table");
    MouseListener[] list = scheduler.getSchedule().getTblCalendar().getMouseListeners();
    if (list.length == 1) {
      MySeriesLogger.logger.log(Level.INFO, "Removing mouse listeners from schedule if there are any");
      scheduler.getSchedule().getTblCalendar().removeMouseListener(list[0]);
    }
    MySeriesLogger.logger.log(Level.INFO, "Setting schedule date to today");
    scheduler.getSchedule().goToToday();
    //Check for updates
    MyUsefulFunctions.initInternetConnection();
    if (Options.toBoolean(Options.CHECK_VERSION)) {
      new CheckUpdate(true);
    }
    if (Options.toBoolean(Options.UPDATE_FEEDS)) {
      FeedsActions.updateFeeds(true, this);
    }
    ApplicationActions.latestNews(this, true);
    
    MySeriesLogger.logger.log(Level.INFO, "Adding schedule mouse listener to schedule");
    scheduler.getSchedule().getTblCalendar().addMouseListener(new ScheduleMouseListener());
    MyUsefulFunctions.createMemoryCons(this);
    MyUsefulFunctions.feedUpdater(this);
    ApplicationActions.warnForLogLevel();
    ApplicationActions.warnForJREVersion();

  }

  private void setGlassPane() {
    //Set the glass pane
    MySeriesLogger.logger.log(Level.INFO, "Creating the glass pane");
    glassPane = new MyDisabledGlassPane();
    JRootPane root = SwingUtilities.getRootPane(this);
    root.setGlassPane(glassPane);
  }

  public static void createLogger() {
    //Create the JVM logger
    MySeriesLogger.logger = MySeriesLogger.createHtmlLogger("MYSERIES", Options._USER_DIR_ + Paths.LOGS_PATH + "MySeriesLog", 262144, true, 5);
    try {
      MySeriesLogger.logger.setLevel(Level.parse(Options.toString(Options.DEBUG_MODE)));
    } catch (IllegalArgumentException ex) {
      MySeriesLogger.logger.setLevel(Level.OFF);
    }
  }

  private void createGUI() throws SQLException {
    // Set column widths
    MySeriesLogger.logger.log(Level.INFO, "Creating GUI");
    MySeriesLogger.logger.log(Level.INFO, "Setting tables widths");
    ArrayList<Integer> widths = Options.toIntegerArrayList();
    Integer widthsArr[] = new Integer[widths.size()];
    widthsArr = widths.toArray(widthsArr);
    seriesTableWidths = Arrays.copyOfRange(widthsArr, 0, 3);
    episodesTableWidths = Arrays.copyOfRange(widthsArr, 3, 10);
    filtersTableWidths = Arrays.copyOfRange(widthsArr, 10, 16);
    //Create tablemodels
    MySeriesLogger.logger.log(Level.INFO, "Creating table models");
    tableModel_episodes = new MyEpisodesTableModel();
    tableModel_filterSeries = new MyFilteredSeriesTableModel();
    tableModel_series = new MySeriesTableModel();
    // Get saved filters
    MySeriesLogger.logger.log(Level.INFO, "Creating filters");
    comboBoxModel_filters = new DefaultComboBoxModel(DBHelper.getFiltersTitlesList());
    //Init gui components
    MySeriesLogger.logger.log(Level.INFO, "Initializong components");
    initComponents();
    MySeriesLogger.logger.log(Level.FINE, "Components initialized");
    int fontHeight = getFontMetrics(MyFont.myFont).getHeight();
    MySeriesLogger.logger.log(Level.INFO, "Sets selected tab");
    tabsPanel.setSelectedComponent(tabpanel_FilteredSeries);

    //EPISODES TABLE
    //table_episodesList.removeColumn(table_episodesList.getColumnModel().getColumn(6));
    MySeriesLogger.logger.log(Level.INFO, "Setting cell editors, renderers and listener for episodes table");
    tableEpisodes.getModel().addTableModelListener(this);
    tableEpisodes.getColumn(Episodes.DOWNLOADED_COLUMN_TITLE).setCellRenderer(new MyDownloadedCellRenderer(Episodes.EPISODERECORD_COLUMN));
    tableEpisodes.getColumn(Episodes.DOWNLOADED_COLUMN_TITLE).setCellEditor(new MyDownloadedCellEditor(Episodes.EPISODERECORD_COLUMN));
    tableEpisodes.getColumn(Episodes.SUBS_COLUMN_TITLE).setCellEditor(new myComponents.myTableCellEditors.MySubtitleCellEditor(Episodes.EPISODERECORD_COLUMN));
    tableEpisodes.getColumn(Episodes.SUBS_COLUMN_TITLE).setCellRenderer(new MySubtitlesCellRenderer(Episodes.EPISODERECORD_COLUMN));
    tableEpisodes.getColumn(Episodes.AIRED_COLUMN_TITLE).setCellEditor(new myComponents.myTableCellEditors.MyJDateChooserCellEditor());
    tableEpisodes.getColumn(Episodes.AIRED_COLUMN_TITLE).setCellRenderer(new MyJDateChooserCellRenderer());
    tableEpisodes.getColumn(Episodes.EPISODERECORD_COLUMN_TITLE).setCellRenderer(new MyTitleCellRenderer(false));
    tableEpisodes.getColumn(Episodes.EPISODERECORD_COLUMN_TITLE).setCellEditor(new MyTitleCellEditor());
    tableEpisodes.getColumn(Episodes.SEEN_COLUMN_TITLE).setCellRenderer(new MyWatchedCellRenderer());
    tableEpisodes.getColumn(Episodes.SEEN_COLUMN_TITLE).setCellEditor(new MyWatchedCellEditor(Episodes.EPISODERECORD_COLUMN));
    tableEpisodes.getColumn(Episodes.RATE_COLUMN_TITLE).setCellRenderer(new StarTableCellRenderer(true, false));
    tableEpisodes.getColumn(Episodes.RATE_COLUMN_TITLE).setCellEditor(new MyRateEditor(true));
    tableEpisodes.getColumn(Episodes.RATE_COLUMN_TITLE).setMinWidth(160);
    //Episodes.setTable_episodes(tableEpisodes);
    Episodes.setTableWidths(tableEpisodes, episodesTableWidths);
    tableEpisodes.setRowHeight(fontHeight + CELL_MARGIN);


    //FILTERS TABLE
    MySeriesLogger.logger.log(Level.INFO, "Setting cell editors, renderers and listener for filters table");
    tableFilters.getModel().addTableModelListener(this);
    tableFilters.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tableFilters.getColumn(Filters.EPISODERECORD_COLUMN_TITLE).setCellRenderer(new MyTitleCellRenderer(true));
    tableFilters.getColumn(Filters.SUBS_COLUMN_TITLE).setCellEditor(new myComponents.myTableCellEditors.MySubtitleCellEditor(Filters.EPISODERECORD_COLUMN));
    tableFilters.getColumn(Filters.SUBS_COLUMN_TITLE).setCellRenderer(new MySubtitlesCellRenderer(Filters.EPISODERECORD_COLUMN));

    tableFilters.getColumn(Filters.AIRED_COLUMN_TITLE).setCellEditor(new myComponents.myTableCellEditors.MyJDateChooserCellEditor());
    tableFilters.getColumn(Filters.AIRED_COLUMN_TITLE).setCellRenderer(new MyJDateChooserCellRenderer());
    tableFilters.getColumn(Filters.DOWNLOADED_COLUMN_TITLE).setCellRenderer(new MyDownloadedCellRenderer(Filters.EPISODERECORD_COLUMN));
    tableFilters.getColumn(Filters.DOWNLOADED_COLUMN_TITLE).setCellEditor(new MyDownloadedCellEditor(Filters.EPISODERECORD_COLUMN));
    tableFilters.getColumn(Filters.SEEN_COLUMN_TITLE).setCellRenderer(new MyWatchedCellRenderer());
    tableFilters.getColumn(Filters.SEEN_COLUMN_TITLE).setCellEditor(new MyWatchedCellEditor(Filters.EPISODERECORD_COLUMN));
    Filters.setTableWidths(tableFilters, filtersTableWidths);
    tableFilters.setRowHeight(fontHeight + CELL_MARGIN);

    //SERIES TABLE
    MySeriesLogger.logger.log(Level.INFO, "Setting cell editors, renderers and listener for series table");
    tableSeries.getModel().addTableModelListener(this);
    tableSeries.getColumn(Series.HIDDEN_COLUMN_TITLE).setCellRenderer(new MySeriesBooleanCellRenderer());
    tableSeries.getColumn(Series.UPDATE_COLUMN_TITLE).setCellRenderer(new MySeriesBooleanCellRenderer());
    tableSeries.setRowHeight(fontHeight + CELL_MARGIN);
    Series.setTableWidths(tableSeries, seriesTableWidths);

    //POPULATE FEEDS TREE
    MySeriesLogger.logger.log(Level.INFO, "Setting cell renderer for feeds tree");
    feedTree.setCellRenderer(new FeedTreeCellRenderer());
    feedTree.populate(-1);

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

    seriesPopUp = new javax.swing.JPopupMenu();
    PopUpItem_AddSeries = new javax.swing.JMenuItem();
    popUpItem_GoToLocalDir = new javax.swing.JMenuItem();
    popUpItem_renameEpisodes = new javax.swing.JMenuItem();
    popUpMenu_GoToSubtitles = new javax.swing.JMenu();
    popUpItem_GoToTvSubs = new javax.swing.JMenuItem();
    popUpItem_GoToSubOn = new javax.swing.JMenuItem();
    popUpItem_WholeSeasonSubs = new javax.swing.JMenuItem();
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
    popUpMenu_downloadTorrent = new javax.swing.JMenu();
    popUpItem_downloadEzTv = new javax.swing.JMenuItem();
    popUpItem_downloadIsohunt = new javax.swing.JMenuItem();
    splitPane_main = new javax.swing.JSplitPane();
    panel_Series = new javax.swing.JPanel();
    scrollPane_series = new javax.swing.JScrollPane();
    tableSeries = new javax.swing.JTable();
    imageLayerPanel = new javax.swing.JLayeredPane();
    panel_episodes = new javax.swing.JPanel();
    tabsPanel = new myComponents.myGUI.MyDnDTabbedPane();
    tabpanel_episodesList = new javax.swing.JPanel();
    panel_episodesList = new javax.swing.JScrollPane();
    tableEpisodes = new javax.swing.JTable();
    tabpanel_FilteredSeries = new javax.swing.JPanel();
    panel_allSeriesEpisodes = new javax.swing.JScrollPane();
    tableFilters = new javax.swing.JTable();
    panel_filters = new javax.swing.JPanel();
    combobox_filters = new javax.swing.JComboBox();
    comboBox_filterSubtitles = new javax.swing.JComboBox();
    combobox_downloaded = new javax.swing.JComboBox();
    comboBox_seen = new javax.swing.JComboBox();
    bt_save = new myComponents.myGUI.buttons.MyButtonSave();
    bt_delete = new myComponents.myGUI.buttons.MyButtonCancel();
    tabpanel_statistics = new javax.swing.JPanel();
    statSeries = new myseries.statistics.StatSeries();
    statEpisodes = new myseries.statistics.StatEpisodes();
    tabpanel_schedule = new javax.swing.JPanel();
    scheduler = new myseries.schedule.Schedule();
    tabpanel_feeds = new javax.swing.JPanel();
    feedSplitPanel = new javax.swing.JSplitPane();
    leftFeedPanel = new javax.swing.JPanel();
    feedTree = new tools.feeds.FeedTree();
    bt_rssAdd = new myComponents.myGUI.buttons.MyDefaultButton(MyAbstractButton.RSS_ADD,"Add a new rss feed");
    bt_rssUpdate = new myComponents.myGUI.buttons.MyDefaultButton(MyAbstractButton.RSS_REFRESH,"Refresh all rss feeds");
    pr_rssUpdating = new javax.swing.JProgressBar();
    feedPreviewPanel = new tools.feeds.FeedPreviewPanel();
    logPanel = new myComponents.MyLogPanel();
    myToolbar = new myComponents.myToolbar.Toolbar(this, visibleButtons);
    menuBar = new javax.swing.JMenuBar();
    menu_MySeries = new javax.swing.JMenu();
    menuItem_createDB = new javax.swing.JMenuItem();
    menuItem_loadDatabase = new javax.swing.JMenuItem();
    menuItem_saveDatabaseAs = new javax.swing.JMenuItem();
    menuItem_restartApplication = new javax.swing.JMenuItem();
    menuItem_exit = new javax.swing.JMenuItem();
    menu_Edit = new javax.swing.JMenu();
    menuItem_addSeries = new javax.swing.JMenuItem();
    menuItem_editSeries = new javax.swing.JMenuItem();
    menuItem_deleteSeries = new javax.swing.JMenuItem();
    menuItem_restore = new javax.swing.JMenuItem();
    menuItem_editEpisode = new javax.swing.JMenuItem();
    menu_Tools = new javax.swing.JMenu();
    menuItem_exportEpisodes = new javax.swing.JMenuItem();
    menuItem_importEpisodes = new javax.swing.JMenuItem();
    menu_Torrents = new javax.swing.JMenu();
    menuItem_downloadEztv = new javax.swing.JMenuItem();
    menuItem_DownloadIsohunt = new javax.swing.JMenuItem();
    menu_InternetUpdate = new javax.swing.JMenu();
    menuItem_IUTvrage = new javax.swing.JMenuItem();
    menuItem_IUEpguides = new javax.swing.JMenuItem();
    menuItem_updateFiles = new javax.swing.JMenuItem();
    menuItem_housekeeping = new javax.swing.JMenuItem();
    menuItem_customizeToolbar = new javax.swing.JMenuItem();
    jSeparator2 = new javax.swing.JSeparator();
    menuItem_options = new javax.swing.JMenuItem();
    menu_online = new javax.swing.JMenu();
    menuItem_googleCode = new javax.swing.JMenuItem();
    menuItem_reportBug = new javax.swing.JMenuItem();
    menuItem_checkUpdate = new javax.swing.JMenuItem();
    menuItem_LatestNews = new javax.swing.JMenuItem();
    menuItem_paypal = new javax.swing.JMenuItem();
    menu_Help = new javax.swing.JMenu();
    menuItem_help = new javax.swing.JMenuItem();
    menuItem_viewLogs = new javax.swing.JMenuItem();
    menuItem_showErrorPanel = new javax.swing.JCheckBoxMenuItem();
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

    popUpItem_WholeSeasonSubs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tvsubtitlesSeason.png"))); // NOI18N
    popUpItem_WholeSeasonSubs.setText("Download whole season subtitles");
    popUpItem_WholeSeasonSubs.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_WholeSeasonSubsActionPerformed(evt);
      }
    });
    popUpMenu_GoToSubtitles.add(popUpItem_WholeSeasonSubs);

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
    popUpItem_downloadSubsTvSubs.setText("Download From TvSubtitles.net");
    popUpItem_downloadSubsTvSubs.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_downloadSubsTvSubsActionPerformed(evt);
      }
    });
    popUpMenu_downloadSubtitles.add(popUpItem_downloadSubsTvSubs);

    popUpItem_downloadSubsSubOn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/subtitleonline.png"))); // NOI18N
    popUpItem_downloadSubsSubOn.setText("Download from SubtitleOnline.com");
    popUpItem_downloadSubsSubOn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_downloadSubsSubOnActionPerformed(evt);
      }
    });
    popUpMenu_downloadSubtitles.add(popUpItem_downloadSubsSubOn);

    episodesPopUp.add(popUpMenu_downloadSubtitles);

    popUpMenu_downloadTorrent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/torrent.png"))); // NOI18N
    popUpMenu_downloadTorrent.setText("Download");
    popUpMenu_downloadTorrent.setToolTipText("Download torrent");

    popUpItem_downloadEzTv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/eztv.png"))); // NOI18N
    popUpItem_downloadEzTv.setText("Download from EzTv");
    popUpItem_downloadEzTv.setToolTipText("Download torrent from EzTv");
    popUpItem_downloadEzTv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_downloadEzTvActionPerformed(evt);
      }
    });
    popUpMenu_downloadTorrent.add(popUpItem_downloadEzTv);

    popUpItem_downloadIsohunt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/isohunt.png"))); // NOI18N
    popUpItem_downloadIsohunt.setText("Download from Isohunt");
    popUpItem_downloadIsohunt.setToolTipText("Download torrent from Isohunt");
    popUpItem_downloadIsohunt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popUpItem_downloadIsohuntActionPerformed(evt);
      }
    });
    popUpMenu_downloadTorrent.add(popUpItem_downloadIsohunt);

    episodesPopUp.add(popUpMenu_downloadTorrent);

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("MySerieS v" + version+ " - Database: " + Options.toString(Options.DB_NAME).replace(".db", ""));
    setIconImages(null);
    setMinimumSize(new java.awt.Dimension(760, 560));
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        formWindowClosing(evt);
      }
      public void windowDeiconified(java.awt.event.WindowEvent evt) {
        formWindowDeiconified(evt);
      }
      public void windowIconified(java.awt.event.WindowEvent evt) {
        formWindowIconified(evt);
      }
    });
    addWindowStateListener(new java.awt.event.WindowStateListener() {
      public void windowStateChanged(java.awt.event.WindowEvent evt) {
        formWindowStateChanged(evt);
      }
    });

    splitPane_main.setDividerLocation(200);

    panel_Series.setMaximumSize(new java.awt.Dimension(216, 32767));
    panel_Series.setMinimumSize(new java.awt.Dimension(200, 600));
    panel_Series.setPreferredSize(new java.awt.Dimension(216, 584));
    panel_Series.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(java.awt.event.ComponentEvent evt) {
        panel_SeriesComponentResized(evt);
      }
    });

    scrollPane_series.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    scrollPane_series.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane_series.setMinimumSize(new java.awt.Dimension(200, 400));
    scrollPane_series.setPreferredSize(new java.awt.Dimension(200, 400));
    scrollPane_series.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        scrollPane_seriesMouseReleased(evt);
      }
    });

    tableSeries.setModel(tableModel_series);
    tableSeries.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
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
        .addGap(10, 10, 10)
        .addComponent(imageLayerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
        .addGap(10, 10, 10))
      .addGroup(panel_SeriesLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(scrollPane_series, javax.swing.GroupLayout.PREFERRED_SIZE, 180, Short.MAX_VALUE)
        .addGap(9, 9, 9))
    );
    panel_SeriesLayout.setVerticalGroup(
      panel_SeriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_SeriesLayout.createSequentialGroup()
        .addGroup(panel_SeriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panel_SeriesLayout.createSequentialGroup()
            .addGap(11, 11, 11)
            .addComponent(imageLayerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE))
          .addGroup(panel_SeriesLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(scrollPane_series, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
            .addGap(213, 213, 213)))
        .addContainerGap())
    );

    splitPane_main.setLeftComponent(panel_Series);

    panel_episodes.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    panel_episodes.setMaximumSize(new java.awt.Dimension(35000, 30000));
    panel_episodes.setPreferredSize(new java.awt.Dimension(812, 584));
    panel_episodes.setLayout(new java.awt.BorderLayout());

    tabsPanel.setToolTipText("");
    tabsPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    tabsPanel.setMinimumSize(new java.awt.Dimension(120, 460));
    tabsPanel.setPaintGhost(false);
    tabsPanel.setPreferredSize(new java.awt.Dimension(400, 463));
    tabsPanel.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        tabsPanelStateChanged(evt);
      }
    });

    tabpanel_episodesList.setToolTipText("List of episodes");
    tabpanel_episodesList.setName(String.valueOf(MySeries.TAB_EPISODES_ID));
    tabpanel_episodesList.setOpaque(false);
    tabpanel_episodesList.setPreferredSize(new java.awt.Dimension(460, 460));

    panel_episodesList.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    panel_episodesList.setAutoscrolls(true);
    panel_episodesList.setOpaque(false);
    panel_episodesList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        panel_episodesListMouseReleased(evt);
      }
    });

    tableEpisodes.setAutoCreateRowSorter(true);
    tableEpisodes.setModel(tableModel_episodes);
    tableEpisodes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
    tableEpisodes.getTableHeader().setReorderingAllowed(false);
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
      .addGroup(tabpanel_episodesListLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(panel_episodesList, javax.swing.GroupLayout.DEFAULT_SIZE, 831, Short.MAX_VALUE)
        .addGap(14, 14, 14))
    );
    tabpanel_episodesListLayout.setVerticalGroup(
      tabpanel_episodesListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(tabpanel_episodesListLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(panel_episodesList, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
        .addContainerGap())
    );

    tabsPanel.addTab("                          ", new javax.swing.ImageIcon(getClass().getResource("/images/series.png")), tabpanel_episodesList, "List of Episodes"); // NOI18N

    tabpanel_FilteredSeries.setToolTipText("Filter series episodes");
    tabpanel_FilteredSeries.setName(String.valueOf(MySeries.TAB_FILTERS_ID));
    tabpanel_FilteredSeries.setOpaque(false);
    tabpanel_FilteredSeries.setPreferredSize(new java.awt.Dimension(460, 464));

    panel_allSeriesEpisodes.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    panel_allSeriesEpisodes.setEnabled(false);
    panel_allSeriesEpisodes.setOpaque(false);

    tableFilters.setAutoCreateRowSorter(true);
    tableFilters.setModel(tableModel_filterSeries);
    tableFilters.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
    tableFilters.getTableHeader().setReorderingAllowed(false);
    tableFilters.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        tableFiltersMouseReleased(evt);
      }
    });
    panel_allSeriesEpisodes.setViewportView(tableFilters);

    panel_filters.setOpaque(false);

    combobox_filters.setEditable(true);
    combobox_filters.setModel(comboBoxModel_filters);
    combobox_filters.setSelectedItem(Options.toString(Options.ACTIVE_FILTER));
    combobox_filters.setMinimumSize(new java.awt.Dimension(250, 18));
    combobox_filters.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        combobox_filtersActionPerformed(evt);
      }
    });

    comboBox_filterSubtitles.setMaximumSize(new java.awt.Dimension(200, 20));
    comboBox_filterSubtitles.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        comboBox_filterSubtitlesActionPerformed(evt);
      }
    });

    combobox_downloaded.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No Video File", "With Video File", " " }));
    combobox_downloaded.setSelectedIndex(1);
    combobox_downloaded.setMaximumSize(new java.awt.Dimension(200, 20));
    combobox_downloaded.setMinimumSize(new java.awt.Dimension(90, 20));
    combobox_downloaded.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        combobox_downloadedActionPerformed(evt);
      }
    });

    comboBox_seen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Not Watched", "Watched", " " }));
    comboBox_seen.setMaximumSize(new java.awt.Dimension(200, 20));
    comboBox_seen.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        comboBox_seenActionPerformed(evt);
      }
    });

    bt_save.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_saveActionPerformed(evt);
      }
    });

    bt_delete.setToolTipText("Delete");
    bt_delete.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_deleteActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout panel_filtersLayout = new javax.swing.GroupLayout(panel_filters);
    panel_filters.setLayout(panel_filtersLayout);
    panel_filtersLayout.setHorizontalGroup(
      panel_filtersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_filtersLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(combobox_downloaded, 0, 210, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(comboBox_seen, 0, 158, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(comboBox_filterSubtitles, 0, 143, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(combobox_filters, 0, 210, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(bt_save, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(bt_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );
    panel_filtersLayout.setVerticalGroup(
      panel_filtersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_filtersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
        .addComponent(combobox_downloaded, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(combobox_filters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(bt_save, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(bt_delete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(comboBox_filterSubtitles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(comboBox_seen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    javax.swing.GroupLayout tabpanel_FilteredSeriesLayout = new javax.swing.GroupLayout(tabpanel_FilteredSeries);
    tabpanel_FilteredSeries.setLayout(tabpanel_FilteredSeriesLayout);
    tabpanel_FilteredSeriesLayout.setHorizontalGroup(
      tabpanel_FilteredSeriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabpanel_FilteredSeriesLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(tabpanel_FilteredSeriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(panel_allSeriesEpisodes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE)
          .addComponent(panel_filters, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    tabpanel_FilteredSeriesLayout.setVerticalGroup(
      tabpanel_FilteredSeriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(tabpanel_FilteredSeriesLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(panel_filters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(17, 17, 17)
        .addComponent(panel_allSeriesEpisodes, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
        .addContainerGap())
    );

    tabsPanel.addTab("Filter Series", new javax.swing.ImageIcon(getClass().getResource("/images/filter.png")), tabpanel_FilteredSeries, "Filter series"); // NOI18N

    tabpanel_statistics.setName(String.valueOf(MySeries.TAB_RATINGS_ID));
    tabpanel_statistics.setOpaque(false);

    statSeries.setTextColor(Skin.getTitleColor());
    statSeries.setPreferredSize(new java.awt.Dimension(400, 121));
    statSeries.setUnifiedSeries(Options.toBoolean(Options.UNIFIED_SERIES));

    statEpisodes.setTextColor(Skin.getTitleColor());

    javax.swing.GroupLayout tabpanel_statisticsLayout = new javax.swing.GroupLayout(tabpanel_statistics);
    tabpanel_statistics.setLayout(tabpanel_statisticsLayout);
    tabpanel_statisticsLayout.setHorizontalGroup(
      tabpanel_statisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(statEpisodes, javax.swing.GroupLayout.DEFAULT_SIZE, 855, Short.MAX_VALUE)
      .addComponent(statSeries, javax.swing.GroupLayout.DEFAULT_SIZE, 855, Short.MAX_VALUE)
    );
    tabpanel_statisticsLayout.setVerticalGroup(
      tabpanel_statisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabpanel_statisticsLayout.createSequentialGroup()
        .addComponent(statSeries, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(statEpisodes, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
        .addContainerGap())
    );

    tabsPanel.addTab("Ratings", new javax.swing.ImageIcon(getClass().getResource("/images/star.png")), tabpanel_statistics, "Series and episodes ratings"); // NOI18N

    tabpanel_schedule.setToolTipText("Schedule");
    tabpanel_schedule.setName(String.valueOf(MySeries.TAB_SCHEDULE_ID));
    tabpanel_schedule.setOpaque(false);
    tabpanel_schedule.setLayout(new java.awt.BorderLayout());

    scheduler.setTextColor(Skin.getTitleColor());
    scheduler.setMaximumSize(new java.awt.Dimension(1200, 800));
    scheduler.setMinimumSize(new java.awt.Dimension(800, 600));
    scheduler.setPreferredSize(new java.awt.Dimension(800, 600));
    tabpanel_schedule.add(scheduler, java.awt.BorderLayout.CENTER);

    tabsPanel.addTab("Schedule", new javax.swing.ImageIcon(getClass().getResource("/images/today.png")), tabpanel_schedule, "Schedule"); // NOI18N

    tabpanel_feeds.setName(String.valueOf(MySeries.TAB_FEEDS_ID));
    tabpanel_feeds.setOpaque(false);

    feedSplitPanel.setDividerLocation(200);
    feedSplitPanel.setOpaque(false);

    leftFeedPanel.setOpaque(false);

    feedTree.setMySeries(this);
    feedTree.setBorder(javax.swing.BorderFactory.createLineBorder(Skin.getSkinColor()));

    bt_rssAdd.setToolTipText("");
    bt_rssAdd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_rssAddActionPerformed(evt);
      }
    });

    bt_rssUpdate.setToolTipText("");
    bt_rssUpdate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_rssUpdateActionPerformed(evt);
      }
    });

    pr_rssUpdating.setIndeterminate(true);
    pr_rssUpdating.setString("Feeds Updating");
    pr_rssUpdating.setStringPainted(true);
    pr_rssUpdating.setVisible(false);

    javax.swing.GroupLayout leftFeedPanelLayout = new javax.swing.GroupLayout(leftFeedPanel);
    leftFeedPanel.setLayout(leftFeedPanelLayout);
    leftFeedPanelLayout.setHorizontalGroup(
      leftFeedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(leftFeedPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(leftFeedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(feedTree, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
          .addGroup(leftFeedPanelLayout.createSequentialGroup()
            .addComponent(bt_rssAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(bt_rssUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(pr_rssUpdating, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)))
        .addContainerGap())
    );
    leftFeedPanelLayout.setVerticalGroup(
      leftFeedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(leftFeedPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(leftFeedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(pr_rssUpdating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(bt_rssAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(bt_rssUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(feedTree, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
        .addContainerGap())
    );

    feedSplitPanel.setLeftComponent(leftFeedPanel);

    feedPreviewPanel.setOpaque(false);
    feedPreviewPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(java.awt.event.ComponentEvent evt) {
        feedPreviewPanelComponentResized(evt);
      }
    });
    feedPreviewPanel.setLayout(new java.awt.BorderLayout());
    feedSplitPanel.setRightComponent(feedPreviewPanel);

    javax.swing.GroupLayout tabpanel_feedsLayout = new javax.swing.GroupLayout(tabpanel_feeds);
    tabpanel_feeds.setLayout(tabpanel_feedsLayout);
    tabpanel_feedsLayout.setHorizontalGroup(
      tabpanel_feedsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(tabpanel_feedsLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(feedSplitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE)
        .addContainerGap())
    );
    tabpanel_feedsLayout.setVerticalGroup(
      tabpanel_feedsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(tabpanel_feedsLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(feedSplitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
        .addContainerGap())
    );

    tabsPanel.addTab("Rss Feeds", new javax.swing.ImageIcon(getClass().getResource("/images/rss_tab.png")), tabpanel_feeds); // NOI18N

    panel_episodes.add(tabsPanel, java.awt.BorderLayout.CENTER);
    tabsPanel.getAccessibleContext().setAccessibleName("");

    MyMessages.setLogPanel(logPanel);
    panel_episodes.add(logPanel, java.awt.BorderLayout.SOUTH);
    logPanel.setVisible(false);

    splitPane_main.setRightComponent(panel_episodes);

    getContentPane().add(splitPane_main, java.awt.BorderLayout.CENTER);

    myToolbar.setRollover(true);
    myToolbar.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        myToolbarPropertyChange(evt);
      }
    });
    getContentPane().add(myToolbar, java.awt.BorderLayout.NORTH);

    menu_MySeries.setText("MySerieS");
    menu_MySeries.setBorderPainted(true);
    menu_MySeries.setMargin(new java.awt.Insets(0, 0, 0, 6));
    menu_MySeries.setMinimumSize(new java.awt.Dimension(80, 22));

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

    menuItem_restartApplication.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_restartApplication.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/restart.png"))); // NOI18N
    menuItem_restartApplication.setText("Restart");
    menuItem_restartApplication.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_restartApplicationActionPerformed(evt);
      }
    });
    menu_MySeries.add(menuItem_restartApplication);

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
    menu_Edit.setContentAreaFilled(false);
    menu_Edit.setMargin(new java.awt.Insets(0, 0, 0, 6));
    menu_Edit.setMinimumSize(new java.awt.Dimension(80, 22));

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
    menuItem_editSeries.setText("Edit Series");
    menuItem_editSeries.setToolTipText("Edit current series");
    menuItem_editSeries.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_editSeriesActionPerformed(evt);
      }
    });
    menu_Edit.add(menuItem_editSeries);

    menuItem_deleteSeries.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_deleteSeries.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_series.png"))); // NOI18N
    menuItem_deleteSeries.setText("Delete Series");
    menuItem_deleteSeries.setToolTipText("Delete current series");
    menuItem_deleteSeries.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_deleteSeriesActionPerformed(evt);
      }
    });
    menu_Edit.add(menuItem_deleteSeries);

    menuItem_restore.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_restore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/restore.png"))); // NOI18N
    menuItem_restore.setText("Restore Series");
    menuItem_restore.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_restoreActionPerformed(evt);
      }
    });
    menu_Edit.add(menuItem_restore);

    menuItem_editEpisode.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_editEpisode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_episode.png"))); // NOI18N
    menuItem_editEpisode.setText("Add Episode");
    menuItem_editEpisode.setToolTipText("Add a new episode to the current series");
    menuItem_editEpisode.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_editEpisodeActionPerformed(evt);
      }
    });
    menu_Edit.add(menuItem_editEpisode);

    menuBar.add(menu_Edit);

    menu_Tools.setText("Tools");
    menu_Tools.setContentAreaFilled(false);
    menu_Tools.setMargin(new java.awt.Insets(0, 0, 0, 6));
    menu_Tools.setMinimumSize(new java.awt.Dimension(80, 22));

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

    menu_Torrents.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/torrent.png"))); // NOI18N
    menu_Torrents.setText("Download Torrent");

    menuItem_downloadEztv.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    menuItem_downloadEztv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/eztv.png"))); // NOI18N
    menuItem_downloadEztv.setText("From " + TorrentConstants.EZTV_NAME);
    menuItem_downloadEztv.setToolTipText("Download an episode's torrent");
    menuItem_downloadEztv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_downloadEztvActionPerformed(evt);
      }
    });
    menu_Torrents.add(menuItem_downloadEztv);

    menuItem_DownloadIsohunt.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    menuItem_DownloadIsohunt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/isohunt.png"))); // NOI18N
    menuItem_DownloadIsohunt.setText("From " + TorrentConstants.ISOHUNT_NAME);
    menuItem_DownloadIsohunt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_DownloadIsohuntActionPerformed(evt);
      }
    });
    menu_Torrents.add(menuItem_DownloadIsohunt);

    menu_Tools.add(menu_Torrents);

    menu_InternetUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/internet_update.png"))); // NOI18N
    menu_InternetUpdate.setText("Internet Update");
    menu_InternetUpdate.setToolTipText("Update all series episodes list");

    menuItem_IUTvrage.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    menuItem_IUTvrage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tvrage.png"))); // NOI18N
    menuItem_IUTvrage.setText("Update from " + InternetUpdate.TV_RAGE_NAME);
    menuItem_IUTvrage.setToolTipText("Update all series episodes list from TvRage");
    menuItem_IUTvrage.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_IUTvrageActionPerformed(evt);
      }
    });
    menu_InternetUpdate.add(menuItem_IUTvrage);

    menuItem_IUEpguides.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
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

    menuItem_updateFiles.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_updateFiles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/updateFiles.png"))); // NOI18N
    menuItem_updateFiles.setText("Update Files");
    menuItem_updateFiles.setText("Update Files");
    menu_Tools.add(menuItem_updateFiles);

    menuItem_housekeeping.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_housekeeping.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/housekeeping.png"))); // NOI18N
    menuItem_housekeeping.setText("Housekeeping");
    menuItem_housekeeping.setToolTipText("<html>\nChoose to delete downloaded torrents, old log files or\nnot used screenshots");
    menuItem_housekeeping.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_housekeepingActionPerformed(evt);
      }
    });
    menu_Tools.add(menuItem_housekeeping);

    menuItem_customizeToolbar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
    menuItem_customizeToolbar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/customizeToolbar.png"))); // NOI18N
    menuItem_customizeToolbar.setText("Customize Toolbar");
    menuItem_customizeToolbar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_customizeToolbarActionPerformed(evt);
      }
    });
    menu_Tools.add(menuItem_customizeToolbar);
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

    menu_online.setText("Online");
    menu_online.setContentAreaFilled(false);
    menu_online.setMargin(new java.awt.Insets(0, 0, 0, 6));
    menu_online.setMinimumSize(new java.awt.Dimension(80, 22));

    menuItem_googleCode.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F10, 0));
    menuItem_googleCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/googlecode.png"))); // NOI18N
    menuItem_googleCode.setText("Google code");
    menuItem_googleCode.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_googleCodeActionPerformed(evt);
      }
    });
    menu_online.add(menuItem_googleCode);

    menuItem_reportBug.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
    menuItem_reportBug.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mailBug.png"))); // NOI18N
    menuItem_reportBug.setText("Contact or Report Bug");
    menuItem_reportBug.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_reportBugActionPerformed(evt);
      }
    });
    menu_online.add(menuItem_reportBug);

    menuItem_checkUpdate.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
    menuItem_checkUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/checkUpdates.png"))); // NOI18N
    menuItem_checkUpdate.setText("Check For Updates");
    menuItem_checkUpdate.setToolTipText("Check for MySerieS updates");
    menuItem_checkUpdate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_checkUpdateActionPerformed(evt);
      }
    });
    menu_online.add(menuItem_checkUpdate);

    menuItem_LatestNews.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
    menuItem_LatestNews.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/news.png"))); // NOI18N
    menuItem_LatestNews.setText("Latest News");
    menuItem_LatestNews.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_LatestNewsActionPerformed(evt);
      }
    });
    menu_online.add(menuItem_LatestNews);

    menuItem_paypal.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
    menuItem_paypal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/paypal.png"))); // NOI18N
    menuItem_paypal.setText("Paypal Donation");
    menuItem_paypal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_paypalActionPerformed(evt);
      }
    });
    menu_online.add(menuItem_paypal);

    menuBar.add(menu_online);

    menu_Help.setText("Help");
    menu_Help.setContentAreaFilled(false);
    menu_Help.setMargin(new java.awt.Insets(0, 0, 0, 6));
    menu_Help.setMinimumSize(new java.awt.Dimension(80, 22));
    menu_Help.addMenuListener(new javax.swing.event.MenuListener() {
      public void menuCanceled(javax.swing.event.MenuEvent evt) {
      }
      public void menuDeselected(javax.swing.event.MenuEvent evt) {
      }
      public void menuSelected(javax.swing.event.MenuEvent evt) {
        menu_HelpMenuSelected(evt);
      }
    });

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

    menuItem_viewLogs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, 0));
    menuItem_viewLogs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/viewLogs.png"))); // NOI18N
    menuItem_viewLogs.setText("View Log File");
    menuItem_viewLogs.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_viewLogsActionPerformed(evt);
      }
    });
    menu_Help.add(menuItem_viewLogs);

    menuItem_showErrorPanel.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
    menuItem_showErrorPanel.setFont(menuItem_showErrorPanel.getFont().deriveFont(menuItem_showErrorPanel.getFont().getSize()-1f));
    menuItem_showErrorPanel.setSelected(logPanel.isVisible());
    menuItem_showErrorPanel.setText("Show Errors Panel");
    menuItem_showErrorPanel.setToolTipText("Show/Hide the errors panel");
    menuItem_showErrorPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/show_log.png"))); // NOI18N
    menuItem_showErrorPanel.setIconTextGap(2);
    menuItem_showErrorPanel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItem_showErrorPanelActionPerformed(evt);
      }
    });
    menu_Help.add(menuItem_showErrorPanel);
    menu_Help.add(jSeparator3);

    menuItem_About.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
    menuItem_About.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/about.png"))); // NOI18N
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

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void tableSeriesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableSeriesMouseClicked
//      try {
//          seriesMouseReleased(evt);
//      } catch (IOException ex) {
//          MySeriesLogger.logger.log(Level.SEVERE, null, ex);
//      }
}//GEN-LAST:event_tableSeriesMouseClicked

  private void seriesMouseClicked() throws IOException {
    int selectedRow = tableSeries.getSelectedRow();
    if (selectedRow > -1) {
    } else {
    }
  }

  private void tableSeriesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableSeriesMouseReleased
    try {
      seriesMouseReleased(evt);
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
}//GEN-LAST:event_tableSeriesMouseReleased

  private void seriesMouseReleased(java.awt.event.MouseEvent evt) throws IOException {
    Point p = evt.getPoint();
    int selectedRow = tableSeries.rowAtPoint(p);
    MySeriesLogger.logger.log(Level.INFO, "Series table row {0} selected", selectedRow);
    if (selectedRow > -1) {
      SeriesRecord series = (SeriesRecord) tableSeries.getValueAt(selectedRow, Series.SERIESRECORD_COLUMN);
      MySeriesLogger.logger.log(Level.INFO, "Series {0} selected", series.getFullTitle());
      MyEvent event = new MyEvent(tableSeries, MyEventHandler.SET_CURRENT_SERIES);
      event.setSeries(series);
      event.setSeriesPanel(true);
      getEvClass().fireMyEvent(event);
      if (evt.getButton() == MouseEvent.BUTTON3) {
        MySeriesLogger.logger.log(Level.INFO, "Showing popup menu");
        seriesPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
      }
    } else {
      if (evt.getButton() == MouseEvent.BUTTON1) {
        try {
          MySeriesLogger.logger.log(Level.INFO, "No series selected");
          tableSeries.removeRowSelectionInterval(0, tableSeries.getRowCount() - 1);
          MyEvent event = new MyEvent(tableSeries, MyEventHandler.SET_CURRENT_SERIES);
          event.setSeries(null);
          event.setSeriesPanel(true);
          getEvClass().fireMyEvent(event);
        } catch (IllegalArgumentException ex) {
        }
      } else if (evt.getButton() == MouseEvent.BUTTON3) {
        MySeriesLogger.logger.log(Level.INFO, "Showing popup menu");
        seriesPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
      }
    }
  }

  private void PopUpItem_AddSeriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PopUpItem_AddSeriesActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Add series action");
    SeriesActions.addSeries(this);
}//GEN-LAST:event_PopUpItem_AddSeriesActionPerformed

  private void PopUpItem_EditSerialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PopUpItem_EditSerialActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Edit series action");
    SeriesActions.editSeries(this);

}//GEN-LAST:event_PopUpItem_EditSerialActionPerformed

  private void PopUpItem_DeleteSerialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PopUpItem_DeleteSerialActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Delete series action");
    SeriesActions.deleteSeries(this);
}//GEN-LAST:event_PopUpItem_DeleteSerialActionPerformed

  private void scrollPane_seriesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPane_seriesMouseReleased
    MySeriesLogger.logger.log(Level.INFO, "Series table action");
    tableSeriesMouseReleased(evt);
}//GEN-LAST:event_scrollPane_seriesMouseReleased

  private void PopUpItem_AddEpisodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PopUpItem_AddEpisodeActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Add episode action");
    EpisodesActions.AddEpisode(this);
}//GEN-LAST:event_PopUpItem_AddEpisodeActionPerformed

  private void menuItem_addSeriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_addSeriesActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Add series action");
    PopUpItem_AddSeriesActionPerformed(evt);
}//GEN-LAST:event_menuItem_addSeriesActionPerformed

  private void menuItem_deleteSeriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_deleteSeriesActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Delete series action");
    PopUpItem_DeleteSerialActionPerformed(evt);
}//GEN-LAST:event_menuItem_deleteSeriesActionPerformed

  private void menuItem_editSeriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_editSeriesActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Edit series action");
    PopUpItem_EditSerialActionPerformed(evt);
  }//GEN-LAST:event_menuItem_editSeriesActionPerformed

  private void menuItem_editEpisodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_editEpisodeActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Add episode action");
    PopUpItem_AddEpisodeActionPerformed(evt);
}//GEN-LAST:event_menuItem_editEpisodeActionPerformed

  private void comboBox_filterSubtitlesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBox_filterSubtitlesActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Filter series action");
    FiltersActions.filterSubtitles(this);
  }//GEN-LAST:event_comboBox_filterSubtitlesActionPerformed

  private void comboBox_seenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBox_seenActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Seen combobox action");
    FiltersActions.filterSeen(this);
}//GEN-LAST:event_comboBox_seenActionPerformed

  private void menuItem_createDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_createDBActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Create database action");
    DatabaseActions.createDatabase(this, true);
}//GEN-LAST:event_menuItem_createDBActionPerformed

  private void menuItem_loadDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_loadDatabaseActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Load database action");
    DatabaseActions.loadDatabase(this);
}//GEN-LAST:event_menuItem_loadDatabaseActionPerformed

  private void combobox_downloadedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combobox_downloadedActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Filter downloaded combobox action");
    FiltersActions.filterDownloaded(this);
  }//GEN-LAST:event_combobox_downloadedActionPerformed

  private void menuItem_optionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_optionsActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Show options action");
    ApplicationActions.showOptions(this);
  }//GEN-LAST:event_menuItem_optionsActionPerformed

  private void menuItem_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_exitActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Exit application action");
    ApplicationActions.exitApplication(this);

}//GEN-LAST:event_menuItem_exitActionPerformed

  private void tabsPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabsPanelStateChanged
    MySeriesLogger.logger.log(Level.INFO, "Change tab action");
    ApplicationActions.changeTab(this, evt);
  }//GEN-LAST:event_tabsPanelStateChanged

  private void menuItem_AboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_AboutActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "About action");
    ApplicationActions.about(this);
  }//GEN-LAST:event_menuItem_AboutActionPerformed

  private void combobox_filtersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combobox_filtersActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Apply filter action");
    FiltersActions.applyFilter(this);
  }//GEN-LAST:event_combobox_filtersActionPerformed

  private void PopUpItem_AddEpisodeInEpisodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PopUpItem_AddEpisodeInEpisodesActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Add episode action");
    EpisodesActions.AddEpisodeInEpisodes(this);

}//GEN-LAST:event_PopUpItem_AddEpisodeInEpisodesActionPerformed

  private void tableEpisodesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableEpisodesMouseReleased
    SeriesRecord series;
    Point p = evt.getPoint();
    MyEvent event = new MyEvent(this, MyEventHandler.SET_CURRENT_EPISODE);
    int rowSelected = tableEpisodes.rowAtPoint(p);
    MySeriesLogger.logger.log(Level.INFO, "Table episodes row {0}  selected", rowSelected);
    if (evt.getButton() == MouseEvent.BUTTON3) {
      if (tableEpisodes.getSelectedRowCount() > 1) {
        MySeriesLogger.logger.log(Level.INFO, "Multiple episodes selected");
        event.setSingleEpisode(false);
        event.setSeries(Series.getCurrentSerial());
        evClass.fireMyEvent(event);
        episodesPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
      } else {
        //init menus
        try {
          EpisodesRecord ep = null;
          if (rowSelected > -1) {
            tableEpisodes.setRowSelectionInterval(rowSelected, rowSelected);

            ep = (EpisodesRecord) tableEpisodes.getValueAt(rowSelected, Episodes.EPISODERECORD_COLUMN);
            MySeriesLogger.logger.log(Level.INFO, "Episode {0} selected", ep.getTitle());
            int series_ID = ep.getSeries_ID();
            series = DBHelper.getSeriesByID(series_ID);
          } else {
            series = Series.getCurrentSerial();
            ep = null;
          }
          MySeriesLogger.logger.log(Level.INFO, "Setting current series event");
          event.setType(MyEventHandler.SET_CURRENT_SERIES);
          event.setSeries(series);
          getEvClass().fireMyEvent(event);
          MySeriesLogger.logger.log(Level.INFO, "Setting current episode event");
          event.setType(MyEventHandler.SET_CURRENT_EPISODE);
          event.setEpisode(ep);
          getEvClass().fireMyEvent(event);

          MySeriesLogger.logger.log(Level.INFO, "Showing episodes popup");
          episodesPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
        } catch (SQLException ex) {
          MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
        } catch (Exception ex) {
        }
      }
    }
  }//GEN-LAST:event_tableEpisodesMouseReleased

  private void popUpItem_deleteEpisodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_deleteEpisodeActionPerformed
    if (tableEpisodes.getSelectedRowCount() < 2) {
      MySeriesLogger.logger.log(Level.INFO, "Deleting single episode");
      EpisodesActions.deleteEpisode(tableEpisodes);
    } else {
      ArrayList<EpisodesRecord> episodes = new ArrayList<EpisodesRecord>();
      MySeriesLogger.logger.log(Level.INFO, "Deleting {0} episodes", episodes.size());
      int[] selRows = tableEpisodes.getSelectedRows();
      for (int i = 0; i < selRows.length; i++) {
        EpisodesRecord ep = (EpisodesRecord) tableEpisodes.getValueAt(selRows[i], 1);
        episodes.add(ep);
      }
      EpisodesActions.deleteEpisodes(tableEpisodes, episodes);
    }
  }//GEN-LAST:event_popUpItem_deleteEpisodeActionPerformed

  private void panel_episodesListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_episodesListMouseReleased
    MySeriesLogger.logger.log(Level.INFO, "Table episodes event");
    tableEpisodesMouseReleased(evt);
}//GEN-LAST:event_panel_episodesListMouseReleased

  private void popUpItem_GoToTvSubsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_GoToTvSubsActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Go to subtitles page action");
    SeriesActions.goToSubtitlePage(this, SubtitleConstants.TV_SUBTITLES_NAME);
  }//GEN-LAST:event_popUpItem_GoToTvSubsActionPerformed

  private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    MySeriesLogger.logger.log(Level.INFO, "Exit application action");
    //Options.setOption(Options.WINDOW_STATE, getExtendedState());
    //Options.save();
    ApplicationActions.exitApplication(this);
  }//GEN-LAST:event_formWindowClosing

  private void menuItem_exportEpisodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_exportEpisodesActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Export episodes action");
    EpisodesActions.exportEpisodes(this);
  }//GEN-LAST:event_menuItem_exportEpisodesActionPerformed

  private void popUpItem_exportEpisodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_exportEpisodesActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "export episodes action");
    EpisodesActions.exportEpisodes(this);
  }//GEN-LAST:event_popUpItem_exportEpisodesActionPerformed

  private void menuItem_importEpisodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_importEpisodesActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Import episodes action");
    EpisodesActions.importEpisodes(this);
  }//GEN-LAST:event_menuItem_importEpisodesActionPerformed

  private void menuItem_helpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_helpActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Help action");
    ApplicationActions.showHelp(this);
}//GEN-LAST:event_menuItem_helpActionPerformed

  private void menuItem_saveDatabaseAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_saveDatabaseAsActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Save database action");
    DatabaseActions.saveDatase();
  }//GEN-LAST:event_menuItem_saveDatabaseAsActionPerformed

  private void menuItem_IUTvrageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_IUTvrageActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Internet update from tvrage action");
    SeriesActions.internetUpdate(this, InternetUpdate.TV_RAGE_NAME);
}//GEN-LAST:event_menuItem_IUTvrageActionPerformed

  private void popUpItem_IUTvrageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_IUTvrageActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Internet update from tvrage action");
    SeriesActions.internetUpdateSeries(this, InternetUpdate.TV_RAGE_NAME);
  }//GEN-LAST:event_popUpItem_IUTvrageActionPerformed

  private void menuItem_checkUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_checkUpdateActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Check updates action");
    ApplicationActions.checkUpdates();
  }//GEN-LAST:event_menuItem_checkUpdateActionPerformed

  private void menuItem_viewLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_viewLogsActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "View log action");
    ApplicationActions.viewLog(this);
  }//GEN-LAST:event_menuItem_viewLogsActionPerformed

  private void popUpItem_GoToLocalDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_GoToLocalDirActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Go to local dir action");
    SeriesActions.goToLocalDir();
  }//GEN-LAST:event_popUpItem_GoToLocalDirActionPerformed

  private void popUpItem_viewEpisodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_viewEpisodeActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "View episode action");
    EpisodesActions.viewEpisode(tableEpisodes);
  }//GEN-LAST:event_popUpItem_viewEpisodeActionPerformed

  private void panel_SeriesComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_panel_SeriesComponentResized
    MySeriesLogger.logger.log(Level.INFO, "Series resized action");
    imagePanel.relocate(this);
  }//GEN-LAST:event_panel_SeriesComponentResized

  private void popUpItem_renameEpisodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_renameEpisodesActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Rename episodes action");
    EpisodesActions.renameEpisodes(tableEpisodes);
  }//GEN-LAST:event_popUpItem_renameEpisodesActionPerformed

  private void popUpItem_downloadSubsTvSubsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_downloadSubsTvSubsActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Download subtitles from tvsubtitles action");
    EpisodesActions.downloadSubtitles(SubtitleConstants.TV_SUBTITLES_NAME, this);
  }//GEN-LAST:event_popUpItem_downloadSubsTvSubsActionPerformed

  private void popUpItem_downloadEzTvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_downloadEzTvActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Download eztv torrent action");
    EpisodesActions.downloadEpisodesTorrent(TorrentConstants.EZTV_NAME);
  }//GEN-LAST:event_popUpItem_downloadEzTvActionPerformed

  private void tableFiltersMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableFiltersMouseReleased
    try {
      Point p = evt.getPoint();
      int rowSelected = tableFilters.rowAtPoint(p);
      MySeriesLogger.logger.log(Level.INFO, "Filters table row {0} selected", rowSelected);
      EpisodesRecord ep = (EpisodesRecord) tableFilters.getValueAt(rowSelected, 2);
      MySeriesLogger.logger.log(Level.INFO, "Episode {0} selected", ep.getTitle());
      SeriesRecord seriesRec = DBHelper.getSeriesByID(ep.getSeries_ID());
      MySeriesLogger.logger.log(Level.INFO, "Setting current series event");
      MyEvent event = new MyEvent(this, MyEventHandler.SET_CURRENT_SERIES);
      event.setSeries(seriesRec);
      getEvClass().fireMyEvent(event);
      if (evt.getButton() == MouseEvent.BUTTON3) {
        MySeriesLogger.logger.log(Level.INFO, "Setting current episode event");
        event.setType(MyEventHandler.SET_CURRENT_EPISODE);
        event.setEpisode(ep);
        event.setEpisodesPanel(false);
        getEvClass().fireMyEvent(event);
        MySeriesLogger.logger.log(Level.INFO, "Showing episodes popup");
        episodesPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
      } else {
      }
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
    }
  }//GEN-LAST:event_tableFiltersMouseReleased

  private void menuItem_downloadEztvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_downloadEztvActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Download eztv torrent action");
    SeriesActions.downloadTorrent(TorrentConstants.EZTV_NAME);
  }//GEN-LAST:event_menuItem_downloadEztvActionPerformed

  private void popUpItem_IUEpguidesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_IUEpguidesActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Internet update from epguides action");
    SeriesActions.internetUpdateSeries(this, InternetUpdate.EP_GUIDES_NAME);
  }//GEN-LAST:event_popUpItem_IUEpguidesActionPerformed

  private void menuItem_IUEpguidesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_IUEpguidesActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Internet update from epguides action");
    SeriesActions.internetUpdate(this, InternetUpdate.EP_GUIDES_NAME);
  }//GEN-LAST:event_menuItem_IUEpguidesActionPerformed

  private void popUpItem_downloadSubsSubOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_downloadSubsSubOnActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Download subtitles from subsonline action");
    EpisodesActions.downloadSubtitles(SubtitleConstants.SUBTITLE_ONLINE_NAME, this);
  }//GEN-LAST:event_popUpItem_downloadSubsSubOnActionPerformed

  private void popUpItem_GoToSubOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_GoToSubOnActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Download subtitles from subsonline action");
    SeriesActions.goToSubtitlePage(this, SubtitleConstants.SUBTITLE_ONLINE_NAME);
  }//GEN-LAST:event_popUpItem_GoToSubOnActionPerformed

  private void popUpItem_renameEpisodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_renameEpisodeActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Rename episode action");
    EpisodesActions.renameEpisode();
  }//GEN-LAST:event_popUpItem_renameEpisodeActionPerformed

  private void menuItem_DownloadIsohuntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_DownloadIsohuntActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Download torrent from isohunt action");
    SeriesActions.downloadTorrent(TorrentConstants.ISOHUNT_NAME);
  }//GEN-LAST:event_menuItem_DownloadIsohuntActionPerformed

  private void menuItem_updateFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_updateFilesActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Update files action");
    SeriesActions.updateFiles(this);
  }//GEN-LAST:event_menuItem_updateFilesActionPerformed

  private void popUpItem_downloadIsohuntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_downloadIsohuntActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Download torrent from isohunt action");
    EpisodesActions.downloadEpisodesTorrent(TorrentConstants.ISOHUNT_NAME);
  }//GEN-LAST:event_popUpItem_downloadIsohuntActionPerformed

  private void menuItem_restoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_restoreActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Restore series action");
    ApplicationActions.restoreSeries(this);
  }//GEN-LAST:event_menuItem_restoreActionPerformed

  private void feedPreviewPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_feedPreviewPanelComponentResized
    MySeriesLogger.logger.log(Level.INFO, "Feeds preview panel resize action");
    feedPreviewPanel.resize();
  }//GEN-LAST:event_feedPreviewPanelComponentResized

  private void popUpItem_WholeSeasonSubsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpItem_WholeSeasonSubsActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Download season subtitles action");
    SeriesActions.downloadSeasonSubtitles();
  }//GEN-LAST:event_popUpItem_WholeSeasonSubsActionPerformed

  private void bt_rssUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_rssUpdateActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Update feeds action");
    FeedsActions.updateFeeds(false, this);
  }//GEN-LAST:event_bt_rssUpdateActionPerformed

  private void bt_rssAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_rssAddActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Add feed action");
    boolean isFeedSaved = FeedsActions.addFeedPanel(0, this);
}//GEN-LAST:event_bt_rssAddActionPerformed

  private void bt_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_saveActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Save filter action");
    String curFilter = (String) comboBoxModel_filters.getSelectedItem();
    FiltersActions.saveFilter(this);
    comboBoxModel_filters.setSelectedItem(curFilter);
  }//GEN-LAST:event_bt_saveActionPerformed

  private void bt_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_deleteActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Delete filter action");
    FiltersActions.deleteFilter(this);
    if (comboBoxModel_filters.getSize() == 0) {
      combobox_downloaded.setSelectedIndex(Filters.DOWNLOADED_YES);
      comboBox_seen.setSelectedIndex(Filters.SEEN_NO);
      comboBox_filterSubtitles.setSelectedIndex(Filters.PRIMARY);
    }
    FiltersActions.applyFilter(this);
  }//GEN-LAST:event_bt_deleteActionPerformed

  private void menuItem_housekeepingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_housekeepingActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Housekeeping action");
    ApplicationActions.houseKeeping();
  }//GEN-LAST:event_menuItem_housekeepingActionPerformed

  private void menuItem_restartApplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_restartApplicationActionPerformed
    if (MyMessages.confirm("Restart application", "Really restart the application?", true) == JOptionPane.YES_OPTION) {
      ApplicationActions.restartApplication(this);
    }
  }//GEN-LAST:event_menuItem_restartApplicationActionPerformed

  private void menuItem_customizeToolbarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_customizeToolbarActionPerformed
    ApplicationActions.customizeToolbar(this);
  }//GEN-LAST:event_menuItem_customizeToolbarActionPerformed

  private void menuItem_googleCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_googleCodeActionPerformed
    ApplicationActions.googleCodePage();
  }//GEN-LAST:event_menuItem_googleCodeActionPerformed

  private void menuItem_reportBugActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_reportBugActionPerformed
    ApplicationActions.chooseContactWay();
  }//GEN-LAST:event_menuItem_reportBugActionPerformed

  private void menuItem_paypalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_paypalActionPerformed
    ApplicationActions.paypalDonation();
  }//GEN-LAST:event_menuItem_paypalActionPerformed

  private void myToolbarPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_myToolbarPropertyChange
    if (evt.getPropertyName().equals("orientation")) {
      MyUsefulFunctions.createMemoryCons(this);
    }
  }//GEN-LAST:event_myToolbarPropertyChange

  private void menuItem_showErrorPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_showErrorPanelActionPerformed
    logPanel.setVisible(menuItem_showErrorPanel.isSelected());
  }//GEN-LAST:event_menuItem_showErrorPanelActionPerformed

  private void menu_HelpMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_menu_HelpMenuSelected
    menuItem_showErrorPanel.setSelected(logPanel.isVisible());
  }//GEN-LAST:event_menu_HelpMenuSelected

  private void formWindowIconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowIconified
    if (myTrayIcon.getTrayIcon() != null && Options.toBoolean(Options.MINIMIZE_TO_TRAY)) {
      myTrayIcon.addIconToTray();
      setVisible(false);
    }
  }//GEN-LAST:event_formWindowIconified

  private void formWindowDeiconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeiconified
    //setExtendedState(Options.toInt(Options.WINDOW_STATE));
  }//GEN-LAST:event_formWindowDeiconified

  private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
    if (evt.getNewState() != NORMAL && evt.getNewState() != MAXIMIZED_BOTH  ) {
      Options.setOption(Options.WINDOW_STATE, evt.getOldState());
      Options.save();
    } else {
      Options.setOption(Options.WINDOW_STATE, evt.getNewState());
      Options.save();
    }
  }//GEN-LAST:event_formWindowStateChanged

  private void menuItem_LatestNewsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_LatestNewsActionPerformed
    ApplicationActions.latestNews(this,false);
  }//GEN-LAST:event_menuItem_LatestNewsActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  public javax.swing.JMenuItem PopUpItem_AddEpisode;
  public javax.swing.JMenuItem PopUpItem_AddEpisodeInEpisodes;
  public javax.swing.JMenuItem PopUpItem_AddSeries;
  public javax.swing.JMenuItem PopUpItem_DeleteSerial;
  public javax.swing.JMenuItem PopUpItem_EditSerial;
  public myComponents.myGUI.buttons.MyButtonCancel bt_delete;
  public myComponents.myGUI.buttons.MyDefaultButton bt_rssAdd;
  public myComponents.myGUI.buttons.MyDefaultButton bt_rssUpdate;
  public myComponents.myGUI.buttons.MyButtonSave bt_save;
  public javax.swing.JComboBox comboBox_filterSubtitles;
  public javax.swing.JComboBox comboBox_seen;
  public javax.swing.JComboBox combobox_downloaded;
  public javax.swing.JComboBox combobox_filters;
  public javax.swing.JPopupMenu episodesPopUp;
  public tools.feeds.FeedPreviewPanel feedPreviewPanel;
  public javax.swing.JSplitPane feedSplitPanel;
  public tools.feeds.FeedTree feedTree;
  public javax.swing.JLayeredPane imageLayerPanel;
  public javax.swing.JSeparator jSeparator1;
  public javax.swing.JSeparator jSeparator2;
  public javax.swing.JSeparator jSeparator3;
  public javax.swing.JPanel leftFeedPanel;
  public myComponents.MyLogPanel logPanel;
  public javax.swing.JMenuBar menuBar;
  public javax.swing.JMenuItem menuItem_About;
  public javax.swing.JMenuItem menuItem_DownloadIsohunt;
  public javax.swing.JMenuItem menuItem_IUEpguides;
  public javax.swing.JMenuItem menuItem_IUTvrage;
  public javax.swing.JMenuItem menuItem_LatestNews;
  public javax.swing.JMenuItem menuItem_addSeries;
  public javax.swing.JMenuItem menuItem_checkUpdate;
  public javax.swing.JMenuItem menuItem_createDB;
  public javax.swing.JMenuItem menuItem_customizeToolbar;
  public javax.swing.JMenuItem menuItem_deleteSeries;
  public javax.swing.JMenuItem menuItem_downloadEztv;
  public javax.swing.JMenuItem menuItem_editEpisode;
  public javax.swing.JMenuItem menuItem_editSeries;
  public javax.swing.JMenuItem menuItem_exit;
  public javax.swing.JMenuItem menuItem_exportEpisodes;
  public javax.swing.JMenuItem menuItem_googleCode;
  public javax.swing.JMenuItem menuItem_help;
  public javax.swing.JMenuItem menuItem_housekeeping;
  public javax.swing.JMenuItem menuItem_importEpisodes;
  public javax.swing.JMenuItem menuItem_loadDatabase;
  public javax.swing.JMenuItem menuItem_options;
  public javax.swing.JMenuItem menuItem_paypal;
  public javax.swing.JMenuItem menuItem_reportBug;
  public javax.swing.JMenuItem menuItem_restartApplication;
  public javax.swing.JMenuItem menuItem_restore;
  public javax.swing.JMenuItem menuItem_saveDatabaseAs;
  public javax.swing.JCheckBoxMenuItem menuItem_showErrorPanel;
  public javax.swing.JMenuItem menuItem_updateFiles;
  public javax.swing.JMenuItem menuItem_viewLogs;
  public javax.swing.JMenu menu_Edit;
  public javax.swing.JMenu menu_Help;
  public javax.swing.JMenu menu_InternetUpdate;
  public javax.swing.JMenu menu_MySeries;
  public javax.swing.JMenu menu_Tools;
  public javax.swing.JMenu menu_Torrents;
  public javax.swing.JMenu menu_online;
  public myComponents.myToolbar.Toolbar myToolbar;
  public javax.swing.JPanel panel_Series;
  public javax.swing.JScrollPane panel_allSeriesEpisodes;
  public javax.swing.JPanel panel_episodes;
  public javax.swing.JScrollPane panel_episodesList;
  public javax.swing.JPanel panel_filters;
  public javax.swing.JMenuItem popUpItem_GoToLocalDir;
  public javax.swing.JMenuItem popUpItem_GoToSubOn;
  public javax.swing.JMenuItem popUpItem_GoToTvSubs;
  public javax.swing.JMenuItem popUpItem_IUEpguides;
  public javax.swing.JMenuItem popUpItem_IUTvrage;
  public javax.swing.JMenuItem popUpItem_WholeSeasonSubs;
  public javax.swing.JMenuItem popUpItem_deleteEpisode;
  public javax.swing.JMenuItem popUpItem_downloadEzTv;
  public javax.swing.JMenuItem popUpItem_downloadIsohunt;
  public javax.swing.JMenuItem popUpItem_downloadSubsSubOn;
  public javax.swing.JMenuItem popUpItem_downloadSubsTvSubs;
  public javax.swing.JMenuItem popUpItem_exportEpisodes;
  public javax.swing.JMenuItem popUpItem_renameEpisode;
  public javax.swing.JMenuItem popUpItem_renameEpisodes;
  public javax.swing.JMenuItem popUpItem_viewEpisode;
  public javax.swing.JMenu popUpMenu_GoToSubtitles;
  public javax.swing.JMenu popUpMenu_downloadSubtitles;
  public javax.swing.JMenu popUpMenu_downloadTorrent;
  public javax.swing.JMenu popUpMenu_internetUpdate;
  public javax.swing.JProgressBar pr_rssUpdating;
  public myseries.schedule.Schedule scheduler;
  public javax.swing.JScrollPane scrollPane_series;
  public javax.swing.JPopupMenu seriesPopUp;
  public javax.swing.JSplitPane splitPane_main;
  public myseries.statistics.StatEpisodes statEpisodes;
  public myseries.statistics.StatSeries statSeries;
  public javax.swing.JTable tableEpisodes;
  public javax.swing.JTable tableFilters;
  public javax.swing.JTable tableSeries;
  public javax.swing.JPanel tabpanel_FilteredSeries;
  public javax.swing.JPanel tabpanel_episodesList;
  public javax.swing.JPanel tabpanel_feeds;
  public javax.swing.JPanel tabpanel_schedule;
  public javax.swing.JPanel tabpanel_statistics;
  public myComponents.myGUI.MyDnDTabbedPane tabsPanel;
  // End of variables declaration//GEN-END:variables

  public int getToolbarPosition() {
    MySeriesLogger.logger.log(Level.INFO, "Getting the toolbar position");
    BorderLayout layout = (BorderLayout) getContentPane().getLayout();
    Component[] comps = new Component[4];
    comps[0] = layout.getLayoutComponent(getContentPane(), BorderLayout.NORTH);
    comps[1] = layout.getLayoutComponent(getContentPane(), BorderLayout.EAST);
    comps[2] = layout.getLayoutComponent(getContentPane(), BorderLayout.SOUTH);
    comps[3] = layout.getLayoutComponent(getContentPane(), BorderLayout.WEST);
    for (int i = 0; i < comps.length; i++) {
      Component component = comps[i];
      if (component instanceof JToolBar) {
        MySeriesLogger.logger.log(Level.FINE, "Tollbar position is {0}", i);
        return i;
      }
    }
    return -1;
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    MySeriesLogger.logger.log(Level.INFO, "Table change event");
    if (e.getSource() instanceof MyEpisodesTableModel) {
      MySeriesLogger.logger.log(Level.INFO, "Table that changed is episodes");
      new UpdateEpisodesTable(e);
    } else if (e.getSource() instanceof MySeriesTableModel) {
      MySeriesLogger.logger.log(Level.INFO, "Table that changed is series");
      new UpdateSeriesTable(e, this);
    } else if (e.getSource() instanceof MyFilteredSeriesTableModel) {
      MySeriesLogger.logger.log(Level.INFO, "Table that changed is filters");
      new UpdateFiltersTable(e, this);
    }

  }

  public void createComboBox_filters() {
    comboBox_filterSubtitles.setModel(new DefaultComboBoxModel(
        new String[]{
          SubtitleConstants.NONE,
          languages.getPrimary().getName(),
          languages.getSecondary().getName(),
          SubtitleConstants.BOTH,
          languages.getPrimary().getName() + " or " + languages.getSecondary().getName(),
          "Not " + languages.getPrimary().getName(),
          SubtitleConstants.UNAWARE
        }));
  }

  public int getSeriesTableRow(SeriesRecord series) {
    if (series == null) {
      return -1;
    }
    TableModel model = tableSeries.getModel();
    for (int i = 0; i < model.getRowCount(); i++) {
      SeriesRecord s = (SeriesRecord) model.getValueAt(i, 0);
      if (s.getSeries_ID() == series.getSeries_ID()) {
        return i;
      }
    }
    return -1;
  }

  /**
   * @return the evClass
   */
  public MyEventsClass getEvClass() {
    return evClass;
  }

  /**
   * @param evClass the evClass to set
   */
  public void setEvClass(MyEventsClass evClass) {
    this.evClass = evClass;
  }

  public static Class<MySeries> getInstance() {
    return MySeries.class;
  }

  @Override
  public void dispose() {
    ApplicationActions.exitApplication(this);
    super.dispose();
  }
}
