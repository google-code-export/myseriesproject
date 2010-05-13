/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soldatos.validators;

import soldatos.exceptions.AttributeException;

/**
 * CompareValidator
 * Check if two values are equal
 * @author ssoldatos
 */
public class CompareValidator extends SValidator {

  /** The value to compare with **/
  private String valueToCompareWith;
  /** The comparing type (lesser, equals, greater) **/
  private int compareType;
  /** The lesser constant : LESSER **/
  public static int LESSER = -1;
  /** the equals constant : EQUALS **/
  public static int EQUALS = 0;
  /** The greater constant : GREATER **/
  public static int GREATER = 1;

  /**
   * Creates a default CompareValidator
   */
  public CompareValidator() {
    super();
    this.compareType = EQUALS;
    this.valueToCompareWith = "";
    afterCreation();
  }

  /**
   * Creates a DateValidator with a value , an attribute map and
   * if empty value is allowed
   * @param value The value to validate or null
   * @param valueToCompare The value to compare with
   * @param compareType
   * @param allowEmpty If empty value is allowed
   */
  public CompareValidator(String value, String valueToCompare, int compareType, boolean allowEmpty) {
    super();
    this.value = value;
    this.valueToCompareWith = valueToCompare;
    setCompareType(compareType);
    this.allowEmpty = allowEmpty;
    afterCreation();
  }

  @Override
  public boolean validate() {
    int empty = validateEmpty();
    int compareEmpty = new RequiredValidator(getValueToCompareWith()).validateEmpty();
    if ((empty == SValidator.EMPTY_ALLOWED) && (compareEmpty == SValidator.EMPTY_ALLOWED)) {
      return true;
    } else if (empty == SValidator.EMPTY_NOTALLOWED) {
      return false;
    }
    try {
      double dValue = Double.parseDouble(value);
      double dValueToCompareWith = Double.parseDouble(valueToCompareWith);
      if (compareType == EQUALS) {
        return dValue == dValueToCompareWith;
      } else if (compareType == LESSER) {
        return dValue < dValueToCompareWith;
      } else if (compareType == GREATER) {
        return dValue > dValueToCompareWith;
      }
    } catch (NumberFormatException ex) {
      if (compareType == EQUALS) {
        return value.equals(valueToCompareWith);
      } else if (compareType == LESSER) {
        return value.compareTo(valueToCompareWith) < 0;
      } else if (compareType == GREATER) {
        return value.compareTo(valueToCompareWith) > 0;
      }
    }
    return false;
  }

  @Override
  protected void setErrorMessage() {
    errorMessage = "The value must be  " + getValueToCompareWith();


  }

  @Override
  public String getType() {
    return SValidator.COMPARE;


  }

  /**
   * @return the valueToCompareWith
   */
  public String getValueToCompareWith() {
    return valueToCompareWith;


  }

  /**
   * @param valueToCompareWith the valueToCompareWith to set
   */
  public void setValueToCompareWith(String valueToCompareWith) {
    this.valueToCompareWith = valueToCompareWith;


  }

  /**
   * @return the compareType
   */
  public int getCompareType() {
    return compareType;


  }

  /**
   * @param compareType the compareType to set
   * @throws AttributeException
   */
  public void setCompareType(int compareType) throws AttributeException {
    if (compareType == LESSER || compareType == EQUALS || compareType == GREATER) {
      this.compareType = compareType;


    } else {
      throw new AttributeException("Compare type must be :" + LESSER + " or " + EQUALS + " or " + GREATER);


    }
  }
}
