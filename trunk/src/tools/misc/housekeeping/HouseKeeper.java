/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.misc.housekeeping;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import tools.MySeriesLogger;

/**
 *
 * @author ssoldatos
 */
class HouseKeeper implements Runnable {

  int totals = 0;
  int deletedCount = 0;
  ArrayList<HouseKeepFile> deleted = new ArrayList<HouseKeepFile>();
  ArrayList<HouseKeepFile> notDeleted = new ArrayList<HouseKeepFile>();
  private final HouseKeeping h;

  public HouseKeeper(HouseKeeping h) {
    this.h = h;
  }

  @Override
  public void run() {
    keep();
    h.dispose();
    KeeperResults r = new KeeperResults(this);
  }

  private void keep() {
    totals += h.cb_torrents.isSelected() ? h.torrents.length : 0;
    totals += h.cb_logs.isSelected() ? h.logs.length : 0;
    totals += h.cb_screenhsots.isSelected() ? h.screenshots.length : 0;
    MySeriesLogger.logger.log(Level.INFO, "Total files to delete : {0}",totals);
    if (h.cb_torrents.isSelected()) {
      MySeriesLogger.logger.log(Level.INFO, "Deleting torrents");
      deleteFiles(h.torrents, HouseKeepFile.TORRENT);
    }
    if (h.cb_logs.isSelected()) {
      MySeriesLogger.logger.log(Level.INFO, "Deleting logs");
      deleteFiles(h.logs, HouseKeepFile.LOG);
    }
    if (h.cb_screenhsots.isSelected()) {
      MySeriesLogger.logger.log(Level.INFO, "Deleting screenshots");
      deleteFiles(h.screenshots, HouseKeepFile.SCREENSHOT);
    }
    MySeriesLogger.logger.log(Level.FINE, "Deleted {0} files",deletedCount);
    if(notDeleted.size() > 0){
      MySeriesLogger.logger.log(Level.WARNING, "Not deleted {0} files", notDeleted.size());
    }
  }

  private void deleteFiles(File[] files, int type) {
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      if (file.delete()) {
        deletedCount++;
        int perc = deletedCount * 100 / totals;
        h.progress.setValue(perc);
        deleted.add(new HouseKeepFile(type, file));
      } else {
        notDeleted.add(new HouseKeepFile(type, file));
      }
    }
  }
}
