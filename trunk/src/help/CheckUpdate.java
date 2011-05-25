/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CheckUpdate.java
 *
 * Created on 23 Ιαν 2009, 10:50:03 πμ
 */
package help;

import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myComponents.myGUI.MyDraggable;
import myComponents.myGUI.buttons.MyButtonCancel;
import myseries.*;
import tools.DesktopSupport;
import tools.Skin;
import tools.MySeriesLogger;
/**
 * Checking for updates of the application
 * @author ssoldatos
 */
public class CheckUpdate extends MyDraggable {

  private boolean isConected;
  private Update up;
  private static final long serialVersionUID = 34646758678L;
  private boolean onStartUp;
  /** The url to project on google code : "http://code.google.com/p/myseriesproject/" **/
  public static final String MYSERIESPROJECT_URL = "http://code.google.com/p/myseriesproject/";
  /** The url of the downloads list : "http://code.google.com/p/myseriesproject/downloads/list" **/
  public static final String MYSERIESPROJECT_DOWNLOAD_LIST = "http://code.google.com/p/myseriesproject/downloads/list";

  /** Creates new form CheckUpdate
   * @param onStartUp 
   */
  public CheckUpdate(boolean onStartUp) {
    MySeriesLogger.logger.log(Level.INFO, "Initializing components");
    initComponents();
    MySeriesLogger.logger.log(Level.FINE, "Components initialized");
    this.onStartUp = onStartUp;
    if (check()) {
      up = new Update();
      Thread t = new Thread(up);
      t.start();
    } else {
      MySeries.glassPane.deactivate();
      return;
    }
    setLocationRelativeTo(null);
    if (!onStartUp) {
      setVisible(true);
    }
  }

  class Update implements Runnable {

    protected String latestVersion;
    private String currentVersion;
    private String latestVersionUri;

    public void run() {
      try {
        MySeriesLogger.logger.log(Level.INFO, "Checking for updates");
        latestVersion = getLatestVersion();
        if (latestVersion != null) {
          currentVersion = MySeries.version;
          label_latestVersion.setText(latestVersion);
          if (currentVersion.compareTo(latestVersion) < 0) {
            MySeriesLogger.logger.log(Level.INFO, "Updated version is found!!!");
            label_needUpdate.setText("An update is found");
            label_needUpdate1.setText("Click here to go to the download page.");
            if (onStartUp) {
              setVisible(true);
            }
          } else {
            label_needUpdate.setText("You are up to date!!!");
            MySeriesLogger.logger.log(Level.INFO, "You are up to date!!!");
          }
        } else {
          MySeriesLogger.logger.log(Level.WARNING, "Can't get the update data");
          label_needUpdate.setText("Can't get the update data!!!");
        }
      } catch (MalformedURLException ex) {
        MySeriesLogger.logger.log(Level.WARNING, null, ex);
        label_needUpdate.setText("Can't connect to server!!!");
      } catch (IOException ex) {
        MySeriesLogger.logger.log(Level.WARNING, null, ex);
        label_needUpdate.setText("Can't connect to server!!!");
      }
    }

