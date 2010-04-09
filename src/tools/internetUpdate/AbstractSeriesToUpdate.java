/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.internetUpdate;

import database.SeriesRecord;

/**
 * Abstract class for the series to update
 * @author ssoldatos
 */
public abstract class AbstractSeriesToUpdate {

  /**
   * The series to update
   */
  public SeriesRecord series;
  /**
   * If the sereis are updated
   */
  public boolean update = false;

  @Override
  public String toString() {
    return series.getFullTitle();
  }
}
