package edu.uclm.esi.devopsmetrics.models;




import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento methods test  en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "methodstest")
public class MethodTest implements Comparable<MethodTest> {
	
	/**
	 * ID.
	 * 
	 * @author FcoCrespo
	 */
	@Id
	private String id;
	/**
	 * idTest.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String idTest;
	/**
	 * feature.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String feature;
	/**
	 * passed.
	 * 
	 * @author FcoCrespo
	 */
	private boolean passed;
	
	public MethodTest(@NotNull String idTest, @NotNull String feature, boolean passed) {
		super();
		this.idTest = idTest;
		this.feature = feature;
		this.passed = passed;
	}

	/**
	 * Constructor vacío de MethodTest.
	 * 
	 * @author FcoCrespo
	 */
	public MethodTest() {

	}

	@Override
	public int compareTo(MethodTest o) {
		return this.getId().compareTo(o.getId());
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

	public String getIdTest() {
		return idTest;
	}

	public void setIdTest(String idTest) {
		this.idTest = idTest;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public boolean isPassed() {
		return passed;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}

	@Override
	public String toString() {
		return "MethodTest [id=" + id + ", idTest=" + idTest + ", feature=" + feature + ", passed=" + passed + "]";
	}
	
}