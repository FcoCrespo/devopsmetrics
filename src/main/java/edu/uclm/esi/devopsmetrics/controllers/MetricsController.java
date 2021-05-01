package edu.uclm.esi.devopsmetrics.controllers;



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

import edu.uclm.esi.devopsmetrics.domain.MetricsOperations;

@RestController
@RequestMapping("/devopsmetrics")
/**
 * 
 * @author FcoCrespo "https://esidevopsmetrics.herokuapp.com"
 * 
 */
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE }, allowedHeaders = "*")
public class MetricsController {
	
	private static final Log LOG = LogFactory.getLog(MetricsController.class);

	private final MetricsOperations metricsOperations;
	
	private String message;

	@Autowired
	/**
	 * @author FcoCrespo
	 */

	public MetricsController(final MetricsOperations metricsOperations) {

		this.metricsOperations = metricsOperations;
		this.message = "Operation completed.";

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
