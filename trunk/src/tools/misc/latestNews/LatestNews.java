/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LatestNews.java
 *
 * Created on 26 Μαϊ 2011, 1:21:38 μμ
 */
package tools.misc.latestNews;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import myComponents.myGUI.MyDraggable;
import myseries.MySeries;
import tools.MySeriesLogger;
import tools.options.Options;

/**
 *
 * @author Spyros Soldatos
 */
public class LatestNews extends MyDraggable {

  private static final long serialVersionUID = 1346346363L;
  private MySeries m;
  private boolean onStartUp;
  private boolean isConected;
  private String LATESTNEWS_URL = "http://code.google.com/p/myseriesproject/wiki/LatestNews?ts=1306404297&updated=LatestNews";
  private ReadNews readNews;
  private int latestNewsId = 0;

  public LatestNews(MySeries m, boolean onStartUp) {
    this.m = m;
    this.onStartUp = onStartUp;
    if (!onStartUp) {
      MySeries.glassPane.activate(null);
    }
    MySeriesLogger.logger.log(Level.INFO, "Initializing components");
    initComponents();
    MySeriesLogger.logger.log(Level.FINE, "Components initialized");
    setLocationRelativeTo(null);

    if (check()) {
      readNews = new ReadNews();
      Thread t = new Thread(readNews);
      t.start();
      if (!onStartUp && !isVisible()) {
        setVisible(true);
      }
    } else {
      MySeries.glassPane.deactivate();
      return;
    }

  }

  class ReadNews implements Runnable {

    private int latestNewsViewed;

    @Override
    public void run() {
      try {
        MySeriesLogger.logger.log(Level.INFO, "Checking for updates");
        latestNewsViewed = Options.toInt(Options.LATEST_NEWS_ID);
        ArrayList<OnlineNew> news = getOnlineNews();

        if (!onStartUp || latestNewsViewed < latestNewsId) {
          showNews(news);
          if (!isVisible()) {
            MySeries.glassPane.activate(null);
            setVisible(true);
          }
          Options.setOption(Options.LATEST_NEWS_ID, latestNewsId);
          Options.save();
        }

      } catch (MalformedURLException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "Could not connect to server", ex);
      } catch (IOException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "Could not read from server", ex);
      }


    }

    private void showNews(ArrayList<OnlineNew> news) {
      MySeriesLogger.logger.log(Level.INFO, "Showing news");
      String n = "<html><head><title>Latest News</title>"
          + "<link rel=\"stylesheet\" href=\"styles.css\" type=\"text/css\" /></head><body><table>";

      for (Iterator<OnlineNew> it = news.iterator(); it.hasNext();) {
        OnlineNew on = it.next();
        if (!onStartUp || (onStartUp && on.id > Options.toInt(Options.LATEST_NEWS_ID))) {
          n += "<tr><th>" + on.date + " - " + on.title + "</th></tr>";
          n += "<tr><td>" + on.news + "</td></tr>";
        }
      }
      n += "</table></body></html>";
      newsPane.setText(n);
    }
  }

  private ArrayList<OnlineNew> getOnlineNews() throws MalformedURLException, IOException {
    ArrayList<OnlineNew> news = new ArrayList<OnlineNew>();
    MySeriesLogger.logger.log(Level.INFO, "Getting the latest news");
    URL v = new URL(LATESTNEWS_URL);
    BufferedReader in = new BufferedReader(new InputStreamReader(v.openStream()));
    progress.setIndeterminate(true);
    progress.setString("Reading news");
    MySeriesLogger.logger.log(Level.FINE, "Latest version found");
    String inputLine;
    while ((inputLine = in.readLine()) != null) {
      int pos = inputLine.indexOf("<div title=\"news_");
      if (pos > -1) {
        String[] tokens = inputLine.split("<div title=\"news_");
        for (int i = 1; i < tokens.length; i++) {
          OnlineNew n = new OnlineNew(tokens[i]);
          news.add(n);
        }

      }
    }
    MySeriesLogger.logger.log(Level.INFO, "{0} news found", news.size());
    progress.setIndeterminate(false);
    progress.setString(news.size() + " news were found");
    in.close();

    return news;
  }

  class OnlineNew {

    int id;
    String date;
    String title;
    String news;

    private OnlineNew(String inputLine) {
      inputLine = inputLine.replaceAll("<a.+?</a>", "");
      String[] t = inputLine.split("(\"> </p><h3>)|( : )|(</h3><p>)",4);
      id = Integer.parseInt(t[0]);
      if (id > latestNewsId) {
        latestNewsId = id;
      }
      date = t[1];
      title = t[2];
      news = t[3];
      MySeriesLogger.logger.log(Level.INFO, "News : {0}", title);
    }

    @Override
    public String toString() {
      return id + "." + date + " - " + title + " : " + news;
    }
  }

  private boolean check() {
    isConected = MyUsefulFunctions.hasInternetConnection(LATESTNEWS_URL);
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

    outer = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    bt_close = new myComponents.myGUI.buttons.MyButtonCancel();
    inner = new javax.swing.JPanel();
    progress = new javax.swing.JProgressBar();
    jScrollPane1 = new javax.swing.JScrollPane();
    newsPane = new javax.swing.JEditorPane();

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

    outer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, jLabel1.getFont().getSize()+2));
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("MySerieS Latest News");

    bt_close.setToolTipText("Close");
    bt_close.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_closeActionPerformed(evt);
      }
    });

    inner.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    progress.setString("Latest News");
    progress.setStringPainted(true);

    jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

    newsPane.setContentType("text/html");
    newsPane.setEditable(false);
    jScrollPane1.setViewportView(newsPane);

    javax.swing.GroupLayout innerLayout = new javax.swing.GroupLayout(inner);
    inner.setLayout(innerLayout);
    innerLayout.setHorizontalGroup(
      innerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(innerLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(innerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
          .addComponent(progress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE))
        .addContainerGap())
    );
    innerLayout.setVerticalGroup(
      innerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(innerLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
        .addContainerGap())
    );

    javax.swing.GroupLayout outerLayout = new javax.swing.GroupLayout(outer);
    outer.setLayout(outerLayout);
    outerLayout.setHorizontalGroup(
      outerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(outerLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(outerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, outerLayout.createSequentialGroup()
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(bt_close, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(outerLayout.createSequentialGroup()
            .addComponent(inner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())))
    );
    outerLayout.setVerticalGroup(
      outerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(outerLayout.createSequentialGroup()
        .addGroup(outerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(bt_close, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel1))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(inner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(outer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(outer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void bt_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_closeActionPerformed
    dispose();
    MySeries.glassPane.deactivate();
  }//GEN-LAST:event_bt_closeActionPerformed
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private myComponents.myGUI.buttons.MyButtonCancel bt_close;
  private javax.swing.JPanel inner;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JEditorPane newsPane;
  private javax.swing.JPanel outer;
  private javax.swing.JProgressBar progress;
  // End of variables declaration//GEN-END:variables
}
