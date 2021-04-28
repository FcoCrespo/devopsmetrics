package edu.uclm.esi.devopsmetrics.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento methods metrics en la base de datos.
 * 
 * @author FcoCrespo
 */
@Document(collection = "methodsmetrics")
public class MethodMetrics implements Comparable<MethodMetrics> {
	/**
	 * ID.
	 * 
	 * @author FcoCrespo
	 */
	@Id
	private String id;
	/**
	 * vg.
	 * 
	 * @author FcoCrespo
	 */
	private double vg;
	/**
	 * mloc.
	 * 
	 * @author FcoCrespo
	 */
	private double mloc;
	/**
	 * par.
	 * 
	 * @author FcoCrespo
	 */
	private double par;
	/**
	 * nbd.
	 * 
	 * @author FcoCrespo
	 */
	private double nbd;
	
	public MethodMetrics(double vg, double mloc, double par, double nbd) {
		super();
		this.vg = vg;
		this.mloc = mloc;
		this.par = par;
		this.nbd = nbd;
	}
	
	/**
	 * Constructor vacío de MethodMetrics.
	 * 
	 * @author FcoCrespo
	 */
	public MethodMetrics() {

	}
	
	@Override
	public int compareTo(MethodMetrics o) {
		return this.getId().compareTo(o.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		MethodMetrics other = (MethodMetrics) obj;
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
	public double getVg() {
		return vg;
	}
	public void setVg(double vg) {
		this.vg = vg;
	}
	public double getMloc() {
		return mloc;
	}
	public void setMloc(double mloc) {
		this.mloc = mloc;
	}
	public double getPar() {
		return par;
	}
	public void setPar(double par) {
		this.par = par;
	}
	public double getNbd() {
		return nbd;
	}
	public void setNbd(double nbd) {
		this.nbd = nbd;
	}
	
	@Override
	public String toString() {
		return "MethodMetrics [id=" + id + ", vg=" + vg + ", mloc=" + mloc + ", par=" + par + ", nbd=" + nbd + "]";
	}

}