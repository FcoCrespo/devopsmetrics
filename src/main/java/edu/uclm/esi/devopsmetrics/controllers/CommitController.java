package edu.uclm.esi.devopsmetrics.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;

import edu.uclm.esi.devopsmetrics.services.CommitService;
import edu.uclm.esi.devopsmetrics.services.UserService;
import edu.uclm.esi.devopsmetrics.services.BranchService;
import edu.uclm.esi.devopsmetrics.models.Commit;
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
	  private final UserService usersService;
	  private final BranchService branchService;
	  private final CommitsGithub cg;

	  @Autowired
	  /**
	   * @author FcoCrespo
	   */

	  public CommitController(final CommitService commitsService,
			  				  final UserService usersService, final BranchService branchService, final CommitsGithub cg) {
	    this.commitsService = commitsService;
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
	  @GetMapping(value = "/allbranches")
	  @ApiOperation(value = "Find all branches", notes = "Return all branches")

	  public ResponseEntity<List<Branch>> allBranches(@RequestParam("tokenpass") final String tokenpass,
		      @RequestParam("reponame") final String reponame,
		      @RequestParam("owner") final String owner) {

	    final User usuario = usersService.getUserByTokenPass(tokenpass);
	    if (usuario != null) {
	      LOG.info("Get branches");
	      cg.getBranches(reponame, owner);
	      return ResponseEntity.ok(branchService.getBranchesByRepository(reponame, true));
	    } else {
	      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
	      return ResponseEntity.badRequest().build();
	    }
		
	  }
	  
	  /**
	   * Devuelve el primer commit de cada rama para poner el orden correcto de creacion de cada rama
	   * 
	   * @author FcoCrespo
	 * @throws IOException 
	 * @throws ClientProtocolException 
	   * @throws InvalidRemoteException, TransportException, IOException, GitAPIException, InterruptedException
	   */
	  @GetMapping(value = "/branchesfirstcommit")
	  @ApiOperation(value = "Find all branches", notes = "Return all branches")

	  public ResponseEntity<String> allBranchesFirstCommit(@RequestParam("tokenpass") final String tokenpass,
		      @RequestParam("reponame") final String reponame,
		      @RequestParam("owner") final String owner) throws IOException {

	    final User usuario = usersService.getUserByTokenPass(tokenpass);
	    if (usuario != null) {
	      LOG.info("Get branches");
	      cg.getFirstCommitByBranch(reponame);
	      return ResponseEntity.ok("ok");
	    } else {
	      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
	      return ResponseEntity.badRequest().build();
	    }
		
	  }
	  
	  /**
	   * ATENCIÓN: Devuelve todos los commits del repositorio por su owner, nombre de repositorio y token de acceso
	   * 
	   * @author FcoCrespo
	 * @throws InterruptedException 
	 * @throws ParseException 
	 * @throws GitAPIException 
	 * @throws TransportException 
	 * @throws InvalidRemoteException 
	   */
	  
	  @GetMapping(value = "/allcommits")
	  @ApiOperation(value = "Find all branches", notes = "Return all branches")

	  public String allCommits(@RequestParam("tokenpass") final String tokenpass,
		      @RequestParam("reponame") final String reponame,
		      @RequestParam("owner") final String owner) throws InterruptedException, InvalidRemoteException, TransportException, GitAPIException, ParseException{
		

	    final User usuario = usersService.getUserByTokenPass(tokenpass);
	    if (usuario != null) {
	      LOG.info("Get commits");
	      try {
	    	  cg.getCommits(reponame, owner); 
		  } catch (IOException e) {
				e.printStackTrace();
		  } 
	      
	      return "bien";
	      
	    } else {
	      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
	      return "mal";
	    }
		
	  }
	  
	  
	  
	  /**
	   * Devuelve los commits de una rama de un repositorio por su owner, nombre de repositorio y token de acceso
	   * 
	   * @author FcoCrespo
	   */
	  
	  @GetMapping(value = "/commitsbranch")
	  @ApiOperation(value = "Find all commits from a repository branch", notes = "Return all commits from a repository branch")
	  
	  public ResponseEntity<List<Commit>> allCommitsBranch(@RequestParam("tokenpass") final String tokenpass,
		      @RequestParam("reponame") final String reponame,
		      @RequestParam("branch") final String branch,
		      @RequestParam("owner") final String owner){

	    final User usuario = usersService.getUserByTokenPass(tokenpass);
	    if (usuario != null) {
	      LOG.info("Get commits");
	      List <Commit> commits = commitsService.getAllByBranch(reponame, branch);
	 
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
	  
	  @GetMapping(value = "/commitsbranchauthor")
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
	  
	  @GetMapping(value = "/commitsbranchdate")
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
	  
	  @GetMapping(value = "/commitsbranchauthordate")
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
	  

	  /**
	   * Elimina los commits de una rama de un repositorio de la BBDD
	   * 
	   * @author FcoCrespo
	   */
	  
	  @DeleteMapping(value = "/deletebranchcommits")
	  @ApiOperation(value = "Delete commit from a repository branch", notes = "Delete commit from a repository branch")

	  public ResponseEntity<String> deleteUser(@RequestParam("tokenpass") final String tokenpass, 
			  @RequestParam("reponame") final String reponame, @RequestParam("owner") final String owner) {

	    final User usuariologin = usersService.getUserByTokenPass(tokenpass);
	    if (usuariologin != null) {
	      LOG.info("Delete commits ");
	      commitsService.deleteCommit(reponame);
	      return ResponseEntity.ok("ok");
	    } else {
	      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
	      return ResponseEntity.badRequest().build();
	    }

	  }

}
