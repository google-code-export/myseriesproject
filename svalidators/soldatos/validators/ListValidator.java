/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soldatos.validators;

import java.util.ArrayList;
import java.util.Collection;
import soldatos.exceptions.AttributeException;

/**
 * CompareValidator
 * Check if two values are equal
 * @author ssoldatos
 */
public class ListValidator extends SValidator {

  /** The list to validate against **/
  private Collection<Object> list = new ArrayList<Object>();

  /**
   * Creates a default ListValidator
   */
  public ListValidator(){
    super();
    afterCreation();
  }

  /**
   * Creates a ListValidator with a value , an attribute map and
   * if empty value is allowed
   * @param value The value to validate
   * @param list
   * @param allowEmpty If empty value is allowed
   */
  public ListValidator(String value, Collection<Object> list, boolean allowEmpty)  {
  super();
  this.value = value;
  this.list = list;
  this.allowEmpty = allowEmpty;
    afterCreation();
  }

  @Override
  public boolean validate()  {
   int empty = validateEmpty();
    if (empty == SValidator.EMPTY_ALLOWED) {
      return true;
    } else if(empty == SValidator.EMPTY_NOTALLOWED){
      return false;
    }
    for (Object ob : getList()) {
      if (ob.equals(value)) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected void setErrorMessage() {
    errorMessage =  "The value must be in the predefined list";
  }

  @Override
  public String getType() {
    return SValidator.LIST;
  }

  /**
   * @return the list
   */
  public Collection<Object> getList() {
    return list;
  }

  /**
   * @param list the list to set
   */
  public void setList(Collection<Object> list) {
    this.list = list;
  }

}
