/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myEvents;

import javax.swing.event.EventListenerList;

/**
 *
 * @author ssoldatos
 */
public class MyEventsClass implements MyEventListenerInterface {

  protected EventListenerList listenerList = new EventListenerList();

  @Override
  public void addCustomEventListener(MyEventListener listener) {
    listenerList.add(MyEventListener.class, listener);
  }

  @Override
  public void removeMyEventListener(MyEventListener listener) {
    listenerList.remove(MyEventListener.class, listener);
  }

  
  @Override
  public void fireMyEvent(MyEvent evt) {
    Object[] listeners = listenerList.getListenerList();
    for (int i = 0; i < listeners.length; i = i + 2) {
      if (listeners[i] == MyEventListener.class) {
        ((MyEventListener) listeners[i + 1]).myEventOccured(evt);
      }
    }
  }
}
