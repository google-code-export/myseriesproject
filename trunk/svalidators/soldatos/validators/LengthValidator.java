/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soldatos.validators;

/**
 * CompareValidator
 * Check if two values are equal
 * @author ssoldatos
 */
public class LengthValidator extends SValidator {

  /** The minimum length allowed **/
  private int min = 0;
  /** The maximum length allowed **/
  private int max = 0;
  /** The length allowed **/
  private int length = 0;


  /**
   * Creates a default LengthValidator<br />
   * If min is provided the validator checks if the value has a length at least min<br />
   * If max is provided the validator checks if the value has a length not greater than max<br />
   * If min & max is provided the validator checks if the value has a length between min and max<br />
   * If length is provided the validator checks if the value has a length exactly the same<br />
   */
  public LengthValidator(){
    super();
    afterCreation();
  }

  /**
   * 
   * @param value
   * @param length
   * @param max
   * @param min
   * @param allowEmpty
   */
  public LengthValidator(String value, int length, int max, int min, boolean allowEmpty){
    super();
    this.value = value;
    this.length = length;
    this.max = max;
    this.min = min;
    this.allowEmpty = allowEmpty;
    afterCreation();
  }

  @Override
  public boolean validate() {
    int empty = validateEmpty();
    if (empty == SValidator.EMPTY_ALLOWED) {
      return true;
    } else if(empty == SValidator.EMPTY_NOTALLOWED){
      return false;
    }
    if (!(value instanceof String)) {
      return false;
    }

    int len = value.length();
    if (getLength() > 0) {
      if (len == getLength()) {
        return true;
      } else {
        return false;
      }
    } else {
      if ((len >= getMin() && getMax() == 0) || (len <= getMax() && getMin() == 0)) {
        return true;
      } else if (len >= getMin() && len <= getMax()) {
        return true;
      } else {
        return false;
      }
    }
  }

  @Override
  protected void setErrorMessage() {
    if (getLength() == 0) {
      errorMessage = "The value must have a length between " + getMin() + " and " + getMax();
    } else {
      errorMessage = "The value must have a length of " + getLength();
    }
  }

  @Override
  public String getType() {
    return SValidator.LENGTH;
  }

  /**
   * @return the min
   */
  public int getMin() {
    return min;
  }

  /**
   * @param min the min to set
   */
  public void setMin(int min) {
    this.min = min;
    this.length = 0;
    setErrorMessage();
  }

  /**
   * @return the max
   */
  public int getMax() {
    return max;
  }

  /**
   * @param max the max to set
   */
  public void setMax(int max) {
    this.max = max;
    this.length = 0;
    setErrorMessage();
  }

  /**
   * @return the length
   */
  public int getLength() {
    return length;
  }

  /**
   * @param length the length to set
   */
  public void setLength(int length) {
    this.max = 0;
    this.min = 0;
    this.length = length;
    setErrorMessage();
  }
}
