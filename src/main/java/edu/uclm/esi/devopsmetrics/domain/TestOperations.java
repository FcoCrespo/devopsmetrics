package edu.uclm.esi.devopsmetrics.domain;

import java.io.BufferedReader;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uclm.esi.devopsmetrics.models.MethodTest;
import edu.uclm.esi.devopsmetrics.models.TestMetrics;
import edu.uclm.esi.devopsmetrics.services.MethodTestService;
import edu.uclm.esi.devopsmetrics.services.TestMetricsService;

@Service
@Scope("singleton")
public class TestOperations {

	private static final Log LOG = LogFactory.getLog(TestOperations.class);

	private final TestMetricsService testMetricsService;
	private final MethodTestService methodTestService;

	private final String elementStr;
	private final String checkFail;

	@Value("${app.userftp}")
	private String user;
	
	@Value("${app.passftp}")
	private String pass;
	
	@Value("${app.serverftp}")
	private String serverftp;

	public TestOperations(final TestMetricsService testMetricsService, final MethodTestService methodTestService) {

		this.testMetricsService = testMetricsService;
		this.methodTestService = methodTestService;
		this.elementStr = "elements";
		this.checkFail = ",\"status\":\"failed\"}";
	}

	public String getRepoTestMetrics(String repository, String owner) {

		List <TestMetrics> listaTestMetrics =  this.testMetricsService.getAllByRepositoryAndOwner(repository, owner);
	
		List <MethodTest> listaMethodTestAux;
		
		JSONArray array = new JSONArray();
		JSONObject json;
		
		for(int i=0; i<listaTestMetrics.size(); i++) {
			listaMethodTestAux = this.methodTestService.getAllByTestId(listaTestMetrics.get(i).getId());
			
			for(int j=0; j<listaMethodTestAux.size(); j++) {
				json = new JSONObject();
				
				json.put("idTest", listaTestMetrics.get(i).getId());
				json.put("repository", listaTestMetrics.get(i).getRepository());
				json.put("owner", listaTestMetrics.get(i).getOwner());
				json.put("date", listaTestMetrics.get(i).getDateTest());
				
				json.put("idMethodTest", listaMethodTestAux.get(j).getId());
				json.put("feature", listaMethodTestAux.get(j).getFeature());
				json.put("passed", listaMethodTestAux.get(j).isPassed());
				
				array.put(json);
			}
			listaMethodTestAux.clear();
		}
		
		listaTestMetrics.clear();
		
		return array.toString();
	}

