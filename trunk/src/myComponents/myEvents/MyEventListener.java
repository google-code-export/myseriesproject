/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myComponents.myEvents;

import java.util.EventListener;
import myseries.MySeries;

/**
 *
 * @author ssoldatos
 */
public interface MyEventListener extends EventListener{

  public void myEventOccured(MyEvent evt, MySeries m);
}

