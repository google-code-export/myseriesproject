/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.episodes;

import database.EpisodesRecord;
import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import myComponents.MyMessages;
import myComponents.myFileFilters.VideoFilter;
import tools.options.Options;

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
  public static void getVideos(File directory, String regex) {
    ArrayList<File> videos = new ArrayList<File>();
    File[] files = directory.listFiles(new VideoFilter());
    Pattern sPattern = Pattern.compile(regex);
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      Matcher sMatcher = sPattern.matcher(file.getName());
      if (sMatcher.find()) {
        videos.add(file);
      }
    }
    if (videos.size() == 1) {
      String videoName = videos.get(0).getName();
      File video = new File(directory + "/" + videoName);
      if (!video.isDirectory()) {
        playVideo(video);
      } else {
        getVideos(video, regex);
      }
    } else if (videos.size() == 0) {
      MyMessages.error("No file found", "Episode was not found");
    } else {
      String[] videosArray = new String[videos.size()];
      int z = 0;
      for (Iterator<File> it = videos.iterator(); it.hasNext();) {
        File video = it.next();
        videosArray[z] = video.getName();
        z++;
      }
      String choice = (String) JOptionPane.showInputDialog(
              null,
              "Multiple files found. Select the one you want to view.",
              "Multiple files found",
              JOptionPane.QUESTION_MESSAGE,
              null,
              videosArray, videosArray[0]);
      if (choice != null) {
        playVideo(new File(directory + "/" + choice));
      }
    }

  }

  /**
   * Plays a video file in the default media player
   * @param video The video file to play
   */
  public static void playVideo(File video) {
    try {
      String app = Options.toString(Options.VIDEO_APP);
      if(app.equals("")){
        Desktop.getDesktop().open(video);
      } else {
        Runtime rtime = Runtime.getRuntime();
        String command = "\""+app + "\" \"" + video.getAbsolutePath()+"\"";
        myseries.MySeries.logger.log(Level.INFO, "Executing command {0}", command);
        rtime.exec(command);
      }
      EpisodesRecord ep = Episodes.getCurrentEpisode();
      ep.setSeen(1);
      ep.save();
      Episodes.updateEpisodesTable();
    } catch (Exception ex) {
      myseries.MySeries.logger.log(Level.WARNING, "Playing videos is not supported", ex);
      MyMessages.error("Not supported", "Playing videos is not supported by your OS");
    }
  }

  private Video() {
  }
}
