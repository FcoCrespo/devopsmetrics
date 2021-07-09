package edu.uclm.esi.devopsmetrics.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.exceptions.IssueCursorNotFoundException;
import edu.uclm.esi.devopsmetrics.models.IssueCursor;
import edu.uclm.esi.devopsmetrics.repositories.IssueCursorRepository;

@Service("IssueCursorService")
/**
 * @author FcoCrespo
 */
@Transactional
public class IssueCursorServiceImpl implements IssueCursorService{
	
	/**
	 * @author FcoCrespo
	 */
	private IssueCursorRepository issueCursorRepository;

	public IssueCursorServiceImpl(final IssueCursorRepository issueCursorRepository) {

		this.issueCursorRepository = issueCursorRepository;

	}


	@Override
	public List<IssueCursor> findAll() {
		final Optional<List<IssueCursor>> issues = issueCursorRepository.findAll();

		final List<IssueCursor> issuesList = new ArrayList<IssueCursor>();

		if (issues.isPresent()) {
			IssueCursor issue;
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
	public void saveIssueCursor(IssueCursor issueCursor) {
		this.issueCursorRepository.saveIssueCursor(issueCursor);
		
	}

	@Override
	public void updateIssueCursor(IssueCursor issueCursor) {
		this.issueCursorRepository.updateIssueCursor(issueCursor);
		
	}

	@Override
	public void deleteIssueCursor(String issueCursorId) {
		this.issueCursorRepository.deleteIssueCursor(issueCursorId);
		
	}

	@Override
	public IssueCursor findOne(String endCursor) {
		
		IssueCursor issuecursor = this.issueCursorRepository.findOne(endCursor);

	    if (issuecursor!=null) {

	      return issuecursor;

	    } else {

	      throw new IssueCursorNotFoundException(endCursor);

	    }

		
	}

	@Override
	public IssueCursor getByRepositoryAndOwner(String repository, String owner) {
		return this.issueCursorRepository.findByRepositoryAndOwner(repository, owner);
	}

	


}
