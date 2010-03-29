/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EzTvForm.java
 *
 * Created on 27 Μαρ 2010, 2:59:57 μμ
 */
package tools.download.torrents;

import database.EpisodesRecord;
import database.SeriesRecord;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import myComponents.MyDraggable;
import myComponents.MyUsefulFunctions;
import myseries.series.Series;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class EzTvForm extends MyDraggable {

  private SeriesRecord series = null;
  private EpisodesRecord episode = null;
  private ComboBoxModel seriesModel = new DefaultComboBoxModel();
  private ComboBoxModel qualityModel = new DefaultComboBoxModel();

  {
    createModels();
  }

  /** Creates new form EzTvForm */
  public EzTvForm() {
    showUp();
  }

  public EzTvForm(SeriesRecord series, EpisodesRecord episode) {
    myseries.MySeries.glassPane.activate(null);
    this.series = series;
    this.episode = episode;
    showUp();

  }

  private void showUp() {
    initComponents();
    setData();
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

    jPanel1 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    combo_series = new javax.swing.JComboBox();
    spinner_season = new javax.swing.JSpinner();
    spinner_episode = new javax.swing.JSpinner();
    combo_quality = new javax.swing.JComboBox();
    button_search = new javax.swing.JButton();
    button_cancel = new javax.swing.JButton();
    progress = new javax.swing.JProgressBar();
    jLabel6 = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    checkbox_exact = new javax.swing.JCheckBox();

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

    jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, jLabel1.getFont().getSize()+2));
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Download torrent from EzTv");

    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel2.setText("Series name:");

    jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel3.setText("Season :");

    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel4.setText("Episode :");

    jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel5.setText("Quality :");

    combo_series.setEditable(true);
    combo_series.setModel(seriesModel);

    spinner_season.setModel(new javax.swing.SpinnerNumberModel(1, 0, 50, 1));

    spinner_episode.setModel(new javax.swing.SpinnerNumberModel(1, 0, 60, 1));

    combo_quality.setModel(qualityModel);

    button_search.setText("Search");
    button_search.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_searchActionPerformed(evt);
      }
    });

    button_cancel.setText("Cancel");
    button_cancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_cancelActionPerformed(evt);
      }
    });

    progress.setString("");
    progress.setStringPainted(true);

    jLabel6.setText("0 : Any season");

    jLabel7.setText("0: Any Episode");

    checkbox_exact.setText("Exact name");

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
              .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
              .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
              .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(combo_quality, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(spinner_season, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(spinner_episode, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jLabel6))
                .addGap(242, 242, 242))
              .addComponent(checkbox_exact)
              .addComponent(combo_series, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(button_search)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(button_cancel))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE))
          .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap())
    );

    jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel2, jLabel3, jLabel4, jLabel5});

    jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel6, jLabel7});

    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(combo_series, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(checkbox_exact)
        .addGap(1, 1, 1)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3)
          .addComponent(spinner_season, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel6))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel4)
          .addComponent(jLabel7)
          .addComponent(spinner_episode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel5)
          .addComponent(combo_quality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(button_search, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(button_cancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void button_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_cancelActionPerformed
    myseries.MySeries.glassPane.deactivate();
    dispose();
  }//GEN-LAST:event_button_cancelActionPerformed

  private void button_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_searchActionPerformed
      
    URI uri = createUri();
    if(uri != null){
      EzTv e = new EzTv(uri, this);
      Thread t = new Thread(e);
      t.start();
    }
    myseries.MySeries.glassPane.deactivate();
    //dispose();

  }//GEN-LAST:event_button_searchActionPerformed
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton button_cancel;
  private javax.swing.JButton button_search;
  private javax.swing.JCheckBox checkbox_exact;
  private javax.swing.JComboBox combo_quality;
  private javax.swing.JComboBox combo_series;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JPanel jPanel1;
  public javax.swing.JProgressBar progress;
  private javax.swing.JSpinner spinner_episode;
  private javax.swing.JSpinner spinner_season;
  // End of variables declaration//GEN-END:variables

  private void createModels() {
    Vector<String> v = new Vector<String>();
    try {
      ArrayList<SeriesRecord> s = Series.getSeries();
      for (Iterator<SeriesRecord> it = s.iterator(); it.hasNext();) {
        SeriesRecord seriesRecord = it.next();
        v.add(seriesRecord.getTitle());
      }
      seriesModel = new DefaultComboBoxModel(v);
      qualityModel = new DefaultComboBoxModel(Options.QUALITIES);
    } catch (SQLException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
    }
  }

  private void setData() {
    if (series != null) {
      combo_series.setSelectedItem(series.getTitle());
      spinner_season.setValue(series.getSeason());
    }
    if (episode != null) {
      spinner_episode.setValue(episode.getEpisode());
    }
  }

  private URI createUri() {
    String address = "http://ezrss.it/search/index.php?";
    String query = "";
    ArrayList<String> q = new ArrayList<String>();
    try {
      q.add("show_name=" + URLEncoder.encode(String.valueOf(combo_series.getSelectedItem()), "UTF-8"));
      q.add("date=");
      q.add("show_name_exact=" + (checkbox_exact.isSelected() ? "true":""));
      q.add("quality=" + URLEncoder.encode(String.valueOf(combo_quality.getSelectedItem()), "UTF-8"));
      q.add("release_group=");
      q.add("episode_title=");
      q.add("season=" + spinner_season.getValue());
      q.add("episode=" + spinner_episode.getValue());
      q.add("video_format=");
      q.add("audio_format=");
      q.add("modifier=");
      q.add("mode=rss");

      query = MyUsefulFunctions.join(q, "&");
      return new URI(address + query);
    } catch (URISyntaxException ex) {
      MyUsefulFunctions.message("Wrong url", "Wrong url " + address + query);
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
      return null;
    } catch (UnsupportedEncodingException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
      return null;
    } catch (IOException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
      return null;
    }
  }
}
