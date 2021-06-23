package edu.uclm.esi.devopsmetrics.controllers;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;


import edu.uclm.esi.devopsmetrics.config.RabbitMqMongo;
import edu.uclm.esi.devopsmetrics.domain.IssueOperations;
import edu.uclm.esi.devopsmetrics.domain.UserOperations;

@RestController
@RequestMapping("/issues")
/**
 * 
 * @author FcoCrespo "https://webesidevopsmetrics.herokuapp.com"
 * 
 */
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE }, allowedHeaders = "*")
public class IssueController {

	private static final Log LOG = LogFactory.getLog(IssueController.class);

	private final String errorMessage;
	private final UserOperations userOperations;
	private final IssueOperations issueOperations;
	private String reponameStr;
	private String ownerStr;
	private String begindateStr;
	private String enddateStr;
	private String idUserStr;

	private String message;

	@Autowired
	RabbitTemplate rabbitTemplate;
	/**
	 * @author FcoCrespo
	 */

	public IssueController(final UserOperations userOperations, final IssueOperations issueOperations) {

		this.errorMessage = "[SERVER] No se ha encontrado ningún usuario con esos datos.";
		this.userOperations = userOperations;
		this.issueOperations = issueOperations;
		this.message = "Operation completed.";
		this.reponameStr = "reponame";
		this.ownerStr = "owner";
		this.begindateStr = "begindate";
		this.enddateStr = "enddate";
		this.idUserStr = "idUser";

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
			@RequestParam("reponame") final String repository, @RequestParam("owner") final String owner) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			try {
				LOG.info("Get issues");
				
		        rabbitTemplate.convertAndSend(RabbitMqMongo.EXCHANGE_NAME,
		        		RabbitMqMongo.ROUTING_KEY, 
		        		this.issueOperations.getIssues(repository, owner));

				
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
	 * Actualiza las issues de un repositorio por su owner, nombre de repositorio y
	 * token de acceso
	 * 
	 * @author FcoCrespo
	 */
	@GetMapping(value = "/updateissues")
	@ApiOperation(value = "Find all issues", notes = "Return all issues")

	public ResponseEntity<String> updateIssues(@RequestParam("tokenpass") final String tokenpass,
			@RequestParam("reponame") final String repository, @RequestParam("owner") final String owner) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			try {
				LOG.info("Update issues");
				rabbitTemplate.convertAndSend(RabbitMqMongo.EXCHANGE_NAME,
		        		RabbitMqMongo.ROUTING_KEY, 
		        		this.issueOperations.actualizarValores(repository, owner));
				
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
	 * Devuelve los issues de un repositorio por su owner
	 * 
	 * @author FcoCrespo
	 */

	@GetMapping(value = "/issuesrepo")
	@ApiOperation(value = "Find all issues of a repository", notes = "Return all issues of a repository")

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
	
	/**
	 * Devuelve los issues de un repositorio por su owner por el usuario de github
	 *  
	 * @author FcoCrespo
	 */

	@GetMapping(value = "/issuesrepouser")
	@ApiOperation(value = "Find all issues of a repository", notes = "Return all issues of a repository")

	public ResponseEntity<String> allIssuesFromRepository(@RequestParam("tokenpass") final String tokenpass,
			@RequestParam("reponame") final String reponame, @RequestParam("owner") final String owner,
			@RequestParam("idUser") final String idUser) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {

			LOG.info("Get issues from the repository");
			return ResponseEntity.ok(this.issueOperations.getIssuesRepositoryByAssignee(reponame, owner, idUser));

		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}
	
	

	/**
	 * Devuelve los issues de un repositorio por su owner entre dos fechas a partir
	 * de sus creacion 
	 * 
	 * @author FcoCrespo
	 */

	@PostMapping(value = "/issuesrepocreationdates")
	@ApiOperation(value = "Find all issues of a repository between two dates by his creation", notes = "Return all issues of a repositorybetween two dates by his creation")

	public ResponseEntity<String> allIssuesFromRepositoryDateCreation(@RequestParam("tokenpass") final String tokenpass,
			@RequestBody final String message) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			final JSONObject jso = new JSONObject(message);
			String reponame = jso.getString(this.reponameStr);
			String owner = jso.getString(this.ownerStr);
			String begindate = jso.getString(this.begindateStr);
			String enddate = jso.getString(this.enddateStr);

			LOG.info("Get repo test metrics between two dates by his creation");
			return ResponseEntity
					.ok(this.issueOperations.getIssuesRepositoryDatesCreation(reponame, owner, begindate, enddate));

		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}
	
	/**
	 * Devuelve los issues de un repositorio por su owner entre dos fechas a partir
	 * de sus creacion por el usuario de github
	 * 
	 * @author FcoCrespo
	 */

	@PostMapping(value = "/issuesrepocreationdatesuser")
	@ApiOperation(value = "Find all issues of a repository between two dates by his creation by user github", notes = "Return all issues of a repositorybetween two dates by his creation by user github")

	public ResponseEntity<String> allIssuesFromRepositoryDateCreationByUser(@RequestParam("tokenpass") final String tokenpass,
			@RequestBody final String message) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			final JSONObject jso = new JSONObject(message);
			String reponame = jso.getString(this.reponameStr);
			String owner = jso.getString(this.ownerStr);
			String begindate = jso.getString(this.begindateStr);
			String enddate = jso.getString(this.enddateStr);
			String idUser = jso.getString(this.idUserStr);

			LOG.info("Get repo test metrics between two dates by his creation");
			return ResponseEntity
					.ok(this.issueOperations.getIssuesRepositoryDatesCreationUser(reponame, owner, begindate, enddate, idUser));

		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}
	
	
	
