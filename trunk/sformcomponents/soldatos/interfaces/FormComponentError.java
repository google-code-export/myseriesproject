/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soldatos.interfaces;

/**
 *
 * @author ssoldatos
 */
public interface FormComponentError {

  /** The error image displayed in the text field **/
  String errorImageUrl = "error.png";

  /**
   * @return The error messages
   */
  String getErrorMessage();

  /** Adds an error to the component **/
  void addError();

  /** Clear component's errors **/
  void clearError();
}
