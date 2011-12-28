/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AddSeries.java
 *
 * Created on 30 Οκτ 2008, 8:04:57 μμ
 */
package myseriesproject.series;

import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import myComponents.myGUI.CopyScreenshot;
import tools.MySeriesLogger;
import myComponents.MyUsefulFunctions;
import database.SeriesRecord;
import java.awt.Dialog.ModalityType;
import java.awt.Image;
import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.SpinnerNumberModel;
import myComponents.MyMessages;
import myComponents.myFileFilters.ScreenshotFilter;
import myComponents.myGUI.MyDraggable;
import myseriesproject.MySeries;
import com.googlecode.svalidators.formcomponents.STextField;
import com.googlecode.svalidators.formcomponents.ValidationGroup;
import com.googlecode.svalidators.validators.FileValidator;
import com.googlecode.svalidators.validators.NoSpaceValidator;
import com.googlecode.svalidators.validators.PositiveNumberValidator;
import com.googlecode.svalidators.validators.RequiredValidator;
import help.HelpWindow;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.DocumentListener;
import myComponents.myEvents.MyEvent;
import myComponents.myEvents.MyEventHandler;
import myComponents.myEvents.MyEventsClass;
import myComponents.myGUI.MyScaledImage;
import myComponents.myTableCellRenderers.MyQualityListRenderer;
import myseriesproject.actions.SeriesActions;
import tools.download.screenshot.DownloadScreenshot;
import tools.download.subtitles.sonline.GetSubtitleOnlineCode;
import tools.download.subtitles.tvsubtitles.GetTvSubtitlesCode;
import tools.internetUpdate.InternetUpdate;
import tools.internetUpdate.tvrage.TrGetId;
import tools.options.MySeriesOptions;
import tools.options.Paths;

/**
 * Administrate series
 * @author lordovol
 */
public class AdminSeries extends MyDraggable {

  private MyEventsClass evClass;
  private final static long serialVersionUID = 43547453657578L;
  /**
   * MySeries main form
   */
  private MySeries m;
  /**
   * The series season
   */
  private int season;
  /**
   * The series title
   */
  private String serial;
  /**
   * The series record
   */
  private SeriesRecord seriesRecord;
  /**
   * The screenshot
   */
  private String screenshot;
  /**
   * The existing series model
   */
  private DefaultComboBoxModel existingSeriesModel = new DefaultComboBoxModel();
  /**
   * If it's a new series
   */
  private boolean newSeries = false;

  /**
   * Creates the adminSeries form
   * @param m The main mySeries form
   * @throws java.sql.SQLException
   */
  public AdminSeries(MySeries m) throws SQLException {
    this(m, new SeriesRecord());

  }

