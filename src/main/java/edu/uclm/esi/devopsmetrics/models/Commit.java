package edu.uclm.esi.devopsmetrics.models;


import java.time.Instant;
import java.util.UUID;

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
public class Commit implements Comparable< Commit >{
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
   * authoredDate.
   * 
   * @author FcoCrespo
   */
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant authoredDate;
  /**
   * messageBody.
   * 
   * @author FcoCrespo
   */
  private String authorName;

  /**
   * authorId.
   * 
   * @author FcoCrespo
   */
  private String authorId;
  /**
   * branch.
   * 
   * @author FcoCrespo
   */
  private String branch;
  /**
   * repository.
   * 
   * @author FcoCrespo
   */
  private String repository;
  
  
  /**
   * Constructor de Commit.
   * 
   * @author FcoCrespo
   */
  public Commit(@NotNull final String oid,
		  				 final String messageHeadline,
		  				 final String message,
		  				 final Instant pushedDate,
		  				 final int changedFiles,
		  				 final Instant authoredDate,
		  				 final String authorName,
		  				 final String authorId,
		  				 final String branch,
		  				 final String repository) {
    super();
    this.id = UUID.randomUUID().toString();
    this.oid = oid;
    this.messageHeadline = messageHeadline;
    this.message = message;
    this.pushedDate = pushedDate;
    this.changedFiles = changedFiles;
    this.authoredDate = authoredDate;
    this.authorName = authorName;
    this.authorId = authorId;
    this.branch = branch;
    this.repository = repository;
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
		if(this.getAuthoredDate()==null && o.getPushedDate()!=null && this.getPushedDate()!=null) {
			return this.getPushedDate().compareTo(o.getPushedDate());
		}
		else if(this.getPushedDate()==null && o.getAuthoredDate()!=null && this.getAuthoredDate()!=null){
			return this.getAuthoredDate().compareTo(o.getAuthoredDate());
		}
		else if(this.getPushedDate()==null && o.getAuthoredDate()==null && o.getPushedDate()!=null){
			return this.getAuthoredDate().compareTo(o.getPushedDate());
		}
		else{
			return this.getPushedDate().compareTo(o.getAuthoredDate());
		}	     
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
	
	public Instant getAuthoredDate() {
		return authoredDate;
	}
	
	
	public void setAuthoredDate(Instant authoredDate) {
		this.authoredDate = authoredDate;
	}
	
	
	public String getAuthorName() {
		return authorName;
	}
	
	
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
	
	public String getAuthorId() {
		return authorId;
	}
	
	
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	
	
	public String getBranch() {
		return branch;
	}
	
	
	public void setBranch(String branch) {
		this.branch = branch;
	}
	
	
	public String getRepository() {
		return repository;
	}
	
	
	public void setRepository(String repository) {
		this.repository = repository;
	}

	@Override
	public String toString() {
		return "Commit [id=" + id + ", oid=" + oid + ", messageHeadline=" + messageHeadline + ", message=" + message
				+ ", pushedDate=" + pushedDate + ", changedFiles=" + changedFiles + ", authoredDate=" + authoredDate
				+ ", authorName=" + authorName + ", authorId=" + authorId + ", branch=" + branch + ", repository="
				+ repository + "]";
	}


}