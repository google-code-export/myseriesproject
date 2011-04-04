/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseries.episodes;

import database.EpisodesRecord;
import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
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
  public static void getVideos(File directory, String regex, String regexFake) {
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
        playVideo(video);
      } else {
        getVideos(video, regex,regexFake);
      }
    } else if (videos.isEmpty()) {
      MyMessages.error("No file found", "Episode was not found");
    } else {
      String[] videosArray = new String[videos.size()];
      int z = 0;
      for (Iterator<File> it = videos.iterator(); it.hasNext();) {
        File video = it.next();
        videosArray[z] = video.getName();
        z++;
      }
      String choice = (String) MyMessages.ask("Multiple files found",  "Multiple files found. Select the one you want to view.", null, videosArray, videosArray[0]);
//      String choice = (String) JOptionPane.showInputDialog(
//              null,
//              "Multiple files found. Select the one you want to view.",
//              "Multiple files found",
//              JOptionPane.QUESTION_MESSAGE,
//              null,
//              videosArray, videosArray[0]);
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
        String[] command = {app,  video.getAbsolutePath()};
        String[] envp = null; // should inherit the environment
        File startingFolder =  video.getParentFile();
        Process p = Runtime.getRuntime().exec(command, envp,startingFolder);
        //MyUsefulFunctions.log(Level.INFO, "Executing command " + command[0] + " " + command[1]);
        //rtime.exec(command);
      }
      EpisodesRecord ep = Episodes.getCurrentEpisode();
      ep.setSeen(1);
      ep.save();
      Episodes.updateEpisodesTable();
    } catch (Exception ex) {
      MyUsefulFunctions.log(Level.WARNING, "Playing videos is not supported", ex);
      MyMessages.error("Not supported", "Playing videos is not supported by your OS");
    }
  }

  private Video() {
  }
}
