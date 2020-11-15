package edu.uclm.esi.devopsmetrics.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import edu.uclm.esi.devopsmetrics.models.CommitInfo;
import edu.uclm.esi.devopsmetrics.repositories.CommitInfoRepository;

@Service("CommitInfoService")
/**
 * @author FcoCrespo
 */
@Transactional

public class CommitInfoServiceImpl implements CommitInfoService{

	
		/**
	   * @author FcoCrespo
	   */
	  private CommitInfoRepository commitInfoRepository;

	  /**
	   * @author FcoCrespo
	   */

	  public CommitInfoServiceImpl(final CommitInfoRepository commitInfoRepository) {

	    this.commitInfoRepository = commitInfoRepository;

	  }
	  
	  
	@Override
	public List<CommitInfo> findAll() {
		final Optional<List<CommitInfo>> commitsInfo = commitInfoRepository.findAll();

	    final List<CommitInfo> commitsInfoList = new ArrayList<CommitInfo>();
	    
	    if(commitsInfo.isPresent()) {
	    	
	    	CommitInfo commitInfo;
	    	for (int i = 0; i < commitsInfo.get().size(); i++) {
	    		commitInfo = commitsInfo.get().get(i);
	    		commitsInfoList.add(commitInfo);
	        }

	        return commitsInfoList;
	    }
	    else {
	    	return Collections.emptyList();
	    }
	}

	@Override
	public CommitInfo findByCommitId(String commitId) {
		final CommitInfo commitInfo = commitInfoRepository.findByCommitId(commitId);
		if(commitInfo!=null) {
			return commitInfo;
	    } else {
	      return null;
	    }
	}

	@Override
	public void saveCommitInfo(CommitInfo commitInfo) {
		this.commitInfoRepository.saveCommitInfo(commitInfo);
	}

	@Override
	public void updateCommitInfo(CommitInfo commitInfo) {
		this.commitInfoRepository.updateCommitInfo(commitInfo);
	}

	@Override
	public void deleteCommitInfo(String commitId) {
		this.commitInfoRepository.deleteCommitInfo(commitId);
	}

}
