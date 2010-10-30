/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import database.EpisodesRecord;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import myComponents.MyUsefulFunctions;
import myseries.episodes.Episodes;
import myseries.filters.Filters;
import tools.download.subtitles.Subtitle;
import tools.download.subtitles.SubtitleConstants;
import tools.languages.Language;

/**
 * The cell renderer for the tile field
 * @author lordovol
 */
public class MyTitleCellRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = 3456463456344572L;
  private final boolean filterTable;

  public MyTitleCellRenderer(boolean filterTable) {
    this.filterTable = filterTable;
  }



  @Override
  public Component getTableCellRendererComponent(JTable table,
          Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    Boolean downloaded;
    Language sub;
    if (value instanceof EpisodesRecord) {
      if(!filterTable){
      downloaded = (Boolean) table.getModel().getValueAt(row, Episodes.DOWNLOADED_COLUMN);
      Boolean seen = (Boolean) table.getModel().getValueAt(row, Episodes.SEEN_COLUMN);
      sub = (Language) table.getModel().getValueAt(row, Episodes.SUBS_COLUMN);
      String date = (String) table.getModel().getValueAt(row, Episodes.AIRED_COLUMN);
      this.setFont(MyUsefulFunctions.getCellFont(this.getFont(), downloaded, seen, sub));
      this.setForeground(MyUsefulFunctions.getCellColor(isSelected, seen, date, downloaded));
      //String size = MyUsefulFunctions.getVideoFileSize((EpisodesRecord)value);
      //String size = "";
      
      }else {
        downloaded = (Boolean) table.getModel().getValueAt(row, Filters.DOWNLOADED_COLUMN);
        sub = (Language) table.getModel().getValueAt(row, Filters.SUBS_COLUMN);
      }
      if (downloaded && !sub.getName().equals(SubtitleConstants.NONE) && MyUsefulFunctions.needRenaming((EpisodesRecord) value)) {
        setIcon(new ImageIcon(getClass().getResource("/images/rename_small.png")));
      } else {
        setIcon(null);
      }
      this.setText(value.toString());
    }

    return this;
  }
}
