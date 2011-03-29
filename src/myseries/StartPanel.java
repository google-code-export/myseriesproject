/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * startPanel.java
 *
 * Created on 8 Δεκ 2008, 5:51:21 μμ
 */
package myseries;

import com.googlecode.svalidators.validators.RequiredValidator;
import database.CreateDatabase;
import database.DBConnection;
import database.Database;
import help.HelpWindow;
import java.awt.Color;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.LookAndFeel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myComponents.myGUI.MyDraggable;
import myComponents.myGUI.MyFont;
import myComponents.myGUI.MyImagePanel;
import tools.DesktopSupport;
import tools.LookAndFeels;
import tools.options.Options;
import tools.Skin;
import tools.download.torrents.AbstractTorrent;
import tools.download.torrents.TorrentConstants;
import tools.feeds.Feed;

/**
 * The start up panel
 * @author lordovol
 */
public class StartPanel extends MyDraggable {

  private final static long serialVersionUID = 45346793847632L;
  public MySeries m = null;
  DefaultComboBoxModel databasesModel = new DefaultComboBoxModel();
  // Dimension big = new Dimension(428, 210);
  // Dimension small = new Dimension(428, 160);
  // Dimension smaller = new Dimension(428, 140);
  public String dbName;
  private boolean createNewDB;

  /** Creates new form startPanel */
  public StartPanel() {
    initComponents();
    textbox_name.addValidator(new RequiredValidator());
    getRootPane().setDefaultButton(bt_ok);
    setIconImage(new javax.swing.ImageIcon(getClass().getResource("/images/subtitles.png")).getImage());
    databasesModel = new DefaultComboBoxModel(Database.getDatabases());
    combobox_databases.setModel(databasesModel);
    //progress.setVisible(false);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  /**
   * Cretates the start panel
   * @param m MySeries main form
   */
  StartPanel(MySeries m) {
    this.m = m;
    initComponents();
    textbox_name.addValidator(new RequiredValidator());
    getRootPane().setDefaultButton(bt_ok);
    setIconImage(new javax.swing.ImageIcon(getClass().getResource("/images/subtitles.png")).getImage());
    //progress.setVisible(false);
    label_title.setText("Create database");
    setLocationRelativeTo(null);
    setVisible(true);
  }

  /**
   * The start panel to create a database
   * @param m The myseries form
   * @param createNewDB Create a database or not
   */
  public StartPanel(MySeries m, boolean createNewDB) {
    this.m = m;
    this.createNewDB = createNewDB;
    initComponents();
    textbox_name.addValidator(new RequiredValidator());
    getRootPane().setDefaultButton(bt_ok);
    //progress.setVisible(false);
    label_title.setText("Create database");
    setLocationRelativeTo(null);
    panel_loadDatabase.setVisible(false);
    //  setSize(smaller);
    pack();
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

    panel = new javax.swing.JPanel();
    label_title = new javax.swing.JLabel();
    jPanel1 = new javax.swing.JPanel();
    panel_newDB = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    textbox_name = new com.googlecode.svalidators.formcomponents.STextField();
    panel_loadDatabase = new javax.swing.JPanel();
    combobox_databases = new javax.swing.JComboBox();
    jLabel2 = new javax.swing.JLabel();
    bt_cancel = new myComponents.myGUI.buttons.MyButtonCancel();
    bt_ok = new myComponents.myGUI.buttons.MyButtonOk();
    bt_help = new myComponents.myGUI.buttons.MyButtonHelp();

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

    panel.setBackground(Skin.getOuterColor());
    panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    label_title.setFont(label_title.getFont().deriveFont(label_title.getFont().getStyle() | java.awt.Font.BOLD, label_title.getFont().getSize()+4));
    label_title.setForeground(Skin.getTitleColor());
    label_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    label_title.setText("No Database!!!");

    jPanel1.setBackground(Skin.getInnerColor());
    jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    panel_newDB.setOpaque(false);

    jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD));
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel1.setText("Create new database:");

