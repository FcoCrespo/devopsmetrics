package edu.uclm.esi.devopsmetrics.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento issuesrepo en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "issuesrepo")
public class IssueRepo implements Comparable<IssueRepo> {
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
	 * repository.
	 * 
	 * @author FcoCrespo
	 */
	private String repository;
	/**
	 * owner.
	 * 
	 * @author FcoCrespo
	 */
	private String owner;
	/**
	 * author.
	 * 
	 * @author FcoCrespo
	 */
	private String author;
	

	/**
	 * Constructor de IssueRepository.
	 * 
	 * @author FcoCrespo
	 */
	public IssueRepo(final String issue, final String repository, final String owner, final String author) {
		super();
		this.issue = issue;
		this.repository = repository;
		this.owner = owner;
		this.author = author;
	}

	
	@Override
	public int compareTo(IssueRepo o) {
		return this.getId().compareTo(o.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		IssueRepo other = (IssueRepo) obj;
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


	public String getIssue() {
		return issue;
	}


	public void setIssue(String issue) {
		this.issue = issue;
	}


	public String getRepository() {
		return repository;
	}


	public void setRepository(String repository) {
		this.repository = repository;
	}


	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	@Override
	public String toString() {
		return "IssueRepository [id=" + id + ", issue=" + issue + ", repository=" + repository + ", owner=" + owner
				+ ", author=" + author + "]";
	}

	
	
}