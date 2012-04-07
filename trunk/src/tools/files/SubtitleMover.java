/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.files;

import database.SeriesRecord;
import java.util.ArrayList;
import myComponents.myFileFilters.SubtitlesFilter;

/**
 *
 * @author ssoldatos
 */
public class SubtitleMover extends AbstractMover{

  public SubtitleMover(ArrayList<SeriesRecord> series) {
    super(series);
    filter = new SubtitlesFilter();
  }
}
