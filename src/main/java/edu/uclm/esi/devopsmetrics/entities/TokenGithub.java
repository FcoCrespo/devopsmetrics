package edu.uclm.esi.devopsmetrics.entities;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento tokenGithub en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "tokensgithub")
public class TokenGithub {
	/**
	 * ID.
	 * 
	 * @author FcoCrespo
	 */
	@Id
	private String id;
	/**
	 * username.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String owner;
	/**
	 * Password.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String secretT;
	public TokenGithub(@NotNull String owner, @NotNull String secretT) {
		super();
		this.owner = owner;
		this.secretT = secretT;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getSecretT() {
		return secretT;
	}
	public void setSecretT(String secretT) {
		this.secretT = secretT;
	}
	@Override
	public String toString() {
		return "TokenGithub [id=" + id + ", owner=" + owner + ", secretT=" + secretT + "]";
	}
	
	
	
}
