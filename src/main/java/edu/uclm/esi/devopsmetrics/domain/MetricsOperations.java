package edu.uclm.esi.devopsmetrics.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import edu.uclm.esi.devopsmetrics.models.ClassMetrics;
import edu.uclm.esi.devopsmetrics.models.CohesionMetrics;
import edu.uclm.esi.devopsmetrics.models.Commit;
import edu.uclm.esi.devopsmetrics.models.CommitInfo;
import edu.uclm.esi.devopsmetrics.models.CouplingMetrics;
import edu.uclm.esi.devopsmetrics.models.MethodMetrics;
import edu.uclm.esi.devopsmetrics.models.UserGithub;
import edu.uclm.esi.devopsmetrics.services.ClassMetricsService;
import edu.uclm.esi.devopsmetrics.services.CohesionMetricsService;
import edu.uclm.esi.devopsmetrics.services.CouplingMetricsService;
import edu.uclm.esi.devopsmetrics.services.MethodMetricsService;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

@Service
@Scope("singleton")
public class MetricsOperations {
	
	private static final Log LOG = LogFactory.getLog(MetricsOperations.class);
	
	private final MethodMetricsService methodMetricsService;
	private final ClassMetricsService classMetricsService;
	private final CohesionMetricsService cohesionMetricsService;
	private final CouplingMetricsService couplingMetricsService;
	private final CommitServices commitServices;
	
	@Value("${app.metricsdir}")
	private String metricsdir;

	/**
	 * @author FcoCrespo
	 */
	public MetricsOperations(final MethodMetricsService methodMetricsService, final ClassMetricsService classMetricsService,
			                 final CohesionMetricsService cohesionMetricsService, final CouplingMetricsService couplingMetricsService,
			                 final CommitServices commitServices) {
		
		this.methodMetricsService = methodMetricsService;
		this.classMetricsService =  classMetricsService;
		this.cohesionMetricsService = cohesionMetricsService;
		this.couplingMetricsService = couplingMetricsService;
		this.commitServices = commitServices;
		
	}
	
