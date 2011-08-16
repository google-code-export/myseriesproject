package myComponents.myGUI.myJTreeTable;

/*
 * %W% %E%
 *
 * Copyright 1997, 1998 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer. 
 *   
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution. 
 *   
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.  
 * 
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE 
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,   
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER  
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF 
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS 
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */
import database.SeriesRecord;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TreeModelListener;
import javax.swing.table.DefaultTableModel;
import myseriesproject.series.Series;

public class SeriesModel extends AbstractTreeTableModel
    implements TreeTableModel {

  // Names of the columns.
  static protected String[] cNames = {Series.SERIES_RECORD_COLUMN_TITLE,
    Series.HIDDEN_COLUMN_TITLE, Series.UPDATE_COLUMN_TITLE};
  // Types of the columns.
  static protected Class[] cTypes = {TreeTableModel.class, Boolean.class, Boolean.class};

  public SeriesModel() {
    super(new SeriesNode(null, false));
  }

  @Override
  public boolean isCellEditable(Object node, int column) {
    return  true;
  }

  //
  // Some convenience methods. 
  //
  protected SeriesRecord getSeriesRecord(Object node) {
    if (node == null) {
      return new SeriesRecord();
    }
    if (node instanceof String) {
      return new SeriesRecord();
    }
    SeriesNode seriesNode = ((SeriesNode) node);
    return seriesNode.getSeriesRecord();
  }

  protected Object[] getChildren(Object node) {
    SeriesNode seriesNode = ((SeriesNode) node);
    return seriesNode.getSeriesChildren();
  }

  //
  // The TreeModel interface
  //
  public int getChildCount(Object node) {
    Object[] children = getChildren(node);
    return (children == null) ? 0 : children.length;
  }

  public Object getChild(Object node, int i) {
    return getChildren(node)[i];
  }

  // The superclass's implementation would work, but this is more efficient. 
  public boolean isLeaf(Object node) {
    return super.isLeaf(node);
  }

  //
  //  The TreeTableNode interface. 
  //
  public int getColumnCount() {
    return cNames.length;
  }

  public String getColumnName(int column) {
    return cNames[column];
  }

  public Class getColumnClass(int column) {
    return cTypes[column];
  }

  public Object getValueAt(Object node, int column) {
   
    SeriesRecord seriesRecord = getSeriesRecord(node);
    if (seriesRecord == null) {
      seriesRecord = new SeriesRecord();
    }
    return seriesRecord;
//      switch (column) {
//        case 0:
//          return seriesRecord;
//        case 1:
//          return seriesRecord.getHidden() == 1 ? true : false;
//        case 2:
//          return seriesRecord.getInternetUpdate() == 1 ? true : false;
//      }
 //   return null;
  }
}



