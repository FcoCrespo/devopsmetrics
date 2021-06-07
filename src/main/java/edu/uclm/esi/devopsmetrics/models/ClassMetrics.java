package edu.uclm.esi.devopsmetrics.models;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento classes metrics en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "classesmetrics")
public class ClassMetrics implements Comparable<ClassMetrics> {
	
	/**
	 * ID.
	 * 
	 * @author FcoCrespo
	 */
	@Id
	private String id;
	/**
	 * noc.
	 * 
	 * @author FcoCrespo
	 */
	private double noc;
	/**
	 * noi.
	 * 
	 * @author FcoCrespo
	 */
	private double noi;
	/**
	 * tloc.
	 * 
	 * @author FcoCrespo
	 */
	private double tloc;
	/**
	 * nom.
	 * 
	 * @author FcoCrespo
	 */
	private double nom;
	
	public ClassMetrics(double noc, double noi, double tloc, double nom) {
		super();
		this.noc = noc;
		this.noi = noi;
		this.tloc = tloc;
		this.nom = nom;
	}
	

	/**
	 * Constructor vacío de ClassMetrics.
	 * 
	 * @author FcoCrespo
	 */
	public ClassMetrics() {

	}
	
	@Override
	public int compareTo(ClassMetrics o) {
		return this.getId().compareTo(o.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		ClassMetrics other = (ClassMetrics) obj;
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
	public double getNoc() {
		return noc;
	}
	public void setNoc(double noc) {
		this.noc = noc;
	}
	public double getNoi() {
		return noi;
	}
	public void setNoi(double noi) {
		this.noi = noi;
	}
	public double getTloc() {
		return tloc;
	}
	public void setTloc(double tloc) {
		this.tloc = tloc;
	}
	public double getNom() {
		return nom;
	}
	public void setNom(double nom) {
		this.nom = nom;
	}
	@Override
	public String toString() {
		return "ClassMetrics [id=" + id + ", noc=" + noc + ", noi=" + noi + ", tloc=" + tloc + ", nom=" + nom + "]";
	}
	
	
	
}