    javax.swing.GroupLayout panel_newDBLayout = new javax.swing.GroupLayout(panel_newDB);
    panel_newDB.setLayout(panel_newDBLayout);
    panel_newDBLayout.setHorizontalGroup(
      panel_newDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_newDBLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel1)
        .addGap(26, 26, 26)
        .addComponent(textbox_name, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
        .addContainerGap())
    );
    panel_newDBLayout.setVerticalGroup(
      panel_newDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_newDBLayout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(panel_newDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(textbox_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(25, 25, 25))
    );

    panel_loadDatabase.setOpaque(false);

    combobox_databases.setModel(databasesModel);
    combobox_databases.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        combobox_databasesActionPerformed(evt);
      }
    });

    jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() | java.awt.Font.BOLD));
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel2.setText("Load Database :");

    javax.swing.GroupLayout panel_loadDatabaseLayout = new javax.swing.GroupLayout(panel_loadDatabase);
    panel_loadDatabase.setLayout(panel_loadDatabaseLayout);
    panel_loadDatabaseLayout.setHorizontalGroup(
      panel_loadDatabaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_loadDatabaseLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(28, 28, 28)
        .addComponent(combobox_databases, 0, 159, Short.MAX_VALUE)
        .addGap(15, 15, 15))
    );
    panel_loadDatabaseLayout.setVerticalGroup(
      panel_loadDatabaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_loadDatabaseLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_loadDatabaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(combobox_databases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap())
    );

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
          .addComponent(panel_loadDatabase, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(panel_newDB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap(38, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addComponent(panel_loadDatabase, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(panel_newDB, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
        .addContainerGap())
    );

    bt_cancel.setText("");
    bt_cancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_cancelActionPerformed(evt);
      }
    });

    bt_ok.setText("");
    bt_ok.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_okActionPerformed(evt);
      }
    });

    bt_help.setText("");
    bt_help.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_helpActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
    panel.setLayout(panelLayout);
    panelLayout.setHorizontalGroup(
      panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
        .addComponent(label_title, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(bt_help, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(bt_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(bt_ok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
      .addGroup(panelLayout.createSequentialGroup()
        .addGap(8, 8, 8)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );
    panelLayout.setVerticalGroup(
      panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panelLayout.createSequentialGroup()
        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(label_title)
          .addComponent(bt_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(bt_help, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(bt_ok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  /**
   * Creates a new database (and populate it with demo data)
   * @param evt
   */
    private void combobox_databasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combobox_databasesActionPerformed
      if (combobox_databases.getSelectedIndex() > 0) {
        panel_newDB.setVisible(false);
        //setSize(small);
        pack();
      } else {
        panel_newDB.setVisible(true);
        //setSize(big);
        pack();
      }
    }//GEN-LAST:event_combobox_databasesActionPerformed

    private void bt_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_cancelActionPerformed
      if (m == null) {
        System.exit(0);
      } else {
        MySeries.glassPane.deactivate();
        dispose();
      }
    }//GEN-LAST:event_bt_cancelActionPerformed

    private void bt_helpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_helpActionPerformed
      new HelpWindow(HelpWindow.START_APPLICATION);
    }//GEN-LAST:event_bt_helpActionPerformed

    private void bt_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_okActionPerformed
      bt_ok.requestFocus();
      dbName = "";
      Boolean loadDemoData = false;
      //Load database
      if (combobox_databases.getSelectedIndex() > 0) {
        dbName = (String) combobox_databases.getSelectedItem();
        loadDemoData = false;
      } else {
        // Create database
        dbName = textbox_name.getText().trim();
        loadDemoData = false;
        createNewDB = true;
      }

      if (new RequiredValidator(dbName).validate()) {
        try {
          CreateDatabase d = new CreateDatabase(this, dbName, createNewDB);
          Thread t = new Thread(d);
          t.start();
          dispose();
        } catch (ClassNotFoundException ex) {
          MySeries.logger.log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
          MySeries.logger.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
          MySeries.logger.log(Level.SEVERE, null, ex);
        }
      } else {
        MySeries.logger.log(Level.WARNING, "The database name should not be empty");
        MyMessages.error("Empty name", "The database name should not be empty");
      }
    }//GEN-LAST:event_bt_okActionPerformed

  /**
   * Starts MySeries Application after getting the database to use and save the options file
   * @throws java.io.IOException
   * @throws java.lang.ClassNotFoundException
   * @throws java.sql.SQLException
   * @throws java.lang.InstantiationException
   * @throws java.lang.IllegalAccessException
   * @throws javax.swing.UnsupportedLookAndFeelException
   */
  public void startMySeries() throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
    Options.setOption(Options.DB_NAME, dbName);
    Options.save();
    MySeries.logger.log(Level.INFO, "MySerieS loading...");
    MySeries mySeries = new MySeries();
    dispose();
  }

  /**
   * Start the application
   * Gets options, and starts logging
   * Create Dirs and if datatbase exists start MySeries else prompts for creating a new DB
   * @param args
   */
  public static void main(String[] args) {
    String[] lafs;
    try {

      // Get options
      Options.getOptions();
      //Create the logger
      MySeries.createLogger();
      MySeries.logger.log(Level.INFO, "=========================");

      // Create the skin
      if (Options.toBoolean(Options.USE_SKIN)) {
        Skin skin = new Skin(Options.toColor(Options.SKIN_COLOR));
        Skin.applySkin();
      } else {
        // Set look and feel
        String laf = Options.toString(Options.LOOK_AND_FEEL);
        if (!laf.equals("")) {
          String className = LookAndFeels.getClassName(laf);
          if (className != null) {
            UIManager.setLookAndFeel(className);
          } else {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          }
        } else {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }

        Skin skin = new Skin();
        //Skin.applySkin();
//        UIManager.put("TabbedPane.contentAreaColor", Color.WHITE);
//        UIManager.put("TabbedPane.selected", Color.WHITE);
//        UIManager.put("TabbedPane.unselectedBackground", Color.GRAY);
//        UIManager.put("TabbedPane.foreground", Color.BLACK);

      }
      //Set Font
      MyFont.SetMyFont();

      //Check Desktop supported
      DesktopSupport ds = new DesktopSupport();

      ToolTipManager.sharedInstance().setDismissDelay(50000);

      //create dirs
      MySeries.logger.log(Level.INFO, "Checking directories");
      MyUsefulFunctions.checkDir(Options._USER_DIR_ + Database.PATH);
      MyUsefulFunctions.checkDir(Options._USER_DIR_ + MyImagePanel.SCREENSHOTS_PATH);
      MyUsefulFunctions.checkDir(Options._USER_DIR_ + TorrentConstants.TORRENTS_PATH);
      MyUsefulFunctions.checkDir(Options._USER_DIR_ + Feed.FEEDS_PATH);
      // Create the default db if not exists and create the conn, stmt

      if (Options.toString(Options.DB_NAME).equals("")
          || Options.toString(Options.DB_NAME).equals("null")
          || !DBConnection.databaseExists(Options.toString(Options.DB_NAME))) {
        StartPanel s = new StartPanel();
      } else {
        // Check if database is in the right format
        if (DBConnection.checkDatabase(Options.toString(Options.DB_NAME))) {
          MySeries.logger.log(Level.INFO, "MySerieS loading...");
          MySeries m = new MySeries();
        } else {
          MyMessages.error("Invalid Database", "The selected database seems to be invalid.\nPlease select another one or create a new one.");
          StartPanel s = new StartPanel();
        }
      }
    } catch (ClassNotFoundException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (UnsupportedLookAndFeelException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (FileNotFoundException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private myComponents.myGUI.buttons.MyButtonCancel bt_cancel;
  private myComponents.myGUI.buttons.MyButtonHelp bt_help;
  private myComponents.myGUI.buttons.MyButtonOk bt_ok;
  private javax.swing.JComboBox combobox_databases;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JLabel label_title;
  private javax.swing.JPanel panel;
  private javax.swing.JPanel panel_loadDatabase;
  private javax.swing.JPanel panel_newDB;
  public com.googlecode.svalidators.formcomponents.STextField textbox_name;
  // End of variables declaration//GEN-END:variables
}
