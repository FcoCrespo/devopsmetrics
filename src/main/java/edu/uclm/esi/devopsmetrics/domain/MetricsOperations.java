package edu.uclm.esi.devopsmetrics.domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

@Service
public class MetricsOperations {

	private static final Log LOG = LogFactory.getLog(MetricsOperations.class);

	private final MethodMetricsService methodMetricsService;
	private final ClassMetricsService classMetricsService;
	private final CohesionMetricsService cohesionMetricsService;
	private final CouplingMetricsService couplingMetricsService;
	private final CommitServices commitServices;
	private final String idGithubStr;

	@Value("${app.userftp}")
	private String user;

	@Value("${app.passftp}")
	private String pass;

	@Value("${app.serverftp}")
	private String serverftp;

	/**
	 * @author FcoCrespo
	 */
	public MetricsOperations(final MethodMetricsService methodMetricsService,
			final ClassMetricsService classMetricsService, final CohesionMetricsService cohesionMetricsService,
			final CouplingMetricsService couplingMetricsService, final CommitServices commitServices) {

		this.methodMetricsService = methodMetricsService;
		this.classMetricsService = classMetricsService;
		this.cohesionMetricsService = cohesionMetricsService;
		this.couplingMetricsService = couplingMetricsService;
		this.commitServices = commitServices;
		this.idGithubStr = "idGithub";

	}