	public String getRepoMetrics(String repository, String owner, String tokenpass) throws IOException {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://devopsmetrics.herokuapp.com/commits/allbranches?owner="+owner+"&reponame=" + repository+"&tokenpass="+tokenpass);
		
		try {
			
			LOG.info("Request Type: " + httpget.getMethod());

			HttpResponse httpresponse = httpclient.execute(httpget);

			HttpEntity entity = httpresponse.getEntity();
			String jsonData = EntityUtils.toString(entity, "UTF-8");
			JsonNode jsonNode = new ObjectMapper().readTree(jsonData);

			Iterator<JsonNode> iter;
			iter = jsonNode.iterator();

			JsonNode parameterNode;
			parameterNode = iter.next();
			
			List <Commit> listaCommits =  new ArrayList<>();
			
			if(!iter.hasNext()) {
				listaCommits = this.commitServices.getCommitService().getAllCommitsByBranch(parameterNode.get("idGithub").textValue());
			}
			else {
				while (iter.hasNext()) {
					
					listaCommits = obtenerCommits(parameterNode, listaCommits);
					parameterNode = iter.next();
					if (!iter.hasNext()) {
						
						listaCommits = obtenerCommits(parameterNode, listaCommits);
						parameterNode = iter.next();

					}
				}
			}
			
			Collections.sort(listaCommits);
			
			List <MethodMetrics> listMethodMetrics = this.methodMetricsService.findAll();
			List <ClassMetrics> listClassMetrics = this.classMetricsService.findAll();
			List <CohesionMetrics> listCohesionMetrics = this.cohesionMetricsService.findAll();
			List <CouplingMetrics> listCouplingMetrics = this.couplingMetricsService.findAll();
			
			listMethodMetrics = obtenerCommitsIgualesMethodMetrics(listaCommits, listMethodMetrics);
			listClassMetrics = obtenerCommitsIgualesClassMetrics(listaCommits, listClassMetrics);
			listCohesionMetrics = obtenerCommitsIgualesCohesionMetrics(listaCommits, listCohesionMetrics);
			listCouplingMetrics = obtenerCommitsIgualesCouplingMetrics(listaCommits, listCouplingMetrics);			
			
			JSONArray array = new JSONArray();
			JSONObject json;
			
			listaCommits = this.commitServices.getCommitService().findAll();
			Map<String, Commit> mapCommits = getMapCommits(listaCommits);
			List <CommitInfo> commitsInfo =  this.commitServices.getCommitInfoService().findAll();
			Map<String, CommitInfo> mapCommitsInfo = getMapCommitsInfo(commitsInfo);
			List <UserGithub> usersGithub =  this.commitServices.getUserGithubService().findAll();
			Map<String, UserGithub> mapUserGithub = getMapUserGithub(usersGithub);
			Commit commit;
			CommitInfo commitinfo;
			UserGithub userGithub;
			for(int i=0; i<listMethodMetrics.size(); i++) {
				
				commit=mapCommits.get(listMethodMetrics.get(i).getId());
				commitinfo=mapCommitsInfo.get(listMethodMetrics.get(i).getId());
				userGithub=mapUserGithub.get(commit.getUsergithub());
				
				json = new JSONObject();
				json.put("oid", listMethodMetrics.get(i).getId());
				json.put("pushedDate", commit.getPushedDate());
				json.put("messageHeadline", commitinfo.getMessageHeadline());
				json.put("message", commitinfo.getMessage());
				json.put("changedFiles", commitinfo.getChangedFiles());
				json.put("user","Login: "+userGithub.getLogin()+" , Name: "+userGithub.getName()+" , Email: "+userGithub.getEmail());
				json.put("VG", listMethodMetrics.get(i).getVg());
				json.put("MLOC", listMethodMetrics.get(i).getMloc());
				json.put("PAR", listMethodMetrics.get(i).getPar());
				json.put("NBD", listMethodMetrics.get(i).getNbd());
				json.put("NOC", listClassMetrics.get(i).getNoc());
				json.put("NOI", listClassMetrics.get(i).getNoi());
				json.put("TLOC", listClassMetrics.get(i).getTloc());
				json.put("NOM", listClassMetrics.get(i).getNom());
				json.put("DIT", listCohesionMetrics.get(i).getDit());
				json.put("WMC", listCohesionMetrics.get(i).getWmc());
				json.put("NSC", listCohesionMetrics.get(i).getNsc());
				json.put("LCOM", listCohesionMetrics.get(i).getLcom());
				json.put("CA", listCouplingMetrics.get(i).getCa());
				json.put("CE", listCouplingMetrics.get(i).getCe());
				json.put("RMI", listCouplingMetrics.get(i).getRmi());
				json.put("RMA", listCouplingMetrics.get(i).getRma());
				
				array.put(json);
				
			}
			listaCommits.clear();
			commitsInfo.clear();
			usersGithub.clear();
			listMethodMetrics.clear();
			listClassMetrics.clear();
			listCohesionMetrics.clear();
			listCouplingMetrics.clear();
			
			
			httpclient.close();
			
			return array.toString();
		}
		catch(Exception e) {
			httpclient.close();
			return "Error al obtener las metricas de los commits";
		}
		finally {
			httpclient.close();
		}
		
	}


	private Map<String, UserGithub> getMapUserGithub(List<UserGithub> usersGithub) {
		Map<String, UserGithub> mapUserGithub = new HashMap<String, UserGithub>();
		for (UserGithub i : usersGithub)
			mapUserGithub.put(i.getId(), i);
		return mapUserGithub;
	}

	private Map<String, Commit> getMapCommits(List<Commit> listaCommits) {
		Map<String, Commit> mapCommit = new HashMap<String, Commit>();
		for (Commit i : listaCommits)
			mapCommit.put(i.getOid(), i);
		return mapCommit;
	}

