/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.download.subtitles.tvsubtitles;

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
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class GetTvSubtitlesCode {
  public String tSubCode;
  private SeriesRecord series;
  private ArrayList<TLink> sLinks;

  public GetTvSubtitlesCode(SeriesRecord series) {
    try {
      this.series = series;
      getCode();
    } catch (IOException ex) {
      myseries.MySeries.logger.log(Level.SEVERE, null, ex);
      MyMessages.error("I/O Error", "could not read from input stream");
    }
  }

  private void getCode() throws IOException{
    MyUsefulFunctions.initInternetConnection();
    if (MyUsefulFunctions.hasInternetConnection()) {
      URL subsUrl = new URL(Options._TV_SUBTITLES_URL_ + "search.php?q=" + URLEncoder.encode(series.getTitle(),"UTF-8"));
      BufferedReader in = new BufferedReader(new InputStreamReader(subsUrl.openStream()));
      parseSearchResult(in);
      in.close();
      if (sLinks.size() == 0) {
        MyMessages.message("Series not found", "The series " + series.getFullTitle() + " is not found in SubtitleOnline");
      } else if (sLinks.size() == 1) {
        this.tSubCode = sLinks.get(0).tSubCode;
      } else {
        TLink tl = (TLink) JOptionPane.showInputDialog(null, "Multiple series found", "Choose the right series", JOptionPane.QUESTION_MESSAGE, null, sLinks.toArray(), 0);
        if (tl != null) {
          this.tSubCode = tl.tSubCode;
        }
      }
    } else {
      MyMessages.internetError();
    }
  }

  private void parseSearchResult(BufferedReader in) throws IOException {
    sLinks = new ArrayList<TLink>();
    String line = "";
    boolean inResults = false;
    String curTitle = "";
    String curSOnlineCode = "";
    while ((line = in.readLine()) != null) {
      if (line.indexOf("<p class=\"description\">Search results") > -1) {
        String[] results = line.split("(<a href=\"/)|(</a>)", -1);
        for (int i =0 ; i < results.length ; i++ ){
          String r = results[i];
          if(r.startsWith("tvshow")){
            String code = r.replaceAll("(tvshow-)|(\\.html.++)", "");
            String title = r.replaceAll("(tvshow-\\d*\\.html\">)", "");
            sLinks.add(new TLink(title, code));
          }
        }
      }
    }

  }

   class TLink {

    String title;
    String tSubCode;

    private TLink(String title, String tSCode) {
      this.title = title.trim();
      this.tSubCode = tSCode.trim()+"-"+series.getSeason();
    }

    @Override
    public String toString() {
      return title;
    }
  }

}
