/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myGUI;

import java.awt.Font;
import javax.swing.UIManager;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class MyFont {

  public static final String DEFAULT_FONT = "SansSerif";
  public static final float DEFAULT_SIZE = 12.0F;
  public static final float SIZE_STEP = 0.5F;
  public static Font myFont;
  public static Font myBoldFont;
  public static Font mySmallerFont;
  public static Font myBiggerFont;
  public static Font myBiggerBoldFont;
  public static Font myBiggestFont;

  
  
  public static void  SetMyFont(){

    String fontFace = Options.toString(Options.FONT_FACE).equals("null") ? DEFAULT_FONT : Options.toString(Options.FONT_FACE);
    float fontSize = Options.toFloat(Options.FONT_SIZE) == 0 ? DEFAULT_SIZE : Options.toFloat(Options.FONT_SIZE);
    Options.setOption(Options.FONT_FACE, fontFace);
    Options.setOption(Options.FONT_SIZE, fontSize);
    Options.save();
    int plain = Font.PLAIN;
    int bold = Font.BOLD;
    myFont = new Font(fontFace,plain,(int)fontSize);
    myBoldFont = myFont.deriveFont(bold);
    mySmallerFont = myFont.deriveFont(fontSize - SIZE_STEP);
    myBiggerFont = myFont.deriveFont(fontSize + SIZE_STEP);
    myBiggerBoldFont = myBiggerFont.deriveFont(bold);
    myBiggestFont = myFont.deriveFont(fontSize + (2*SIZE_STEP));
    UIManager.put("Button.font", myBoldFont);
    UIManager.put("CheckBox.font", myFont);
    UIManager.put("CheckBoxMenuItem.accelerator.font", mySmallerFont);
    UIManager.put("CheckBoxMenuItem.font", myFont);
    UIManager.put("ColorChooser.font", myFont);
    UIManager.put("ComboBox.font", myFont);
    UIManager.put("DesktopIcon.font",myFont);
    UIManager.put("EditorPane.font", myFont);
    UIManager.put("FormattedTextField.font", myFont);
    UIManager.put("InternalFrame.title.font", myFont);
    UIManager.put("Label.font", myFont);
    UIManager.put("List.font", myFont);
    UIManager.put("Menu.accelerator.font",mySmallerFont);
    UIManager.put("Menu.font", myBoldFont);
    UIManager.put("MenuBar.font", myBoldFont);
    UIManager.put("MenuItem.accelerator.font",mySmallerFont);
    UIManager.put("MenuItem.font", mySmallerFont);
    UIManager.put("OptionPane.font", myFont);
    UIManager.put("Panel.font", myFont);
    UIManager.put("PasswordField.font", myFont);
    UIManager.put("PopupMenu.font", myFont);
    UIManager.put("ProgressBar.font", myBoldFont);
    UIManager.put("RadioButton.font", myFont);
    UIManager.put("RadioButtonMenuItem.accelerator.font", mySmallerFont);
    UIManager.put("RadioButtonMenuItem.font", myFont);
    UIManager.put("ScrollPane.font", myFont);
    UIManager.put("Slider.font", myFont);
    UIManager.put("Spinner.font", myFont);
    UIManager.put("TabbedPane.font", myBoldFont);
    UIManager.put("Table.font", myFont);
    UIManager.put("TableHeader.font", myBoldFont);
    UIManager.put("TextArea.font", myFont);
    UIManager.put("TextField.font", myFont);
    UIManager.put("TextPane.font", myFont);
    UIManager.put("TitledBorder.font", myBoldFont);
    UIManager.put("ToggleButton.font", myFont);
    UIManager.put("ToolBar.font", myFont);
    UIManager.put("ToolTip.font", myFont);
    UIManager.put("Tree.font", myFont);
    UIManager.put("Viewport.font", myFont);
  }

  private MyFont() {
  }

  
}
