/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.options;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

/**
 *
 * @author ssoldatos
 */
public class MyOptionsFontRenderer extends JLabel implements ListCellRenderer {

  public MyOptionsFontRenderer() {
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
    String val = (String) value;
    setText(val);
    setFont(new Font((String) value, Font.PLAIN, 12));

    return this;
  }
}
