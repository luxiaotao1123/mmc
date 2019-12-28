package com.cool.mmc.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class KeyedDigest {

    private static final String MD5 = "MD5";
    private static final String SHA1 = "SHA1";

    public static String getKeyedDigestMD5(String strSrc){
        return getKeyedDigestMD5(strSrc, "");
    }

    public static String getKeyedDigestMD5(String strSrc, String key) {
        return getKeyedDigest(MD5, strSrc, key);
    }

    public static String getKeyedDigestSHA1(String strSrc){
        return getKeyedDigestSHA1(strSrc, "");
    }

    public static String getKeyedDigestSHA1(String strSrc, String key) {
        return getKeyedDigest(SHA1, strSrc, key);
    }

    private static String getKeyedDigest(String algorithm, String strSrc, String key) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(strSrc.getBytes(StandardCharsets.UTF_8));
            return getFormattedText(messageDigest.digest(key.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getFormattedText(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(Integer.toHexString((0x000000ff & aByte) | 0xffffff00).substring(6));
        }
        return result.toString();
    }

}
