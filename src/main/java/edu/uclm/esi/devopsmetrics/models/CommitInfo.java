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
public class CommitInfo implements Comparable<CommitInfo> {
	/**
	 * ID.
	 * 
	 * @author FcoCrespo
	 */
	@Id
	private String id;
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
	 * changedFiles.
	 * 
	 * @author FcoCrespo
	 */
	private int changedFiles;

	/**
	 * Constructor de CommitInfo.
	 * 
	 * @author FcoCrespo
	 */
	public CommitInfo(@NotNull final String messageHeadline, final String message, final int changedFiles) {
		super();
		this.messageHeadline = messageHeadline;
		this.message = message;
		this.changedFiles = changedFiles;
	}

	/**
	 * Constructor vacío de CommitInfo.
	 * 
	 * @author FcoCrespo
	 */
	public CommitInfo() {

	}

	@Override
	public int compareTo(CommitInfo o) {
		return this.getId().compareTo(o.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		CommitInfo other = (CommitInfo) obj;
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

	public int getChangedFiles() {
		return changedFiles;
	}

	public void setChangedFiles(int changedFiles) {
		this.changedFiles = changedFiles;
	}

	@Override
	public String toString() {
		return "CommitInfo [id=" + id + ", messageHeadline=" + messageHeadline + ", message=" + message
				+ ", changedFiles=" + changedFiles + "]";
	}

	

}