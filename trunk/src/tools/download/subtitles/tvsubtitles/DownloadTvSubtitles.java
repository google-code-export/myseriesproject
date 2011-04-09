/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.subtitles.tvsubtitles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import tools.MySeriesLogger;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import myComponents.MyMessages;
import myComponents.MyUsefulFunctions;
import tools.download.subtitles.AbstractDownloadSubtitle;
import tools.download.subtitles.Subtitle;
import tools.download.subtitles.SubtitleConstants;

/**
 * Download from tvSubtitles
 * @author lordovol
 */
public class DownloadTvSubtitles extends AbstractDownloadSubtitle implements Runnable, SubtitleConstants {

    private final String link;

    /**
     * Download the subtitles from tvSubtitles
     * @param link The link to the subtitles page
     * @param season The series season
     * @param episode The episode number
     * @param form The messages form
     */
    public DownloadTvSubtitles(String link, int season, int episode, TvSubtitlesForm form) {
        this.link = link;
        this.season = season;
        this.episode = episode;
        this.form = form;
        this.progress = form.progress;
    }

    public void run() {
        progress.setIndeterminate(true);
        progress.setString("Searching for subtitles");
        if (episode != -1) {
            MySeriesLogger.logger.log(Level.INFO, "Downloading from tv subtitles series {0} seadon {1} episode {2}", new Object[]{link, season, episode});
            getSubtitle();
        } else {
            try {
                MySeriesLogger.logger.log(Level.INFO, "Downloading whole season subtitles for series {0} seadon {1}", new Object[]{link, season});
                subs.add(new Subtitle("Whole season subtitles", new URL(link)));
            } catch (MalformedURLException ex) {
                MySeriesLogger.logger.log(Level.WARNING, "Malformed url: {0}", link);
                MyMessages.error("Download whole season subtitles", "Malformed url: " + link);
            }
        }
        progress.setIndeterminate(false);
        if (subs.isEmpty()) {
            MySeriesLogger.logger.log(Level.INFO, "Subtitle not found");
            form.dispose();
            if (!cancel) {
                MyMessages.error("Subtitle not found", "The subtitle was not found");
            }
        } else if (subs.size() == 1) {
            MySeriesLogger.logger.log(Level.FINE, "subtitle found {0}",subs.get(0).title);
            download(subs.get(0));
            form.dispose();
        } else {
            // Subtitle sub = (Subtitle) JOptionPane.showInputDialog(null, "Choose the subtitle to download", "Choose subtitle", JOptionPane.QUESTION_MESSAGE, null, subs.toArray(), 0);
            MySeriesLogger.logger.log(Level.FINE, "Found {0} subtitles", subs.size());
            Subtitle sub = (Subtitle) MyMessages.ask("Choose subtitle", "Choose the subtitle to download", null, subs.toArray(), null);
            if (sub != null) {
                MySeriesLogger.logger.log(Level.INFO, "Downloading subtitle {0}",sub.title);
                String newPath = sub.url.getPath().replace("/subtitle", "download");
                try {
                    sub.url = new URL(TV_SUBTITLES_URL + newPath);
                } catch (MalformedURLException ex) {
                    MyMessages.error("Error occured!!!", "Wrong url : " + sub.url);
                    MySeriesLogger.logger.log(Level.SEVERE, null, ex);
                    form.dispose();
                }
                download(sub);
                form.dispose();
            }
        }
        if (!srtFilename.equals("")) {
            MySeriesLogger.logger.log(Level.FINE, "Subtitle downloaded");
            MyMessages.message("Subtitle downloaded", "Subtitle\n" + srtFilename + "\nwas downloaded and extracted succesfully");
        }
    }

