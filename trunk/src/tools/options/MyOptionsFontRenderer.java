/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.options;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import tools.Skin;

/**
 *
 * @author ssoldatos
 */
public class MyOptionsFontRenderer extends JLabel implements ListCellRenderer {

  public MyOptionsFontRenderer() {
    setOpaque(true);
    setHorizontalAlignment(LEFT);
    setVerticalAlignment(CENTER);
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

    //Set the icon and text.  If icon was null, say so.

    String val = (String) list.getModel().getElementAt(index);
    setText(val);
    setFont(new Font(val, Font.PLAIN, 12));

    return this;
  }
}
