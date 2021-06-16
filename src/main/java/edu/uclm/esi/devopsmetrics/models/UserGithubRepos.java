package edu.uclm.esi.devopsmetrics.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento user github en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "usersgithubrepos")
public class UserGithubRepos implements Comparable<UserGithubRepos> {

	/**
	 * ID.
	 * 
	 * @author FcoCrespo
	 */
	@Id
	private String id;
	/**
	 * idusergithub.
	 * 
	 * @author FcoCrespo
	 */
	private String idusergithub;
	/**
	 * repository.
	 * 
	 * @author FcoCrespo
	 */
	private String repository;
	/**
	 * owner.
	 * 
	 * @author FcoCrespo
	 */
	private String owner;
	public UserGithubRepos(String idusergithub, String repository, String owner) {
		super();
		this.idusergithub = idusergithub;
		this.repository = repository;
		this.owner = owner;
	}
	
	@Override
	public int compareTo(UserGithubRepos o) {
		return this.getIdusergithub().compareTo(o.getIdusergithub());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		UserGithubRepos other = (UserGithubRepos) obj;
		return this.getIdusergithub() == other.getIdusergithub();

	}

	@Override
	public int hashCode() {
		return this.hashCode();
	}

	
	@Override
	public String toString() {
		return "UserGithubRepos [id=" + id + ", idusergithub=" + idusergithub + ", repository=" + repository
				+ ", owner=" + owner + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdusergithub() {
		return idusergithub;
	}
	public void setIdusergithub(String idusergithub) {
		this.idusergithub = idusergithub;
	}
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	


}
