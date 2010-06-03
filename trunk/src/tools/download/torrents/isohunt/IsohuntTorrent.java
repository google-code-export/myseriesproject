/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.download.torrents.isohunt;

import tools.download.torrents.AbstractTorrent;

/**
 *
 * @author ssoldatos
 */
public class IsohuntTorrent extends AbstractTorrent{

  private int files = 0;
  private long length = 0;
  private String size = "";
  private int seeds=0;
  private int leechers=0;
  private String publishedDate = "";
  private int votes = 0;
  private int comments = 0;
  private String summaryLink = "";
  private String originalLink = "";
  private String hash = "";
  private int downloads = 0;
  private String tracker = "";

  /**
   * @return the files
   */
  public int getFiles() {
    return files;
  }

  /**
   * @param files the files to set
   */
  public void setFiles(int files) {
    this.files = files;
  }

  /**
   * @return the length
   */
  public long getLength() {
    return length;
  }

  /**
   * @param length the length to set
   */
  public void setLength(long length) {
    this.length = length;
  }

  /**
   * @return the size
   */
  public String getSize() {
    return size;
  }

  /**
   * @param size the size to set
   */
  public void setSize(String size) {
    this.size = size;
  }

  /**
   * @return the seeds
   */
  public int getSeeds() {
    return seeds;
  }

  /**
   * @param seeds the seeds to set
   */
  public void setSeeds(int seeds) {
    this.seeds = seeds;
  }

  /**
   * @return the leechers
   */
  public int getLeechers() {
    return leechers;
  }

  /**
   * @param leechers the leechers to set
   */
  public void setLeechers(int leechers) {
    this.leechers = leechers;
  }

  /**
   * @return the publishedDate
   */
  public String getPublishedDate() {
    return publishedDate;
  }

  /**
   * @param publishedDate the publishedDate to set
   */
  public void setPublishedDate(String publishedDate) {
    this.publishedDate = publishedDate;
  }

  /**
   * @return the votes
   */
  public int getVotes() {
    return votes;
  }

  /**
   * @param votes the votes to set
   */
  public void setVotes(int votes) {
    this.votes = votes;
  }

  /**
   * @return the comments
   */
  public int getComments() {
    return comments;
  }

  /**
   * @param comments the comments to set
   */
  public void setComments(int comments) {
    this.comments = comments;
  }

  /**
   * @return the summaryLink
   */
  public String getSummaryLink() {
    return summaryLink;
  }

  /**
   * @param summaryLink the summaryLink to set
   */
  public void setSummaryLink(String summaryLink) {
    this.summaryLink = summaryLink;
  }

  /**
   * @return the originalLink
   */
  public String getOriginalLink() {
    return originalLink;
  }

  /**
   * @param originalLink the originalLink to set
   */
  public void setOriginalLink(String originalLink) {
    this.originalLink = originalLink;
  }

  /**
   * @return the hash
   */
  public String getHash() {
    return hash;
  }

  /**
   * @param hash the hash to set
   */
  public void setHash(String hash) {
    this.hash = hash;
  }

  /**
   * @return the downloads
   */
  public int getDownloads() {
    return downloads;
  }

  /**
   * @param downloads the downloads to set
   */
  public void setDownloads(int downloads) {
    this.downloads = downloads;
  }

  /**
   * @return the tracker
   */
  public String getTracker() {
    return tracker;
  }

  /**
   * @param tracker the tracker to set
   */
  public void setTracker(String tracker) {
    this.tracker = tracker;
  }
}
