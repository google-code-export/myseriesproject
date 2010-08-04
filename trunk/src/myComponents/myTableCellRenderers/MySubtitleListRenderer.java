/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import tools.languages.Language;

/**
 *
 * @author ssoldatos
 */
public class MySubtitleListRenderer extends JLabel implements ListCellRenderer {

  private static final long serialVersionUID = 24536457567L;

  public MySubtitleListRenderer() {
    setOpaque(true);
    setBorder(BorderFactory.createEmptyBorder());
    setHorizontalAlignment(LEFT);
    setPreferredSize(new Dimension(this.getWidth(), 20));
  }

  @Override
  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    if (isSelected) {
      setBackground(list.getSelectionBackground());
      setForeground(list.getSelectionForeground());
    } else {
      setBackground(list.getBackground());
      setForeground(list.getForeground());
    }
    if(value instanceof Language){

    Language lang = (Language) value;
    setText(lang.getName());
    setIcon(lang.getIcon());
    } else {
      setText(value.toString());
    }

    return this;
  }
}
