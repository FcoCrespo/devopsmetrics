package edu.uclm.esi.devopsmetrics.models;


import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento commitcursor en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "commitscursor")
public class CommitCursor {
   /**
   * ID.
   * 
   * @author FcoCrespo
   */
  @Id
  private String id;
  /**
   * hasNextPage.
   * 
   * @author FcoCrespo
   */
  private boolean hasNextPage;
  /**
   * endCursor.
   * 
   * @author FcoCrespo
   */
  private String endCursor;
  /**
   * startCursor.
   * 
   * @author FcoCrespo
   */
  private String startCursor;
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
   * Constructor de CommitCursor.
   * 
   * @author FcoCrespo
   */
  public CommitCursor(final boolean hasNextPage, 
			  		  final String endCursor, 
			  		  final String startCursor,
			  		  final String branch,
			  		  final String repository) {
    super();
    this.id = UUID.randomUUID().toString();
    this.hasNextPage = hasNextPage;
    this.endCursor = endCursor;
    this.startCursor = startCursor;
    this.branch = branch;
    this.repository = repository;
  }

  /**
   * Constructor vacío de CommitCursor.
   * 
   * @author FcoCrespo
   */
  public CommitCursor() {

  }

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public boolean getHasNextPage() {
		return hasNextPage;
	}
	
	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
	
	public String getEndCursor() {
		return endCursor;
	}
	
	public void setEndCursor(String endCursor) {
		this.endCursor = endCursor;
	}
	
	public String getStartCursor() {
		return startCursor;
	}
	
	public void setStartCursor(String startCursor) {
		this.startCursor = startCursor;
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
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		result = prime * result + ((endCursor == null) ? 0 : endCursor.hashCode());
		result = prime * result + (hasNextPage ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((repository == null) ? 0 : repository.hashCode());
		result = prime * result + ((startCursor == null) ? 0 : startCursor.hashCode());
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
		CommitCursor other = (CommitCursor) obj;
		if (branch == null) {
			if (other.branch != null)
				return false;
		} else if (!branch.equals(other.branch))
			return false;
		if (endCursor == null) {
			if (other.endCursor != null)
				return false;
		} else if (!endCursor.equals(other.endCursor))
			return false;
		if (hasNextPage != other.hasNextPage)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (repository == null) {
			if (other.repository != null)
				return false;
		} else if (!repository.equals(other.repository))
			return false;
		if (startCursor == null) {
			if (other.startCursor != null)
				return false;
		} else if (!startCursor.equals(other.startCursor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CommitCursor [id=" + id + ", hasNextPage=" + hasNextPage + ", endCursor=" + endCursor + ", startCursor="
				+ startCursor + ", branch=" + branch + ", repository=" + repository + "]";
	}
	  
	  
	  
}