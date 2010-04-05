/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents;

import java.awt.Font;
import javax.swing.UIManager;

/**
 *
 * @author lordovol
 */
public class MyUIManager {

  
  
  public static void  SetMyFont(){
    Font myFont = new Font(Font.DIALOG,Font.PLAIN,12);
    Font myBoldFont = myFont.deriveFont(Font.BOLD);
    UIManager.put("Button.font", myBoldFont);
    UIManager.put("CheckBox.font", myFont);
    UIManager.put("CheckBoxMenuItem.accelerator.font", myFont);
    UIManager.put("CheckBoxMenuItem.font", myFont);
    UIManager.put("ColorChooser.font", myFont);
    UIManager.put("ComboBox.font", myFont);
    UIManager.put("DesktopIcon.font",myFont);
    UIManager.put("EditorPane.font", myFont);
    UIManager.put("FormattedTextField.font", myFont);
    UIManager.put("InternalFrame.title.font", myFont);
    UIManager.put("Label.font", myFont);
    UIManager.put("List.font", myFont);
    UIManager.put("Menu.accelerator.font",myFont);
    UIManager.put("Menu.font", myBoldFont);
    UIManager.put("MenuBar.font", myBoldFont);
    UIManager.put("MenuItem.accelerator.font",myFont);
    UIManager.put("MenuItem.font", myFont);
    UIManager.put("OptionPane.font", myFont);
    UIManager.put("Panel.font", myFont);
    UIManager.put("PasswordField.font", myFont);
    UIManager.put("PopupMenu.font", myFont);
    UIManager.put("ProgressBar.font", myFont);
    UIManager.put("RadioButton.font", myFont);
    UIManager.put("RadioButtonMenuItem.accelerator.font", myFont);
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
    UIManager.put("TitledBorder.font", myFont);
    UIManager.put("ToggleButton.font", myFont);
    UIManager.put("ToolBar.font", myFont);
    UIManager.put("ToolTip.font", myFont);
    UIManager.put("Tree.font", myFont);
    UIManager.put("Viewport.font", myFont);
  }

  private MyUIManager() {
  }

  
}
