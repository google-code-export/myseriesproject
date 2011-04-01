/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicViewportUI;
import org.jdesktop.swingbinding.adapters.BeanAdapterBase;
import tools.options.Options;

/**
 * Skin class
 * @author lordovol
 */
public class Skin {

  private static final long limit = -4289918;
  private static Color skinColor = new Color(240, 240, 240);;
  private static Color color_1;
  private static Color color_2;
  private static Color color_4;
  private static Color color_5;
  private static int skinRed;
  private static int skinGreen;
  private static int skinBlue;
  private static float brightness;

  /**
   * Create the default skin
   */
  public Skin() {
    skinColor = new Color(240, 240, 240);
    brightness = getBrightness();
    skinColor = fixColor(skinColor);

  }

  /**
   * Create a skin of the specific color
   * @param color The color to use
   */
  public Skin(Color color) {
    skinColor = color;
    brightness = getBrightness();
    skinColor = fixColor(skinColor);

  }

  private Color fixColor(Color color) {
    if (brightness > 0.5) {
      return color;
    }
    int step = (int) (brightness * 50 * 2);
    Color newColor = null;
    skinRed = skinColor.getRed();
    skinGreen = skinColor.getGreen();
    skinBlue = skinColor.getBlue();
    if (skinRed > 255 - step) {
      skinRed = 255 - step;
    } else if (skinRed < step) {
      skinRed = step;
    }
    if (skinGreen > 255 - step) {
      skinGreen = 255 - step;
    } else if (skinGreen < step) {
      skinGreen = step;
    }
    if (skinBlue > 255 - step) {
      skinBlue = 255 - step;
    } else if (skinBlue < step) {
      skinBlue = step;
    }

    return new Color(skinRed, skinGreen, skinBlue);
  }

  /**
   * The brightness of the main Skin
   * @return the brightness of the main skin color
   */
  public float getBrightness() {
    if (skinColor == null) {
      return 1;
    }
    float[] hsbVals = new float[3];
    Color.RGBtoHSB(skinColor.getRed(), skinColor.getGreen(), skinColor.getBlue(), hsbVals);
    brightness = hsbVals[2];
    return brightness;
  }

  /**
   * @return the brighter
   */
  public static Color getColor_2() {
    return Options.toBoolean(Options.USE_SKIN) ? skinColor.brighter():null;
  }

  /**
   * @return the brightest
   */
  public static Color getColor_1() {
    return Options.toBoolean(Options.USE_SKIN) ? skinColor.brighter().brighter():null;
  }

  /**
   * @return the darker
   */
  public static Color getColor_4() {
    return Options.toBoolean(Options.USE_SKIN) ? skinColor.darker():null;
  }

  /**
   * @return the darkest
   */
  public static Color getColor_5() {
    return Options.toBoolean(Options.USE_SKIN) ? skinColor.darker().darker():null;
  }

  /**
   * @return the skinRed
   */
  public static int getSkinRed() {
    return skinRed;
  }

  /**
   * @return the skinGreen
   */
  public static int getSkinGreen() {
    return skinGreen;
  }

  /**
   * @return the skinBlue
   */
  public static int getSkinBlue() {
    return skinBlue;
  }

  public static Color getSkinColor() {
    return skinColor;
  }

  public static Color getButtonForegroundColor() {
    return Options.toBoolean(Options.USE_SKIN) ? getForeGroundColor(Color.white):null;
  }

  public static Color getSpinnerForegroundColor() {
    return Options.toBoolean(Options.USE_SKIN) ? getForeGroundColor(Color.white):null;
  }

  public static Color getOuterColor() {
    return Options.toBoolean(Options.USE_SKIN) ? getColor_5():Color.GRAY;
  }

  public static Color getInnerColor() {
    return Options.toBoolean(Options.USE_SKIN) ? getColor_1():null;
  }

  public static Color getTitleColor() {
    return Options.toBoolean(Options.USE_SKIN) ? getColor_1() : Color.WHITE;
  }

