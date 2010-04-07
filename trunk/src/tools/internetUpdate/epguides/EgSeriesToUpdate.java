/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.internetUpdate.epguides;

import database.SeriesRecord;
import java.util.ArrayList;
import tools.internetUpdate.AbstractSeriesToUpdate;

/**
 *
 * @author lordovol
 */
public class EgSeriesToUpdate extends AbstractSeriesToUpdate {
  public ArrayList<EgEpisode> episodes = new ArrayList<EgEpisode>();

  public EgSeriesToUpdate(SeriesRecord series) {
    this.series = series;
  }
}
