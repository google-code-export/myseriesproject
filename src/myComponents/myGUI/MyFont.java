/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myComponents.myGUI;

import com.googlecode.soptions.Option;
import java.awt.Font;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.xml.parsers.ParserConfigurationException;
import myseriesproject.MySeries;
import org.xml.sax.SAXException;
import tools.MySeriesLogger;
import tools.options.MySeriesOptions;

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

  
  /**
   * Sets the font for the application elements
   */
  public static void  SetMyFont(){

    String fontFace = MySeries.options.getStringOption(MySeriesOptions.FONT_FACE).equals("null") ? DEFAULT_FONT : MySeries.options.getStringOption(MySeriesOptions.FONT_FACE);
    float fontSize = MySeries.options.getFloatOption(MySeriesOptions.FONT_SIZE) == 0 ? DEFAULT_SIZE : MySeries.options.getFloatOption(MySeriesOptions.FONT_SIZE);
    MySeries.options.setOption(new Option(MySeriesOptions.FONT_FACE, Option.STRING_CLASS,fontFace));
    MySeries.options.setOption(new Option(MySeriesOptions.FONT_SIZE, Option.FLOAT_CLASS,fontSize));
    try {
      MySeries.options.save();
    } catch (ParserConfigurationException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    } catch (SAXException ex) {
     MySeriesLogger.logger.log(Level.SEVERE, null, ex);
    }
    int plain = Font.PLAIN;
    int bold = Font.BOLD;
    myFont = new FontUIResource(new Font(fontFace,plain,(int)fontSize));
    myBoldFont =  new FontUIResource(myFont.deriveFont(bold));
    mySmallerFont =  new FontUIResource(myFont.deriveFont(fontSize - SIZE_STEP));
    myBiggerFont =  new FontUIResource(myFont.deriveFont(fontSize + SIZE_STEP));
    myBiggerBoldFont =  new FontUIResource(myBiggerFont.deriveFont(bold));
    myBiggestFont =  new FontUIResource(myFont.deriveFont(fontSize + (2*SIZE_STEP)));
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
    UIManager.put("MenuBar.font", myBiggerBoldFont);
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
