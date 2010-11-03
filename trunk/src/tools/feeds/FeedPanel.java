/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PhotoPanel.java
 *
 * Created on 5 Σεπ 2010, 10:44:29 μμ
 */
package tools.feeds;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import myComponents.MyMessages;
import myComponents.myGUI.MyScrollableFlowPanel;
import tools.DesktopSupport;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class FeedPanel extends javax.swing.JPanel implements Runnable {

  public static final int FEED_WIDTH = 600;
  public static final int FEED_HEIGHT= 200;
  public static final long serialVersionUID = 235346345645L;
  public static final Color BORDER_HIGHLIGHT_COLOR = Color.BLACK;
  public static final Color BORDER_MEDIUM_COLOR = Color.LIGHT_GRAY;
  private MyScrollableFlowPanel feedPanel;
  private int index;
  private SyndEntryImpl entry;
  private URI uri;

  /** Creates new form PhotoPanel */
  public FeedPanel() {
    initComponents();
  }

  FeedPanel(MyScrollableFlowPanel feedPanel, SyndEntryImpl entry) {
    try {
      this.feedPanel = feedPanel;
      this.entry = entry;
      initComponents();
      uri = new URL(entry.getLink()).toURI();
    } catch (URISyntaxException ex) {
      bt_link.setVisible(false);
    } catch (MalformedURLException ex) {
      bt_link.setVisible(false);
    }
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    label_title = new javax.swing.JLabel();
    label_date = new javax.swing.JLabel();
    scroll = new javax.swing.JScrollPane();
    ep_content = new javax.swing.JEditorPane();
    bt_link = new javax.swing.JButton();

    setBackground(new java.awt.Color(255, 255, 255));
    setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    label_title.setBackground(new java.awt.Color(255, 255, 255));
    label_title.setFont(label_title.getFont().deriveFont(label_title.getFont().getStyle() | java.awt.Font.BOLD, label_title.getFont().getSize()+1));
    label_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    label_title.setText("jLabel1");
    label_title.setOpaque(true);

    label_date.setFont(label_date.getFont().deriveFont((label_date.getFont().getStyle() | java.awt.Font.ITALIC) | java.awt.Font.BOLD, label_date.getFont().getSize()-1));
    label_date.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    label_date.setText("jLabel1");

    scroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.setViewportView(ep_content);

    bt_link.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/weblink.png"))); // NOI18N
    bt_link.setToolTipText("Vist webpage");
    bt_link.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    bt_link.setBorderPainted(false);
    bt_link.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    bt_link.setIconTextGap(0);
    bt_link.setMargin(new java.awt.Insets(0, 0, 0, 0));
    bt_link.setOpaque(false);
    bt_link.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_linkActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(scroll, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
          .addComponent(label_date, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(label_title, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(bt_link, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(label_title)
          .addComponent(bt_link))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(label_date)
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents

  private void bt_linkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_linkActionPerformed
    if(DesktopSupport.isDesktopSupport()){
      try {
        DesktopSupport.getDesktop().browse(uri);
      } catch (IOException ex) {
        Logger.getLogger(FeedPanel.class.getName()).log(Level.SEVERE, null, ex);
      }
    }else {
      MyMessages.error("Visit Feed webpage", "Your OS doesn'yt support opening a browser window");
    }
  }//GEN-LAST:event_bt_linkActionPerformed

  public void selectFeed() {
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton bt_link;
  private javax.swing.JEditorPane ep_content;
  private javax.swing.JLabel label_date;
  private javax.swing.JLabel label_title;
  private javax.swing.JScrollPane scroll;
  // End of variables declaration//GEN-END:variables

  public void run() {
    label_title.setText(entry.getTitle());
    DateFormat df = new SimpleDateFormat(Options.toString(Options.DATE_FORMAT));
    if(entry.getPublishedDate()!= null){
      label_date.setText(df.format(entry.getPublishedDate()));
    } else if(entry.getUpdatedDate()!= null){
      label_date.setText(df.format(entry.getUpdatedDate()));
    } else {
      label_date.setText("");
    }
    ep_content.setContentType(entry.getDescription().getType());
    ep_content.setText(entry.getDescription().getValue());
    setPreferredSize(new Dimension(FEED_WIDTH,FEED_HEIGHT));
    setMaximumSize(new Dimension(FEED_WIDTH,FEED_HEIGHT));
    feedPanel.add(this);
    feedPanel.revalidate();
    feedPanel.repaint();

  }
}