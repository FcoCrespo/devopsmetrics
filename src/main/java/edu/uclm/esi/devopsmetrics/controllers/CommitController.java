package edu.uclm.esi.devopsmetrics.controllers;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;

import edu.uclm.esi.devopsmetrics.domain.GithubOperations;
import edu.uclm.esi.devopsmetrics.domain.UserOperations;
import esi.uclm.esi.devopsmetrics.config.RabbitMqConfig;

@RestController
@RequestMapping("/commits")
/**
 * 
 * @author FcoCrespo "https://webesidevopsmetrics.herokuapp.com"
 * 
 */
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE }, allowedHeaders = "*")
public class CommitController {

	private static final Log LOG = LogFactory.getLog(CommitController.class);

	private final String errorMessage;
	private final UserOperations userOperations;
	private final GithubOperations githubOperations;

	private String message;

	@Autowired
	RabbitTemplate rabbitTemplate;
	/**
	 * @author FcoCrespo
	 */

	public CommitController(final UserOperations userOperations, final GithubOperations githubOperations) {

		this.errorMessage = "[SERVER] No se ha encontrado ningún usuario con esos datos.";
		this.userOperations = userOperations;
		this.githubOperations = githubOperations;
		this.message = "Operation completed.";

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
			@RequestParam("reponame") final String repository, @RequestParam("owner") final String owner) {

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
	 * Devuelve los repositorios registrados en el sistema token de acceso
	 * 
	 * @author FcoCrespo
	 */
	@GetMapping(value = "/allrepositories")
	@ApiOperation(value = "Find all repositories", notes = "Return all repositories")

	public ResponseEntity<String> allRepositories(@RequestParam("tokenpass") final String tokenpass) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			LOG.info("Get repositories");
			return ResponseEntity.ok(this.githubOperations.getRepositories());
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
	@ApiOperation(value = "Find all commits", notes = "Return all commits")

	public ResponseEntity<String> allCommits(@RequestParam("tokenpass") final String tokenpass,
			@RequestParam("reponame") final String repository, @RequestParam("owner") final String owner) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {

			try {
				this.githubOperations.getCommits(repository, owner);
				return ResponseEntity.ok(this.message);
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
			return ResponseEntity.ok(this.message);

		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}

	/**
	 * Devuelve el primer commit de cada rama para poner el orden correcto de
	 * creacion de cada rama
	 * 
	 * @author FcoCrespo
	 */

	@GetMapping(value = "/branchesfirstcommit")
	@ApiOperation(value = "Find all branches", notes = "Return all branches")

	public ResponseEntity<String> allBranchesFirstCommit(@RequestParam("tokenpass") final String tokenpass,
			@RequestParam("reponame") final String reponame, @RequestParam("owner") final String owner) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {

			try {
				this.githubOperations.getFirstCommitByBranch(reponame, owner);
				return ResponseEntity.ok(this.message);
			} catch (IOException e) {
				return ResponseEntity.badRequest().build();
			}

		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}

	/**
	 * Devuelve los commits de una branch de un repositorio
	 * 
	 * @author FcoCrespo
	 */

	@GetMapping(value = "/commitsbranch")
	@ApiOperation(value = "Find all commits of a repository branch", notes = "Return all commits of a repository branch")

	public ResponseEntity<String> allCommitsFromRepositoryBranch(@RequestParam("tokenpass") final String tokenpass,
			@RequestParam("reponame") final String reponame, @RequestParam("branch") final String branch) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			
			String response = (String) rabbitTemplate.convertSendAndReceive(RabbitMqConfig.EXCHANGE_NAME,
		        		RabbitMqConfig.ROUTING_KEY, this.githubOperations.getCommitsFromRepositoryBranch(reponame, branch));

			LOG.info("Get commits from repository branch");
			return ResponseEntity.ok(response);

		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}

	/**
	 * Devuelve los commits de una branch de un repositorio por el nombre del autor
	 * 
	 * @author FcoCrespo
	 */

	@GetMapping(value = "/commitsbranchauthor")
	@ApiOperation(value = "Find all commits of a repository branch by his author", notes = "Return all commits of a repository branch by his author")

	public ResponseEntity<String> allCommitsBranchAuthor(@RequestParam("tokenpass") final String tokenpass,
			@RequestParam("reponame") final String reponame, @RequestParam("branch") final String branch,
			@RequestParam("authorname") final String authorName) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {

			LOG.info("Get commits from repository branch by his author");
			return ResponseEntity
					.ok(this.githubOperations.getCommitsByBranchAndAuthorName(reponame, branch, authorName));

		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}

	/**
	 * Devuelve los commits de una branch de un repositorio por el nombre del autor
	 * 
	 * @author FcoCrespo
	 */

	@PostMapping(value = "/commitsbranchdate")
	@ApiOperation(value = "Find all commits of a repository branch between two dates", notes = "Return all commits of a repository branch between two dates")

	public ResponseEntity<String> allCommitsBranchByDate(@RequestParam("tokenpass") final String tokenpass,
			@RequestBody final String message) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			final JSONObject jso = new JSONObject(message);
			String reponame = jso.getString("reponame");
			String branch = jso.getString("branch");
			String begindate = jso.getString("begindate");
			String enddate = jso.getString("enddate");

			LOG.info("Get commits from repository branch between the dates.");

			return ResponseEntity
					.ok(this.githubOperations.getAllByBranchBeginEndDate(reponame, branch, begindate, enddate));

		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}

	@PostMapping(value = "/commitsbranchdateauthor")
	@ApiOperation(value = "Find all commits of a repository branch between two dates by the author", notes = "Return all commits of a repository branch between two dates by the author")

	public ResponseEntity<String> allCommitsBranchByDateAuthor(@RequestParam("tokenpass") final String tokenpass,
			@RequestBody final String message) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			final JSONObject jso = new JSONObject(message);
			String reponame = jso.getString("reponame");
			String branch = jso.getString("branch");
			String begindate = jso.getString("begindate");
			String enddate = jso.getString("enddate");
			String authorname = jso.getString("authorname");

			LOG.info("Get commits from repository branch between the dates.");

			return ResponseEntity.ok(this.githubOperations.getAllByBranchBeginEndDateByAuthor(reponame, branch,
					begindate, enddate, authorname));

		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}

}
