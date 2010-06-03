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

import database.CreateDatabase;
import database.DBConnection;
import database.Database;
import java.awt.Color;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myComponents.myGUI.MyDraggable;
import myComponents.myGUI.MyFont;
import myComponents.myGUI.MyImagePanel;
import tools.DesktopSupport;
import tools.options.Options;
import tools.Skin;
import tools.download.torrents.AbstractTorrent;
import tools.download.torrents.TorrentConstants;

/**
 * The start up panel
 * @author lordovol
 */
public class StartPanel extends MyDraggable {

  private final static long serialVersionUID = 45346793847632L;
  public MySeries m = null;
  DefaultComboBoxModel databasesModel = new DefaultComboBoxModel();
  Dimension big = new Dimension(428, 210);
  Dimension small = new Dimension(428, 160);
  Dimension smaller = new Dimension(428, 140);
  private String dbName;
  private boolean createNewDB;

  /** Creates new form startPanel */
  public StartPanel() {
    initComponents();
    getRootPane().setDefaultButton(button_create);
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
    getRootPane().setDefaultButton(button_create);
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
    getRootPane().setDefaultButton(button_create);
    //progress.setVisible(false);
    label_title.setText("Create database");
    setLocationRelativeTo(null);
    panel_loadDatabase.setVisible(false);
    setSize(smaller);
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
    panel_newDB = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    textbox_name = new javax.swing.JTextField();
    button_exit = new javax.swing.JButton();
    button_create = new javax.swing.JButton();
    label_title = new javax.swing.JLabel();
    label_wait = new javax.swing.JLabel();
    panel_loadDatabase = new javax.swing.JPanel();
    combobox_databases = new javax.swing.JComboBox();
    jLabel2 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

    panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    panel.setOpaque(false);

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
        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(textbox_name, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(85, Short.MAX_VALUE))
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

    button_exit.setText("Exit");
    button_exit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_exitActionPerformed(evt);
      }
    });

    button_create.setText("OK");
    button_create.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_createActionPerformed(evt);
      }
    });

    label_title.setFont(label_title.getFont().deriveFont(label_title.getFont().getStyle() | java.awt.Font.BOLD, label_title.getFont().getSize()+4));
    label_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    label_title.setText("No Database!!!");

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
        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(combobox_databases, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(90, Short.MAX_VALUE))
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

    javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
    panel.setLayout(panelLayout);
    panelLayout.setHorizontalGroup(
      panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panelLayout.createSequentialGroup()
            .addComponent(button_create, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(button_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(panelLayout.createSequentialGroup()
            .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(panel_newDB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(panel_loadDatabase, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(label_title, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)))
            .addGap(136, 136, 136)
            .addComponent(label_wait, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))))
    );
    panelLayout.setVerticalGroup(
      panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(label_title)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(panel_loadDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(label_wait, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(panel_newDB, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(button_exit)
          .addComponent(button_create))
        .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void button_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_exitActionPerformed
      if (m == null) {
        System.exit(0);
      } else {
        MySeries.glassPane.deactivate();
        dispose();
      }
    }//GEN-LAST:event_button_exitActionPerformed

  /**
   * Creates a new database (and populate it with demo data)
   * @param evt
   */
    private void button_createActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_createActionPerformed
      button_create.requestFocus();
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

      if (!dbName.equals("")) {
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
    }//GEN-LAST:event_button_createActionPerformed

    private void combobox_databasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combobox_databasesActionPerformed
      if (combobox_databases.getSelectedIndex() > 0) {
        panel_newDB.setVisible(false);
        setSize(small);
        button_create.setText("Load");
      } else {
        panel_newDB.setVisible(true);
        button_create.setText("Create");
        setSize(big);
      }
    }//GEN-LAST:event_combobox_databasesActionPerformed

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
      // Set look and feel
      //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
        Skin skin = new Skin();
        Skin.applySkin();
        UIManager.put("TabbedPane.contentAreaColor", Color.WHITE);
        UIManager.put("TabbedPane.selected", Color.WHITE);
        UIManager.put("TabbedPane.unselectedBackground", Color.GRAY);
        UIManager.put("TabbedPane.foreground", Color.BLACK);

      }
      //Set Font
      MyFont.SetMyFont();

      //Check Desktop supported
      DesktopSupport ds = new DesktopSupport();

      ToolTipManager.sharedInstance().setDismissDelay(50000);
      
      //create dirs
      MySeries.logger.log(Level.INFO, "Checking directories");
      MyUsefulFunctions.checkDir(Options._USER_DIR_ + "/" + Database.PATH);
      MyUsefulFunctions.checkDir(Options._USER_DIR_ + "/" + MyImagePanel.PATH);
      MyUsefulFunctions.checkDir(Options._USER_DIR_ + "/" + TorrentConstants.TORRENTS_PATH);
      // Create the default db if not exists and create the conn, stmt

      if (Options.toString(Options.DB_NAME).equals("") ||
          Options.toString(Options.DB_NAME).equals("null") ||
          !DBConnection.databaseExists(Options.toString(Options.DB_NAME))) {
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
  private javax.swing.JButton button_create;
  private javax.swing.JButton button_exit;
  private javax.swing.JComboBox combobox_databases;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel label_title;
  public javax.swing.JLabel label_wait;
  private javax.swing.JPanel panel;
  private javax.swing.JPanel panel_loadDatabase;
  private javax.swing.JPanel panel_newDB;
  public javax.swing.JTextField textbox_name;
  // End of variables declaration//GEN-END:variables

}
