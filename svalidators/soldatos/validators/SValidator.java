/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soldatos.validators;

import javax.annotation.PostConstruct;
import soldatos.exceptions.AttributeException;

/**
 * Validator class.Base for all validators
 * Validates a value against some user defined rules.
 * @author ssoldatos
 */
public abstract class SValidator {

  /**
   * ATTRIBUTES
   */
  /** If empty value is allowed **/
  protected boolean allowEmpty = false;
  /** The value to validate **/
  protected String value = "";
  /** The error message when validation fails */
  protected String errorMessage;
  /** When value is empty and it's allowed **/
  protected static final int EMPTY_ALLOWED = 0;
  /** When value is empty but it's not allowed **/
  protected static final int EMPTY_NOTALLOWED = 1;
  /** When value is not empty **/
  protected static final int NOT_EMPTY = 2;

  public static final String COMPARE = "compare";
  public static final String DATE = "date";
  public static final String EMAIL = "email";
  public static final String FILE = "file";
  public static final String INTEGER = "integer";
  public static final String LENGTH = "length";
  public static final String LIST = "list";
  public static final String NEGATIVE = "negative";
  public static final String NOSPACE = "nospace";
  public static final String NULL = "null";
  public static final String NUMERIC = "numeric";
  public static final String POSITIVE = "positive";
  public static final String RANGE = "range";
  public static final String REGEX = "regex";
  public static final String REQUIRED = "required";
  public static final String URL = "url";

  /**
   * ABSTRACT METHODS
   */
  /**
   * Validate the value
   * @return true if validation succeds else false
   */
  public abstract boolean validate();

  /**
   * Sets the rror message of the validator
   */
  protected abstract void setErrorMessage();

  /**
   * Return the type of the validator
   * @return The type of the validator
   */
  public abstract String getType();

  /**
   * CONSTRUCTORS
   */
  /**
   * Default constructor
   */
  protected SValidator() {
  }

  

   /**
   * PROPTECTED METHODS
   * /

  /**
   * Validate if a value is allowed to be empty
   * @return an integers that defines if the value is allowed to be empty or not,
   * or if it's not empty
   */
  protected int validateEmpty() {
    if ((getValue() == null || getValue().equals("")) && isAllowEmpty()) {
      return EMPTY_ALLOWED;
    } else if ((getValue() == null || getValue().equals("")) && !isAllowEmpty()) {
      return EMPTY_NOTALLOWED;
    } else {
      return NOT_EMPTY;
    }
  }

  /**
   * Called affter the creation of the Validator.<br />
   * Reads the attributes sets the error message and then validates the value.
   * @throws AttributeException
   */
  protected void afterCreation() throws AttributeException {
    setErrorMessage();
    validate();
  }

  /**
   * GETTERS / SETTERS
   */
  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * @return the allowEmpty
   */
  public boolean isAllowEmpty() {
    return allowEmpty;
  }

  /**
   * @param allowEmpty the allowEmpty to set
   */
  public void setAllowEmpty(boolean allowEmpty) {
    this.allowEmpty = allowEmpty;
  }

   /**
   * @return the errorMessage
   */
  public String getErrorMessage() {
    return errorMessage;
  }
}
