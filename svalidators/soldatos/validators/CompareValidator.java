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
  public static int LESS = 0;
  /** the equals constant : EQUALS **/
  public static int EQUALS = 1;
  /** The greater constant : GREATER **/
  public static int GREATER = 2;
  /** The less or equals constant : LESS_OR_EQUALS **/
  public static int LESS_OR_EQUALS = 3;
  /** The greater or equals constant : GREATER_OR_EQUALS **/
  public static int GREATER_OR_EQUALS = 4;


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
      } else if (compareType == LESS) {
        return dValue < dValueToCompareWith;
      } else if (compareType == GREATER) {
        return dValue > dValueToCompareWith;
      }else if (compareType == LESS_OR_EQUALS) {
        return dValue <= dValueToCompareWith;
      }else if (compareType == GREATER_OR_EQUALS) {
        return dValue >= dValueToCompareWith;
      }
    } catch (NumberFormatException ex) {
      if (compareType == EQUALS) {
        return value.equals(valueToCompareWith);
      } else if (compareType == LESS) {
        return value.compareTo(valueToCompareWith) < 0;
      } else if (compareType == GREATER) {
        return value.compareTo(valueToCompareWith) > 0;
      } else if (compareType == LESS_OR_EQUALS) {
        return value.compareTo(valueToCompareWith) <= 0;
      } else if (compareType == GREATER_OR_EQUALS) {
        return value.compareTo(valueToCompareWith) >= 0;
      }
    }
    return false;
  }

  @Override
  protected void setErrorMessage() {
    errorMessage = "The value must be  "
        + (compareType == LESS ? " less than "
        : compareType == EQUALS ? " equals to "
        : compareType == GREATER ? " greater than " : "")
        + getValueToCompareWith();


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
    setErrorMessage();
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
    if (compareType == LESS || compareType == EQUALS || compareType == GREATER
        || compareType == LESS_OR_EQUALS || compareType == GREATER_OR_EQUALS) {
      this.compareType = compareType;
      setErrorMessage();
    } else {
      throw new AttributeException("Compare type must be :" + LESS + " or " + 
          EQUALS + " or " + GREATER + " or " + LESS_OR_EQUALS + " or " + GREATER_OR_EQUALS);
    }
  }
}
