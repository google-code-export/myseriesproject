/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soldatos.validators;

/**
 *
 * @author lordovol
 */
public class PositiveNumberValidator extends NumericValidator {

  private boolean includeZero = false;

  /**
   * Creates a default PositiveNumberValidator
   */
  public PositiveNumberValidator() {
    super();
    afterCreation();
  }

  /**
   * Creates a PositiveNumberValidator with a value and set if empty is allowed
   * @param value The value to validate
   * @param includeZero
   * @param allowEmpty If empty value is allowed
   */
  public PositiveNumberValidator(String value, boolean includeZero, boolean allowEmpty) {
    super();
    this.value = value;
    this.allowEmpty = allowEmpty;
    this.includeZero = includeZero;
    afterCreation();
  }

  @Override
  public boolean validate() {
    if (!super.validate()) {
      return false;
    }
    Double dValue = Double.parseDouble(value);
    if (includeZero && dValue == 0) {
      return true;
    } else {
      if (dValue > 0) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected void setErrorMessage() {
    errorMessage = "The value must be a positive number";
  }

  @Override
  public String getType() {
    return SValidator.POSITIVE;
  }
}
