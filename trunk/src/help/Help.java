/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Help.java
 *
 * Created on 26 Δεκ 2008, 12:13:01 μμ
 */
package help;

import help.SearchHelp.Result;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.text.BadLocationException;
import myseries.MySeries;
import myComponents.MyUsefulFunctions;
import myComponents.myGUI.buttons.MyAbstractButton;
import org.jdom.Document;
import tools.MySeriesLogger;

/**
 * Help
 * @author ssoldatos
 */
public class Help extends JFrame {

  private MySeries m;
  private String selectedSection;
  private static final long serialVersionUID = 1245475878L;

  /** Creates new form Help
   * @param m
   */
  public Help(MySeries m) {
    this.m = m;
    MySeries.isHelp = true;
    //Create the links map
    Links.createLinksMap();
    MySeriesLogger.logger.log(Level.INFO, "Initializing components");
    initComponents();
    MySeriesLogger.logger.log(Level.FINE, "Components initialized");
    setIconImage(new javax.swing.ImageIcon(getClass().getResource("/images/help.png")).getImage());
    setLocationRelativeTo(m);
    tree_help.setSelectionPath(tree_help.getPathForRow(0));
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

    panel_help = new javax.swing.JPanel();
    splitPanel = new javax.swing.JSplitPane();
    panel_left = new javax.swing.JPanel();
    tree_panel = new javax.swing.JScrollPane();
    tree_help = new javax.swing.JTree();
    tf_search = new javax.swing.JTextField();
    bt_search = new myComponents.myGUI.buttons.MyDefaultButton(MyAbstractButton.SEARCH,"Search Help");
    panel_right = new javax.swing.JPanel();
    mainScrollPane = new ScrollablePanel();
    mainContent = new javax.swing.JEditorPane();
    jLabel1 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("MySerieS Help");
    setMinimumSize(new java.awt.Dimension(600, 400));
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        formWindowClosing(evt);
      }
    });

    panel_help.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    panel_help.setPreferredSize(new java.awt.Dimension(800, 546));

    splitPanel.setBorder(null);
    splitPanel.setDividerLocation(200);
    splitPanel.setMinimumSize(new java.awt.Dimension(600, 502));

    tree_panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    tree_panel.setMinimumSize(new java.awt.Dimension(180, 200));
    tree_panel.setPreferredSize(new java.awt.Dimension(180, 200));

    tree_help.setFont(tree_help.getFont().deriveFont(tree_help.getFont().getStyle() | java.awt.Font.BOLD));
    javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("MySerieS Help");
    javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Start Application");
    treeNode1.add(treeNode2);
    treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Main Window");
    javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Series Panel");
    javax.swing.tree.DefaultMutableTreeNode treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Series PopUp");
    treeNode3.add(treeNode4);
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Episodes Panel");
    treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Episodes PopUp");
    treeNode3.add(treeNode4);
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Filters Panel");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Ratings Panel");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Schedule Panel");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Feeds Panel");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Errors Panel");
    treeNode2.add(treeNode3);
    treeNode1.add(treeNode2);
    treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Administration");
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Series Administration");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Episodes Administration");
    treeNode2.add(treeNode3);
    treeNode1.add(treeNode2);
    treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Database");
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Create Database");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Load Database");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Save Database");
    treeNode2.add(treeNode3);
    treeNode1.add(treeNode2);
    treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Tools");
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Export Episodes");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Import Episodes");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Restore Series");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Download Torrent");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Internet Update");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Update Files");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Housekeeping");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Customize Toolbar");
    treeNode2.add(treeNode3);
    treeNode1.add(treeNode2);
    treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Options");
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("General Options");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Appearance Options");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Internet Options");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Renaming Options");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Performance Options");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Subtitles Options");
    treeNode2.add(treeNode3);
    treeNode1.add(treeNode2);
    treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("MySerieS Online");
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Google Code");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Contact/Report Bug");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Check for updates");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Latest News");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Donate");
    treeNode2.add(treeNode3);
    treeNode1.add(treeNode2);
    treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Help");
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("View Log File");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Show Errors Panel");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("About");
    treeNode2.add(treeNode3);
    treeNode1.add(treeNode2);
    tree_help.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
    tree_help.setMaximumSize(new java.awt.Dimension(300, 2000));
    tree_help.setMinimumSize(new java.awt.Dimension(140, 300));
    tree_help.setPreferredSize(new java.awt.Dimension(140, 300));
    tree_help.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tree_helpMouseClicked(evt);
      }
    });
    tree_help.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
      public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
        tree_helpValueChanged(evt);
      }
    });
    tree_panel.setViewportView(tree_help);

    bt_search.setToolTipText("Search");
    bt_search.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_searchActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout panel_leftLayout = new javax.swing.GroupLayout(panel_left);
    panel_left.setLayout(panel_leftLayout);
    panel_leftLayout.setHorizontalGroup(
      panel_leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_leftLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(tf_search, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(bt_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(11, 11, 11))
      .addGroup(panel_leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(panel_leftLayout.createSequentialGroup()
          .addContainerGap()
          .addComponent(tree_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addContainerGap()))
    );
    panel_leftLayout.setVerticalGroup(
      panel_leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_leftLayout.createSequentialGroup()
        .addGroup(panel_leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(bt_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(tf_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(473, Short.MAX_VALUE))
      .addGroup(panel_leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_leftLayout.createSequentialGroup()
          .addGap(37, 37, 37)
          .addComponent(tree_panel, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)))
    );

    splitPanel.setLeftComponent(panel_left);

    panel_right.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    panel_right.setOpaque(false);

    mainScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    mainScrollPane.setHorizontalScrollBar(null);

    mainContent.setContentType("text/html");
    mainContent.setEditable(false);
    mainContent.setText("\n");
    mainContent.setMaximumSize(new java.awt.Dimension(600, 600));
    mainContent.setMinimumSize(new java.awt.Dimension(600, 300));
    mainContent.setPreferredSize(new java.awt.Dimension(600, 400));
    mainContent.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        mainContentMouseClicked(evt);
      }
    });
    mainScrollPane.setViewportView(mainContent);

    javax.swing.GroupLayout panel_rightLayout = new javax.swing.GroupLayout(panel_right);
    panel_right.setLayout(panel_rightLayout);
    panel_rightLayout.setHorizontalGroup(
      panel_rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_rightLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(mainScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
        .addContainerGap())
    );
    panel_rightLayout.setVerticalGroup(
      panel_rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_rightLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(mainScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
        .addContainerGap())
    );

    splitPanel.setRightComponent(panel_right);

    jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, jLabel1.getFont().getSize()+2));
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("MySerieS Help");

    javax.swing.GroupLayout panel_helpLayout = new javax.swing.GroupLayout(panel_help);
    panel_help.setLayout(panel_helpLayout);
    panel_helpLayout.setHorizontalGroup(
      panel_helpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_helpLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panel_helpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(splitPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 885, Short.MAX_VALUE)
          .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 885, Short.MAX_VALUE))
        .addContainerGap())
    );
    panel_helpLayout.setVerticalGroup(
      panel_helpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panel_helpLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(splitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
        .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(panel_help, javax.swing.GroupLayout.DEFAULT_SIZE, 907, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(panel_help, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void tree_helpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tree_helpMouseClicked
      selectedSection = tree_help.getSelectionPath().getLastPathComponent().toString();
      followHyperLink(selectedSection);
    }//GEN-LAST:event_tree_helpMouseClicked

    private void mainContentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainContentMouseClicked
      try {
        followHyperLink();
      } catch (BadLocationException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "Wrong hyperlink", ex);
      } catch (IOException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "Could not find the help file", ex);
      }
    }//GEN-LAST:event_mainContentMouseClicked

    private void tree_helpValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_tree_helpValueChanged
      try {
        String sel = evt.getNewLeadSelectionPath().toString();
        sel = sel.split(",")[sel.split(",").length - 1].replaceAll("\\[", "").replaceAll("\\]", "");
        setMainContent(sel.trim());
      } catch (NullPointerException ex) {
        MySeriesLogger.logger.log(Level.WARNING, "Null pointer when selecting help tree ", ex);
      }
    }//GEN-LAST:event_tree_helpValueChanged

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
      MySeries.isHelp = false;
    }//GEN-LAST:event_formWindowClosing

    private void bt_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_searchActionPerformed
      String s = tf_search.getText();
      SearchHelp search = new SearchHelp(this, s);
      search.search();
      setMainContent(search.results);
    }//GEN-LAST:event_bt_searchActionPerformed

  private void followHyperLink() throws BadLocationException, IOException {
    MySeriesLogger.logger.log(Level.INFO, "Following link");
    int pos = mainContent.getCaretPosition();
    if (isLink(pos)) {
      String link = getLink(pos);
      MySeriesLogger.logger.log(Level.INFO, "Link is {0}", link);
      if (Links.links.get(link) != null) {
        setMainContent(Links.links.get(link));
      } else {
        setMainContent(link);
      }
    }

  }

  private void followHyperLink(String link) {
    MySeriesLogger.logger.log(Level.INFO, "Following link {0)", link);
    if (Links.links.get(link) != null) {
      setMainContent(Links.links.get(link));
    } else {
      setMainContent(link);
    }
  }

  private String getLink(int pos) throws BadLocationException {
    MySeriesLogger.logger.log(Level.INFO, "Getting link from position {0}", pos);
    String link = "";
    for (int i = pos; i > 2 && link.equals(""); i--) {
      if (mainContent.getText(i, 1).equals("[")) {
        link = mainContent.getText(i + 1, pos - i - 1);

      }
    }
    for (int i = pos; i < mainContent.getText().length() - 2; i++) {
      if (mainContent.getText(i, 1).equals("]")) {
        link += mainContent.getText(pos, i - pos);
        MySeriesLogger.logger.log(Level.FINE, "Found link: {0}", link);
        return link;
      }
    }
    MySeriesLogger.logger.log(Level.FINE, "Found link: {0}", link);
    return link;
  }

  private boolean isLink(int caretPosition) throws BadLocationException {
    MySeriesLogger.logger.log(Level.INFO, "Check if there's a link");
    for (int i = caretPosition; i > 0; i--) {
      if (mainContent.getText(i, 1).equals("]")) {
        MySeriesLogger.logger.log(Level.INFO, "No link found");
        return false;
      } else if (mainContent.getText(i, 1).equals("[")) {
        MySeriesLogger.logger.log(Level.FINE, "There's a link");
        return true;
      }
    }
    return false;
  }

  private void setMainContent(String section) {
    try {
      MySeriesLogger.logger.log(Level.INFO, "Setting the main content to section {0}", section);
      java.net.URL helpURL = Help.class.getResource("/help/html/" + section.toLowerCase().replaceAll(" ", "_") + ".html");
      MySeriesLogger.logger.log(Level.INFO, "Setting the content to help url : {0}", helpURL);
      mainContent.setPage(helpURL);
      MySeriesLogger.logger.log(Level.FINE, "Content succesfuly set");
      MySeriesLogger.logger.log(Level.INFO, "Setting caret position to the start of the document");
      mainContent.setCaretPosition(0);
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.INFO, "Could not set help content to file " + section, ex);
      MySeriesLogger.logger.log(Level.INFO, "Stting content to empty page", ex);
      mainContent.setText("");
      mainContent.setCaretPosition(0);
    }
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private myComponents.myGUI.buttons.MyDefaultButton bt_search;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JEditorPane mainContent;
  private help.ScrollablePanel mainScrollPane;
  private javax.swing.JPanel panel_help;
  private javax.swing.JPanel panel_left;
  private javax.swing.JPanel panel_right;
  private javax.swing.JSplitPane splitPanel;
  private javax.swing.JTextField tf_search;
  private javax.swing.JTree tree_help;
  private javax.swing.JScrollPane tree_panel;
  // End of variables declaration//GEN-END:variables

  private void setMainContent(ArrayList<Result> results) {
    String text = "<link rel=\"stylesheet\" href=\"styles.css\" type=\"text/css\" /><table class=\"mainTable\">";
    text +="<tr><th >MySeries Help</th></tr><tr><th><img src=\"../../images/logo.png\"><br></th></tr>";
    text += "<tr><th>Search Results</th></tr>";
    for (Iterator<Result> it = results.iterator(); it.hasNext();) {
      Result result = it.next();
      text += "<tr><td>"+result.link+" ("+result.text+")</td></tr>";
    }
    text += "</html>";   
    mainContent.setText(text);
  }
}
