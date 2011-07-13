/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents;

import database.DBConnection;
import database.EpisodesRecord;
import database.SeriesRecord;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import myComponents.myGUI.MyFont;
import myComponents.myListeners.MyFeedsTimerListener;
import myComponents.myListeners.MyMemoryTimerListener;
import myComponents.myTableCellRenderers.MyDownloadedCellRenderer;
import myComponents.myTableCellRenderers.MySubtitlesCellRenderer;
import myComponents.myTableCellRenderers.MyTitleCellRenderer;
import myComponents.myToolbar.AbstractToolbar;
import myComponents.myToolbar.ToolbarButtonActions;
import myComponents.myToolbar.ToolbarSeperator;
import myseries.MySeries;
import myseries.series.Series;
import tools.DesktopSupport;
import tools.MySeriesLogger;
import tools.Skin;
import tools.archive.ArchiveConstants;
import tools.archive.ArchiveFile;
import tools.download.subtitles.SubtitleConstants;
import tools.languages.LangsList;
import tools.languages.Language;
import tools.options.Options;

/**
 * Useful functions helper class
 * @author lordovol
 */
public class MyUsefulFunctions {

  public static final int VIDEO_FILE = 0;
  public static final int SUBTITLE_FILE = 1;
  public static final String GOOGLE = "http://www.google.com";

  /**
   * Converts a date from the database (YYYY-MM-DD) to the Options._DATE_FORMAT_
   * @param date
   * @return
   */
  public static String convertDateForRendering(String date) {
    MySeriesLogger.logger.log(Level.INFO, "Converting date {0} for rendering", date);
    SimpleDateFormat sdf = new SimpleDateFormat(Options.toString(Options.DATE_FORMAT));
    try {
      Date dateD = sdf.parse(date);
      sdf = new SimpleDateFormat(EpisodesRecord.MYSQL_DATE_FORMAT);
      date = sdf.format(dateD);
    } catch (ParseException ex) {
      //MySeriesLogger.logger.log(Level.SEVERE, "Parse exception while parsing date " + date, ex);
    }
    if (date.equals("")) {
      MySeriesLogger.logger.log(Level.WARNING, "Empty date");
      return "";
    }
    try {
      String[] d = date.split("-", -1);
      if (d[1].equals("00") || d[2].equals("00")) {
        MySeriesLogger.logger.log(Level.WARNING, "Zero date");
        return "";
      }
    } catch (ArrayIndexOutOfBoundsException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Wrong date format: " + date, ex);
    }
    try {
      DateFormat df = new SimpleDateFormat(EpisodesRecord.MYSQL_DATE_FORMAT);
      Date sDate = df.parse(date);
      SimpleDateFormat f = new SimpleDateFormat(Options.toString(Options.DATE_FORMAT));
      String formatedDate = f.format(sDate);
      MySeriesLogger.logger.log(Level.FINE, "Date converted to {0}", formatedDate);
      return formatedDate;
    } catch (ParseException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Parse exception. Returning the original date : " + date, ex);
      return date;
    }
  }

  /**
   * Convert date to insert in Database (DD/MM/YYYY)--> (YYYY-MM-DD)
   * @param date
   * @return
   */
  public static String convertDateForMySQL(String date) {
    try {
      MySeriesLogger.logger.log(Level.INFO, "Converting date {0} for sql", date);
      DateFormat df = new SimpleDateFormat(Options.toString(Options.DATE_FORMAT));
      Date sDate = df.parse(date);
      SimpleDateFormat f = new SimpleDateFormat(EpisodesRecord.MYSQL_DATE_FORMAT);
      String formatedDate = f.format(sDate);
      MySeriesLogger.logger.log(Level.FINE, "Date formated to {0}", formatedDate);
      return formatedDate;
    } catch (ParseException ex) {
      //MySeriesLogger.logger.log(Level.WARNING, "Could not format date. Returning original value {0}", date);
      return date;
    }
  }

  /**
   * Escape string for insertion in DB
   * @param str The string to escape
   * @return
   */
  public static String escapeString(String str) {
    str = str.replaceAll("'", "''");
    return str;
  }

