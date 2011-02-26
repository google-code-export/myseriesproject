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
        button_getTvRageID = new javax.swing.JButton();
        label_localDir = new javax.swing.JLabel();
        label_screenshot = new javax.swing.JLabel();
        button_browseScreenshot = new javax.swing.JButton();
        label_subOnlineId = new javax.swing.JLabel();
        button_getTvSubtitlesId = new javax.swing.JButton();
        button_getSubOnlineId = new javax.swing.JButton();
        label_message = new javax.swing.JLabel();
        textfield_localDir = new com.googlecode.svalidators.formcomponents.STextField(new FileValidator("",FileValidator.Type.DIR,true));
        button_browseLocalDir = new javax.swing.JButton();
        textfield_screenshot = new com.googlecode.svalidators.formcomponents.STextField(new FileValidator("", FileValidator.Type.FILE, true));
        textfield_subsOnline = new com.googlecode.svalidators.formcomponents.STextField(new NoSpaceValidator("",true));
        textfield_tvSubsId = new com.googlecode.svalidators.formcomponents.STextField(new NoSpaceValidator("",true));
        jLabel1 = new javax.swing.JLabel();
        checkbox_updateEpisodes = new javax.swing.JCheckBox();
        button_getScreenshot = new javax.swing.JButton();
        scrPanel = new myComponents.myGUI.MyImagePanel();
        myButtonCancel1 = new myComponents.myGUI.buttons.MyButtonCancel();
        myButtonHelp1 = new myComponents.myGUI.buttons.MyButtonHelp();
        label_Title = new javax.swing.JLabel();
        myButtonOk1 = new myComponents.myGUI.buttons.MyButtonOk();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(102, 102, 102));
        setResizable(false);

        main_panel.setBackground(Skin.getColor_5());
        main_panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        inner_panel.setBackground(new java.awt.Color(255, 255, 255));
        inner_panel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

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

        button_getTvRageID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/internet_update_small.png"))); // NOI18N
        button_getTvRageID.setToolTipText("Get ID for TvRage.com");
        button_getTvRageID.setIconTextGap(2);
        button_getTvRageID.setMaximumSize(new java.awt.Dimension(31, 25));
        button_getTvRageID.setMinimumSize(new java.awt.Dimension(31, 25));
        button_getTvRageID.setOpaque(false);
        button_getTvRageID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_getTvRageIDMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_getTvRageIDMouseExited(evt);
            }
        });
        button_getTvRageID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_getTvRageIDActionPerformed(evt);
            }
        });

        label_localDir.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label_localDir.setText("Local Dir :");

        label_screenshot.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label_screenshot.setText("Screenshot :");

        button_browseScreenshot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/localDir_small.png"))); // NOI18N
        button_browseScreenshot.setMaximumSize(new java.awt.Dimension(31, 25));
        button_browseScreenshot.setMinimumSize(new java.awt.Dimension(31, 25));
        button_browseScreenshot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_browseScreenshotActionPerformed(evt);
            }
        });

        label_subOnlineId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label_subOnlineId.setText("SubtitleOnline ID :");

        button_getTvSubtitlesId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/internet_update_small.png"))); // NOI18N
        button_getTvSubtitlesId.setToolTipText("Get ID for TvSubtitles.net");
        button_getTvSubtitlesId.setIconTextGap(2);
        button_getTvSubtitlesId.setMaximumSize(new java.awt.Dimension(31, 25));
        button_getTvSubtitlesId.setMinimumSize(new java.awt.Dimension(31, 25));
        button_getTvSubtitlesId.setOpaque(false);
        button_getTvSubtitlesId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_getTvSubtitlesIdMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_getTvSubtitlesIdMouseExited(evt);
            }
        });
        button_getTvSubtitlesId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_getTvSubtitlesIdActionPerformed(evt);
            }
        });

        button_getSubOnlineId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/internet_update_small.png"))); // NOI18N
        button_getSubOnlineId.setToolTipText("Get ID for SubtitleOnline.com");
        button_getSubOnlineId.setIconTextGap(2);
        button_getSubOnlineId.setMaximumSize(new java.awt.Dimension(31, 25));
        button_getSubOnlineId.setMinimumSize(new java.awt.Dimension(31, 25));
        button_getSubOnlineId.setOpaque(false);
        button_getSubOnlineId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_getSubOnlineIdMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_getSubOnlineIdMouseExited(evt);
            }
        });
        button_getSubOnlineId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_getSubOnlineIdActionPerformed(evt);
            }
        });

        textfield_localDir.setName("Local Directory"); // NOI18N

        button_browseLocalDir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/localDir_small.png"))); // NOI18N
        button_browseLocalDir.setMaximumSize(new java.awt.Dimension(31, 25));
        button_browseLocalDir.setMinimumSize(new java.awt.Dimension(31, 25));
        button_browseLocalDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_browseLocalDirActionPerformed(evt);
            }
        });

        textfield_screenshot.setName("Screenshot"); // NOI18N

        textfield_subsOnline.setName("SubtitleOnline ID"); // NOI18N

        textfield_tvSubsId.setName("TvSubtitles ID"); // NOI18N

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Update episodes list :");

        checkbox_updateEpisodes.setSelected(seriesRecord.getSeries_ID() ==0);
        checkbox_updateEpisodes.setEnabled(seriesRecord.getSeries_ID() == 0);
        checkbox_updateEpisodes.setMargin(new java.awt.Insets(0, 0, 0, 0));
        checkbox_updateEpisodes.setOpaque(false);

        button_getScreenshot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/internet_update_small.png"))); // NOI18N
        button_getScreenshot.setToolTipText("Download a screenshot from tvrage");
        button_getScreenshot.setIconTextGap(2);
        button_getScreenshot.setMaximumSize(new java.awt.Dimension(31, 25));
        button_getScreenshot.setMinimumSize(new java.awt.Dimension(31, 25));
        button_getScreenshot.setOpaque(false);
        button_getScreenshot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_getScreenshotMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_getScreenshotMouseExited(evt);
            }
        });
        button_getScreenshot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_getScreenshotActionPerformed(evt);
            }
        });

        scrPanel.setMaximumSize(new java.awt.Dimension(180, 110));

        javax.swing.GroupLayout scrPanelLayout = new javax.swing.GroupLayout(scrPanel);
        scrPanel.setLayout(scrPanelLayout);
        scrPanelLayout.setHorizontalGroup(
            scrPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 178, Short.MAX_VALUE)
        );
        scrPanelLayout.setVerticalGroup(
            scrPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 108, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout inner_panelLayout = new javax.swing.GroupLayout(inner_panel);
        inner_panel.setLayout(inner_panelLayout);
        inner_panelLayout.setHorizontalGroup(
            inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inner_panelLayout.createSequentialGroup()
                .addGap(46, 46, 46)
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
                            .addComponent(textField_Serial, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                            .addGroup(inner_panelLayout.createSequentialGroup()
                                .addComponent(spinner_season, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(165, 165, 165))
                            .addComponent(textfield_localDir, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                            .addComponent(textfield_screenshot, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
                        .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(inner_panelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button_browseLocalDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(inner_panelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(button_browseScreenshot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button_getScreenshot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(85, 85, 85))
                    .addGroup(inner_panelLayout.createSequentialGroup()
                        .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(inner_panelLayout.createSequentialGroup()
                                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(textfield_tvRageID, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(textfield_subsOnline, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(textfield_tvSubsId, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(button_getTvSubtitlesId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button_getSubOnlineId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button_getTvRageID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(inner_panelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(checkbox_updateEpisodes)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        inner_panelLayout.setVerticalGroup(
            inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inner_panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inner_panelLayout.createSequentialGroup()
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
                            .addComponent(button_browseLocalDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(textfield_screenshot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(button_browseScreenshot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(button_getScreenshot, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(inner_panelLayout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(label_tvsubsId)
                                    .addComponent(textfield_tvSubsId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button_getTvSubtitlesId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(label_subOnlineId)
                                    .addComponent(textfield_subsOnline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button_getSubOnlineId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(label_tvrageId)
                                    .addComponent(textfield_tvRageID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button_getTvRageID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(checkbox_updateEpisodes)
                                    .addComponent(jLabel1)))
                            .addGroup(inner_panelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(inner_panelLayout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(label_screenshot)))
                .addGap(55, 55, 55))
        );

        myButtonCancel1.setText("");
        myButtonCancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myButtonCancel1ActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout main_panelLayout = new javax.swing.GroupLayout(main_panel);
        main_panel.setLayout(main_panelLayout);
        main_panelLayout.setHorizontalGroup(
            main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label_Title, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inner_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, main_panelLayout.createSequentialGroup()
                .addContainerGap(516, Short.MAX_VALUE)
                .addComponent(myButtonHelp1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myButtonCancel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, main_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(myButtonOk1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        main_panelLayout.setVerticalGroup(
            main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_panelLayout.createSequentialGroup()
                .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(main_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(label_Title))
                    .addComponent(myButtonCancel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(myButtonHelp1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inner_panel, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(myButtonOk1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void button_getTvRageIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_getTvRageIDActionPerformed
        setModalityType(ModalityType.MODELESS);
        dispose();
        TrGetId g = new TrGetId(this, textField_Serial.getText().trim());
        //setVisible(true);
        //textfield_tvRageID.setText(String.valueOf(g.tvRageID));
    }//GEN-LAST:event_button_getTvRageIDActionPerformed

    private void button_getTvRageIDMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_getTvRageIDMouseEntered
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_button_getTvRageIDMouseEntered

    private void button_getTvRageIDMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_getTvRageIDMouseExited
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_button_getTvRageIDMouseExited

    private void button_browseScreenshotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_browseScreenshotActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("./"));
        fc.setDialogTitle("Choose the Series screenshot");
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new ScreenshotFilter());
        fc.showOpenDialog(this);
        File fScreenshot = fc.getSelectedFile();
        textfield_screenshot.setText(fScreenshot.getAbsolutePath());
    }//GEN-LAST:event_button_browseScreenshotActionPerformed

    private void button_getTvSubtitlesIdMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_getTvSubtitlesIdMouseEntered
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_button_getTvSubtitlesIdMouseEntered

    private void button_getTvSubtitlesIdMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_getTvSubtitlesIdMouseExited
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_button_getTvSubtitlesIdMouseExited

    private void button_getTvSubtitlesIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_getTvSubtitlesIdActionPerformed
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
    }//GEN-LAST:event_button_getTvSubtitlesIdActionPerformed

    private void button_getSubOnlineIdMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_getSubOnlineIdMouseEntered
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_button_getSubOnlineIdMouseEntered

    private void button_getSubOnlineIdMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_getSubOnlineIdMouseExited
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_button_getSubOnlineIdMouseExited

    private void button_getSubOnlineIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_getSubOnlineIdActionPerformed
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
    }//GEN-LAST:event_button_getSubOnlineIdActionPerformed

    private void button_browseLocalDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_browseLocalDirActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(textfield_localDir.getText()));
        fc.setDialogTitle("Choose the Series directory");
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.showOpenDialog(this);
        File localDir = fc.getSelectedFile();
        textfield_localDir.setText(localDir.getAbsolutePath());
    }//GEN-LAST:event_button_browseLocalDirActionPerformed

    private void button_getScreenshotMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_getScreenshotMouseEntered
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_button_getScreenshotMouseEntered

    private void button_getScreenshotMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_getScreenshotMouseExited
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_button_getScreenshotMouseExited

    private void button_getScreenshotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_getScreenshotActionPerformed
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
                }
            } else {
            }
            setVisible(true);
        }
    }//GEN-LAST:event_button_getScreenshotActionPerformed

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
            group.errorMessage(true);
            return;
        }
        addSeries();
    }//GEN-LAST:event_myButtonOk1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_browseLocalDir;
    private javax.swing.JButton button_browseScreenshot;
    private javax.swing.JButton button_getScreenshot;
    private javax.swing.JButton button_getSubOnlineId;
    private javax.swing.JButton button_getTvRageID;
    private javax.swing.JButton button_getTvSubtitlesId;
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
