/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * IsohuntResults.java
 *
 * Created on 3 Ιουν 2010, 12:53:44 μμ
 */
package tools.download.torrents.isohunt;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;
import myComponents.MyUsefulFunctions;
import myComponents.myGUI.MyDraggable;
import myComponents.myTableCellRenderers.IsoHuntInfoRenderer;
import myComponents.myTableCellRenderers.IsoHuntLinkRenderer;
import myComponents.myTableCellRenderers.IsoHuntSizeRenderer;
import tools.download.torrents.AbstractTorrent;
import tools.download.torrents.TorrentConstants;

/**
 *
 * @author ssoldatos
 */
public class IsohuntResults extends MyDraggable implements TorrentConstants {
  private static final long serialVersionUID = 354646575474L;

  private IsohuntTorrent selectedTorrent;
  private ArrayList<AbstractTorrent> torrents;

  /** Creates new form IsohuntResults
   * @param torrents
   */
  public IsohuntResults(ArrayList<AbstractTorrent> torrents) {
    this.torrents = torrents;
    initComponents();
    createTable();
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
    jScrollPane1 = new javax.swing.JScrollPane();
    table = new javax.swing.JTable();
    button_cancel = new javax.swing.JButton();
    jLabel3 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setUndecorated(true);

    jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

    jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, jLabel1.getFont().getSize()+2));
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Isohunt search results");

    table.setAutoCreateRowSorter(true);
    table.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {
        "Title", "Files", "Size", "Seeds", "Leechers", "Download"
      }
    ) {
      Class[] types = new Class [] {
        java.lang.String.class, java.lang.Integer.class, java.lang.Long.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class
      };
      boolean[] canEdit = new boolean [] {
        false, false, false, false, false, false
      };

      public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
    table.setPreferredSize(new java.awt.Dimension(600, 300));
    table.setRequestFocusEnabled(false);
    table.setRowHeight(20);
    jScrollPane1.setViewportView(table);

    button_cancel.setText("Cancel");
    button_cancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        button_cancelActionPerformed(evt);
      }
    });

    jLabel3.setText("Mouse over the title for more info about the torrent");

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 797, Short.MAX_VALUE)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(10, 10, 10)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 787, Short.MAX_VALUE)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(button_cancel))
                .addGap(254, 254, 254)))))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel3)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(button_cancel)
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

  private void button_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_cancelActionPerformed
    dispose();
  }//GEN-LAST:event_button_cancelActionPerformed
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton button_cancel;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JTable table;
  // End of variables declaration//GEN-END:variables

  /**
   * @return the selectedTorrent
   */
  public IsohuntTorrent getSelectedTorrent() {
    return selectedTorrent;
  }

  private void createTable() {
    table.getColumnModel().getColumn(ISOHUNT_RESULTS_TITLE).setPreferredWidth(340);
    table.getColumnModel().getColumn(ISOHUNT_RESULTS_TITLE).setCellRenderer(new IsoHuntInfoRenderer());
    table.getColumnModel().getColumn(ISOHUNT_RESULTS_FILES).setPreferredWidth(40);
    table.getColumnModel().getColumn(ISOHUNT_RESULTS_LENGTH).setPreferredWidth(80);
    table.getColumnModel().getColumn(ISOHUNT_RESULTS_LENGTH).setCellRenderer(new IsoHuntSizeRenderer());
    table.getColumnModel().getColumn(ISOHUNT_RESULTS_SEEDS).setPreferredWidth(50);
    table.getColumnModel().getColumn(ISOHUNT_RESULTS_LEECHERS).setPreferredWidth(50);
    table.getColumnModel().getColumn(ISOHUNT_RESULTS_DOWNLOAD).setPreferredWidth(40);
    table.getColumnModel().getColumn(ISOHUNT_RESULTS_DOWNLOAD).setCellRenderer(new IsoHuntLinkRenderer());
    createModel();
  }

  private void createModel() {
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    for (Iterator<AbstractTorrent> it = torrents.iterator(); it.hasNext();) {
      IsohuntTorrent torrent = (IsohuntTorrent) it.next();
      model.addRow(new Object[]{
            torrent,
            torrent.getFiles(),
            torrent.getLength(),
            torrent.getSeeds(),
            torrent.getLeechers(),
            torrent
          });
    }

  }
}
