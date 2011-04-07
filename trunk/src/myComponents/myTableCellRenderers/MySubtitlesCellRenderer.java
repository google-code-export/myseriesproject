/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import database.EpisodesRecord;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import myComponents.MyUsefulFunctions;
import myComponents.myFileFilters.ZipFilter;
import tools.download.subtitles.SubtitleConstants;
import tools.languages.LangsList;
import tools.languages.Language;

/**
 *
 * @author ssoldatos
 */
public class MySubtitlesCellRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = 7897867559L;
  public static final String OTHER = "other";
  public static final String NONE = "none";
  public static final String MULTIPLE = "Multiple";
  public static final int IMAGE_WIDTH = 24;
  public static final int IMAGE_HEIGHT = 14;
  public static final int GAP = 2;
  private final int episodeColumn;

  public MySubtitlesCellRenderer(int episodeColumn) {
    this.episodeColumn = episodeColumn;
  }

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    String[][] langs;
    String tooltip = "";
    setIcon(null);
    if (value instanceof Language) {
      Language val = (Language) value;
      setText("");
      if (val != LangsList.NONE) {
        EpisodesRecord ep = (EpisodesRecord) table.getValueAt(row, this.episodeColumn);
        langs = MyUsefulFunctions.getSubtitleLangs(ep);
        if (langs != null) {
          tooltip = createToolTip(langs);
          setIcon(createIcon(langs));
        } else {
          tooltip = ep.getSubs().getName();
          String[][] l = new String[1][2];
          l[0][0] = ep.getSubs().getCode();
          l[0][1]="srt";
          setIcon(createIcon(l));
        }
      } else {
        setIcon(null);
      }
    }
    //setHorizontalAlignment(SwingConstants.CENTER);

    setVerticalAlignment(SwingConstants.CENTER);
    setToolTipText(tooltip);
    return this;
  }

  private Icon createIcon(String[][] langs) {
    if (langs.length == 1 && langs[0][0].equals(NONE)) {
      langs[0][0] = "other";
    }
    if (langs.length == 1 && langs[0][0].equals("")) {
      langs[0][0] = "multiple";
    }

    BufferedImage buff = new BufferedImage(langs.length * IMAGE_WIDTH + langs.length * GAP, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    for (int i = 0; i < langs.length; i++) {
      if (langs[i][0].equals(OTHER)) {
        langs[i][0] = myseries.MySeries.languages.getPrimary().getCode();
      }
      ImageIcon im = new ImageIcon(getClass().getResource("/images/langs/" + langs[i][0] + ".png"));
      buff.getGraphics().drawImage(im.getImage(), (i * IMAGE_WIDTH) + (i * GAP), 0, IMAGE_WIDTH, IMAGE_HEIGHT, this);
      if (MyUsefulFunctions.isInArray(ZipFilter.ZIP_EXT, langs[i][1].toLowerCase())) {
        ImageIcon imZ = new ImageIcon(getClass().getResource("/images/langs/zip.png"));
        buff.getGraphics().drawImage(imZ.getImage(), (i * IMAGE_WIDTH) + (i * GAP) , 0, IMAGE_WIDTH, IMAGE_HEIGHT, this);
      }
    }

    return new ImageIcon(buff);
  }

  private String createToolTip(String[][] langs) {
    String[] tooltip = new String[langs.length];
    for (int i = 0; i < langs.length; i++) {
      String lang = langs[i][0];
      if (lang.equals(OTHER)) {
        lang = myseries.MySeries.languages.getPrimary().getCode();
      }
      tooltip[i] = LangsList.getLanguageByCode(lang).getName();
      String ext = langs[i][1].toLowerCase();
       if (MyUsefulFunctions.isInArray(ZipFilter.ZIP_EXT, ext)) {
         tooltip[i] += " ("+ext+") ";
       }
    }
    return Arrays.toString(tooltip);
  }
}
