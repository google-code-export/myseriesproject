/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.internetUpdate.tvrage;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author lordovol
 */
public class TrEpisode {

    public int number;
    public String title;
    public String airDate;

    public TrEpisode(Node epNode) {
      Element episodeElement = (Element) epNode;
      number = Integer.parseInt(episodeElement.getElementsByTagName("seasonnum").item(0).getFirstChild().getNodeValue());
      title = episodeElement.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
      airDate = episodeElement.getElementsByTagName("airdate").item(0).getFirstChild().getNodeValue();
    }
  }
