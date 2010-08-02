/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myseries.schedule;

/**
 *
 * @author ssoldatos
 */
public class ScheduleEvent {
  private String series;
  private int episodeNumber;
  private String episode;
  private String image;
  private int downloaded;

  /**
   * @return the series
   */
  public String getSeries() {
    return series;
  }

  /**
   * @param series the series to set
   */
  public void setSeries(String series) {
    this.series = series;
  }

  /**
   * @return the episodeNumber
   */
  public int getEpisodeNumber() {
    return episodeNumber;
  }

  /**
   * @param episodeNumber the episodeNumber to set
   */
  public void setEpisodeNumber(int episodeNumber) {
    this.episodeNumber = episodeNumber;
  }

  /**
   * @return the episode
   */
  public String getEpisode() {
    return episode;
  }

  /**
   * @param episode the episode to set
   */
  public void setEpisode(String episode) {
    this.episode = episode;
  }

  /**
   * @return the image
   */
  public String getImage() {
    return image;
  }

  /**
   * @param image the image to set
   */
  public void setImage(String image) {
    this.image = image;
  }

  /**
   * @return the downloaded
   */
  public int getDownloaded() {
    return downloaded;
  }

  /**
   * @param downloaded the downloaded to set
   */
  public void setDownloaded(int downloaded) {
    this.downloaded = downloaded;
  }

}
