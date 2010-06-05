/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import database.DBHelper;
import database.EpisodesRecord;
import database.SeriesRecord;
import java.awt.Component;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import myseries.statistics.StatSeries;

/**
 *
 * @author lordovol
 */
public class MyDecimalFormatRenderer extends DefaultTableCellRenderer {

  public static final long serialVersionUID = 14232533456L;
  private static final DecimalFormat formatter = new DecimalFormat("#.00");

  @Override
  public Component getTableCellRendererComponent(
          JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    if (table.getName().equals("seriesStats")) {
      SeriesRecord series = (SeriesRecord) table.getValueAt(row, StatSeries.SERIES_COLUMN);
      Vector<EpisodesRecord> episodes = DBHelper.getSeriesEpisodesByRate(series.getSeries_ID());
      setToolTipText(createTooltip(episodes));
    }
    value = formatter.format(value);
    setHorizontalAlignment(SwingConstants.RIGHT);
    return super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);
  }

  private String createTooltip(Vector<EpisodesRecord> episodes) {
    String tip = "<html><table>";
    for (Iterator<EpisodesRecord> it = episodes.iterator(); it.hasNext();) {
      EpisodesRecord episode = it.next();
      tip += "<hr color='black'><tr><td align='right'>"+episode.getEpisode()+"</td><td>" + episode.getTitle() + "</td><td align='right'>" + episode.getRate() + "</td></tr>";
    }
    tip += "</table></html>";
    return tip;
  }
}
