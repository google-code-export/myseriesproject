/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.internetUpdate;

import database.SeriesRecord;

/**
 *
 * @author ssoldatos
 */
public abstract class AbstractSeriesToUpdate {

  public SeriesRecord series;
  public boolean update = false;

  @Override
  public String toString() {
    return series.getFullTitle();
  }
}
