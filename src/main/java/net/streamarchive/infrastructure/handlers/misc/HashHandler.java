package net.streamarchive.infrastructure.handlers.misc;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class HashHandler {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(HashHandler.class.getName());
    private String key;

    private static String createSignatureWithSHA256(String secret, String payload)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(payload.getBytes());
        return Hex.encodeHexString(rawHmac);
    }

    public void generateKey() {
        key = RandomStringUtils.randomNumeric(8);
        log.trace("key {}", key);
    }

    public boolean compare(String header, String dataModel) {

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

    public String getKey() {
        return key;
    }

}
