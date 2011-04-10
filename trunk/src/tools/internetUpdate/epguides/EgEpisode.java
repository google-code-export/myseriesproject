/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.internetUpdate.epguides;

import java.util.logging.Level;
import myComponents.MyUsefulFunctions;
import myseries.*;
import tools.MySeriesLogger;

/**
 *
 * @author lordovol
 */
public class EgEpisode {

    public int number;
    public String title;
    public String airDate;
    private String[] months = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    /**
     * Construct an epGuide episode
     * @param data The episodes data
     */
    public EgEpisode(String data) {
        MySeriesLogger.logger.log(Level.INFO, "New epGuide episode");
        number = getNumber(data);
        title = getTitle(data);
        airDate = getAirDate(data);
    }

    private int getNumber(String data) {
        MySeriesLogger.logger.log(Level.INFO, "Getting episodes number");
        String ep = data.substring(0, 12).trim();
        String epStr = ep.substring(ep.lastIndexOf("-") + 1);
        try {
            int n = Integer.parseInt(epStr.trim());
            MySeriesLogger.logger.log(Level.FINE, "Episode number : {0}",n);
            return n;
        } catch (NumberFormatException ex) {
            MySeriesLogger.logger.log(Level.WARNING, "Wrong episode number : ''{0}''", ep);
            return -1;
        }
    }

    private String getTitle(String data) {
        try {
            MySeriesLogger.logger.log(Level.INFO, "Getting episode title");
            int pos = data.indexOf("<a");
            String link = data.substring(pos).trim();
            String l = link.replaceAll("\\<.*?>", "");
            MySeriesLogger.logger.log(Level.FINE, "Episode title : {0}",l);
            return l;
        } catch (IndexOutOfBoundsException ex) {
            MySeriesLogger.logger.log(Level.WARNING, "Episode title not found");
            return "";
        }
    }

    private String getAirDate(String data) {
        String date = getDate(data);
        MySeriesLogger.logger.log(Level.INFO, "Getting episodes aired date");
        String[] arr = date.split("[/ ]", -1);
        if (arr.length != 3) {
            MySeriesLogger.logger.log(Level.WARNING, "Wrong episode date : ''{0}''", date);
            return "";
        }
        String day = MyUsefulFunctions.padLeft(arr[0].trim(), 2, "0");
        String year = "";
        try {
            year = Integer.parseInt(arr[2].trim()) < 50 ? "20" + arr[2].trim() : "19" + arr[2].trim();
        } catch (NumberFormatException ex) {
            MySeriesLogger.logger.log(Level.WARNING, "Wrong episode date : ''{0}''", date);
            return "";
        }
        String month = arr[1].equals("Jan") ? "01" : arr[1].equals("Feb") ? "02" : arr[1].equals("Mar") ? "03" : arr[1].equals("Apr") ? "04" : arr[1].equals("May") ? "05" : arr[1].equals("Jun") ? "06" : arr[1].equals("Jul") ? "07" : arr[1].equals("Aug") ? "08" : arr[1].equals("Sep") ? "09" : arr[1].equals("Oct") ? "10" : arr[1].equals("Nov") ? "11" : arr[1].equals("Dec") ? "12" : "-1";

        if (Integer.parseInt(day) > 31 || Integer.parseInt(day) < 1) {
            MySeriesLogger.logger.log(Level.WARNING, "Wrong episode date : ''{0}''", date);
            return "";
        } else if (Integer.parseInt(month) == -1) {
            MySeriesLogger.logger.log(Level.WARNING, "Wrong episode date : ''{0}''", date);
            return "";
        } else {
            airDate = year + "-" + month + "-" + day;
        }
        MySeriesLogger.logger.log(Level.FINE, "Aired date : {0}",airDate);
        return airDate;
    }

    private String getDate(String data) {
        MySeriesLogger.logger.log(Level.INFO, "Getting date");
        data = data.replaceAll("/", " ");
        for (int i = 1; i < months.length; i++) {
            int pos = data.indexOf(" " + months[i] + " ");
            if (pos > -1) {
                String d = data.substring(pos - 4, pos + 8).trim();
                MySeriesLogger.logger.log(Level.FINE, "Date :{0}",d);
                return d;
            }
        }
        MySeriesLogger.logger.log(Level.WARNING, "Date not found");
        return "";
    }
}
