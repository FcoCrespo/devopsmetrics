package edu.uclm.esi.devopsmetrics.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.exceptions.TokenGithubNotFoundException;
import edu.uclm.esi.devopsmetrics.models.TokenGithub;
import edu.uclm.esi.devopsmetrics.repositories.TokenGithubRepository;

@Service("TokenGithubService")
/**
 * @author FcoCrespo
 */
@Transactional

public class TokenGithubServiceImpl implements TokenGithubService{
	
	/**
	   * @author FcoCrespo
	   */
	  private TokenGithubRepository tokenGithubRepository;

	  /**
	   * @author FcoCrespo
	   */
	  @Autowired

	  public TokenGithubServiceImpl(final TokenGithubRepository tokenGithubRepository) {

	    this.tokenGithubRepository = tokenGithubRepository;

	  }

	@Override
	public List<TokenGithub> findAll() {
		return this.tokenGithubRepository.findAll();
	}

	@Override
	public void saveTokenGithub(TokenGithub tokenGithub) {
		this.tokenGithubRepository.saveTokenGithub(tokenGithub);
	}

	@Override
	public void updateTokenGithub(TokenGithub tokenGithub) {
		this.tokenGithubRepository.saveTokenGithub(tokenGithub);
	}

	@Override
	public void deleteTokenGithub(String tokenGithub) {
		this.tokenGithubRepository.deleteTokenGithub(tokenGithub);
	}

	@Override
	public TokenGithub findByOwner(String owner) {
		
		TokenGithub tokenGithub = this.tokenGithubRepository.findByOwner(owner);
		
		if(tokenGithub != null) {
			return tokenGithub;
		}
		else {
			throw new TokenGithubNotFoundException(owner);
		}
	}

	@Override
	public TokenGithub findByOwnerAndToken(String owner, String secretT) {
		return this.tokenGithubRepository.findByOwnerAndToken(owner, secretT);
	}

}