    private String getLatestVersion() throws MalformedURLException, IOException {
      MySeriesLogger.logger.log(Level.INFO, "Getting the latest version");
      URL v = new URL(MYSERIESPROJECT_URL);
      BufferedReader in = new BufferedReader(new InputStreamReader(v.openStream()));
      label_needUpdate.setText("Connected to server!!!");
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        inputLine = " Current version : 1.5(<a href=\"/p/myseriesproject/source/detail?r=573\">r808 - beta</a>) - PLEASE SEE readme.txt </div> </p>";
        int pos = inputLine.indexOf("Current version :");
        if (pos > -1) {
          inputLine = inputLine.substring(pos + 17);
          inputLine = MyUsefulFunctions.stripHTML(inputLine).trim();
          String uri = inputLine.replaceAll("[\\(\\)]", "").replaceAll("rev", "r");
          latestVersionUri = MYSERIESPROJECT_DOWNLOAD_LIST;
          in.close();
          progressBar_checkUpdates.setIndeterminate(false);
          progressBar_checkUpdates.setString("");
          MySeriesLogger.logger.log(Level.FINE, "Latest version found");
          return inputLine;
        }
      }
      in.close();
      return null;
    }
  }

  private boolean check() {
    isConected = MyUsefulFunctions.hasInternetConnection(MYSERIESPROJECT_URL);
    if (!isConected) {
      MyMessages.internetError(!onStartUp);
      return false;
    }
    return true;
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
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    label_currentVersion = new javax.swing.JLabel();
    label_latestVersion = new javax.swing.JLabel();
    progressBar_checkUpdates = new javax.swing.JProgressBar();
    jPanel3 = new javax.swing.JPanel();
    label_needUpdate = new javax.swing.JLabel();
    label_needUpdate1 = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();
    bt_exit = new myComponents.myGUI.buttons.MyButtonCancel();

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    setTitle("Check for  updates");

    main_panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    inner_panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    inner_panel.setPreferredSize(new java.awt.Dimension(300, 196));

    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel2.setText("Current version :");

    jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel3.setText("Latest version :");

    label_currentVersion.setText(MySeries.version);

    label_latestVersion.setText(" ");

    progressBar_checkUpdates.setIndeterminate(true);
    progressBar_checkUpdates.setString("Checking for updates");
    progressBar_checkUpdates.setStringPainted(true);

    jPanel3.setBackground(Skin.getInnerColor());
    jPanel3.setOpaque(false);

    label_needUpdate.setText(" ");

    label_needUpdate1.setFont(label_needUpdate1.getFont().deriveFont(label_needUpdate1.getFont().getStyle() | java.awt.Font.BOLD, label_needUpdate1.getFont().getSize()+1));
    label_needUpdate1.setText(" ");
    label_needUpdate1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        label_needUpdate1MouseEntered(evt);
      }
      public void mouseExited(java.awt.event.MouseEvent evt) {
        label_needUpdate1MouseExited(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        label_needUpdate1MouseReleased(evt);
      }
    });

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(label_needUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addComponent(label_needUpdate1, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
            .addContainerGap())))
    );
    jPanel3Layout.setVerticalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addComponent(label_needUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(label_needUpdate1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    javax.swing.GroupLayout inner_panelLayout = new javax.swing.GroupLayout(inner_panel);
    inner_panel.setLayout(inner_panelLayout);
    inner_panelLayout.setHorizontalGroup(
      inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(inner_panelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(inner_panelLayout.createSequentialGroup()
            .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(label_currentVersion, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
              .addComponent(label_latestVersion, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)))
          .addComponent(progressBar_checkUpdates, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
          .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    inner_panelLayout.setVerticalGroup(
      inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(inner_panelLayout.createSequentialGroup()
        .addGap(33, 33, 33)
        .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(label_currentVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3)
          .addComponent(label_latestVersion))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(progressBar_checkUpdates, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(62, 62, 62))
    );

    jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, jLabel1.getFont().getSize()+2));
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("MySeries - Checking for updates");
    jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

    bt_exit.setToolTipText("Close");
    bt_exit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_exitActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout main_panelLayout = new javax.swing.GroupLayout(main_panel);
    main_panel.setLayout(main_panelLayout);
    main_panelLayout.setHorizontalGroup(
      main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(main_panelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(main_panelLayout.createSequentialGroup()
            .addComponent(inner_panel, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
            .addContainerGap())
          .addGroup(main_panelLayout.createSequentialGroup()
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(bt_exit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
    );
    main_panelLayout.setVerticalGroup(
      main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(main_panelLayout.createSequentialGroup()
        .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabel1)
          .addComponent(bt_exit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(inner_panel, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(main_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(main_panel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void downloadUpdate() {
    try {
      MySeriesLogger.logger.log(Level.INFO, "Downloading update");
      URI download = new URI(up.latestVersionUri);
      if (DesktopSupport.isBrowseSupport()) {
        MyUsefulFunctions.browse(download);
      } else {
        MyMessages.warning("Browsing not supported", "Opening a browser window is not supported in your OS", true);
        MySeriesLogger.logger.log(Level.WARNING, "Opening a browser window is not supported in your OS");
      }
    } catch (URISyntaxException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Wrong URL address", ex);
    }
  }
    private void label_needUpdate1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_needUpdate1MouseEntered
      this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_label_needUpdate1MouseEntered

    private void label_needUpdate1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_needUpdate1MouseExited
      this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_label_needUpdate1MouseExited

    private void label_needUpdate1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_needUpdate1MouseReleased
      downloadUpdate();
    }//GEN-LAST:event_label_needUpdate1MouseReleased

    private void bt_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_exitActionPerformed
        dispose();
        MySeries.glassPane.deactivate();
    }//GEN-LAST:event_bt_exitActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private myComponents.myGUI.buttons.MyButtonCancel bt_exit;
  private javax.swing.JPanel inner_panel;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JLabel label_currentVersion;
  private javax.swing.JLabel label_latestVersion;
  private javax.swing.JLabel label_needUpdate;
  private javax.swing.JLabel label_needUpdate1;
  private javax.swing.JPanel main_panel;
  private javax.swing.JProgressBar progressBar_checkUpdates;
  // End of variables declaration//GEN-END:variables
}
