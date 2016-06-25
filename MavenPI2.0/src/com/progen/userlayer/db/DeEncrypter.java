/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.userlayer.db;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.log4j.Logger;

public final class DeEncrypter {

    private static DeEncrypter instance = new DeEncrypter();
    private Cipher cipher;
    private Key key;
    public static Logger logger = Logger.getLogger(DeEncrypter.class);
//	private final BASE64Encoder b64Encoder = new BASE64Encoder();
//	private final Base64 b64Decoder = Base64.;

    public static DeEncrypter getInstance() {

        return DeEncrypter.instance;
    }

    private DeEncrypter() {

        try {
            this.cipher = Cipher.getInstance("AES");
            byte[] raw = {(byte) 0xA5, (byte) 0x01, (byte) 0x7B, (byte) 0xE5,
                (byte) 0x23, (byte) 0xCA, (byte) 0xD4, (byte) 0xD2,
                (byte) 0xC6, (byte) 0x5F, (byte) 0x7D, (byte) 0x8B,
                (byte) 0x0B, (byte) 0x9A, (byte) 0x3C, (byte) 0xF1};
            this.key = new SecretKeySpec(raw, "AES");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Exception: ", e);
        } catch (NoSuchPaddingException e) {
            logger.error("Exception: ", e);
        }
    }

    public String encrypt(String aData) {
        String result = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            if (aData != "" && aData != null) {
                byte[] utf8 = aData.getBytes("UTF8");
                byte[] encryptedData = cipher.doFinal(utf8);
                result = new sun.misc.BASE64Encoder().encode(encryptedData);
            }
        } catch (InvalidKeyException oException) {
            logger.error("Exception: ", oException);
        } catch (IllegalBlockSizeException oException) {
            logger.error("Exception: ", oException);
        } catch (BadPaddingException oException) {
            logger.error("Exception: ", oException);
        } catch (IOException oException) {
            logger.error("Exception: ", oException);
        }
        return result;
    }

    public String decrypt(String aData) {
        String result = "";

        //byte[] bData =null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            if (aData != null) {
                byte[] decodedData = new sun.misc.BASE64Decoder().decodeBuffer(aData);
                byte[] utf8 = cipher.doFinal(decodedData);
                result = new String(utf8, "UTF8");
            }
        } catch (InvalidKeyException oException) {
            logger.error("Exception: ", oException);
        } catch (Exception oException) {
            logger.error("Exception: ", oException);
        }
        return result;
    }
}