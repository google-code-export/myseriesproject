/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soldatos.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import soldatos.exceptions.AttributeException;

/**
 * DateValidator.
 * Validates that a given string is a date
 * @author ssoldatos
 */
public class DateValidator extends SValidator {

  /** The date format to validate against **/
  private String dateFormat;

  /**
   * Creates a default DateValidator
   */
  public DateValidator() {
    super();
    setDateFormat("dd/MM/yyyy");
    afterCreation();
  }

  /**
   * Creates a DateValidator with a value and an attribute map
   * @param value The value to validate or null
   * @param dateFormat The dateFormat
   * @param allowEmpty If empty value is allowed
   */
  public DateValidator(String value, String dateFormat, boolean allowEmpty) {
    super();
    this.value = value;
    setDateFormat(dateFormat);
    this.allowEmpty = allowEmpty;
    afterCreation();
  }

  @Override
  public boolean validate() {
    int empty = validateEmpty();
    if (empty == SValidator.EMPTY_ALLOWED) {
      return true;
    } else if (empty == SValidator.EMPTY_NOTALLOWED) {
      return false;
    }
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getDateFormat());
    try {
      Date testDate = simpleDateFormat.parse(value);
      if (!simpleDateFormat.format(testDate).equals(value)) {
        return false;
      }
      return true;
    } catch (ParseException ex) {
      return false;
    }
  }

  @Override
  protected void setErrorMessage() {
    errorMessage = "The value must be valid date";
  }

  @Override
  public String getType() {
    return SValidator.DATE;
  }

  /**
   * @return the dateFormat
   */
  public String getDateFormat() {
    return dateFormat;
  }

  /**
   * @param dateFormat the dateFormat to set
   * @throws AttributeException
   */
  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }
}
