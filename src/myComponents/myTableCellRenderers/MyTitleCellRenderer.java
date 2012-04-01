/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myTableCellRenderers;

import database.EpisodesRecord;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import myComponents.MyUsefulFunctions;
import myseriesproject.episodes.Episodes;
import myseriesproject.filters.Filters;
import tools.download.subtitles.SubtitleConstants;
import tools.languages.Language;

/**
 * The cell renderer for the tile field
 * @author lordovol
 */
public class MyTitleCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 3456463456344572L;
    private final boolean filterTable;
    public static final int NO_ICON = -1;
    public static final int NOT_AIRED = 0;
    public static final int NOT_DOWNLOADED = 1;
    public static final int NO_SUBTITLES = 2;
    public static final int NOT_RENAMED = 3;
    public static final int NOT_SEEN = 4;

    public MyTitleCellRenderer(boolean filterTable) {
        this.filterTable = filterTable;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Boolean downloaded;
        Language sub;
        if (value instanceof EpisodesRecord) {
            EpisodesRecord ep = (EpisodesRecord) value;
            if (!filterTable) {
                downloaded = (Boolean) table.getModel().getValueAt(row, Episodes.DOWNLOADED_COLUMN);
                Boolean seen = (Boolean) table.getModel().getValueAt(row, Episodes.SEEN_COLUMN);
                sub = (Language) table.getModel().getValueAt(row, Episodes.SUBS_COLUMN);
                String date = (String) table.getModel().getValueAt(row, Episodes.AIRED_COLUMN);
                this.setFont(MyUsefulFunctions.getCellFont(this.getFont(), downloaded, seen, sub));
                //this.setForeground(MyUsefulFunctions.getCellColor(this,isSelected, seen, date, downloaded));
            } else {
                downloaded = (Boolean) table.getModel().getValueAt(row, Filters.DOWNLOADED_COLUMN);
                sub = (Language) table.getModel().getValueAt(row, Filters.SUBS_COLUMN);
            }
            addIcon(ep);

            this.setText(value.toString());
        }

        return this;
    }

    private void addIcon(EpisodesRecord ep) {
        int icon = MyUsefulFunctions.getTitleCellIcon(ep,true);
        String image = null;
        setToolTipText(null);
        switch (icon) {
            case -1:
                setIcon(null);
                return;
            case 0:
                setIcon(null);
                return;
            case 1:
                image = "small_download";
                setToolTipText("Should download video file");
                break;
            case 2:
                image = "small_subs";
                setToolTipText("Should download subtitle file");
                break;
            case 3:
                image = "small_rename";
                setToolTipText("Should rename files");
                break;
            case 4:
                image = "small_watch";
                setToolTipText("Should watch!!!");
                break;
        }
        setIcon(new ImageIcon(getClass().getResource("/images/" + image + ".png")));
    }
}
