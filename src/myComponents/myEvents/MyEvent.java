/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myComponents.myEvents;

import database.SeriesRecord;
import java.util.EventObject;

/**
 *
 * @author ssoldatos
 */
public class MyEvent extends EventObject  {
 private static final long serialVersionUID = 34536467474567L;
  private int type;
  private SeriesRecord series = null;

  public MyEvent(Object source, int type) {
    super(source);
    this.type = type;

  }

  public int getType(){
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(int type) {
    this.type = type;
  }

  /**
   * @return the series
   */
  public SeriesRecord getSeries() {
    return series;
  }

  /**
   * @param series the series to set
   */
  public void setSeries(SeriesRecord series) {
    this.series = series;
  }

}
