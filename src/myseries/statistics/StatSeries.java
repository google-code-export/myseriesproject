/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StatSeries.java
 *
 * Created on 28 Μαϊ 2010, 3:51:43 μμ
 */
package myseries.statistics;

import database.DBConnection;
import database.DBHelper;
import database.SeriesRecord;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import myComponents.myTableCellRenderers.MyDecimalFormatRenderer;
import tools.MySeriesLogger;
import tools.options.Options;

/**
 *
 * @author ssoldatos
 */
public class StatSeries extends javax.swing.JPanel {

    public static int SERIES_COLUMN = 0;
    public static int EPISODES_COLUMN = 1;
    public static int RATE_COLUMN = 2;
    private boolean unifiedSeries = false;
    private DefaultTableModel model;

    /** Creates new form StatSeries */
    public StatSeries() {
        super();
        initComponents();
        jScrollPane1.getViewport().setOpaque(false);
        model = (DefaultTableModel) table_stat_series.getModel();
        table_stat_series.getColumnModel().getColumn(RATE_COLUMN).setCellRenderer(new MyDecimalFormatRenderer());
        table_stat_series.getTableHeader().setReorderingAllowed(false);
        validate();
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

        jScrollPane1 = new javax.swing.JScrollPane();
        table_stat_series = new javax.swing.JTable();
        lb_title = new javax.swing.JLabel();
        lb_tip = new javax.swing.JLabel();
        cb_unified = new javax.swing.JCheckBox();

        setOpaque(false);

        jScrollPane1.setOpaque(false);

        table_stat_series.setAutoCreateRowSorter(true);
        table_stat_series.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Series", "Episodes", "Rate"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_stat_series.setName("seriesStats"); // NOI18N
        jScrollPane1.setViewportView(table_stat_series);

        lb_title.setFont(lb_title.getFont().deriveFont(lb_title.getFont().getStyle() | java.awt.Font.BOLD, lb_title.getFont().getSize()+2));
        lb_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_title.setText("Series Ratings");

        lb_tip.setText("Mouse over rate to see a list of the series episodes rates");

        cb_unified.setText("Unified series");
        cb_unified.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        cb_unified.setOpaque(false);
        cb_unified.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_unifiedActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
                    .add(lb_title, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(lb_tip, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                        .add(44, 44, 44)
                        .add(cb_unified, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 227, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(lb_title)
                .add(14, 14, 14)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lb_tip)
                    .add(cb_unified))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

  private void cb_unifiedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_unifiedActionPerformed
      if (!cb_unified.isSelected()) {
          refresh(false);
      } else {
          refresh(true);
      }
      unifiedSeries = cb_unified.isSelected();
      Options.setOption(Options.UNIFIED_SERIES, cb_unified.isSelected());
  }//GEN-LAST:event_cb_unifiedActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cb_unified;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_tip;
    private javax.swing.JLabel lb_title;
    private javax.swing.JTable table_stat_series;
    // End of variables declaration//GEN-END:variables

    public void refresh(boolean unified) {
        clearModel();
        try {
            Statement stmt = DBConnection.stmt;
            String sql;
            if (!unified) {
                sql = "SELECT series.series_ID AS series_ID, series.title AS series, sum(episodes.rate)/count(1) as rate, count(1) as episodes "
                        + "FROM series join episodes on series.series_ID = episodes.series_ID "
                        + "where episodes.rate > 0 group by series.series_ID order by rate desc";
            } else {
                sql = "SELECT series.series_ID AS series_ID, series.title AS series, sum(episodes.rate)/count(1) as rate, count(1) as episodes "
                        + "FROM series join episodes on series.series_ID = episodes.series_ID "
                        + "where episodes.rate > 0 group by series.title order by rate desc";
            }
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                SeriesRecord series = DBHelper.getSeriesByID(rs.getInt("series_ID"));
                int episodes = rs.getInt("episodes");
                double rate = rs.getDouble("rate");
                Object[] data = {unified ? series.getTitle() : series, episodes, rate};
                getModel().addRow(data);
            }
        } catch (SQLException ex) {
        } catch (NullPointerException ex) {
        }
    }

    private void clearModel() {
        setModel((DefaultTableModel) table_stat_series.getModel());
        getModel().getDataVector().removeAllElements();
        getModel().fireTableDataChanged();
    }

    /**
     * @return the model
     */
    public DefaultTableModel getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(DefaultTableModel model) {
        this.model = model;
    }

    /**
     * @return the isUnifiedSeries
     */
    public boolean isUnifiedSeries() {
        return unifiedSeries;
    }

    /**
     * @param isUnifiedSeries the isUnifiedSeries to set
     */
    public void setUnifiedSeries(boolean un) {
        this.unifiedSeries = un;
        cb_unified.setSelected(un);
        refresh(unifiedSeries);
    }

    public void setTextColor(Color color) {
        lb_title.setForeground(color);
        lb_tip.setForeground(color);
        cb_unified.setForeground(color);
    }
}
