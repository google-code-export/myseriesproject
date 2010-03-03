/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import myComponents.MyUsefulFunctions;

/**
 *
 * @author lordovol
 */
public class SavedFilterRecord extends Record {

  String table = "filters";
  private int filter_ID = 0;
  private String title = "";
  private int downloaded = 0;
  private int seen = 0;
  private int subtitles = 0;

  public SavedFilterRecord() {
    super();
  }

  public static Vector<SavedFilterRecord> getAllSavedFilterRecords() throws SQLException {
    Vector<SavedFilterRecord> recs = getSavedFilterRecordBySql("SELECT * FROM filters ORDER BY title");
    return recs;
  }

  public static SavedFilterRecord getSavedFilterByTitle(String title) throws SQLException {
    Vector<SavedFilterRecord> a = getSavedFilterRecordBySql("SELECT * FROM filters WHERE title = '" + title + "' LIMIT 1");
    if (a.size() == 1) {
      return a.get(0);
    } else {
      return null;
    }


  }

  public static Vector<SavedFilterRecord> getSavedFilterRecordBySql(String sql) throws SQLException {
    SavedFilterRecord s = new SavedFilterRecord();
    ResultSet rs = null;
    try{
    rs = SavedFilterRecord.query(sql);
    Vector<SavedFilterRecord> a = new Vector<SavedFilterRecord>();
    while (rs.next()) {
      s = new SavedFilterRecord();
      s.setFilter_ID(rs.getInt("filter_ID"));
      s.setDownloaded(rs.getInt("downloaded"));
      s.setSeen(rs.getInt("seen"));
      s.setSubtitles(rs.getInt("subtitles"));
      s.setTitle(rs.getString("title"));
      a.add(s);
    }
    rs.close();
    return a;
    } finally{
      if (rs != null) {
        rs.close();
      }
    }
  }

  public static String[] getFiltersList() throws SQLException{
    Vector<SavedFilterRecord> sfr = SavedFilterRecord.getAllSavedFilterRecords();
    String[] filters = new String[sfr.size()];
    for (int i = 0 ; i < sfr.size() ; i++){
      filters[i] = sfr.get(i).getTitle();
    }
    return filters;
  }

  public int delete() throws SQLException {
    String sql = "";
    if (this.getFilter_ID() != 0) {
      sql = "DELETE FROM filters WHERE filter_ID = " + this.getFilter_ID();
      return queryUpdate(sql);
    }
    return -1;
  }

  public int save() throws SQLException {
    int result = 0;
    String sql;
    if (this.getFilter_ID() != 0) {
      sql = "UPDATE filters SET title = '" + this.getTitle() + "', downloaded = " + this.getDownloaded() +
              ", seen = " + this.getSeen() + ", subtitles = " + this.getSubtitles() + " WHERE filter_ID = " + this.getFilter_ID();
    } else {
      sql = "INSERT INTO filters (title, downloaded, seen, subtitles) VALUES('" + this.getTitle() + "', " +
              this.getDownloaded() + ", " + this.getSeen() + ", " + this.getSubtitles() + ")";
    }
    return queryUpdate(sql);
  }

   
   
  /**
   * @return the filter_ID
   */
  public int getFilter_ID() {
    return filter_ID;
  }

  /**
   * @param filter_ID the filter_ID to set
   */
  public void setFilter_ID(int filter_ID) {
    this.filter_ID = filter_ID;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return MyUsefulFunctions.unescapeString(title);
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = MyUsefulFunctions.escapeString(title);
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

  /**
   * @return the seen
   */
  public int getSeen() {
    return seen;
  }

  /**
   * @param seen the seen to set
   */
  public void setSeen(int seen) {
    this.seen = seen;
  }

  /**
   * @return the subtitles
   */
  public int getSubtitles() {
    return subtitles;
  }

  /**
   * @param subtitles the subtitles to set
   */
  public void setSubtitles(int subtitles) {
    this.subtitles = subtitles;
  }

 
}
