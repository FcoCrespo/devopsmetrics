package edu.uclm.esi.devopsmetrics.entities;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento usuario en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "usersemail")
public class UserEmail{
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
	private String username;
	/**
	 * email.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String email;
	public UserEmail(@NotNull String username, @NotNull String email) {
		super();
		this.username = username;
		this.email = email;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "UserEmail [id=" + id + ", username=" + username + ", email=" + email + "]";
	}
	

	
}