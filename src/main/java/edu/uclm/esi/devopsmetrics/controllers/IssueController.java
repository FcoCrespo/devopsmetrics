package edu.uclm.esi.devopsmetrics.controllers;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;

import edu.uclm.esi.devopsmetrics.domain.IssueOperations;
import edu.uclm.esi.devopsmetrics.domain.UserOperations;
import edu.uclm.esi.devopsmetrics.models.IssueRepo;

@RestController
@RequestMapping("/issues")
/**
 * 
 * @author FcoCrespo "https://esidevopsmetrics.herokuapp.com"
 * 
 */
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE }, allowedHeaders = "*")
public class IssueController {
	
	private static final Log LOG = LogFactory.getLog(IssueController.class);

	private final String errorMessage;
	private final UserOperations userOperations;
	private final IssueOperations issueOperations;

	private String message;

	@Autowired
	/**
	 * @author FcoCrespo
	 */

	public IssueController(final UserOperations userOperations, final IssueOperations issueOperations) {

		this.errorMessage = "[SERVER] No se ha encontrado ningún usuario con esos datos.";
		this.userOperations = userOperations;
		this.issueOperations = issueOperations;
		this.message = "Operation completed.";

	}

	/**
	 * Guarda las issues de un repositorio por su owner, nombre de repositorio y
	 * token de acceso
	 * 
	 * @author FcoCrespo
	 */
	@GetMapping(value = "/allissues")
	@ApiOperation(value = "Find all issues", notes = "Return all issues")

	public ResponseEntity<String> allIssues(@RequestParam("tokenpass") final String tokenpass,
			@RequestParam("reponame") final String reponame, @RequestParam("owner") final String owner) {

		String repository = reponame;
		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			try {
				LOG.info("Get issues");
				this.issueOperations.getIssues(repository, owner);
				return ResponseEntity.ok(this.message);
			}
			catch (IOException e) {
				return ResponseEntity.badRequest().build();
			}
			
		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}
	
	/**
	 * Devuelve los issues de un repositorio por su owner
	 * 
	 * @author FcoCrespo
	 */

	@GetMapping(value = "/issuesrepo")
	@ApiOperation(value = "Find all commitsb of a repository branch", notes = "Return all issues of a repository")

	public ResponseEntity<String> allIssuesFromRepository(@RequestParam("tokenpass") final String tokenpass,
			@RequestParam("reponame") final String reponame, @RequestParam("owner") final String owner) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {

			LOG.info("Get issues from the repository");
			return ResponseEntity.ok(this.issueOperations.getIssuesRepository(reponame, owner));

		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}


}
