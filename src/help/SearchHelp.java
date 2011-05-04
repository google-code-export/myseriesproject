/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package help;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.logging.Level;
import java.util.logging.Logger;
import myComponents.MyUsefulFunctions;
import tools.MySeriesLogger;

/**
 *
 * @author ssoldatos
 */
public class SearchHelp {

  private final Help help;
  private final String phrase;
  private ArrayList<String> results = new ArrayList<String>();

  public SearchHelp(Help help, String phrase) {
    this.help = help;
    this.phrase = phrase;
  }

  public void search() {
    URL res = this.getClass().getResource("/help/html/");
    File[] resources = getResourceFiles(res);
    for (int i = 0; i < resources.length; i++) {
      File file = resources[i];
      if (phraseFound(file)) {
        results.add(file.getName());
      }
    }
    System.out.println(results);

  }

  private File[] getResourceFiles(URL res) {
    try {
      File dir = new File(res.toURI());
      return dir.listFiles();
    } catch (URISyntaxException ex) {
      return new File[]{};
    }
  }

  private boolean phraseFound(File file) {
    try {
      StringBuffer sb = new StringBuffer(1024);
      BufferedInputStream inStream = new BufferedInputStream(
          this.getClass().getResourceAsStream("/help/html/" + file.getName()));
      byte[] chars = new byte[1024];
      int bytesRead = 0;
      while ((bytesRead = inStream.read(chars)) > -1) {
        //System.out.println(bytesRead);
        sb.append(new String(chars, 0, bytesRead));
      }
      inStream.close();
      String contents = MyUsefulFunctions.stripHTML(sb.toString());
      if (contents.indexOf(phrase) > -1) {
        return true;
      }
      return false;
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not read help file " + file.getName(), ex);
      return false;
    }
  }
}
