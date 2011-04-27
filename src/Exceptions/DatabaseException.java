/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

/**
 * Error when accessing / creating options
 * @author lordovol
 */
public class DatabaseException extends CustomException {

  public static final long serialVersionUID = 235354745632L;
  public DatabaseException() {
    super();
  }

  public DatabaseException(String message) {
    super(message);
  }
}
