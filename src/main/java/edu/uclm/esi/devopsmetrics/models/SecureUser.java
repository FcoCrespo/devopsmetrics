package edu.uclm.esi.devopsmetrics.models;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import edu.uclm.esi.devopsmetrics.utilities.Utilities;

/**
 * Documento usuario en la base de datos.
 * 
 * @author FcoCrespo
 */

public class SecureUser {
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
    this.id=id;
    this.username=username;
    this.role=role;
    this.tokenPass=tokenPass;
    this.tokenValidity=tokenValidity;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((tokenPass == null) ? 0 : tokenPass.hashCode());
		result = prime * result + ((tokenValidity == null) ? 0 : tokenValidity.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SecureUser other = (SecureUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (tokenPass == null) {
			if (other.tokenPass != null)
				return false;
		} else if (!tokenPass.equals(other.tokenPass))
			return false;
		if (tokenValidity == null) {
			if (other.tokenValidity != null)
				return false;
		} else if (!tokenValidity.equals(other.tokenValidity))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "SecureUser [id=" + id + ", username=" + username + ", role=" + role + ", tokenPass=" + tokenPass
				+ ", tokenValidity=" + tokenValidity + "]";
	}

}