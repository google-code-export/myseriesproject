/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.download;

import java.net.URL;

/**
 *
 * @author lordovol
 */
public class Subtitle {

  public String title;
  public URL url;

  public Subtitle(String title, URL url) {
    this.title = title;
    this.url = url;
  }



  @Override
  public String toString() {
    return title;
  }



}
