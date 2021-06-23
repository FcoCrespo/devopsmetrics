package edu.uclm.esi.devopsmetrics.models;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento commit en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "branches")
public class Branch implements Comparable<Branch> {
	/**
	 * ID.
	 * 
	 * @author FcoCrespo
	 */
	@Id
	private String id;
	/**
	 * idGithub.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String idGithub;
	/**
	 * repository.
	 * 
	 * @author FcoCrespo
	 */
	private String repository;
	/**
	 * name.
	 * 
	 * @author FcoCrespo
	 */
	private String name;
	/**
	 * order.
	 * 
	 * @author FcoCrespo
	 */
	private int order;

	/**
	 * owner.
	 * 
	 * @author FcoCrespo
	 */
	private String owner;
	/**
	 * Constructor de Branch.
	 * 
	 * @author FcoCrespo
	 */
	public Branch(@NotNull final String idGithub, final String repository, final String name, final int order, final String owner) {
		super();
		this.idGithub = idGithub;
		this.repository = repository;
		this.name = name;
		this.order = order;
		this.owner = owner;
	}

	/**
	 * Constructor vacío de Branch.
	 * 
	 * @author FcoCrespo
	 */
	public Branch() {

	}

	@Override
	public int compareTo(Branch o) {
		return Integer.compare(this.getOrder(), o.getOrder());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		Branch other = (Branch) obj;
		return this.getIdGithub() == other.getIdGithub();

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

	public String getIdGithub() {
		return idGithub;
	}

	public void setIdGithub(String idGithub) {
		this.idGithub = idGithub;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "Branch [idGithub=" + idGithub + ", repository=" + repository + ", name=" + name + ", owner=" + owner + ", order=" + order
				+ "]";
	}

}