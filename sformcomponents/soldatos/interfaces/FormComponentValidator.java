/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soldatos.interfaces;

import soldatos.sformcomponents.ValidatorList;
import soldatos.validators.SValidator;

/**
 *
 * @author ssoldatos
 */
public interface FormComponentValidator {

  /**
   * Validates the components value
   * @return true if validation succeeds else false
   */
  boolean validateValue();

  /**
   * Adds a validator to the component *
   * @param validator The validator to add
   */
  void addValidator(SValidator validator);

  /**
   * Remove a validator from the component
   * @param type The validator to remove
   */
  void removeValidator(String type);

  /**
   * Clears the components validators
   */
  void clearValidatorsList();

  /**
   * Gets the list of the component's validators
   * @return The components validators
   */
  ValidatorList getValidatorsList();

  /**
   * Gets the number of the component's validators
   * @return The number of the components validators
   */
  int getValidatorsListSize();
}
