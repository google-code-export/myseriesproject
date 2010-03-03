/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.internetUpdate.tvrage;

import database.SeriesRecord;
import java.util.ArrayList;
import org.w3c.dom.Node;
import tools.internetUpdate.AbstractSeriesToUpdate;

/**
 *
 * @author lordovol
 */
public class TrSeriesToUpdate extends AbstractSeriesToUpdate {

  private Node epNode;
  public ArrayList<TrEpisode> episodes = new ArrayList<TrEpisode>();

  TrSeriesToUpdate(SeriesRecord series) {
    this.series = series;
  }

}
