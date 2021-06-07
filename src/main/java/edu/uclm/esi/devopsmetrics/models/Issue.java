package edu.uclm.esi.devopsmetrics.models;
import java.io.Serializable;
import java.time.Instant;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Documento issue en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "issues")
public class Issue implements Comparable<Issue>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ID.
	 * 
	 * @author FcoCrespo
	 */
	@Id
	private String id;
	/**
	 * state.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String state;
	/**
	 * title.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String title;
	/**
	 * body.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String body;
	/**
	 * createdAt.
	 * 
	 * @author FcoCrespo
	 */
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Instant createdAt;
	/**
	 * closedAt.
	 * 
	 * @author FcoCrespo
	 */
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Instant closedAt;
	
	/**
	 * Constructor de Issue
	 * .
	 * 
	 * @author FcoCrespo
	 */
	public Issue(@NotNull final String state, @NotNull final String title, String body, final Instant createdAt, final Instant closedAt) {
		super();
		this.state = state;
		this.title = title;
		this.createdAt = createdAt;
		this.closedAt = closedAt;
		this.body = body;
	}

	/**
	 * Constructor vacío de Issue.
	 * 
	 * @author FcoCrespo
	 */
	public Issue() {

	}

	@Override
	public int compareTo(Issue o) {
		return this.getCreatedAt().compareTo(o.getCreatedAt());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		Issue other = (Issue) obj;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getClosedAt() {
		return closedAt;
	}

	public void setClosedAt(Instant closedAt) {
		this.closedAt = closedAt;
	}

	@Override
	public String toString() {
		return "Issue [id=" + id + ", state=" + state + ", title=" + title + ", body=" + body + ", createdAt="
				+ createdAt + ", closedAt=" + closedAt + "]";
	}
	
}
