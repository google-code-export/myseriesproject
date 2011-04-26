/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.subtitles.sonline;

import database.SeriesRecord;
import java.io.BufferedReader;
import tools.MySeriesLogger;
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
public class GetSubtitleOnlineCode implements SubtitleConstants {

    public String subtitleOnlineCode;
    private SeriesRecord series;
    private ArrayList<SCode> sLinks;

    public GetSubtitleOnlineCode(SeriesRecord series) {
        this.series = series;
        try {
            getCode();
        } catch (IOException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, null, ex);
            MyMessages.error("I/O Error", "could not read from input stream");
        }
    }

    private void getCode() throws IOException {
        MySeriesLogger.logger.log(Level.INFO, "Getting subtitle online code for series {0}", series.getFullTitle());
        if (MyUsefulFunctions.hasInternetConnection(SUBTITLE_ONLINE_URL)) {
            URL subsUrl = new URL(SUBTITLE_ONLINE_URL + "search?query=" + URLEncoder.encode(series.getTitle(), "UTF-8"));
            MySeriesLogger.logger.log(Level.INFO, "Reading from {0}", subsUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(subsUrl.openStream()));
            parseSearchResult(in);
            in.close();
            if (sLinks.isEmpty()) {
                MySeriesLogger.logger.log(Level.INFO, "Series not found");
                MyMessages.message("Series not found", "The series " + series.getFullTitle() + " is not found in SubtitleOnline");
            } else if (sLinks.size() == 1) {
                MySeriesLogger.logger.log(Level.INFO, "Subtitle online code found :{0}", sLinks.get(0).getCode());
                this.subtitleOnlineCode = sLinks.get(0).getCode();
            } else {
                MySeriesLogger.logger.log(Level.FINE, "{0} series found.Ask for the right one", sLinks.size());
                SCode tl = (SCode) MyMessages.ask("Choose the right series", "Multiple series found", null, sLinks.toArray(), null);
                if (tl != null) {
                    MySeriesLogger.logger.log(Level.INFO, "Subtitle online code chosen is {0}",tl.getCode());
                    this.subtitleOnlineCode = tl.getCode();
                }
            }
        } else {
            MyMessages.internetError(true);
        }
    }

    private void parseSearchResult(BufferedReader in) throws IOException {
        MySeriesLogger.logger.log(Level.INFO, "Parsing search result");
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
                    MySeriesLogger.logger.log(Level.FINE, "Found code {0} for title {1}",new Object[] {s.getCode(),s.getTitle()});
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
