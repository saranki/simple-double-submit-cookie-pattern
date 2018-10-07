/**
 * 
 */
package com.sliit.ssd.doublesubmitcookie.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Saranki
 *
 */
@Component
@ConfigurationProperties
public class HashUtil {
	public String convertToHash(String seed) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
        byte[] result = mDigest.digest(seed.getBytes());
        StringBuilder sBuffer = new StringBuilder();

        for (int i = 0; i < result.length; i++) {
            sBuffer.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sBuffer.toString();
    }
}