	private Map<String, CommitInfo> getMapCommitsInfo(List<CommitInfo> commitsInfo) {
		Map<String, CommitInfo> mapCommitInfo = new HashMap<String, CommitInfo>();
		for (CommitInfo i : commitsInfo)
			mapCommitInfo.put(i.getIdCommit(), i);
		return mapCommitInfo;
	}

	private List<CouplingMetrics> obtenerCommitsIgualesCouplingMetrics(List<Commit> listaCommits,
			List<CouplingMetrics> listCouplingMetrics) {
		
		List <CouplingMetrics> listCouplingMetricsNueva = new ArrayList<>();
		
		for(int i=0; i<listaCommits.size(); i++) {
			for(int j=0; j<listCouplingMetrics.size(); j++) {
				if(listaCommits.get(i).getOid().equals(listCouplingMetrics.get(j).getId())) {
					listCouplingMetricsNueva.add(listCouplingMetrics.get(j));
				}
			}
		}
		
		return listCouplingMetricsNueva;
	}

	private List<CohesionMetrics> obtenerCommitsIgualesCohesionMetrics(List<Commit> listaCommits,
			List<CohesionMetrics> listCohesionMetrics) {
		
		List <CohesionMetrics> listCohesionMetricsNueva = new ArrayList<>();
		
		for(int i=0; i<listaCommits.size(); i++) {
			for(int j=0; j<listCohesionMetrics.size(); j++) {
				if(listaCommits.get(i).getOid().equals(listCohesionMetrics.get(j).getId())) {
					listCohesionMetricsNueva.add(listCohesionMetrics.get(j));
				}
			}
		}
		
		return listCohesionMetricsNueva;
	}

	private List<ClassMetrics> obtenerCommitsIgualesClassMetrics(List<Commit> listaCommits,
			List<ClassMetrics> listClassMetrics) {
		
		List <ClassMetrics> listClassMetricsNueva = new ArrayList<>();
		for(int i=0; i<listaCommits.size(); i++) {
			for(int j=0; j<listClassMetrics.size(); j++) {
				if(listaCommits.get(i).getOid().equals(listClassMetrics.get(j).getId())) {
					listClassMetricsNueva.add(listClassMetrics.get(j));
				}
			}
		}
		
		return listClassMetricsNueva;
	}

	private List<MethodMetrics> obtenerCommitsIgualesMethodMetrics(List<Commit> listaCommits,
			List<MethodMetrics> listMethodMetrics) {

		List <MethodMetrics> listMethodMetricsNueva = new ArrayList<>();
		for(int i=0; i<listaCommits.size(); i++) {
			for(int j=0; j<listMethodMetrics.size(); j++) {
				if(listaCommits.get(i).getOid().equals(listMethodMetrics.get(j).getId())) {
					listMethodMetricsNueva.add(listMethodMetrics.get(j));
				}
			}
		}
		
		return listMethodMetricsNueva;
	}

	private List<Commit> obtenerCommits(JsonNode parameterNode, List <Commit> listaCommits) {
		
		List <Commit> listaOriginal =  listaCommits;
		List <Commit> listaNueva = this.commitServices.getCommitService().getAllCommitsByBranch(parameterNode.get("idGithub").textValue());

		for(int i=0; i<listaNueva.size(); i++) {
			listaOriginal.add(listaNueva.get(i));
		}
		
		return listaOriginal;
		
	}

