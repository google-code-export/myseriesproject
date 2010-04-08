/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

/**
 * Error in importing episodes
 * @author ssoldatos
 */
public class EpisodeImportFormatException extends CustomException {

  public static final long serialVersionUID = 23535658687989L;

  public EpisodeImportFormatException() {
    super();
  }

  public EpisodeImportFormatException(String message) {
    super(message);
  }
}
