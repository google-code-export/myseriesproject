/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soldatos.validators;

/**
 * Creates an integer validator to check if a given value is an integer
 * @author ssoldatos
 */
public class IntegerValidator extends NumericValidator {

  /**
   * Creates a default IntegerValidator
   */
  public IntegerValidator() {
    super();
    afterCreation();
  }

  /**
   * Creates an Integer validator to check if a value is an integer
   * @param value The value to check. If null it will use the components text value
   * @param allowEmpty If empty value is allowed
   */
  public IntegerValidator(String value, boolean allowEmpty) {
    super();
    this.value = value;
    this.allowEmpty = allowEmpty;
    afterCreation();
  }

  @Override
  public boolean validate() {
    if (!super.validate()) {
      return false;
    }
    if (Double.parseDouble(value) == Long.parseLong(value)) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  protected void setErrorMessage() {
    errorMessage = "The value must be a valid integer";
  }

  @Override
  public String getType() {
    return SValidator.INTEGER;
  }
}
