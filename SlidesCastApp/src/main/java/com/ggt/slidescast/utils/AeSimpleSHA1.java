package com.ggt.slidescast.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Sha1 utils.
 *
 * @author guiguito
 */
public class AeSimpleSHA1 {

    public static String sha12(String s) throws NoSuchAlgorithmException {
        MessageDigest digest = null;
        digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        byte[] data = digest.digest(s.getBytes());
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1, data));
    }
}