	public String getRepoMetrics(String repository, String owner, String tokenpass) throws IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://devopsmetrics.herokuapp.com/commits/allbranches?owner=" + owner
				+ "&reponame=" + repository + "&tokenpass=" + tokenpass);
		try {

			LOG.info("Request Type: " + httpget.getMethod());

			HttpResponse httpresponse = httpclient.execute(httpget);

			HttpEntity entity = httpresponse.getEntity();
			String jsonData = EntityUtils.toString(entity, "UTF-8");
			JsonNode jsonNode = new ObjectMapper().readTree(jsonData);

			return obtenerRepoMetrics(jsonNode);

		} catch (Exception e) {
			httpclient.close();
			return "Error al obtener las metricas de los commits";
		} finally {
			httpclient.close();
		}

	}

	public String getRepoMetricsDate(String tokenpass, String message) throws IOException {

		final JSONObject jso = new JSONObject(message);
		String reponame = jso.getString("reponame");
		String owner = jso.getString("owner");
		String begindate = jso.getString("begindate");
		String enddate = jso.getString("enddate");

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://devopsmetrics.herokuapp.com/commits/allbranches?owner=" + owner
				+ "&reponame=" + reponame + "&tokenpass=" + tokenpass);
		try {

			LOG.info("Request Type: " + httpget.getMethod());

			HttpResponse httpresponse = httpclient.execute(httpget);

			HttpEntity entity = httpresponse.getEntity();
			String jsonData = EntityUtils.toString(entity, "UTF-8");
			JsonNode jsonNode = new ObjectMapper().readTree(jsonData);

			return obtenerRepoMetricsDate(jsonNode, begindate, enddate);

		} catch (Exception e) {
			httpclient.close();
			return "Error al obtener las metricas de los commits";
		} finally {
			httpclient.close();
		}
	}

	private String obtenerRepoMetricsDate(JsonNode jsonNode, String begindate, String enddate) {

		Instant[] dates = DateUtils.getDatesInstant(begindate, enddate);

		Instant beginDateInstant = dates[0];
		Instant endDateInstant = dates[1];

		Iterator<JsonNode> iter;
		iter = jsonNode.iterator();

		JsonNode parameterNode;
		parameterNode = iter.next();

		List<Commit> listaCommits = new ArrayList<>();

		if (!iter.hasNext()) {
			listaCommits = this.commitServices.getCommitService().getAllByBranchBeginEndDate(
					parameterNode.get(this.idGithubStr).textValue(), beginDateInstant, endDateInstant);
		} else {
			while (iter.hasNext()) {

				listaCommits = obtenerCommitsDate(parameterNode, listaCommits, beginDateInstant, endDateInstant);
				parameterNode = iter.next();

				if (!iter.hasNext()) {

					listaCommits = obtenerCommitsDate(parameterNode, listaCommits, beginDateInstant, endDateInstant);

				}
			}
		}
		return obtenerMetricasCommits(listaCommits);
	}

	private List<Commit> obtenerCommitsDate(JsonNode parameterNode, List<Commit> listaCommits, Instant beginDateInstant,
			Instant endDateInstant) {
		List<Commit> listaOriginal = listaCommits;
		List<Commit> listaNueva = this.commitServices.getCommitService().getAllByBranchBeginEndDate(
				parameterNode.get(this.idGithubStr).textValue(), beginDateInstant, endDateInstant);

		for (int i = 0; i < listaNueva.size(); i++) {
			listaOriginal.add(listaNueva.get(i));
		}

		return listaOriginal;
	}

	private List<Commit> obtenerCommits(JsonNode parameterNode, List<Commit> listaCommits) {

		List<Commit> listaOriginal = listaCommits;
		List<Commit> listaNueva = this.commitServices.getCommitService()
				.getAllCommitsByBranch(parameterNode.get(this.idGithubStr).textValue());

		for (int i = 0; i < listaNueva.size(); i++) {
			listaOriginal.add(listaNueva.get(i));
		}

		return listaOriginal;

	}

	private String obtenerRepoMetrics(JsonNode jsonNode) {
		Iterator<JsonNode> iter;
		iter = jsonNode.iterator();

		JsonNode parameterNode;
		parameterNode = iter.next();

		List<Commit> listaCommits = new ArrayList<>();

		if (!iter.hasNext()) {
			listaCommits = this.commitServices.getCommitService()
					.getAllCommitsByBranch(parameterNode.get(this.idGithubStr).textValue());
		} else {
			while (iter.hasNext()) {

				listaCommits = obtenerCommits(parameterNode, listaCommits);
				parameterNode = iter.next();

				if (!iter.hasNext()) {

					listaCommits = obtenerCommits(parameterNode, listaCommits);

				}
			}
		}

		return obtenerMetricasCommits(listaCommits);
	}

	private String obtenerMetricasCommits(List<Commit> listaCommits) {

		Collections.sort(listaCommits);

		Map<String, Commit> mapCommits = getMapCommits(listaCommits);

		List<MethodMetrics> listMethodMetrics = this.methodMetricsService.findAll();
		Map<String, MethodMetrics> mapMethodMetrics = getMapMethodMetrics(listMethodMetrics, mapCommits);
		List<ClassMetrics> listClassMetrics = this.classMetricsService.findAll();
		Map<String, ClassMetrics> mapClassMetrics = getMapClassMetrics(listClassMetrics, mapCommits);
		List<CohesionMetrics> listCohesionMetrics = this.cohesionMetricsService.findAll();
		Map<String, CohesionMetrics> mapCohesionMetrics = getMapCohesionMetrics(listCohesionMetrics, mapCommits);
		List<CouplingMetrics> listCouplingMetrics = this.couplingMetricsService.findAll();
		Map<String, CouplingMetrics> mapCouplingMetrics = getMapCouplingMetrics(listCouplingMetrics, mapCommits);

		List<CommitInfo> commitsInfo = this.commitServices.getCommitInfoService().findAll();
		Map<String, CommitInfo> mapCommitsInfo = getMapCommitsInfo(commitsInfo);
		List<UserGithub> usersGithub = this.commitServices.getUserGithubService().findAll();
		Map<String, UserGithub> mapUserGithub = getMapUserGithub(usersGithub);

		Commit commit;
		CommitInfo commitinfo;
		UserGithub userGithub;
		MethodMetrics methodMetrics;
		ClassMetrics classMetrics;
		CohesionMetrics cohesionMetrics;
		CouplingMetrics couplingMetrics;

		JSONArray array = new JSONArray();
		JSONObject json;

		Set<Entry<String, MethodMetrics>> entrySet = mapMethodMetrics.entrySet();

		for (Entry<String, MethodMetrics> entry : entrySet) {
			commit = mapCommits.get(entry.getKey());
			commitinfo = mapCommitsInfo.get(entry.getKey());
			userGithub = mapUserGithub.get(commit.getUsergithub());
			methodMetrics = mapMethodMetrics.get(entry.getKey());
			classMetrics = mapClassMetrics.get(entry.getKey());
			cohesionMetrics = mapCohesionMetrics.get(entry.getKey());
			couplingMetrics = mapCouplingMetrics.get(entry.getKey());

			json = new JSONObject();
			json.put("oid", methodMetrics.getId());
			json.put("pushedDate", commit.getPushedDate());
			json.put("messageHeadline", commitinfo.getMessageHeadline());
			json.put("message", commitinfo.getMessage());
			json.put("changedFiles", commitinfo.getChangedFiles());
			json.put("user", "Login: " + userGithub.getLogin() + " , Name: " + userGithub.getName() + " , Email: "
					+ userGithub.getEmail());
			json.put("VG", methodMetrics.getVg());
			json.put("MLOC", methodMetrics.getMloc());
			json.put("PAR", methodMetrics.getPar());
			json.put("NBD", methodMetrics.getNbd());
			json.put("NOC", classMetrics.getNoc());
			json.put("NOI", classMetrics.getNoi());
			json.put("TLOC", classMetrics.getTloc());
			json.put("NOM", classMetrics.getNom());
			json.put("DIT", cohesionMetrics.getDit());
			json.put("WMC", cohesionMetrics.getWmc());
			json.put("NSC", cohesionMetrics.getNsc());
			json.put("LCOM", cohesionMetrics.getLcom());
			json.put("CA", couplingMetrics.getCa());
			json.put("CE", couplingMetrics.getCe());
			json.put("RMI", couplingMetrics.getRmi());
			json.put("RMA", couplingMetrics.getRma());

			array.put(json);
		}

		return array.toString();
	}

	private Map<String, CouplingMetrics> getMapCouplingMetrics(List<CouplingMetrics> listCouplingMetrics,
			Map<String, Commit> mapCommits) {
		Map<String, CouplingMetrics> mapCouplingMetrics = new HashMap<String, CouplingMetrics>();
		Commit commit;
		for (CouplingMetrics i : listCouplingMetrics) {
			commit = mapCommits.get(i.getId());
			if (commit != null && commit.getOid().equals(i.getId())) {
				mapCouplingMetrics.put(i.getId(), i);
			}
		}
		return mapCouplingMetrics;
	}

	private Map<String, CohesionMetrics> getMapCohesionMetrics(List<CohesionMetrics> listCohesionMetrics,
			Map<String, Commit> mapCommits) {
		Map<String, CohesionMetrics> mapCohesionMetrics = new HashMap<String, CohesionMetrics>();
		Commit commit;
		for (CohesionMetrics i : listCohesionMetrics) {
			commit = mapCommits.get(i.getId());
			if (commit != null && commit.getOid().equals(i.getId())) {
				mapCohesionMetrics.put(i.getId(), i);
			}
		}
		return mapCohesionMetrics;
	}

	private Map<String, ClassMetrics> getMapClassMetrics(List<ClassMetrics> listClassMetrics,
			Map<String, Commit> mapCommits) {
		Map<String, ClassMetrics> mapClassMetrics = new HashMap<String, ClassMetrics>();
		Commit commit;
		for (ClassMetrics i : listClassMetrics) {
			commit = mapCommits.get(i.getId());
			if (commit != null && commit.getOid().equals(i.getId())) {
				mapClassMetrics.put(i.getId(), i);
			}
		}
		return mapClassMetrics;
	}

	private Map<String, MethodMetrics> getMapMethodMetrics(List<MethodMetrics> listMethodMetrics,
			Map<String, Commit> mapCommits) {

		Map<String, MethodMetrics> mapMethodMetrics = new HashMap<String, MethodMetrics>();
		Commit commit;
		for (MethodMetrics i : listMethodMetrics) {
			commit = mapCommits.get(i.getId());
			if (commit != null && commit.getOid().equals(i.getId())) {
				mapMethodMetrics.put(i.getId(), i);
			}
		}
		return mapMethodMetrics;

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

	public void saveRepoMetrics(String repository, String owner) throws ParserConfigurationException, SAXException {

		String server = serverftp;
		int port = 21;

		FTPClient ftpClient = new FTPClient();

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

			List<String> filesList = new ArrayList<>();

			listDirectory(ftpClient, filesList, "/commitsmetrics/", "", 0);

			for (int i = 0; i < filesList.size(); i++) {

				LOG.info(filesList.get(i));

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
				dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

				String fileRoute = "commitsmetrics\\" + filesList.get(i);

				Document doc = obtenerXML(ftpClient, fileRoute, dbf);

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

				while (!ftpClient.completePendingCommand())
					;
				LOG.info("-------[END:" + filesList.get(i) + "]---------------------");

				String[] filename = filesList.get(i).split(".xml");
				String newName = "commitsmetrics\\" + filename[0] + ".xmldone";

				success = ftpClient.rename(fileRoute, newName);

			}

		} catch (IOException e) {
			LOG.info("Error: " + e.getMessage());
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

	private Document obtenerXML(FTPClient ftpClient, String fileRoute, DocumentBuilderFactory dbf)
			throws IOException, ParserConfigurationException, SAXException {
		InputStream stream = ftpClient.retrieveFileStream(fileRoute);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

		String xmlData = reader.lines().collect(Collectors.joining());

		dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

		DocumentBuilder db = dbf.newDocumentBuilder();

		stream.close();

		return db.parse(new InputSource(new StringReader(xmlData)));
	}

	static void listDirectory(FTPClient ftpClient, List<String> filesList, String parentDir, String currentDir,
			int level) throws IOException {
		String dirToList = parentDir;
		if (!currentDir.equals("")) {
			dirToList += "/" + currentDir;
		}
		FTPFile[] subFiles = ftpClient.listFiles(dirToList);
		if (subFiles != null && subFiles.length > 0) {
			for (FTPFile aFile : subFiles) {
				String currentFileName = aFile.getName();
				if (currentFileName.equals(".") || currentFileName.equals("..")) {
					// skip parent directory and directory itself
					continue;
				}
				if (aFile.isDirectory()) {
					listDirectory(ftpClient, filesList, dirToList, currentFileName, level + 1);
				}
				String ext = FilenameUtils.getExtension(aFile.getName());
				if (ext.equals("xml")) {
					filesList.add(currentFileName);
				}

			}
		}
	}

	private void saveCouplingMetrics(HashMap<String, String> data) {
		CouplingMetrics couplingMetrics = new CouplingMetrics(Double.parseDouble(data.get("CA")),
				Double.parseDouble(data.get("CE")), Double.parseDouble(data.get("RMI")),
				Double.parseDouble(data.get("RMA")));
		couplingMetrics.setId(data.get("oid"));

		List<CouplingMetrics> couplingMetricsList = this.couplingMetricsService.findAll();

		boolean existe = false;

		for (int i = 0; i < couplingMetricsList.size(); i++) {
			if (couplingMetricsList.get(i).getId().equals(couplingMetrics.getId())) {
				existe = true;
			}
		}
		if (!existe) {
			this.couplingMetricsService.saveCouplingMetrics(couplingMetrics);
		}

		couplingMetricsList.clear();
	}

	private void saveCohesionMetrics(HashMap<String, String> data) {

		CohesionMetrics cohesionMetrics = new CohesionMetrics(Double.parseDouble(data.get("DIT")),
				Double.parseDouble(data.get("WMC")), Double.parseDouble(data.get("NSC")),
				Double.parseDouble(data.get("LCOM")));
		cohesionMetrics.setId(data.get("oid"));

		List<CohesionMetrics> cohesionMetricsList = this.cohesionMetricsService.findAll();

		boolean existe = false;

		for (int i = 0; i < cohesionMetricsList.size(); i++) {
			if (cohesionMetricsList.get(i).getId().equals(cohesionMetrics.getId())) {
				existe = true;
			}
		}
		if (!existe) {
			this.cohesionMetricsService.saveCohesionMetrics(cohesionMetrics);
		}

		cohesionMetricsList.clear();

	}

	private void saveClassMetrics(HashMap<String, String> data) {

		ClassMetrics classMetrics = new ClassMetrics(Double.parseDouble(data.get("NOC")),
				Double.parseDouble(data.get("NOI")), Double.parseDouble(data.get("TLOC")),
				Double.parseDouble(data.get("NOM")));
		classMetrics.setId(data.get("oid"));

		List<ClassMetrics> classMetricsList = this.classMetricsService.findAll();

		boolean existe = false;

		for (int i = 0; i < classMetricsList.size(); i++) {
			if (classMetricsList.get(i).getId().equals(classMetrics.getId())) {
				existe = true;
			}
		}
		if (!existe) {
			this.classMetricsService.saveClassMetrics(classMetrics);
		}

		classMetricsList.clear();
	}

	private void saveMethodMetrics(HashMap<String, String> data) {

		MethodMetrics methodMetrics = new MethodMetrics(Double.parseDouble(data.get("VG")),
				Double.parseDouble(data.get("MLOC")), Double.parseDouble(data.get("PAR")),
				Double.parseDouble(data.get("NBD")));
		methodMetrics.setId(data.get("oid"));

		boolean existe;

		List<MethodMetrics> methodMetricsList = this.methodMetricsService.findAll();

		existe = false;

		for (int i = 0; i < methodMetricsList.size(); i++) {
			if (methodMetricsList.get(i).getId().equals(methodMetrics.getId())) {
				existe = true;
			}
		}
		if (!existe) {
			this.methodMetricsService.saveMethodMetrics(methodMetrics);
		}

		methodMetricsList.clear();

	}

	private static void printNote(NodeList nodeList, HashMap<String, String> data, String repository, String owner) {

		for (int count = 0; count < nodeList.getLength(); count++) {

			Node tempNode = nodeList.item(count);

			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

				if (tempNode.getNodeName().equals("commit")) {
					obtenerDatosCommit(tempNode, data, repository, owner);
				}

				if (tempNode.getNodeName().equals("Metric") && !data.get("oid").isEmpty()) {
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

			if (!id.equals("TLOC") && !id.equals("NOP")) {

				NamedNodeMap nodeMap = nodeList.item(1).getAttributes();
				avg = nodeMap.getNamedItem("avg").getNodeValue();

				data.put(id, avg);
			} else {
				NamedNodeMap nodeMap = nodeList.item(1).getAttributes();
				avg = nodeMap.getNamedItem("value").getNodeValue();

				data.put(id, avg);
			}

		}

	}

	private static void obtenerDatosCommit(Node tempNode, HashMap<String, String> data, String repository,
			String owner) {

		NodeList nodeList = tempNode.getChildNodes();
		String repoNow = "";
		String ownerNow = "";
		String oid = "";

		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				if (nodeList.item(i).getNodeName().equals("oid")) {
					oid = nodeList.item(i).getTextContent();
				} else if (nodeList.item(i).getNodeName().equals("repository")) {
					repoNow = nodeList.item(i).getTextContent();
				} else if (nodeList.item(i).getNodeName().equals("owner")) {
					ownerNow = nodeList.item(i).getTextContent();
				}
			}
		}

		if (repoNow.equals(repository) && ownerNow.equals(owner)) {
			data.put("oid", oid);
		}
	}

	public List<String> listFilesForFolder(final File folder, List<String> filenames, String extension) {

		List<String> updatedFiles = filenames;
		String ext;

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry, filenames, extension);
			} else {
				ext = FilenameUtils.getExtension(fileEntry.getName());
				if (ext.equals(extension)) {
					filenames.add(fileEntry.getPath());
				}
			}
		}

		return updatedFiles;

	}

}
