/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myToolbar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import tools.Skin;

/**
 *
 * @author ssoldatos
 */
public class ToolbarButtonMouseListener extends MouseAdapter {

  private Container parent;
  private Border border;
  private int order;
  private Container grandpa;
  private final JPanel panelUsed;
  private final JPanel panelUnused;

  ToolbarButtonMouseListener(JPanel panelUsed, JPanel panelUnused) {
    this.panelUsed = panelUsed;
    this.panelUnused = panelUnused;

  }

  @Override
  public void mousePressed(MouseEvent e) {
    Component c = (Component) e.getSource();
    if (c instanceof ToolbarButton) {
      ToolbarButton t = (ToolbarButton) c;
      parent = t.getParent();
      order = getOrder(parent, t);
      grandpa = t.getTopLevelAncestor();
      t.origin.x = e.getX();
      t.origin.y = e.getY();
      t.startPoint.x = t.getX();
      t.startPoint.y = t.getY();
    } else {
      ToolbarSeperator t = (ToolbarSeperator) c;
      parent = t.getParent();
      order = getOrder(parent, t);
      grandpa = t.getTopLevelAncestor();
      t.origin.x = e.getX();
      t.origin.y = e.getY();
      t.startPoint.x = t.getX();
      t.startPoint.y = t.getY();
    }
  }



  @Override
  public void mouseEntered(MouseEvent e) {
    Component c = (Component) e.getSource();

    if (c instanceof ToolbarButton) {
      ToolbarButton t = (ToolbarButton) c;
      border = t.getBorder();
      t.setBorder(BorderFactory.createLineBorder(Color.RED));
      t.setCursor(new Cursor(Cursor.MOVE_CURSOR));
      t.setBackground(Skin.getSkinColor());
    } else {
      ToolbarSeperator t = (ToolbarSeperator) c;
      border = t.getBorder();
      t.setBorder(BorderFactory.createLineBorder(Color.RED));
      t.setCursor(new Cursor(Cursor.MOVE_CURSOR));
    }

  }

  @Override
  public void mouseExited(MouseEvent e) {
    Component c = (Component) e.getSource();
    if (c instanceof ToolbarButton) {
      ToolbarButton t = (ToolbarButton) c;
      t.setBorder(border);
      t.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      t.setBackground(Skin.getColor_1());
    } else {
      ToolbarSeperator t = (ToolbarSeperator) c;
      t.setBorder(border);
      t.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

  }

  @Override
  public void mouseReleased(MouseEvent e) {
    Component c = (Component) e.getSource();
   
    if(grandpa.getComponentCount()>1){
      grandpa.remove(0);
    }
    if(notMoved(c)){
//      parent.add(c, order);
//      parent.validate();
//      parent.repaint();
      return;
    }
    Container panelUnder = getPanelUnder(c);
    if (panelUnder == null || panelUnder == parent) {
      parent.add(c, getNewOrder((JPanel)parent, c));
      parent.validate();
      parent.repaint();
    } else {
      if (parent == panelUsed) {
        panelUnused.add(c,getNewOrder(panelUnused,c));
        panelUnused.validate();
        panelUnused.repaint();
      } else {
        panelUsed.add(c,getNewOrder(panelUsed,c));
        panelUsed.validate();
        panelUsed.repaint();
      }
    }
    grandpa.validate();
    grandpa.repaint();
  }

  private int getNewOrder(JPanel panel, Component c){
    Component[] comps = panel.getComponents();
    for (int i = 0; i < comps.length; i++) {
      Component underComp = comps[i];
      Rectangle underCompoRect = underComp.getBounds();
      Rectangle compRect= c.getBounds();
      if(underCompoRect.x > compRect.x){
        return i;
      }
    }
    return comps.length;
  }

  private int getOrder(Container parent, Component t) {
    Component[] comps = parent.getComponents();
    for (int i = 0; i < comps.length; i++) {
      Component component = comps[i];
      if (component == t) {
        return i;
      }
    }
    return -1;
  }

  private Container getPanelUnder(Component t) {
    if (panelUsed.getBounds().contains(t.getBounds())) {
      return panelUsed;
    }
    if (panelUnused.getBounds().contains(t.getBounds())) {
      return panelUnused;
    }
    return null;
  }

  private boolean notMoved(Component c) {
    Point loc = c.getLocation();
    if(c instanceof ToolbarButton){
      return ((ToolbarButton)c).startPoint == loc;
    }if(c instanceof ToolbarSeperator){
      return ((ToolbarSeperator)c).startPoint == loc;
    }else{
      return false;
    }
  }
}
