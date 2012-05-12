/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseriesproject.statistics;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lordovol
 */
public abstract class AbstractStat extends JPanel {

  protected DefaultTableModel model;

  protected void clearModel() {
    setModel((DefaultTableModel) getTable().getModel());
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

  abstract JTable getTable();

  public abstract void setTextColor(Color color);
}
