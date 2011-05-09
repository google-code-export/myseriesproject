/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * OptionsPanel.java
 *
 * Created on 23 Νοε 2008, 8:34:12 μμ
 */
package tools.options;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import tools.MySeriesLogger;
import java.awt.GraphicsEnvironment;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myComponents.myGUI.MyDraggable;
import myseries.MySeries;
import com.googlecode.svalidators.formcomponents.ValidationGroup;
import com.googlecode.svalidators.validators.CompareValidator;
import com.googlecode.svalidators.validators.FileValidator;
import com.googlecode.svalidators.validators.ListValidator;
import com.googlecode.svalidators.validators.NoSpaceValidator;
import com.googlecode.svalidators.validators.NullValidator;
import com.googlecode.svalidators.validators.PositiveNumberValidator;
import com.googlecode.svalidators.validators.RegularExpressionValidator;
import com.googlecode.svalidators.validators.SValidator;
import help.HelpWindow;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import myComponents.myGUI.MyFont;
import myComponents.myTableCellRenderers.MySubtitleListRenderer;
import myseries.actions.ApplicationActions;
import tools.LookAndFeels;
import tools.Skin;
import tools.download.subtitles.SubtitleConstants;
import tools.languages.Language;

/**
 *
 * @author lordovol
 */
public class OptionsPanel extends MyDraggable {

  public static final int GENERAL_OPTIONS_TAB = 0;
  public static final int INTERNET_OPTIONS_TAB = 1;
  public static final int RENAME_OPTIONS_TAB = 2;
  public static final int APPEARANCE_TAB = 3;
  public static final long serialVersionUID = 5676235253653L;
  private MySeries m;
  private DefaultComboBoxModel model_laf = new DefaultComboBoxModel();
  private ComboBoxModel model_fonts;
  private boolean colorsChanged;
  private String oldFontSize;
  private String oldFontFace;
  private Color oldColor;
  private boolean oldUseSkin;
  private String oldLaf;
  private ComboBoxModel primarySubtitlesModel = new DefaultComboBoxModel(SubtitleConstants.SUBTITLE_LANG.toArray());
  private ComboBoxModel secondarySubtitlesModel = new DefaultComboBoxModel(SubtitleConstants.SUBTITLE_LANG.toArray());
  private String sepRegex = "^[^/\\\\?%*:|\\\"<>\\.]*$";
  private String[] fonts;

  /** Creates new form OptionsPanel
   * @param m MySeries main form
   */
  public OptionsPanel(MySeries m) {
    this.m = m;
    MySeriesLogger.logger.log(Level.INFO, "Initializing components");
    initComponents();
    MySeriesLogger.logger.log(Level.FINE, "Components initialized");
    combobox_laf.setEnabled(!checkbox_dontUseSkin.isSelected());
    tf_episodeSep.addValidator(new RegularExpressionValidator("", sepRegex, false, false));
    tf_seasonSep.addValidator(new RegularExpressionValidator("", sepRegex, false, true));
    tf_titleSep.addValidator(new RegularExpressionValidator("", sepRegex, false, false));
    tf_videoApp.addValidator(new FileValidator("", FileValidator.Type.FILE, true));
    tf_episodeSep.setTrimValue(false);
    tf_seasonSep.setTrimValue(false);
    tf_titleSep.setTrimValue(false);
    combo_secondaryLang.addValidator(
        new CompareValidator(combo_secondaryLang.getSelectedItem() != null ? combo_secondaryLang.getSelectedItem().toString() : "",
        combo_primaryLang.getSelectedItem() != null ? combo_primaryLang.getSelectedItem().toString() : "",
        CompareValidator.Type.NOT_EQUAL, true));
    combo_primaryLang.setRenderer(new MySubtitleListRenderer());
    combo_secondaryLang.setRenderer(new MySubtitleListRenderer());
    checkbox_useProxyActionPerformed(null);
    setLocationRelativeTo(m);
    oldFontFace = Options.toString(Options.FONT_FACE);
    oldFontSize = Options.toString(Options.FONT_SIZE);
    oldColor = Options.toColor(Options.SKIN_COLOR);
    oldUseSkin = Options.toBoolean(Options.USE_SKIN);
    oldLaf = Options.toString(Options.LOOK_AND_FEEL);
    createModelFonts();
    JTextField t = (JTextField) combobox_fonts.getEditor().getEditorComponent();
    t.setText(Options.toString(Options.FONT_FACE));
    addSliderLabels();
    setVisible(true);

  }

  private void createLafModel() {
    combobox_laf.setModel(LookAndFeels.getComboBoxModel());
  }

  private void createModelFonts() {
    MySeriesLogger.logger.log(Level.INFO, "Creating fonts model");
    //System.out.println(System.currentTimeMillis());
    fonts = getCachedFonts();
    model_fonts = new DefaultComboBoxModel(fonts);
    combobox_fonts.setModel(model_fonts);
    model_fonts.setSelectedItem(Options.toString(Options.FONT_FACE));
    combobox_fonts.addValidator(new ListValidator("", fonts, false));
    MySeriesLogger.logger.log(Level.INFO, "Added {0} fonts", model_fonts.getSize());
    //System.out.println(System.currentTimeMillis());
  }