	/**
	 * Devuelve los issues de un repositorio por su owner entre dos fechas que estan
	 * cerradas por el usuario de github
	 * 
	 * @author FcoCrespo
	 */

	@PostMapping(value = "/issuesrepocloseddates")
	@ApiOperation(value = "Find all issues of a repository between two dates that are closed ", notes = "Return all issues of a repositorybetween two dates that are closed")

	public ResponseEntity<String> allIssuesFromRepositoryDateClosed(@RequestParam("tokenpass") final String tokenpass,
			@RequestBody final String message) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			final JSONObject jso = new JSONObject(message);
			String reponame = jso.getString(this.reponameStr);
			String owner = jso.getString(this.ownerStr);
			String begindate = jso.getString(this.begindateStr);
			String enddate = jso.getString(this.enddateStr);

			LOG.info("Get repo test metrics between two dates that are closed");
			return ResponseEntity
					.ok(this.issueOperations.getIssuesRepositoryDatesClosed(reponame, owner, begindate, enddate));

		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}

	/**
	 * Devuelve los issues de un repositorio por su owner entre dos fechas que estan
	 * cerradas por el usuario
	 * 
	 * @author FcoCrespo
	 */

	@PostMapping(value = "/issuesrepocloseddatesuser")
	@ApiOperation(value = "Find all issues of a repository between two dates that are closed by user  github", notes = "Return all issues of a repositorybetween two dates that are closed")

	public ResponseEntity<String> allIssuesFromRepositoryDateClosedUser(@RequestParam("tokenpass") final String tokenpass,
			@RequestBody final String message) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			final JSONObject jso = new JSONObject(message);
			String reponame = jso.getString(this.reponameStr);
			String owner = jso.getString(this.ownerStr);
			String begindate = jso.getString(this.begindateStr);
			String enddate = jso.getString(this.enddateStr);
			String idUser = jso.getString(this.idUserStr);

			LOG.info("Get repo test metrics between two dates that are closed");
			return ResponseEntity
					.ok(this.issueOperations.getIssuesRepositoryDatesClosedUser(reponame, owner, begindate, enddate, idUser));

		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}
	
	

	/**
	 * Devuelve los issues de un repositorio por su owner entre dos fechas que estan
	 * abiertas
	 * 
	 * @author FcoCrespo
	 */

	@PostMapping(value = "/issuesrepoopeneddates")
	@ApiOperation(value = "Find all issues of a repository between two dates that are opened by user  github", notes = "Return all issues of a repositorybetween two dates that are opened")

	public ResponseEntity<String> allIssuesFromRepositoryDateOpened(@RequestParam("tokenpass") final String tokenpass,
			@RequestBody final String message) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			final JSONObject jso = new JSONObject(message);
			String reponame = jso.getString(this.reponameStr);
			String owner = jso.getString(this.ownerStr);
			String begindate = jso.getString(this.begindateStr);
			String enddate = jso.getString(this.enddateStr);

			LOG.info("Get repo test metrics between two dates that are opened");
			return ResponseEntity
					.ok(this.issueOperations.getIssuesRepositoryDatesOpened(reponame, owner, begindate, enddate));

		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}

	/**
	 * Devuelve los issues de un repositorio por su owner entre dos fechas que estan
	 * abiertas por el usuario de github
	 * 
	 * @author FcoCrespo
	 */

	@PostMapping(value = "/issuesrepoopeneddatesuser")
	@ApiOperation(value = "Find all issues of a repository between two dates that are opened by user  github", notes = "Return all issues of a repositorybetween two dates that are opened")

	public ResponseEntity<String> allIssuesFromRepositoryDateOpenedUser(@RequestParam("tokenpass") final String tokenpass,
			@RequestBody final String message) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			final JSONObject jso = new JSONObject(message);
			String reponame = jso.getString(this.reponameStr);
			String owner = jso.getString(this.ownerStr);
			String begindate = jso.getString(this.begindateStr);
			String enddate = jso.getString(this.enddateStr);
			String idUser = jso.getString(this.idUserStr);

			LOG.info("Get repo test metrics between two dates that are opened");
			return ResponseEntity
					.ok(this.issueOperations.getIssuesRepositoryDatesOpenedUser(reponame, owner, begindate, enddate, idUser));

		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}



	
	
	
}
