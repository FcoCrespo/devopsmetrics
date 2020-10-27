package edu.uclm.esi.devopsmetrics.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
	  
	  /**
	   * Devuelve las ramas de un repositorio por su owner, nombre de repositorio y token de acceso
	   * 
	   * @author FcoCrespo
	   * @throws InvalidRemoteException, TransportException, IOException, GitAPIException, InterruptedException
	   */
	  @RequestMapping(value = "/allbranches", method = RequestMethod.GET)
	  @ApiOperation(value = "Find all branches", notes = "Return all branches")

	  public ResponseEntity<List<Branch>> allBranches(@RequestParam("tokenpass") final String tokenpass,
		      @RequestParam("reponame") final String reponame,
		      @RequestParam("owner") final String owner) throws InvalidRemoteException, TransportException, IOException, GitAPIException, InterruptedException {

	    final User usuario = usersService.getUserByTokenPass(tokenpass);
	    if (usuario != null) {
	      LOG.info("Get branches");
	      cg.getBranches(reponame, owner);
	      return ResponseEntity.ok(branchService.getBranchesByRepository(reponame));
	    } else {
	      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
	      return ResponseEntity.badRequest().build();
	    }
		
	  }
	  
	  /**
	   * ATENCIÓN: Devuelve todos los commits del repositorio por su owner, nombre de repositorio y token de acceso
	   * 
	   * @author FcoCrespo
	   */
	  
	  @RequestMapping(value = "/allcommits", method = RequestMethod.GET)
	  @ApiOperation(value = "Find all branches", notes = "Return all branches")

	  public String allCommits(@RequestParam("tokenpass") final String tokenpass,
		      @RequestParam("reponame") final String reponame,
		      @RequestParam("owner") final String owner){
		

	    final User usuario = usersService.getUserByTokenPass(tokenpass);
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
	  
	  /**
	   * Devuelve los commits de una rama de un repositorio por su owner, nombre de repositorio y token de acceso
	   * 
	   * @author FcoCrespo
	   */
	  
	  @RequestMapping(value = "/commitsbranch", method = RequestMethod.GET)
	  @ApiOperation(value = "Find all commits from a repository branch", notes = "Return all commits from a repository branch")
	  
	  public ResponseEntity<List<Commit>> allCommitsBranch(@RequestParam("tokenpass") final String tokenpass,
		      @RequestParam("reponame") final String reponame,
		      @RequestParam("branch") final String branch,
		      @RequestParam("owner") final String owner){

	    final User usuario = usersService.getUserByTokenPass(tokenpass);
	    if (usuario != null) {
	      LOG.info("Get commits");
	      List <Commit> commits = commitsService.getAllByBranch(reponame, branch);
	      System.out.println("Size: "+commits.size());
	     
	      return ResponseEntity.ok(commits);
	    } else {
	    	LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
		    return ResponseEntity.badRequest().build();
	    }
		
	  }
	  
	  /**
	   * Devuelve los commits de un autor en una rama de un repositorio por su owner, nombre de repositorio y token de acceso
	   * 
	   * @author FcoCrespo
	   */
	  
	  @RequestMapping(value = "/commitsbranchauthor", method = RequestMethod.GET)
	  @ApiOperation(value = "Find all commits from a repository branch", notes = "Return all commits from a repository branch")
	  
	  public ResponseEntity<List<Commit>> allCommitsBranchAuthor(@RequestParam("tokenpass") final String tokenpass,
		      @RequestParam("reponame") final String reponame,
		      @RequestParam("branch") final String branch,
		      @RequestParam("authorname") final String authorName,
		      @RequestParam("owner") final String owner){

	    final User usuario = usersService.getUserByTokenPass(tokenpass);
	    if (usuario != null) {
	      LOG.info("Get commits");
	      List <Commit> commits = commitsService.getAllByBranchAndAuthorName(reponame, branch, authorName);
	      System.out.println("Size: "+commits.size());
	     
	      return ResponseEntity.ok(commits);
	    } else {
	    	LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
		    return ResponseEntity.badRequest().build();
	    }
		
	  }
	  
	  /**
	   * Devuelve los commits en un intervalo de tiempo en una rama de un repositorio por su owner, nombre de repositorio y token de acceso
	   * 
	   * @author FcoCrespo
	   */
	  
	  @RequestMapping(value = "/commitsbranchdate", method = RequestMethod.GET)
	  @ApiOperation(value = "Find all commits from a repository branch by date", notes = "Return all commits from a repository branch")
	  
	  public ResponseEntity<List<Commit>> allCommitsBranchBeginEndDate(@RequestParam("tokenpass") final String tokenpass,
		      @RequestParam("reponame") final String reponame,
		      @RequestParam("branch") final String branch,
		      @RequestParam("begindate") final String beginDateString,
		      @RequestParam("enddate") final String endDateString,
		      @RequestParam("owner") final String owner){

	    final User usuario = usersService.getUserByTokenPass(tokenpass);
	    if (usuario != null) {
	      LOG.info("Get commits");
	      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu hh:mm");
	      LocalDate ldtBegin = LocalDate.parse( beginDateString , formatter );
	      LocalDate ldtEnd = LocalDate.parse( endDateString , formatter );
	      
	      Instant beginDate = ldtBegin.atStartOfDay(ZoneId.systemDefault()).toInstant();
	      Instant endDate = ldtEnd.atStartOfDay(ZoneId.systemDefault()).toInstant();
	      
	      String [] bestDataDates = commitsService.getBestBeginEndData(reponame, branch, beginDate, endDate);
	      
	      List <Commit> commits = commitsService.getAllByBranchBeginEndDate(reponame, branch, beginDate, bestDataDates[0], endDate, bestDataDates[1]);
	      
	     
	      return ResponseEntity.ok(commits);
	    } else {
	    	LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
		    return ResponseEntity.badRequest().build();
	    }
		
	  }
	  
	  /**
	   * Devuelve los commits de un autor en un intervalo de tiempo en una rama de un repositorio por su owner, nombre de repositorio y token de acceso
	   * 
	   * @author FcoCrespo
	   */
	  
	  @RequestMapping(value = "/commitsbranchauthordate", method = RequestMethod.GET)
	  @ApiOperation(value = "Find all commits from a repository branch by author and date", notes = "Return all commits from a repository branch")
	  
	  public ResponseEntity<List<Commit>> allCommitsBranchBeginEndDate(@RequestParam("tokenpass") final String tokenpass,
		      @RequestParam("reponame") final String reponame,
		      @RequestParam("branch") final String branch,
		      @RequestParam("authorname") final String authorName,
		      @RequestParam("begindate") final String beginDateString,
		      @RequestParam("enddate") final String endDateString,
		      @RequestParam("owner") final String owner){

	    final User usuario = usersService.getUserByTokenPass(tokenpass);
	    if (usuario != null) {
	      LOG.info("Get commits");
	      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu hh:mm");
	      LocalDate ldtBegin = LocalDate.parse( beginDateString , formatter );
	      LocalDate ldtEnd = LocalDate.parse( endDateString , formatter );
	      
	      Instant beginDate = ldtBegin.atStartOfDay(ZoneId.systemDefault()).toInstant();
	      Instant endDate = ldtEnd.atStartOfDay(ZoneId.systemDefault()).toInstant();
	      
	      String [] bestDataDates = commitsService.getBestBeginEndData(reponame, branch, beginDate, endDate);
	      
	      List <Commit> commits = commitsService.getAllByBranchAuthorBeginEndDate(reponame, branch, authorName, beginDate, bestDataDates[0], endDate, bestDataDates[1]);
	      
	     
	      return ResponseEntity.ok(commits);
	    } else {
	    	LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
		    return ResponseEntity.badRequest().build();
	    }
		
	  }

}
