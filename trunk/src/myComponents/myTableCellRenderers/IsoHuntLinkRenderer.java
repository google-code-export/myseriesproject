/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import myComponents.MyUsefulFunctions;
import tools.download.torrents.isohunt.IsohuntTorrent;

/**
 *
 * @author ssoldatos
 */
public class IsoHuntLinkRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = 23543643523654L;
  private final DecimalFormat formatter = new DecimalFormat("#.00");

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    setIcon(new ImageIcon(getClass().getResource("/images/torrent.png")));
    setText("");
    setHorizontalAlignment(SwingConstants.CENTER);
    addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent e) {
        
        super.mouseClicked(e);
      }

    });
    return super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
  }
}
