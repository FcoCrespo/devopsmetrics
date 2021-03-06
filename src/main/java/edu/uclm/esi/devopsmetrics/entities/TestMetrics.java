package edu.uclm.esi.devopsmetrics.entities;

import java.time.Instant;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Documento test metrics en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "testsmetrics")
public class TestMetrics implements Comparable<TestMetrics> {
	/**
	 * ID.
	 * 
	 * @author FcoCrespo
	 */
	@Id
	private String id;
	/**
	 * repository.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String repository;
	/**
	 * owner.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String owner;
	/**
	 * pushedDate.
	 * 
	 * @author FcoCrespo
	 */
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Instant dateTest;
	
	public TestMetrics(@NotNull String repository, String owner, Instant dateTest) {
		super();
		this.repository = repository;
		this.owner = owner;
		this.dateTest = dateTest;
	}

	/**
	 * Constructor vacío de TestMetrics.
	 * 
	 * @author FcoCrespo
	 */
	public TestMetrics() {

	}

	@Override
	public int compareTo(TestMetrics o) {
		return this.getDateTest().compareTo(o.getDateTest());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		TestMetrics other = (TestMetrics) obj;
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

	public Instant getDateTest() {
		return dateTest;
	}

	public void setDateTest(Instant newdateTest) {
		this.dateTest = newdateTest;
	}

	@Override
	public String toString() {
		return "TestMetrics [id=" + id + ", repository=" + repository + ", owner=" + owner + ", dateTest="
				+ dateTest + "]";
	}
	
	
}