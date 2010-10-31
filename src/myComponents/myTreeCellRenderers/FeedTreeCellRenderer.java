/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTreeCellRenderers;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 *
 * @author ΔΙΟΝΥΣΗΣ
 */
public class FeedTreeCellRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer {

  private static final long serialVersionUID = 534646765786987L;

  public FeedTreeCellRenderer() {
    setOpaque(true);
    setLeafIcon(new ImageIcon(getClass().getResource("/images/rss.png")));
    setClosedIcon(new ImageIcon(getClass().getResource("/images/rss_closed.png")));
    setOpenIcon(new ImageIcon(getClass().getResource("/images/rss_opened.png")));
  }

}
