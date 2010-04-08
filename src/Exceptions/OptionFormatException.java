/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

/**
 * Error when accessing / creating options
 * @author lordovol
 */
public class OptionFormatException extends CustomException {

  public static final long serialVersionUID = 235354745632L;
  public OptionFormatException() {
    super();
  }

  public OptionFormatException(String message) {
    super(message);
  }
}
