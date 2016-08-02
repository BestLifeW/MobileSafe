package com.lovec.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lovec on 2016/8/2.
 */
public class MD5Utils {
    public static String passwordMD5(String pass) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = messageDigest.digest(pass.getBytes());
            for (int i = 0; i < digest.length; i++) {
                int result = digest[i] & 0xff;
                String hexString = Integer.toHexString(result);
                if (hexString.length() < 2) {
                    sb.append("0");
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
