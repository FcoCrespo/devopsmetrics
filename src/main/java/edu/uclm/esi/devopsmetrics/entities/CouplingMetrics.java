package edu.uclm.esi.devopsmetrics.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento coupling metrics en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "couplingmetrics")
public class CouplingMetrics implements Comparable<CouplingMetrics> {
	/**
	 * ID.
	 * 
	 * @author FcoCrespo
	 */
	@Id
	private String id;
	/**
	 * ca.
	 * 
	 * @author FcoCrespo
	 */
	private double ca;
	/**
	 * ce.
	 * 
	 * @author FcoCrespo
	 */
	private double ce;
	/**
	 * rmi.
	 * 
	 * @author FcoCrespo
	 */
	private double rmi;
	/**
	 * rma.
	 * 
	 * @author FcoCrespo
	 */
	private double rma;
	
	public CouplingMetrics(double ca, double ce, double rmi, double rma) {
		super();
		this.ca = ca;
		this.ce = ce;
		this.rmi = rmi;
		this.rma = rma;
	}
	
	/**
	 * Constructor vacío de CouplingMetrics.
	 * 
	 * @author FcoCrespo
	 */
	public CouplingMetrics() {

	}
	
	@Override
	public int compareTo(CouplingMetrics o) {
		return this.getId().compareTo(o.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		CouplingMetrics other = (CouplingMetrics) obj;
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

	public double getCa() {
		return ca;
	}

	public void setCa(double ca) {
		this.ca = ca;
	}

	public double getCe() {
		return ce;
	}

	public void setCe(double ce) {
		this.ce = ce;
	}

	public double getRmi() {
		return rmi;
	}

	public void setRmi(double rmi) {
		this.rmi = rmi;
	}

	public double getRma() {
		return rma;
	}

	public void setRma(double rma) {
		this.rma = rma;
	}

	@Override
	public String toString() {
		return "CouplingMetrics [id=" + id + ", ca=" + ca + ", ce=" + ce + ", rmi=" + rmi + ", rma=" + rma + "]";
	}
	
	
	
}