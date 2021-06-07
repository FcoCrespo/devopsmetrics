package edu.uclm.esi.devopsmetrics.models;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento cohesion metrics en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "cohesionmetrics")
public class CohesionMetrics implements Comparable<CohesionMetrics>{
	
	/**
	 * ID.
	 * 
	 * @author FcoCrespo
	 */
	@Id
	private String id;
	/**
	 * dit.
	 * 
	 * @author FcoCrespo
	 */
	private double dit;
	/**
	 * wmc.
	 * 
	 * @author FcoCrespo
	 */
	private double wmc;
	/**
	 * nsc.
	 * 
	 * @author FcoCrespo
	 */
	private double nsc;
	/**
	 * lcom.
	 * 
	 * @author FcoCrespo
	 */
	private double lcom;
	public CohesionMetrics(double dit, double wmc, double nsc, double lcom) {
		super();
		this.dit = dit;
		this.wmc = wmc;
		this.nsc = nsc;
		this.lcom = lcom;
	}
	

	/**
	 * Constructor vacío de CohesionMetrics.
	 * 
	 * @author FcoCrespo
	 */
	public CohesionMetrics() {

	}
	
	@Override
	public int compareTo(CohesionMetrics o) {
		return this.getId().compareTo(o.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		CohesionMetrics other = (CohesionMetrics) obj;
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
	public double getDit() {
		return dit;
	}
	public void setDit(double dit) {
		this.dit = dit;
	}
	public double getWmc() {
		return wmc;
	}
	public void setWmc(double wmc) {
		this.wmc = wmc;
	}
	public double getNsc() {
		return nsc;
	}
	public void setNsc(double nsc) {
		this.nsc = nsc;
	}
	public double getLcom() {
		return lcom;
	}
	public void setLcom(double lcom) {
		this.lcom = lcom;
	}
	
	@Override
	public String toString() {
		return "CohesionMetrics [id=" + id + ", dit=" + dit + ", wmc=" + wmc + ", nsc=" + nsc + ", lcom=" + lcom + "]";
	}
	
	
	
}