package edu.uclm.esi.devopsmetrics.utilities;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class Utilities {
	
	private final KeyValue keyvalue;
	
	protected Utilities(KeyValue keyvalue) {
		this.keyvalue = keyvalue;
	}
	
	private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
	
	public String encriptar(String privateString) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		
		byte[] iv = new byte[GCM_IV_LENGTH];
        (new SecureRandom()).nextBytes(iv);
        
        String secretKey = this.keyvalue.getSecret();
    	MessageDigest md = MessageDigest.getInstance("SHA-256");
    	byte[] digestOfPassword = md.digest(secretKey.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        SecretKey key = new SecretKeySpec(keyBytes, "AES"); 

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] ciphertext = cipher.doFinal(privateString.getBytes(StandardCharsets.UTF_8));
        byte[] encrypted = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, encrypted, 0, iv.length);
        System.arraycopy(ciphertext, 0, encrypted, iv.length, ciphertext.length);

        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String desencriptar(String encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException  {
    	
    	String secretKey = this.keyvalue.getSecret();
    	MessageDigest md = MessageDigest.getInstance("SHA-256");
    	byte[] digestOfPassword = md.digest(secretKey.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        
        SecretKey key = new SecretKeySpec(keyBytes, "AES"); 
        
    	byte[] decoded = Base64.getDecoder().decode(encrypted);

        byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        byte[] ciphertext = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.length - GCM_IV_LENGTH);

        return new String(ciphertext, StandardCharsets.UTF_8);
    }
    
}