  private String[] getCachedFonts() {
    MySeriesLogger.logger.log(Level.INFO, "Checking for cached fonts model");
    File c = new File(Options._USER_DIR_ + "/f.obj");
    if (c.exists() && (System.currentTimeMillis() - c.lastModified()) / 3600000 / 24 < 15) {
      MySeriesLogger.logger.log(Level.INFO, "Cached model exists an it's new.Reading file");
      try {
        FileInputStream fin = new FileInputStream(c);
        ObjectInputStream ois = new ObjectInputStream(fin);
        String[] f = (String[]) ois.readObject();
        ois.close();
        return f;
      } catch (Exception ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "Could not read the fonts model file", ex);
        return null;
      }
    } else {
      MySeriesLogger.logger.log(Level.INFO, "Cached file does not exist or it's old.Creating it");
      GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
      String[] f = env.getAvailableFontFamilyNames();
      FileOutputStream fout;
      try {
        fout = new FileOutputStream(Options._USER_DIR_ + "/f.obj");
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(f);
        oos.close();
      } catch (IOException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "Could not create fonts model file", ex);
        return null;
      }
      return f;
    }

  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

    panel_DateFormatHelp = new javax.swing.JPanel();
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenu1 = new javax.swing.JMenu();
    jMenu2 = new javax.swing.JMenu();
    jPanel1 = new javax.swing.JPanel();
    panel_options = new javax.swing.JPanel();
    tabbedPane_options = new javax.swing.JTabbedPane();
    panel_general = new javax.swing.JPanel();
    jLabel3 = new javax.swing.JLabel();
    combobox_debugMode = new javax.swing.JComboBox();
    jLabel4 = new javax.swing.JLabel();
    combobox_dateFormat = new javax.swing.JComboBox();
    jLabel11 = new javax.swing.JLabel();
    tf_videoApp = new com.googlecode.svalidators.formcomponents.STextField();
    jLabel14 = new javax.swing.JLabel();
    bt_videoViewer = new myComponents.myGUI.buttons.MyButtonDir();
    jLabel7 = new javax.swing.JLabel();
    tf_mainDir = new com.googlecode.svalidators.formcomponents.STextField(new FileValidator("",FileValidator.Type.DIR ,true));
    bt_mainDirectory = new myComponents.myGUI.buttons.MyButtonDir();
    jLabel22 = new javax.swing.JLabel();
    bt_dfhelp = new myComponents.myGUI.buttons.MyButtonHelp();
    panel_appearance = new javax.swing.JPanel();
    jLabel5 = new javax.swing.JLabel();
    combobox_laf = new javax.swing.JComboBox();
    checkbox_dontUseSkin = new javax.swing.JCheckBox();
    button_BGColor = new javax.swing.JButton();
    combobox_fonts = new com.googlecode.svalidators.formcomponents.SComboBox();
    jLabel10 = new javax.swing.JLabel();
    spinner_fontSize = new javax.swing.JSpinner();
    jLabel16 = new javax.swing.JLabel();
    label_preview = new javax.swing.JLabel();
    cb_minimizeTray = new javax.swing.JCheckBox();
    cb_randomizeLaf = new javax.swing.JCheckBox();
    panel_internet = new javax.swing.JPanel();
    checkbox_useProxy = new javax.swing.JCheckBox();
    jLabel8 = new javax.swing.JLabel();
    jLabel9 = new javax.swing.JLabel();
    textfield_proxy = new com.googlecode.svalidators.formcomponents.STextField(new NoSpaceValidator("",false));
    jCheckBox1 = new javax.swing.JCheckBox();
    textfield_port = new com.googlecode.svalidators.formcomponents.STextField(new PositiveNumberValidator("",false,false));
    jLabel13 = new javax.swing.JLabel();
    combo_primaryLang = new com.googlecode.svalidators.formcomponents.SComboBox();
    jLabel15 = new javax.swing.JLabel();
    combo_secondaryLang = new com.googlecode.svalidators.formcomponents.SComboBox();
    jLabel2 = new javax.swing.JLabel();
    spinner_columns = new javax.swing.JSpinner();
    cb_updateFeeds = new javax.swing.JCheckBox();
    panel_renaming = new javax.swing.JPanel();
    jLabel18 = new javax.swing.JLabel();
    jLabel19 = new javax.swing.JLabel();
    jLabel20 = new javax.swing.JLabel();
    tf_seasonSep = new com.googlecode.svalidators.formcomponents.STextField();
    tf_episodeSep = new com.googlecode.svalidators.formcomponents.STextField();
    tf_titleSep = new com.googlecode.svalidators.formcomponents.STextField();
    cb_noRenameConf = new javax.swing.JCheckBox();
    cd_autoRename = new javax.swing.JCheckBox();
    jLabel6 = new javax.swing.JLabel();
    jLabel12 = new javax.swing.JLabel();
    panel_performance = new javax.swing.JPanel();
    jLabel17 = new javax.swing.JLabel();
    cb_autoUnzip = new javax.swing.JCheckBox();
    cb_autoUpdate = new javax.swing.JCheckBox();
    jLabel1 = new javax.swing.JLabel();
    sl_memory = new javax.swing.JSlider();
    jLabel21 = new javax.swing.JLabel();
    lb_title = new javax.swing.JLabel();
    bt_cancel = new myComponents.myGUI.buttons.MyButtonCancel();
    bt_help = new myComponents.myGUI.buttons.MyButtonHelp();
    bt_ok = new myComponents.myGUI.buttons.MyButtonOk();

    javax.swing.GroupLayout panel_DateFormatHelpLayout = new javax.swing.GroupLayout(panel_DateFormatHelp);
    panel_DateFormatHelp.setLayout(panel_DateFormatHelpLayout);
    panel_DateFormatHelpLayout.setHorizontalGroup(
      panel_DateFormatHelpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 100, Short.MAX_VALUE)
    );
    panel_DateFormatHelpLayout.setVerticalGroup(
      panel_DateFormatHelpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 100, Short.MAX_VALUE)
    );

    jMenu1.setText("File");
    jMenuBar1.add(jMenu1);

    jMenu2.setText("Edit");
    jMenuBar1.add(jMenu2);

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    setUndecorated(true);

    jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jPanel1.setMaximumSize(new java.awt.Dimension(800, 500));
    jPanel1.setMinimumSize(new java.awt.Dimension(627, 430));

    panel_options.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    tabbedPane_options.setName("Tab"); // NOI18N

    panel_general.setToolTipText("General options");
    panel_general.setName("General"); // NOI18N
    panel_general.setOpaque(false);

    jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel3.setText("Logging level :");
    jLabel3.setToolTipText("<html>\nThe level of Debuging info that's written in the log file<br />\nFATAL : Only fatal errors are written<br />\nWARNING : Warnings and fatal errors are written<br />\nINFO : Everything is written<br />\nNO LOGGING: Nothing is written<br />");
    jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    jLabel3.setName("noname"); // NOI18N

    combobox_debugMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "OFF", "SEVERE", "WARNING", "ALL" }));
    combobox_debugMode.setSelectedItem(Options.toString(Options.DEBUG_MODE));
    combobox_debugMode.setToolTipText("<html>\nThe level of Debuging info that's written in the log file<br />\nSEVERE : Only fatal errors are written<br />\nWARNING : Warnings and fatal errors are written<br />\nALL : Everything is written<br />\nOFF: Nothing is written<br />");
    combobox_debugMode.setName(Options.DEBUG_MODE);
    combobox_debugMode.setOpaque(false);

    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel4.setText("Date Format :");
    jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    jLabel4.setName("noname"); // NOI18N

    combobox_dateFormat.setEditable(true);
    combobox_dateFormat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dd/MM/yyyy", "dd-MM-yyyy", "dd/MM/yy", "dd-MM-yy", "d/M/yyyy", "d/M/yy" }));
    combobox_dateFormat.setSelectedItem(Options.toString(Options.DATE_FORMAT));
    combobox_dateFormat.setName(Options.DATE_FORMAT);

    jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel11.setText("Video Viewer :");
    jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

    tf_videoApp.setText(Options.toString(Options.VIDEO_APP));
    tf_videoApp.setName(Options.VIDEO_APP);

    jLabel14.setFont(jLabel14.getFont().deriveFont((jLabel14.getFont().getStyle() | java.awt.Font.ITALIC), jLabel14.getFont().getSize()-1));
    jLabel14.setText("(Leave it blank to use your OS default video viewer)");

    bt_videoViewer.setToolTipText("Browse for video viewer");
    bt_videoViewer.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_videoViewerActionPerformed(evt);
      }
    });

    jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel7.setText("Main Series Directory:");
    jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

    tf_mainDir.setText(Options.toString(Options.MAIN_DIRECTORY));
    tf_mainDir.setToolTipText("<html>A directory where subtitle (zipped or not) files should be saved. Then the application will move them to the series directory if possible");
    tf_mainDir.setName(Options.MAIN_DIRECTORY);

    bt_mainDirectory.setToolTipText("Browse for main directory");
    bt_mainDirectory.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_mainDirectoryActionPerformed(evt);
      }
    });

    jLabel22.setFont(jLabel22.getFont().deriveFont((jLabel22.getFont().getStyle() | java.awt.Font.ITALIC), jLabel22.getFont().getSize()-1));
    jLabel22.setText("Setting logging level to ALL will slow down the application");

    bt_dfhelp.setToolTipText("Date format help");
    bt_dfhelp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_dfhelpActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout panel_generalLayout = new javax.swing.GroupLayout(panel_general);
    panel_general.setLayout(panel_generalLayout);
    panel_generalLayout.setHorizontalGroup(
      panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_generalLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jLabel7)
          .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(combobox_debugMode, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(panel_generalLayout.createSequentialGroup()
            .addComponent(combobox_dateFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(bt_dfhelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(panel_generalLayout.createSequentialGroup()
            .addComponent(tf_videoApp, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(bt_videoViewer, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(panel_generalLayout.createSequentialGroup()
            .addComponent(tf_mainDir, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(bt_mainDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(78, Short.MAX_VALUE))
    );
    panel_generalLayout.setVerticalGroup(
      panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_generalLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(combobox_debugMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel3))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel22)
        .addGap(8, 8, 8)
        .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(combobox_dateFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel4)
          .addComponent(bt_dfhelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(bt_videoViewer, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel11)
          .addComponent(tf_videoApp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(4, 4, 4)
        .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(bt_mainDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(tf_mainDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel7))
        .addContainerGap(105, Short.MAX_VALUE))
    );

    panel_generalLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {combobox_debugMode, jLabel22, jLabel3, jLabel4});

    panel_generalLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel11, tf_videoApp});

    tabbedPane_options.addTab("General", panel_general);

    panel_appearance.setToolTipText("Appearance options");
    panel_appearance.setName("Appearance"); // NOI18N
    panel_appearance.setOpaque(false);

    jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel5.setText("Look And Feel :");
    jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    jLabel5.setName("noname"); // NOI18N

    combobox_laf.setModel(model_laf);
    combobox_laf.setSelectedItem(Options.toString(Options.LOOK_AND_FEEL));
    combobox_laf.setName(Options.LOOK_AND_FEEL);
    combobox_laf.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        combobox_lafActionPerformed(evt);
      }
    });

    checkbox_dontUseSkin.setSelected(Options.toBoolean(Options.USE_SKIN));
    checkbox_dontUseSkin.setText("Use Skin :");
    checkbox_dontUseSkin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    checkbox_dontUseSkin.setName(Options.USE_SKIN);
    checkbox_dontUseSkin.setOpaque(false);
    checkbox_dontUseSkin.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        checkbox_dontUseSkinActionPerformed(evt);
      }
    });

    button_BGColor.setBackground(Options.toColor(Options.SKIN_COLOR));
    button_BGColor.setText("Set Color");
    button_BGColor.setName(Options.SKIN_COLOR);

    org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, checkbox_dontUseSkin, org.jdesktop.beansbinding.ELProperty.create("${selected}"), button_BGColor, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
    bindingGroup.addBinding(binding);

    button_BGColor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_BGColorActionPerformed(evt);
      }
    });

    combobox_fonts.setMinimumSize(new java.awt.Dimension(23, 20));
    combobox_fonts.setName(Options.FONT_FACE);
    combobox_fonts.setRenderer(new MyOptionsFontRenderer());
    combobox_fonts.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        combobox_fontsActionPerformed(evt);
      }
    });

    jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel10.setText("Font : ");
    jLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    jLabel10.setName("noname"); // NOI18N

    spinner_fontSize.setModel(new javax.swing.SpinnerNumberModel(1, 1, 36, 1));
    spinner_fontSize.setName(Options.FONT_SIZE);
    spinner_fontSize.setOpaque(false);
    spinner_fontSize.setValue((int)Options.toFloat(Options.FONT_SIZE));
    spinner_fontSize.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        spinner_fontSizeStateChanged(evt);
      }
    });

    jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel16.setText("Font preview :");
    jLabel16.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

    setFont(getSelectedFont());
    label_preview.setText(String.valueOf(combobox_fonts.getSelectedItem()));
    label_preview.setName("noname"); // NOI18N

    cb_minimizeTray.setSelected(Options.toBoolean(Options.MINIMIZE_TO_TRAY));
    cb_minimizeTray.setText("Minimize to tray");
    cb_minimizeTray.setToolTipText("Minimize Application to tray");
    cb_minimizeTray.setName(Options.MINIMIZE_TO_TRAY);
    cb_minimizeTray.setOpaque(false);

    cb_randomizeLaf.setSelected(Options.toBoolean(Options.RANDOMIZE_LAF));
    cb_randomizeLaf.setText("Use a random LAF on startup");
    cb_randomizeLaf.setName(Options.RANDOMIZE_LAF);
    cb_randomizeLaf.setOpaque(false);

    javax.swing.GroupLayout panel_appearanceLayout = new javax.swing.GroupLayout(panel_appearance);
    panel_appearance.setLayout(panel_appearanceLayout);
    panel_appearanceLayout.setHorizontalGroup(
      panel_appearanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_appearanceLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_appearanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panel_appearanceLayout.createSequentialGroup()
            .addComponent(cb_minimizeTray, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
          .addGroup(panel_appearanceLayout.createSequentialGroup()
            .addGroup(panel_appearanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
              .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
              .addComponent(checkbox_dontUseSkin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE))
            .addGroup(panel_appearanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(panel_appearanceLayout.createSequentialGroup()
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_appearanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                  .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_appearanceLayout.createSequentialGroup()
                    .addGroup(panel_appearanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                      .addComponent(cb_randomizeLaf, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                      .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_appearanceLayout.createSequentialGroup()
                        .addGroup(panel_appearanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                          .addComponent(button_BGColor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                          .addComponent(combobox_fonts, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinner_fontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(30, 30, 30))
                  .addComponent(label_preview, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))
                .addGap(184, 184, 184))
              .addGroup(panel_appearanceLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(combobox_laf, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))))))
    );
    panel_appearanceLayout.setVerticalGroup(
      panel_appearanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_appearanceLayout.createSequentialGroup()
        .addGap(23, 23, 23)
        .addGroup(panel_appearanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel5)
          .addComponent(combobox_laf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(cb_randomizeLaf)
        .addGap(8, 8, 8)
        .addGroup(panel_appearanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(checkbox_dontUseSkin)
          .addComponent(button_BGColor))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panel_appearanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(panel_appearanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(combobox_fonts, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(spinner_fontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panel_appearanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(label_preview, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(cb_minimizeTray)
        .addContainerGap(121, Short.MAX_VALUE))
    );

    panel_appearanceLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {combobox_fonts, jLabel10, spinner_fontSize});

    createLafModel();
    combobox_laf.setSelectedItem(Options.toString(Options.LOOK_AND_FEEL));

    tabbedPane_options.addTab("Appearance", panel_appearance);

    panel_internet.setName("Internet"); // NOI18N
    panel_internet.setOpaque(false);

    checkbox_useProxy.setSelected(Options.toBoolean(Options.USE_PROXY));
    checkbox_useProxy.setText("Proxy Settings");
    checkbox_useProxy.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    checkbox_useProxy.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    checkbox_useProxy.setMargin(new java.awt.Insets(0, 0, 0, 0));
    checkbox_useProxy.setName(Options.USE_PROXY);
    checkbox_useProxy.setOpaque(false);
    checkbox_useProxy.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        checkbox_useProxyActionPerformed(evt);
      }
    });

    jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel8.setText("Http proxy :");
    jLabel8.setName("noname"); // NOI18N

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, checkbox_useProxy, org.jdesktop.beansbinding.ELProperty.create("${selected}"), jLabel8, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
    bindingGroup.addBinding(binding);

    jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel9.setText("port :");
    jLabel9.setName("noname"); // NOI18N

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, checkbox_useProxy, org.jdesktop.beansbinding.ELProperty.create("${selected}"), jLabel9, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
    bindingGroup.addBinding(binding);

    textfield_proxy.setText(Options.toString(Options.PROXY_HOST));
    textfield_proxy.setName(Options.PROXY_HOST);

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, checkbox_useProxy, org.jdesktop.beansbinding.ELProperty.create("${selected}"), textfield_proxy, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
    bindingGroup.addBinding(binding);

    textfield_proxy.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        textfield_proxyKeyReleased(evt);
      }
    });

    jCheckBox1.setSelected(Options.toBoolean(Options.CHECK_VERSION));
    jCheckBox1.setText("Check for updates on startup");
    jCheckBox1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jCheckBox1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
    jCheckBox1.setName(Options.CHECK_VERSION);
    jCheckBox1.setOpaque(false);

    textfield_port.setText(Options.toString(Options.PROXY_PORT));
    textfield_port.setName(Options.PROXY_PORT);

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, checkbox_useProxy, org.jdesktop.beansbinding.ELProperty.create("${selected}"), textfield_port, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
    bindingGroup.addBinding(binding);

    textfield_port.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        textfield_portKeyReleased(evt);
      }
    });

    jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel13.setText("Primary subtitles lang. :");
    jLabel13.setToolTipText("When downloading subtitles this language will be queried first.");
    jLabel13.setName("noname"); // NOI18N

    combo_primaryLang.setModel(primarySubtitlesModel);
    combo_primaryLang.setSelectedItem(MySeries.languages.getLanguageByName(Options.toString(Options.PRIMARY_SUB)));
    combo_primaryLang.setName(Options.PRIMARY_SUB);
    combo_primaryLang.setOpaque(false);
    combo_primaryLang.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        combo_primaryLangActionPerformed(evt);
      }
    });

    jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel15.setText("Secondary subtitles lang. :");
    jLabel15.setToolTipText("When downloading subtitles this language will be queried first.");
    jLabel15.setName("noname"); // NOI18N

    combo_secondaryLang.setModel(secondarySubtitlesModel);
    combo_secondaryLang.setSelectedItem(MySeries.languages.getLanguageByName(Options.toString(Options.SECONDARY_SUB)));
    combo_secondaryLang.setMinimumSize(new java.awt.Dimension(23, 20));
    combo_secondaryLang.setName(Options.SECONDARY_SUB);
    combo_secondaryLang.setOpaque(false);
    combo_secondaryLang.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        combo_secondaryLangActionPerformed(evt);
      }
    });

    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel2.setText("Number of columns in Feeds :");

    spinner_columns.setModel(new javax.swing.SpinnerNumberModel(2, 1, 5, 1));
    spinner_columns.setName(Options.FEED_COLUMNS);
    spinner_columns.setValue(Options.toInt(Options.FEED_COLUMNS));

    cb_updateFeeds.setSelected(Options.toBoolean(Options.UPDATE_FEEDS));
    cb_updateFeeds.setText("Update feeds on startup");
    cb_updateFeeds.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    cb_updateFeeds.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    cb_updateFeeds.setMargin(new java.awt.Insets(0, 0, 0, 0));
    cb_updateFeeds.setName(Options.UPDATE_FEEDS);
    cb_updateFeeds.setOpaque(false);

    javax.swing.GroupLayout panel_internetLayout = new javax.swing.GroupLayout(panel_internet);
    panel_internet.setLayout(panel_internetLayout);
    panel_internetLayout.setHorizontalGroup(
      panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_internetLayout.createSequentialGroup()
        .addGroup(panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panel_internetLayout.createSequentialGroup()
            .addGap(10, 10, 10)
            .addGroup(panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(cb_updateFeeds, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
              .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
              .addComponent(checkbox_useProxy, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
              .addGroup(panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(panel_internetLayout.createSequentialGroup()
                  .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(18, 18, 18)
                  .addComponent(textfield_port, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panel_internetLayout.createSequentialGroup()
                  .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(18, 18, 18)
                  .addComponent(textfield_proxy, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))))
          .addGroup(panel_internetLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
              .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(combo_primaryLang, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(combo_secondaryLang, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(spinner_columns, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))))
        .addContainerGap())
    );
    panel_internetLayout.setVerticalGroup(
      panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_internetLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panel_internetLayout.createSequentialGroup()
            .addComponent(jLabel13)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel15)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel2))
          .addGroup(panel_internetLayout.createSequentialGroup()
            .addComponent(combo_primaryLang, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(combo_secondaryLang, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(spinner_columns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(cb_updateFeeds)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(15, 15, 15)
        .addComponent(checkbox_useProxy, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel8)
          .addComponent(textfield_proxy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel9)
          .addComponent(textfield_port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(83, Short.MAX_VALUE))
    );

    panel_internetLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {combo_primaryLang, jLabel13});

    panel_internetLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {combo_secondaryLang, jLabel15});

    panel_internetLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, spinner_columns});

    tabbedPane_options.addTab("Internet", panel_internet);

    panel_renaming.setName("Renaming"); // NOI18N
    panel_renaming.setOpaque(false);

    jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel18.setText("Season separator :");

    jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel19.setText("Episode separator :");

    jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel20.setText("Title separator :");

    tf_seasonSep.setText(Options.toString(Options.SEASON_SEPARATOR,false));
    tf_seasonSep.setName(Options.SEASON_SEPARATOR);

    tf_episodeSep.setText(Options.toString(Options.EPISODE_SEPARATOR,false));
    tf_episodeSep.setName(Options.EPISODE_SEPARATOR);

    tf_titleSep.setText(Options.toString(Options.TITLE_SEPARATOR,false));
    tf_titleSep.setName(Options.TITLE_SEPARATOR);

    cb_noRenameConf.setSelected(Options.toBoolean(Options.NO_RENAME_CONFIRMATION));
    cb_noRenameConf.setText("Fast single episode renaming");
    cb_noRenameConf.setToolTipText("<html>\nWhen this is checked renaming of a single episode will not bring up the renaming window<br />\nand the files will be renamed with the current renaming options");
    cb_noRenameConf.setName(Options.NO_RENAME_CONFIRMATION);
    cb_noRenameConf.setOpaque(false);

    cd_autoRename.setSelected(Options.toBoolean(Options.AUTO_RENAME_SUBS));
    cd_autoRename.setText("Auto Renaming");
    cd_autoRename.setToolTipText("<html>Rename subtitles when unzipped or moved from the main series directory to the series directory where they belong");
    cd_autoRename.setName(Options.AUTO_RENAME_SUBS);
    cd_autoRename.setOpaque(false);
    cd_autoRename.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cd_autoRenameActionPerformed(evt);
      }
    });

    jLabel6.setFont(jLabel6.getFont().deriveFont((jLabel6.getFont().getStyle() | java.awt.Font.ITALIC), jLabel6.getFont().getSize()-1));
    jLabel6.setText("(Rename single episodes without naming confirmation)");

    jLabel12.setFont(jLabel12.getFont().deriveFont((jLabel12.getFont().getStyle() | java.awt.Font.ITALIC), jLabel12.getFont().getSize()-1));
    jLabel12.setText("(Auto rename subtitles when downloaded, unziped or moved from the Main Series Directory)");

    javax.swing.GroupLayout panel_renamingLayout = new javax.swing.GroupLayout(panel_renaming);
    panel_renaming.setLayout(panel_renamingLayout);
    panel_renamingLayout.setHorizontalGroup(
      panel_renamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_renamingLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_renamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panel_renamingLayout.createSequentialGroup()
            .addGroup(panel_renamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(panel_renamingLayout.createSequentialGroup()
                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf_seasonSep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(panel_renamingLayout.createSequentialGroup()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf_episodeSep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(panel_renamingLayout.createSequentialGroup()
                .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf_titleSep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(524, 524, 524))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_renamingLayout.createSequentialGroup()
            .addGroup(panel_renamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addGroup(panel_renamingLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE))
              .addComponent(cb_noRenameConf, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE))
            .addGap(269, 269, 269))
          .addGroup(panel_renamingLayout.createSequentialGroup()
            .addGap(21, 21, 21)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGap(263, 263, 263))
          .addGroup(panel_renamingLayout.createSequentialGroup()
            .addComponent(cd_autoRename, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())))
    );
    panel_renamingLayout.setVerticalGroup(
      panel_renamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_renamingLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_renamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
          .addComponent(tf_seasonSep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panel_renamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
          .addComponent(tf_episodeSep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panel_renamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
          .addComponent(tf_titleSep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(cb_noRenameConf)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel6)
        .addGap(5, 5, 5)
        .addComponent(cd_autoRename, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel12)
        .addGap(291, 291, 291))
    );

    tabbedPane_options.addTab("Renaming", panel_renaming);

    panel_performance.setName("Performance"); // NOI18N

    jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel17.setText("Auto update Files :");
    jLabel17.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

    cb_autoUnzip.setFont(cb_autoUnzip.getFont().deriveFont((cb_autoUnzip.getFont().getStyle() | java.awt.Font.ITALIC), cb_autoUnzip.getFont().getSize()-1));
    cb_autoUnzip.setSelected(Options.toBoolean(Options.AUTO_EXTRACT_ZIPS));
    cb_autoUnzip.setText("(Also extract subtitles in zip files and delete the zip)");
    cb_autoUnzip.setName(Options.AUTO_EXTRACT_ZIPS);
    cb_autoUnzip.setOpaque(false);

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cb_autoUpdate, org.jdesktop.beansbinding.ELProperty.create("${selected}"), cb_autoUnzip, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
    bindingGroup.addBinding(binding);

    cb_autoUpdate.setFont(cb_autoUpdate.getFont().deriveFont((cb_autoUpdate.getFont().getStyle() | java.awt.Font.ITALIC), cb_autoUpdate.getFont().getSize()-1));
    cb_autoUpdate.setSelected(Options.toBoolean(Options.AUTO_FILE_UPDATING));
    cb_autoUpdate.setText("(Auto update of videos and subs from series folder)");
    cb_autoUpdate.setName(Options.AUTO_FILE_UPDATING);
    cb_autoUpdate.setOpaque(false);
    cb_autoUpdate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cb_autoUpdateActionPerformed(evt);
      }
    });

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, sl_memory, org.jdesktop.beansbinding.ELProperty.create(" Update Memory consumption every ${value} ms"), jLabel1, org.jdesktop.beansbinding.BeanProperty.create("text"));
    bindingGroup.addBinding(binding);

    sl_memory.setMajorTickSpacing(1000);
    sl_memory.setMaximum(10000);
    sl_memory.setMinorTickSpacing(200);
    sl_memory.setPaintTicks(true);
    sl_memory.setSnapToTicks(true);
    sl_memory.setToolTipText("Frequency of memory consumption update");
    sl_memory.setValue(Options.toInt(Options.MEMORY_CONSUMPTION_UPDATE));
    sl_memory.setName(Options.MEMORY_CONSUMPTION_UPDATE);
    sl_memory.setOpaque(false);

    jLabel21.setFont(jLabel21.getFont().deriveFont((jLabel21.getFont().getStyle() | java.awt.Font.ITALIC)));
    jLabel21.setText("(Set it to 0 to remove the memory indicator)");

    javax.swing.GroupLayout panel_performanceLayout = new javax.swing.GroupLayout(panel_performance);
    panel_performance.setLayout(panel_performanceLayout);
    panel_performanceLayout.setHorizontalGroup(
      panel_performanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_performanceLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_performanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panel_performanceLayout.createSequentialGroup()
            .addGroup(panel_performanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(sl_memory, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(panel_performanceLayout.createSequentialGroup()
            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(panel_performanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(cb_autoUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(cb_autoUnzip, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE))))
        .addGap(91, 91, 91))
    );
    panel_performanceLayout.setVerticalGroup(
      panel_performanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_performanceLayout.createSequentialGroup()
        .addGap(15, 15, 15)
        .addGroup(panel_performanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(sl_memory, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(panel_performanceLayout.createSequentialGroup()
            .addComponent(jLabel1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addGroup(panel_performanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(cb_autoUpdate))))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(cb_autoUnzip)
        .addContainerGap(189, Short.MAX_VALUE))
    );

    panel_performanceLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cb_autoUpdate, jLabel17});

    tabbedPane_options.addTab("Performance", panel_performance);

    javax.swing.GroupLayout panel_optionsLayout = new javax.swing.GroupLayout(panel_options);
    panel_options.setLayout(panel_optionsLayout);
    panel_optionsLayout.setHorizontalGroup(
      panel_optionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_optionsLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(tabbedPane_options, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(37, Short.MAX_VALUE))
    );
    panel_optionsLayout.setVerticalGroup(
      panel_optionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_optionsLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(tabbedPane_options, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    lb_title.setFont(lb_title.getFont().deriveFont(lb_title.getFont().getStyle() | java.awt.Font.BOLD, lb_title.getFont().getSize()+2));
    lb_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lb_title.setText("Options");

    bt_cancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_cancelActionPerformed(evt);
      }
    });

    bt_help.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_helpActionPerformed(evt);
      }
    });

    bt_ok.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_okActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addGap(58, 58, 58)
            .addComponent(lb_title, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
            .addComponent(bt_help, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(bt_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(bt_ok, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(panel_options, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(lb_title)
          .addComponent(bt_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(bt_help, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(panel_options, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(bt_ok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    );

    bindingGroup.bind();

    pack();
  }// </editor-fold>//GEN-END:initComponents

  /**
   * Saves the options
   * @throws java.io.IOException
   */
  private void saveOptions() throws IOException, ParseException {
    try {
      // Check for illegal arguments in date format
      MySeriesLogger.logger.log(Level.INFO, "Saving options");
      SimpleDateFormat f = new SimpleDateFormat(String.valueOf(combobox_dateFormat.getSelectedItem()));
      getOptionsComponents();
      Options.save();
      Options.getOptions();
      dispose();
      MySeries.glassPane.deactivate();
    } catch (FileNotFoundException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not save the options", ex);
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not write to options file", ex);
    } catch (IllegalArgumentException ex) {
      MyMessages.error("Wrong Arguments", "The date format pattern you provided is invalid", true);
      MySeriesLogger.logger.log(Level.WARNING, "The date format "
          + String.valueOf(combobox_dateFormat.getSelectedItem())
          + " pattern you provided is invalid", ex);
    }
  }

  private void combobox_lafActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combobox_lafActionPerformed
    //LookAndFeelInfo laf = (LookAndFeelInfo) lafMap.get(combobox_laf.getSelectedItem());
    //LookAndFeels.setLookAndFeel(m,laf);
  }//GEN-LAST:event_combobox_lafActionPerformed

  private void button_BGColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_BGColorActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "howing color browser");
    JColorChooser c = new JColorChooser(Options.toColor(Options.SKIN_COLOR));
    Color newColor = JColorChooser.showDialog(null, "Choose a background color", Options.toColor(Options.SKIN_COLOR));
    if (newColor != null) {
      MySeriesLogger.logger.log(Level.INFO, "Selected color {0}", newColor);
      button_BGColor.setBackground(newColor);
      colorsChanged = true;
    }
}//GEN-LAST:event_button_BGColorActionPerformed

  private void checkbox_dontUseSkinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkbox_dontUseSkinActionPerformed
    combobox_laf.setEnabled(!checkbox_dontUseSkin.isSelected());
    cb_randomizeLaf.setEnabled(!checkbox_dontUseSkin.isSelected());
  }//GEN-LAST:event_checkbox_dontUseSkinActionPerformed

  private void combobox_fontsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combobox_fontsActionPerformed
    label_preview.setFont(getSelectedFont());
    label_preview.setText(String.valueOf(combobox_fonts.getSelectedItem()));
  }//GEN-LAST:event_combobox_fontsActionPerformed

  private void textfield_proxyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textfield_proxyKeyReleased
    textfield_proxy.validateValue();
  }//GEN-LAST:event_textfield_proxyKeyReleased

  private void textfield_portKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textfield_portKeyReleased
    textfield_port.validateValue();
  }//GEN-LAST:event_textfield_portKeyReleased

  private void checkbox_useProxyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkbox_useProxyActionPerformed
    textfield_port.clearValidatorsList();
    textfield_proxy.clearValidatorsList();
    if (!checkbox_useProxy.isSelected()) {
      textfield_port.addValidator(new NullValidator());
      textfield_proxy.addValidator(new NullValidator());
    } else {
      textfield_port.addValidator(new PositiveNumberValidator(Options.toString(Options.PROXY_PORT), false, false));
      textfield_proxy.addValidator(new NoSpaceValidator(Options.toString(Options.PROXY_HOST), false));
    }
    textfield_port.validateValue();
    textfield_proxy.validateValue();
  }//GEN-LAST:event_checkbox_useProxyActionPerformed

  private void combo_secondaryLangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_secondaryLangActionPerformed
    combo_primaryLangActionPerformed(evt);
  }//GEN-LAST:event_combo_secondaryLangActionPerformed

  private void combo_primaryLangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_primaryLangActionPerformed
    CompareValidator val = (CompareValidator) combo_secondaryLang.getValidator(SValidator.COMPARE);
    val.setValueToCompareWith(combo_primaryLang.getSelectedItem().toString());
    val.setValue(combo_secondaryLang.getSelectedItem().toString());
    combo_secondaryLang.validateValue();
  }//GEN-LAST:event_combo_primaryLangActionPerformed

  private void spinner_fontSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinner_fontSizeStateChanged
    int size = Integer.parseInt(String.valueOf(spinner_fontSize.getValue()));
    Font newFont = new Font(combobox_fonts.getSelectedItem().toString(), Font.PLAIN, size);
    label_preview.setFont(newFont);
  }//GEN-LAST:event_spinner_fontSizeStateChanged

  private void cb_autoUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_autoUpdateActionPerformed
    if (!cb_autoUpdate.isSelected()) {
      cb_autoUnzip.setSelected(false);
    }
  }//GEN-LAST:event_cb_autoUpdateActionPerformed

  private void bt_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_cancelActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Canceled by the user");
    dispose();
    MySeries.glassPane.deactivate();
  }//GEN-LAST:event_bt_cancelActionPerformed

  private void bt_helpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_helpActionPerformed
    if (tabbedPane_options.getSelectedIndex() == GENERAL_OPTIONS_TAB) {
      MySeriesLogger.logger.log(Level.INFO, "Showing General options help window");
      HelpWindow helpWindow = new HelpWindow(HelpWindow.GENERAL_OPTIONS);
    } else if (tabbedPane_options.getSelectedIndex() == INTERNET_OPTIONS_TAB) {
      MySeriesLogger.logger.log(Level.INFO, "Showing Internet options help window");
      HelpWindow helpWindow = new HelpWindow(HelpWindow.INTERNET_OPTIONS);
    } else if (tabbedPane_options.getSelectedIndex() == RENAME_OPTIONS_TAB) {
      MySeriesLogger.logger.log(Level.INFO, "Showing Rename options help window");
      HelpWindow helpWindow = new HelpWindow(HelpWindow.RENAME_OPTIONS);
    }else if (tabbedPane_options.getSelectedIndex() == APPEARANCE_TAB) {
      MySeriesLogger.logger.log(Level.INFO, "Showing appearance options help window");
      HelpWindow helpWindow = new HelpWindow(HelpWindow.APPEARANCE_OPTIONS);
    }
  }//GEN-LAST:event_bt_helpActionPerformed

  private void bt_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_okActionPerformed
    boolean skinChange = false;
    ValidationGroup group = new ValidationGroup();
    group.addComponent(textfield_port);
    group.addComponent(textfield_proxy);
    group.addComponent(combo_secondaryLang);
    group.addComponent(tf_episodeSep);
    group.addComponent(tf_titleSep);
    group.addComponent(tf_seasonSep);
    group.addComponent(tf_videoApp);
    MySeriesLogger.logger.log(Level.INFO, "Validating user input");
    if (!group.validate()) {
      MySeriesLogger.logger.log(Level.WARNING, "Validation failed\nError message: {0}", group.getErrorMessage());
      MyMessages.error("Options Form", group.getErrorMessage(), true);
      return;
    }
    try {
      String mess = "";
      saveOptions();
      MyUsefulFunctions.createMemoryCons(m);
      MySeries.languages.setPrimary((Language) combo_primaryLang.getSelectedItem());
      MySeries.languages.setSecondary((Language) combo_secondaryLang.getSelectedItem());
      MyUsefulFunctions.initInternetConnection();
      MySeriesLogger.logger.setLevel(Level.parse(Options.toString(Options.DEBUG_MODE)));
      m.createComboBox_filters();
      if (!oldFontFace.equals(Options.toString(Options.FONT_FACE))) {
        MySeriesLogger.logger.log(Level.INFO, "Font face changed");
        mess = "Font face, ";
      }
      if (!oldFontSize.equals(Options.toString(Options.FONT_SIZE))) {
        MySeriesLogger.logger.log(Level.INFO, "Font size changed");
        mess += "Font size, ";
      }
      if (!oldColor.equals(Options.toColor(Options.SKIN_COLOR))) {
        MySeriesLogger.logger.log(Level.INFO, "Skin color changed");
        mess += "Skin color, ";
        skinChange = true;
      }
      if ((!oldUseSkin && checkbox_dontUseSkin.isSelected()) || (oldUseSkin && !checkbox_dontUseSkin.isSelected())) {
        MySeriesLogger.logger.log(Level.INFO, "Skin usage changed");
        mess += "Skin, ";
        skinChange = true;
      }
      if (!oldLaf.equals(Options.toString(Options.LOOK_AND_FEEL))) {
        MySeriesLogger.logger.log(Level.INFO, "Look and feel changed");
        mess += "Look and Feel, ";
      }
      mess = mess.trim();

      if (!mess.equals("")) {
       if(!skinChange){
        MyFont.SetMyFont();
        try {
           LookAndFeels.setLookAndFeel(Options.toString(Options.LOOK_AND_FEEL));
        } catch (Exception ex) {
          Logger.getLogger(OptionsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        SwingUtilities.updateComponentTreeUI(m);
        SwingUtilities.updateComponentTreeUI(m.seriesPopUp);
        SwingUtilities.updateComponentTreeUI(m.episodesPopUp);
       // m.pack();
       m.setExtendedState(Options.toInt(Options.WINDOW_STATE));
       } else {
        mess = "Skin changes ";
        int a = MyMessages.confirm("Options", mess + "will take effect when you restart the application\nRestart now?", false);
        if (a == JOptionPane.YES_OPTION) {
          ApplicationActions.restartApplication(m);
        }
      }
      }
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not write to options file", ex);
    } catch (ParseException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not parse options objects", ex);
    }
  }//GEN-LAST:event_bt_okActionPerformed

  private void bt_videoViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_videoViewerActionPerformed
    File folder;
    MySeriesLogger.logger.log(Level.INFO, "Selecting video viewer");
    String os = System.getProperty("os.name");
    if (os.toLowerCase().indexOf("windows") > -1) {
      folder = new File("C:\\Program Files");
    } else {
      folder = new File(System.getProperty("user.home"));
    }
    JFileChooser f = new JFileChooser();
    f.setDialogTitle("Choose the application to open video files");
    f.setDialogType(JFileChooser.OPEN_DIALOG);
    f.setFileSelectionMode(JFileChooser.FILES_ONLY);
    f.setMultiSelectionEnabled(false);
    if (folder.isDirectory()) {
      f.setCurrentDirectory(folder);
    }
    f.showOpenDialog(this);
    File sel = f.getSelectedFile();
    if (sel != null) {
      String app = sel.getAbsolutePath();
      tf_videoApp.setText(app);
      MySeriesLogger.logger.log(Level.INFO, "Video viewer set to {0}", app);
    }
  }//GEN-LAST:event_bt_videoViewerActionPerformed

  private void bt_mainDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_mainDirectoryActionPerformed
    File folder;
    MySeriesLogger.logger.log(Level.INFO, "Selecting series main directory");
    String os = System.getProperty("os.name");
    folder = new File(Options.toString(Options.MAIN_DIRECTORY));
    JFileChooser f = new JFileChooser();
    f.setDialogTitle("Choose your series main directory");
    f.setDialogType(JFileChooser.OPEN_DIALOG);
    f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    f.setMultiSelectionEnabled(false);
    if (folder.isDirectory()) {
      f.setCurrentDirectory(folder);
    }
    f.showOpenDialog(this);
    File sel = f.getSelectedFile();
    if (sel != null) {
      String md = sel.getAbsolutePath();
      tf_mainDir.setText(md);
      MySeriesLogger.logger.log(Level.INFO, "Main directory set to {0}", md);
    }
  }//GEN-LAST:event_bt_mainDirectoryActionPerformed

  private void bt_dfhelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_dfhelpActionPerformed
     try {
      DateFormatHelp dfh = new DateFormatHelp();
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not find date format help file", ex);
    }
  }//GEN-LAST:event_bt_dfhelpActionPerformed

  private void cd_autoRenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cd_autoRenameActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cd_autoRenameActionPerformed

  private Font getSelectedFont() {
    Font font = new Font((String) combobox_fonts.getSelectedItem(), Font.PLAIN, (int) Float.parseFloat(String.valueOf(spinner_fontSize.getValue())));
    return font;
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private myComponents.myGUI.buttons.MyButtonCancel bt_cancel;
  private myComponents.myGUI.buttons.MyButtonHelp bt_dfhelp;
  private myComponents.myGUI.buttons.MyButtonHelp bt_help;
  private myComponents.myGUI.buttons.MyButtonDir bt_mainDirectory;
  private myComponents.myGUI.buttons.MyButtonOk bt_ok;
  private myComponents.myGUI.buttons.MyButtonDir bt_videoViewer;
  private javax.swing.JButton button_BGColor;
  private javax.swing.JCheckBox cb_autoUnzip;
  private javax.swing.JCheckBox cb_autoUpdate;
  private javax.swing.JCheckBox cb_minimizeTray;
  private javax.swing.JCheckBox cb_noRenameConf;
  private javax.swing.JCheckBox cb_randomizeLaf;
  private javax.swing.JCheckBox cb_updateFeeds;
  private javax.swing.JCheckBox cd_autoRename;
  private javax.swing.JCheckBox checkbox_dontUseSkin;
  private javax.swing.JCheckBox checkbox_useProxy;
  private com.googlecode.svalidators.formcomponents.SComboBox combo_primaryLang;
  private com.googlecode.svalidators.formcomponents.SComboBox combo_secondaryLang;
  private javax.swing.JComboBox combobox_dateFormat;
  private javax.swing.JComboBox combobox_debugMode;
  private com.googlecode.svalidators.formcomponents.SComboBox combobox_fonts;
  private javax.swing.JComboBox combobox_laf;
  private javax.swing.JCheckBox jCheckBox1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel11;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel14;
  private javax.swing.JLabel jLabel15;
  private javax.swing.JLabel jLabel16;
  private javax.swing.JLabel jLabel17;
  private javax.swing.JLabel jLabel18;
  private javax.swing.JLabel jLabel19;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel20;
  private javax.swing.JLabel jLabel21;
  private javax.swing.JLabel jLabel22;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JMenu jMenu1;
  private javax.swing.JMenu jMenu2;
  private javax.swing.JMenuBar jMenuBar1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JLabel label_preview;
  private javax.swing.JLabel lb_title;
  private javax.swing.JPanel panel_DateFormatHelp;
  private javax.swing.JPanel panel_appearance;
  private javax.swing.JPanel panel_general;
  private javax.swing.JPanel panel_internet;
  private javax.swing.JPanel panel_options;
  private javax.swing.JPanel panel_performance;
  private javax.swing.JPanel panel_renaming;
  private javax.swing.JSlider sl_memory;
  private javax.swing.JSpinner spinner_columns;
  private javax.swing.JSpinner spinner_fontSize;
  private javax.swing.JTabbedPane tabbedPane_options;
  private com.googlecode.svalidators.formcomponents.STextField textfield_port;
  private com.googlecode.svalidators.formcomponents.STextField textfield_proxy;
  private com.googlecode.svalidators.formcomponents.STextField tf_episodeSep;
  private com.googlecode.svalidators.formcomponents.STextField tf_mainDir;
  private com.googlecode.svalidators.formcomponents.STextField tf_seasonSep;
  private com.googlecode.svalidators.formcomponents.STextField tf_titleSep;
  private com.googlecode.svalidators.formcomponents.STextField tf_videoApp;
  private org.jdesktop.beansbinding.BindingGroup bindingGroup;
  // End of variables declaration//GEN-END:variables

  /**
   * Parsing all the options panels for option components
   */
  private void getOptionsComponents() {
    parse(panel_general);
    parse(panel_appearance);
    parse(panel_internet);
    parse(panel_renaming);
    parse(panel_performance);
    
  }

  /**
   * Parsing a jpanel and gets components with a name diff than "noname"<br />
   * Then sets the option with the name of the component to the value of the componont
   * @param panel
   */
  private void parse(JPanel panel) {
    MySeriesLogger.logger.log(Level.INFO, "Parsing panel {0}", panel.getName());
    Component[] c = panel.getComponents();
    for (int i = 0; i < c.length; i++) {
      if (c[i].getName() == null) {
      } else if (!c[i].getName().equals("noname") && !c[i].getName().equals("null")) {
        Options.setOption(c[i].getName(), getValue(c[i]));
      }
    }
  }

  /**
   * Getting a value of a component <br />
   * Components are JSpinner, JCheckBox, JComboBox
   * @param c The component
   * @return the value of the component
   */
  private Object getValue(Component c) {
    MySeriesLogger.logger.log(Level.INFO, "Getting {0} components value", c.getName());
    Object obj = "";
    if (c instanceof JSpinner) {
      JSpinner spin = (JSpinner) c;
      obj = spin.getValue();
    } else if (c instanceof JCheckBox) {
      JCheckBox check = (JCheckBox) c;
      obj = check.isSelected();
    } else if (c instanceof JTextField) {
      JTextField text = (JTextField) c;
      obj = text.getText();
    } else if (c instanceof JComboBox) {
      JComboBox combo = (JComboBox) c;
      // In some combos get the item instead of index
      String name = combo.getName();
      if (MyUsefulFunctions.isInArray(Options._COMBO_OPTIONS_, name)) {
        obj = String.valueOf(combo.getSelectedItem());
      } else {
        obj = combo.getSelectedIndex();
      }
    } else // Get color buttons
    if (c instanceof JButton) {
      JButton button = (JButton) c;
      return button.getBackground().getRed() + ", "
          + button.getBackground().getGreen() + ", "
          + button.getBackground().getBlue();
    } else if (c instanceof JSlider){
        JSlider sl = (JSlider) c;
         obj = sl.getValue();
    }
    MySeriesLogger.logger.log(Level.INFO, "Components value is {0}", obj);
    return obj;
  }

    private void addSliderLabels() {
        Hashtable labelTable = new Hashtable();
        labelTable.put( new Integer( 0 ), new JLabel("0") );
        labelTable.put( new Integer( 5000 ), new JLabel("5000") );
        labelTable.put( new Integer( 10000 ), new JLabel("10000") );
        sl_memory.setLabelTable(labelTable);
        sl_memory.setPaintLabels(true);
    }
}