	public void saveRepoMetrics(String repository, String owner) {
		
		LOG.info(this.metricsdir);
		
		File folder = new File(this.metricsdir.replace("%20", " "));
				
		List <String> filenames = new ArrayList<String>();
		
		String extension = "xml";
		String str;
		
		filenames = listFilesForFolder(folder,filenames,extension);
		
		for(int i=0; i<filenames.size(); i++) {
			LOG.info(filenames.get(i));

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

		      try {

		          dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

		          DocumentBuilder db = dbf.newDocumentBuilder();

		          Document doc = db.parse(new File(filenames.get(i)));

		          doc.getDocumentElement().normalize();
		          
		          HashMap<String, String> data = new HashMap<String, String>();

		          if (doc.hasChildNodes()) {
		              printNote(doc.getChildNodes(), data, repository, owner);
		              LOG.info(data.toString());
		          }		      
		          
		          
		          data.put("VG", data.get("VG").replace(",", "."));
		          data.put("MLOC", data.get("MLOC").replace(",", "."));
		          data.put("PAR", data.get("PAR").replace(",", "."));
		          data.put("NBD", data.get("NBD").replace(",", "."));
		          data.put("NOC", data.get("NOC").replace(",", "."));
		          data.put("NOI", data.get("NOI").replace(",", "."));
		          data.put("TLOC", data.get("TLOC").replace(",", "."));
		          data.put("NOM", data.get("NOM").replace(",", "."));
		          data.put("DIT", data.get("DIT").replace(",", "."));
		          data.put("WMC", data.get("WMC").replace(",", "."));
		          data.put("NSC", data.get("NSC").replace(",", "."));
		          data.put("LCOM", data.get("LCOM").replace(",", "."));
		          data.put("CA", data.get("CA").replace(",", "."));
		          data.put("CE", data.get("CE").replace(",", "."));
		          data.put("RMI", data.get("RMI").replace(",", "."));
		          data.put("RMA", data.get("RMA").replace(",", "."));
		          
		          saveMethodMetrics(data);
		          saveClassMetrics(data);
		          saveCohesionMetrics(data);
		          saveCouplingMetrics(data);
		          
		          File file  = new File(filenames.get(i));
		          str = file.getPath().replace(".xml", ".xmldone");
		          if(file.renameTo(new File(str))) {
		        	  LOG.info("Archivo procesado: "+filenames.get(i));
		          }

		      } catch (ParserConfigurationException | SAXException | IOException e) {
		          e.printStackTrace();
		      }
		}
		
	}

	private void saveCouplingMetrics(HashMap<String, String> data) {
		CouplingMetrics couplingMetrics = new CouplingMetrics(Double.parseDouble(data.get("CA")),
				Double.parseDouble(data.get("CE")),Double.parseDouble(data.get("RMI")),Double.parseDouble(data.get("RMA")));
		couplingMetrics.setId(data.get("oid"));
		
		List <CouplingMetrics> couplingMetricsList = this.couplingMetricsService.findAll();
		
		boolean existe=false;
		
		for(int i=0; i<couplingMetricsList.size(); i++ ) {
			if(couplingMetricsList.get(i).getId().equals(couplingMetrics.getId())){
				existe=true;
			}
		}
		if(!existe) {
			this.couplingMetricsService.saveCouplingMetrics(couplingMetrics);
		}
		
		couplingMetricsList.clear();
	}

	private void saveCohesionMetrics(HashMap<String, String> data) {
		
		CohesionMetrics cohesionMetrics = new CohesionMetrics(Double.parseDouble(data.get("DIT")),
				Double.parseDouble(data.get("WMC")),Double.parseDouble(data.get("NSC")),Double.parseDouble(data.get("LCOM")));
		cohesionMetrics.setId(data.get("oid"));
		
		List <CohesionMetrics> cohesionMetricsList = this.cohesionMetricsService.findAll();
		
		boolean existe=false;
		
		for(int i=0; i<cohesionMetricsList.size(); i++ ) {
			if(cohesionMetricsList.get(i).getId().equals(cohesionMetrics.getId())){
				existe=true;
			}
		}
		if(!existe) {
			this.cohesionMetricsService.saveCohesionMetrics(cohesionMetrics);
		}
		
		cohesionMetricsList.clear();
		
	}

	private void saveClassMetrics(HashMap<String, String> data) {
		
		ClassMetrics classMetrics = new ClassMetrics(Double.parseDouble(data.get("NOC")),
				Double.parseDouble(data.get("NOI")),Double.parseDouble(data.get("TLOC")),Double.parseDouble(data.get("NOM")));
		classMetrics.setId(data.get("oid"));
		
		List <ClassMetrics> classMetricsList = this.classMetricsService.findAll();
		
		boolean existe=false;
		
		for(int i=0; i<classMetricsList.size(); i++ ) {
			if(classMetricsList.get(i).getId().equals(classMetrics.getId())){
				existe=true;
			}
		}
		if(!existe) {
			this.classMetricsService.saveClassMetrics(classMetrics);
		}
		
		classMetricsList.clear();
	}

