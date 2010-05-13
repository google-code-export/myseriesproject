/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soldatos.validators;

/**
 * EmailValidator
 * Validates that a value is an email address
 * @author ssoldatos
 */
public class UrlValidator extends RegularExpressionValidator {

  /** The emails regular expression **/
  static final String URL_REGEX = "(ftp|https?)://([a-z_\\-0-9]*\\.){2}[a-z]{2,4}";

  /**
   * Creates a default EmailValidator
   */
  public UrlValidator() {
    super();
    setRegex(URL_REGEX);
    afterCreation();
  }

  /**
   * Creates an EmailValidator with a value and set if empty value is allowed
   * @param value The value to validate
   * @param allowEmpty
   */
  public UrlValidator(String value, boolean allowEmpty) {
    super();
    this.value = value;
    this.allowEmpty = allowEmpty;
    setRegex(URL_REGEX);
    afterCreation();
  }

  @Override
  public boolean validate() {
    return super.validate();
  }

  @Override
  protected void setErrorMessage() {
    errorMessage = "Value must be a valid url";
  }

  @Override
  public String getType() {
    return SValidator.URL;
  }
}
