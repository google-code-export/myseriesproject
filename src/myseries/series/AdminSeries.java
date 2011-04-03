/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AddSeries.java
 *
 * Created on 30 Οκτ 2008, 8:04:57 μμ
 */
package myseries.series;

import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import myComponents.myGUI.CopyScreenshot;
import java.io.IOException;
import myComponents.MyUsefulFunctions;
import database.SeriesRecord;
import java.awt.Cursor;
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
import myseries.MySeries;
import com.googlecode.svalidators.formcomponents.STextField;
import com.googlecode.svalidators.formcomponents.ValidationGroup;
import com.googlecode.svalidators.validators.FileValidator;
import com.googlecode.svalidators.validators.NoSpaceValidator;
import com.googlecode.svalidators.validators.PositiveNumberValidator;
import com.googlecode.svalidators.validators.RequiredValidator;
import help.HelpWindow;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyDescriptor;
import javax.swing.event.DocumentListener;
import myComponents.myEvents.MyEvent;
import myComponents.myEvents.MyEventHandler;
import myComponents.myEvents.MyEventsClass;
import myComponents.myGUI.MyImagePanel;
import myComponents.myGUI.MyScaledImage;
import myseries.actions.SeriesActions;
import tools.Skin;
import tools.download.screenshot.DownloadScreenshot;
import tools.download.subtitles.sonline.GetSubtitleOnlineCode;
import tools.download.subtitles.tvsubtitles.GetTvSubtitlesCode;
import tools.internetUpdate.InternetUpdate;
import tools.internetUpdate.tvrage.TrGetId;
import tools.options.Options;

/**
 * Administrate series
 * @author lordovol
 */
public class AdminSeries extends MyDraggable {

    private MyEventsClass evClass = new MyEventsClass();
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
        this.seriesRecord = seriesRecord;
        initComponents();
        setLocationRelativeTo(m);
        spinner_season.setModel(new SpinnerNumberModel(Series.DEFAULT_SEASON, Series.MINIMUM_SEASON, Series.MAXIMUM_SEASON, Series.SEASON_STEP));
        if (seriesRecord.getSeries_ID() > 0) {
            spinner_season.setValue(seriesRecord.getSeason());
            textField_Serial.setText(seriesRecord.getTitle());
            label_Title.setText("Edit Series " + seriesRecord.getTitle()
                    + " S" + MyUsefulFunctions.padLeft(seriesRecord.getSeason(), 2, "0"));
            textfield_tvSubsId.setText(seriesRecord.getTvSubtitlesCode());
            textfield_tvRageID.setText(String.valueOf(seriesRecord.getTvrage_ID()));
            textfield_localDir.setText(seriesRecord.getLocalDir());

            textfield_screenshot.setText(seriesRecord.getScreenshot().equals("") ? "" : Options._USER_DIR_ + MyImagePanel.SCREENSHOTS_PATH + seriesRecord.getScreenshot());
            setScreenshot();
            textfield_tvSubsId.setText(seriesRecord.getTvSubtitlesCode());
            textfield_subsOnline.setText(seriesRecord.getSOnlineCode());
            // button_Add.setText("Edit");
        } else {
            label_Title.setText("Add New Series");
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
        scrPanel = new myComponents.myGUI.MyImagePanel();
        bt_localDir = new myComponents.myGUI.buttons.MyButtonDir();
        bt_screenshot = new myComponents.myGUI.buttons.MyButtonDir();
        bt_iuScreenshot = new myComponents.myGUI.buttons.MyButtonInternet();
        bt_tvSubs = new myComponents.myGUI.buttons.MyButtonInternet();
        bt_subsOn = new myComponents.myGUI.buttons.MyButtonInternet();
        bt_tvRage = new myComponents.myGUI.buttons.MyButtonInternet();
        myButtonHelp1 = new myComponents.myGUI.buttons.MyButtonHelp();
        label_Title = new javax.swing.JLabel();
        myButtonOk1 = new myComponents.myGUI.buttons.MyButtonOk();
        myButtonCancel1 = new myComponents.myGUI.buttons.MyButtonCancel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(102, 102, 102));
        setResizable(false);

        main_panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        main_panel.setOpaque(false);

        inner_panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        label_title.setFont(label_title.getFont());
        label_title.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label_title.setText("Title:");

        textField_Serial.setName("Title"); // NOI18N

        label_season.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label_season.setText("Season :");

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

