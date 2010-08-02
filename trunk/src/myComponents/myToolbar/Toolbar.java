/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myToolbar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JToolBar;
import myseries.MySeries;
import tools.options.Options;

/**
 *
 * @author ssoldatos
 */
public class Toolbar extends AbstractToolbar {

  private static final long serialVersionUID = 32414141414L;
  private PopupMenu popup;
  public static boolean customize = false;
  public MySeries ms;

  public Toolbar() {
    super(new Integer[]{});
  }

  public Toolbar(MySeries ms, Integer[] visibleButtons) {
    super(visibleButtons);
    this.ms = ms;
    createButtons();
    addButtons();
    createPopUp();
    addMouseListener(new ToolbarMouseListener());

  }

  private void createPopUp() {
    popup = new PopupMenu();
    MenuItem customizeItem = new MenuItem("Customize toolbar");
    customizeItem.addActionListener(new PopupActionListener());
    popup.add(customizeItem);
    add(popup);
  }

  @Override
  protected void populateToolbar() {
    removeAll();
    visibleButtons = visibleButtons==null?Options.getDefaultToolbarButtons():visibleButtons;
    for (int i = 0; i < visibleButtons.length; i++) {
      Integer key = visibleButtons[i];
      if (buttons.containsKey(key)) {
        Component b = buttons.get(key);
        if (b instanceof ToolbarButton) {
          b.setPreferredSize(BUTTON_DIMENSION);
        } else {
          ToolbarSeperator s = (ToolbarSeperator) b;
          s.setPreferredSize(BUTTON_DIMENSION);
        }
        add(b);
      }
    }

    validate();
    repaint();
  }

  public Map<Integer, Component> getButtons() {
    return buttons;
  }

  public Integer[] getVisibleButtons() {
    return visibleButtons;
  }

  class ToolbarMouseListener extends MouseAdapter {

    @Override
    public void mouseReleased(MouseEvent e) {
      super.mouseReleased(e);
      if (e.getButton() == MouseEvent.BUTTON3) {
        popup.show((Component) e.getSource(), (int) e.getPoint().getX(), (int) e.getPoint().getY());
      }
    }
  }

  class PopupActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      ToolbarCustomize tc = new ToolbarCustomize(MySeries.myToolbar.getVisibleButtons());
      visibleButtons = tc.newVisibleButtons;
      Options.setOption(Options.TOOLBAR_BUTTONS, visibleButtons);
      Options.save();
      populateToolbar();
    }
  }
}