  /**
   * Creates the adminSeries form
   * @param m The main mySeries form
   * @param seriesRecord The series to edit.If null it creates a new series
   * @throws java.sql.SQLException
   */
  public AdminSeries(MySeries m, SeriesRecord seriesRecord) throws SQLException {
    this.m = m;
    evClass = new MyEventsClass(m);
    this.seriesRecord = seriesRecord;
    MySeriesLogger.logger.log(Level.INFO, "Initializing components");
    initComponents();
    MySeriesLogger.logger.log(Level.FINE, "Components initialized");
    setLocationRelativeTo(m);
    spinner_season.setModel(new SpinnerNumberModel(Series.DEFAULT_SEASON, Series.MINIMUM_SEASON, Series.MAXIMUM_SEASON, Series.SEASON_STEP));
    combo_quality.setModel(new DefaultComboBoxModel(Quality.getAllQualities().toArray()));
    combo_quality.setRenderer(new MyQualityListRenderer());
    if (seriesRecord.getSeries_ID() > 0) {
      newSeries = false;
      cb_existingSeries.setVisible(false);
      MySeriesLogger.logger.log(Level.INFO, "Editing series {0}", seriesRecord.getFullTitle());
      spinner_season.setValue(seriesRecord.getSeason());
      textField_Serial.setText(seriesRecord.getTitle());
      label_Title.setText("Edit Series " + seriesRecord.getFullTitle());
      textfield_tvSubsId.setText(seriesRecord.getTvSubtitlesCode());
      textfield_tvRageID.setText(String.valueOf(seriesRecord.getTvrage_ID()));
      textfield_localDir.setText(seriesRecord.getLocalDir());
      textfield_screenshot.setText(seriesRecord.getScreenshot().equals("") ? "" : MySeriesOptions._USER_DIR_ + Paths.SCREENSHOTS_PATH + seriesRecord.getScreenshot());
      setScreenshot();
      textfield_tvSubsId.setText(seriesRecord.getTvSubtitlesCode());
      textfield_subsOnline.setText(seriesRecord.getSOnlineCode());
      combo_quality.setSelectedIndex(seriesRecord.getQuality());
      // button_Add.setText("Edit");
    } else {
      newSeries = true;
      MySeriesLogger.logger.log(Level.INFO, "Adding new series");
      label_Title.setText("Add New Series");
      createExistingSeriesModel();
    }
    textfield_screenshot.getDocument().addDocumentListener(new DocumentListener() {

      @Override
      public void insertUpdate(DocumentEvent e) {
        setScreenshot();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        setScreenshot();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        setScreenshot();
      }
    });
    setVisible(true);
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        main_panel = new javax.swing.JPanel();
        inner_panel = new javax.swing.JPanel();
        label_title = new javax.swing.JLabel();
        textField_Serial = new STextField(new RequiredValidator());
        label_season = new javax.swing.JLabel();
        spinner_season = new javax.swing.JSpinner();
        label_tvsubsId = new javax.swing.JLabel();
        label_tvrageId = new javax.swing.JLabel();
        textfield_tvRageID = new STextField(new PositiveNumberValidator("",true,true));
        label_localDir = new javax.swing.JLabel();
        label_screenshot = new javax.swing.JLabel();
        label_subOnlineId = new javax.swing.JLabel();
        label_message = new javax.swing.JLabel();
        textfield_localDir = new com.googlecode.svalidators.formcomponents.STextField(new FileValidator("",FileValidator.Type.DIR,true));
        textfield_screenshot = new com.googlecode.svalidators.formcomponents.STextField(new FileValidator("", FileValidator.Type.FILE, true));
        textfield_subsOnline = new com.googlecode.svalidators.formcomponents.STextField(new NoSpaceValidator("",true));
        textfield_tvSubsId = new com.googlecode.svalidators.formcomponents.STextField(new NoSpaceValidator("",true));
        jLabel1 = new javax.swing.JLabel();
        checkbox_updateEpisodes = new javax.swing.JCheckBox();
        scrPanel = new myComponents.myGUI.MyImagePanel(false);
        bt_localDir = new myComponents.myGUI.buttons.MyButtonDir();
        bt_screenshot = new myComponents.myGUI.buttons.MyButtonDir();
        bt_iuScreenshot = new myComponents.myGUI.buttons.MyButtonInternet();
        bt_tvSubs = new myComponents.myGUI.buttons.MyButtonInternet();
        bt_subsOn = new myComponents.myGUI.buttons.MyButtonInternet();
        bt_tvRage = new myComponents.myGUI.buttons.MyButtonInternet();
        cb_existingSeries = new javax.swing.JComboBox();
        label_quality = new javax.swing.JLabel();
        combo_quality = new javax.swing.JComboBox();
        myButtonHelp1 = new myComponents.myGUI.buttons.MyButtonHelp();
        label_Title = new javax.swing.JLabel();
        myButtonOk1 = new myComponents.myGUI.buttons.MyButtonOk();
        myButtonCancel1 = new myComponents.myGUI.buttons.MyButtonCancel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        main_panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        inner_panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        label_title.setFont(label_title.getFont());
        label_title.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label_title.setText("Title:");

        textField_Serial.setName("Title"); // NOI18N

        label_season.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label_season.setText("Season :");

        spinner_season.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinner_seasonStateChanged(evt);
            }
        });

        label_tvsubsId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label_tvsubsId.setText("TvSubtitles ID :");

        label_tvrageId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label_tvrageId.setText("TvRage ID :");

        textfield_tvRageID.setName("TvRage ID"); // NOI18N

        label_localDir.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label_localDir.setText("Local Dir :");

        label_screenshot.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label_screenshot.setText("Screenshot :");

        label_subOnlineId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label_subOnlineId.setText("SubtitleOnline ID :");

        textfield_localDir.setName("Local Directory"); // NOI18N

        textfield_screenshot.setName("Screenshot"); // NOI18N

        textfield_subsOnline.setName("SubtitleOnline ID"); // NOI18N

        textfield_tvSubsId.setName("TvSubtitles ID"); // NOI18N

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Update episodes list :");

        checkbox_updateEpisodes.setSelected(seriesRecord.getSeries_ID() ==0);
        checkbox_updateEpisodes.setEnabled(seriesRecord.getSeries_ID() == 0);
        checkbox_updateEpisodes.setMargin(new java.awt.Insets(0, 0, 0, 0));
        checkbox_updateEpisodes.setOpaque(false);

        scrPanel.setMaximumSize(new java.awt.Dimension(180, 110));

        javax.swing.GroupLayout scrPanelLayout = new javax.swing.GroupLayout(scrPanel);
        scrPanel.setLayout(scrPanelLayout);
        scrPanelLayout.setHorizontalGroup(
            scrPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 225, Short.MAX_VALUE)
        );
        scrPanelLayout.setVerticalGroup(
            scrPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 146, Short.MAX_VALUE)
        );

        bt_localDir.setToolTipText("Browse for the local Directory");
        bt_localDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_localDirActionPerformed(evt);
            }
        });

        bt_screenshot.setToolTipText("Browse for a screenshot");
        bt_screenshot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_screenshotActionPerformed(evt);
            }
        });

        bt_iuScreenshot.setToolTipText("Download screenshot");
        bt_iuScreenshot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_iuScreenshotActionPerformed(evt);
            }
        });

        bt_tvSubs.setToolTipText("Get TvSubtitles ID");
        bt_tvSubs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_tvSubsActionPerformed(evt);
            }
        });

        bt_subsOn.setToolTipText("Get subtitleOnline ID");
        bt_subsOn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_subsOnActionPerformed(evt);
            }
        });

        bt_tvRage.setToolTipText("Get TvRage ID");
        bt_tvRage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_tvRageActionPerformed(evt);
            }
        });

        cb_existingSeries.setModel(existingSeriesModel);
        cb_existingSeries.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_existingSeriesActionPerformed(evt);
            }
        });

        label_quality.setText("Quality :");

        javax.swing.GroupLayout inner_panelLayout = new javax.swing.GroupLayout(inner_panel);
        inner_panel.setLayout(inner_panelLayout);
        inner_panelLayout.setHorizontalGroup(
            inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inner_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label_tvrageId)
                    .addComponent(label_season)
                    .addComponent(label_title)
                    .addComponent(label_localDir)
                    .addComponent(label_screenshot)
                    .addComponent(label_tvsubsId)
                    .addComponent(label_subOnlineId)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inner_panelLayout.createSequentialGroup()
                        .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textField_Serial, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                            .addComponent(textfield_localDir, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                            .addComponent(textfield_screenshot, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                            .addGroup(inner_panelLayout.createSequentialGroup()
                                .addComponent(spinner_season, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(label_quality)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(combo_quality, 0, 123, Short.MAX_VALUE)))
                        .addGap(6, 6, 6)
                        .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(inner_panelLayout.createSequentialGroup()
                                .addComponent(bt_iuScreenshot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bt_screenshot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(bt_localDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb_existingSeries, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29))
                    .addGroup(inner_panelLayout.createSequentialGroup()
                        .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(inner_panelLayout.createSequentialGroup()
                                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(textfield_tvRageID, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(textfield_subsOnline, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(textfield_tvSubsId, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(bt_subsOn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bt_tvSubs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bt_tvRage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(checkbox_updateEpisodes))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        inner_panelLayout.setVerticalGroup(
            inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inner_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(label_title)
                    .addComponent(textField_Serial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_existingSeries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(label_season)
                    .addComponent(spinner_season, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_quality)
                    .addComponent(combo_quality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(label_localDir)
                    .addComponent(textfield_localDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_localDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(label_screenshot)
                    .addComponent(textfield_screenshot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_screenshot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_iuScreenshot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inner_panelLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(label_tvsubsId)
                            .addComponent(textfield_tvSubsId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bt_tvSubs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(label_subOnlineId)
                            .addComponent(textfield_subsOnline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bt_subsOn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(label_tvrageId)
                            .addComponent(textfield_tvRageID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bt_tvRage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkbox_updateEpisodes)
                            .addComponent(jLabel1)))
                    .addGroup(inner_panelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(45, 45, 45))
        );

        myButtonHelp1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myButtonHelp1ActionPerformed(evt);
            }
        });

        label_Title.setFont(label_Title.getFont().deriveFont(label_Title.getFont().getStyle() | java.awt.Font.BOLD, label_Title.getFont().getSize()+2));
        label_Title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_Title.setText("Add A New Series");

        myButtonOk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myButtonOk1ActionPerformed(evt);
            }
        });

        myButtonCancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myButtonCancel1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout main_panelLayout = new javax.swing.GroupLayout(main_panel);
        main_panel.setLayout(main_panelLayout);
        main_panelLayout.setHorizontalGroup(
            main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, main_panelLayout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(label_Title, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(myButtonHelp1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(myButtonCancel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(myButtonOk1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(main_panelLayout.createSequentialGroup()
                        .addComponent(inner_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        main_panelLayout.setVerticalGroup(
            main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_panelLayout.createSequentialGroup()
                .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(myButtonHelp1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(label_Title))
                    .addComponent(myButtonCancel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inner_panel, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myButtonOk1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  /**
   * Adds the series in the database
   * @throws java.io.IOException
   */
  private void addSeries() {
    MyEvent ev;
    if (newSeries && seriesExist()) {
      MySeriesLogger.logger.log(Level.WARNING, "Series {0} season {1} already exists", new Object[]{
            textField_Serial.getText(), spinner_season.getValue()});
      MyMessages.warning("Add new Series", "Series already exists", true);
      return;
    }
    try {
      int tvRageID = 0;
      try {
        tvRageID = Integer.parseInt(textfield_tvRageID.getText());
      } catch (NumberFormatException ex) {
        MySeriesLogger.logger.log(Level.WARNING, "Wrong tvrage id");
      }
      screenshot = textfield_screenshot.getText().trim();
      season = Integer.parseInt(String.valueOf(spinner_season.getValue()).trim());
      serial = textField_Serial.getText().trim();
      if (Series.getCurrentSerial().getSeries_ID() == 0) {
        seriesRecord = new SeriesRecord();
      }
      seriesRecord.setTitle(serial);
      seriesRecord.setSeason(season);
      seriesRecord.setTvSubtitlesCode(textfield_tvSubsId.getText().trim());
      seriesRecord.setTvrage_ID(tvRageID);
      seriesRecord.setLocalDir(textfield_localDir.getText());
      seriesRecord.setTvSubtitlesCode(textfield_tvSubsId.getText().trim());
      seriesRecord.setSOnlineCode(textfield_subsOnline.getText().trim());
      seriesRecord.setQuality(combo_quality.getSelectedIndex());
      File sc = null;
      if (!screenshot.equals("")) {
        sc = new File(screenshot);
        if (sc.isFile() && !sc.getAbsoluteFile().getParent().equals(new File("images").getAbsolutePath())) {
          CopyScreenshot c = new CopyScreenshot(screenshot);
          //Thread t = new Thread(c);
          //t.start();
          c.run();
          seriesRecord.setScreenshot(sc.getName());
        } else {
          seriesRecord.setScreenshot(sc.getName());
        }
        Image im = new ImageIcon(sc.getAbsolutePath()).getImage();
        m.imagePanel.setImage(im, false, m);

      } else {
        seriesRecord.setScreenshot("");
        m.imagePanel.setImage(null, true, m);
      }
      try {
        int series_ID = seriesRecord.save();
        MySeriesLogger.logger.log(Level.FINE, "Series saved");
        if (series_ID > 0) {
          seriesRecord.setSeries_ID(series_ID);
        }
        ev = new MyEvent(this, MyEventHandler.SERIES_UPDATE);
        evClass.fireMyEvent(ev);
        MySeries.glassPane.deactivate();
        dispose();
        ev.setType(MyEventHandler.SET_CURRENT_SERIES);
        ev.setSeries(seriesRecord);
        m.getEvClass().fireMyEvent(ev);
        if (checkbox_updateEpisodes.isSelected()) {
          SeriesActions.internetUpdateSeries(m, InternetUpdate.TV_RAGE_NAME);
        }
      } catch (SQLException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "SQL error occured", ex);
      }
    } catch (NumberFormatException ex) {
      MySeriesLogger.logger.log(Level.WARNING, "Season must be a number", ex);
      MyMessages.warning("Season not a number!!!", "Season must be a number", true);
    }
  }

    private void myButtonHelp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myButtonHelp1ActionPerformed
      new HelpWindow(HelpWindow.ADMIN_SERIES);
    }//GEN-LAST:event_myButtonHelp1ActionPerformed

    private void myButtonCancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myButtonCancel1ActionPerformed
      MySeriesLogger.logger.log(Level.INFO, "Action aborted by the user");
      MySeries.glassPane.deactivate();
      dispose();
    }//GEN-LAST:event_myButtonCancel1ActionPerformed

    private void myButtonOk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myButtonOk1ActionPerformed
      ValidationGroup group = new ValidationGroup();
      group.addComponent(textField_Serial);
      group.addComponent(textfield_subsOnline);
      group.addComponent(textfield_tvSubsId);
      group.addComponent(textfield_tvRageID);
      group.addComponent(textfield_localDir);
      group.addComponent(textfield_screenshot);
      MySeriesLogger.logger.log(Level.INFO, "Validating input");
      if (!group.validate()) {
        MySeriesLogger.logger.log(Level.WARNING, "Validation error :\n", group.getErrorMessage());
        MyMessages.validationError("Series Form", group.getErrorMessage());
        return;
      }
      addSeries();
    }//GEN-LAST:event_myButtonOk1ActionPerformed

    private void bt_localDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_localDirActionPerformed
      MySeriesLogger.logger.log(Level.INFO, "Browsing for local directory");
      JFileChooser fc = new JFileChooser();
      String md = textfield_localDir.getText().equals("") ? MySeries.options.getStringOption(MySeriesOptions.MAIN_DIRECTORY) : textfield_localDir.getText();
      fc.setCurrentDirectory(new File(md));
      fc.setDialogTitle("Choose the Series directory");
      fc.setDialogType(JFileChooser.OPEN_DIALOG);
      fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      fc.showOpenDialog(this);
      if (fc.getSelectedFile() != null) {
        File localDir = fc.getSelectedFile();
        textfield_localDir.setText(localDir.getAbsolutePath());
        MySeriesLogger.logger.log(Level.FINE, "Local directory set to {0}", localDir);
      }
    }//GEN-LAST:event_bt_localDirActionPerformed

    private void bt_screenshotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_screenshotActionPerformed
      MySeriesLogger.logger.log(Level.INFO, "Browsing for screenshot");
      JFileChooser fc = new JFileChooser();
      File d;
      if (textfield_screenshot.getText().trim().equals("")) {
        d = new File(MySeries.options.getStringOption(MySeriesOptions.MAIN_DIRECTORY));
      } else {
        d = new File(textfield_screenshot.getText().trim()).getParentFile();
      }
      fc.setCurrentDirectory(d);
      fc.setDialogTitle("Choose the Series screenshot");
      fc.setDialogType(JFileChooser.OPEN_DIALOG);
      fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
      fc.setFileFilter(new ScreenshotFilter());
      fc.showOpenDialog(this);
      File fScreenshot = fc.getSelectedFile();
      if (fScreenshot != null) {
        MySeriesLogger.logger.log(Level.FINE, "Screenshot set to {0}", fScreenshot);
        textfield_screenshot.setText(fScreenshot.getAbsolutePath());
      }
    }//GEN-LAST:event_bt_screenshotActionPerformed

    private void bt_iuScreenshotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_iuScreenshotActionPerformed
      MySeriesLogger.logger.log(Level.INFO, "Downloading screenshot from tvrage");
      if (textfield_tvRageID.getText().equals("") || textfield_tvRageID.getText().equals("0")) {
        MySeriesLogger.logger.log(Level.WARNING, "Tvrage id is not available");
        setModalityType(ModalityType.MODELESS);
        dispose();
        TrGetId g = new TrGetId(this, textField_Serial.getText().trim(), true);
      } else if (MyUsefulFunctions.isNumeric(textfield_tvRageID.getText())) {
        setVisible(false);
        int tvRageId = Integer.parseInt(textfield_tvRageID.getText());
        if (tvRageId > 0) {
          DownloadScreenshot g = new DownloadScreenshot(Integer.parseInt(textfield_tvRageID.getText()), textField_Serial.getText().trim());
          if (g.isSuccess()) {
            textfield_screenshot.setText(g.getFilename());
            MySeriesLogger.logger.log(Level.FINE, "Screenshot downloaded");
            MyMessages.message("Downloading screenshot", "The screenshot was saved in the images folder");
          } else {
            MySeriesLogger.logger.log(Level.WARNING, "Screenshot not found");
            MyMessages.warning("Downloading screenshot", "No screenshot was found", true);
    }//GEN-LAST:event_bt_iuScreenshotActionPerformed

      }
      setVisible(true);
    }
  }
          private void bt_tvSubsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_tvSubsActionPerformed
            MySeriesLogger.logger.log(Level.INFO, "Getting tv subtitles code");
            if (seriesRecord.getSeries_ID() == 0) {
              if (textField_Serial.getText().trim().equals("")) {
                MyMessages.warning("Empty title", "The series title shouldn't be empty", true);
                return;
              } else {
                seriesRecord = new SeriesRecord();
                seriesRecord.setTitle(textField_Serial.getText().trim());
                seriesRecord.setSeason(Integer.parseInt(String.valueOf(spinner_season.getValue())));
              }
            }
            setModalityType(ModalityType.MODELESS);
            setVisible(false);
            GetTvSubtitlesCode c = new GetTvSubtitlesCode(seriesRecord);
            String id = c.tSubCode;
            if (id == null) {
              MySeriesLogger.logger.log(Level.WARNING, "TvSubtitles code not found");
            } else {
              textfield_tvSubsId.setText(c.tSubCode);
              label_message.setText("");
              label_message.setText("");
              MySeriesLogger.logger.log(Level.FINE, "Found TvSubtitles code {0}", c.tSubCode);
            }
            setVisible(true);
          }//GEN-LAST:event_bt_tvSubsActionPerformed

          private void bt_subsOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_subsOnActionPerformed
            MySeriesLogger.logger.log(Level.INFO, "Getting subtitles online code");
            setModalityType(ModalityType.MODELESS);
            setVisible(false);
            GetSubtitleOnlineCode c = new GetSubtitleOnlineCode(seriesRecord);
            String id = c.subtitleOnlineCode;
            if (id == null) {
              MySeriesLogger.logger.log(Level.WARNING, "Subtitles online code not found");
            } else {
              textfield_subsOnline.setText(c.subtitleOnlineCode);
              label_message.setText("");
              label_message.setText("");
              MySeriesLogger.logger.log(Level.FINE, "Found TvSubtitles code {0}", c.subtitleOnlineCode);
            }
            setVisible(true);
          }//GEN-LAST:event_bt_subsOnActionPerformed

          private void bt_tvRageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_tvRageActionPerformed
            MySeriesLogger.logger.log(Level.INFO, "Getting tvRage id");
            setModalityType(ModalityType.MODELESS);
            dispose();
            TrGetId g = new TrGetId(this, textField_Serial.getText().trim());
            //setVisible(true);
            //textfield_tvRageID.setText(String.valueOf(g.tvRageID));
          }//GEN-LAST:event_bt_tvRageActionPerformed

          private void cb_existingSeriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_existingSeriesActionPerformed
            if (cb_existingSeries.getSelectedIndex() > 0) {
              String title = (String) cb_existingSeries.getSelectedItem();
              ResultSet rs = null;
              try {
                rs = SeriesRecord.query("SELECT * FROM series WHERE "+SeriesRecord.C_TITLE+" ='"
                    + title + "'  ORDER BY season DESC LIMIT 1");
                while (rs.next()) {
                  textField_Serial.setText(rs.getString(SeriesRecord.C_TITLE));
                  textfield_screenshot.setText("./images/" + rs.getString(SeriesRecord.C_SCREENSHOT));
                  textfield_subsOnline.setText(rs.getString(SeriesRecord.C_SONLINE));
                  textfield_tvSubsId.setText(rs.getString(SeriesRecord.C_TV_SUBTITLES_CODE));
                  textfield_tvRageID.setText(String.valueOf(rs.getInt(SeriesRecord.C_TVRAGE_ID)));
                  textfield_localDir.setText(MySeries.options.getStringOption(MySeriesOptions.MAIN_DIRECTORY));
                  spinner_season.setValue(rs.getInt(SeriesRecord.C_SEASON)+1);
                }
              } catch (SQLException ex) {
                MySeriesLogger.logger.log(Level.SEVERE, "Could not get series info", ex);
              } finally {
                if (rs != null) {
                  try {
                    rs.close();
                  } catch (SQLException ex) {
                    Logger.getLogger(AdminSeries.class.getName()).log(Level.SEVERE, null, ex);
                  }
                }
              }
            }
          }//GEN-LAST:event_cb_existingSeriesActionPerformed

          private void spinner_seasonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinner_seasonStateChanged
              int selSeason = Integer.parseInt(String.valueOf(spinner_season.getValue()));
              String id = textfield_tvSubsId.getText();
              id = id.replaceFirst("\\-\\d","-"+selSeason );
              textfield_tvSubsId.setText(id);
          }//GEN-LAST:event_spinner_seasonStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private myComponents.myGUI.buttons.MyButtonInternet bt_iuScreenshot;
    private myComponents.myGUI.buttons.MyButtonDir bt_localDir;
    private myComponents.myGUI.buttons.MyButtonDir bt_screenshot;
    private myComponents.myGUI.buttons.MyButtonInternet bt_subsOn;
    private myComponents.myGUI.buttons.MyButtonInternet bt_tvRage;
    private myComponents.myGUI.buttons.MyButtonInternet bt_tvSubs;
    private javax.swing.JComboBox cb_existingSeries;
    private javax.swing.JCheckBox checkbox_updateEpisodes;
    private javax.swing.JComboBox combo_quality;
    private javax.swing.JPanel inner_panel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel label_Title;
    private javax.swing.JLabel label_localDir;
    private javax.swing.JLabel label_message;
    private javax.swing.JLabel label_quality;
    private javax.swing.JLabel label_screenshot;
    private javax.swing.JLabel label_season;
    private javax.swing.JLabel label_subOnlineId;
    private javax.swing.JLabel label_title;
    private javax.swing.JLabel label_tvrageId;
    private javax.swing.JLabel label_tvsubsId;
    private javax.swing.JPanel main_panel;
    private myComponents.myGUI.buttons.MyButtonCancel myButtonCancel1;
    private myComponents.myGUI.buttons.MyButtonHelp myButtonHelp1;
    private myComponents.myGUI.buttons.MyButtonOk myButtonOk1;
    private myComponents.myGUI.MyImagePanel scrPanel;
    private javax.swing.JSpinner spinner_season;
    private com.googlecode.svalidators.formcomponents.STextField textField_Serial;
    private com.googlecode.svalidators.formcomponents.STextField textfield_localDir;
    public com.googlecode.svalidators.formcomponents.STextField textfield_screenshot;
    private com.googlecode.svalidators.formcomponents.STextField textfield_subsOnline;
    public com.googlecode.svalidators.formcomponents.STextField textfield_tvRageID;
    private com.googlecode.svalidators.formcomponents.STextField textfield_tvSubsId;
    // End of variables declaration//GEN-END:variables

  private void setScreenshot() {
    String sc = textfield_screenshot.getText().trim();
    MySeriesLogger.logger.log(Level.INFO, "Setting the screenshot to {0}", sc);
    MyScaledImage im = getScaledImage(sc);
    if (im != null) {
      im.fitImageIn(scrPanel.getWidth(), scrPanel.getHeight());
      scrPanel.setPreferredSize(new Dimension(im.getWidth(), im.getHeight()));

      scrPanel.setImage(im.getImage(), false, m);
      scrPanel.changeSize(im.getWidth(), im.getHeight());
      MySeriesLogger.logger.log(Level.FINE, "Screenshot set");
    } else {
      scrPanel.setImage(null, true, m);
      scrPanel.changeSize(scrPanel.getWidth(), scrPanel.getHeight());
    }
  }

  private MyScaledImage getScaledImage(String sc) {
    if (sc.equals("")) {
      MySeriesLogger.logger.log(Level.WARNING, "Screenshot is empty");
      return null;
    }
    if (!(new File(sc).isFile())) {
      MySeriesLogger.logger.log(Level.WARNING, "Screenshot is not a file");
      return null;
    }
    Image ic = new ImageIcon(sc).getImage();
    MyScaledImage im = new MyScaledImage(ic);
    if (im.getImage().getWidth(this) > 0 && im.getImage().getHeight(this) > 0) {
      return im;
    } else {
      MySeriesLogger.logger.log(Level.WARNING, "Screenshot file is empty");
      return null;
    }

  }

  private void createExistingSeriesModel() {

    ArrayList<String> list = new ArrayList<String>();
    ResultSet rs = null;
    try {
      rs = SeriesRecord.query("SELECT DISTINCT title FROM series");
      list.add("Select from existing series");
      while (rs.next()) {
        list.add(rs.getString(1));
      }
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not get series titles", ex);
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
      } catch (SQLException ex) {
        Logger.getLogger(AdminSeries.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    existingSeriesModel = new DefaultComboBoxModel(list.toArray());
    cb_existingSeries.setModel(existingSeriesModel);
  }

  private boolean seriesExist() {
    String title = textField_Serial.getText();
    int serSeason = Integer.parseInt(String.valueOf(spinner_season.getValue()));
    ResultSet rs = null;
    try {
      rs = SeriesRecord.query("SELECT * FROM series WHERE title = '" + title + "' AND season = " + serSeason);
      while (rs.next()) {
        return true;
      }
      return false;
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not check for duplicate series", ex);
      return false;
    } finally {
      try {
        rs.close();
      } catch (SQLException ex) {
        Logger.getLogger(AdminSeries.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
}