	public void saveRepoTestMetrics(String repository, String owner) throws IOException {

		TestMetrics testMetrics;

		String repoTest;
		String ownerTest;
		String dateTest;
		Instant fecha;
		
		String server = serverftp;
		int port = 21;
		

		FTPClient ftpClient = new FTPClient();
		
		String[] variables;

		JsonNode nodes;
		Iterator<JsonNode> iter;
		JsonNode parameterNode;

		String[] elemFecha = new String[5];
		
		try {

			ftpClient.connect(server, port);
			
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				LOG.info("Operation failed. Server reply code: " + replyCode);
			}
			boolean success = ftpClient.login(user, pass);
			
			if (!success) {
				LOG.info("Could not login to the server");
			}
			
			ftpClient.enterLocalPassiveMode();
			
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
			ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			
			List<String> filesList =  new ArrayList<>();
			
			listDirectory(ftpClient, filesList, "/", "", 0);
			
			
			for (int i = 0; i < filesList.size(); i++) {
				
				variables = filesList.get(i).split("-");

				repoTest = variables[1];
				ownerTest = variables[2];
				
				if(repoTest.equals(repository) && ownerTest.equals(owner)) {
					
					elemFecha[0] = variables[3];
					elemFecha[1] = variables[4];
					elemFecha[2] = variables[5];
					elemFecha[3] = variables[6];
					elemFecha[4] = variables[7];

					dateTest = getDateCorrect(elemFecha);
					fecha = getDatesInstant(dateTest);

					LOG.info("repoTest: " + repoTest + ", ownerTest: " + ownerTest + ", dateToCorrect: "
							+ fecha.toString());

				
					String jsonData = "";
					
					String [] folder = filesList.get(i).split(".json");
					
					String fileRoute = folder[0]+"\\"+filesList.get(i);
					
					jsonData = obtenerJSON(ftpClient, fileRoute);
						
					nodes = new ObjectMapper().readTree(jsonData);

					iter = nodes.iterator();
					parameterNode = iter.next();

					testMetrics = new TestMetrics(repoTest, ownerTest, fecha);

					this.testMetricsService.saveTestMetrics(testMetrics);
					
					LOG.info(testMetrics.toString());

					comprobarContenido(iter, parameterNode, testMetrics);
					
					
					while(!ftpClient.completePendingCommand());
					LOG.info("-------[END:" + filesList.get(i)
		                    + "]---------------------");
					
		            String newName = folder[0]+"\\"+folder[0]+".jsondone";
		            
		            success = ftpClient.rename(fileRoute, newName);
		           
				}

				

			}

			
			
		} catch (IOException ex) {
			LOG.info("Error: " + ex.getMessage());
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				LOG.info("Error: " + ex.getMessage());
			}
		}
		
		
	}
	
	private String obtenerJSON(FTPClient ftpClient, String fileRoute) throws IOException {
		
		InputStream stream = ftpClient.retrieveFileStream(fileRoute);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
		
		stream.close();
		
		return reader.lines().collect(Collectors.joining());
	}

	static void listDirectory(FTPClient ftpClient, List<String>filesList, String parentDir,
	        String currentDir, int level) throws IOException {
	    String dirToList = parentDir;
	    if (!currentDir.equals("")) {
	        dirToList += "/" + currentDir;
	    }
	    FTPFile[] subFiles = ftpClient.listFiles(dirToList);
	    if (subFiles != null && subFiles.length > 0) {
	        for (FTPFile aFile : subFiles) {
	            String currentFileName = aFile.getName();
	            if (currentFileName.equals(".")
	                    || currentFileName.equals("..")) {
	                // skip parent directory and directory itself
	                continue;
	            }
	            if (aFile.isDirectory()) {
	                listDirectory(ftpClient, filesList, dirToList, currentFileName, level + 1);
	            } 
            	String ext = FilenameUtils.getExtension(aFile.getName());
            	if (ext.equals("json")) {
            		filesList.add(currentFileName);
				}
	                
	            
	        }
	    }
	}

	private void comprobarContenido(Iterator<JsonNode> iter, JsonNode parameterNode, TestMetrics testMetrics) {

		String nodeElements;

		if (iter.hasNext()) {
			while (iter.hasNext()) {

				nodeElements = parameterNode.get(this.elementStr).toString();
				saveMethodMetric(testMetrics, nodeElements, parameterNode);

				parameterNode = iter.next();

			}

			if (!iter.hasNext()) {

				nodeElements = parameterNode.get(this.elementStr).toString();
				saveMethodMetric(testMetrics, nodeElements, parameterNode);

			}
		} else {

			nodeElements = parameterNode.get(this.elementStr).toString();
			saveMethodMetric(testMetrics, nodeElements, parameterNode);

		}

	}

	private void saveMethodMetric(TestMetrics testMetrics, String nodeElements, JsonNode parameterNode) {

		MethodTest methodTest;

		if (nodeElements.contains(this.checkFail)) {

			methodTest = new MethodTest(testMetrics.getId(), parameterNode.get("name").textValue(), false);
			this.methodTestService.saveMethodTest(methodTest);
		} else {

			methodTest = new MethodTest(testMetrics.getId(), parameterNode.get("name").textValue(), true);
			this.methodTestService.saveMethodTest(methodTest);

		}
	}

	private String getDateCorrect(String[] dateToCorrect) {

		String year = dateToCorrect[2];
		String month = dateToCorrect[1];
		String day = dateToCorrect[0];

		String hour = dateToCorrect[3];

		String minute = dateToCorrect[4].substring(0, 2);

		return day + "/" + month + "/" + year + " " + hour + ":" + minute;

	}


	private Instant getDatesInstant(String dateFinal) {

		Instant instant;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm", Locale.US);
		ZoneId z = ZoneId.of("America/Toronto");

		LocalDateTime ldtfecha = LocalDateTime.parse(dateFinal, formatter);
		ZonedDateTime zdtfecha = ldtfecha.atZone(z);

		instant = zdtfecha.toInstant().minus(4, ChronoUnit.HOURS);

		return instant;

	}

}
