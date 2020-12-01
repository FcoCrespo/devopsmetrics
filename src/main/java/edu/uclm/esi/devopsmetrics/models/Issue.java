package edu.uclm.esi.devopsmetrics.models;
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
public class Issue implements Comparable<Issue> {
	
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
	 * createAt.
	 * 
	 * @author FcoCrespo
	 */
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Instant createAt;
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
	public Issue(@NotNull final String state, @NotNull final String title, final Instant createAt, final Instant closedAt) {
		super();
		this.state = state;
		this.title = title;
		this.createAt = createAt;
		this.closedAt = closedAt;
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
		return this.getCreateAt().compareTo(o.getCreateAt());
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

	public Instant getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Instant createAt) {
		this.createAt = createAt;
	}

	public Instant getClosedAt() {
		return closedAt;
	}

	public void setClosedAt(Instant closedAt) {
		this.closedAt = closedAt;
	}

	@Override
	public String toString() {
		return "Issue [id=" + id + ", state=" + state + ", title=" + title + ", createAt=" + createAt + ", closedAt="
				+ closedAt + "]";
	}

	

}
