/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.subtitles.sonline;

import database.SeriesRecord;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import tools.options.Options;

/**
 *
 * @author ΔΙΟΝΥΣΗΣ
 */
public class GetSOnlineCode {

  private SeriesRecord series;
  private ArrayList<SLink> sLinks;
  public String sOnlineCode = "";

  public GetSOnlineCode(SeriesRecord series) {
    try {
      this.series = series;
      getCode();
    } catch (IOException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
      MyMessages.error("I/O Error", "could not read from input stream");
    }
  }

  private void getCode() throws IOException {
    MyUsefulFunctions.initInternetConnection();
    if (MyUsefulFunctions.hasInternetConnection()) {
    } else {
      MyMessages.internetError();
      File f = new File(Options._SUBTITLE_ONLINE_URL_ + "search_query=" + series.getTitle() + ".htm");
      BufferedReader in = MyUsefulFunctions.createInputStream(f);
      parseSearchResult(in);
      in.close();
      if (sLinks.size() == 0) {
        MyMessages.message("Series not found", "The series " + series.getFullTitle() + " is not found in SubtitleOnline");
      } else if (sLinks.size() == 1) {
        this.sOnlineCode = sLinks.get(0).sOnlineCode;
      } else {
        SLink sl = (SLink) JOptionPane.showInputDialog(null, "Multiple series found", "Choose the right series", JOptionPane.QUESTION_MESSAGE, null, sLinks.toArray(), 0);
        if (sl != null) {
          this.sOnlineCode = sl.sOnlineCode;
        }
      }
    }
  }

  private void parseSearchResult(BufferedReader in) throws IOException {
    sLinks = new ArrayList<SLink>();
    String line = "";
    boolean inResults = false;
    String curTitle = "";
    String curSOnlineCode = "";
    while ((line = in.readLine()) != null) {
      if (line.indexOf("<h2>Following TV Shows Were Found:</h2>") > -1) {
        inResults = true;
      }
      if (inResults && line.indexOf("</table>") > -1) {
        inResults = false;
      }
      if (inResults) {
        if (line.indexOf("<a href") > -1) {
          curTitle = line.replaceAll("(<a href=\"/)|(\">)|(-subtitles.html)", "");
          line = in.readLine();
          curSOnlineCode = line.replaceAll("</a></td>", "");
          if (!curTitle.equals("") && !curSOnlineCode.equals("")) {
            sLinks.add(new SLink(curTitle, curSOnlineCode));
          }
        }
      }
    }
  }

  class SLink {

    String title;
    String sOnlineCode;

    private SLink(String title, String sOnlineCode) {
      this.title = title.trim();
      this.sOnlineCode = sOnlineCode.trim();
    }

    @Override
    public String toString() {
      return title;
    }
  }
}
