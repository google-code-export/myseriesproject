/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellEditors;

import database.EpisodesRecord;
import database.SeriesRecord;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import myComponents.MyUsefulFunctions;
import myComponents.myTableCellRenderers.MySubtitleListRenderer;
import tools.languages.LangsList;
import tools.languages.Language;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class MySubtitleCellEditor extends AbstractCellEditor implements TableCellEditor {

  public static final long serialVersionUID = 234534634646L;
  JComboBox combo = new JComboBox();
  private int episodeColumn;

  public MySubtitleCellEditor(int episodeColumn) {
    this.episodeColumn = episodeColumn;


  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

    if (value instanceof Language) {
      Language[] subStatuses = {
        LangsList.NONE,
        myseries.MySeries.languages.getPrimary(),
        myseries.MySeries.languages.getSecondary(),
        LangsList.MULTIPLE};
      combo.setModel(new DefaultComboBoxModel(subStatuses));
      combo.setSelectedItem(value);
      combo.setRenderer(new MySubtitleListRenderer());
      combo.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          stopCellEditing();
        }
      });
    }
    return combo;
  }

  @Override
  public boolean isCellEditable(EventObject e) {
    MouseEvent me;
    if (e instanceof MouseEvent) {
      me = (MouseEvent) e;
    } else {
      return false;
    }
    SeriesRecord series = new SeriesRecord();
    EpisodesRecord ep = new EpisodesRecord();
    if (e.getSource() instanceof JTable) {
      JTable table = (JTable) e.getSource();
      int row = table.rowAtPoint(((MouseEvent) e).getPoint());
      ep = (EpisodesRecord) table.getValueAt(row, episodeColumn);
      int sid = ep.getSeries_ID();
      try {
        series = database.DBHelper.getSeriesByID(sid);
      } catch (SQLException ex) {
        return true;
      }
    }
    if (series.isValidLocalDir() && Options.toBoolean(Options.AUTO_FILE_UPDATING) && !MyUsefulFunctions.isNetworkPath(new File(series.getLocalDir()))) {
      return false;
    }
    if (!MyUsefulFunctions.hasBeenAired(ep.getAired(),true)) {
      return false;
    }
    return true;
  }

  @Override
  public Language getCellEditorValue() {
    return (Language) combo.getSelectedItem();
  }
}