    private void getSubtitle() {
        try {
            if (MyUsefulFunctions.hasInternetConnection(TV_SUBTITLES_URL)) {
                MySeriesLogger.logger.log(Level.INFO, "Getting the subtitle links");
                String buff = parseWebPage();
                if (!buff.equals("")) {
                    String subsLink = getLink(buff, true);
                    if (subsLink != null) {
                        MySeriesLogger.logger.log(Level.FINE, "Subtitle page found");
                        getDownloadLinks(subsLink);
                    } else {
                        MySeriesLogger.logger.log(Level.INFO, "No links found");
                        form.dispose();
                    }
                } else {
                    MySeriesLogger.logger.log(Level.INFO, "Could not parse page");
                    form.dispose();
                }
            } else {
                MyMessages.internetError();
                form.dispose();
            }
        } catch (MalformedURLException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, null, ex);
            MyMessages.error("Error occured!!!", "Wrong url");
            form.dispose();
        } catch (IOException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, null, ex);
            MyMessages.error("Error occured!!!", "Could not read input stream");
            form.dispose();
        }
    }

    private String parseWebPage() throws MalformedURLException, IOException {
        MySeriesLogger.logger.log(Level.INFO, "Parsing webpage {0}",link);
        URL subsUrl = new URL(link);
        BufferedReader in = new BufferedReader(new InputStreamReader(subsUrl.openStream()));
        String inputLine;
        boolean copy = false;
        String buff = "";
        while ((inputLine = in.readLine()) != null) {
            String regex = "<td>0*" + season + "x0*" + episode + "</td>";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(inputLine);
            if (matcher.find()) {
                copy = true;
            }
            if (copy && inputLine.indexOf("</tr>") > -1) {
                copy = false;
            }
            if (copy) {
                buff += inputLine;
            }
        }
        return buff;
    }

    private String getLink(String buff, boolean primary) {
        MySeriesLogger.logger.log(Level.INFO, "Getting the subtitle link");
        String lang = "";
        lang = primary ? myseries.MySeries.languages.getPrimary().getCode()
                : myseries.MySeries.languages.getSecondary().getCode();

        int pos = buff.indexOf("<img src=\"images/flags/" + lang + ".gif\"");
        int i = pos;
        String subLink = null;
        while (i > 0) {
            String character = buff.substring(i - 1, i);
            if (character.equals("<")) {
                subLink = buff.substring(i, pos).replace("a href=\"", "").replaceFirst("\">", "");
                i = 0;
            }
            i--;
        }
        if (subLink != null) {
            MySeriesLogger.logger.log(Level.FINE, "Subtitle link found");
            return subLink;
        } else {
            if (!primary) {
                MySeriesLogger.logger.log(Level.INFO, "Subtitle not found");
                return null;
            }
            MySeriesLogger.logger.log(Level.INFO, "Primary subtitle not found.Asking for secondary");
            if (MyMessages.question("Download secondary language", "Primary language subs not found.\nSearch for secondary?") == JOptionPane.YES_OPTION) {
                MySeriesLogger.logger.log(Level.INFO, "Getting secondary subtitle");
                return getLink(buff, false);
            } else {
                MySeriesLogger.logger.log(Level.INFO, "Downloading aborted by the user");
                cancel = true;
                return null;
            }

        }
    }

    private void getDownloadLinks(String subsLink) throws MalformedURLException, IOException {
        MySeriesLogger.logger.log(Level.INFO, "Getting the download links from {0}",subsLink);
        URL subsUrl = new URL(TV_SUBTITLES_URL + subsLink);
        BufferedReader in = new BufferedReader(new InputStreamReader(subsUrl.openStream()));
        String inputLine, line = "";
        while ((inputLine = in.readLine()) != null) {
            String regex = "(<a href=\"/subtitle-)|(<a href=\"download)|(</h5>)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(inputLine);
            if (matcher.find()) {
                line += inputLine + "\n";
            }
        }
        String[] fields = line.split("(<a href=\"/)|(\"><div href=\"/)|(align=absmiddle>)|(<a href=\")|(\">)", -1);
        String curLink = "";
        String curTitle = "";
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            if ((field.startsWith("subtitle") || (field.startsWith("download"))) && field.endsWith("html")) {
                curLink = TV_SUBTITLES_URL + URLEncoder.encode(field, "UTF-8");
                if (field.startsWith("download")) {
                    curTitle = "dummy";
                }
            } else if (fields[i].indexOf("</h5>") > -1) {
                curTitle = field.substring(0, fields[i].indexOf("</h5>"));
            }
            if (!curTitle.equals("") && !curLink.equals("")) {
                MySeriesLogger.logger.log(Level.FINE, "Subtitle found {0}",curTitle);
                subs.add(new Subtitle(curTitle, new URL(curLink)));
                curTitle = "";
                curLink = "";

            }
        }
    }
}
