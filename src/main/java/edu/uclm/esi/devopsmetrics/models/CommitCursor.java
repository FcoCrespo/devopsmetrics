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
	 * branchIdGithub.
	 * 
	 * @author FcoCrespo
	 */
	private String branchIdGithub;
	

	/**
	 * Constructor de CommitCursor.
	 * 
	 * @author FcoCrespo
	 */
	public CommitCursor(final boolean hasNextPage, final String endCursor, final String startCursor,
			final String branchIdGithub) {
		super();
		this.hasNextPage = hasNextPage;
		this.endCursor = endCursor;
		this.startCursor = startCursor;
		this.branchIdGithub = branchIdGithub;
		
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

	public String getBranchIdGithub() {
		return branchIdGithub;
	}

	public void setBranchIdGithub(String branchIdGithub) {
		this.branchIdGithub = branchIdGithub;
	}

	

	@Override
	public String toString() {
		return "CommitCursor [id=" + id + ", hasNextPage=" + hasNextPage + ", endCursor=" + endCursor + ", startCursor="
				+ startCursor + ", branchIdGithub=" + branchIdGithub + "]";
	}

}