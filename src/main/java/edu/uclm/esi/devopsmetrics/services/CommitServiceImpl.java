package edu.uclm.esi.devopsmetrics.services;


import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.exceptions.CommitNotFoundException;
import edu.uclm.esi.devopsmetrics.models.Branch;
import edu.uclm.esi.devopsmetrics.models.Commit;
import edu.uclm.esi.devopsmetrics.repositories.CommitRepository;


@Service("CommitService")
/**
 * @author FcoCrespo
 */
@Transactional

public class CommitServiceImpl implements CommitService {
  /**
   * @author FcoCrespo
   */
  private static final Log log = LogFactory.getLog(CommitServiceImpl.class);
  /**
   * @author FcoCrespo
   */
  private CommitRepository commitRepository;

  /**
   * @author FcoCrespo
   */
  @Autowired
  private final BranchService branchService;

  public CommitServiceImpl(final CommitRepository commitRepository, final BranchService branchService) {

    this.commitRepository = commitRepository;
    this.branchService = branchService;

  }

  /**
   * @author FcoCrespo
   */
  public Commit findByOid(final String oid) {

    final Optional<Commit> commit = commitRepository.findOne(oid);

    if (commit.isPresent()) {

      log.debug(String.format("Read username '{}'", commit));

      final Optional<Commit> userOpt = commit;

      return userOpt.get();

    } else {

      throw new CommitNotFoundException(oid);

    }

  }

  /**
   * @author FcoCrespo
   */
  public List<Commit> findAll() {

    final Optional<List<Commit>> commits = commitRepository.findAll();

    final List<Commit> commitsList = null;
    
    for (int i = 0; i < commits.get().size(); i++) {
        final Commit commit = commits.get().get(i);
        commitsList.add(commit);
   }

    return commitsList;

  }

  /**
   * @author FcoCrespo
   */
  public void saveCommit(final Commit commit) {

	  commitRepository.saveCommit(commit);

  }

  /**
   * @author FcoCrespo
   */
  public void updateCommit(final Commit commit) {

	  commitRepository.updateCommit(commit);

  }

  /**
   * @author FcoCrespo
   */
  public void deleteCommit(final String commitId) {

	  commitRepository.deleteCommit(commitId);

  }

  @Override
  public Commit getCommitByOidyBranch(final String oid, final String branch) {

    final Commit commit = commitRepository.findByOidyBranch(oid, branch);
    return commit;
    
  }

  @Override
  	public List<Commit> getAllByBranch(final String reponame, final String branchname) {
	 
		Branch branch = branchService.getBranchByRepositoryyName(reponame, branchname);
		
		if(branch==null) {
			return null;
		}
		else if(branch.getOrder()==0 || branch.getOrder()==1) {
			final List<Commit> commits = commitRepository.findAllByBranch(reponame, branchname);
			Collections.sort(commits);
			
		    return commits;
		}
		else {
			  
			  Branch branchBefore = branchService.getBeforeBranchByOrder(reponame, branch.getOrder());
			
			  List <Commit> listQuery = commitRepository.findAllByBranch(reponame, branchname);
				
			  List <Commit> listBefore = commitRepository.findAllByBranch(reponame, branchBefore.getName());
		      
			  List <Commit> commits = decantarCommits(listQuery, listBefore);
		      
		      return commits;
		}
	}

	@Override
	public List<Commit> getAllByBranchAndAuthorName(String reponame, String branchname, String authorName) {
		Branch branch = branchService.getBranchByRepositoryyName(reponame, branchname);
		
		if(branch==null) {
			return null;
		}
		else if(branch.getOrder()==0 || branch.getOrder()==1) {
			final List<Commit> commits = commitRepository.findAllByBranchAndAuthorName(reponame, branchname, authorName);
			Collections.sort(commits);
			
		    return commits;
		}
		else {
			  
			  Branch branchBefore = branchService.getBeforeBranchByOrder(reponame, branch.getOrder());
			
			  List <Commit> listQuery = commitRepository.findAllByBranchAndAuthorName(reponame, branchname, authorName);
				
			  List <Commit> listBefore = commitRepository.findAllByBranchAndAuthorName(reponame, branchBefore.getName(), authorName);
		      
			  List <Commit> commits = decantarCommits(listQuery, listBefore);
		      
		      return commits;
		}
	}
	
