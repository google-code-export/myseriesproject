/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myEvents;

/**
 *
 * @author ssoldatos
 */
public interface MyEventListenerInterface {

  public void addCustomEventListener(MyEventListener listener);

  public void removeMyEventListener(MyEventListener listener);

  void fireMyEvent(MyEvent evt);
}
