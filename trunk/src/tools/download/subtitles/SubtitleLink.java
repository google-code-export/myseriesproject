/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.subtitles;

/**
 *
 * @author ssoldatos
 */
public abstract class SubtitleLink {

  protected String title;
  protected String code;

  public void setTitle(String title){
    this.title = title;
  }

  public String getCode(){
    return this.code;
  }

  public String getTitle(){
    return this.title;
  }

  public abstract void setCode(String code);
  
  @Override
  public String toString() {
    return title;
  }
}

