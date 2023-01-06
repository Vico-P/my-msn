package com.mymsn.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class MyMsnUtils {
    public static final String TOKEN_SEPERATOR = ";";

    public static final String REGEX_EMAIL = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private static final String aesKeyValue = "mYq3t6w9z$C&F)J@NcRfUjWnZr4u7x!A";

    public static String formatDate(LocalDateTime toFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return toFormat.format(formatter);
    }

    /**
     * Encrypt a string using AES encryption algorithm.
     *
     * @param toEncrypt the password to be encrypted
     * @return the encrypted string
     */
    public static String encrypt(String toEncrypt) {
        String encodedPwd = "";
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(toEncrypt.getBytes());
            encodedPwd = Hex.encodeHexString(encVal);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodedPwd;

    }

    /**
     * Decrypt a string with AES encryption algorithm.
     *
     * @param encryptedData the data to be decrypted
     * @return the decrypted string
     */
    public static String decrypt(String encryptedData) {
        String decodedPWD = "";
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = Hex.decodeHex(encryptedData);
            byte[] decValue = c.doFinal(decordedValue);
            decodedPWD = new String(decValue);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodedPWD;
    }

    /**
     * Generate a new encryption key.
     */
    private static Key generateKey() throws UnsupportedEncodingException {
        SecretKeySpec key = new SecretKeySpec(aesKeyValue.getBytes("UTF-8"), "AES");
        return key;
    }
}
