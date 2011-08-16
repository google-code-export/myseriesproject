package myseriesproject.series;

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
import java.awt.Component;
import java.awt.ScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import myComponents.myGUI.myJTreeTable.AbstractCellEditor;
import myComponents.myGUI.myJTreeTable.JTreeTable;
import myComponents.myGUI.myJTreeTable.MyTreeTableCheckBoxCellRenderer;
import myComponents.myGUI.myJTreeTable.SeriesModel;
import myComponents.myGUI.myJTreeTable.SeriesTreeCellRenderer;
import myComponents.myTableCellEditors.MyDownloadedCellEditor;
import myComponents.myTableCellEditors.MyWatchedCellEditor;

/**
 * A TreeTable example, showing a JTreeTable, operating on the local file
 * system.
 *
 * @version %I% %G%
 *
 * @author Philip Milne
 */
public class SeriesTreeTable extends JTreeTable implements TableModelListener{

  public class TreeCellEditor extends DefaultCellEditor {
    private JCheckBox checkbox;
    public TreeCellEditor(JCheckBox checkbox) {
      super(checkbox);
      this.checkbox = checkbox;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
     return super.getTableCellEditorComponent(table, value, isSelected, row, column);
      
    }
    
    
    

   
  }

  public SeriesTreeTable() {
    super(new SeriesModel());
    tree.setRootVisible(false);
    tree.setShowsRootHandles(false);
    tree.setCellRenderer(new SeriesTreeCellRenderer());
    setShowGrid(true);
    getColumnModel().getColumn(Series.HIDDEN_COLUMN).setCellRenderer(new MyTreeTableCheckBoxCellRenderer());
    getColumnModel().getColumn(Series.UPDATE_COLUMN).setCellRenderer(new MyTreeTableCheckBoxCellRenderer());
    getColumnModel().getColumn(Series.UPDATE_COLUMN).setCellEditor(getDefaultEditor(Boolean.class));
   
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    super.tableChanged(e);
  }

 
  
  
  
  
}
