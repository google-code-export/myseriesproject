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
import java.awt.GraphicsEnvironment;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
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
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myComponents.myGUI.MyDraggable;
import myseries.MySeries;
import myseries.StartPanel;
import myseries.episodes.NextEpisodes;
import com.googlecode.svalidators.formcomponents.ValidationGroup;
import com.googlecode.svalidators.validators.CompareValidator;
import com.googlecode.svalidators.validators.ListValidator;
import com.googlecode.svalidators.validators.NoSpaceValidator;
import com.googlecode.svalidators.validators.NullValidator;
import com.googlecode.svalidators.validators.PositiveNumberValidator;
import com.googlecode.svalidators.validators.RegularExpressionValidator;
import com.googlecode.svalidators.validators.SValidator;
import tools.LookAndFeels;
import tools.Skin;
import tools.download.subtitles.Subtitle;
import tools.internetUpdate.InternetUpdate;
import tools.languages.Language;

/**
 *
 * @author lordovol
 */
public class OptionsPanel extends MyDraggable {

  public static final long serialVersionUID = 5676235253653L;
  private MySeries m;
  private DefaultComboBoxModel model_laf = new DefaultComboBoxModel();
  private HashMap<String, LookAndFeelInfo> lafMap;
  private ComboBoxModel model_fonts;
  private boolean colorsChanged;
  private String oldFontSize;
  private String oldFontFace;
  private Color oldColor;
  private boolean oldUseSkin;
  private ComboBoxModel updateDbModel = new DefaultComboBoxModel(InternetUpdate.DB_UPDATERS);
  private ComboBoxModel primarySubtitlesModel = new DefaultComboBoxModel(Subtitle.SUBTITLE_LANG);
  private ComboBoxModel secondarySubtitlesModel = new DefaultComboBoxModel(Subtitle.SUBTITLE_LANG);
  private ComboBoxModel subtitleSitesModel = new DefaultComboBoxModel(Subtitle.SUBTITLE_SITES);
  private String sepRegex = "^[^/\\\\?%*:|\\\"<>\\.]*$";

  /** Creates new form OptionsPanel
   * @param m MySeries main form
   */
  public OptionsPanel(MySeries m) {
    this.m = m;
    initComponents();
    tf_episodeSep.addValidator(new RegularExpressionValidator("", sepRegex, false, false));
    tf_seasonSep.addValidator(new RegularExpressionValidator("", sepRegex, false, false));
    tf_titleSep.addValidator(new RegularExpressionValidator("", sepRegex, false, false));
    tf_episodeSep.setTrimValue(false);
    tf_seasonSep.setTrimValue(false);
    tf_titleSep.setTrimValue(false);
    combo_secondaryLang.addValidator(
            new CompareValidator(combo_secondaryLang.getSelectedItem().toString(),
            combo_primaryLang.getSelectedItem().toString(),
            CompareValidator.Type.NOT_EQUAL, true));
    checkbox_useProxyActionPerformed(null);
    setLocationRelativeTo(m);
    oldFontFace = Options.toString(Options.FONT_FACE);
    oldFontSize = Options.toString(Options.FONT_SIZE);
    oldColor = Options.toColor(Options.SKIN_COLOR);
    oldUseSkin = Options.toBoolean(Options.USE_SKIN);
    createModelFonts();
    JTextField t = (JTextField) combobox_fonts.getEditor().getEditorComponent();
    t.setText(Options.toString(Options.FONT_FACE));
    setVisible(true);
  }

  private void createLafModel() {
    LookAndFeelInfo[] laf = LookAndFeels.getLookAndFeels();
    lafMap = new HashMap<String, LookAndFeelInfo>();
    String lafNames[] = new String[laf.length];
    for (int i = 0; i < laf.length; i++) {
      LookAndFeelInfo lookAndFeelInfo = laf[i];
      lafMap.put(lookAndFeelInfo.getName(), lookAndFeelInfo);
      lafNames[i] = lookAndFeelInfo.getName();
    }
    combobox_laf.setModel(new DefaultComboBoxModel(lafNames));
  }