        bt_localDir.setText("");
        bt_localDir.setToolTipText("Browse for the local Directory");
        bt_localDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_localDirActionPerformed(evt);
            }
        });

        bt_screenshot.setText("");
        bt_screenshot.setToolTipText("Browse for a screenshot");
        bt_screenshot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_screenshotActionPerformed(evt);
            }
        });

        bt_iuScreenshot.setText("");
        bt_iuScreenshot.setToolTipText("Download screenshot");
        bt_iuScreenshot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_iuScreenshotActionPerformed(evt);
            }
        });

        bt_tvSubs.setText("");
        bt_tvSubs.setToolTipText("Get TvSubtitles ID");
        bt_tvSubs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_tvSubsActionPerformed(evt);
            }
        });

        bt_subsOn.setText("");
        bt_subsOn.setToolTipText("Get subtitleOnline ID");
        bt_subsOn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_subsOnActionPerformed(evt);
            }
        });

        bt_tvRage.setText("");
        bt_tvRage.setToolTipText("Get TvRage ID");
        bt_tvRage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_tvRageActionPerformed(evt);
            }
        });

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
                            .addGroup(inner_panelLayout.createSequentialGroup()
                                .addComponent(spinner_season, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(165, 165, 165))
                            .addComponent(textfield_localDir, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                            .addComponent(textfield_screenshot, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE))
                        .addGap(6, 6, 6)
                        .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(inner_panelLayout.createSequentialGroup()
                                .addComponent(bt_iuScreenshot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bt_screenshot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(bt_localDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(148, 148, 148))
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
                    .addComponent(textField_Serial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(label_season)
                    .addComponent(spinner_season, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        myButtonHelp1.setText("");
        myButtonHelp1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myButtonHelp1ActionPerformed(evt);
            }
        });

        label_Title.setFont(label_Title.getFont().deriveFont(label_Title.getFont().getStyle() | java.awt.Font.BOLD, label_Title.getFont().getSize()+2));
        label_Title.setForeground(Skin.getColor_1());
        label_Title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_Title.setText("Add A New Series");

        myButtonOk1.setText("");
        myButtonOk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myButtonOk1ActionPerformed(evt);
            }
        });

        myButtonCancel1.setText("");
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

        try {
            int tvRageID = 0;
            try {
                tvRageID = Integer.parseInt(textfield_tvRageID.getText());
            } catch (NumberFormatException ex) {
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
            File sc = null;
            if (!screenshot.equals("")) {
                sc = new File(screenshot);
                if (sc.isFile() && !screenshot.startsWith("./images")) {
                    CopyScreenshot c = new CopyScreenshot(screenshot);
                    Thread t = new Thread(c);
                    t.start();
                    seriesRecord.setScreenshot(sc.getName());
                } else {
                    seriesRecord.setScreenshot(sc.getName());
                }
                Image im = new ImageIcon(sc.getAbsolutePath()).getImage();
                MySeries.imagePanel.setImage(im, false);

            } else {
                seriesRecord.setScreenshot("");
                MySeries.imagePanel.setImage(null, true);
            }
            try {
                int series_ID = seriesRecord.save();
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
                MySeries.logger.log(Level.SEVERE, "SQL error occured", ex);
            }
        } catch (NumberFormatException ex) {
            MySeries.logger.log(Level.WARNING, "Season must be a number", ex);
            MyMessages.error("Season not a number!!!", "Season must be a number");
        }
    }

    private void myButtonHelp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myButtonHelp1ActionPerformed
        new HelpWindow(HelpWindow.ADMIN_SERIES);
    }//GEN-LAST:event_myButtonHelp1ActionPerformed

    private void myButtonCancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myButtonCancel1ActionPerformed
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
        if (!group.validate()) {
            MyMessages.error("Series Form", group.getErrorMessage());
            return;
        }
        addSeries();
    }//GEN-LAST:event_myButtonOk1ActionPerformed

    private void bt_localDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_localDirActionPerformed
        JFileChooser fc = new JFileChooser();
        String md = textfield_localDir.getText().equals("") ? Options.toString(Options.MAIN_DIRECTORY) : textfield_localDir.getText();
        fc.setCurrentDirectory(new File(md));
        fc.setDialogTitle("Choose the Series directory");
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.showOpenDialog(this);
        if (fc.getSelectedFile() != null) {
            File localDir = fc.getSelectedFile();
            textfield_localDir.setText(localDir.getAbsolutePath());
        }
    }//GEN-LAST:event_bt_localDirActionPerformed

    private void bt_screenshotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_screenshotActionPerformed
        JFileChooser fc = new JFileChooser();
        File d;
        if (textfield_screenshot.getText().trim().equals("")) {
            d = new File(Options.toString(Options.MAIN_DIRECTORY));
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
            textfield_screenshot.setText(fScreenshot.getAbsolutePath());
        }
    }//GEN-LAST:event_bt_screenshotActionPerformed

    private void bt_iuScreenshotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_iuScreenshotActionPerformed
        if (textfield_tvRageID.getText().equals("") || textfield_tvRageID.getText().equals("0")) {
            setModalityType(ModalityType.MODELESS);
            dispose();
            TrGetId g = new TrGetId(this, textField_Serial.getText().trim(), true);
        } else if (MyUsefulFunctions.isNumeric(textfield_tvRageID.getText())) {
            setVisible(false);
            int tvRageId = Integer.parseInt(textfield_tvRageID.getText());
            if (tvRageId > 0) {
                DownloadScreenshot g = new DownloadScreenshot(Integer.parseInt(textfield_tvRageID.getText()));
                if (g.isSuccess()) {
                    textfield_screenshot.setText(g.getFilename());
                    MyMessages.message("Downloading screenshot", "The screenshot was saved in the images folder");
                } else {
                    MyMessages.error("Downloading screenshot", "No screenshot was found");
    }//GEN-LAST:event_bt_iuScreenshotActionPerformed

            }
            setVisible(true);
        }
    }
          private void bt_tvSubsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_tvSubsActionPerformed
              if (seriesRecord.getSeries_ID() == 0) {
                  if (textField_Serial.getText().trim().equals("")) {
                      MyMessages.error("Empty title", "The series title shouldn't be empty");
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
              } else {
                  textfield_tvSubsId.setText(c.tSubCode);
                  label_message.setText("");
                  label_message.setText("");
              }
              setVisible(true);
          }//GEN-LAST:event_bt_tvSubsActionPerformed

          private void bt_subsOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_subsOnActionPerformed
              setModalityType(ModalityType.MODELESS);
              setVisible(false);
              GetSubtitleOnlineCode c = new GetSubtitleOnlineCode(seriesRecord);
              String id = c.subtitleOnlineCode;
              if (id == null) {
              } else {
                  textfield_subsOnline.setText(c.subtitleOnlineCode);
                  label_message.setText("");
                  label_message.setText("");
              }
              setVisible(true);
          }//GEN-LAST:event_bt_subsOnActionPerformed

          private void bt_tvRageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_tvRageActionPerformed
              setModalityType(ModalityType.MODELESS);
              dispose();
              TrGetId g = new TrGetId(this, textField_Serial.getText().trim());
              //setVisible(true);
              //textfield_tvRageID.setText(String.valueOf(g.tvRageID));
          }//GEN-LAST:event_bt_tvRageActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private myComponents.myGUI.buttons.MyButtonInternet bt_iuScreenshot;
    private myComponents.myGUI.buttons.MyButtonDir bt_localDir;
    private myComponents.myGUI.buttons.MyButtonDir bt_screenshot;
    private myComponents.myGUI.buttons.MyButtonInternet bt_subsOn;
    private myComponents.myGUI.buttons.MyButtonInternet bt_tvRage;
    private myComponents.myGUI.buttons.MyButtonInternet bt_tvSubs;
    private javax.swing.JCheckBox checkbox_updateEpisodes;
    private javax.swing.JPanel inner_panel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel label_Title;
    private javax.swing.JLabel label_localDir;
    private javax.swing.JLabel label_message;
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
        if (!sc.equals("")) {
            File f = new File(sc);
            if (f.isFile()) {
                MyScaledImage im = new MyScaledImage(new ImageIcon(sc).getImage());
                im.fitImageIn(scrPanel.getWidth(), scrPanel.getHeight());
                scrPanel.setPreferredSize(new Dimension(im.getWidth(), im.getHeight()));
                scrPanel.repaint();
                scrPanel.revalidate();
                scrPanel.changeSize(im.getImage(), im.getWidth(), im.getHeight());
            } else {
                scrPanel.changeSize(null, scrPanel.getWidth(), scrPanel.getHeight());
            }
        } else {
            scrPanel.changeSize(null, scrPanel.getWidth(), scrPanel.getHeight());
        }

    }
}
