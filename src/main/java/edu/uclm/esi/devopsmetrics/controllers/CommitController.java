package edu.uclm.esi.devopsmetrics.controllers;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;

import edu.uclm.esi.devopsmetrics.services.CommitService;
import edu.uclm.esi.devopsmetrics.services.UserService;
import edu.uclm.esi.devopsmetrics.services.BranchService;
import edu.uclm.esi.devopsmetrics.services.CommitCursorService;
import edu.uclm.esi.devopsmetrics.exceptions.CommitCursorNotFoundException;
import edu.uclm.esi.devopsmetrics.exceptions.CommitNotFoundException;
import edu.uclm.esi.devopsmetrics.exceptions.UserNotFoundException;
import edu.uclm.esi.devopsmetrics.models.Commit;
import edu.uclm.esi.devopsmetrics.models.CommitCursor;
import edu.uclm.esi.devopsmetrics.models.User;
import edu.uclm.esi.devopsmetrics.models.Branch;
import edu.uclm.esi.devopsmetrics.utilities.CommitsGithub;
import edu.uclm.esi.devopsmetrics.utilities.Utilities;

@RestController
@RequestMapping("/commits")
/**
 * @author FcoCrespo
 */
//src CORS: https://www.arquitecturajava.com/spring-rest-cors-y-su-configuracion/
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, allowedHeaders = "*")
public class CommitController {

	  private static final Log LOG = LogFactory.getLog(CommitController.class);

	  private final CommitService commitsService;
	  private final CommitCursorService commitscursorService;
	  private final UserService usersService;
	  private final BranchService branchService;
	  private final CommitsGithub cg;

	  @Autowired
	  /**
	   * @author FcoCrespo
	   */

	  public CommitController(final CommitService commitsService, CommitCursorService commitscursorService,
			  				  final UserService usersService, final BranchService branchService, final CommitsGithub cg) {
	    this.commitsService = commitsService;
	    this.commitscursorService = commitscursorService;
	    this.usersService = usersService;
	    this.branchService =  branchService;
	    this.cg = cg;
	  }
	  
	  @RequestMapping(value = "/allbranches", method = RequestMethod.GET)
	  @ApiOperation(value = "Find all branches", notes = "Return all branches")

	  //public ResponseEntity<List<Commit>> allCommits(@RequestParam("username") final String usernamelogin,
	  public ResponseEntity<List<Branch>> allBranches(@RequestParam("reponame") final String reponame,
		      @RequestParam("owner") final String owner) throws InvalidRemoteException, TransportException, IOException, GitAPIException, InterruptedException {
		
		//final String usernameloginEncriptado = Utilities.encriptar(usernamelogin);
	    //final String contrasenaloginEncriptado = Utilities.encriptar(passwordlogin);

	    //final User usuario = usersService.getUserByUsernameAndPassword(usernameloginEncriptado, contrasenaloginEncriptado);
	    //if (usuario != null) {
	      LOG.info("Get commit");
	      //cg.getBranches(reponame, owner);
	      return ResponseEntity.ok(branchService.getBranchesByRepository(reponame));
	    /*} else {
	      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
	      return ResponseEntity.badRequest().build();
	    }*/
		
	  }

}
