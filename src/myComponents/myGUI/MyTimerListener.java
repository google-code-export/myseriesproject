/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myGUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import myComponents.MyUsefulFunctions;
import myComponents.myToolbar.ToolbarButton;
import myComponents.myToolbar.ToolbarSeperator;

/**
 *
 * @author ssoldatos
 */
public class MyTimerListener implements ActionListener {

  private static final long serialVersionUID = 8768767857564L;
  private final ToolbarSeperator mem;
  private final int orientation;

  public MyTimerListener(ToolbarSeperator mem,int orientation) {
    this.mem = mem;
    this.orientation = orientation;

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    System.gc();
    if (mem != null) {
      //mem.setIcon(null);
      //Free jvm memory
      long jvmFreeMem = Runtime.getRuntime().freeMemory();
      //Memory dedicated to jvm at the momment
      long jvmTotMem = Runtime.getRuntime().totalMemory();
      //Memory given to jvm by the OS (256MB in win)
      long jvmMaxMem = Runtime.getRuntime().maxMemory();

      String memory = calc(jvmTotMem - jvmFreeMem);
      int perc = calcPerc(jvmTotMem - jvmFreeMem, jvmMaxMem);
      if (perc < 20) {
        mem.setBackground(Color.GREEN);
      } else if (perc < 50) {
        mem.setBackground(Color.YELLOW);
      } else if (perc < 80) {
        mem.setBackground(Color.ORANGE);
      } else {
        mem.setBackground(Color.RED);
      }
      String text = memory + " " + perc + "% ";
      if ( orientation == SwingConstants.HORIZONTAL) {
        mem.setText(text);
      }

      mem.setToolTipText(text);
      mem.revalidate();
    }
  }

  private String calc(long l) {
    return MyUsefulFunctions.createFileSize(l);
  }

  private int calcPerc(long used, long max) {
    return (int) (used * 100 / max);
  }
}
