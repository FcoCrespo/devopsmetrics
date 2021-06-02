package edu.uclm.esi.devopsmetrics.controllers;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
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

import edu.uclm.esi.devopsmetrics.domain.MetricsOperations;
import edu.uclm.esi.devopsmetrics.domain.TestOperations;
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

	private final MetricsOperations metricsOperations;
	private final TestOperations testsOperations;
	private final UserOperations userOperations;

	private String message;
	private final String errorMessage;

	@Autowired
	/**
	 * @author FcoCrespo
	 */

	public MetricsController(final MetricsOperations metricsOperations, final UserOperations userOperations,
			final TestOperations testsOperations) {

		this.metricsOperations = metricsOperations;
		this.userOperations = userOperations;
		this.testsOperations = testsOperations;
		this.message = "Operation completed.";
		this.errorMessage = "[SERVER] No se ha encontrado ningún usuario con esos datos.";

	}

	/**
	 * Obtiene las metricas de un repositorio y su owner a traves del acceso del
	 * directorio en que se alojan los archivos xml
	 * 
	 * @author FcoCrespo
	 */
	@GetMapping(value = "/savemetrics")
	@ApiOperation(value = "Get all metrics from repo", notes = "Get all metrics from repo")

	public ResponseEntity<String> saveMetrics(@RequestParam("reponame") final String repository,
			@RequestParam("owner") final String owner) {

		try {
			LOG.info("Save repo metrics");
			this.metricsOperations.saveRepoMetrics(repository, owner);
			return ResponseEntity.ok(this.message);
		} catch (Exception e) {
			return ResponseEntity.ok("Error al guardar los datos de los archivos de las métricas.");
		}

	}

	/**
	 * Devuelve las metricas de un repositorio y su owner a traves de un token de
	 * acceso
	 * 
	 * @author FcoCrespo
	 * @throws IOException
	 */
	@GetMapping(value = "/allmetrics")
	@ApiOperation(value = "Find all metrics from repo", notes = "Find all metrics from repo")

	public ResponseEntity<String> allMetrics(@RequestParam("tokenpass") final String tokenpass,
			@RequestParam("reponame") final String repository, @RequestParam("owner") final String owner)
			throws IOException {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			LOG.info("Get repo metrics");
			return ResponseEntity.ok(this.metricsOperations.getRepoMetrics(repository, owner, tokenpass));
		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}

	/**
	 * Devuelve las metricas los tests de un repositorio y su owner a traves de un
	 * token de acceso entre dos fechas dadas
	 * 
	 * @author FcoCrespo
	 * @throws IOException
	 */
	@PostMapping(value = "/allmetricsdate")
	@ApiOperation(value = "Find all metrics from repo between two dates", notes = "Find all metrics from repo between two dates")

	public ResponseEntity<String> allMetricsDate(@RequestParam("tokenpass") final String tokenpass,
			@RequestBody final String message) throws IOException {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			LOG.info("Get repo test metrics");

			return ResponseEntity.ok(this.metricsOperations.getRepoMetricsDate(tokenpass, message));
		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}

	/**
	 * Obtiene las metricas de los test de un repositorio y su owner a traves del
	 * acceso al directorio en que se alojan los archivos xml
	 * 
	 * @author FcoCrespo
	 */
	@GetMapping(value = "/savetestmetrics")
	@ApiOperation(value = "Get all test metrics from repo", notes = "Get all test metrics from repo")

	public ResponseEntity<String> saveTestMetrics(@RequestParam("reponame") final String reponame,
			@RequestParam("owner") final String owner) {

		try {
			LOG.info("Save repo test metrics");
			this.testsOperations.saveRepoTestMetrics(reponame, owner);
			return ResponseEntity.ok(this.message);
		} catch (Exception e) {
			return ResponseEntity.ok("Error al guardar los datos de los archivos de los test reports.");
		}

	}

	/**
	 * Devuelve las metricas los tests de un repositorio y su owner a traves de un
	 * token de acceso
	 * 
	 * @author FcoCrespo
	 */
	@GetMapping(value = "/alltestmetrics")
	@ApiOperation(value = "Find all metrics from repo", notes = "Find all metrics from repo")

	public ResponseEntity<String> allTestMetrics(@RequestParam("tokenpass") final String tokenpass,
			@RequestParam("reponame") final String repository, @RequestParam("owner") final String owner) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			LOG.info("Get repo test metrics");
			return ResponseEntity.ok(this.testsOperations.getRepoTestMetrics(repository, owner));
		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}

	/**
	 * Devuelve las metricas los tests de un repositorio y su owner a traves de un
	 * token de acceso entre dos fechas dadas
	 * 
	 * @author FcoCrespo
	 */
	@PostMapping(value = "/alltestmetricsdate")
	@ApiOperation(value = "Find all metrics from repo between two dates", notes = "Find all metrics from repo between two dates")

	public ResponseEntity<String> allTestMetricsDate(@RequestParam("tokenpass") final String tokenpass,
			@RequestBody final String message) {

		boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			final JSONObject jso = new JSONObject(message);
			String reponame = jso.getString("reponame");
			String owner = jso.getString("owner");
			String begindate = jso.getString("begindate");
			String enddate = jso.getString("enddate");

			LOG.info("Get repo test metrics between two dates");
			return ResponseEntity.ok(this.testsOperations.getRepoTestMetricsDates(reponame, owner, begindate, enddate));

		} else {
			LOG.info(this.errorMessage);
			return ResponseEntity.badRequest().build();
		}

	}

}