/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.languages;

import java.util.ArrayList;
import java.util.Iterator;
import tools.options.Options;

/**
 *
 * @author ssoldatos
 */
public class LangsList {
  private static ArrayList<Language> list;

   public LangsList() {
    list = new ArrayList<Language>();
    Language greek = new Language("Greek", "gr",Language.NO_PRIORITY);
    greek.setIsPrimary( Options.toString(Options.PRIMARY_SUB).equals("Greek"));
    Language english = new Language("English", "en",Language.NO_PRIORITY);
    english.setIsPrimary( Options.toString(Options.PRIMARY_SUB).equals("English"));
    Language german = new Language("German", "ge",Language.NO_PRIORITY);
    german.setIsPrimary( Options.toString(Options.PRIMARY_SUB).equals("German"));
    german.setIsSecondary(true);
    list.add(greek);
    list.add(english);
    list.add(german);
  }

  /**
   * @return the langs
   */
  public ArrayList<Language> getLangs() {
    return list;
  }

  public void addLanguage(Language lang){
    getLangs().add(lang);
  }

  public Language getPrimary(){
    for (Iterator<Language> it = list.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language.isIsPrimary()){
        return language;
      }
    }
    return getDefaultLanguage(Language.PRIMARY);
  }

  public Language getSecondary(){
    for (Iterator<Language> it = list.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language.isIsSecondary()){
        return language;
      }
    }
    return getDefaultLanguage(Language.SECONDARY);
  }

  public void setPrimary(Language lang){
    for (Iterator<Language> it = list.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language == lang){
        language.setIsPrimary(true);
        language.setIsSecondary(false);
      } else {
        language.setIsPrimary(false);
      }
    }
  }

  public void setSecondary(Language lang){
    for (Iterator<Language> it = list.iterator(); it.hasNext();) {
      Language language = it.next();
      if (language == lang){
        language.setIsSecondary(true);
        language.setIsPrimary(false);
      } else {
        language.setIsSecondary(false);
      }
    }
  }

  private Language getDefaultLanguage(int priority) {
    return list.get(priority);
  }
}
