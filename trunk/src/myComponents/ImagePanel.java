/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myComponents;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author ssoldatos
 */
public class ImagePanel extends JPanel {
  private static final long serialVersionUID = 356475743574387L;
  JLabel label = new JLabel("");

  public ImagePanel() {
    label.setIcon(new ImageIcon(getClass().getResource("../images/database.png")));
    add(label);
  }



  public void updatePosition(int yPos, int width){
     setBounds(2, yPos, width, 200 );
  }
}
