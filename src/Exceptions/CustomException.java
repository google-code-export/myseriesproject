/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Exceptions;

/**
 * Custom exception
 * @author ssoldatos
 */
public class CustomException extends Exception{
  String message;
  protected  static final long serialVersionUID = 34647586897L;

 /**
  * New exception
  */
  public CustomException() {
    super();
    message = "";
  }

  /**
   * New custom exception with message
   * @param message
   */
  public CustomException(String message) {
    super(message);
    this.message = message;
  }

  /**
   * Get the error message
   * @return
   */
  public String getError() {
    return message;
  }
}
