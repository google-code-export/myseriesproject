/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myEvents;

import database.EpisodesRecord;
import database.SeriesRecord;
import java.util.EventObject;

/**
 *
 * @author ssoldatos
 */
public class MyEvent extends EventObject {

  private static final long serialVersionUID = 34536467474567L;
  private int type;
  private SeriesRecord series = null;
  private EpisodesRecord episode = null;
  private boolean singleEpisode = true;
  private boolean episodesPanel = true;
  private boolean seriesPanel = false;


  public MyEvent(Object source, int type) {
    super(source);
    this.type = type;

  }

  public int getType() {
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

  /**
   * @return the episodes
   */
  public EpisodesRecord getEpisode() {
    return episode;
  }

  /**
   * @param episodes the episodes to set
   */
  public void setEpisode(EpisodesRecord episode) {
    this.episode = episode;
  }

  /**
   * @return the singleEpisode
   */
  public boolean isSingleEpisode() {
    return singleEpisode;
  }

  /**
   * @param singleEpisode the singleEpisode to set
   */
  public void setSingleEpisode(boolean singleEpisode) {
    this.singleEpisode = singleEpisode;
  }

  /**
   * @return the episodesPanel
   */
  public boolean isEpisodesPanel() {
    return episodesPanel;
  }

  /**
   * @param episodesPanel the episodesPanel to set
   */
  public void setEpisodesPanel(boolean episodesPanel) {
    this.episodesPanel = episodesPanel;
  }

  /**
   * @return the seriesPanel
   */
  public boolean isSeriesPanel() {
    return seriesPanel;
  }

  /**
   * @param seriesPanel the seriesPanel to set
   */
  public void setSeriesPanel(boolean seriesPanel) {
    this.seriesPanel = seriesPanel;
  }
}
