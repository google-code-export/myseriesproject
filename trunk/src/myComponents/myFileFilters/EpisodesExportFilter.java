/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myFileFilters;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import tools.importExport.ExportEpisodes;

/**
 *
 * @author ssoldatos
 */
public class EpisodesExportFilter  extends FileFilter {

  /**
   * The file filter extensions
   * @param f
   * @return
   */
  public boolean accept(File f) {
    return f.isDirectory() || f.getName().toLowerCase().endsWith(ExportEpisodes.EXT);
  }

  /**
   * The file filter description
   * @return
   */
  public String getDescription() {
    return "My Series episodes export file (*" + ExportEpisodes.EXT + ")";
  }
}
