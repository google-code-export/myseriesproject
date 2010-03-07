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

import java.io.IOException;
import myComponents.MyUsefulFunctions;
import database.SeriesRecord;
import java.awt.Color;
import java.awt.Cursor;
import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import myComponents.MyDraggable;
import myseries.MySeries;
import tools.internetUpdate.tvrage.TrGetId;

/**
 * Administrate series
 * @author lordovol
 */
public class AdminSeries extends MyDraggable {

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
   * Creates the adminSeries form
   * @param m The main mySeries form
   * @throws java.sql.SQLException
   */
  AdminSeries(MySeries m) throws SQLException {
    this.m = m;
    initComponents();
    setLocationRelativeTo(m);

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
    if (seriesRecord != null) {
      spinner_season.setValue(seriesRecord.getSeason());
      textField_Serial.setText(seriesRecord.getTitle());
      label_Title.setText("Edit Series " + seriesRecord.getTitle() +
              " S" + MyUsefulFunctions.padLeft(seriesRecord.getSeason(), 2, "0"));
      textfield_link.setText(seriesRecord.getLink());
      textfield_tvRageID.setText(String.valueOf(seriesRecord.getTvrage_ID()));
      textfield_localDir.setText(seriesRecord.getLocalDir());
      button_Add.setText("Edit");
    } else {
      label_Title.setText("Add New Series");
    }
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

    jPanel1 = new javax.swing.JPanel();
    label_Title = new javax.swing.JLabel();
    button_Add = new javax.swing.JButton();
    button_Cancel = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();
    textField_Serial = new javax.swing.JTextField();
    jLabel2 = new javax.swing.JLabel();
    spinner_season = new javax.swing.JSpinner();
    jLabel3 = new javax.swing.JLabel();
    textfield_link = new javax.swing.JTextField();
    jLabel4 = new javax.swing.JLabel();
    textfield_tvRageID = new javax.swing.JTextField();
    button_getTvRageID = new javax.swing.JButton();
    label_localDir = new javax.swing.JLabel();
    textfield_localDir = new javax.swing.JTextField();
    button_browse = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    setBackground(new java.awt.Color(102, 102, 102));
    setResizable(false);

    jPanel1.setBackground(new java.awt.Color(255, 255, 255));
    jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
    jPanel1.setOpaque(false);

    label_Title.setFont(label_Title.getFont().deriveFont(label_Title.getFont().getStyle() | java.awt.Font.BOLD, label_Title.getFont().getSize()+2));
    label_Title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    label_Title.setText("Add A New Series");

    button_Add.setText("Add");
    button_Add.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_AddActionPerformed(evt);
      }
    });

    button_Cancel.setText("Cancel");
    button_Cancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_CancelActionPerformed(evt);
      }
    });

    jLabel1.setFont(jLabel1.getFont());
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel1.setText("Title:");

    jLabel2.setText("Season :");

    jLabel3.setText("Subs link :");

    jLabel4.setText("TvRage ID :");

    textfield_tvRageID.setText("0");

    button_getTvRageID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/internet_update_small.png"))); // NOI18N
    button_getTvRageID.setToolTipText("Get ID from TvRage");
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

    label_localDir.setText("Local Dir :");

    button_browse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/localDir.png"))); // NOI18N
    button_browse.setMaximumSize(new java.awt.Dimension(31, 25));
    button_browse.setMinimumSize(new java.awt.Dimension(31, 25));
    button_browse.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_browseActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(label_Title, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(button_Add, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_Cancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 279, Short.MAX_VALUE))))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(29, 29, 29)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel2)
              .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(label_localDir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(textfield_tvRageID, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_getTvRageID, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(spinner_season, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                  .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(textField_Serial, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                    .addComponent(textfield_link, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                      .addComponent(textfield_localDir, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                      .addComponent(button_browse, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                  .addGap(9, 9, 9))))))
        .addContainerGap())
    );

    jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {button_browse, button_getTvRageID});

    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(label_Title)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(textField_Serial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(spinner_season, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3)
          .addComponent(textfield_link, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(label_localDir)
          .addComponent(button_browse, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(textfield_localDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(textfield_tvRageID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel4)
          .addComponent(button_getTvRageID, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(button_Add)
          .addComponent(button_Cancel))
        .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void button_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_CancelActionPerformed
      MySeries.glassPane.deactivate();
      dispose();
    }//GEN-LAST:event_button_CancelActionPerformed

  /**
   * Adds the series in the database
   * @throws java.io.IOException
   */
  private void addSeries() {
    if (textField_Serial.getText().trim().equals("")) {
      MySeries.logger.log(Level.WARNING, "You must type the title");
      MyUsefulFunctions.error("No Title!!!", "You must type the title");
    } else if (String.valueOf(spinner_season.getValue()).trim().equals("")) {
      MySeries.logger.log(Level.WARNING, "You must type the season");
      MyUsefulFunctions.error("No Season!!!", "You must type the season");
    } else if (!MyUsefulFunctions.isLink(textfield_link.getText()) && !textfield_link.getText().trim().equals("")) {
      MySeries.logger.log(Level.WARNING, "The link is not in the right format");
      MyUsefulFunctions.error("Wrong link format", "The link is not in the right format");
    } else {
      try {
        int tvRageID = 0;
        try{
          tvRageID = Integer.parseInt(textfield_tvRageID.getText());
        } catch(NumberFormatException ex){

        }
        season = Integer.parseInt(String.valueOf(spinner_season.getValue()).trim());
        serial = textField_Serial.getText().trim();
        if (Series.getCurrentSerial().getSeries_ID() == 0) {
          seriesRecord = new SeriesRecord();
        }
        seriesRecord.setTitle(serial);
        seriesRecord.setSeason(season);
        seriesRecord.setLink(textfield_link.getText().trim());
        seriesRecord.setTvrage_ID(tvRageID);
        seriesRecord.setLocalDir(textfield_localDir.getText());
        try {
          seriesRecord.save();
          Series.getSeries();
          MySeries.glassPane.deactivate();
          dispose();
        } catch (SQLException ex) {
          MySeries.logger.log(Level.SEVERE, "SQL error occured", ex);
        }
      } catch (NumberFormatException ex) {
        MySeries.logger.log(Level.WARNING, "Season must be a number", ex);
        MyUsefulFunctions.error("Season not a number!!!", "Season must be a number");
      }
    }
  }

    private void button_AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_AddActionPerformed
      addSeries();
    }//GEN-LAST:event_button_AddActionPerformed

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

    private void button_browseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_browseActionPerformed
      JFileChooser fc = new JFileChooser();
      fc.setCurrentDirectory(new File(textfield_localDir.getText()));
      fc.setDialogTitle("Choose the Series directory");
      fc.setDialogType(JFileChooser.OPEN_DIALOG);
      fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      fc.showOpenDialog(this);
      File localDir = fc.getSelectedFile();
      textfield_localDir.setText(localDir.getAbsolutePath());
    }//GEN-LAST:event_button_browseActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton button_Add;
  private javax.swing.JButton button_Cancel;
  private javax.swing.JButton button_browse;
  private javax.swing.JButton button_getTvRageID;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JLabel label_Title;
  private javax.swing.JLabel label_localDir;
  private javax.swing.JSpinner spinner_season;
  private javax.swing.JTextField textField_Serial;
  private javax.swing.JTextField textfield_link;
  private javax.swing.JTextField textfield_localDir;
  public javax.swing.JTextField textfield_tvRageID;
  // End of variables declaration//GEN-END:variables

  
}