  public static Color getForeGroundColor(Color color) {
     if(!Options.toBoolean(Options.USE_SKIN)){
         return null;
     }
    float[] hsbVals = new float[3];
    Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbVals);
    brightness = hsbVals[2];
    if (brightness > 0.70) {
      return getColor_5();
    }
    return Color.white;
  }

  /**
   * Apply the skin
   */
  public static void applySkin() {
    /**
     * activeCaption
     */
    UIManager.put("activeCaption", Skin.getColor_2());
    UIManager.put("activeCaptionBorder", Skin.getColor_4());
    UIManager.put("activeCaptionText", Skin.getColor_5());
    /**
     * Button
     */
    UIManager.put("Button.background", Skin.getColor_2());
    UIManager.put("Button.darkShadow", Skin.getColor_4());
    UIManager.put("Button.disabledText", Skin.getColor_1());
    UIManager.put("Button.disabledToolBarBorderBackground", Skin.getColor_2());
    UIManager.put("Button.focus", Skin.getColor_2());
    UIManager.put("Button.foreground", Skin.getForeGroundColor(getColor_2()));
    UIManager.put("Button.highlight", Skin.getColor_2());
    UIManager.put("Button.light", Skin.getColor_1());
    UIManager.put("Button.select", Skin.getColor_4());
    UIManager.put("Button.shadow", Skin.getColor_4());
    UIManager.put("Button.toolBarBorderBackground", Skin.getColor_5());

    /**
     * Checkbox
     */
    UIManager.put("CheckBox.background", Skin.getSkinColor());
    UIManager.put("CheckBox.disabledText", Skin.getColor_1());
    UIManager.put("CheckBox.focus", Skin.getColor_2());
    UIManager.put("CheckBox.foreground", Skin.getForeGroundColor(getSkinColor()));
    UIManager.put("Checkbox.select", Skin.getColor_4());

    /**
     * Checkbox menu item
     */
    UIManager.put("CheckBoxMenuItem.acceleratorForeground", Skin.getColor_5());
    UIManager.put("CheckBoxMenuItem.acceleratorSelectionForeground", Skin.getColor_1());
    UIManager.put("CheckBoxMenuItem.background", Skin.getSkinColor());
    UIManager.put("CheckBoxMenuItem.disabledForeground", Skin.getColor_1());
    UIManager.put("CheckBoxMenuItem.foreground", getForeGroundColor(getSkinColor()));
    UIManager.put("CheckBoxMenuItem.selectionBackground", Skin.getColor_5());
    UIManager.put("CheckBoxMenuItem.selectionForeground", getForeGroundColor(getColor_5()));

    /**
     * Color chooser
     */
    UIManager.put("ColorChooser.background", Skin.getColor_2());
    UIManager.put("ColorChooser.foreground", getForeGroundColor(getColor_2()));
    UIManager.put("ColorChooser.swatchesDefaultRecentColor", Skin.getSkinColor());

    /**
     * Combo
     */
    UIManager.put("ComboBox.background", Skin.getColor_2());
    UIManager.put("ComboBox.buttonBackground", Skin.getColor_4());
    UIManager.put("ComboBox.buttonDarkShadow", Skin.getColor_5());
    UIManager.put("ComboBox.buttonHighlight", Skin.getColor_1());
    UIManager.put("ComboBox.buttonShadow", Skin.getColor_4());
    UIManager.put("ComboBox.disabledBackground", Skin.getColor_2());
    UIManager.put("ComboBox.disabledForeground", Skin.getColor_4());
    UIManager.put("ComboBox.foreground", Skin.getForeGroundColor(Skin.getColor_2()));
    UIManager.put("ComboBox.selectionBackground", Skin.getColor_4());
    UIManager.put("ComboBox.selectionForeground", Skin.getForeGroundColor(getColor_4()));

    /**
     * Control
     */
    UIManager.put("control", Skin.getColor_2());
    UIManager.put("controlDkShadow", Skin.getColor_5());
    UIManager.put("controlHighlight", Skin.getColor_2());
    UIManager.put("controlLtHighlight", Skin.getColor_1());
    UIManager.put("controlShadow", Skin.getColor_4());
    UIManager.put("controlText", getForeGroundColor(getSkinColor()));

    /**
     * Desktop
     */
    UIManager.put("desktop", Skin.getColor_1());
    UIManager.put("Desktop.background", Skin.getColor_1());
    UIManager.put("DesktopIcon.background", Skin.getColor_2());
    UIManager.put("DesktopIcon.foreground", Skin.getColor_5());


    /**
     * Editor Pane
     */
    UIManager.put("EditorPane.background", Color.WHITE);
    UIManager.put("EditorPane.caretForeground", Skin.getColor_5());
    UIManager.put("EditorPane.foreground", Color.BLACK);
    UIManager.put("EditorPane.inactiveForeground", Skin.getColor_4());
    UIManager.put("EditorPane.selectionBackground", Skin.getColor_4());
    UIManager.put("EditorPane.selectionForeground", Skin.getColor_2());

    /**
     * Formated text field
     */
    UIManager.put("FormattedTextField.background", Skin.getColor_1());
    UIManager.put("FormattedTextField.caretForeground", Skin.getColor_5());
    UIManager.put("FormattedTextField.foreground", Skin.getColor_5());
    UIManager.put("FormattedTextField.inactiveBackground", Skin.getColor_2());
    UIManager.put("FormattedTextField.inactiveForeground", Skin.getColor_4());
    UIManager.put("FormattedTextField.selectionBackground", Skin.getColor_5());
    UIManager.put("FormattedTextField.selectionForeground", Skin.getColor_1());

    /**
     * Inactive caption
     */
    UIManager.put("inactiveCaption", Skin.getColor_2());
    UIManager.put("inactiveCaptionBorder", Skin.getSkinColor());
    UIManager.put("inactiveCaptionText", getForeGroundColor(getSkinColor()));
    /**
     * Info
     */
    UIManager.put("info", Skin.getSkinColor());
    UIManager.put("infoText", getForeGroundColor(getSkinColor()));

    /**
     * Internal frame
     */
    UIManager.put("InternalFrame.activeTitleBackground", Skin.getColor_2());
    UIManager.put("InternalFrame.activeTitleForeground", getForeGroundColor(getColor_2()));
    UIManager.put("InternalFrame.borderColor", Skin.getColor_4());
    UIManager.put("InternalFrame.borderDarkShadow", Skin.getColor_5());
    UIManager.put("InternalFrame.borderHighlight", Skin.getColor_2());
    UIManager.put("InternalFrame.borderLight", Skin.getColor_1());
    UIManager.put("InternalFrame.borderShadow", Skin.getColor_4());
    UIManager.put("InternalFrame.inactiveTitleBackground", Skin.getColor_1());
    UIManager.put("InternalFrame.inactiveTitleForeground", Skin.getColor_2());

    /**
     * Label
     */
    UIManager.put("Label.background", Skin.getSkinColor());
    UIManager.put("Label.disabledForeground", Skin.getColor_4());
    UIManager.put("Label.disabledShadow", Skin.getColor_2());
    UIManager.put("Label.foreground", getForeGroundColor(getSkinColor()));


    /**
     * List
     */
    UIManager.put("List.background", Skin.getColor_1());
    UIManager.put("List.dropCellBackground", Skin.getColor_2());
    UIManager.put("List.dropLineColor", Skin.getSkinColor());
    UIManager.put("List.foreground", getForeGroundColor(getColor_1()));
    UIManager.put("List.selectionBackground", Skin.getColor_4());
    UIManager.put("List.selectionForeground", getForeGroundColor(getColor_4()));

    /**
     * Menu
     */
    UIManager.put("menu", Skin.getColor_4());
    UIManager.put("Menu.background", Skin.getColor_4());
    UIManager.put("Menu.foreground", getForeGroundColor(getColor_4()));
    UIManager.put("Menu.selectionBackground", Skin.getColor_1());
    UIManager.put("Menu.selectionForeground", getForeGroundColor(getColor_1()));
    UIManager.put("Menu.acceleratorForeground", getForeGroundColor(getColor_1()));
    UIManager.put("Menu.acceleratorSelectionForeground", getForeGroundColor(getColor_4()));
    UIManager.put("Menu.disabledForeground", Skin.getColor_2());

    /**
     * MenuBar
     */
    UIManager.put("MenuBar.background", Skin.getColor_4());
    UIManager.put("MenuBar.borderColor", Skin.getColor_5());
    UIManager.put("MenuBar.foreground", getForeGroundColor(getColor_4()));
    UIManager.put("MenuBar.highlight", Skin.getColor_2());
    UIManager.put("MenuBar.shadow", Skin.getColor_4());

    /**
     * Menu Item
     */
    UIManager.put("MenuItem.background", Skin.getColor_4());
    UIManager.put("MenuItem.foreground", getForeGroundColor(getColor_4()));
    UIManager.put("MenuItem.acceleratorForeground", getForeGroundColor(getColor_4()));
    UIManager.put("MenuItem.selectionBackground", Skin.getColor_1());
    UIManager.put("MenuItem.selectionForeground", getForeGroundColor(getColor_1()));
    UIManager.put("MenuItem.acceleratorSelectionForeground", getForeGroundColor(getColor_1()));
    UIManager.put("MenuItem.disabledForeground", Skin.getColor_2());
    UIManager.put("menuText", getForeGroundColor(getColor_4()));


    /**
     * Option Pane
     */
    UIManager.put("OptionPane.background", Skin.getSkinColor());
    UIManager.put("OptionPane.errorDialog.border.background", Skin.getColor_5());
    UIManager.put("OptionPane.errorDialog.titlePane.background", Skin.getColor_2());
    UIManager.put("OptionPane.errorDialog.titlePane.foreground", Skin.getColor_4());
    UIManager.put("OptionPane.errorDialog.titlePane.shadow", Skin.getColor_5());
    UIManager.put("OptionPane.foreground", Skin.getColor_5());
    UIManager.put("OptionPane.messageForeground", Skin.getColor_5());
    UIManager.put("OptionPane.questionDialog.border.background", Skin.getColor_5());
    UIManager.put("OptionPane.questionDialog.titlePane.background", Skin.getColor_2());
    UIManager.put("OptionPane.questionDialog.titlePane.foreground", Skin.getColor_4());
    UIManager.put("OptionPane.questionDialog.titlePane.shadow", Skin.getColor_5());
    UIManager.put("OptionPane.warningDialog.border.background", Skin.getColor_5());
    UIManager.put("OptionPane.warningDialog.titlePane.background", Skin.getColor_2());
    UIManager.put("OptionPane.warningDialog.titlePane.foreground", Skin.getColor_4());
    UIManager.put("OptionPane.warningDialog.titlePane.shadow", Skin.getColor_5());

    /**
     * Panel
     */
    UIManager.put("Panel.background", getSkinColor());
    UIManager.put("Panel.foreground", getForeGroundColor(getSkinColor()));

    /**
     * Password
     */
    UIManager.put("PasswordField.background", Skin.getColor_1());
    UIManager.put("PasswordField.caretForeground", Skin.getColor_5());
    UIManager.put("PasswordField.foreground", getForeGroundColor(getColor_1()));
    UIManager.put("PasswordField.inactiveBackground", Skin.getColor_4());
    UIManager.put("PasswordField.inactiveForeground", Skin.getColor_2());
    UIManager.put("PasswordField.selectionBackground", Skin.getColor_5());
    UIManager.put("PasswordField.selectionForeground", getForeGroundColor(getColor_5()));

    /**
     * Popup
     */
    UIManager.put("PopupMenu.background", Skin.getColor_4());
    UIManager.put("PopupMenu.foreground", getForeGroundColor(getColor_4()));

    /**
     * Progress
     */
    UIManager.put("ProgressBar.background", Skin.getColor_1());
    UIManager.put("ProgressBar.foreground", getForeGroundColor(getColor_1()));
    UIManager.put("ProgressBar.selectionBackground", Skin.getColor_5());
    UIManager.put("ProgressBar.selectionForeground", getForeGroundColor(getColor_5()));

    /**
     * Radio button
     */
    UIManager.put("RadioButton.background", Skin.getSkinColor());
    UIManager.put("RadioButton.darkShadow", Skin.getColor_5());
    UIManager.put("RadioButton.disabledText", Skin.getColor_2());
    UIManager.put("RadioButton.focus", Skin.getSkinColor());
    UIManager.put("RadioButton.foreground", getForeGroundColor(getSkinColor()));
    UIManager.put("RadioButton.highlight", Skin.getColor_2());
    UIManager.put("RadioButton.light", Skin.getColor_1());
    UIManager.put("RadioButton.select", Skin.getColor_1());
    UIManager.put("RadioButton.shadow", Skin.getColor_4());
    /**
     * Radio button menu item
     */
    UIManager.put("RadioButtonMenuItem.acceleratorForeground", getForeGroundColor(getColor_4()));
    UIManager.put("RadioButtonMenuItem.acceleratorSelectionForeground", getForeGroundColor(getColor_1()));
    UIManager.put("RadioButtonMenuItem.background", getColor_4());
    UIManager.put("RadioButtonMenuItem.disabledForeground", Skin.getColor_2());
    UIManager.put("RadioButtonMenuItem.foreground", getForeGroundColor(getColor_4()));
    UIManager.put("RadioButtonMenuItem.selectionBackground", Skin.getColor_1());
    UIManager.put("RadioButtonMenuItem.selectionForeground", getForeGroundColor(getColor_1()));

    /**
     * Scrollbar
     */
    UIManager.put("scrollbar", Skin.getColor_1());
    UIManager.put("ScrollBar.background", Skin.getColor_1());
    UIManager.put("ScrollBar.darkShadow", Skin.getColor_5());
    UIManager.put("ScrollBar.foreground", Skin.getColor_4());
    UIManager.put("ScrollBar.highlight", Skin.getColor_1());
    UIManager.put("ScrollBar.shadow", Skin.getColor_4());
    UIManager.put("ScrollBar.thumb", Skin.getSkinColor());
    UIManager.put("ScrollBar.thumbDarkShadow", Skin.getColor_4());
    UIManager.put("ScrollBar.thumbHighlight", Skin.getColor_2());
    UIManager.put("ScrollBar.thumbShadow", Skin.getColor_4());
    UIManager.put("ScrollBar.track", Skin.getColor_2());
    UIManager.put("ScrollBar.trackHighlight", Skin.getColor_1());

    /**
     * ScrollPane
     */
    UIManager.put("ScrollPane.background", Skin.getSkinColor());
    UIManager.put("ScrollPane.foreground", Skin.getForeGroundColor(getSkinColor()));

    /**
     * Seperator
     */
    UIManager.put("Separator.background", Skin.getSkinColor());
    UIManager.put("Separator.foreground", getForeGroundColor(getSkinColor()));
    UIManager.put("Separator.highlight", Skin.getColor_1());
    UIManager.put("Separator.shadow", Skin.getColor_5());


    /**
     * Slider
     */
    UIManager.put("Slider.altTrackColor", Skin.getSkinColor());
    UIManager.put("Slider.background", Skin.getColor_2());
    UIManager.put("Slider.focus", Skin.getColor_1());
    UIManager.put("Slider.foreground", getForeGroundColor(getColor_2()));
    UIManager.put("Slider.highlight", Skin.getColor_1());
    UIManager.put("Slider.shadow", Skin.getColor_4());
    UIManager.put("Slider.tickColor", Skin.getColor_5());

    /**
     * Spinner
     */
    UIManager.put("Spinner.background", Skin.getSkinColor());
    UIManager.put("Spinner.foreground", getForeGroundColor(getSkinColor()));

    /**
     * Split pane
     */
    UIManager.put("SplitPane.background", Skin.getSkinColor());
    UIManager.put("SplitPane.darkShadow", Skin.getColor_5());
    UIManager.put("SplitPane.dividerFocusColor", Skin.getColor_1());
    UIManager.put("SplitPane.highlight", Skin.getColor_1());
    UIManager.put("SplitPane.shadow", Skin.getColor_4());
    UIManager.put("SplitPaneDivider.draggingColor", Skin.getColor_5());

    /**
     * Tabbed Panel
     */
    UIManager.put("TabbedPane.background", getColor_5()); // The unselected tabs tab bg
    UIManager.put("TabbedPane.foreground", getForeGroundColor(getColor_5()));// The unselected tabs tab fg
    UIManager.put("TabbedPane.selected", getColor_4()); // The selected tabs tab bg
    UIManager.put("TabbedPane.tabAreaBackground", getSkinColor()); // The bg of the tab area
    UIManager.put("TabbedPane.contentAreaColor", Skin.getColor_4()); // The visible tab main panel color
    UIManager.put("TabbedPane.light", getSkinColor()); // Tabs tab border
    UIManager.put("TabbedPane.highlight", Skin.getColor_2());
    UIManager.put("TabbedPane.selectHighlight", Skin.getColor_1()); // The selected tabs tab border
    UIManager.put("TabbedPane.borderHightlightColor", Skin.getColor_1());
    UIManager.put("TabbedPane.darkShadow", Skin.getColor_5());
    UIManager.put("TabbedPane.focus", Skin.getColor_2());
    UIManager.put("TabbedPane.shadow", Skin.getColor_4());
    UIManager.put("TabbedPane.unselectedBackground", Skin.getColor_5());

    /**
     * Table
     */
    UIManager.put("Table.background", Skin.getColor_1());
    UIManager.put("Table.foreground", getForeGroundColor(getColor_1()));
    UIManager.put("Table.selectionBackground", Skin.getColor_4());
    UIManager.put("Table.selectionForeground", getForeGroundColor(getColor_4()));
    UIManager.put("Table.gridColor", Skin.getColor_5());
    UIManager.put("Table.sortIconColor", Skin.getColor_1());
    UIManager.put("Table.dropCellBackground", Skin.getColor_2());
    UIManager.put("Table.dropLineColor", Skin.getColor_4());
    UIManager.put("Table.dropLineShortColor", Skin.getColor_4());
    UIManager.put("Table.focusCellBackground", Skin.getColor_4());
    UIManager.put("Table.focusCellForeground", getForeGroundColor(getColor_4()));
 

    /**
     * Table header
     */
    UIManager.put("TableHeader.background", Skin.getColor_5());
    UIManager.put("TableHeader.focusCellBackground", Skin.getColor_1());
    UIManager.put("TableHeader.foreground", getForeGroundColor(getColor_5()));

    /**
     * Text
     */
    UIManager.put("text", getColor_1());
    UIManager.put("textHighlight", Skin.getColor_1());
    UIManager.put("textHighlightText", Skin.getColor_4());
    UIManager.put("textInactiveText", Skin.getColor_2());
    UIManager.put("textText", getForeGroundColor(getColor_1()));


    /**
     * Textarea
     */
    UIManager.put("TextArea.background", Skin.getColor_1());
    UIManager.put("TextArea.caretForeground", Skin.getColor_4());
    UIManager.put("TextArea.foreground", getForeGroundColor(getColor_1()));
    UIManager.put("TextArea.inactiveForeground", Skin.getColor_2());
    UIManager.put("TextArea.selectionBackground", Skin.getColor_4());
    UIManager.put("TextArea.selectionForeground", getForeGroundColor(getColor_4()));

    /**
     * Textfield
     */
    UIManager.put("TextField.background", Skin.getColor_1());
    UIManager.put("TextField.caretForeground", Skin.getColor_4());
    UIManager.put("TextField.darkShadow", Skin.getColor_4());
    UIManager.put("TextField.foreground", getForeGroundColor(getColor_1()));
    UIManager.put("TextField.highlight", Skin.getColor_1());
    UIManager.put("TextField.inactiveBackground", Skin.getColor_2());
    UIManager.put("TextField.inactiveForeground", getForeGroundColor(getColor_2()));
    UIManager.put("TextField.light", Skin.getColor_5());
    UIManager.put("TextField.selectionBackground", Skin.getColor_4());
    UIManager.put("TextField.selectionForeground", getForeGroundColor(getColor_4()));
    UIManager.put("TextField.shadow", Skin.getColor_4());


    /**
     * Text Pane
     */
    UIManager.put("TextPane.background", Skin.getSkinColor());
    UIManager.put("TextPane.caretForeground", Skin.getForeGroundColor(getSkinColor()));
    UIManager.put("TextPane.foreground", Skin.getForeGroundColor(getSkinColor()));
    UIManager.put("TextPane.inactiveForeground", Skin.getColor_4());
    UIManager.put("TextPane.selectionBackground", Skin.getColor_5());
    UIManager.put("TextPane.selectionForeground", Skin.getColor_1());
    /**
     * Title
     */
    UIManager.put("TitledBorder.titleColor", getForeGroundColor(getSkinColor()));

    /**
     * Toggle button
     */
    UIManager.put("ToggleButton.background", Skin.getColor_2());
    UIManager.put("ToggleButton.darkShadow", Skin.getColor_4());
    UIManager.put("ToggleButton.disabledText", Skin.getColor_2());
    UIManager.put("ToggleButton.focus", Skin.getColor_1());
    UIManager.put("ToggleButton.foreground", getForeGroundColor(getColor_2()));
    UIManager.put("ToggleButton.highlight", Skin.getColor_1());
    UIManager.put("ToggleButton.light", Skin.getColor_1());
    UIManager.put("ToggleButton.select", Skin.getColor_4());
    UIManager.put("ToggleButton.shadow", Skin.getColor_5());


    /**
     * Toolbar
     */
    UIManager.put("ToolBar.background", Skin.getSkinColor());
    UIManager.put("ToolBar.borderColor", Skin.getColor_5());
    UIManager.put("ToolBar.darkShadow", Skin.getColor_4());
    UIManager.put("ToolBar.dockingBackground", Skin.getSkinColor());
    UIManager.put("ToolBar.dockingForeground", getForeGroundColor(Skin.getSkinColor()));
    UIManager.put("ToolBar.floatingBackground", Skin.getSkinColor());
    UIManager.put("ToolBar.floatingForeground", getForeGroundColor(Skin.getSkinColor()));
    UIManager.put("ToolBar.foreground", getForeGroundColor(Skin.getSkinColor()));
    UIManager.put("ToolBar.highlight", Skin.getColor_1());
    UIManager.put("ToolBar.light", Skin.getColor_1());
    UIManager.put("ToolBar.shadow", Skin.getColor_4());

    /**
     * Tooltip
     */
    UIManager.put("ToolTip.background", getColor_2());
    UIManager.put("ToolTip.backgroundInactive", getColor_4());
    UIManager.put("ToolTip.foreground", getForeGroundColor(getColor_2()));
    UIManager.put("ToolTip.foregroundInactive", getForeGroundColor(getColor_4()));

    /**
     * Tree
     */
    UIManager.put("Tree.background", Skin.getColor_1());
    UIManager.put("Tree.dropCellBackground", Skin.getColor_4());
    UIManager.put("Tree.dropLineColor", Skin.getColor_4());
    UIManager.put("Tree.foreground", getForeGroundColor(getColor_1()));
    UIManager.put("Tree.hash", Skin.getSkinColor());
    UIManager.put("Tree.line", Skin.getColor_5());
    UIManager.put("Tree.selectionBackground", getSkinColor());
    UIManager.put("Tree.selectionBorderColor", Skin.getColor_5());
    UIManager.put("Tree.selectionForeground", getForeGroundColor(getSkinColor()));
    UIManager.put("Tree.textBackground", getColor_1());
    UIManager.put("Tree.textForeground", getForeGroundColor(getColor_1()));
    /**
     * Viewport
     */
    UIManager.put("Viewport.background", Skin.getSkinColor());
    UIManager.put("Viewport.foreground", getForeGroundColor(getSkinColor()));
    /**
     * Window
     */
    UIManager.put("window", Skin.getSkinColor());
    UIManager.put("windowBorder", Skin.getColor_4());
    UIManager.put("windowText", getForeGroundColor(getSkinColor()));




    applyCommonSkin();

  }

  public static void applyCommonSkin() {
    Border bord = BorderFactory.createBevelBorder(BevelBorder.RAISED, skinColor.brighter(), skinColor.darker());
    UIManager.put("SplitPane.dividerSize", 5);
    UIManager.put("SplitPaneDivider.border", bord);
    UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
    UIManager.put("Table.scrollPaneBorder", BorderFactory.createEmptyBorder());
    UIManager.put("TableHeader.cellBorder", BorderFactory.createLineBorder(Color.GRAY));
  }
}
