/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myToolbar;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 *
 * @author ssoldatos
 */
public class ToolbarButtonMouseMotionListener extends MouseAdapter {

    private final JPanel panelUsed;
    private final JPanel panelUnused;

    ToolbarButtonMouseMotionListener(JPanel panelUsed, JPanel panelUnused) {
        this.panelUsed = panelUsed;
        this.panelUnused = panelUnused;

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Component c = (Component) e.getSource();

        int order = c.getParent().getComponentZOrder(c);
        c.setSize(ToolbarButtonActions.BUTTON_DIMENSION);
        c.setMaximumSize(ToolbarButtonActions.BUTTON_DIMENSION);
        
        if (c instanceof ToolbarButton) {
            ToolbarButton t = (ToolbarButton) c;
                t.getTopLevelAncestor().setComponentZOrder(t, 0);
            Point p = c.getLocation();
            c.setLocation(p.x + e.getX() - t.origin.x, p.y + e.getY() - t.origin.y);

        } else {
            ToolbarSeperator t = (ToolbarSeperator) c;
            t.getTopLevelAncestor().setComponentZOrder(t, 0);
            Point p = c.getLocation();
            c.setLocation(p.x + e.getX() - t.origin.x, p.y + e.getY() - t.origin.y);
        }
    }
}
