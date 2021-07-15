package edu.uclm.esi.devopsmetrics.entities;

public class Repository {
	
	String reponame;
	String owner;
	
	public Repository(String reponame, String owner) {
		super();
		this.reponame = reponame;
		this.owner = owner;
	}
	
	public String getRepository() {
		return reponame;
	}
	public void setRepository(String repository) {
		this.reponame = repository;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	

}
