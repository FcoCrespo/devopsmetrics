package edu.uclm.esi.devopsmetrics.entities;

public class SecureUser {

	private String id;
	private String username;
	private String role;
	private String tokenPass;
	private String userGithub;
	public SecureUser(String id, String username, String role, String tokenPass, String userGithub) {
		super();
		this.id = id;
		this.username = username;
		this.role = role;
		this.tokenPass = tokenPass;
		this.userGithub = userGithub;
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
	public void setUserGithub(String userGithub) {
		this.userGithub = userGithub;
	}
	@Override
	public String toString() {
		return "SecureUser [id=" + id + ", username=" + username + ", role=" + role + ", tokenPass=" + tokenPass
				+ ", userGithub=" + userGithub + "]";
	}
	
}
