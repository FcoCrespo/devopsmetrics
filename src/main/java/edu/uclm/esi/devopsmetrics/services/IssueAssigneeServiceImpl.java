package edu.uclm.esi.devopsmetrics.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.models.IssueAssignee;
import edu.uclm.esi.devopsmetrics.repositories.IssueAssigneeRepository;

@Service("IssueAssigneeService")
/**
 * @author FcoCrespo
 */
@Transactional
public class IssueAssigneeServiceImpl implements IssueAssigneeService{
	
	/**
	 * @author FcoCrespo
	 */
	private IssueAssigneeRepository issueAssigneeRepository;

	public IssueAssigneeServiceImpl(final IssueAssigneeRepository issueAssigneeRepository) {

		this.issueAssigneeRepository = issueAssigneeRepository;

	}

	@Override
	public List<IssueAssignee> findAll() {
		final Optional<List<IssueAssignee>> issues = issueAssigneeRepository.findAll();

		final List<IssueAssignee> issuesList = new ArrayList<IssueAssignee>();

		if (issues.isPresent()) {
			IssueAssignee issue;
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
	public void saveIssueAssignee(IssueAssignee issueAssignee) {
		this.issueAssigneeRepository.saveIssueAssignee(issueAssignee);
		
	}

	@Override
	public void updateIssueAssignee(IssueAssignee issueAssignee) {
		this.issueAssigneeRepository.updateIssueAssignee(issueAssignee);
		
	}

	@Override
	public void deleteIssueAssignee(String issueAssigneeId) {
		this.issueAssigneeRepository.deleteIssueAssignee(issueAssigneeId);
		
	}

	@Override
	public IssueAssignee findOne(String issueid) {
		return this.issueAssigneeRepository.findOne(issueid);
	}

	@Override
	public IssueAssignee getByAssignee(String usergithub) {
		return this.issueAssigneeRepository.findByAssignee(usergithub);
	}

	@Override
	public List<IssueAssignee> getAllByIdIssue(String issue) {
		return this.issueAssigneeRepository.findAllByIdIssue(issue);
	}

	@Override
	public IssueAssignee getByAssigneeAndIssue(String usergithub, String issue) {
		return this.issueAssigneeRepository.findByAssigneeAndIssue(usergithub, issue);
	}

}
