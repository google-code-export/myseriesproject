/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myComponents.myGUI.buttons;

import buttons.Button;

/**
 *
 * @author lordovol
 */
public class MyButtonCancel extends MyAbstractButton{
    private static final long serialVersionUID = 346457645764674L;

    public MyButtonCancel() {
        super(CANCEL);
        setToolTipText("Cancel");
    }
}
