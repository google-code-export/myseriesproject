/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import database.DBHelper;
import database.EpisodesRecord;
import database.SeriesRecord;
import java.awt.Component;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import myseriesproject.statistics.StatSeries;
import tools.options.MySeriesOptions;

/**
 *
 * @author lordovol
 */
public class MyDecimalFormatRenderer extends DefaultTableCellRenderer {

  public static final long serialVersionUID = 14232533456L;

  @Override
  public Component getTableCellRendererComponent(
          JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    if (table.getName().equals("seriesStats")) {
      Vector<EpisodesRecord> episodes;
      if (table.getValueAt(row, StatSeries.SERIES_COLUMN) instanceof SeriesRecord) {
        SeriesRecord series = (SeriesRecord) table.getValueAt(row, StatSeries.SERIES_COLUMN);
        episodes = DBHelper.getSeriesEpisodesByRate(series.getSeries_ID());
        setToolTipText(createTooltip(episodes, false));
      } else {
        String title = (String) table.getValueAt(row, StatSeries.SERIES_COLUMN);
        episodes = DBHelper.getSeriesEpisodesByRate(title);
        setToolTipText(createTooltip(episodes, true));
      }

    }
    value = MySeriesOptions._DEC_FORMAT_.format(value);
    setHorizontalAlignment(SwingConstants.RIGHT);
    return super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);
  }

  private String createTooltip(Vector<EpisodesRecord> episodes, boolean unified) {
    if(episodes == null){
      return "";
    }
    String tip = "<html><table>";
    for (Iterator<EpisodesRecord> it = episodes.iterator(); it.hasNext();) {
      EpisodesRecord episode = it.next();
      String seasonStr = "";
      if (unified) {
        int season = DBHelper.getSeasonByEpisodeId(episode.getEpisode_ID());
        seasonStr = season + ".";
      }
      tip += "<hr color='black'><tr><td align='right'>" + seasonStr + episode.getEpisode() + "</td><td>" + episode.getTitle() + "</td><td align='right'>" + episode.getRate() + "</td></tr>";
    }
    tip += "</table></html>";
    return tip;
  }
}
