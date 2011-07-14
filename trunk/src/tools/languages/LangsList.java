/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.languages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import tools.MySeriesLogger;

/**
 *
 * @author ssoldatos
 */
public class LangsList {

  private static ArrayList<Language> list;
  public static Language NONE = new Language("None", "", Language.NO_PRIORITY, 0);
  public static Language MULTIPLE = new Language("Multiple", "", Language.NO_PRIORITY, 3);

  public LangsList() {
    MySeriesLogger.logger.log(Level.INFO, "Creating languages list");
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
    MySeriesLogger.logger.log(Level.INFO, "Getting primary language");
    for (Iterator<Language> it = list.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language.isIsPrimary()) {
        MySeriesLogger.logger.log(Level.FINE, "Primary language {0}",language.getName());
        return language;
      }
    }
    MySeriesLogger.logger.log(Level.INFO, "Primary language not found , setting default {0}",
        getDefaultLanguage(Language.PRIMARY).getName());
    return getDefaultLanguage(Language.PRIMARY);
  }

  public Language getSecondary() {
    MySeriesLogger.logger.log(Level.INFO, "Getting secondary language");
    for (Iterator<Language> it = list.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language.isIsSecondary()) {
        MySeriesLogger.logger.log(Level.FINE, "Secondary language {0}",language.getName());
        return language;
      }
    }
    MySeriesLogger.logger.log(Level.INFO, "Secondary language not found , setting default {0}",
        getDefaultLanguage(Language.SECONDARY).getName());
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
    MySeriesLogger.logger.log(Level.INFO, "Getting language by name {0}",name);
    for (Iterator<Language> it = list.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language.getName().equals(name)) {
        MySeriesLogger.logger.log(Level.FINE, "Found language {0}",language.getId());
        return language;
      }
    }
    MySeriesLogger.logger.log(Level.INFO, "Language not found");
    return LangsList.NONE;
  }

  public static Language getLanguageByCode(String code) {
    MySeriesLogger.logger.log(Level.INFO, "Getting language by code {0}",code);
     for (Iterator<Language> it = list.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language.getCode().equals(code)) {
        MySeriesLogger.logger.log(Level.FINE, "Found language {0}",language.getId());
        return language;
      }
    }
    MySeriesLogger.logger.log(Level.INFO, "Language not found");
    return LangsList.NONE;
  }

  public static Language getLanguageById(int id) {
    if (id == 0) {
      return NONE;
    }
    if (id == 3) {
      return MULTIPLE;
    }
     MySeriesLogger.logger.log(Level.INFO, "Getting language by id {0}",id);
    for (Iterator<Language> it = list.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language.getId() == id) {
        MySeriesLogger.logger.log(Level.FINE, "Found language {0}",language.getName());
        return language;
      }
    }
     MySeriesLogger.logger.log(Level.INFO, "Language not found");
    return null;
  }

  private Language getDefaultLanguage(int priority) {
    return list.get(priority);
  }

  public static boolean isLanguageCode(String code) {
    MySeriesLogger.logger.log(Level.INFO, "Checking if {0} is a language code",code);
    for (Iterator<Language> it = list.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language.getCode().equals(code)) {
        MySeriesLogger.logger.log(Level.FINE, "{0} is a language code : {1}",new String[]{code,language.getName()});
        return true;
      }
    }
    MySeriesLogger.logger.log(Level.INFO, "{0} is not a language code",code);
    return false;
  }
}
