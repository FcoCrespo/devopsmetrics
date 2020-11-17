package edu.uclm.esi.devopsmetrics.controllers;

import java.io.IOException;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;

import edu.uclm.esi.devopsmetrics.domain.GithubOperations;
import edu.uclm.esi.devopsmetrics.domain.UserOperations;

@RestController
@RequestMapping("/commits")
/**
 * 
 * @author FcoCrespo
 * "https://esidevopsmetrics.herokuapp.com"
 */
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE }, allowedHeaders = "*")
public class CommitController {

	private static final Log LOG = LogFactory.getLog(CommitController.class);

	private final String errorMessage;
	private final UserOperations userOperations;
	private final GithubOperations githubOperations;

	@Autowired
	/**
	 * @author FcoCrespo
	 */

	public CommitController(final UserOperations userOperations, final GithubOperations githubOperations) {

		this.errorMessage = "[SERVER] No se ha encontrado ningún usuario con esos datos.";
		this.userOperations = userOperations;
		this.githubOperations = githubOperations;


	}

	/**
	 * Devuelve las ramas de un repositorio por su owner, nombre de repositorio y
	 * token de acceso
	 * 
	 * @author FcoCrespo
	 */
	@GetMapping(value = "/allbranches")
	@ApiOperation(value = "Find all branches", notes = "Return all branches")

	public ResponseEntity<String> allBranches(@RequestParam("tokenpass") final String tokenpass,
			@RequestParam("reponame") final String reponame, @RequestParam("owner") final String owner) {

		String repository = reponame;
		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			LOG.info("Get branches");
			return ResponseEntity.ok(this.githubOperations.getBranches(repository, owner));
		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}

	/**
	 * ATENCIÓN: Devuelve todos los commits del repositorio por su owner, nombre de
	 * repositorio y token de acceso
	 * 
	 * @author FcoCrespo
	 */

	@GetMapping(value = "/allcommits")
	@ApiOperation(value = "Find all branches", notes = "Return all branches")

	public ResponseEntity<String> allCommits(@RequestParam("tokenpass") final String tokenpass,
			@RequestParam("reponame") final String reponame, @RequestParam("owner") final String owner) {

		String repository = reponame;
		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {

			try {
				this.githubOperations.getCommits(repository, owner);
				return ResponseEntity.ok("Operation completed.");
			} catch (IOException e) {
				return ResponseEntity.badRequest().build();
			}	

		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}
	
	
	/**
	 * ATENCIÓN: Elimina los commits de una rama de la BBDD
	 * 
	 * @author FcoCrespo
	 */

	@DeleteMapping(value = "/deleteCommitsFromBranch")
	@ApiOperation(value = "Delete commits from a branch", notes = "Delete commits from a branch")

	public ResponseEntity<String> allCommits(@RequestParam("tokenpass") final String tokenpass,
			@RequestParam("branchId") final String branchId) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {

			this.githubOperations.deleteCommits(branchId);
			return ResponseEntity.ok("Operation completed.");	

		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}
}
