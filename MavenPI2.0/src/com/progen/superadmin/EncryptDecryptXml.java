/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.superadmin;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 *
 * @author progen
 */
public class EncryptDecryptXml {

    public String encryptXml(String xmlData) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("ProGen");
        String encryptedText = textEncryptor.encrypt(xmlData);
        return encryptedText;
    }

    public String decryptXml(String decryptXml) {
        BasicTextEncryptor textDecryptor = new BasicTextEncryptor();
        textDecryptor.setPassword("ProGen");
        String xml = textDecryptor.decrypt(decryptXml);
        return xml;
    }
}
