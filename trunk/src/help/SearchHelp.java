/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package help;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import myComponents.MyUsefulFunctions;
import tools.MySeriesLogger;

/**
 *
 * @author ssoldatos
 */
public class SearchHelp {

  private final Help help;
  private final String phrase;
  public ArrayList<Result> results = new ArrayList<Result>();

  public SearchHelp(Help help, String phrase) {
    this.help = help;
    this.phrase = phrase;
  }

  public void search() {
    URL res = this.getClass().getResource("/help/html/");
    File[] resources = getResourceFiles(res);
    for (int i = 0; i < resources.length; i++) {
      File file = resources[i];
      String phraseFound = phraseFound(file);
      if ( phraseFound != null) {
        results.add(new Result(file.getName(),phraseFound));
      }
    }
  }

  private File[] getResourceFiles(URL res) {
    try {
      File dir = new File(res.toURI());
      return dir.listFiles();
    } catch (URISyntaxException ex) {
      return new File[]{};
    }
  }

  private String phraseFound(File file) {
    try {
      StringBuilder sb = new StringBuilder(1024);
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
      String[] tokens = phrase.split(" ");
      for (int i = 0; i < tokens.length; i++) {
        String tok = tokens[i];
        int pos = contents.indexOf(tok);
        if (pos > -1 && tok.length() > 3) {
          int l = contents.length();
          return getText(contents,pos);
        }
      }

      return null;
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not read help file " + file.getName(), ex);
      return null;
    }
  }

  private String getText(String contents, int pos) {
    int start = 0;
    int length = contents.length();
    int end = contents.length();
    start = pos-40 < 0 ? 0 : pos-40;
    end = pos + 40 > length ? length : pos+40;
    String pre = start==0 ? "" : "...";
    String suf = end==length ? "" : "...";
    return pre + contents.substring(start, end) + suf;
  }

  public class Result {

    private final String fileName;
    public String link;
    public String title;
    public String text;

    private Result(String fileName, String text) {
      this.fileName = fileName.replace(".html", "");
      this.title = getTitle();
      this.link = getLink();
      this.text = text;
    }

    private String getTitle() {
      Set<String> keys = Links.links.keySet();
      for (Iterator<String> it = keys.iterator(); it.hasNext();) {
        String key = it.next();
        String l = Links.links.get(key);
        if (l.equals(fileName)) {
          return key;
        }
      }

      return MyUsefulFunctions.capitalizeString(fileName.replaceAll("_", " "));

    }

    private String getLink() {
      return "[<span class=\"link\">" + title + "</span>]";
    }

    @Override
    public String toString() {
      return link;
    }
  }
}
