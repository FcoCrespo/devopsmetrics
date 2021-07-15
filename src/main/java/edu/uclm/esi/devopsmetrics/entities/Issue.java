package edu.uclm.esi.devopsmetrics.entities;
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
	 * number.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private int number;
	
	/**
	 * Constructor de Issue
	 * .
	 * 
	 * @author FcoCrespo
	 */
	public Issue(@NotNull final String state, @NotNull final String title, final Instant createdAt, final Instant closedAt
			, int number) {
		super();
		this.state = state;
		this.title = title;
		this.createdAt = createdAt;
		this.closedAt = closedAt;
		this.number = number;
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
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
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
		return "Issue [id=" + id + ", state=" + state + ", title=" + title + ", number: " + number + ", createdAt="
				+ createdAt + ", closedAt=" + closedAt + "]";
	}
	
}
