/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myComponents.myToolbar;

import java.awt.Cursor;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author ssoldatos
 */
public class ToolbarButton extends JButton implements ToolbarButtonActions {
  private static final long serialVersionUID = 9471034018190L;
  private int actionName = NONE;
  private String tooltip;
  public Point origin = new Point();
  public Point startPoint = new Point();

  public ToolbarButton() {
    this(-1,"","");
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }


  public ToolbarButton(int actionName,String tooltip, String image) {
    this.actionName = actionName;
    this.tooltip = tooltip;
    this.setToolTipText(tooltip);
    if(image.equals("")){
     // setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
    }else{
    this.setIcon(new ImageIcon(getClass().getResource("../../images/"+image)));
    }
    setCursor(new Cursor(Cursor.HAND_CURSOR));
    addActionListener(new ToolbarActionListener());
  }

  /**
   * @return the actionName
   */
  public int getActionName() {
    return actionName;
  }

  /**
   * @param actionName the actionName to set
   */
  public void setActionName(int actionName) {
    this.actionName = actionName;
  }

  @Override
  public String toString() {
    return tooltip;
  }



}
