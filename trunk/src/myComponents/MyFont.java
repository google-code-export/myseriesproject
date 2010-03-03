/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents;

import java.awt.Font;
import javax.swing.UIManager;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class MyFont {

  
  
  public static void  SetMyFont(){

    String fontFace = Options.toString(Options.FONT_FACE).equals("null") ? "SansSerif" : Options.toString(Options.FONT_FACE);
    int fontSize = Options.toInt(Options.FONT_SIZE) == 0 ? 12 : Options.toInt(Options.FONT_SIZE);
    Options.setOption(Options.FONT_FACE, fontFace);
    Options.setOption(Options.FONT_SIZE, fontSize);
    Options.save();
    int plain = Font.PLAIN;
    int bold = Font.BOLD;
    Font myFont = new Font(fontFace,plain,fontSize);
    Font myBoldFont = myFont.deriveFont(bold);
    Font mySmallerFont = myFont.deriveFont((float)(fontSize -1));
    Font myBiggerFont = myFont.deriveFont((float)(fontSize +1));
    Font myBiggerBoldFont = myBiggerFont.deriveFont(bold);
    Font myBiggestFont = myFont.deriveFont((float)(fontSize +4));
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
