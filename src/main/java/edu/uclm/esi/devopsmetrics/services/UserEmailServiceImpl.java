package edu.uclm.esi.devopsmetrics.services;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.exceptions.UserEmailNotFoundException;
import edu.uclm.esi.devopsmetrics.models.UserEmail;
import edu.uclm.esi.devopsmetrics.repositories.UserEmailRepository;
import edu.uclm.esi.devopsmetrics.utilities.Utilities;

@Service("UserEmailService")
/**
 * @author FcoCrespo
 */
@Transactional

public class UserEmailServiceImpl implements UserEmailService{
	
	/**
	   * @author FcoCrespo
	   */
	  private UserEmailRepository userEmailRepository;
	  private Utilities utilities;

	  /**
	   * @author FcoCrespo
	   */
	  @Autowired

	  public UserEmailServiceImpl(final UserEmailRepository userEmailRepository, final Utilities utilities) {

	    this.userEmailRepository = userEmailRepository;
	    this.utilities = utilities;

	  }

	@Override
	public List<UserEmail> findAll() {
		final Optional<List<UserEmail>> usersEmail = userEmailRepository.findAll();

	    final List<UserEmail> userEmailList = new ArrayList<UserEmail>();
	    
	    if(usersEmail.isPresent()) {
	    	
	    	UserEmail userEmail;
	    	for (int i = 0; i < usersEmail.get().size(); i++) {
	    		userEmail = usersEmail.get().get(i);
	    		userEmailList.add(userEmail);
	        }

	        return userEmailList;
	    }
	    else {
	    	return Collections.emptyList();
	    }
	}

	@Override
	public void saveUserEmail(UserEmail userEmail) {
		this.userEmailRepository.saveUserEmail(userEmail);
		
	}

	@Override
	public void updateUserEmail(UserEmail userEmail) {
		this.userEmailRepository.updateUserEmail(userEmail);
		
	}

	@Override
	public void deleteUserGithubRepos(String username) {
		this.userEmailRepository.deleteUserEmail(username);
		
	}

	@Override
	public UserEmail findByUsernameAndEmail(String username, String email) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List <UserEmail> usuarios = findAll();
		
		String userdesencriptado;
		String emaildesencriptado;
		
		UserEmail user = null;
		
		boolean seguir = true;
		for(int i = 0; i<usuarios.size()&&seguir; i++) {
	               
			userdesencriptado = this.utilities.desencriptar(usuarios.get(i).getUsername());
			emaildesencriptado = this.utilities.desencriptar(usuarios.get(i).getEmail());
			if(username.equals(userdesencriptado) && email.equals(emaildesencriptado)) {
				user = usuarios.get(i);
				seguir=false;
			}
			
		}
	    if(user!=null) {
	    	return user;
	    }
	    else {
	    	throw new UserEmailNotFoundException(username, email);
	    }
	}

	@Override
	public UserEmail findByUsername(String username) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List <UserEmail> usuarios = findAll();
		
		String userdesencriptado;
		
		UserEmail user = null;
		
		boolean seguir = true;
		for(int i = 0; i<usuarios.size()&&seguir; i++) {
	               
			userdesencriptado = this.utilities.desencriptar(usuarios.get(i).getUsername());
			if(username.equals(userdesencriptado)) {
				user = usuarios.get(i);
				seguir=false;
			}
			
		}
	    if(user!=null) {
	    	return user;
	    }
	    else {
	    	return null;
	    }
	}

}
