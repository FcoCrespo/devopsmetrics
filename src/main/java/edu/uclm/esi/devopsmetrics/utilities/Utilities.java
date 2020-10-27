package edu.uclm.esi.devopsmetrics.utilities;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

import edu.uclm.esi.devopsmetrics.models.User;
import edu.uclm.esi.devopsmetrics.models.KeyValue;

public class Utilities {
	
	public static String encriptar(String texto) {
		
        String secretKey = KeyValue.getSecret(); //llave para encriptar datos
        
        String base64EncryptedString = "";

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] plainTextBytes = texto.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.encodeBase64(buf);
            base64EncryptedString = new String(base64Bytes);

        } catch (Exception ex) {
        }
        return base64EncryptedString;
    }

    public static String desencriptar(String textoEncriptado) throws Exception {
    	
    	String secretKey = KeyValue.getSecret(); //llave para encriptar datos
   
    	String base64EncryptedString = "";
    	try {
            byte[] message = Base64.decodeBase64(textoEncriptado.getBytes("utf-8"));
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");

            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(Cipher.DECRYPT_MODE, key);

            byte[] plainText = decipher.doFinal(message);

            base64EncryptedString = new String(plainText, "UTF-8");

        } catch (Exception ex) {
        }
        return base64EncryptedString;
    }
    
    public static Optional<User> desencriptarOptionalUser(Optional<User> user) {

        try {

          user.get().setUsername(desencriptar(user.get().getUsername()));
          user.get().setPassword(desencriptar(user.get().getPassword()));
          user.get().setRole(desencriptar(user.get().getRole()));
     
          return user;
          
        } catch (Exception ex) {

          return null;
        }

   }
    
    public static List<User> desencriptarListaUsers(Optional<List<User>> users) {

        final List<User> usersDesencriptado = new ArrayList<User>();

        for (int i = 0; i < users.get().size(); i++) {
          final User usuario = users.get().get(i);
          System.out.println(usuario.toString());
          usersDesencriptado.add(desencriptarUser(usuario));
        }

        return usersDesencriptado;
   }
    
    public static List<User> desencriptarUsers(List<User> users) {

        final List<User> usersDesencriptado = new ArrayList<User>();

        for (int i = 0; i < users.size(); i++) {
          final User usuario = users.get(i);
          System.out.println(usuario.toString());
          usersDesencriptado.add(desencriptarUser(usuario));
        }

        return usersDesencriptado;
      }

    
    public static User desencriptarUser(User user) {

        try {    
          user.setUsername(desencriptar(user.getUsername()));
          user.setRole(desencriptar(user.getRole()));
          return user;
        } catch (Exception ex) {

          return null;
        }

    }
}
