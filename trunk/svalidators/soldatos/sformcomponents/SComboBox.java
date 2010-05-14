/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soldatos.sformcomponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.UIManager;
import soldatos.exceptions.AttributeException;
import soldatos.interfaces.FormComponentError;
import soldatos.interfaces.FormComponentValidator;
import soldatos.validators.NullValidator;
import soldatos.validators.SValidator;

/**
 *
 * @author ssoldatos
 */
public class SComboBox extends JComboBox implements FormComponentError, FormComponentValidator {

  private static final long serialVersionUID = 346467346236435L;
  private boolean allowEmpty = false;
  private boolean error;
  private Image errorImage;
  private String errorMessage;
  private ValidatorList validators = new ValidatorList();

  /**
   * Called before every constructor to get the errorImage
   */
  {
    URL url = FormComponentError.class.getResource(errorImageUrl);
    setPreferredSize(new Dimension(100, 20));
    try {
      Image errorIm = ImageIO.read(url);
      errorImage = errorIm.getScaledInstance(errorIm.getWidth(this), getHeight() - 2, Image.SCALE_SMOOTH);
    } catch (IOException ex) {
    }

  }

  /**
   * Creates a default SComboBox
   */
  public SComboBox() {
    this(new NullValidator(), false);
  }

  /**
   * Creates a SComboBox with a Validator
   * @param validator The validator to add to the component
   */
  public SComboBox(SValidator validator) {
    this(validator, false);
  }

 
  /**
   * Creates a SComboBox with a Validator and setting if empty value is allowed
   * @param validator The validator to add to the component
   * @param allowEmpty If empty is allowed
   */
  public SComboBox(SValidator validator, boolean allowEmpty) {
    super();
    this.allowEmpty = allowEmpty;
    addValidator(validator);
  }

 

  @Override
  public void addError() {
    setBorder(BorderFactory.createLineBorder(Color.RED));
    setToolTipText("<html>" + errorMessage);
    error = true;
    repaint();
  }

  @Override
  public void clearError() {
    setBorder(UIManager.getBorder("ComboBox.border"));
    setToolTipText("");
    error = false;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (error) {
      setOpaque(false);
      JComponent comboEditor = (JComponent) getEditor().getEditorComponent();
      comboEditor.setOpaque(false);
      int y = (getHeight() - errorImage.getHeight(this)) / 2;
      int x = getWidth() - errorImage.getWidth(this) - getInsets().right;
      g.drawImage(errorImage, x - 20, y, this);
    }
  }

  @Override
  protected void processFocusEvent(FocusEvent e) {
    super.processFocusEvent(e);
    validateValue();
  }

  @Override
  public void processKeyEvent(KeyEvent e) {
    super.processKeyEvent(e);
    validateValue();
  }

  @Override
  protected void selectedItemChanged() {
    super.selectedItemChanged();
    validateValue();
  }

  @Override
  public void setEditable(boolean aFlag) {
    super.setEditable(aFlag);
    if (isEditable()) {
      getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {

        @Override
        public void keyReleased(KeyEvent e) {
          validateValue();
        }
      });
    }
    validateValue();
  }

  @Override
  public void setModel(ComboBoxModel aModel) {
    super.setModel(aModel);
    validateValue();
  }

  @Override
  public boolean validateValue() {
    if (!isEditable()) {
      if (allowEmpty || getSelectedIndex() > 0) {
        clearError();
        return true;
      } else {
        addError();
        this.errorMessage = " - Default value is not valid";
        return false;
      }
    }
    String val = String.valueOf(getEditor().getItem());
    Collection<SValidator> c = validators.validators.values();
    Iterator<SValidator> it = c.iterator();
    this.errorMessage = "";
    clearError();
    while (it.hasNext()) {
      SValidator cValidator = it.next();
      cValidator.setValue(val);
      try {
        if (!cValidator.validate()) {
          error = true;
          this.errorMessage += " - " + cValidator.getErrorMessage() + "<br />";
        }
      } catch (AttributeException ex) {
        error = true;
        addError();
        this.errorMessage += " - " + cValidator.getErrorMessage() + "<br />";
      }
    }
    if (error) {
      addError();
      return false;
    }
    clearError();
    return true;
  }

  @Override
  public String getErrorMessage() {
    String er = "";
    Collection<SValidator> c = validators.validators.values();
    Iterator<SValidator> it = c.iterator();
    while (it.hasNext()) {
      SValidator cValidator = it.next();
      cValidator.setValue(getSelectedItem().toString().trim());
      er += " - " + cValidator.getErrorMessage() + "<br />";
    }
    return er;
  }

  @Override
  public void addValidator(SValidator validator) {
    validators.addValidator(validator);
    validateValue();
  }

  @Override
  public void removeValidator(String type) {
    validators.removeValidator(type);
    validateValue();
  }

  @Override
  public void clearValidatorsList() {
    validators.clearValidators();
    validateValue();
  }

  @Override
  public ValidatorList getValidatorsList() {
    return validators;
  }

  @Override
  public int getValidatorsListSize() {
    return validators.getSize();
  }
}
