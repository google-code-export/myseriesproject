/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RestoreSeries.java
 *
 * Created on 14 Ιουν 2010, 1:26:09 μμ
 */
package myseries.series;

import database.SeriesRecord;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import myComponents.myEvents.MyEvent;
import myComponents.myEvents.MyEventHandler;
import myComponents.myGUI.MyDraggable;
import myseries.MySeries;

/**
 *
 * @author ssoldatos
 */
public class RestoreSeries extends MyDraggable {

  private final ArrayList<SeriesRecord> series;
  private MySeries m;

  /** Creates new form RestoreSeries */
  public RestoreSeries() {
    this(null,new ArrayList<SeriesRecord>());
  }

  public RestoreSeries(MySeries m, ArrayList<SeriesRecord> series) {
    initComponents();
    this.m = m;
    table.getColumnModel().getColumn(0).setPreferredWidth(400);
    table.getColumnModel().getColumn(1).setPreferredWidth(100);

    this.series = series;
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    for (Iterator<SeriesRecord> it = series.iterator(); it.hasNext();) {
      SeriesRecord seriesRecord = it.next();
      model.addRow(new Object[]{seriesRecord, false});
    }
    table.setModel(model);
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
    title = new javax.swing.JLabel();
    scroll = new javax.swing.JScrollPane();
    table = new javax.swing.JTable();
    restore = new javax.swing.JButton();
    cancel = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

    jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

    title.setFont(title.getFont().deriveFont(title.getFont().getStyle() | java.awt.Font.BOLD, title.getFont().getSize()+2));
    title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    title.setText("Restore Series");

    table.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {
        "Series", "Restore"
      }
    ) {
      Class[] types = new Class [] {
        java.lang.String.class, java.lang.Boolean.class
      };
      boolean[] canEdit = new boolean [] {
        false, true
      };

      public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    scroll.setViewportView(table);

    restore.setText("Restore");
    restore.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        restoreActionPerformed(evt);
      }
    });

    cancel.setText("Cancel");
    cancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(restore)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(cancel)))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(title)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(restore)
          .addComponent(cancel))
        .addContainerGap(17, Short.MAX_VALUE))
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

  private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
    dispose();
  }//GEN-LAST:event_cancelActionPerformed

  private void restoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restoreActionPerformed
    TableModel model = table.getModel();
    for (int i=0;i<model.getRowCount();i++){
      if((Boolean)model.getValueAt(i, 1)){
        try {
          SeriesRecord ser = (SeriesRecord) model.getValueAt(i, 0);
          ser.setDeleted(0);
          ser.save();
        } catch (SQLException ex) {
           myseries.MySeries.logger.log(Level.SEVERE, null, ex);
        }
      }
    }
    m.fireMyEvent(new MyEvent(m, MyEventHandler.SERIES_UPDATE));
    dispose();
  }//GEN-LAST:event_restoreActionPerformed
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton cancel;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JButton restore;
  private javax.swing.JScrollPane scroll;
  private javax.swing.JTable table;
  private javax.swing.JLabel title;
  // End of variables declaration//GEN-END:variables
}
