/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import database.SeriesRecord;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import myseriesproject.series.Quality;
import tools.download.subtitles.Subtitle;

/**
 *
 * @author lordovol
 */
public class MyQualityListRenderer extends JLabel implements ListCellRenderer {

  public MyQualityListRenderer() {
    setOpaque(true);
    setBorder(BorderFactory.createEmptyBorder());
    setHorizontalAlignment(LEFT);
    setPreferredSize(new Dimension(this.getWidth(), 20));
  }

  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    if (isSelected) {
      setBackground(list.getSelectionBackground());
      setForeground(list.getSelectionForeground());
    } else {
      setBackground(list.getBackground());
      setForeground(list.getForeground());
    }
    if (value instanceof Quality) {
      Quality quality = (Quality) value;
      setText(quality.getName());
      setIcon(quality.getIcon());
    } else {
      setText(value.toString());
    }

    return this;
  }
}
