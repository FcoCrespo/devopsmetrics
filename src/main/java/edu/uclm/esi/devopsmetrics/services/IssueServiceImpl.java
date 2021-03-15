package edu.uclm.esi.devopsmetrics.services;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.models.Issue;
import edu.uclm.esi.devopsmetrics.repositories.IssueRepository;


@Service("IssueService")
/**
 * @author FcoCrespo
 */
@Transactional
public class IssueServiceImpl implements IssueService{
	
	/**
	 * @author FcoCrespo
	 */
	private IssueRepository issueRepository;

	public IssueServiceImpl(final IssueRepository issueRepository) {

		this.issueRepository = issueRepository;

	}

	@Override
	public List<Issue> findAll() {
		final Optional<List<Issue>> issues = issueRepository.findAll();

		return obtenerIssues(issues);
	}

	@Override
	public void saveIssue(Issue issue) {
		this.issueRepository.saveIssue(issue);
		
	}

	@Override
	public void updateIssue(Issue issue) {
		this.issueRepository.updateIssue(issue);
	}

	@Override
	public void deleteIssue(String issueId) {
		this.issueRepository.deleteIssue(issueId);
		
	}

	@Override
	public Issue findOne(String id) {
		return this.issueRepository.findOne(id);
	}

	@Override
	public Issue getByIdyState(String id, String state) {
		return this.issueRepository.findByIdyState(id, state);
	}

	@Override
	public List<Issue> getAllByState(String state) {
		final Optional<List<Issue>> issues = issueRepository.findAllByState(state);

		return obtenerIssues(issues);
	}

	
	@Override
	public List<Issue> getAllByBranchBeginEndDate(Instant beginDate, Instant endDate) {
		return this.issueRepository.findAllByBranchBeginEndDate(beginDate, endDate);
	}
	
	private List<Issue> obtenerIssues(Optional<List<Issue>> issues) {
		final List<Issue> issuesList = new ArrayList<Issue>();

		if (issues.isPresent()) {
			Issue issue;
			for (int i = 0; i < issues.get().size(); i++) {
				issue = issues.get().get(i);
				issuesList.add(issue);
			}

			return issuesList;
		} else {
			return Collections.emptyList();
		}
	}


}
