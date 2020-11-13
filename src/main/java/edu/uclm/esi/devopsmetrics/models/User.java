package edu.uclm.esi.devopsmetrics.models;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento usuario en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "users")
public class User implements Comparable<User> {
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
	private String usernameUser;
	/**
	 * Password.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String passwordUser;
	/**
	 * Role.
	 * 
	 * @author FcoCrespo
	 */
	private String roleUser;

	/**
	 * tokenPass.
	 * 
	 * @author FcoCrespo
	 */
	private String tokenPass;

	/**
	 * tokenValidity.
	 * 
	 * @author FcoCrespo
	 */
	private Instant tokenValidity;


	/**
	 * Constructor de Usuario.
	 * 
	 * @author FcoCrespo
	 */
	public User(@NotNull final String usernameUser, @NotNull final String passwordUser, final String roleUser) {
		super();
		this.usernameUser = usernameUser;
		this.passwordUser = passwordUser;
		this.roleUser = roleUser;
	}

	@Override
	public int compareTo(User o) {
		return this.getUsernameUser().compareTo(o.getUsernameUser());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		User other = (User) obj;
		return this.getUsernameUser() == other.getUsernameUser();

	}

	@Override
	public int hashCode() {
		return this.hashCode();
	}

	public String getIdUser() {
		return id;
	}

	public void setIdUser(String id) {
		this.id = id;
	}

	public String getUsernameUser() {
		return usernameUser;
	}

	public void setUsernameUser(String usernameUser) {
		this.usernameUser = usernameUser;
	}

	public String getPasswordUser() {
		return passwordUser;
	}

	public void setPasswordUser(String passwordUser) {
		this.passwordUser = passwordUser;
	}

	public String getRoleUser() {
		return roleUser;
	}

	public void setRoleUser(String roleUser) {
		this.roleUser = roleUser;
	}
	
	public String getTokenPass() {
		return tokenPass;
	}

	public void setTokenPass(String tokenPass) {
		this.tokenPass = tokenPass;
	}

	public void newTokenPass() {
		this.tokenPass = UUID.randomUUID().toString();
	}

	public Instant getTokenValidity() {
		return tokenValidity;
	}

	public void setTokenValidity() {
		this.tokenValidity = Instant.now().plus(3, ChronoUnit.HOURS)
				.plus(17, ChronoUnit.MINUTES).plus(41, ChronoUnit.SECONDS);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", usernameUser=" + usernameUser + ", passwordUser=" + passwordUser + ", roleUser="
				+ roleUser + ", tokenPass=" + tokenPass + "]";
	}

	
}
