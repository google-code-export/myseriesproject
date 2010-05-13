/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soldatos.validators;

import soldatos.exceptions.AttributeException;

/**
 * Numeric range validator.
 * Checks if a valus is greater or equal to a min value, lesser or ewual to
 * a max value or both of them.
 * The attributes must be provided and should be one of min , max or both of them
 * @author ssoldatos
 */
public class RangeValidator extends NumericValidator {

  /** The range minimum value **/
  protected double min = -Double.MAX_VALUE;
  /** The range maximum value **/
  protected double max = Double.MAX_VALUE;

  /**
   * Creates a numeric range validator
   */
  public RangeValidator() {
    super();
    afterCreation();
  }

  /**
   * Creates a numeric range validator
   * @param value The value to validate
   * @param min
   * @param max
   * @param allowEmpty If empty value is allowed
   */
  public RangeValidator(String value, double min, double max, boolean allowEmpty) {
    super();
    this.value = value;
    this.max = max;
    this.allowEmpty = allowEmpty;
    this.min = min;
    afterCreation();
  }

  @Override
  public boolean validate() {
    if (!super.validate()) {
      return false;
    }
    double doubleValue = Double.parseDouble(getValue());
    if (doubleValue <= max && doubleValue >= min) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  protected void setErrorMessage() {
    if (min != -Double.MAX_VALUE && max != Double.MAX_VALUE) {
      errorMessage = "The value must be between " + min + " and " + max;
    } else {
      errorMessage = "The value must be  " + (min != -Double.MAX_VALUE ? "greater than " + min : "lesser than " + max);
    }
  }

  @Override
  public String getType() {
    return SValidator.RANGE;
  }
}
