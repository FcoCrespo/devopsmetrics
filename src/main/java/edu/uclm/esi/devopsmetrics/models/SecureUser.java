package edu.uclm.esi.devopsmetrics.models;

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
	 * userGithub.
	 * 
	 * @author FcoCrespo
	 */
	private String userGithub;

	/**
	 * Constructor de Usuario.
	 * 
	 * @author FcoCrespo
	 */
	public SecureUser(String id, String username, String role, String tokenPass, String userGithub) {
		this.id = id;
		this.username = username;
		this.role = role;
		this.tokenPass = tokenPass;
		this.userGithub = userGithub;
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

	public String getUserGithub() {
		return userGithub;
	}

	public void setTokenValidity(String newUserGithub) {
		this.userGithub = newUserGithub;
	}

	@Override
	public String toString() {
		return "SecureUser [id=" + id + ", username=" + username + ", role=" + role + ", tokenPass=" + tokenPass
				+ ", userGithub=" + userGithub + "]";
	}

}