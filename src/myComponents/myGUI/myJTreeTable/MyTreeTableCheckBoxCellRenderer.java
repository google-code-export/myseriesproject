/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myGUI.myJTreeTable;

import database.SeriesRecord;
import myComponents.myTableCellRenderers.*;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import myseriesproject.series.Series;

/**
 *
 * @author ssoldatos
 */
public class MyTreeTableCheckBoxCellRenderer extends MyCheckBoxCellRenderer {

  private static final long serialVersionUID = 343346363643L;
  

 
  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    JTreeTable treetable = (JTreeTable) table;
    SeriesRecord s = (SeriesRecord) treetable.getModel().getValueAt(row, Series.SERIESRECORD_COLUMN);
    if(s.getSeries_ID()==0){
     this.setVisible(false);
     setIcon(null);
     
    } else  {
      Boolean val = ((SeriesRecord) value).getInternetUpdate() ==1?true:false;
      setText("");
      setIcon(createIcon(val));
    }
    //setHorizontalAlignment(SwingConstants.CENTER);
    setHorizontalAlignment(SwingConstants.CENTER);
    return this;
  }

  private Icon createIcon(Boolean checked) {
    if (!checked) {
      ImageIcon im = new ImageIcon(getClass().getResource(NOT_CHECKED));
      return im;
    }
    ImageIcon im = new ImageIcon(getClass().getResource(CHECKED_OK));
    return im;
  }
}
