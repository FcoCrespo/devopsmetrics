package edu.uclm.esi.devopsmetrics.entities;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento user github en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "usersgithub")
public class UserGithub implements Comparable<UserGithub> {

	/**
	 * ID.
	 * 
	 * @author FcoCrespo
	 */
	@Id
	private String id;
	/**
	 * login.
	 * 
	 * @author FcoCrespo
	 */
	private String login;
	/**
	 * email.
	 * 
	 * @author FcoCrespo
	 */
	private String email;
	/**
	 * avatarURL.
	 * 
	 * @author FcoCrespo
	 */
	private String avatarURL;
	/**
	 * idGithub.
	 * 
	 * @author FcoCrespo
	 */
	private String idGithub;
	/**
	 * name.
	 * 
	 * @author FcoCrespo
	 */
	private String name;
	
	/**
	 * Constructor de UserGithub.
	 * 
	 * @author FcoCrespo
	 */
	public UserGithub(final String login, final String email, final String avatarURL, final String idGithub,
			final String name) {
		super();
		this.login = login;
		this.email = email;
		this.avatarURL = avatarURL;
		this.idGithub = idGithub;
		this.name = name;
	}

	@Override
	public int compareTo(UserGithub o) {
		return this.getLogin().compareTo(o.getLogin());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		UserGithub other = (UserGithub) obj;
		return this.getLogin() == other.getLogin();

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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatarURL() {
		return avatarURL;
	}

	public void setAvatarURL(String avatarURL) {
		this.avatarURL = avatarURL;
	}

	public String getIdGithub() {
		return idGithub;
	}

	public void setIdGithub(String idGithub) {
		this.idGithub = idGithub;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	@Override
	public String toString() {
		return "UserGithub [id=" + id + ", login=" + login + ", email=" + email + ", avatarURL=" + avatarURL
				+ ", idGithub=" + idGithub + ", name=" + name +"]";
	}



}
