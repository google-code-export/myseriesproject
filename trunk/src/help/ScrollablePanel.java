/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package help;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;

/**
 *
 * @author lordovol
 */
public class ScrollablePanel extends JScrollPane implements Scrollable {

  public static final long serialVersionUID = 3424242L;

  public ScrollablePanel() {
  }

  @Override
  public Dimension getPreferredScrollableViewportSize() {
    return new Dimension(600, 400);
  }

  @Override
  public int getScrollableUnitIncrement(
          Rectangle visibleRect, int orientation, int direction) {
    return 20;
  }

  @Override
  public int getScrollableBlockIncrement(
          Rectangle visibleRect, int orientation, int direction) {
    return 60;
  }

  @Override
  public boolean getScrollableTracksViewportWidth() {
    return true;
  }

  @Override
  public boolean getScrollableTracksViewportHeight() {
    return false;
  }
}
