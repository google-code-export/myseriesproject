/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myGUI.buttons;

import buttons.Button;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;

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

    private class Adapter extends MouseAdapter{
        
        private Adapter() {
        
        }

        @Override
        public void mouseEntered(MouseEvent e) {
           MyAbstractButton.this.setBorderPainted(true);
           MyAbstractButton.this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        }

        @Override
        public void mouseExited(MouseEvent e) {
           MyAbstractButton.this.setBorderPainted(false);
           MyAbstractButton.this.setBorder(BorderFactory.createEmptyBorder());
        }

      

        @Override
        public void mousePressed(MouseEvent e) {
            MyAbstractButton.this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
    }

    public MyAbstractButton() {
        super();
        addMouseListener(new Adapter());
    }

    public MyAbstractButton(String type) {
        super(images, type);
        addMouseListener(new Adapter());
    }
}
