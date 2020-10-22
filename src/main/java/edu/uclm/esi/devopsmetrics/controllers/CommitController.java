package edu.uclm.esi.devopsmetrics.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
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

	  public ResponseEntity<List<Branch>> allBranches(@RequestParam("username") final String usernamelogin,
		      @RequestParam("password") final String passwordlogin,
		      @RequestParam("reponame") final String reponame,
		      @RequestParam("owner") final String owner) throws InvalidRemoteException, TransportException, IOException, GitAPIException, InterruptedException {
		System.out.println("estoy dentro");
		final String usernameloginEncriptado = Utilities.encriptar(usernamelogin);
	    final String contrasenaloginEncriptado = Utilities.encriptar(passwordlogin);

	    final User usuario = usersService.getUserByUsernameAndPassword(usernameloginEncriptado, contrasenaloginEncriptado);
	    if (usuario != null) {
	      LOG.info("Get branches");
	      cg.getBranches(reponame, owner);
	      return ResponseEntity.ok(branchService.getBranchesByRepository(reponame));
	    } else {
	      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
	      return ResponseEntity.badRequest().build();
	    }
		
	  }
	  
	  @RequestMapping(value = "/allcommits", method = RequestMethod.GET)
	  @ApiOperation(value = "Find all branches", notes = "Return all branches")

	  //public ResponseEntity<List<Commit>> allCommits(@RequestParam("username") final String usernamelogin,
	  public String allCommits(@RequestParam("username") final String usernamelogin,
		      @RequestParam("password") final String passwordlogin,
		      @RequestParam("reponame") final String reponame,
		      @RequestParam("owner") final String owner){
		
		final String usernameloginEncriptado = Utilities.encriptar(usernamelogin);
	    final String contrasenaloginEncriptado = Utilities.encriptar(passwordlogin);

	    final User usuario = usersService.getUserByUsernameAndPassword(usernameloginEncriptado, contrasenaloginEncriptado);
	    if (usuario != null) {
	      LOG.info("Get commits");
	      try {
	    	  cg.getCommits(reponame, owner); 
		  } catch (IOException | GitAPIException | InterruptedException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		  } 
	      
	      return "bien";
	      //return ResponseEntity.ok(branchService.getBranchesByRepository(reponame));
	    } else {
	      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
	      return "mal";
	      //return ResponseEntity.badRequest().build();
	    }
		
	  }
	  
	  @RequestMapping(value = "/commitsbranch", method = RequestMethod.GET)
	  @ApiOperation(value = "Find all commits from a repository branch", notes = "Return all commits from a repository branch")
	  
	  public ResponseEntity<List<Commit>> allCommitsBranch(@RequestParam("username") final String usernamelogin,
		      @RequestParam("password") final String passwordlogin,
		      @RequestParam("reponame") final String reponame,
		      @RequestParam("branch") final String branch,
		      @RequestParam("owner") final String owner){
		
		final String usernameloginEncriptado = Utilities.encriptar(usernamelogin);
	    final String contrasenaloginEncriptado = Utilities.encriptar(passwordlogin);

	    final User usuario = usersService.getUserByUsernameAndPassword(usernameloginEncriptado, contrasenaloginEncriptado);
	    if (usuario != null) {
	      LOG.info("Get commits");
	      List <Commit> list1 = commitsService.getAllByBranch(reponame, branch);
	      System.out.println(list1.size());
	      List <Commit> list2 = commitsService.getAllByBranch(reponame, "0.7-stable");
	      System.out.println(list2.size());
	      
	      boolean existe = false, seguir=true;
	      List <Commit> listaNueva =  new ArrayList<Commit>();
	      
	      
	      for(int i = 0; i<list2.size(); i++) {
	    	  for(int j = 0; j<list1.size()&&seguir==true; j++) {
	    		  if (list2.get(i).getOid().equals(list1.get(j).getOid())){
	    			  seguir=false;
	    		  }
	    	  }
	    	  if(seguir==true) {
	    		  listaNueva.add(list2.get(i));
	    	  }
	    	  seguir=true;
	      }
	      
	      System.out.println(listaNueva.size());
	      return ResponseEntity.ok(listaNueva);
	    } else {
	    	LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
		    return ResponseEntity.badRequest().build();
	    }
		
	  }

}