  public static boolean hasInternetConnection(String address) {
    BufferedReader in = null;
    MySeriesLogger.logger.log(Level.INFO, "Checking internet availability : {0} ", address);
    try {
      URL url = new URL(address);
      in = new BufferedReader(new InputStreamReader(url.openStream()));
      MySeriesLogger.logger.log(Level.FINE, "Internet connection established");
      return true;
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.WARNING, "No internet connection");
      return false;
    }
  }

  /**
   * Unescape string for getting it DB
   * @param str The string to escape
   * @return
   */
  public static String unescapeString(String str) {
    str = str.replaceAll("''", "'");

    return str;
  }

  /**
   * Prompt user for input
   * @param title The window title
   * @param message The message to display
   * @return The user's input
   */
  public static String getInput(String title, String message) {
    //String answer = JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE);
    return MyMessages.ask(title, message);
  }

  /**
   * Get today in the givven format
   * @param dateFormat The date format to use
   * @return Today in the given format
   */
  public static String getToday(String dateFormat) {
    MySeriesLogger.logger.log(Level.INFO, "Getting today");
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    String today = sdf.format(cal.getTime());
    MySeriesLogger.logger.log(Level.FINE, "Today : {0}", today);
    return today;

  }

  /**
   * Check if the given string is a valid URL
   * @param str The string to check
   * @return true if it is or false if it is not
   */
  public static boolean isLink(String str) {
    try {
      MySeriesLogger.logger.log(Level.INFO, "Checking if {0} is link", str);
      URL url = new URL(str.trim());
      MySeriesLogger.logger.log(Level.FINE, "{0} is link", str);
      return true;
    } catch (MalformedURLException ex) {
      MySeriesLogger.logger.log(Level.WARNING, "{0} is not a link", str);
      return false;
    }
  }

  /**
   * Check if given string is a number
   * @param string The string to check
   * @return true if it is or false if it is not
   */
  public static boolean isNumeric(String string) {
    try {
      long l = Long.parseLong(string.trim());
    } catch (NumberFormatException ex) {
      return false;
    }
    return true;
  }

  /**
   * Check if a given String is a date in the format YY-MM-DD
   * @param date The string to check
   * @return true if it is or false if it is not
   */
  public static boolean isValidDate(String date) {
    MySeriesLogger.logger.log(Level.INFO, "Checking valid date {0}", date);
    if (date == null) {
      return false;
    }
    SimpleDateFormat mySQLDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat userDateFormat = new SimpleDateFormat(Options.toString(Options.DATE_FORMAT));

    mySQLDateFormat.setLenient(false);
    try {
      //parse the inDate parameter with mysql format
      mySQLDateFormat.parse(date.trim());
      if (date.trim().length() != mySQLDateFormat.toPattern().length()) {
        MySeriesLogger.logger.log(Level.WARNING, "Wrong date format : {0}", date);
        return false;
      }
    } catch (ParseException pe) {
      try {
        userDateFormat.parse(date.trim());
      } catch (ParseException ex1) {
        MySeriesLogger.logger.log(Level.WARNING, "Parse exception");
        return false;
      }
    }
    return true;
  }

  /**
   * Left pads a string
   * @param str The string to pad
   * @param length The desired length of the string
   * @param padStr Which string to use for the padding
   * @return The padded string
   */
  public static String padLeft(String str, int length, String padStr) {

    if (str.length() >= length) {
      return str;
    }
    str = padStr + str;
    while (str.length() < length) {
      str += padStr + str;
    }

    return str;
  }

  /**
   * Left pads an integer
   * @param i The integer to pad
   * @param length The desired length of the string
   * @param padStr Which string to use for the padding
   * @return The padded string
   */
  public static String padLeft(int i, int length, String padStr) {
    String str = String.valueOf(i);

    return padLeft(str, length, padStr);
  }

  /**
   * Copies a file
   * @param srFile The source file
   * @param dtFile The destination file
   * @throws java.io.FileNotFoundException
   * @throws java.io.IOException
   * @return true if succedded copying
   */
  public static boolean copyfile(String srFile, String dtFile) throws FileNotFoundException, IOException {
    if (MySeriesLogger.logger != null) {
      MySeriesLogger.logger.log(Level.INFO, "Copying file {0} to {1}", new String[]{srFile, dtFile});
    }
    File f1 = new File(srFile);
    File f2 = new File(dtFile);
    InputStream in = new FileInputStream(f1);
    OutputStream out = new FileOutputStream(f2);
    try {
      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
    } finally {
      in.close();
      out.close();
    }
    return true;
  }

  /**
   * Prompts the user to select a file form a drop down menu
   * @param dir The directory of the files to choose from
   * @param filter The filter to use
   * @param title The title
   * @param message The message
   * @return
   */
  public static String getSelectedFile(String dir, String filter[], String title, String message) {
    String[] files;
    Object name;
    final String[] fileFilter = filter;
    File directory = new File(dir);
    String[] fields;
    MySeriesLogger.logger.log(Level.INFO, "Prompt file selection interface dir {0}", dir);
    if (directory.isDirectory()) {
      files = directory.list(new FilenameFilter() {

        @Override
        public boolean accept(File dir, String name) {
          for (int i = 0; i < fileFilter.length; i++) {
            if (name.endsWith(fileFilter[i])) {
              return true;
            }

          }
          return false;
        }
      });

      if (files.length == 0) {
        MyMessages.warning("Select File", "No files to select", true, true);
        MySeriesLogger.logger.log(Level.WARNING, "No files in the directory");
        System.exit(0);
      } else {
        return (String) MyMessages.ask(title, message, null, files, Options.toString(Options.DB_NAME), true);
      }

      return null;
    } else {
      MySeriesLogger.logger.log(Level.WARNING, "{0} is not a directory", dir);
      return null;
    }
  }

  /**
   * Cretates a buffered reader for reading a file
   * @param file The file to read
   * @return The BufferedReader
   * @throws java.io.FileNotFoundException
   */
  public static BufferedReader createInputStream(File file) throws FileNotFoundException {
    return new BufferedReader(new FileReader(file));
  }

  /**
   * Creates a printer writer of a file to write to
   * @param file The file to write to
   * @param append if the file exists append the new text or truncate it first
   * @return The PrinterWriter
   * @throws java.io.IOException
   */
  public static PrintWriter createOutputStream(File file, boolean append) throws IOException {
    return new PrintWriter(new BufferedWriter(new FileWriter(file, append)));
  }

  /**
   * Initializes internet connection settings
   */
  public static void initInternetConnection() {
    if (Options.toBoolean(Options.USE_PROXY)) {
      MySeriesLogger.logger.log(Level.INFO,
          "Initializing internet connection with proxy [host:{0},port:{1}]",
          new String[]{Options.toString(Options.PROXY_HOST), Options.toString(Options.PROXY_PORT)});
      System.setProperty("http.proxyHost", Options.toString(Options.PROXY_HOST));
      System.setProperty("http.proxyPort", Options.toString(Options.PROXY_PORT));
    } else {
      MySeriesLogger.logger.log(Level.INFO, "Initializing internet connection");
      System.setProperty("http.proxyHost", "");
      System.setProperty("http.proxyPort", "80");

    }
  }

  /**
   * Strips a string of its HTML elements
   * @param str The string to strip
   * @return
   */
  public static String stripHTML(String str) {
    return str.replaceAll("\\<.*?>", "");

  }

  /**
   * Gets the directory of the jar file
   * @param main The applications main method
   * @return The path to the jar file
   */
  public static String getJarDir(Object main) {
    File p = new File(main.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
    if (p.getParent().endsWith("build")) {
      return p.getParent().replace("build", "").replace("%20", " ");
    }
    return p.getParent().replace("%20", " ") + "/";
  }

  /**
   * Checks if an episode has been eired
   * @param aired The date that the episode is aired (yyyy-mm-dd or dd/mm/yyyy)
   * @param includeToday
   * @return True if it's aired or false
   */
  public static boolean hasBeenAired(String aired, boolean includeToday) {
    MySeriesLogger.logger.log(Level.INFO, "Checking if an episode has been aired. Aired date : {0}", aired);
    if (aired.length() != 10) {
      MySeriesLogger.logger.log(Level.WARNING, "Wrong aired date : {0}", aired);
      return false;
    }

    //Check aired format
    if (aired.substring(2, 3).equals("/")) {
      String[] d = aired.split("/");
      aired = d[2] + "-" + d[1] + "-" + d[0];
    }
    if (!isValidDate(aired)) {
      MySeriesLogger.logger.log(Level.WARNING, "Invalid date {0}", aired);
      return false;
    }

    if (includeToday) {
      if (aired.compareTo(getToday("yyyy-MM-dd")) <= 0) {
        MySeriesLogger.logger.log(Level.FINE, "Episode has been aired (including today)");
        return true;
      }
      MySeriesLogger.logger.log(Level.INFO, "Episode has not been aired (including today)");
      return false;
    } else {
      if (aired.compareTo(getToday("yyyy-MM-dd")) < 0) {
        MySeriesLogger.logger.log(Level.FINE, "Episode has been aired (not including today)");
        return true;
      }
      MySeriesLogger.logger.log(Level.INFO, "Episode has not been aired (not including today)");
      return false;
    }

  }

  /**
   * Creates the regex for finding episodes
   * @param season The episode's season
   * @param episode The episode's number
   * @return The regular expression
   */
  public static String createRegex(int season, int episode) {
    return "\\D*" + season + Options._REGEX_ + episode + "\\D";
  }

  /**
   * Creates a random string
   * @param length The length of the string
   * @return The random string
   */
  public static String createRandomString(int length) {
    Random random = new Random();
    StringBuilder sb = new StringBuilder();
    while (sb.length() < length) {
      sb.append(Integer.toHexString(random.nextInt()));
    }
    return sb.toString();
  }

  /**
   * Joins an iterable with a seperator
   * @param iter The iterable object
   * @param separator The separator to use
   * @return The joined String
   */
  public static String join(Iterable<? extends Object> iter, String separator) {
    Iterator<? extends Object> oIter;
    if (iter == null || (!(oIter = iter.iterator()).hasNext())) {
      return "";
    }
    StringBuilder oBuilder = new StringBuilder(String.valueOf(oIter.next()));
    while (oIter.hasNext()) {
      oBuilder.append(separator).append(oIter.next());
    }
    return oBuilder.toString();
  }

  /**
   * Joins an iterable with a seperator
   * @param iter The iterable object
   * @param separator The separator to use
   * @return The joined String
   */
  public static String join(String[] array, String separator) {
    List<String> iter = Arrays.asList(array);
    return join(iter, separator);
  }

  /**
   * Deletes double spaces from a string
   * @param title The string to delete double spaces
   * @return The string without double spaces
   */
  public static String deleteDoubleSpaces(String title) {
    while (title.indexOf("  ") > -1) {
      title = title.replaceAll("  ", " ");
    }
    return title.trim();
  }

  /**
   * Gets the font to use in the title field of the episodes table
   * @param defaultFont The default font
   * @param downloaded If the episode is downloaded
   * @param seen If the episode is seen
   * @param sub The sub language of the episode
   * @return The font to use
   */
  public static Font getCellFont(Font defaultFont, boolean downloaded, boolean seen, Language sub) {
    Font font = defaultFont;
    if (seen) {
      font = font.deriveFont(Font.ITALIC, defaultFont.getSize());
    } else if (!seen && downloaded && !sub.equals(LangsList.NONE)) {
      font = font.deriveFont(Font.BOLD, defaultFont.getSize() + MyFont.SIZE_STEP);
    }
    return font;
  }

  /**
   * Gets the font color to use in the title field of the episodes table
   * @param isSelected If the row of the cell is selected
   * @param seen If the episode is seen
   * @return The color to use
   */
  public static Color getCellColor(Component c, boolean isSelected, boolean seen, String date, boolean downloaded) {
    if (isSelected) {
      return Skin.getColor_1();
    } else {
      if (seen) {
        return Skin.getColor_4();
      } else {
        if (MyUsefulFunctions.hasBeenAired(date, false) && !downloaded) {
          Color b = c.getBackground();
          return new Color(255 - b.getRed(), 255 - b.getGreen(), 255 - b.getBlue());
        } else {
          return Skin.getColor_5();
        }
      }
    }
  }

  /**
   * Check if needed directories exist and if not create them
   * @param dirPath The directory to check
   */
  public static void checkDir(String dirPath) {
    if (MySeriesLogger.logger != null) {
      MySeriesLogger.logger.log(Level.INFO, "Checking need for creation of dir {0}", dirPath);
    }
    if (!new File(dirPath).isDirectory()) {
      if (new File(dirPath).mkdir()) {
        if (MySeriesLogger.logger != null) {
          MySeriesLogger.logger.log(Level.INFO, "Created directory {0}", dirPath);
        }
      } else {
        if (MySeriesLogger.logger != null) {
          MySeriesLogger.logger.log(Level.SEVERE, "Could not create directory {0}", dirPath);
        }
      }
    }
    if (MySeriesLogger.logger != null) {
      MySeriesLogger.logger.log(Level.INFO, "No need for creating the directory {0}", dirPath);
    }
  }

  /**
   * Checks if a value is in an array
   * @param array The array to check against
   * @param value The value to search
   * @return True if value is in array else false
   */
  public static boolean isInArray(Object[] array, Object value) {
    if (array == null) {
      return false;
    }
    for (int i = 0; i < array.length; i++) {
      if (array[i] != null && array[i].equals(value)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if a value is in an array
   * @param array The array to check against
   * @param value The value to search
   * @return True if value is in array else false
   */
  public static boolean isInArray(String[] array, String value) {
    if (array == null) {
      return false;
    }
    for (int i = 0; i < array.length; i++) {
      if (array[i].toLowerCase().equals(value.toLowerCase())) {
        return true;
      }
    }
    return false;
  }

  /**
   * List an array elements
   * @param array The array to list
   * @param newLine If new line should be used
   * @return The string
   */
  public static String listArray(Object[] array, boolean newLine) {
    String list = "";
    for (int i = 0; i < array.length; i++) {
      String name;
      if (array[i] instanceof File) {
        name = ((File) array[i]).getName();
      } else {
        name = String.valueOf(array[i]);
      }

      list += name + (newLine ? "\n" : ", ");
    }
    return list.substring(0, list.length() - (newLine ? 1 : 2));
  }

  public static String getVideoFileSize(EpisodesRecord episode) {
    String size = "";
    SeriesRecord series;
    try {
      series = SeriesRecord.queryOne(SeriesRecord.C_SERIES_ID + "=?",
          new String[]{String.valueOf(episode.getSeries_ID())}, null);
    } catch (SQLException ex) {
      return null;
    }
    if (new File(series.getLocalDir()).isDirectory()) {
      File video = MyUsefulFunctions.getVideoFile(series, episode);
      if (video != null) {
        return " (" + createFileSize(video.length()) + ")";
      }
    }
    return size;
  }

  private static File getVideoFile(SeriesRecord series, EpisodesRecord episode) {
    MySeriesLogger.logger.log(Level.INFO, "Getting video file for series {0} and episode {1} ",
        new String[]{series.getFullTitle(), episode.getTitle()});
    String regex = MyUsefulFunctions.createRegex(series.getSeason(), episode.getEpisode());
    String regexFake = MyUsefulFunctions.createRegex(series.getSeason(), series.getSeason() * 10 + episode.getEpisode());
    File[] videoFiles = Series.getVideoFiles(series);
    Pattern pattern = Pattern.compile(regex);
    Pattern patternFake = Pattern.compile(regexFake);
    for (int j = 0; j < videoFiles.length; j++) {
      File file = videoFiles[j];
      Matcher matcher = pattern.matcher(file.getName());
      Matcher matcherFake = patternFake.matcher(file.getName());
      if (matcher.find() && !matcherFake.find()) {
        MySeriesLogger.logger.log(Level.FINE, "Found video file {0}", file.getName());
        return file;
      }
    }
    MySeriesLogger.logger.log(Level.INFO, "No video files found");
    return null;
  }

  private static ArrayList<File> getVideoFiles(SeriesRecord series, EpisodesRecord episode) {
    MySeriesLogger.logger.log(Level.INFO, "Getting video files for series {0} and episode {1} ",
        new String[]{series.getFullTitle(), episode.getTitle()});

    String regex = MyUsefulFunctions.createRegex(series.getSeason(), episode.getEpisode());
    String regexFake = MyUsefulFunctions.createRegex(series.getSeason(), series.getSeason() * 10 + episode.getEpisode());
    ArrayList<File> files = new ArrayList<File>();
    File[] videoFiles = Series.getVideoFiles(series);
    Pattern pattern = Pattern.compile(regex);
    Pattern patternFake = Pattern.compile(regexFake);
    if (videoFiles == null) {
      MySeriesLogger.logger.log(Level.INFO, "No video files found");
      return new ArrayList<File>();
    }
    for (int j = 0; j < videoFiles.length; j++) {
      File file = videoFiles[j];
      Matcher matcher = pattern.matcher(file.getName());
      Matcher matcherFake = patternFake.matcher(file.getName());
      if (matcher.find() && !matcherFake.find()) {
        MySeriesLogger.logger.log(Level.FINE, "Found video file {0}", file.getName());
        files.add(file);
      }
    }
    MySeriesLogger.logger.log(Level.FINE, "Found {0} video files", files.size());
    return files;
  }

  public static String[] getVideoFileTypes(EpisodesRecord ep) {
    SeriesRecord series;
    MySeriesLogger.logger.log(Level.INFO, "Getting video file types for episode : {0}", ep.getTitle());
    try {
      series = SeriesRecord.queryOne(SeriesRecord.C_SERIES_ID + "=?",
          new String[]{String.valueOf(ep.getSeries_ID())}, null);
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "SQL exception while getting series " + ep.getSeries_ID(), ex);
      return null;
    }
    ArrayList<File> videos = new ArrayList<File>();
    if (series.isValidLocalDir() && Options.toBoolean(Options.AUTO_FILE_UPDATING)) {
      videos = MyUsefulFunctions.getVideoFiles(series, ep);
      if (videos.isEmpty()) {
        return null;
      }
    } else {
      return null;
    }
    String[] types = new String[videos.size()];
    int i = 0;
    for (Iterator<File> it = videos.iterator(); it.hasNext();) {
      String file = it.next().getName();
      String type = file.substring(file.length() - 3, file.length());

      if (file.indexOf(MyDownloadedCellRenderer.SAMPLE) > -1) {
        types[i++] = MyDownloadedCellRenderer.SAMPLE;
      } else if (type.equals(MyDownloadedCellRenderer.AVI)) {
        types[i++] = MyDownloadedCellRenderer.AVI;
      } else if (type.equals(MyDownloadedCellRenderer.MKV)) {
        types[i++] = MyDownloadedCellRenderer.MKV;
      } else if (type.equals(MyDownloadedCellRenderer.MPG)) {
        types[i++] = MyDownloadedCellRenderer.MPG;
      } else if (type.equals(MyDownloadedCellRenderer.MP4)) {
        types[i++] = MyDownloadedCellRenderer.MP4;
      } else {
        types[i++] = MyDownloadedCellRenderer.OTHER;
      }
    }
    MySeriesLogger.logger.log(Level.FINE, "Found {0} types ({1})",
        new Object[]{types.length, MyUsefulFunctions.listArray(types, false)});
    return types;
  }

  private static ArrayList<File> getSubtitles(SeriesRecord series, EpisodesRecord episode) {
    MySeriesLogger.logger.log(Level.INFO, "Getting subtitles for {0} episode {1}",
        new String[]{series.getFullTitle(), episode.getTitle()});
    String regex = MyUsefulFunctions.createRegex(series.getSeason(), episode.getEpisode());
    String regexFake = MyUsefulFunctions.createRegex(series.getSeason(), series.getSeason() * 10 + episode.getEpisode());
    ArrayList<File> subs = new ArrayList<File>();
    File[] subtitles = Series.getSubtitleFiles(series);
    Pattern pattern = Pattern.compile(regex);
    Pattern patternFake = Pattern.compile(regexFake);
    if (subtitles == null) {
      MySeriesLogger.logger.log(Level.INFO, "No subtitles found");
      return subs;
    }
    for (int j = 0; j < subtitles.length; j++) {
      File file = subtitles[j];
      Matcher matcher = pattern.matcher(file.getName());
      Matcher matcherFake = patternFake.matcher(file.getName());
      if (matcher.find() && !matcherFake.find()) {
        if (MyUsefulFunctions.isSubtitle(file)) {
          subs.add(file);
        }
      }
    }
    MySeriesLogger.logger.log(Level.FINE, "Found {0} subtitles", subs);
    return subs;
  }

  public static String[][] getSubtitleLangs(EpisodesRecord ep) {
    MySeriesLogger.logger.log(Level.INFO, "Getting subtitle langs episode {1}",
        ep.getTitle());
    SeriesRecord series;
    try {
      series = SeriesRecord.queryOne(SeriesRecord.C_SERIES_ID + "=?",
          new String[]{String.valueOf(ep.getSeries_ID())}, null);
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "SQL exception while getting series " + ep.getSeries_ID(), ex);
      return null;
    }
    ArrayList<File> subtitles = new ArrayList<File>();
    if (series.isValidLocalDir() && Options.toBoolean(Options.AUTO_FILE_UPDATING)) {
      subtitles = MyUsefulFunctions.getSubtitles(series, ep);
    } else {
      return null;
    }
    if (subtitles.isEmpty()) {
      return null;
    }
    String[][] types = new String[subtitles.size()][2];
    int i = -1;
    for (Iterator<File> it = subtitles.iterator(); it.hasNext();) {
      String name = it.next().getName();
      String[] tokens = name.split("\\.", -1);
      String ext = tokens[tokens.length - 1];
      if (tokens.length < 3) {
        i++;
        types[i][0] = MySubtitlesCellRenderer.OTHER;
        types[i][1] = ext;
      } else {
        String lan = tokens[tokens.length - 2];
        if (LangsList.isLanguageCode(lan)) {
          i++;
          types[i][0] = lan;
          types[i][1] = ext;
        } else {
          i++;
          types[i][0] = MySubtitlesCellRenderer.OTHER;
          types[i][1] = ext;
        }
      }
    }
    MySeriesLogger.logger.log(Level.FINE, "Found {0} types",
        types.length);
    return types;
  }

  public static String createFileSize(long value) {
    String strValue = String.valueOf(value);
    if (value < 1024) {
      strValue = strValue + " bytes";
    } else if (value < Math.pow(1024l, 2)) {
      strValue = Options._DEC_FORMAT_.format(value / 1024) + " KB";
    } else if (value < Math.pow(1024l, 3)) {
      strValue = Options._DEC_FORMAT_.format(value / Math.pow(1024, 2)) + " MB";
    } else if (value < Math.pow(1024l, 4)) {
      strValue = Options._DEC_FORMAT_.format(value / Math.pow(1024, 3)) + " GB";
    } else if (value < Math.pow(1024l, 5)) {
      strValue = Options._DEC_FORMAT_.format(value / Math.pow(1024, 4)) + " TB";
    }
    return strValue;
  }

  public static boolean isNetworkPath(File directory) {
    if (directory.toURI().toString().startsWith("file:////")) {
      return true;
    }
    return false;
  }

  public static boolean needRenaming(EpisodesRecord ep) {
    SeriesRecord series;
    try {
      series = SeriesRecord.queryOne(SeriesRecord.C_SERIES_ID + "=?",
          new String[]{String.valueOf(ep.getSeries_ID())}, null);
      ArrayList<File> videos = new ArrayList<File>();
      ArrayList<File> subs = new ArrayList<File>();
      if (series.isValidLocalDir()) {
        MySeriesLogger.logger.log(Level.INFO, "Check if episodes need renaming {0}", ep.getTitle());
        videos = getVideoFiles(series, ep);
        subs = getSubtitles(series, ep);
        if (videos.isEmpty() || subs.isEmpty()) {
          MySeriesLogger.logger.log(Level.INFO, "No video or subtitle files");
          return false;
        } else {
          return !areVideoAndSubsRenamed(videos, subs);
        }
      } else {
        return false;
      }
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
      return false;
    }

  }

  private static boolean areVideoAndSubsRenamed(ArrayList<File> videos, ArrayList<File> subs) {
    boolean[] results = new boolean[videos.size()];
    int i = 0;
    for (Iterator<File> it = videos.iterator(); it.hasNext();) {
      File video = it.next();
      String videoBaseName = getBaseName(video, VIDEO_FILE).replace("_sample", "");
      for (Iterator<File> it1 = subs.iterator(); it1.hasNext();) {
        File sub = it1.next();
        String subBaseName = getBaseName(sub, SUBTITLE_FILE);
        if (videoBaseName.equals(subBaseName)) {
          results[i] = true;
        }
      }
      i++;
    }
    for (int j = 0; j < results.length; j++) {
      boolean b = results[j];
      if (!b) {
        MySeriesLogger.logger.log(Level.INFO, "Episode needs renaming");
        return false;
      }
    }
    MySeriesLogger.logger.log(Level.INFO, "Episode does not need renaming");
    return true;
  }

  private static String getBaseName(File file, int type) {
    String basename = "";
    if (file == null || file.isDirectory()) {
      return "";
    }
    String name = file.getName();
    MySeriesLogger.logger.log(Level.INFO, "Getting base name of file {0}", file);
    if (type == VIDEO_FILE) {
      basename = name.substring(0, name.lastIndexOf("."));
    } else {
      // when theres no lang string interface name
      if (name.matches(".*\\..{2}\\..{3}")) {
        basename = name.substring(0, name.length() - 7);
      } else {
        basename = name.substring(0, name.length() - 4);
      }
    }
    MySeriesLogger.logger.log(Level.FINE, "Basename is : {0}", basename);
    return basename;
  }

  /**
   * Opens a uri in the browser
   * @param uri  The uri to browse
   */
  public static void browse(URI uri) {
    if (DesktopSupport.isDesktopSupport()) {
      try {
        MySeriesLogger.logger.log(Level.INFO, "Browsing URI : {0}", uri);
        DesktopSupport.getDesktop().browse(uri);
      } catch (IOException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "IO exception", ex);
      }
    } else {
      MyMessages.warning("Open browser", "Your OS doesn't support opening a browser window", true, true);
    }
  }

  /**
   * Opens  a  url in the browser
   * @param url The url
   */
  public static void browse(URL url) {
    try {
      MySeriesLogger.logger.log(Level.INFO, "Browsing URL : {0}", url);
      URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
      browse(uri);
    } catch (URISyntaxException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Wrong uri " + url, ex);
      MyMessages.warning("Open browser", url + "\nThis is not a valid url", true, true);
    }
  }

  /**
   * Browse a url
   * @param url The url
   */
  public static void browse(String strUrl) {
    try {
      URL url = new URL(strUrl);
      URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
      browse(uri);
    } catch (MalformedURLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Wrong url " + strUrl, ex);
      MyMessages.warning("Open browser", strUrl + "\nThis is not a valid url", true, true);
    } catch (URISyntaxException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Wrong url " + strUrl, ex);
      MyMessages.warning("Open browser", strUrl + "\nThis is not a valid url", true, true);
    }
  }

  public static void removeDuplicate(ArrayList<Object> arlList) {
    HashSet<Object> h = new HashSet<Object>(arlList);
    arlList.clear();
    arlList.addAll(h);
  }

  public static String getBaseUrl(String url) {
    try {
      MySeriesLogger.logger.log(Level.INFO, "Getting base url of {0}", url);
      URL u = new URL(url);
      return u.getHost();
    } catch (MalformedURLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Wrong url", ex);
      return url;
    }
  }

  public static boolean isNoticableRss(String title) {
    try {
      ArrayList<SeriesRecord> series = Series.getSeries(false);
      MySeriesLogger.logger.log(Level.INFO, "Check if feed {0} is a noticable feed", title);
      for (Iterator<SeriesRecord> it = series.iterator(); it.hasNext();) {
        SeriesRecord seriesRecord = it.next();
        String sTitle = seriesRecord.getTitle();
        String regex = "\\b" + sTitle.toLowerCase() + "\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(title.toLowerCase());
        if (matcher.find()) {
          MySeriesLogger.logger.log(Level.FINE, "Noticable url");
          return true;
        }
      }
      return false;
    } catch (SQLException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "Sql exception", ex);
      return false;
    }
  }

  public static boolean isSubtitle(Object obj) {
    String filename = null;
    File file = null;
    if (obj == null) {
      return false;
    }
    //EntryName
    if (obj instanceof String) {
      filename = (String) obj;
    } else if (obj instanceof File) {
      file = (File) obj;
      filename = ((File) obj).getName();
    }
    MySeriesLogger.logger.log(Level.INFO, "Checking if {0} is subtitle", filename);
    int p = filename.lastIndexOf(".");
    String ext = filename.substring(p + 1);
    ArrayList<String> entries;
    if (isInArray(SubtitleConstants.EXTENSIONS, ext)) {
      MySeriesLogger.logger.log(Level.FINE, "{0} is a subtitle", filename);
      return true;
    } else if (isInArray(SubtitleConstants.ZIP_EXT, ext)) {
      if (file != null) {
        ArchiveFile z = new ArchiveFile(file);
        try {
          entries = z.getEntries(ArchiveConstants.SUBTITLES);
          return entries != null && !entries.isEmpty();
        } catch (Exception ex) {
          MySeriesLogger.logger.log(Level.SEVERE, "Could not get entries from archive " + file.getName(), ex);
          return false;
        }
      }
      return false;
    } else {
      MySeriesLogger.logger.log(Level.INFO, "{0} is not a subtitle", filename);
      return false;
    }

  }

  public static String sanitize(String str) {
    return str.replaceAll("[\\\\ /:*?\"<>|]", "_");
  }

  public static String getExtension(File file) {
    return getExtension(file.getAbsolutePath());
  }

  public static String getExtension(String file) {
    String[] tok = file.split("\\.");
    return tok[tok.length - 1].toLowerCase();
  }

  public static String getRenamedEpisode(File file, SeriesRecord series, EpisodesRecord episode) {
    String filename = file.getName();
    String[] tokens = filename.split("\\.", -1);
    String ext = tokens[tokens.length - 1];
    if (MyUsefulFunctions.isSubtitle(file)) {
      if (isLanguage(tokens[tokens.length - 2])) {
        ext = tokens[tokens.length - 2] + "." + tokens[tokens.length - 1];
      }
    }
    String sample = "";
    if (filename.indexOf("sample") > -1) {
      sample = "_sample";
    }
    return series.getTitle()
        + Options.toString(Options.SEASON_SEPARATOR, false) + MyUsefulFunctions.padLeft(series.getSeason(), 2, "0")
        + Options.toString(Options.EPISODE_SEPARATOR, false) + MyUsefulFunctions.padLeft(episode.getEpisode(), 2, "0")
        + Options.toString(Options.TITLE_SEPARATOR, false) + episode.getTitle() + sample + "." + ext;
  }

  public static boolean renameEpisode(SeriesRecord series, String filename) {
    MySeriesLogger.logger.log(Level.INFO, "renaming episode {0}", filename);
    String path = series.getLocalDir();
    File oldFile = new File(path + "/" + filename);
    String[] numbers = filename.split("\\D+");
    int episode = -1;
    for (int i = 0; i < numbers.length; i++) {
      String n = numbers[i];
      if (MyUsefulFunctions.isNumeric(n)
          && Integer.parseInt(n) == series.getSeason()
          && i < numbers.length - 1) {
        episode = MyUsefulFunctions.isNumeric(numbers[i + 1])
            ? Integer.parseInt(numbers[i + 1])
            : -1;
      }
    }
    if (episode > -1) {
      ResultSet rs = null;
      try {
        rs = EpisodesRecord.query("SELECT * FROM episodes WHERE series_ID =" + series.getSeries_ID() + " AND episode = " + episode);
        while (rs.next()) {
          EpisodesRecord e = new EpisodesRecord();
          e.setSeries_ID(rs.getInt("series_ID"));
          e.setEpisode_ID(rs.getInt("episode_ID"));
          e.setTitle(rs.getString("title"));
          e.setAired(rs.getString("aired"));
          episode = rs.getInt("episode");
          e.setEpisode(rs.getInt("episode"));
          e.setDownloaded(rs.getInt("downloaded"));
          e.setSeen(rs.getInt("seen"));
          e.setRate(rs.getDouble("rate"));
          e.setSubs(LangsList.getLanguageById(rs.getInt("subs")));
          String newName = getRenamedEpisode(oldFile, series, e);
          File newFile = new File(path + "/" + newName);
          oldFile.renameTo(newFile);
          MySeriesLogger.logger.log(Level.FINE, "Episode renamed");
          return true;
        }
      } catch (SQLException ex) {
        MySeriesLogger.logger.log(Level.SEVERE, "Sql error occured", ex);
      } finally {
        try {
          rs.close();
        } catch (SQLException ex) {
          MySeriesLogger.logger.log(Level.SEVERE, null, ex);
        }
      }
    }
    return false;
  }

  private static boolean isLanguage(String lang) {
    MySeriesLogger.logger.log(Level.INFO, "Checking if {0} is a language", lang);
    ArrayList<Language> langs = myseries.MySeries.languages.getLangs();
    for (Iterator<Language> it = langs.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language.getCode().equals(lang)) {
        MySeriesLogger.logger.log(Level.FINE, "Found language {0}", language);
        return true;
      }
    }
    MySeriesLogger.logger.log(Level.WARNING, "Did not find a language for {0}", lang);
    return false;
  }

  /**
   * Run external program
   * @param command
   * @param startingFolder
   * @throws IOException
   */
  public static void runExternalProgram(String[] command, File startingFolder) throws IOException {
    String[] envp = null; // should inherit the environment
    Process p = Runtime.getRuntime().exec(command, envp, startingFolder);
  }

  public static boolean isInList(Object sc, Collection<?> col) {
    for (Iterator<?> it = col.iterator(); it.hasNext();) {
      Object o = it.next();
      if (o.equals(sc)) {
        return true;
      }
    }
    return false;
  }

  public static void feedUpdater(MySeries m) {
    if (Options.toInt(Options.FEED_UPDATE_FREQUENCY) > 0) {
      int fr = Options.toInt(Options.FEED_UPDATE_FREQUENCY) * 60 * 1000;
      m.feedsTimer = new Timer(fr, new MyFeedsTimerListener(m));
      m.feedsTimer.start();
    } else {
      m.feedsTimer.stop();
    }
  }

  public static void createMemoryCons(MySeries m) {

    MySeriesLogger.logger.log(Level.INFO, "Creating the timer for memory consumption");
    if (Options.toInt(Options.MEMORY_CONSUMPTION_UPDATE) > 0) {
      m.myToolbar.addButton(AbstractToolbar.MEMORY);
      ToolbarSeperator mem = getMemoryToolbarSeperator(m);
      m.memoryTimer = new Timer(Options.toInt(Options.MEMORY_CONSUMPTION_UPDATE), new MyMemoryTimerListener(mem, m.myToolbar.getOrientation()));
      MySeriesLogger.logger.log(Level.INFO, "Starting timer");
      m.memoryTimer.start();
    } else {
      MySeriesLogger.logger.log(Level.INFO, "Stopping timer");
      m.memoryTimer.stop();
      m.myToolbar.removeButton(AbstractToolbar.MEMORY);
    }
  }

  private static ToolbarSeperator getMemoryToolbarSeperator(MySeries m) {
    MySeriesLogger.logger.log(Level.INFO, "Getting the memory toolbar component");
    String Dummy = "88,888 MB (88%)";
    Component[] comps = m.myToolbar.getComponents();
    for (int i = 0; i < comps.length; i++) {
      Component component = comps[i];
      if (component instanceof ToolbarSeperator) {
        ToolbarSeperator t = (ToolbarSeperator) component;
        if (t.getActionName() == ToolbarButtonActions.MEMORY) {
          MySeriesLogger.logger.log(Level.FINE, "Component found");
          MySeriesLogger.logger.log(Level.INFO, "Customizing component");
          t.setOpaque(true);
          // t.setBounds(0, 0, 120, 32);
          if (m.myToolbar.getOrientation() == SwingConstants.HORIZONTAL) {
            FontMetrics fm = t.getFontMetrics(new Font(Options.toString(Options.FONT_FACE), Font.BOLD, (int) Options.toFloat(Options.FONT_SIZE)));
            int width = fm.stringWidth(Dummy);
            t.setMinimumSize(new Dimension(width + 40, 26));
            t.setPreferredSize(new Dimension(width + 40, 26));
            t.setMaximumSize(new Dimension(width + 40, 26));
          } else {
            t.setMinimumSize(new Dimension(26, 26));
            t.setPreferredSize(new Dimension(26, 26));
            t.setMaximumSize(new Dimension(26, 26));
          }
          t.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
          return t;
        }
      }
    }
    MySeriesLogger.logger.log(Level.WARNING, "Component not found");
    return null;
  }

  public static void sendMail(URI email) throws IOException {
    if (Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
      Desktop.getDesktop().mail(email);
    } else {
      throw new UnsupportedOperationException("Sending mail does not supported by the OS");
    }
  }

  public static String capitalizeString(String string) {
    char[] chars = string.toLowerCase().toCharArray();
    boolean found = false;
    for (int i = 0; i < chars.length; i++) {
      if (!found && Character.isLetter(chars[i])) {
        chars[i] = Character.toUpperCase(chars[i]);
        found = true;
      } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
        found = false;
      }
    }
    return String.valueOf(chars);
  }

  public static int getTitleCellIcon(EpisodesRecord ep) {
    boolean aired = MyUsefulFunctions.hasBeenAired(ep.getAired(), false);
    boolean downloaded = ep.getDownloaded() == 1;
    boolean subs = !ep.getSubs().getName().equals(SubtitleConstants.NONE);
    boolean needRenaming = MyUsefulFunctions.needRenaming(ep);
    boolean watched = ep.getSeen() == 1;
    if (watched || !aired) {
      return MyTitleCellRenderer.NO_ICON;
    }
    if (!downloaded) {
      return MyTitleCellRenderer.NOT_DOWNLOADED;
    } else if (!subs) {
      return MyTitleCellRenderer.NO_SUBTITLES;
    } else if (needRenaming) {
      return MyTitleCellRenderer.NOT_RENAMED;
    } else if (!watched) {
      return MyTitleCellRenderer.NOT_SEEN;
    }
    return MyTitleCellRenderer.NOT_AIRED;
  }

  public static boolean isAllArrayElementsNull(String[] arr) {
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] != null) {
        return false;
      }
    }
    return true;
  }

  private MyUsefulFunctions() {
  }
}
