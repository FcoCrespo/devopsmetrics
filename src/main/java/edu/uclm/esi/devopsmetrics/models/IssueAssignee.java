package edu.uclm.esi.devopsmetrics.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento issuesassignees en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "issuesassignees")
public class IssueAssignee implements Comparable<IssueAssignee> {
	/**
	 * ID.
	 * 
	 * @author FcoCrespo
	 */
	@Id
	private String id;
	/**
	 * issue.
	 * 
	 * @author FcoCrespo
	 */
	private String issue;
	/**
	 * usergithub.
	 * 
	 * @author FcoCrespo
	 */
	private String usergithub;
	

	/**
	 * Constructor de IssueAssignee.
	 * 
	 * @author FcoCrespo
	 */
	public IssueAssignee(final String issue, final String usergithub) {
		super();
		this.issue = issue;
		this.usergithub = usergithub;
	}

	
	@Override
	public int compareTo(IssueAssignee o) {
		return this.getId().compareTo(o.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		IssueAssignee other = (IssueAssignee) obj;
		return this.getId() == other.getId();

	}

	@Override
	public int hashCode() {
		return this.hashCode();
	}

	public String getIssue() {
		return issue;
	}


	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getUsergithub() {
		return usergithub;
	}


	public void setUsergithub(String usergithub) {
		this.usergithub = usergithub;
	}
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	


	@Override
	public String toString() {
		return "IssueAssignee [id=" + id + ", issue=" + issue + ", usergithub=" + usergithub + "]";
	}
	
	
}


