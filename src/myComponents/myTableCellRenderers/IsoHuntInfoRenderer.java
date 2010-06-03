/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myComponents.myTableCellRenderers;

import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import tools.download.torrents.isohunt.IsohuntTorrent;

/**
 *
 * @author ssoldatos
 */
  public class IsoHuntInfoRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 23543643523654L;

    private final DecimalFormat formatter = new DecimalFormat("#.00");

    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      IsohuntTorrent torrent = (IsohuntTorrent) value;
      setToolTipText(torrent.getInfo());
      
      return this;
    }
  }
