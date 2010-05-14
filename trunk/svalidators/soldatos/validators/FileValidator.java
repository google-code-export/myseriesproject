/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soldatos.validators;

import java.io.File;
import soldatos.exceptions.AttributeException;

/**
 * FileValidator.
 * Validates that a given string is a valis file or directory
 * The type is decided by the type attribute. Default type to file
 * @author ssoldatos
 */
public class FileValidator extends SValidator {

  /** The file type **/
  public static final int FILE = 0;
  /** The directory type **/
  public static final int DIR = 1;
  /** The type of the file to validate (file or dir) **/
  private int fileType = 0;

  /**
   * Creates a default FileValidator
   */
  public FileValidator() {
    super();
    afterCreation();
  }

  /**
   * Creates a FileValidator with a value and an attribute map
   * @param value The value to validate
   * @param fileType
   * @param allowEmpty If empty value is allowed
   */
  public FileValidator(String value, int fileType, boolean allowEmpty){
    super();
    this.value = value;
    this.allowEmpty = allowEmpty;
    this.fileType = fileType;
    afterCreation();
  }

  @Override
  public boolean validate(){
    int empty = validateEmpty();
    if (empty == SValidator.EMPTY_ALLOWED) {
      return true;
    } else if (empty == SValidator.EMPTY_NOTALLOWED) {
      return false;
    }
    File f = new File(value);
    return ((fileType == FILE && f.isFile()) || (fileType == DIR && f.isDirectory()));
  }

  @Override
  protected void setErrorMessage() {
    errorMessage = "The value must be a valid " + (fileType == FILE ? "file" : "directory");
  }

  @Override
  public String getType() {
    return SValidator.FILE;
  }

  /**
   * @return the fileType
   */
  public int getFileType() {
    return fileType;
  }

  /**
   * @param fileType the fileType to set
   * @throws AttributeException
   */
  public void setFileType(int fileType) throws AttributeException {
    if (fileType == FILE || fileType == DIR) {
      this.fileType = fileType;
      setErrorMessage();
    } else {
      throw new AttributeException("The fileType must be " + FILE + " or " + DIR);
    }
  }
}
