/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

import java.sql.SQLException;

/**
 * Error when accessing / creating options
 * @author lordovol
 */
public class DatabaseException extends SQLException {

  public static final long serialVersionUID = 235354745632L;
  public DatabaseException() {
    super();
  }

  public DatabaseException(String message) {
    super(message);
  }
}
