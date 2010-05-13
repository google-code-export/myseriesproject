/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package soldatos.sformcomponents;

import java.util.ArrayList;
import javax.swing.JComponent;
import soldatos.exceptions.ComponentException;

/**
 *
 * @author ssoldatos
 */
public class ValidationGroup {
  ArrayList<JComponent> components = new ArrayList<JComponent>();

  public ValidationGroup() {
  }

  public ValidationGroup(ArrayList<JComponent> components){
    for (JComponent jComponent : components) {
      addComponent(jComponent);
    }
  }

  public void addComponent(JComponent component) throws ComponentException{
    if(component instanceof SComboBox || component instanceof STextField){
      components.add(component);
    } else {
      throw new ComponentException("Not valid component");
    }
  }

  public void removeComponent(JComponent component){
    components.remove(component);
  }

  public void clear(){
    components.clear();
  }

  public int getSize(){
    return components.size();
  }

  public boolean validate(){
    for (JComponent jComponent : components) {
      if (jComponent instanceof SComboBox){
        SComboBox combo = (SComboBox )jComponent;
        if(!combo.validateValue()){
          return false;
        }
      }else if (jComponent instanceof STextField){
        STextField textfield = (STextField )jComponent;
        if(!textfield.validateValue()){
          return false;
        }
      }
    }
    return true;
  }

}