	private void saveMethodMetrics(HashMap<String, String> data) {
		
		MethodMetrics methodMetrics = new MethodMetrics(Double.parseDouble(data.get("VG")),
				Double.parseDouble(data.get("MLOC")),Double.parseDouble(data.get("PAR")),Double.parseDouble(data.get("NBD")));
		methodMetrics.setId(data.get("oid"));
		
		boolean existe;
		
		List <MethodMetrics> methodMetricsList = this.methodMetricsService.findAll();
		
		existe=false;
		
		for(int i=0; i<methodMetricsList.size(); i++ ) {
			if(methodMetricsList.get(i).getId().equals(methodMetrics.getId())){
				existe=true;
			}
		}
		if(!existe) {
			this.methodMetricsService.saveMethodMetrics(methodMetrics);
		}
		
		methodMetricsList.clear();
		
	}

	private static void printNote(NodeList nodeList, HashMap<String, String> data, String repository, String owner) {

	      for (int count = 0; count < nodeList.getLength(); count++) {
	    	  
	          Node tempNode = nodeList.item(count);

	          if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
	         	 	              
	              if(tempNode.getNodeName().equals("commit")) {
	            	  obtenerDatosCommit(tempNode, data, repository, owner);
	              }

	              if(tempNode.getNodeName().equals("Metric") && !data.get("oid").isEmpty()) {
	            	  checkAttributtes(tempNode, data);
	              }
	             
	              if (tempNode.hasChildNodes()) {
	                  printNote(tempNode.getChildNodes(), data, repository, owner);
	              }

	          }

	      }
	      
	  }
	
	private static void checkAttributtes(Node tempNode, HashMap<String, String> data) {
		if (tempNode.hasAttributes()) {
			
            String id = tempNode.getAttributes().getNamedItem("id").getTextContent();
            NodeList nodeList = tempNode.getChildNodes();
            String avg = "";
            
            if(!id.equals("TLOC") && !id.equals("NOP")) {

                 NamedNodeMap nodeMap = nodeList.item(1).getAttributes();
                 avg=nodeMap.getNamedItem("avg").getNodeValue();
                 	
                 data.put(id, avg);
            }
            else {
            	NamedNodeMap nodeMap = nodeList.item(1).getAttributes();
                avg=nodeMap.getNamedItem("value").getNodeValue();
               
                data.put(id, avg);
            }
           
		}
		
	}

	private static void obtenerDatosCommit(Node tempNode, HashMap<String, String> data, String repository, String owner) {
		
		NodeList nodeList = tempNode.getChildNodes();
		String repoNow="";
		String ownerNow="";
		String oid="";
		
		for(int i = 0; i<nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				if(nodeList.item(i).getNodeName().equals("oid")) {
					oid = nodeList.item(i).getTextContent();
				}
				else if(nodeList.item(i).getNodeName().equals("repository")) {
					repoNow = nodeList.item(i).getTextContent();			
				}
				else if(nodeList.item(i).getNodeName().equals("owner")) {
					ownerNow = nodeList.item(i).getTextContent();
				}
			}
		}
		
		if(repoNow.equals(repository) && ownerNow.equals(owner)) {
			data.put("oid", oid);
		}
	}

	public List <String> listFilesForFolder(final File folder, List <String> filenames, String extension) {
	    
		List <String> updatedFiles = filenames;
		String ext;
				
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry, filenames, extension);
	        } else {
	        	ext = FilenameUtils.getExtension(fileEntry.getName());
	        	if(ext.equals(extension)) {
	        		filenames.add(fileEntry.getPath());
	        	}
	        }
	    }
		
		return updatedFiles;
		
	}

	
}
