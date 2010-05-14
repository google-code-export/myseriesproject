/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soldatos.validators;

/**
 * Numeric range validator.
 * Checks if a valus is greater or equal to a min value, lesser or ewual to
 * a max value or both of them.
 * The attributes must be provided and should be one of min , max or both of them
 * @author ssoldatos
 */
public class RangeValidator extends NumericValidator {
  /** The range maximum value **/
  private double min = -Double.MAX_VALUE;
  private double max = Double.MAX_VALUE;

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
    if (doubleValue <= getMax() && doubleValue >= getMin()) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  protected void setErrorMessage() {
    if (getMin() != -Double.MAX_VALUE && getMax() != Double.MAX_VALUE) {
      errorMessage = "The value must be between " + getMin() + " and " + getMax();
    } else {
      errorMessage = "The value must be  " + (getMin() != -Double.MAX_VALUE ? "greater than " + getMin() : "lesser than " + getMax());
    }
  }

  @Override
  public String getType() {
    return SValidator.RANGE;
  }

  /**
   * @return the min
   */
  public double getMin() {
    return min;
  }

  /**
   * @param min the min to set
   */
  public void setMin(double min) {
    this.min = min;
    setErrorMessage();
  }

  /**
   * @return the max
   */
  public double getMax() {
    return max;
  }

  /**
   * @param max the max to set
   */
  public void setMax(double max) {
    this.max = max;
    setErrorMessage();
  }
}
