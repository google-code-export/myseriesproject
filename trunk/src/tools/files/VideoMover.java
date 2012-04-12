/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.files;

import database.SeriesRecord;
import java.util.ArrayList;
import myComponents.myFileFilters.VideoFilter;

/**
 *
 * @author ssoldatos
 */
public class VideoMover extends AbstractMover{

  
  public VideoMover(ArrayList<SeriesRecord> series) {
    super(series);
    filter = new VideoFilter();
  }
}
