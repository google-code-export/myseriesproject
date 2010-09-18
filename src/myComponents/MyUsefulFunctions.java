/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents;

import database.EpisodesRecord;
import database.SeriesRecord;
import java.awt.Color;
import java.awt.Font;
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
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import myComponents.myGUI.MyFont;
import myComponents.myTableCellRenderers.MyDownloadedCellRenderer;
import myComponents.myTableCellRenderers.MySubtitlesCellRenderer;
import myseries.series.Series;
import tools.Skin;
import tools.languages.LangsList;
import tools.languages.Language;
import tools.options.Options;

/**
 * Useful functions helper class
 * @author lordovol
 */
public class MyUsefulFunctions {

  /**
   * Converts a date from the database (YYYY-MM-DD) to the Options._DATE_FORMAT_
   * @param date
   * @return
   */
  public static String convertDateForRendering(String date) {
    SimpleDateFormat sdf = new SimpleDateFormat(Options.toString(Options.DATE_FORMAT));
    try {
      Date dateD = sdf.parse(date);
      sdf = new SimpleDateFormat(EpisodesRecord.MYSQL_DATE_FORMAT);
      date = sdf.format(dateD);
    } catch (ParseException ex) {
    }
    if (date.equals("")) {
      return "";
    }
    try {
      String[] d = date.split("-", -1);

      if (d[1].equals("00") || d[2].equals("00")) {
        return "";
      }
    } catch (ArrayIndexOutOfBoundsException ex) {
    }
    try {
      DateFormat df = new SimpleDateFormat(EpisodesRecord.MYSQL_DATE_FORMAT);
      Date sDate = df.parse(date);
      SimpleDateFormat f = new SimpleDateFormat(Options.toString(Options.DATE_FORMAT));
      return f.format(sDate);
    } catch (ParseException ex) {
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
      DateFormat df = new SimpleDateFormat(Options.toString(Options.DATE_FORMAT));
      Date sDate = df.parse(date);
      SimpleDateFormat f = new SimpleDateFormat(EpisodesRecord.MYSQL_DATE_FORMAT);
      return f.format(sDate);
    } catch (ParseException ex) {
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
    try {
      URL url = new URL(address);
      in = new BufferedReader(new InputStreamReader(url.openStream()));
      return true;
    } catch (IOException ex) {
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
    String answer = JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE);
    return answer;
  }

  /**
   * Get today in the givven format
   * @param dateFormat The date format to use
   * @return Today in the given format
   */
  public static String getToday(String dateFormat) {

    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    return sdf.format(cal.getTime());

  }

  /**
   * Check if the given string is a valid URL
   * @param str The string to check
   * @return true if it is or false if it is not
   */
  public static boolean isLink(String str) {
    try {
      URL url = new URL(str.trim());
      return true;
    } catch (MalformedURLException ex) {
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
    if (date == null) {
      return false;
    }

    //set the format to use as a constructor argument
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    if (date.trim().length() != dateFormat.toPattern().length()) {
      return false;
    }

    dateFormat.setLenient(false);

    try {
      //parse the inDate parameter
      dateFormat.parse(date.trim());
    } catch (ParseException pe) {
      return false;
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
        MyMessages.error("Select File", "No files to select");
        System.exit(0);
      } else {
        name = JOptionPane.showInputDialog(null,
            message,
            title,
            JOptionPane.PLAIN_MESSAGE,
            null,
            files, files[0]);

        return String.valueOf(name);
      }

      return null;
    } else {
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
      Properties props = System.getProperties();
      props.put("http.proxyHost", Options.toString(Options.PROXY_HOST));
      props.put("http.proxyPort", Options.toString(Options.PROXY_PORT));
      System.setProperties(props);
    } else {
      Properties props = System.getProperties();
      props.put("http.proxyHost", "");
      props.put("http.proxyPort", "80");
      System.setProperties(props);
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
   * @param aired The date that the episode is aired
   * @return True if it's aired or false
   */
  public static boolean hasBeenAired(String aired) {
    if (aired.compareTo(getToday("yyyy-MM-dd")) <= 0) {
      return true;
    }
    return false;
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
  public static Color getCellColor(boolean isSelected, boolean seen, String date, boolean downloaded) {
    if (isSelected) {
      return Skin.getColor_1();
    } else {
      if (seen) {
        return Skin.getColor_4();
      } else {
        if(MyUsefulFunctions.hasBeenAired(date) && !downloaded){
          return Color.RED;
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
    if (!new File(dirPath).isDirectory()) {
      if (new File(dirPath).mkdir()) {
        myseries.MySeries.logger.log(Level.INFO, "Created directory " + dirPath);
      } else {
        myseries.MySeries.logger.log(Level.SEVERE, "Could not create directory " + dirPath);
      }
    }
  }

  /**
   * Checks if a value is in an array
   * @param array The array to check against
   * @param value The value to search
   * @return True if value is in array else false
   */
  public static boolean isInArray(Object[] array, Object value) {
    for (int i = 0; i < array.length; i++) {
      if (array[i].equals(value)) {
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
  public static String listAray(File[] array, boolean newLine) {
    String list = "";
    for (int i = 0; i < array.length; i++) {
      File file = array[i];
      list += file.getName() + (newLine ? "\n" : ", ");
    }
    return list.substring(0, list.length() - (newLine ? 1 : 2));
  }

  public static String getVideoFileSize(EpisodesRecord episode) {
    String size = "";
     SeriesRecord series;
    try {
      series = database.DBHelper.getSeriesByID(episode.getSeries_ID());
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
    String regex = MyUsefulFunctions.createRegex(series.getSeason(), episode.getEpisode());
    File[] videoFiles = Series.getVideoFiles(series);
    Pattern pattern = Pattern.compile(regex);
    for (int j = 0; j < videoFiles.length; j++) {
      File file = videoFiles[j];
      Matcher matcher = pattern.matcher(file.getName());
      if (matcher.find()) {
        return file;
      }
    }
    return null;
  }

  private static ArrayList<File> getVideoFiles(SeriesRecord series, EpisodesRecord episode) {
    String regex = MyUsefulFunctions.createRegex(series.getSeason(), episode.getEpisode());
    ArrayList<File> files = new ArrayList<File>();
    File[] videoFiles = Series.getVideoFiles(series);
    Pattern pattern = Pattern.compile(regex);
    if(videoFiles==null){
      return new ArrayList<File>();
    }
    for (int j = 0; j < videoFiles.length; j++) {
      File file = videoFiles[j];
      Matcher matcher = pattern.matcher(file.getName());
      if (matcher.find()) {
        files.add(file);
      }
    }
    return files;
  }

  public static String[] getVideoFileTypes(EpisodesRecord ep) {
     SeriesRecord series;
    try {
      series = database.DBHelper.getSeriesByID(ep.getSeries_ID());
    } catch (SQLException ex) {
      return null;
    }
    ArrayList<File> videos = new ArrayList<File>();
    if (series.isValidLocalDir() && Options.toBoolean(Options.AUTO_FILE_UPDATING)) {
      videos = MyUsefulFunctions.getVideoFiles(series, ep);
      if(videos.isEmpty()){
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
      if (type.equals(MyDownloadedCellRenderer.AVI)) {
        types[i++] = MyDownloadedCellRenderer.AVI;
      } else if (type.equals(MyDownloadedCellRenderer.MKV)) {
        types[i++] = MyDownloadedCellRenderer.MKV;
      } else {
        types[i++] = MyDownloadedCellRenderer.OTHER;
      }

    }
    return types;
  }

  private static ArrayList<File> getSubtitles(SeriesRecord series, EpisodesRecord episode) {
    String regex = MyUsefulFunctions.createRegex(series.getSeason(), episode.getEpisode());
    ArrayList<File> subs = new ArrayList<File>();
    File[] subtitles = Series.getSubtitleFiles(series);
    Pattern pattern = Pattern.compile(regex);
    if(subtitles == null ){
      return subs;
    }
    for (int j = 0; j < subtitles.length; j++) {
      File file = subtitles[j];
      Matcher matcher = pattern.matcher(file.getName());
      if (matcher.find()) {
        subs.add(file);
      }
    }
    return subs;
  }

  public static String[] getSubtitleLangs(EpisodesRecord ep) {
    SeriesRecord series;
    try {
      series = database.DBHelper.getSeriesByID(ep.getSeries_ID());
    } catch (SQLException ex) {
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
    String[] types = new String[subtitles.size()];
    int i = 0;
    for (Iterator<File> it = subtitles.iterator(); it.hasNext();) {
      String name = it.next().getName();
      String[] tokens = name.split("\\.", -1);
      if (tokens.length < 3) {
        types[i++] = MySubtitlesCellRenderer.OTHER;
      } else {
        String lan = tokens[tokens.length - 2];
        if (LangsList.isLanguageCode(lan)) {
          types[i++] = lan;
        } else {
          types[i++] = MySubtitlesCellRenderer.OTHER;
        }
      }
    }
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
    try {
      if (directory.getCanonicalPath().startsWith("\\\\")) {
        return true;
      }
      return false;
    } catch (IOException ex) {
      return false;
    }
  }

  private MyUsefulFunctions() {
  }
}
