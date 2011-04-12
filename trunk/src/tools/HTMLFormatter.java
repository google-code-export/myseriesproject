/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Html log formatter
 * @author ssoldatos
 */
public class HTMLFormatter extends Formatter {
  // This method is called for every log records

  /**
   * Create formatter for the log record
   * @param rec The log Record
   * @return
   */
  public String format(LogRecord rec) {
    StringBuilder buf = new StringBuilder(1000);
    // Bold any levels >= WARNING
    if (rec.getLevel().intValue() == Level.WARNING.intValue()) {
            buf.append("<tr bgcolor='yellow' valign='top'><td>").append(rec.getLevel()).append("</td>");
    } else if (rec.getLevel().intValue() == Level.SEVERE.intValue()) {
            buf.append("<tr bgcolor='red' valign='top'><td>").append(rec.getLevel()).append("</td>");
    } else if (rec.getLevel().intValue() == Level.FINE.intValue()) {
            buf.append("<tr bgcolor='lightGreen' valign='top'><td>").append(rec.getLevel()).append("</td>");
    }
      else {
            buf.append("<tr bgcolor='white'  valign='top'><td>").append(rec.getLevel()).append("</td>");
    }
    //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS");
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss SSS",Locale.ENGLISH);
    Date d = new Date(rec.getMillis());
    String thrown;
        buf.append("<td>&nbsp;").append(sdf.format(d)).append("</td>");
        buf.append("<td>&nbsp;" + rec.getThreadID() + "</td>");
        buf.append("<td>&nbsp;").append(rec.getSourceClassName()).append("</td>");
        buf.append("<td>&nbsp;").append(rec.getSourceMethodName()).append("</td>");
    if(rec.getThrown() != null){
            buf.append("<td>&nbsp;").append(getStackTrace(rec.getThrown())).append("</td></tr>\n");
    } else {
            buf.append("<td>&nbsp;").append(formatMessage(rec)).append("</td></tr>\n");
    }
    return buf.toString();
  }

  
  // This method is called just after the handler using this
  // formatter is created
  @Override
  public String getHead(Handler h) {
    return "<head>" +
        "<title>LOG FILE</title>" +
        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
        "<style>" +
        "body {font-size:16px;font-weight:bold}" +
        "td {font-face:monospaced;font-size:12px}" +
        "th {font-size:14px;font-weight:bold;background:#AAAAAA}" +
        "</style></head><body><p>\n"+
        (new Date()) +
        "\n" +
        "<table width = '90%' border='1' cellspacing = '0' cellpadding='3'>" +
        "<tr valign='top'>"+
        "<th width='50px'>LEVEL</th>" +
        "<th width='100px'>TIME</th>" +
        "<th>THREAD</th>" +
        "<th>CLASS</th>" +
        "<th>METHOD</th>" +
        "<th>MESSAGE</th>" +
        "</tr>\n";
  }

  // This method is called just after the handler using this
  // formatter is closed
  @Override
  public String getTail(Handler h) {
    return "</table></body>\n";
  }

  private static String getStackTrace(Throwable aThrowable) {
    final Writer result = new StringWriter();
    final PrintWriter printWriter = new PrintWriter(result);
    aThrowable.printStackTrace(printWriter);
    return result.toString().replaceAll("\n", "<br />");
  }

}
