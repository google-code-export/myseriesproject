/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myGUI.buttons;

import buttons.Button;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import tools.MySeriesLogger;

/**
 *
 * @author lordovol
 */
public abstract class MyAbstractButton extends Button {

    public static final String images = "/myComponents/myGUI/buttons/images/";
    public static final String OK = "ok";
    public static final String CANCEL = "exit";
    public static final String HELP = "help";
    public static final String ADD_ALL = "addall";
    public static final String REMOVE_ALL = "removeall";
    public static final String APPLY = "apply";
    public static final String SAVE = "save";
    public static final String DIRECTORY = "dir";
    public static final String INTERNET = "internet";
    public static final String RSS_ADD = "rss_add";
    public static final String RSS_REFRESH = "rss_refresh";
    public static final String GOOGLE_CODE = "googlecode";
    public static final String EMAIL = "mail";
    public static final String CLEAR = "clear";

    private class Adapter extends MouseAdapter {

        private Adapter() {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            MyAbstractButton.this.setBorderPainted(false);
            //MyAbstractButton.this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            MyAbstractButton.this.setBorderPainted(false);
            //MyAbstractButton.this.setBorder(BorderFactory.createEmptyBorder());
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Button b = (buttons.Button) e.getSource();
            MySeriesLogger.logger.log(Level.INFO, "Button {0} was pressed", b.getType());
            //  MyAbstractButton.this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
    }

    public MyAbstractButton() {
        this(CANCEL);
    }

    public MyAbstractButton(String type) {
        super(images, type);
        setOpaque(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
       // addMouseListener(new Adapter());
    }

    static class MyButtonUI extends BasicButtonUI {

        public static ComponentUI createUI(JComponent c) {
            return new MyButtonUI();
        }
    }
}
