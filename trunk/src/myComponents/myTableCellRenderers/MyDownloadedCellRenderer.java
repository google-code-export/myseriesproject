/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myComponents.myTableCellRenderers;

import database.EpisodesRecord;
import java.awt.Component;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import myComponents.MyUsefulFunctions;
import myseries.episodes.Episodes;
import tools.download.torrents.isohunt.IsohuntTorrent;

/**
 *
 * @author ssoldatos
 */
public class MyDownloadedCellRenderer extends DefaultTableCellRenderer {
  private static final long serialVersionUID = 7897867559L;
  public static final String AVI = "avi";
  public static final String MKV = "mkv";
  public static final String OTHER = "other";
  public static final String NONE = "none";
  public static final int IMAGE_SIZE = 16;
  public static final int GAP = 10;

   @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
     super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
     if(value instanceof Boolean){
       Boolean val = (Boolean) value;
       setText("");
       if(val){
         EpisodesRecord ep = (EpisodesRecord) table.getValueAt(row, Episodes.EPISODERECORD_COLUMN);
         String[] types = MyUsefulFunctions.getVideoFileTypes(ep);
         if(types != null){
          setIcon(createIcon(types));
         } else{
           setIcon(createIcon(new String[]{OTHER}));
         }
       } else{
         setIcon(null);
       }
     }
    return this;
  }

  private Icon createIcon(String[] types) {
    if(types.length == 1 && types[0].equals(NONE)){
      return null;
    }
    BufferedImage buff = new BufferedImage(types.length*IMAGE_SIZE+types.length*GAP, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
      for (int i = 0; i < types.length; i++) {
      ImageIcon im = new ImageIcon(getClass().getResource("/images/" + types[i] + ".png"));
      buff.getGraphics().drawImage(im.getImage(), (i*IMAGE_SIZE)+(i*GAP), 0, this);
    }

    return new ImageIcon(buff);
  }
}
