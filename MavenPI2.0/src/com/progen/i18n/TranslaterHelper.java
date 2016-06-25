/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author progen
 */
public class TranslaterHelper {

    public static String getTranslatedString(String text, Locale locale) {
        ResourceBundle labels = ResourceBundle.getBundle("com.progen.resourcebundle.ProgenLanguageBundle", locale);
        try {
            return labels.getString(text);
        } catch (Exception e) {
            return text;
        }
    }
    //Added By Dinanath for translating hindi, enlish or chinize.

    public static String getTranslatedInLocale(String text, Locale locale) {

        Locale.setDefault(locale);
        ResourceBundle bundle = ResourceBundle.getBundle("com.progen.resourcebundle.MessageBundle");
        try {
            return bundle.getString(text);
        } catch (Exception e) {
            return text;
        }
    }
}
