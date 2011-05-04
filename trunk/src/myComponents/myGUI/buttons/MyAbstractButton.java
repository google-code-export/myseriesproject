/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myGUI.buttons;

import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ButtonUI;

/**
 *
 * @author lordovol
 */
public abstract class MyAbstractButton extends JButton {

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
    public static final String SEARCH = "search";
    Dimension size = new Dimension(32, 32);

    public MyAbstractButton(){
        this(CANCEL);
    }

    public MyAbstractButton(String type) {
        this.setIcon(new ImageIcon(getClass().getResource(images + type +".png")));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);
        ButtonUI b = (ButtonUI) UIManager.getDefaults().getUI(this);
        setUI(b);
    }
}
