/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.internetUpdate.epguides;

import database.SeriesRecord;
import java.util.ArrayList;
import tools.internetUpdate.AbstractSeriesToUpdate;

/**
 * The series to update from epguides
 * @author lordovol
 */
public class EgSeriesToUpdate extends AbstractSeriesToUpdate {
  public ArrayList<EgEpisode> episodes = new ArrayList<EgEpisode>();

  /**
   * The series to update
   * @param series an arraylist of series
   */
  public EgSeriesToUpdate(SeriesRecord series) {
    this.series = series;
  }
}
