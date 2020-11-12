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
public class Commit implements Comparable<Commit> {
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
	 * messageHeadline.
	 * 
	 * @author FcoCrespo
	 */
	private String messageHeadline;
	/**
	 * message.
	 * 
	 * @author FcoCrespo
	 */
	private String message;
	/**
	 * pushedDate.
	 * 
	 * @author FcoCrespo
	 */
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Instant pushedDate;
	/**
	 * changedFiles.
	 * 
	 * @author FcoCrespo
	 */
	private int changedFiles;
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
	public Commit(@NotNull final String oid, final String messageHeadline, final String message,
			final Instant pushedDate, final int changedFiles, final String usergithub, final String branchId) {
		super();
		this.oid = oid;
		this.messageHeadline = messageHeadline;
		this.message = message;
		this.pushedDate = pushedDate;
		this.changedFiles = changedFiles;
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

	public String getMessageHeadline() {
		return messageHeadline;
	}

	public void setMessageHeadline(String messageHeadline) {
		this.messageHeadline = messageHeadline;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Instant getPushedDate() {
		return pushedDate;
	}

	public void setPushedDate(Instant pushedDate) {
		this.pushedDate = pushedDate;
	}

	public int getChangedFiles() {
		return changedFiles;
	}

	public void setChangedFiles(int changedFiles) {
		this.changedFiles = changedFiles;
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
		return "Commit [id=" + id + ", oid=" + oid + ", messageHeadline=" + messageHeadline + ", message=" + message
				+ ", pushedDate=" + pushedDate + ", changedFiles=" + changedFiles + ", usergithub=" + usergithub
				+ ", branchId=" + branchId + "]";
	}

}