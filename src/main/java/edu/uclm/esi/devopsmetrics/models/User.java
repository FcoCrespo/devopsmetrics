package edu.uclm.esi.devopsmetrics.models;


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
@Document(collection = "users")
public class User {
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
   * Constructor de Usuario.
   * 
   * @author FcoCrespo
   */
  public User(@NotNull final String username, @NotNull final String password, final String role) {
    super();
    this.id = UUID.randomUUID().toString();
    this.username = Utilities.encriptar(username);
    this.password = Utilities.encriptar(password);
    this.role = Utilities.encriptar(role);
  }

  /**
   * Constructor vacío de Usuario.
   * 
   * @author FcoCrespo
   */
  public User() {

  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }
  
  public void setPassword(final String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(final String role) {
    this.role = role;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((password == null) ? 0 : password.hashCode());
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((role == null) ? 0 : role.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    User other = (User) obj;
 
    if (password == null) {
      if (other.password != null) {
        return false;
      }
    } else if (!password.equals(other.password)) {
      return false;
    }
    if (username == null) {
      if (other.username != null) {
        return false;
      }
    } else if (!username.equals(other.username)) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (role == null) {
      if (other.role != null) {
        return false;
      }
    } else if (!role.equals(other.role)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Usuario [id=" + id + ", username=" + username + ", password="
        + password + ", role=" + role + "]";
  }

}
