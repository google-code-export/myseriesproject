/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import myComponents.MyUsefulFunctions;
import tools.download.torrents.isohunt.IsohuntTorrent;

/**
 *
 * @author ssoldatos
 */
public class IsoHuntInfoRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = 23543643523654L;

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    IsohuntTorrent torrent = (IsohuntTorrent) value;
    setText(MyUsefulFunctions.stripHTML(torrent.getTitle()));
    setToolTipText(torrent.getInfo());
    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
  }
}
