/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.internetUpdate.epguides;

import database.SeriesRecord;

/**
 *
 * @author ssoldatos
 */
public class EgEpisodeOld {

    SeriesRecord series;
    String data;
    private int episodes;

    public EgEpisodeOld(SeriesRecord series, String data) {
      this.series = series;
      this.data = data;
    }

    /**
     * @return the episodes
     */
    public int getEpisodes() {
      return episodes;
    }

    /**
     * @param episodes the episodes to set
     */
    public void setEpisodes(int episodes) {
      this.episodes = episodes;
    }
  }
