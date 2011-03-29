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

/**
 * Skin class
 * @author lordovol
 */
public class Skin {

  private static final long limit = -4289918;
  private static Color skinColor;
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
    //skinColor = fixColor(skinColor);

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
    float[] hsbVals = new float[3];
    Color.RGBtoHSB(skinColor.getRed(), skinColor.getGreen(), skinColor.getBlue(), hsbVals);
    brightness = hsbVals[2];
    return brightness;
  }

  /**
   * @return the brighter
   */
  public static Color getColor_2() {
    return skinColor.brighter();
  }

  /**
   * @return the brightest
   */
  public static Color getColor_1() {
    return skinColor.brighter().brighter();
  }

  /**
   * @return the darker
   */
  public static Color getColor_4() {
    return skinColor.darker();
  }

  /**
   * @return the darkest
   */
  public static Color getColor_5() {
    return skinColor.darker().darker();
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
    return getForeGroundColor(Color.white);
  }

  public static Color getSpinnerForegroundColor() {
    return getForeGroundColor(Color.white);
  }

  private static Color getForeGroundColor(Color color) {
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
    UIManager.put("TableHeader.background", getColor_5());
    UIManager.put("TableHeader.foreground", getForeGroundColor(getColor_5()));

    UIManager.put("Table.background", getColor_2());
    UIManager.put("Table.foreground", getForeGroundColor(getColor_2()));

    UIManager.put("Table.selectionBackground", getColor_4());
    UIManager.put("Table.selectionForeground", getForeGroundColor(getColor_4()));

    UIManager.put("TabbedPane.contentAreaColor", getColor_4());
    UIManager.put("TabbedPane.selected", getColor_4());
    UIManager.put("TabbedPane.background", getColor_4());
    UIManager.put("TabbedPane.foreground", getForeGroundColor(getColor_4()));
    UIManager.put("TabbedPane.unselectedBackground", getColor_5());

    UIManager.put("ComboBox.background", getColor_2());
    UIManager.put("ComboBox.foreground", getForeGroundColor(getColor_2()));
    UIManager.put("ComboBox.selectionBackground", getColor_4());
    UIManager.put("ComboBox.selectionForeground", getForeGroundColor(getColor_4()));

    UIManager.put("ComboBox.disabledForeground", getColor_1());
    UIManager.put("ComboBox.buttonShadow", getForeGroundColor(getColor_1()));

    UIManager.put("Button.select", getColor_1());
    UIManager.put("Button.background", getColor_2());
    UIManager.put("Button.foreground", getForeGroundColor(getColor_2()));
    //UIManager.put("Button.margin", new Insets(2, 2, 2, 2));

    UIManager.put("Spinner.background", getColor_1());
    UIManager.put("Spinner.foreground", getSpinnerForegroundColor());

    UIManager.put("Panel.background", skinColor);
    UIManager.put("Panel.foreground", getForeGroundColor(skinColor));
    UIManager.put("OptionPane.background", skinColor);
    UIManager.put("OptionPane.foreground", getForeGroundColor(skinColor));
    UIManager.put("OptionPane.messageForeground", getForeGroundColor(skinColor));

    UIManager.put("Label.foreground", getForeGroundColor(skinColor));
    UIManager.put("CheckBox.foreground", getForeGroundColor(skinColor));
    UIManager.put("OptionPane.background", skinColor);
    UIManager.put("OptionPane.foreground", getForeGroundColor(skinColor));
    UIManager.put("window", skinColor);
    UIManager.put("window.text", getForeGroundColor(skinColor));

    UIManager.put("TextField.background", getColor_2());
    UIManager.put("TextField.foreground", getForeGroundColor(getColor_2()));
    UIManager.put("TextField.selectionBackground", getColor_4());
    UIManager.put("TextField.selectionForeground", getForeGroundColor(getColor_4()));

    UIManager.put("ProgressBar.background", getColor_1());
    UIManager.put("ProgressBar.foreground", getForeGroundColor(getColor_1()));

    UIManager.put("ScrollBar.thumb", getColor_4());

    UIManager.put("ToolBar.floatingBackground",getSkinColor());
    UIManager.put("ToolBar.background",getSkinColor());

    UIManager.put("MenuBar.background", getColor_4());
    UIManager.put("MenuBar.foreground", getForeGroundColor(getColor_4()));
    UIManager.put("Menu.background", getColor_4());
    UIManager.put("Menu.foreground", getForeGroundColor(getColor_4()));
    UIManager.put("MenuItem.background", getColor_4());
    UIManager.put("MenuItem.foreground", getForeGroundColor(getColor_4()));
    UIManager.put("MenuItem.selectionBackground", getColor_1());
    UIManager.put("MenuItem.selectionForeground", getForeGroundColor(getColor_1()));
    UIManager.put("Menu.selectionBackground", getColor_1());

    UIManager.put("PopupMenu.background", getColor_4());
    UIManager.put("PopupMenu.foreground", getForeGroundColor(getColor_4()));

    UIManager.put("SplitPane.background", getColor_5());
    Border bord = BorderFactory.createBevelBorder(BevelBorder.RAISED, skinColor.brighter(), skinColor.darker());
    UIManager.put("SplitPane.dividerSize", 5);
    UIManager.put("SplitPaneDivider.border", bord);
  }
}
