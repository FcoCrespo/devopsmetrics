package edu.uclm.esi.devopsmetrics.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento issuescursor en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "issuescursor")
public class IssueCursor implements Comparable<IssueCursor> {
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
	 * repository.
	 * 
	 * @author FcoCrespo
	 */
	private String repository;

	/**
	 * Constructor de IssueCursor.
	 * 
	 * @author FcoCrespo
	 */
	public IssueCursor(final boolean hasNextPage, final String endCursor, final String startCursor,
			final String repository) {
		super();
		this.hasNextPage = hasNextPage;
		this.endCursor = endCursor;
		this.startCursor = startCursor;
		this.repository = repository;
	}

	
	@Override
	public int compareTo(IssueCursor o) {
		return this.getId().compareTo(o.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		IssueCursor other = (IssueCursor) obj;
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


	public boolean isHasNextPage() {
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


	public String getRepository() {
		return repository;
	}


	public void setRepository(String repository) {
		this.repository = repository;
	}


	@Override
	public String toString() {
		return "IssueCursor [id=" + id + ", hasNextPage=" + hasNextPage + ", endCursor=" + endCursor + ", startCursor="
				+ startCursor + ", repository=" + repository + "]";
	}
	
	
}