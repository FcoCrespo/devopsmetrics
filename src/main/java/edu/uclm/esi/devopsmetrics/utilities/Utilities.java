package edu.uclm.esi.devopsmetrics.utilities;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.util.Base64;

import edu.uclm.esi.devopsmetrics.models.User;
import edu.uclm.esi.devopsmetrics.models.KeyValue;

public class Utilities {
	
	private Utilities() {
		
	}
	
	private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
	
	public static String encriptar(String privateString) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		byte[] iv = new byte[GCM_IV_LENGTH];
        (new SecureRandom()).nextBytes(iv);
        
        SecretKey key = new SecretKeySpec(new byte[16], KeyValue.getSecret());

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] ciphertext = cipher.doFinal(privateString.getBytes(StandardCharsets.UTF_8));
        byte[] encrypted = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, encrypted, 0, iv.length);
        System.arraycopy(ciphertext, 0, encrypted, iv.length, ciphertext.length);

        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String desencriptar(String encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException  {
    	
    	SecretKey key = new SecretKeySpec(new byte[16], KeyValue.getSecret());
    	byte[] decoded = Base64.getDecoder().decode(encrypted);

        byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        byte[] ciphertext = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.length - GCM_IV_LENGTH);

        return new String(ciphertext, StandardCharsets.UTF_8);
    }
    
   /* public static Optional<User> desencriptarOptionalUser(Optional<User> user) {

        try {
          if(user.isPresent()) {
        	  user.get().setUsername(desencriptar(user.get().getUsername()));
              user.get().setPassword(desencriptar(user.get().getPassword()));
              user.get().setRole(desencriptar(user.get().getRole()));
         
              return user;
          }
          else {
        	  return Optional.empty();
          }
          
        } catch (Exception ex) {

          return Optional.empty();
        }

   }*/
    
   /* public static List<User> desencriptarListaUsers(Optional<List<User>> users) {

        final List<User> usersDesencriptado = new ArrayList<User>();

        if(users.isPresent()) {
        	for (int i = 0; i < users.get().size(); i++) {
                final User usuario = users.get().get(i);
                usersDesencriptado.add(desencriptarUser(usuario));
           }

           return usersDesencriptado;
        }
        else {
        	return Collections.emptyList();
        }
        
   }*/
    
    /*public static List<User> desencriptarUsers(List<User> users) {

        final List<User> usersDesencriptado = new ArrayList<User>();

        for (int i = 0; i < users.size(); i++) {
          final User usuario = users.get(i);          
          usersDesencriptado.add(desencriptarUser(usuario));
        }

        return usersDesencriptado;
      }*/

    
    /*public static User desencriptarUser(User user) {

        try {    
          user.setUsername(desencriptar(user.getUsername()));
          user.setRole(desencriptar(user.getRole()));
          return user;
        } catch (Exception ex) {

          return null;
        }

    }*/
}
