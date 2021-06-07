package edu.uclm.esi.devopsmetrics.models;


import java.time.Instant;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Documento commit en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "commits")
public class Commit implements Comparable<Commit>{
	
	/**
	 * ID.
	 * 
	 * @author FcoCrespo
	 */
	@Id
	private String id;
	/**
	 * oid.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String oid;
	/**
	 * pushedDate.
	 * 
	 * @author FcoCrespo
	 */
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Instant pushedDate;
	/**
	 * usergithub.
	 * 
	 * @author FcoCrespo
	 */
	private String usergithub;
	/**
	 * branchId.
	 * 
	 * @author FcoCrespo
	 */
	private String branchId;
	
	/**
	 * Constructor de Commit.
	 * 
	 * @author FcoCrespo
	 */
	public Commit(@NotNull final String oid, final Instant pushedDate, final String usergithub, final String branchId) {
		super();
		this.oid = oid;
		this.pushedDate = pushedDate;
		this.usergithub = usergithub;
		this.branchId = branchId;
	}

	/**
	 * Constructor vacío de Commit.
	 * 
	 * @author FcoCrespo
	 */
	public Commit() {

	}

	@Override
	public int compareTo(Commit o) {
		return this.getPushedDate().compareTo(o.getPushedDate());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		Commit other = (Commit) obj;
		return this.getId() == other.getId();

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

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public Instant getPushedDate() {
		return pushedDate;
	}

	public void setPushedDate(Instant pushedDate) {
		this.pushedDate = pushedDate;
	}

	public String getUsergithub() {
		return usergithub;
	}

	public void setUsergithub(String usergithub) {
		this.usergithub = usergithub;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	@Override
	public String toString() {
		return "Commit [id=" + id + ", oid=" + oid + ", pushedDate=" + pushedDate + ", usergithub=" + usergithub
				+ ", branchId=" + branchId + "]";
	}

	

}