	@Override
	public List<Commit> getAllByBranchBeginEndDate(String reponame, String branchname, Instant beginDate, String bestBeginData, Instant endDate, String bestEndData) {
		Branch branch = branchService.getBranchByRepositoryyName(reponame, branchname);
		
		if(branch==null) {
			return null;
		}
		else if(branch.getOrder()==0 || branch.getOrder()==1) {
			final List<Commit> commits = commitRepository.findAllByBranchBeginEndDate(reponame, branchname, beginDate, bestBeginData, endDate, bestEndData);
			Collections.sort(commits);
			
		    return commits;
		}
		else {
			  
			  Branch branchBefore = branchService.getBeforeBranchByOrder(reponame, branch.getOrder());
			
			  List <Commit> listQuery = commitRepository.findAllByBranchBeginEndDate(reponame, branchname, beginDate, bestBeginData, endDate, bestEndData);
				
			  List <Commit> listBefore = commitRepository.findAllByBranchBeginEndDate(reponame, branchBefore.getName(), beginDate, bestBeginData, endDate, bestEndData);
		      
			  List <Commit> commits = decantarCommits(listQuery, listBefore);
		      
		      return commits;
		}
	}

	private List<Commit> decantarCommits(List<Commit> listQuery, List<Commit> listBefore) {
		  boolean seguir=true;
	      List <Commit> commits =  new ArrayList<Commit>();
	      
	      
	      for(int i = 0; i<listQuery.size(); i++) {
	    	  for(int j = 0; j<listBefore.size()&&seguir==true; j++) {
	    		  if (listQuery.get(i).getOid().equals(listBefore.get(j).getOid())){
	    			  seguir=false;
	    		  }
	    	  }
	    	  if(seguir==true) {
	    		  commits.add(listQuery.get(i));
	    	  }
	    	  seguir=true;
	      }
	      
	      Collections.sort(commits);
	      
	      return commits;
	}

	@Override
	public String[] getBestBeginEndData(String reponame, String branch, Instant beginDate, Instant endDate) {
		return commitRepository.findBestBeginEndData(reponame, branch, beginDate, endDate);
	}

	@Override
	public List<Commit> getAllByBranchAuthorBeginEndDate(String reponame, String branchname, String authorName,
			Instant beginDate, String bestBeginData, Instant endDate, String bestEndData) {
		Branch branch = branchService.getBranchByRepositoryyName(reponame, branchname);
		
		if(branch==null) {
			return null;
		}
		else if(branch.getOrder()==0 || branch.getOrder()==1) {
			final List<Commit> commits = commitRepository.findAllByBranchAuthorBeginEndDate(reponame, branchname, authorName, beginDate, bestBeginData, endDate, bestEndData);
			Collections.sort(commits);
			
		    return commits;
		}
		else {
			  
			  Branch branchBefore = branchService.getBeforeBranchByOrder(reponame, branch.getOrder());
			
			  List <Commit> listQuery = commitRepository.findAllByBranchAuthorBeginEndDate(reponame, branchname, authorName, beginDate, bestBeginData, endDate, bestEndData);
				
			  List <Commit> listBefore = commitRepository.findAllByBranchAuthorBeginEndDate(reponame, branchBefore.getName(), authorName, beginDate, bestBeginData, endDate, bestEndData);
		      
			  List <Commit> commits = decantarCommits(listQuery, listBefore);
		      
		      return commits;
		}
	}

	@Override
	public Commit getRepository(String repository) {
		final Commit commit = commitRepository.findRepository(repository);
	    return commit;
	}

	
	
    
  }

