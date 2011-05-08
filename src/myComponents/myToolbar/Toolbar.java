/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myToolbar;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import myseries.MySeries;
import myseries.actions.ApplicationActions;
import tools.MySeriesLogger;
import tools.options.Options;

/**
 *
 * @author ssoldatos
 */
public class Toolbar extends AbstractToolbar {

  private static final long serialVersionUID = 32414141414L;
  private JPopupMenu popup;
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
    MySeriesLogger.logger.log(Level.INFO, "Creting the popup");
    popup = new JPopupMenu();
    JMenuItem customizeItem = new JMenuItem("Customize toolbar",
        new javax.swing.ImageIcon(getClass().getResource("/images/customizeToolbar.png")));
    customizeItem.addActionListener(new PopupActionListener());
    popup.add(customizeItem);
    add(popup);
  }

  @Override
  public void populateToolbar() {
    MySeriesLogger.logger.log(Level.INFO, "Populating the toolbar");
    removeAll();
    visibleButtons = visibleButtons == null ? Options.getDefaultToolbarButtons() : visibleButtons;
    for (int i = 0; i < visibleButtons.length; i++) {
      Integer key = visibleButtons[i];
      if (key != null && buttons.containsKey(key)) {
        Component b = buttons.get(key);
        if (b instanceof ToolbarButton) {
          ToolbarButton but = (ToolbarButton) b;
          if (!but.isDeprecated()) {
            but.setPreferredSize(BUTTON_DIMENSION);
            but.setMinimumSize(BUTTON_DIMENSION);
            but.setMaximumSize(BUTTON_DIMENSION);
            MySeriesLogger.logger.log(Level.FINE, "Adding button {0}", ((ToolbarButton) b).toString());
            add(but);
          }
        } else {
          ToolbarSeperator s = (ToolbarSeperator) b;
          s.setPreferredSize(BUTTON_DIMENSION);
          s.setMinimumSize(BUTTON_DIMENSION);
          s.setMaximumSize(BUTTON_DIMENSION);
          MySeriesLogger.logger.log(Level.FINE, "Adding Seperator");
          add(b);
        }
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

  public void removeButton(int id){
      for (int j = 0; j < visibleButtons.length; j++) {
          Integer  i = visibleButtons[j];
          if(i==id){
              visibleButtons[j]=null;
          }
      }
      
  }

  class ToolbarMouseListener extends MouseAdapter {

    @Override
    public void mouseReleased(MouseEvent e) {
      super.mouseReleased(e);
      if (e.getButton() == MouseEvent.BUTTON3) {
        MySeriesLogger.logger.log(Level.INFO, "Showing popup");
        popup.show((Component) e.getSource(), (int) e.getPoint().getX(), (int) e.getPoint().getY());
      }
    }
  }

  class PopupActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      ApplicationActions.customizeToolbar(ms);
    }
  }
}