  private void createModelFonts() {
    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    String[] fonts = env.getAvailableFontFamilyNames();
    model_fonts = new DefaultComboBoxModel(fonts);
    combobox_fonts.setModel(model_fonts);
    model_fonts.setSelectedItem(Options.toString(Options.FONT_FACE));
    combobox_fonts.addValidator(new ListValidator("", fonts, false));
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
    panel_options = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    button_ok = new javax.swing.JButton();
    button_cancel = new javax.swing.JButton();
    tabbedPane_options = new javax.swing.JTabbedPane();
    panel_general = new javax.swing.JPanel();
    button_BGColor = new javax.swing.JButton();
    jLabel3 = new javax.swing.JLabel();
    combobox_debugMode = new javax.swing.JComboBox();
    checkBox_modal = new javax.swing.JCheckBox();
    jLabel4 = new javax.swing.JLabel();
    combobox_dateFormat = new javax.swing.JComboBox();
    button_dateFormatHelp = new javax.swing.JButton();
    jLabel5 = new javax.swing.JLabel();
    combobox_laf = new javax.swing.JComboBox();
    jLabel7 = new javax.swing.JLabel();
    checkbox_dontUseSkin = new javax.swing.JCheckBox();
    jLabel6 = new javax.swing.JLabel();
    jLabel10 = new javax.swing.JLabel();
    spinner_fontSize = new javax.swing.JSpinner();
    combobox_fonts = new com.googlecode.svalidators.formcomponents.SComboBox();
    label_preview = new javax.swing.JLabel();
    jLabel16 = new javax.swing.JLabel();
    jLabel17 = new javax.swing.JLabel();
    jCheckBox2 = new javax.swing.JCheckBox();
    panel_nextepisodes = new javax.swing.JPanel();
    jLabel2 = new javax.swing.JLabel();
    checkbox_showUnseen = new javax.swing.JCheckBox();
    spinner_maxNextEpisodes = new javax.swing.JSpinner();
    checkbox_showNotDownloaded = new javax.swing.JCheckBox();
    panel_internet = new javax.swing.JPanel();
    checkbox_useProxy = new javax.swing.JCheckBox();
    jLabel8 = new javax.swing.JLabel();
    jLabel9 = new javax.swing.JLabel();
    textfield_proxy = new com.googlecode.svalidators.formcomponents.STextField(new NoSpaceValidator("",false));
    jCheckBox1 = new javax.swing.JCheckBox();
    jLabel12 = new javax.swing.JLabel();
    textfield_port = new com.googlecode.svalidators.formcomponents.STextField(new PositiveNumberValidator("",false,false));
    jLabel13 = new javax.swing.JLabel();
    combo_primaryLang = new com.googlecode.svalidators.formcomponents.SComboBox();
    jLabel15 = new javax.swing.JLabel();
    combo_secondaryLang = new com.googlecode.svalidators.formcomponents.SComboBox();
    panel_renaming = new javax.swing.JPanel();
    jLabel18 = new javax.swing.JLabel();
    jLabel19 = new javax.swing.JLabel();
    jLabel20 = new javax.swing.JLabel();
    tf_seasonSep = new com.googlecode.svalidators.formcomponents.STextField();
    tf_episodeSep = new com.googlecode.svalidators.formcomponents.STextField();
    tf_titleSep = new com.googlecode.svalidators.formcomponents.STextField();

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

    panel_options.setBackground(new java.awt.Color(255, 255, 255));
    panel_options.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    panel_options.setOpaque(false);

    jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, jLabel1.getFont().getSize()+2));
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Options");

    button_ok.setText("OK");
    button_ok.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_okActionPerformed(evt);
      }
    });

    button_cancel.setText("Cancel");
    button_cancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_cancelActionPerformed(evt);
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

    jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel3.setText("Logging level :");
    jLabel3.setToolTipText("<html>\nThe level of Debuging info that's written in the log file<br />\nFATAL : Only fatal errors are written<br />\nWARNING : Warnings and fatal errors are written<br />\nINFO : Everything is written<br />\nNO LOGGING: Nothing is written<br />");
    jLabel3.setName("noname"); // NOI18N

    combobox_debugMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "OFF", "ALL", "SEVERE", "WARNING", "INFO" }));
    combobox_debugMode.setSelectedItem(Options.toString(Options.DEBUG_MODE));
    combobox_debugMode.setToolTipText("<html>\nThe level of Debuging info that's written in the log file<br />\nFATAL : Only fatal errors are written<br />\nWARNING : Warnings and fatal errors are written<br />\nINFO : Everything is written<br />\nNO LOGGING: Nothing is written<br />");
    combobox_debugMode.setName(Options.DEBUG_MODE);
    combobox_debugMode.setOpaque(false);

    checkBox_modal.setSelected(Options.toBoolean(Options.MODAL));
    checkBox_modal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    checkBox_modal.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    checkBox_modal.setMargin(new java.awt.Insets(0, 0, 0, 0));
    checkBox_modal.setName(Options.MODAL);
    checkBox_modal.setOpaque(false);

    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel4.setText("Date Format :");
    jLabel4.setName("noname"); // NOI18N

    combobox_dateFormat.setEditable(true);
    combobox_dateFormat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dd/MM/yyyy", "dd-MM-yyyy", "dd/MM/yy", "dd-MM-yy", "d/M/yyyy", "d/M/yy" }));
    combobox_dateFormat.setSelectedItem(Options.toString(Options.DATE_FORMAT));
    combobox_dateFormat.setName(Options.DATE_FORMAT);

    button_dateFormatHelp.setBackground(new java.awt.Color(255, 255, 255));
    button_dateFormatHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
    button_dateFormatHelp.setToolTipText("Brings up help about formating date");
    button_dateFormatHelp.setBorder(null);
    button_dateFormatHelp.setBorderPainted(false);
    button_dateFormatHelp.setContentAreaFilled(false);
    button_dateFormatHelp.setFocusPainted(false);
    button_dateFormatHelp.setName("noname"); // NOI18N
    button_dateFormatHelp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_dateFormatHelpActionPerformed(evt);
      }
    });

    jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel5.setText("Look And Feel :");
    jLabel5.setName("noname"); // NOI18N

    combobox_laf.setModel(model_laf);
    combobox_laf.setSelectedItem(Options.toString(Options.LOOK_AND_FEEL));
    combobox_laf.setName(Options.LOOK_AND_FEEL);
    combobox_laf.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        combobox_lafActionPerformed(evt);
      }
    });

    jLabel7.setText("(Not Available yet)");
    jLabel7.setName("noname"); // NOI18N

    checkbox_dontUseSkin.setSelected(Options.toBoolean(Options.USE_SKIN));
    checkbox_dontUseSkin.setText("Use Skin Color :");
    checkbox_dontUseSkin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    checkbox_dontUseSkin.setName(Options.USE_SKIN);
    checkbox_dontUseSkin.setOpaque(false);
    checkbox_dontUseSkin.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        checkbox_dontUseSkinActionPerformed(evt);
      }
    });

    jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel6.setText("Modal Windows :");
    jLabel6.setName("noname"); // NOI18N

    jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel10.setText("Font : ");
    jLabel10.setName("noname"); // NOI18N

    spinner_fontSize.setModel(new javax.swing.SpinnerNumberModel(1, 1, 36, 1));
    spinner_fontSize.setName(Options.FONT_SIZE);
    spinner_fontSize.setOpaque(false);
    spinner_fontSize.setValue(Options.toFloat(Options.FONT_SIZE));
    spinner_fontSize.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        spinner_fontSizeStateChanged(evt);
      }
    });

    combobox_fonts.setName(Options.FONT_FACE);
    combobox_fonts.setRenderer(new MyOptionsFontRenderer());
    combobox_fonts.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        combobox_fontsActionPerformed(evt);
      }
    });

    setFont(getSelectedFont());
    label_preview.setText(String.valueOf(combobox_fonts.getSelectedItem()));
    label_preview.setName("noname"); // NOI18N

    jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel16.setText("Font preview :");

    jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel17.setText("Auto update :");

    jCheckBox2.setFont(jCheckBox2.getFont().deriveFont((jCheckBox2.getFont().getStyle() | java.awt.Font.ITALIC), jCheckBox2.getFont().getSize()-1));
    jCheckBox2.setSelected(Options.toBoolean(Options.AUTO_FILE_UPDATING));
    jCheckBox2.setText("(Auto update of videos and subs from series folder)");
    jCheckBox2.setName(Options.AUTO_FILE_UPDATING);
    jCheckBox2.setOpaque(false);

    javax.swing.GroupLayout panel_generalLayout = new javax.swing.GroupLayout(panel_general);
    panel_general.setLayout(panel_generalLayout);
    panel_generalLayout.setHorizontalGroup(
      panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_generalLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panel_generalLayout.createSequentialGroup()
            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_generalLayout.createSequentialGroup()
            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_generalLayout.createSequentialGroup()
            .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
              .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
              .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
              .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
              .addComponent(checkbox_dontUseSkin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(4, 4, 4))
          .addGroup(panel_generalLayout.createSequentialGroup()
            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
        .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panel_generalLayout.createSequentialGroup()
            .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addGroup(panel_generalLayout.createSequentialGroup()
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(combobox_fonts, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(button_BGColor, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                .addComponent(combobox_laf, 0, 183, Short.MAX_VALUE)
                .addComponent(combobox_dateFormat, 0, 183, Short.MAX_VALUE)
                .addComponent(checkBox_modal)
                .addComponent(combobox_debugMode, 0, 183, Short.MAX_VALUE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(button_dateFormatHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(panel_generalLayout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(90, 90, 90))
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_generalLayout.createSequentialGroup()
                .addComponent(spinner_fontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(141, 141, 141))))
          .addComponent(jCheckBox2)
          .addComponent(label_preview, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE))
        .addContainerGap())
    );
    panel_generalLayout.setVerticalGroup(
      panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_generalLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabel3)
          .addComponent(combobox_debugMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(3, 3, 3)
        .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabel6)
          .addComponent(checkBox_modal))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabel4)
          .addComponent(combobox_dateFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(button_dateFormatHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabel5)
          .addComponent(combobox_laf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel7))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(checkbox_dontUseSkin)
          .addComponent(button_BGColor))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
            .addComponent(spinner_fontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(combobox_fonts, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabel16)
          .addComponent(label_preview, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panel_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jCheckBox2))
        .addGap(70, 70, 70))
    );

    panel_generalLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {button_dateFormatHelp, combobox_dateFormat});

    panel_generalLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {checkbox_dontUseSkin, jLabel3, jLabel4, jLabel5, jLabel6});

    createLafModel();
    combobox_laf.setSelectedItem(Options.toString(Options.LOOK_AND_FEEL));

    tabbedPane_options.addTab("General", panel_general);

    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel2.setText("Maximum number of episodes :");
    jLabel2.setName("noname"); // NOI18N

    checkbox_showUnseen.setSelected(Options.toBoolean(Options.SHOW_UNSEEN));
    checkbox_showUnseen.setText("Show downloaded but not seen yet :");
    checkbox_showUnseen.setToolTipText("<html>\nShow episodes that have been downloaded but not seen yet");
    checkbox_showUnseen.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    checkbox_showUnseen.setName(Options.SHOW_UNSEEN);
    checkbox_showUnseen.setOpaque(false);

    spinner_maxNextEpisodes.setModel(new javax.swing.SpinnerNumberModel(5, 1, 99, 1));
    spinner_maxNextEpisodes.setToolTipText("The maximum number of episodes");
    spinner_maxNextEpisodes.setName(Options.NEXT_EPISODES_LIMIT);
    spinner_maxNextEpisodes.setValue(Options.toInt(Options.NEXT_EPISODES_LIMIT));

    checkbox_showNotDownloaded.setSelected(Options.toBoolean(Options.SHOW_UNDOWNLOADED));
    checkbox_showNotDownloaded.setText("Show not downloaded :");
    checkbox_showNotDownloaded.setToolTipText("<html>\nShow episodes that have been aired in the past but not have been downloaded yet");
    checkbox_showNotDownloaded.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    checkbox_showNotDownloaded.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    checkbox_showNotDownloaded.setName(Options.SHOW_UNDOWNLOADED);
    checkbox_showNotDownloaded.setOpaque(false);

    javax.swing.GroupLayout panel_nextepisodesLayout = new javax.swing.GroupLayout(panel_nextepisodes);
    panel_nextepisodes.setLayout(panel_nextepisodesLayout);
    panel_nextepisodesLayout.setHorizontalGroup(
      panel_nextepisodesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_nextepisodesLayout.createSequentialGroup()
        .addGap(28, 28, 28)
        .addGroup(panel_nextepisodesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panel_nextepisodesLayout.createSequentialGroup()
            .addGap(3, 3, 3)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(spinner_maxNextEpisodes, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
          .addGroup(panel_nextepisodesLayout.createSequentialGroup()
            .addGroup(panel_nextepisodesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(checkbox_showUnseen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addGroup(panel_nextepisodesLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(checkbox_showNotDownloaded, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGap(25, 25, 25)))
        .addGap(248, 248, 248))
    );
    panel_nextepisodesLayout.setVerticalGroup(
      panel_nextepisodesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_nextepisodesLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_nextepisodesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(spinner_maxNextEpisodes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(checkbox_showUnseen)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(checkbox_showNotDownloaded)
        .addContainerGap(189, Short.MAX_VALUE))
    );

    tabbedPane_options.addTab("Next Episodes", panel_nextepisodes);

    checkbox_useProxy.setSelected(Options.toBoolean(Options.USE_PROXY));
    checkbox_useProxy.setText("Proxy Settings");
    checkbox_useProxy.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    checkbox_useProxy.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
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
    jCheckBox1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jCheckBox1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
    jCheckBox1.setName(Options.CHECK_VERSION);
    jCheckBox1.setOpaque(false);

    jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel12.setText("Check for updates :");
    jLabel12.setName("noname"); // NOI18N

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

    javax.swing.GroupLayout panel_internetLayout = new javax.swing.GroupLayout(panel_internet);
    panel_internet.setLayout(panel_internetLayout);
    panel_internetLayout.setHorizontalGroup(
      panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_internetLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panel_internetLayout.createSequentialGroup()
            .addGroup(panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(checkbox_useProxy, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
              .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
              .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
              .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(combo_primaryLang, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
              .addComponent(jCheckBox1)
              .addComponent(combo_secondaryLang, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE))
            .addGap(152, 152, 152))
          .addGroup(panel_internetLayout.createSequentialGroup()
            .addGroup(panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addGroup(panel_internetLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
              .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
            .addGap(18, 18, 18)
            .addGroup(panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(textfield_port, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
              .addComponent(textfield_proxy, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
            .addGap(220, 220, 220))))
    );
    panel_internetLayout.setVerticalGroup(
      panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_internetLayout.createSequentialGroup()
        .addGap(21, 21, 21)
        .addGroup(panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
          .addComponent(jCheckBox1))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabel13)
          .addComponent(combo_primaryLang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabel15)
          .addComponent(combo_secondaryLang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(33, 33, 33)
        .addComponent(checkbox_useProxy)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(panel_internetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(panel_internetLayout.createSequentialGroup()
            .addComponent(jLabel8)
            .addGap(10, 10, 10)
            .addComponent(jLabel9))
          .addGroup(panel_internetLayout.createSequentialGroup()
            .addComponent(textfield_proxy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(4, 4, 4)
            .addComponent(textfield_port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addGap(143, 143, 143))
    );

    tabbedPane_options.addTab("Internet", panel_internet);

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

    javax.swing.GroupLayout panel_renamingLayout = new javax.swing.GroupLayout(panel_renaming);
    panel_renaming.setLayout(panel_renamingLayout);
    panel_renamingLayout.setHorizontalGroup(
      panel_renamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_renamingLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_renamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panel_renamingLayout.createSequentialGroup()
            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(tf_seasonSep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(panel_renamingLayout.createSequentialGroup()
            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(tf_episodeSep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(panel_renamingLayout.createSequentialGroup()
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(tf_titleSep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addGap(291, 291, 291))
    );
    panel_renamingLayout.setVerticalGroup(
      panel_renamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_renamingLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_renamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel18)
          .addComponent(tf_seasonSep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panel_renamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel19)
          .addComponent(tf_episodeSep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panel_renamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel20)
          .addComponent(tf_titleSep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(185, Short.MAX_VALUE))
    );

    tabbedPane_options.addTab("Renaming", panel_renaming);

    javax.swing.GroupLayout panel_optionsLayout = new javax.swing.GroupLayout(panel_options);
    panel_options.setLayout(panel_optionsLayout);
    panel_optionsLayout.setHorizontalGroup(
      panel_optionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_optionsLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_optionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_optionsLayout.createSequentialGroup()
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
            .addContainerGap())
          .addGroup(panel_optionsLayout.createSequentialGroup()
            .addComponent(button_ok, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(button_cancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGap(380, 380, 380))
          .addGroup(panel_optionsLayout.createSequentialGroup()
            .addComponent(tabbedPane_options, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())))
    );
    panel_optionsLayout.setVerticalGroup(
      panel_optionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_optionsLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
        .addGap(23, 23, 23)
        .addComponent(tabbedPane_options, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addGroup(panel_optionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(button_ok)
          .addComponent(button_cancel))
        .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(panel_options, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(panel_options, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    bindingGroup.bind();

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void button_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_cancelActionPerformed
    dispose();
    MySeries.glassPane.deactivate();
}//GEN-LAST:event_button_cancelActionPerformed

  /**
   * Saves the options
   * @throws java.io.IOException
   */
  private void saveOptions() throws IOException, ParseException {
    try {
      // Check for illegal arguments in date format
      SimpleDateFormat f = new SimpleDateFormat(String.valueOf(combobox_dateFormat.getSelectedItem()));
      getOptionsComponents();
      Options.save();
      Options.getOptions();
      NextEpisodes.createNextEpisodes();
      NextEpisodes.show();
      dispose();
      MySeries.glassPane.deactivate();
    } catch (FileNotFoundException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (IllegalArgumentException ex) {
      MyMessages.error("Wrong Arguments", "The date format pattern you provided is invalid");
      MySeries.logger.log(Level.WARNING, "The date format "
              + String.valueOf(combobox_dateFormat.getSelectedItem())
              + " pattern you provided is invalid", ex);
    }
  }

  private void button_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_okActionPerformed
    ValidationGroup group = new ValidationGroup();
    group.addComponent(textfield_port);
    group.addComponent(textfield_proxy);
    group.addComponent(combo_secondaryLang);
    group.addComponent(tf_episodeSep);
    group.addComponent(tf_titleSep);
    group.addComponent(tf_seasonSep);
    if (!group.validate()) {
      group.errorMessage(true);
      return;
    }
    try {
      String mess = "";
      saveOptions();
      MySeries.languages.setPrimary((Language) combo_primaryLang.getSelectedItem());
      MySeries.languages.setSecondary((Language) combo_secondaryLang.getSelectedItem());
      MyUsefulFunctions.initInternetConnection();
      m.createComboBox_filters();
      if (!oldFontFace.equals(Options.toString(Options.FONT_FACE))) {
        mess = "Font face was changed\n";
      }
      if (!oldFontSize.equals(Options.toString(Options.FONT_SIZE))) {
        mess += "Font size was changed\n";
      }
      if (!oldColor.equals(Options.toColor(Options.SKIN_COLOR))) {
        mess += "Skin color was changed\n";
      }

      if ((!oldUseSkin && checkbox_dontUseSkin.isSelected()) || (oldUseSkin && !checkbox_dontUseSkin.isSelected())) {
        mess += "Skin using has changed\n";
      }
      if (!mess.equals("")) {
        int ans = MyMessages.question("Restart?",
                mess + "\nRestart The application?");
        if (ans == 0) {
          m.dispose();
          StartPanel.main(null);
        } else {
        }
      }

    } catch (IOException ex) {
      MySeries.logger.log(Level.SEVERE, "Could not write to options file", ex);
    } catch (ParseException ex) {
      MySeries.logger.log(Level.SEVERE, "Could not parse options objects", ex);
    }
  }//GEN-LAST:event_button_okActionPerformed

  private void button_dateFormatHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_dateFormatHelpActionPerformed
    try {
      DateFormatHelp dfh = new DateFormatHelp();
    } catch (IOException ex) {
      MySeries.logger.log(Level.SEVERE, "Could not find date format help file", ex);
    }
}//GEN-LAST:event_button_dateFormatHelpActionPerformed

  private void combobox_lafActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combobox_lafActionPerformed
    //LookAndFeelInfo laf = (LookAndFeelInfo) lafMap.get(combobox_laf.getSelectedItem());
    //LookAndFeels.setLookAndFeel(m,laf);
  }//GEN-LAST:event_combobox_lafActionPerformed

  private void button_BGColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_BGColorActionPerformed
    JColorChooser c = new JColorChooser(Options.toColor(Options.SKIN_COLOR));
    Color newColor = JColorChooser.showDialog(null, "Choose a background color", Options.toColor(Options.SKIN_COLOR));
    button_BGColor.setBackground(newColor);
    colorsChanged = true;

}//GEN-LAST:event_button_BGColorActionPerformed

  private void checkbox_dontUseSkinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkbox_dontUseSkinActionPerformed
    Skin.applySkin();
    UIManager.getLookAndFeel().initialize();
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

  private Font getSelectedFont() {
    Font font = new Font((String) combobox_fonts.getSelectedItem(), Font.PLAIN, (int) Float.parseFloat(String.valueOf(spinner_fontSize.getValue())));
    return font;
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton button_BGColor;
  private javax.swing.JButton button_cancel;
  private javax.swing.JButton button_dateFormatHelp;
  private javax.swing.JButton button_ok;
  private javax.swing.JCheckBox checkBox_modal;
  private javax.swing.JCheckBox checkbox_dontUseSkin;
  private javax.swing.JCheckBox checkbox_showNotDownloaded;
  private javax.swing.JCheckBox checkbox_showUnseen;
  private javax.swing.JCheckBox checkbox_useProxy;
  private com.googlecode.svalidators.formcomponents.SComboBox combo_primaryLang;
  private com.googlecode.svalidators.formcomponents.SComboBox combo_secondaryLang;
  private javax.swing.JComboBox combobox_dateFormat;
  private javax.swing.JComboBox combobox_debugMode;
  private com.googlecode.svalidators.formcomponents.SComboBox combobox_fonts;
  private javax.swing.JComboBox combobox_laf;
  private javax.swing.JCheckBox jCheckBox1;
  private javax.swing.JCheckBox jCheckBox2;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel15;
  private javax.swing.JLabel jLabel16;
  private javax.swing.JLabel jLabel17;
  private javax.swing.JLabel jLabel18;
  private javax.swing.JLabel jLabel19;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel20;
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
  private javax.swing.JLabel label_preview;
  private javax.swing.JPanel panel_DateFormatHelp;
  private javax.swing.JPanel panel_general;
  private javax.swing.JPanel panel_internet;
  private javax.swing.JPanel panel_nextepisodes;
  private javax.swing.JPanel panel_options;
  private javax.swing.JPanel panel_renaming;
  private javax.swing.JSpinner spinner_fontSize;
  private javax.swing.JSpinner spinner_maxNextEpisodes;
  private javax.swing.JTabbedPane tabbedPane_options;
  private com.googlecode.svalidators.formcomponents.STextField textfield_port;
  private com.googlecode.svalidators.formcomponents.STextField textfield_proxy;
  private com.googlecode.svalidators.formcomponents.STextField tf_episodeSep;
  private com.googlecode.svalidators.formcomponents.STextField tf_seasonSep;
  private com.googlecode.svalidators.formcomponents.STextField tf_titleSep;
  private org.jdesktop.beansbinding.BindingGroup bindingGroup;
  // End of variables declaration//GEN-END:variables

  /**
   * Parsing all the options panels for option components
   */
  private void getOptionsComponents() {
    parse(panel_nextepisodes);
    parse(panel_general);
    parse(panel_internet);
    parse(panel_renaming);
  }

  /**
   * Parsing a jpanel and gets components with a name diff than "noname"<br />
   * Then sets the option with the name of the component to the value of the componont
   * @param panel
   */
  private void parse(JPanel panel) {
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
    if (c instanceof JSpinner) {
      JSpinner spin = (JSpinner) c;
      return spin.getValue();
    }
    if (c instanceof JCheckBox) {
      JCheckBox check = (JCheckBox) c;
      return check.isSelected();
    }
    if (c instanceof JTextField) {
      JTextField text = (JTextField) c;
      return text.getText();
    }
    if (c instanceof JComboBox) {
      JComboBox combo = (JComboBox) c;
      // In some combos get the item instead of index
      String name = combo.getName();
      if (MyUsefulFunctions.isInArray(Options._COMBO_OPTIONS_, name)) {
        return String.valueOf(combo.getSelectedItem());
      }
      return combo.getSelectedIndex();
    }
    // Get color buttons
    if (c instanceof JButton) {
      JButton button = (JButton) c;
      return button.getBackground().getRed() + ", "
              + button.getBackground().getGreen() + ", "
              + button.getBackground().getBlue();
    }
    return "";
  }
}
