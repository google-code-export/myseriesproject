/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import database.SeriesRecord;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import myseriesproject.series.Quality;

/**
 *
 * @author lordovol
 */
public class MySeriesTitleCellRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = 345646234236262L;

  @Override
  public Component getTableCellRendererComponent(JTable table,
          Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    if (isSelected) {
      setBackground(table.getSelectionBackground());
      setForeground(table.getSelectionForeground());
    } else {
      setBackground(table.getBackground());
      setForeground(table.getForeground());
    }
    if (value instanceof SeriesRecord) {
      SeriesRecord s = (SeriesRecord) value;
      setIcon(s.getQualityIcon());
      setToolTipText(s.getFullTitle() + " (Preferred video quality:"+Quality.QUALITIES_NAMES[s.getQuality()]+")");
      setText(s.getFullTitle());
    } else {
      setText(value.toString());
    }

    return this;

  }
}
