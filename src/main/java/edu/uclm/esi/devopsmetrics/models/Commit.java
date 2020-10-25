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
   * idGithub.
   * 
   * @author FcoCrespo
   */
  @NotNull
  private String idGithub;
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
   * messageBody.
   * 
   * @author FcoCrespo
   */
  private String messageBody;
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
   * authoredByCommiter.
   * 
   * @author FcoCrespo
   */
  private String authoredByCommitter;
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
   * authorEmail.
   * 
   * @author FcoCrespo
   */
  private String authorEmail;
  /**
   * authorDate.
   * 
   * @author FcoCrespo
   */
  private String authorDate;
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
  public Commit(@NotNull final String idGithub, 
		  		@NotNull final String oid,
		  				 final String messageHeadline,
		  				 final String message,
		  				 final String messageBody,
		  				 final Instant pushedDate,
		  				 final int changedFiles,
		  				 final String authoredByCommitter,
		  				 final Instant authoredDate,
		  				 final String authorName,
		  				 final String authorEmail,
		  				 final String authorDate,
		  				 final String authorId,
		  				 final String branch,
		  				 final String repository) {
    super();
    this.id = UUID.randomUUID().toString();
    this.idGithub = idGithub;
    this.oid = oid;
    this.messageHeadline = messageHeadline;
    this.message = message;
    this.pushedDate = pushedDate;
    this.changedFiles = changedFiles;
    this.authoredDate = authoredDate;
    this.authorName = authorName;
    this.authorEmail = authorEmail;
    this.authorDate = authorDate;
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
		if(this.getAuthoredDate()==null && !(o.getPushedDate()==null) && !(this.getPushedDate()==null)) {
			return this.getPushedDate().compareTo(o.getPushedDate());
		}
		else if(this.getPushedDate()==null && !(o.getAuthoredDate()==null) && !(this.getAuthoredDate()==null)){
			return this.getAuthoredDate().compareTo(o.getAuthoredDate());
		}
		else if(this.getPushedDate()==null && o.getAuthoredDate()==null && !(o.getPushedDate()==null)){
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
	
	
	public String getIdGithub() {
		return idGithub;
	}
	
	
	public void setIdGithub(String idGithub) {
		this.idGithub = idGithub;
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
	
	
	public String getMessageBody() {
		return messageBody;
	}
	
	
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
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
	
	
	public String getAuthoredByCommitter() {
		return authoredByCommitter;
	}
	
	
	public void setAuthoredByCommitter(String authoredByCommitter) {
		this.authoredByCommitter = authoredByCommitter;
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
	
	
	public String getAuthorEmail() {
		return authorEmail;
	}
	
	
	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}
	
	
	public String getAuthorDate() {
		return authorDate;
	}
	
	
	public void setAuthorDate(String authorDate) {
		this.authorDate = authorDate;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authorDate == null) ? 0 : authorDate.hashCode());
		result = prime * result + ((authorEmail == null) ? 0 : authorEmail.hashCode());
		result = prime * result + ((authorId == null) ? 0 : authorId.hashCode());
		result = prime * result + ((authorName == null) ? 0 : authorName.hashCode());
		result = prime * result + ((authoredByCommitter == null) ? 0 : authoredByCommitter.hashCode());
		result = prime * result + ((authoredDate == null) ? 0 : authoredDate.hashCode());
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		result = prime * result + changedFiles;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idGithub == null) ? 0 : idGithub.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((messageBody == null) ? 0 : messageBody.hashCode());
		result = prime * result + ((messageHeadline == null) ? 0 : messageHeadline.hashCode());
		result = prime * result + ((oid == null) ? 0 : oid.hashCode());
		result = prime * result + ((pushedDate == null) ? 0 : pushedDate.hashCode());
		result = prime * result + ((repository == null) ? 0 : repository.hashCode());
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
		Commit other = (Commit) obj;
		if (authorDate == null) {
			if (other.authorDate != null)
				return false;
		} else if (!authorDate.equals(other.authorDate))
			return false;
		if (authorEmail == null) {
			if (other.authorEmail != null)
				return false;
		} else if (!authorEmail.equals(other.authorEmail))
			return false;
		if (authorId == null) {
			if (other.authorId != null)
				return false;
		} else if (!authorId.equals(other.authorId))
			return false;
		if (authorName == null) {
			if (other.authorName != null)
				return false;
		} else if (!authorName.equals(other.authorName))
			return false;
		if (authoredByCommitter == null) {
			if (other.authoredByCommitter != null)
				return false;
		} else if (!authoredByCommitter.equals(other.authoredByCommitter))
			return false;
		if (authoredDate == null) {
			if (other.authoredDate != null)
				return false;
		} else if (!authoredDate.equals(other.authoredDate))
			return false;
		if (branch == null) {
			if (other.branch != null)
				return false;
		} else if (!branch.equals(other.branch))
			return false;
		if (changedFiles != other.changedFiles)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idGithub == null) {
			if (other.idGithub != null)
				return false;
		} else if (!idGithub.equals(other.idGithub))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (messageBody == null) {
			if (other.messageBody != null)
				return false;
		} else if (!messageBody.equals(other.messageBody))
			return false;
		if (messageHeadline == null) {
			if (other.messageHeadline != null)
				return false;
		} else if (!messageHeadline.equals(other.messageHeadline))
			return false;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		if (pushedDate == null) {
			if (other.pushedDate != null)
				return false;
		} else if (!pushedDate.equals(other.pushedDate))
			return false;
		if (repository == null) {
			if (other.repository != null)
				return false;
		} else if (!repository.equals(other.repository))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return "Commit [id=" + id + ", idGithub=" + idGithub + ", oid=" + oid + ", messageHeadline=" + messageHeadline
				+ ", message=" + message + ", messageBody=" + messageBody + ", pushedDate=" + pushedDate + ", changedFiles="
				+ changedFiles + ", authoredByCommitter=" + authoredByCommitter + ", authoredDate=" + authoredDate
				+ ", authorName=" + authorName + ", authorEmail=" + authorEmail + ", authorDate=" + authorDate
				+ ", authorId=" + authorId + ", branch=" + branch + ", repository=" + repository + "]";
	}


}