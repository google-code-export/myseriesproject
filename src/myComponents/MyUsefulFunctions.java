/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents;

import database.EpisodesRecord;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import javax.swing.JOptionPane;
import myComponents.myGUI.MyFont;
import tools.Skin;
import tools.options.Options;

/**
 * Useful functions
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
      sdf = new SimpleDateFormat(Options._MYSQL_DATE_FORMAT_);
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
      DateFormat df = new SimpleDateFormat(Options._MYSQL_DATE_FORMAT_);
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
      SimpleDateFormat f = new SimpleDateFormat(Options._MYSQL_DATE_FORMAT_);
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

  public static boolean hasInternetConnection() {
    BufferedReader in = null;
    try {
      URL google = new URL("http://www.google.com/");
      in = new BufferedReader(new InputStreamReader(google.openStream()));
      return true;
    } catch (IOException ex) {
      try {
        URL msn = new URL("http://www.msn.com/");
        in = new BufferedReader(new InputStreamReader(msn.openStream()));
        return true;
      } catch (IOException ex1) {
        return false;
      }
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

  public static String getJarDir(Object main) {
    File p = new File(main.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
    if (p.getParent().endsWith("build")) {

      return p.getParent().replace("build", "").replace("%20", " ");
    }
    return p.getParent().replace("%20", " ") + "/";
  }

  public static boolean hasBeenAired(String aired) {
    if (aired.compareTo(getToday("yyyy-MM-dd")) <= 0) {
      return true;
    }
    return false;
  }

  public static int getSubsId(String subs) {
    if (subs.equals("None")) {
      return EpisodesRecord.NO_SUBS;
    } else if (subs.equals("English")) {
      return EpisodesRecord.SEC_SUB;
    } else if (subs.equals("Greek")) {
      return EpisodesRecord.PRIM_SUB;
    } else {
      return EpisodesRecord.BOTH_SUBS;
    }
  }

  public static String createRegex(int season, int episode) {
    return "\\D*" + season + Options._REGEX_ + episode + "\\D";
  }

  public static String createRandomString(int length) {
    Random random = new Random();
    StringBuilder sb = new StringBuilder();
    while (sb.length() < length) {
      sb.append(Integer.toHexString(random.nextInt()));
    }
    return sb.toString();
  }

  public static String join(Iterable<? extends Object> pColl, String separator) {
    Iterator<? extends Object> oIter;
    if (pColl == null || (!(oIter = pColl.iterator()).hasNext())) {
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

  public static Font getCellFont(Font defaultFont, boolean downloaded, boolean seen, String sub) {
    Font font = defaultFont;
    if (seen) {
      font = font.deriveFont(Font.ITALIC, defaultFont.getSize());
    } else if (!seen && downloaded && !sub.equals("None")) {
      font = font.deriveFont(Font.BOLD, defaultFont.getSize() + MyFont.SIZE_STEP);
    }
    return font;
  }

  public static Color getCellColor(boolean isSelected, boolean seen) {
    if (isSelected) {
      return Skin.getColor_1();
    } else {
      if (seen) {
        return Skin.getColor_4();
      } else {
        return Skin.getColor_5();
      }
    }
  }

  private MyUsefulFunctions() {
  }
}
