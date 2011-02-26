/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.subtitles.sonline;

import database.SeriesRecord;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import tools.download.subtitles.SubtitleCode;
import tools.download.subtitles.SubtitleConstants;

/**
 *
 * @author lordovol
 */
public class GetSubtitleOnlineCode implements SubtitleConstants{

  public String subtitleOnlineCode;
  private SeriesRecord series;
  private ArrayList<SCode> sLinks;

  public GetSubtitleOnlineCode(SeriesRecord series) {
    this.series = series;
    try {
      getCode();
    } catch (IOException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
      MyMessages.error("I/O Error", "could not read from input stream");
    }
  }

  private void getCode() throws IOException {
    if (MyUsefulFunctions.hasInternetConnection(SUBTITLE_ONLINE_URL)) {
      URL subsUrl = new URL(SUBTITLE_ONLINE_URL + "search?query=" + URLEncoder.encode(series.getTitle(), "UTF-8"));
      BufferedReader in = new BufferedReader(new InputStreamReader(subsUrl.openStream()));
      parseSearchResult(in);
      in.close();
      if (sLinks.size() == 0) {
        MyMessages.message("Series not found", "The series " + series.getFullTitle() + " is not found in SubtitleOnline");
      } else if (sLinks.size() == 1) {
        this.subtitleOnlineCode = sLinks.get(0).getCode();
      } else {
        SCode tl = (SCode) JOptionPane.showInputDialog(null, "Multiple series found", "Choose the right series", JOptionPane.QUESTION_MESSAGE, null, sLinks.toArray(), 0);
        if (tl != null) {
          this.subtitleOnlineCode = tl.getCode();
        }
      }
    } else {
      MyMessages.internetError();
    }
  }

  private void parseSearchResult(BufferedReader in) throws IOException {
    sLinks = new ArrayList<SCode>();
    String line = "";
    boolean inResults = false;
    String curTitle = "";
    String curSOnlineCode = "";
    while ((line = in.readLine()) != null) {
      if (line.indexOf("<h2>Following TV Shows Were Found:</h2>") > -1) {
        inResults = true;
      }
      if (line.indexOf("<h2>Following Episodes Were Found:</h2>") > -1) {
        inResults = false;
      }
      if (inResults) {
        if (line.indexOf("-subtitles.html\">") > -1) {
          String code = line.replaceAll("(<a href=\"/)|(-subtitles.html\">)", "");
          String title = in.readLine().replaceAll("</a></td>", "");
          SCode s = new SCode();
          s.setTitle(title.trim());
          s.setCode(code.trim());
          sLinks.add(s);
        }
      }
      

    }

  }

  class SCode extends SubtitleCode {

    @Override
    public void setCode(String code) {
      this.code = code;
    }
  }
}
