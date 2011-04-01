/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RenameEpisodes.java
 *
 * Created on 21 Μαρ 2010, 10:25:47 πμ
 */
package tools.renaming;

import com.googlecode.svalidators.formcomponents.ValidationGroup;
import com.googlecode.svalidators.validators.RegularExpressionValidator;
import database.EpisodesRecord;
import database.SeriesRecord;
import help.HelpWindow;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.table.TableModel;
import myComponents.MyMessages;
import myComponents.MyTableModels.MyRenameEpisodesTableModel;
import myComponents.MyUsefulFunctions;
import myComponents.myGUI.MyDraggable;
import tools.Skin;
import tools.languages.LangsList;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class RenameEpisodes extends MyDraggable {

    /**
     * RenameEpisodes table id column : 0
     */
    public static final int ID_COLUMN = 0;
    /**
     * RenameEpisodes table original name column : 1
     */
    public static final int ORIGINAL_NAME_COLUMN = 1;
    /**
     * RenameEpisodes table new name column : 2
     */
    public static final int NEW_NAME_COLUMN = 2;
    /**
     * RenameEpisodes table edit column : 3
     */
    public static final int EDIT_COLUMN = 3;
    private static final long serialVersionUID = 35546363456L;
    private ArrayList<File> oldNames;
    private MyRenameEpisodesTableModel renameEpisodesModel = new MyRenameEpisodesTableModel();
    private ArrayList<EpisodesRecord> newNames;
    private SeriesRecord series;
    private boolean checkAll = false;
    private String sepRegex = "^[^/\\\\?%*:|\\\"<>\\.]*$";

    /**
     * Create rename episodes form
     * @param oldNames The oldnames arraylist
     * @param newNames The newnames arraylist
     * @param series The series record
     */
    public RenameEpisodes(ArrayList<File> oldNames,
            ArrayList<EpisodesRecord> newNames, SeriesRecord series) {
        myseries.MySeries.glassPane.activate(null);
        this.oldNames = oldNames;
        this.newNames = newNames;
        this.series = series;
        initComponents();
        textfield_episode.addValidator(new RegularExpressionValidator("", sepRegex, false, true));
        textfield_season.addValidator(new RegularExpressionValidator("", sepRegex, false, true));
        textfield_title.addValidator(new RegularExpressionValidator("", sepRegex, false, true));
        textfield_episode.setTrimValue(false);
        textfield_season.setTrimValue(false);
        textfield_title.setTrimValue(false);
        createTableModel();
        setLocationRelativeTo(null);
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

    jTextField4 = new javax.swing.JTextField();
    jPanel3 = new javax.swing.JPanel();
    jPanel1 = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    table_rename = new javax.swing.JTable();
    jPanel2 = new javax.swing.JPanel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    textfield_season = new com.googlecode.svalidators.formcomponents.STextField();
    textfield_episode = new com.googlecode.svalidators.formcomponents.STextField();
    textfield_title = new com.googlecode.svalidators.formcomponents.STextField();
    jLabel6 = new javax.swing.JLabel();
    checkBox_checkAll = new javax.swing.JCheckBox();
    bt_apply = new myComponents.myGUI.buttons.MyButtonApply();
    bt_save = new myComponents.myGUI.buttons.MyButtonSave();
    jLabel1 = new javax.swing.JLabel();
    bt_cancel = new myComponents.myGUI.buttons.MyButtonCancel();
    bt_ok = new myComponents.myGUI.buttons.MyButtonOk();
    bt_help = new myComponents.myGUI.buttons.MyButtonHelp();

    jTextField4.setText("jTextField4");

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

    jPanel3.setBackground(Skin.getOuterColor());
    jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    table_rename.setModel(renameEpisodesModel);
    jScrollPane1.setViewportView(table_rename);

    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel2.setText("Separators :");

    jLabel3.setText("Season");

    jLabel4.setText("Episode");

    jLabel5.setText("Title");

    textfield_season.setText(Options.toString(Options.SEASON_SEPARATOR, false));
    textfield_season.setName("Season separator"); // NOI18N
    textfield_season.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        textfield_seasonKeyReleased(evt);
      }
    });

    textfield_episode.setText(Options.toString(Options.EPISODE_SEPARATOR, false));
    textfield_episode.setName("Episode separator"); // NOI18N
    textfield_episode.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        textfield_episodeKeyReleased(evt);
      }
    });

    textfield_title.setText(Options.toString(Options.TITLE_SEPARATOR, false));
    textfield_title.setName("Title separator"); // NOI18N
    textfield_title.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        textfield_titleKeyReleased(evt);
      }
    });

    jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel6.setText("Check/Uncheck  all :");

    checkBox_checkAll.setOpaque(false);
    checkBox_checkAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        checkBox_checkAllActionPerformed(evt);
      }
    });

    bt_apply.setText("");
    bt_apply.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_applyActionPerformed(evt);
      }
    });

    bt_save.setText("");
    bt_save.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_saveActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
        .addGap(18, 18, 18)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(checkBox_checkAll)
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(textfield_season, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel3))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel4)
              .addComponent(textfield_episode, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel5)
              .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(textfield_title, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_apply, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_save, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        .addGap(287, 287, 287))
    );

    jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {textfield_episode, textfield_season, textfield_title});

    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
        .addContainerGap(27, Short.MAX_VALUE)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel4)
          .addComponent(jLabel5)
          .addComponent(jLabel3))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(textfield_season, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(textfield_episode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(textfield_title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(bt_apply, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(bt_save, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel2))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(checkBox_checkAll, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)))
    );

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
          .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
        .addContainerGap())
    );

    jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, jLabel1.getFont().getSize()+2));
    jLabel1.setForeground(Skin.getTitleColor());
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Renaming episodes");

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

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
              .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addContainerGap())
            .addComponent(bt_ok, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(bt_help, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(bt_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
    );
    jPanel3Layout.setVerticalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(bt_help, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(bt_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel1))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(bt_ok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private boolean validateValues() {
        ValidationGroup group = new ValidationGroup();
        group.addComponent(textfield_episode);
        group.addComponent(textfield_season);
        group.addComponent(textfield_title);
        if (group.validate()) {
            return true;
        } else {
            MyMessages.error("Renaming Episodes Form", group.getErrorMessage());
            return false;
        }
    }

  private void checkBox_checkAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox_checkAllActionPerformed
      checkAll = checkBox_checkAll.isSelected();
      createTableModel();
  }//GEN-LAST:event_checkBox_checkAllActionPerformed

  private void textfield_seasonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textfield_seasonKeyReleased
      bt_applyActionPerformed(null);
  }//GEN-LAST:event_textfield_seasonKeyReleased

  private void textfield_episodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textfield_episodeKeyReleased
      bt_applyActionPerformed(null);
  }//GEN-LAST:event_textfield_episodeKeyReleased

  private void textfield_titleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textfield_titleKeyReleased
      bt_applyActionPerformed(null);
  }//GEN-LAST:event_textfield_titleKeyReleased

  private void bt_helpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_helpActionPerformed
      new HelpWindow(HelpWindow.RENAMING);
  }//GEN-LAST:event_bt_helpActionPerformed

  private void bt_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_cancelActionPerformed
      myseries.MySeries.glassPane.deactivate();
      dispose();
  }//GEN-LAST:event_bt_cancelActionPerformed

  private void bt_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_okActionPerformed
      if (!validateValues()) {
          return;
      }
      TableModel model = table_rename.getModel();
      int renames = 0;
      int fails = 0;
      for (int i = 0; i < model.getRowCount(); i++) {
          if (Boolean.parseBoolean(String.valueOf(model.getValueAt(i, 3)))) {
              File oldFile = new File(series.getLocalDir() + "/" + model.getValueAt(i, 1));
              File newFile = new File(series.getLocalDir() + "/" + model.getValueAt(i, 2));
              myseries.MySeries.logger.log(Level.INFO, "Renaming :" + oldFile + " to " + newFile);
              if (oldFile.renameTo(newFile)) {
                  myseries.MySeries.logger.log(Level.INFO, "Renamed :" + oldFile + " to " + newFile);
                  renames++;
              } else {
                  myseries.MySeries.logger.log(Level.INFO, "Fail to rename :" + oldFile + " to " + newFile);
                  fails++;
              }
          }
      }
      dispose();
      MyMessages.message("Renaming episodes", renames + " episodes renamed and " + fails + " episodes failed");
      myseries.MySeries.glassPane.deactivate();
  }//GEN-LAST:event_bt_okActionPerformed

  private void bt_applyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_applyActionPerformed
      if (evt == null) {
          createTableModel();
      } else if (validateValues()) {
          createTableModel();
      }

  }//GEN-LAST:event_bt_applyActionPerformed

  private void bt_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_saveActionPerformed
      if (validateValues()) {
          Options.setOption(Options.SEASON_SEPARATOR, textfield_season.getText());
          Options.setOption(Options.EPISODE_SEPARATOR, textfield_episode.getText());
          Options.setOption(Options.TITLE_SEPARATOR, textfield_title.getText());
          Options.save();
          MyMessages.message("Saving options", "Options saved");
      }
  }//GEN-LAST:event_bt_saveActionPerformed
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private myComponents.myGUI.buttons.MyButtonApply bt_apply;
  private myComponents.myGUI.buttons.MyButtonCancel bt_cancel;
  private myComponents.myGUI.buttons.MyButtonHelp bt_help;
  private myComponents.myGUI.buttons.MyButtonOk bt_ok;
  private myComponents.myGUI.buttons.MyButtonSave bt_save;
  private javax.swing.JCheckBox checkBox_checkAll;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JTextField jTextField4;
  private javax.swing.JTable table_rename;
  private com.googlecode.svalidators.formcomponents.STextField textfield_episode;
  private com.googlecode.svalidators.formcomponents.STextField textfield_season;
  private com.googlecode.svalidators.formcomponents.STextField textfield_title;
  // End of variables declaration//GEN-END:variables

    private void createTableModel() {
        renameEpisodesModel = new MyRenameEpisodesTableModel();
        renameEpisodesModel.addColumn("ID");
        renameEpisodesModel.addColumn("Old Name");
        renameEpisodesModel.addColumn("New Name");
        renameEpisodesModel.addColumn("Rename");
        table_rename.setModel(renameEpisodesModel);
        table_rename.getColumn("ID").setPreferredWidth(30);
        table_rename.getColumn("ID").setMaxWidth(30);
        table_rename.getColumn("Old Name").setPreferredWidth(200);
        table_rename.getColumn("New Name").setPreferredWidth(200);
        table_rename.getColumn("Rename").setPreferredWidth(60);
        table_rename.getColumn("Rename").setMaxWidth(60);
        if (oldNames.size() != newNames.size()) {
            myseries.MySeries.logger.log(Level.WARNING, "An error occured, old names size != new names size");
            MyMessages.error("Error", "An error occured, old names size != new names size");
            dispose();
        }
        for (int i = 0; i < oldNames.size(); i++) {
            File oldFile = oldNames.get(i);
            File newFile = createFile(newNames.get(i), oldFile);
            if (oldFile.equals(newFile)) {
                Object[] data = {i, oldFile.getName(), newFile.getName(), false};
                renameEpisodesModel.addRow(data);
            } else {
                Object[] data = {i, oldFile.getName(), newFile.getName(), checkAll};
                renameEpisodesModel.addRow(data);
            }
        }
    }

    private File createFile(EpisodesRecord episode, File oldFile) {
        String oldName = oldFile.getName();
        String[] tokens = oldName.split("\\.", -1);
        String ext = tokens[tokens.length - 1];
        if (MyUsefulFunctions.isSubtitle(oldName)) {
            if (LangsList.isLanguageCode(tokens[tokens.length - 2])) {
                ext = tokens[tokens.length - 2] + "." + tokens[tokens.length - 1];
            }
        }

        String sample = "";
        if (oldFile.getName().indexOf("sample") > -1) {
            sample = "_sample";
        }

        String newFilename = series.getTitle() + textfield_season.getText()
                + MyUsefulFunctions.padLeft(series.getSeason(), 2, "0")
                + textfield_episode.getText()
                + MyUsefulFunctions.padLeft(episode.getEpisode(), 2, "0")
                + textfield_title.getText() + episode.getTitle();

        String newName = series.getLocalDir() + "/"
                + newFilename.replaceAll("[\\Q/\\?%*:|\"<>.;\\E]", "") + sample + "." + ext;
        return new File(newName);
    }
}
