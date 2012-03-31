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
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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

  public void search() throws IOException {

    ArrayList<File> resources = getResourceFiles();
    for (int i = 0; i < resources.size(); i++) {
      File file = resources.get(i);
      MySeriesLogger.logger.log(Level.INFO, "Searching file {0}", file);
      String phraseFound = phraseFound(file);
      if (phraseFound != null) {
        MySeriesLogger.logger.log(Level.FINE, "Found phrase {0}", phraseFound);
        results.add(new Result(file.getName(), phraseFound));
      }
    }

    if (results.isEmpty()) {
      MySeriesLogger.logger.log(Level.WARNING, "No results found");
    }
  }

  private ArrayList<File> getResourceFiles() throws IOException {
    CodeSource src = this.getClass().getProtectionDomain().getCodeSource();
    ArrayList<File> resources = new ArrayList<File>();
    if (src != null) {
      URL jar = src.getLocation();
      ZipInputStream zip = new ZipInputStream(jar.openStream());
      ZipEntry ze = null;
      while ((ze = zip.getNextEntry()) != null) {
        String entryName = ze.getName();
        if (entryName.startsWith("help") && entryName.endsWith("html")) {
          resources.add(new File(entryName));
        }
      }
    }
    return resources;
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
          return getText(contents, pos);
        }
      }

      return null;
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not read help file " + file.getName(), ex);
      return null;
    } catch (Exception ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Could not read help file " + file.getName(), ex);
      return null;
    }
  }

  private String getText(String contents, int pos) {
    int start = 0;
    int length = contents.length();
    int end = contents.length();
    start = pos - 40 < 0 ? 0 : pos - 40;
    end = pos + 40 > length ? length : pos + 40;
    String pre = start == 0 ? "" : "...";
    String suf = end == length ? "" : "...";
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
