package edu.uclm.esi.devopsmetrics.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento commitcursor en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "commitscursor")
public class CommitCursor implements Comparable<CommitCursor> {
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
	public CommitCursor(final boolean hasNextPage, final String endCursor, final String startCursor,
			final String branch, final String repository) {
		super();
		this.hasNextPage = hasNextPage;
		this.endCursor = endCursor;
		this.startCursor = startCursor;
		this.branch = branch;
		this.repository = repository;
	}

	
	@Override
	public int compareTo(CommitCursor o) {
		return this.getId().compareTo(o.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		CommitCursor other = (CommitCursor) obj;
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
	public String toString() {
		return "CommitCursor [id=" + id + ", hasNextPage=" + hasNextPage + ", endCursor=" + endCursor + ", startCursor="
				+ startCursor + ", branch=" + branch + ", repository=" + repository + "]";
	}

}