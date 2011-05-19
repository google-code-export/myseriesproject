/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myListeners;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingConstants;
import myComponents.MyUsefulFunctions;
import myComponents.myToolbar.ToolbarSeperator;
import myseries.MySeries;
import myseries.actions.FeedsActions;

/**
 *
 * @author ssoldatos
 */
public class MyFeedsTimerListener implements ActionListener {

    private static final long serialVersionUID = 876876785723524L;
  private final MySeries m;
   
    public MyFeedsTimerListener(MySeries m) {
    this.m = m;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       FeedsActions.updateFeeds(true, m);
    }

   
}
