package edu.uclm.esi.devopsmetrics.models;

import java.time.Instant;

/**
 * Documento usuario en la base de datos.
 * 
 * @author FcoCrespo
 */

public class SecureUser implements Comparable<SecureUser> {
	/**
	 * ID.
	 * 
	 * @author FcoCrespo
	 */
	private String id;
	/**
	 * username.
	 * 
	 * @author FcoCrespo
	 */
	private String username;

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
	public SecureUser(String id, String username, String role, String tokenPass, Instant tokenValidity) {
		this.id = id;
		this.username = username;
		this.role = role;
		this.tokenPass = tokenPass;
		this.tokenValidity = tokenValidity;
	}

	@Override
	public int compareTo(SecureUser o) {
		return this.getUsername().compareTo(o.getUsername());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		SecureUser other = (SecureUser) obj;
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

	public Instant getTokenValidity() {
		return tokenValidity;
	}

	public void setTokenValidity(Instant tokenValidity) {
		this.tokenValidity = tokenValidity;
	}

	@Override
	public String toString() {
		return "SecureUser [id=" + id + ", username=" + username + ", role=" + role + ", tokenPass=" + tokenPass
				+ ", tokenValidity=" + tokenValidity + "]";
	}

}