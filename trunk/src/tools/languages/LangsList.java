/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.languages;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author ssoldatos
 */
public class LangsList {

  private static ArrayList<Language> list;
  public static Language NONE = new Language("None", "", Language.NO_PRIORITY, 0);
  public static Language MULTIPLE = new Language("Multiple", "", Language.NO_PRIORITY, 3);

  public LangsList() {
    list = new ArrayList<Language>();
    list.add(new Language("Arabic", "ar", Language.NO_PRIORITY, 4));
    list.add(new Language("Brazilian", "br", Language.NO_PRIORITY, 5));
    list.add(new Language("Bulgarian", "bg", Language.NO_PRIORITY, 6));
    list.add(new Language("Dutch", "nl", Language.NO_PRIORITY, 7));
    list.add(new Language("English", "en", Language.NO_PRIORITY, 1));
    list.add(new Language("French", "fr", Language.NO_PRIORITY, 8));
    list.add(new Language("German", "de", Language.NO_PRIORITY, 9));
    list.add(new Language("Greek", "gr", Language.NO_PRIORITY, 2));
    list.add(new Language("Hungarian", "hu", Language.NO_PRIORITY, 10));
    list.add(new Language("Italian", "it", Language.NO_PRIORITY, 11));
    list.add(new Language("Korean", "ko", Language.NO_PRIORITY, 12));
    list.add(new Language("Polish", "pl", Language.NO_PRIORITY, 13));
    list.add(new Language("Portuguese", "pt", Language.NO_PRIORITY, 14));
    list.add(new Language("Romanian", "ro", Language.NO_PRIORITY, 15));
    list.add(new Language("Russian", "ru", Language.NO_PRIORITY, 16));
    list.add(new Language("Spanish", "es", Language.NO_PRIORITY, 17));
    list.add(new Language("Turkish", "tr", Language.NO_PRIORITY, 18));
    list.add(new Language("Ukrainian", "ua", Language.NO_PRIORITY, 19));
  }

  /**
   * @return the langs
   */
  public ArrayList<Language> getLangs() {
    return list;
  }

  public void addLanguage(Language lang) {
    getLangs().add(lang);
  }

  public Language getPrimary() {
    for (Iterator<Language> it = list.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language.isIsPrimary()) {
        return language;
      }
    }
    return getDefaultLanguage(Language.PRIMARY);
  }

  public Language getSecondary() {
    for (Iterator<Language> it = list.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language.isIsSecondary()) {
        return language;
      }
    }
    return getDefaultLanguage(Language.SECONDARY);
  }

  public void setPrimary(Language lang) {
    for (Iterator<Language> it = list.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language == lang) {
        language.setIsPrimary(true);
        language.setIsSecondary(false);
      } else {
        language.setIsPrimary(false);
      }
    }
  }

  public void setSecondary(Language lang) {
    for (Iterator<Language> it = list.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language == lang) {
        language.setIsSecondary(true);
        language.setIsPrimary(false);
      } else {
        language.setIsSecondary(false);
      }
    }
  }

  public static Language getLanguageByName(String name) {
    for (Iterator<Language> it = list.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language.getName().equals(name)) {
        return language;
      }
    }
    return null;
  }

  public static Language getLanguageById(int id) {
    if (id == 0) {
      return NONE;
    }
    if (id == 3) {
      return MULTIPLE;
    }
    for (Iterator<Language> it = list.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language.getId() == id) {
        return language;
      }
    }
    return null;
  }

  private Language getDefaultLanguage(int priority) {
    return list.get(priority);
  }

  public static boolean isLanguageCode(String code) {
    for (Iterator<Language> it = list.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language.getCode().equals(code)) {
        return true;
      }
    }
    return false;
  }
}
