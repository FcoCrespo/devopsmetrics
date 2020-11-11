package edu.uclm.esi.devopsmetrics.repositories;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.models.Commit;

/**
 * Clase que implementa la interfaz CommitRepository.
 * 
 * @author FcoCrespo
 */

@Repository
public class CommitRepositoryImpl implements CommitRepository {
	  /**
	   * Instancia de la interfaz MongoOperations.
	   * 
	   * @author FcoCrespo
	   */
	  private final MongoOperations mongoOperations;

	  /**
	   * Constructor de la clase.
	   * 
	   * @author FcoCrespo
	   */
	  @Autowired

	  public CommitRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;

	  }

	  /**
	   * Devuelve todos los commits.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<List<Commit>> findAll() {

	    List<Commit> commits = this.mongoOperations.find(new Query(), Commit.class);

	    return Optional.ofNullable(commits);

	  }

	  /**
	   * Devuelve un usuario en función de su oid.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<Commit> findOne(final String oid) {
	    Commit d = this.mongoOperations.findOne(new Query(Criteria.where("oid").is(oid)), Commit.class);
	    return Optional.ofNullable(d);
	  }

	  /**
	   * Guarda un commit en la base de datos.
	   * 
	   * @author e3corp
	   */
	  public void saveCommit(final Commit commit) {
	    this.mongoOperations.save(commit);
	  }

	  /**
	   * Actualiza un commit en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void updateCommit(final Commit commit) {

	    this.mongoOperations.save(commit);

	  }

	  @Override
	  public Commit findByOidyBranch(final String oid, final String branch) {
		return this.mongoOperations
	        .findOne(new Query(Criteria.where("oid").is(oid).and("branch").is(branch)), Commit.class);
	  }
	  
	  @Override
	  public List<Commit> findAllByBranch(final String reponame, final String branch) {
		return this.mongoOperations
	        .find(new Query(Criteria.where("branch").is(branch).and("repository").is(reponame)), Commit.class);
	  }

	@Override
	public List<Commit> findAllByBranchAndAuthorName(String reponame, String branch, String authorName) {
		return this.mongoOperations
		        .find(new Query(Criteria.where("branch").is(branch).
		        		and("repository").is(reponame).
		        		and("authorName").is(authorName)), Commit.class);
	}

	@Override
	public List<Commit> findAllByBranchBeginEndDate(String reponame, String branch, Instant beginDate, String bestBeginDate,
			Instant endDate, String bestEndDate) {
		
		if(bestBeginDate.equals(bestEndDate)) {
			return this.mongoOperations
			        .find(new Query(Criteria.where("branch").is(branch).
			        		and("repository").is(reponame).
			        		and(bestBeginDate).gte(beginDate).lte(endDate)), Commit.class);
		}
		
		return this.mongoOperations
		        .find(new Query(Criteria.where("branch").is(branch).and("repository").is(reponame).
		        		and(bestBeginDate).gte(beginDate).
		        		and(bestEndDate).lte(endDate)), Commit.class);
	}

	@Override
	public String[] findBestBeginEndData(String reponame, String branch, Instant beginDate, Instant endDate) {
		Commit commitPushedBegin = this.mongoOperations
		        .findOne(new Query(Criteria.where("branch").is(branch).
		        		and("repository").is(reponame).
		        		and("pushedDate").gte(beginDate)), Commit.class);
		
		Commit commitAuthoredBegin = this.mongoOperations
		        .findOne(new Query(Criteria.where("branch").is(branch).
		        		and("repository").is(reponame).
		        		and("authoredDate").gte(beginDate)), Commit.class);
		
		String beginDateData="";
		
		if(commitPushedBegin==null) {
			beginDateData="authoredDate";
		}
		else if(commitPushedBegin.getPushedDate()==null) {
			beginDateData="authoredDate";
		}
		else if(commitAuthoredBegin==null) {
			beginDateData="pushedDate";
		}
		else if(commitAuthoredBegin.getAuthoredDate()==null) {
			beginDateData="pushedDate";
		}
		else if(commitPushedBegin.getPushedDate().truncatedTo(ChronoUnit.DAYS).isAfter(commitAuthoredBegin.getAuthoredDate().truncatedTo(ChronoUnit.DAYS))){
			beginDateData="authoredDate";
		}
		else {
			beginDateData="pushedDate";
		}
		
		Commit commitPushedEnd = this.mongoOperations
		        .findOne(new Query(Criteria.where("branch").is(branch).
		        		and("repository").is(reponame).
		        		and("pushedDate").lte(endDate)), Commit.class);
		
		Commit commitAuthoredEnd = this.mongoOperations
		        .findOne(new Query(Criteria.where("branch").is(branch).
		        		and("repository").is(reponame).
		        		and("authoredDate").lte(endDate)), Commit.class);

		String endDateData="";
		
		if(commitPushedEnd==null) {
			endDateData="authoredDate";
		}
		else if(commitPushedEnd.getPushedDate()==null) {
			endDateData="authoredDate";
		}
		else if(commitAuthoredEnd==null) {
			endDateData="pushedDate";
		}
		else if(commitAuthoredEnd.getAuthoredDate()==null) {
			endDateData="pushedDate";
		}
		else if(commitAuthoredBegin!=null && commitPushedBegin!=null){
			
			if(commitPushedBegin.getPushedDate().truncatedTo(ChronoUnit.DAYS).isAfter(commitAuthoredBegin.getAuthoredDate().truncatedTo(ChronoUnit.DAYS))) {
					endDateData="pushedDate";
			}
			else {
				endDateData="authoredDate";
			}
			
		}
		
		
		String [] datasDate = new String[2];
		datasDate[0]=beginDateData;
		datasDate[1]=endDateData;
		
		return datasDate;
	}

	@Override
	public List<Commit> findAllByBranchAuthorBeginEndDate(String reponame, String branch, String authorName,
			Instant beginDate, String bestBeginDate, Instant endDate, String bestEndDate) {
		if(bestBeginDate.equals(bestEndDate)) {
			return this.mongoOperations
			        .find(new Query(Criteria.where("branch").is(branch).
			        		and("repository").is(reponame).
			        		and("authorName").is(authorName).
			        		and(bestBeginDate).gte(beginDate).lte(endDate)), Commit.class);
		}
		return this.mongoOperations
		        .find(new Query(Criteria.where("branch").is(branch).
		        		and("repository").is(reponame).
		        		and(bestBeginDate).gte(beginDate).
		        		and(bestEndDate).lte(endDate)), Commit.class);
	    
	}

	@Override
	public Commit findRepository(String reponame) {
		return this.mongoOperations
		        .findOne(new Query(Criteria.where("repository").is(reponame)), Commit.class);
	}

	/**
	   * Borra los commits de un repositorio en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	@Override
	public void deleteCommit(String repository) {
		this.mongoOperations.findAllAndRemove(new Query(Criteria.where("repository").is(repository)), Commit.class);
	}

	
}