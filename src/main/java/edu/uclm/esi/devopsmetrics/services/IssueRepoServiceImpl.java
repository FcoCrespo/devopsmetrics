package edu.uclm.esi.devopsmetrics.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.models.IssueRepo;
import edu.uclm.esi.devopsmetrics.repositories.IssueRepoRepository;


@Service("IssueRepoService")
/**
 * @author FcoCrespo
 */
@Transactional
public class IssueRepoServiceImpl implements IssueRepoService{
	
	/**
	 * @author FcoCrespo
	 */
	private IssueRepoRepository issueRepoRepository;

	public IssueRepoServiceImpl(final IssueRepoRepository issueRepoRepository) {

		this.issueRepoRepository = issueRepoRepository;

	}

	@Override
	public List<IssueRepo> findAll() {
		final Optional<List<IssueRepo>> issues = issueRepoRepository.findAll();

		final List<IssueRepo> issuesList = new ArrayList<IssueRepo>();

		if (issues.isPresent()) {
			IssueRepo issue;
			for (int i = 0; i < issues.get().size(); i++) {
				issue = issues.get().get(i);
				issuesList.add(issue);
			}

			return issuesList;
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public void saveIssueRepo(IssueRepo issueRepo) {
		this.issueRepoRepository.saveIssueRepo(issueRepo);
		
	}

	@Override
	public void updateIssueRepo(IssueRepo issueRepo) {
		this.issueRepoRepository.updateIssueRepo(issueRepo);
		
	}

	@Override
	public void deleteIssueRepo(String issueRepoId) {
		this.deleteIssueRepo(issueRepoId);
		
	}

	@Override
	public IssueRepo findOne(String issueid) {
		return this.issueRepoRepository.findOne(issueid);
	}

	@Override
	public IssueRepo getByRepoyOwner(String repository, String owner) {
		return this.issueRepoRepository.findByRepoyOwner(repository, owner);
	}
	
	

}
