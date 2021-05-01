package edu.uclm.esi.devopsmetrics.controllers;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
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

import edu.uclm.esi.devopsmetrics.domain.MetricsOperations;
import edu.uclm.esi.devopsmetrics.domain.TestsOperations;
import edu.uclm.esi.devopsmetrics.domain.UserOperations;

@RestController
@RequestMapping("/metrics")
/**
 * 
 * @author FcoCrespo "https://esidevopsmetrics.herokuapp.com"
 * 
 */
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE }, allowedHeaders = "*")
public class MetricsController {
	
	private static final Log LOG = LogFactory.getLog(MetricsController.class);

	private final String errorMessage;
	private final MetricsOperations metricsOperations;
	//private final TestsOperations testsOperations;
	private final UserOperations userOperations;
	
	private String message;

	@Autowired
	/**
	 * @author FcoCrespo
	 */

	public MetricsController(final MetricsOperations metricsOperations,final UserOperations userOperations) {
		//, final TestsOperations testsOperations){

		this.errorMessage = "[SERVER] No se ha encontrado ningún usuario con esos datos.";
		this.metricsOperations = metricsOperations;
		//this.testsOperations = testsOperations;
		this.userOperations = userOperations;
		this.message = "Operation completed.";

	}
	
	/**
	 * Devuelve las metricas de un repositorio y su owner a traves de un token de acceso
	 * 
	 * @author FcoCrespo
	 */
	@GetMapping(value = "/allmetrics")
	@ApiOperation(value = "Find all metrics from repo", notes = "Find all metrics from repo")

	public ResponseEntity<String> allMetrics(@RequestParam("tokenpass") final String tokenpass,
			@RequestParam("reponame") final String reponame, @RequestParam("owner") final String owner) {

		String repository = reponame;
		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			LOG.info("Get repo metrics");
			return ResponseEntity.ok(this.metricsOperations.getRepoMetrics(repository, owner));
		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}
	
	/**
	 * Obtiene las metricas de un repositorio y su owner a traves del acceso del directorio en que se alojan los archivos xml
	 * 
	 * @author FcoCrespo
	 */
	@GetMapping(value = "/savemetrics")
	@ApiOperation(value = "Get all metrics from repo", notes = "Get all metrics from repo")

	public ResponseEntity<String> saveMetrics(@RequestParam("reponame") final String reponame, @RequestParam("owner") final String owner) {

		String repository = reponame;
		try {
			LOG.info("Save repo metrics");
			this.metricsOperations.saveRepoMetrics(repository, owner);
			return ResponseEntity.ok(this.message);
		}
		catch(Exception e) {
			return ResponseEntity.ok("Error al guardar los datos de los archivos de las métricas.");
		}
		
	}
	
	
}
