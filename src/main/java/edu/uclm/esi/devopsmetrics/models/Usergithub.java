package edu.uclm.esi.devopsmetrics.models;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento user github en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "usersgithub")
public class Usergithub implements Comparable<Usergithub> {

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
	 * location.
	 * 
	 * @author FcoCrespo
	 */
	private String location;

	/**
	 * Constructor de Commit.
	 * 
	 * @author FcoCrespo
	 */
	public Usergithub(@NotNull final String login, final String email, final String avatarURL, final String idGithub,
			final String location) {
		super();
		this.login = login;
		this.email = email;
		this.avatarURL = avatarURL;
		this.idGithub = idGithub;
		this.location = location;

	}

	@Override
	public int compareTo(Usergithub o) {
		return this.getLogin().compareTo(o.getLogin());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		Usergithub other = (Usergithub) obj;
		return this.getLogin() == other.getLogin();

	}

	@Override
	public int hashCode() {
		return this.hashCode();
	}

	/**
	 * Constructor vacío de Commit.
	 * 
	 * @author FcoCrespo
	 */
	public Usergithub() {

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Usergithub [id=" + id + ", login=" + login + ", email=" + email + ", avatarURL=" + avatarURL
				+ ", idGithub=" + idGithub + ", location=" + location + "]";
	}

}
