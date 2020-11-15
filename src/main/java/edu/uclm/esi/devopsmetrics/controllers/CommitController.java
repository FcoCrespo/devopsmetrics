package edu.uclm.esi.devopsmetrics.controllers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
 * src CORS:
 * https://www.arquitecturajava.com/spring-rest-cors-y-su-configuracion/
 * 
 * @author FcoCrespo
 */
@CrossOrigin(origins = { "http://localhost:4200", "https://esidevopsmetrics.herokuapp.com" }, allowedHeaders = "*")
@Configuration
@EnableWebSecurity(debug = false) 
public class CommitController {

	private final Logger logger;

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
		this.logger = Logger.getLogger(CommitController.class.getName());


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
			logger.log(Level.INFO, "Get branches");
			return ResponseEntity.ok(this.githubOperations.getBranches(repository, owner));
		} else {
			logger.log(Level.INFO, this.errorMessage);
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

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {

			try {
				this.githubOperations.getCommits(reponame, owner);
				return ResponseEntity.ok("Operation completed.");
			} catch (IOException e) {
				logger.log(Level.INFO, e.toString());
				return ResponseEntity.badRequest().build();
			}	

		} else {
			logger.log(Level.INFO, this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}
}
