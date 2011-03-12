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
public abstract class AbstractButton extends Button {

    private class Adapter extends MouseAdapter{
        
        private Adapter() {
        
        }

        @Override
        public void mouseEntered(MouseEvent e) {
           AbstractButton.this.setBorderPainted(true);
           AbstractButton.this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        }

        @Override
        public void mouseExited(MouseEvent e) {
           AbstractButton.this.setBorderPainted(false);
           AbstractButton.this.setBorder(BorderFactory.createEmptyBorder());
        }

      

        @Override
        public void mousePressed(MouseEvent e) {
            AbstractButton.this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }




        
    }

    public AbstractButton() {
        super();
        addMouseListener(new Adapter());
    }

    public AbstractButton(String type) {
        super(type);
        addMouseListener(new Adapter());
    }
}
