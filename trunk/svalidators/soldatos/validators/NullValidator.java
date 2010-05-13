/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package soldatos.validators;

/**
 *
 * @author ssoldatos
 */
public class NullValidator extends SValidator{

  /**
   * Creates a NullValidator (always validates)
   */
  public NullValidator() {
    super();
    afterCreation();
  }


  @Override
  public boolean validate(){
    return true;
  }

  @Override
  protected void setErrorMessage() {}

  @Override
  public String getType() {
    return SValidator.NULL;
  }

}
