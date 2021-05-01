package edu.uclm.esi.devopsmetrics.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import edu.uclm.esi.devopsmetrics.models.ClassMetrics;
import edu.uclm.esi.devopsmetrics.models.CohesionMetrics;
import edu.uclm.esi.devopsmetrics.models.CouplingMetrics;
import edu.uclm.esi.devopsmetrics.models.MethodMetrics;
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
	
	@Value("${app.metricsdir}")
	private String metricsdir;

	/**
	 * @author FcoCrespo
	 */
	public MetricsOperations(final MethodMetricsService methodMetricsService, final ClassMetricsService classMetricsService,
			                 final CohesionMetricsService cohesionMetricsService, final CouplingMetricsService couplingMetricsService) {
		
		this.methodMetricsService = methodMetricsService;
		this.classMetricsService =  classMetricsService;
		this.cohesionMetricsService = cohesionMetricsService;
		this.couplingMetricsService = couplingMetricsService;
		
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
		          data.put("MLOC", data.get("VG").replace(",", "."));
		          data.put("PAR", data.get("VG").replace(",", "."));
		          data.put("NBD", data.get("VG").replace(",", "."));
		          data.put("NOC", data.get("VG").replace(",", "."));
		          data.put("NOI", data.get("VG").replace(",", "."));
		          data.put("TLOC", data.get("VG").replace(",", "."));
		          data.put("NOM", data.get("VG").replace(",", "."));
		          data.put("DIT", data.get("VG").replace(",", "."));
		          data.put("WMC", data.get("VG").replace(",", "."));
		          data.put("NSC", data.get("VG").replace(",", "."));
		          data.put("LCOM", data.get("VG").replace(",", "."));
		          data.put("CA", data.get("VG").replace(",", "."));
		          data.put("CE", data.get("VG").replace(",", "."));
		          data.put("RMI", data.get("VG").replace(",", "."));
		          data.put("RMA", data.get("VG").replace(",", "."));
		          
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
