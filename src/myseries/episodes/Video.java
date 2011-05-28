/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.episodes;

import database.DBConnection;
import database.EpisodesRecord;
import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import tools.MySeriesLogger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import myComponents.MyMessages;
import myComponents.myFileFilters.VideoFilter;
import tools.options.Options;
import myComponents.MyUsefulFunctions;
/**
 * Episode video helper class
 * @author lordovol
 */
public class Video {

  /**
   * Get the videos from a directory using a regex
   * @param directory The directory to scan
   * @param regex The regex to use
   */
  public static void getVideos(File directory, String regex, String regexFake, JTable episodesTable) {
    MySeriesLogger.logger.log(Level.INFO, "Getting videos interface directory {0}",directory);
    ArrayList<File> videos = new ArrayList<File>();
    File[] files = directory.listFiles(new VideoFilter());
    Pattern sPattern = Pattern.compile(regex);
    Pattern sPatternFake = Pattern.compile(regexFake);
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      Matcher sMatcher = sPattern.matcher(file.getName());
      Matcher sMatcherFake = sPatternFake.matcher(file.getName());
      if (sMatcher.find() && !sMatcherFake.find()) {
        videos.add(file);
      }
    }
    if (videos.size() == 1) {
      String videoName = videos.get(0).getName();
      File video = new File(directory + "/" + videoName);
      if (!video.isDirectory()) {
        playVideo(video, episodesTable);
      } else {
        getVideos(video, regex,regexFake, episodesTable);
      }
    } else if (videos.isEmpty()) {
      MySeriesLogger.logger.log(Level.INFO, "No videos found");
      MyMessages.error("No file found", "Episode was not found", true, true);
    } else {
      MySeriesLogger.logger.log(Level.FINE, "Found {0} videos",videos.size());
      String[] videosArray = new String[videos.size()];
      int z = 0;
      for (Iterator<File> it = videos.iterator(); it.hasNext();) {
        File video = it.next();
        videosArray[z] = video.getName();
        z++;
      }
      String choice = (String) MyMessages.ask("Multiple files found",  "Multiple files found. Select the one you want to view.", null, videosArray, videosArray[0], true);
      if (choice != null) {
        playVideo(new File(directory + "/" + choice), episodesTable);
      } else {
        MySeriesLogger.logger.log(Level.INFO, "Playing of video aborted");
      }
    }

  }

  /**
   * Plays a video file in the default media player
   * @param video The video file to play
   */
  public static void playVideo(File video, JTable episodesTable) {
    try {
       MySeriesLogger.logger.log(Level.INFO, "Playing video {0}",video.getName());
      String app = Options.toString(Options.VIDEO_APP);
      if(app.equals("")){
        MySeriesLogger.logger.log(Level.INFO, "Using default OS video player");
        Desktop.getDesktop().open(video);
      } else {
        MySeriesLogger.logger.log(Level.INFO, "Using application",app);
        MyUsefulFunctions.runExternalProgram(new String[] {app, video.getAbsolutePath()}, video.getParentFile());
      }
      MySeriesLogger.logger.log(Level.INFO, "Setting episode assert watched");
      EpisodesRecord ep = Episodes.getCurrentEpisode();
      ep.setSeen(1);
      ep.save(DBConnection.conn.createStatement());
      MySeriesLogger.logger.log(Level.FINE, "Episode set as watched;");
      Episodes.updateEpisodesTable(episodesTable);
    } catch (Exception ex) {
      MySeriesLogger.logger.log(Level.WARNING, "Playing videos is not supported", ex);
      MyMessages.warning("Not supported", "Playing videos is not supported by your OS", true, true);
    }
  }

  private Video() {
  }
}
