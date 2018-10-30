package com.pingwinno.infrastructure;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class HashHandler {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(HashHandler.class.getName());
    private static String key;


    public static void generateKey() {

        key = RandomStringUtils.randomAlphanumeric(20);
        log.trace("key {}", key);

    }

    public static boolean compare(String header, String dataModel) {

        log.trace("header {} ", header);


        String sha256hex = null;
        try {
            sha256hex = "sha256=" +
                    createSignatureWithSHA256(key, dataModel);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        log.trace("computed hash {} ", sha256hex);
        return header.equals(sha256hex);
    }

    public static String getKey() {
        return key;
    }


    private static String createSignatureWithSHA256(String secret, String payload)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(payload.getBytes());
        return Hex.encodeHexString(rawHmac);
    }

}
