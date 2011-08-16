/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myGUI.myJTreeTable;

import database.SeriesRecord;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import myseriesproject.series.Series;
import tools.MySeriesLogger;


public class SeriesNode implements Comparable<SeriesNode> {

  Object value;
  Object[] children;
  boolean isChild;

  public SeriesNode(Object value, boolean isChild) {
    this.value = value;
    this.isChild = isChild;
  }
  // Used to sort the file names.
  static private MergeSort fileMS = new MergeSort() {

    public int compareElementsAt(int a, int b) {
      return ((String) toSort[a]).compareTo((String) toSort[b]);
    }
  };

  /**
   * Returns the the string to be used to display this leaf in the JTree.
   */
  public String toString() {
    if (value == null) {
      return "";
    }
    if (value instanceof String) {
      return (String) value;
    }
    return ((SeriesRecord) value).getFullTitle();
  }

  public SeriesRecord getSeriesRecord() {
    if (value instanceof SeriesRecord) {
      return (SeriesRecord) value;
    }
    return null;
  }

  /**
   * Loads the children, caching the results in the children ivar.
   */
  protected Object[] getSeriesChildren() {
    if (children != null) {
      return children;
    }
    ArrayList<SeriesRecord> series = new ArrayList<SeriesRecord>();
    try {
      if (value == null) {
        ResultSet rs = SeriesRecord.query("SELECT * FROM series WHERE deleted = 0 GROUP BY title ORDER BY title");
        while (rs.next()) {          
          series.add(SeriesRecord.create(rs));
        }
        children = new SeriesNode[series.size()];
        int i = 0;
        for (Iterator<SeriesRecord> it = series.iterator(); it.hasNext();) {
          SeriesRecord seriesRecord = it.next();
          children[i] = new SeriesNode(seriesRecord, false);
          i++;
        }

      } else if (!isChild) {
        SeriesRecord s = (SeriesRecord) value;
        Vector<SeriesRecord> seriesV = SeriesRecord.queryAll(
            SeriesRecord.C_TITLE + "=? AND " + SeriesRecord.C_SEASON + "<> ?",
            new String[]{s.getTitle(), String.valueOf(s.getSeason())}, SeriesRecord.C_SEASON+" ASC", null);
        int i=0;
        if(!seriesV.isEmpty()){
          value = s.getTitle();
        children = new SeriesNode[seriesV.size()+1];
        children[0] = new SeriesNode(s, true);
        i++;
        } else{
          children = new SeriesNode[seriesV.size()];
        }
        for (Iterator<SeriesRecord> it = seriesV.iterator(); it.hasNext();) {
          SeriesRecord seriesRecord = it.next();
          children[i] = new SeriesNode(seriesRecord, true);
          i++;
        }
        Arrays.sort(children);
      }
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
    
    return children;
  }

  public int compareTo(SeriesNode o) {
    if(this.value instanceof SeriesRecord && o.value instanceof SeriesRecord){
      SeriesRecord sThis = (SeriesRecord) this.value;
      SeriesRecord sOther = (SeriesRecord) o.value;
      return sThis.getSeason()-sOther.getSeason();
    }
    return 0;
  }
}
