package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.models.CommitInfo;

/**
 * Clase que implementa la interfaz CommitInfoRepository.
 * 
 * @author FcoCrespo
 */

@Repository
public class CommitInfoRepositoryImpl implements CommitInfoRepository {

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

	public CommitInfoRepositoryImpl(final MongoOperations mongoOperations) {
		Assert.notNull(mongoOperations, "notNull");
		this.mongoOperations = mongoOperations;
	}

	@Override
	public Optional<List<CommitInfo>> findAll() {
		List<CommitInfo> commitsInfo = this.mongoOperations.find(new Query(), CommitInfo.class);

		return Optional.ofNullable(commitsInfo);
	}

	@Override
	public void saveCommitInfo(CommitInfo commitInfo) {
		this.mongoOperations.save(commitInfo);
	}

	@Override
	public void updateCommitInfo(CommitInfo commitInfo) {
		this.mongoOperations.save(commitInfo);
	}

	@Override
	public void deleteCommitInfo(String commitId) {
		this.mongoOperations.findAllAndRemove(new Query(Criteria.where("commitId").is(commitId)), CommitInfo.class);
	}

	@Override
	public CommitInfo findByCommitId(String idCommit) {
		return this.mongoOperations.findOne(new Query(Criteria.where("idCommit").is(idCommit)), CommitInfo.class);
	}

}
