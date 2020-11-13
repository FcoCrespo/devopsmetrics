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
	private String username;
	/**
	 * Password.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String password;
	/**
	 * Role.
	 * 
	 * @author FcoCrespo
	 */
	private String role;

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
	public User(@NotNull final String username, @NotNull final String password, final String role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}

	@Override
	public int compareTo(User o) {
		return this.getUsername().compareTo(o.getUsername());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		User other = (User) obj;
		return this.getUsername() == other.getUsername();

	}

	@Override
	public int hashCode() {
		return this.hashCode();
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role
				+ ", tokenPass=" + tokenPass + ", tokenValidity=" + tokenValidity + "]";
	}

}
