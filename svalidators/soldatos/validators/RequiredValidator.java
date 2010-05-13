/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soldatos.validators;

/**
 * Required validator.
 * Ensures that the value is not empty
 * @author ssoldatos
 */
public class RequiredValidator extends SValidator {
  
  
/**
 * Creates a default RequiredValidator
 */
  public RequiredValidator() {
    super();
    afterCreation();
  }

  /**
   * Creates the validator.
   * @param value The value to check.
   */
  public RequiredValidator(String value) {
    super();
    this.value = value;
    afterCreation();
  }

  @Override
  public boolean validate() {
    return !value.equals("");
  }

 
  @Override
  protected void setErrorMessage() {
    errorMessage = "Empty value is not allowed";
  }

  @Override
  public String getType() {
    return SValidator.REQUIRED;
  }

}
