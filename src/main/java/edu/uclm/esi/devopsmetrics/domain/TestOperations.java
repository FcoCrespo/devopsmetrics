package edu.uclm.esi.devopsmetrics.domain;

import java.io.File;
import java.io.IOException;
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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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

	@Value("${app.testsdir}")
	private String testsdir;

	public TestOperations(final TestMetricsService testMetricsService, final MethodTestService methodTestService) {

		this.testMetricsService = testMetricsService;
		this.methodTestService = methodTestService;
		this.elementStr = "elements";
		this.checkFail = ",\"status\":\"failed\"}";
	}

	public String getRepoTestMetrics(String repository, String owner) {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveRepoTestMetrics(String repository, String owner) throws IOException {

		LOG.info(this.testsdir);

		File folder = new File(this.testsdir.replace("%20", " "));

		List<String> filenames = new ArrayList<String>();
		List<String> filepaths = new ArrayList<String>();

		TestMetrics testMetrics;

		String extension = "json";
		String repoTest;
		String ownerTest;
		String dateTest;
		Instant fecha;

		File file;
		String str;

		filenames = listFilesNames(folder, filenames, extension);
		filepaths = listFilesForFolder(folder, filepaths, extension);

		String[] variables;

		JsonNode nodes;
		Iterator<JsonNode> iter;
		JsonNode parameterNode;

		String[] elemFecha = new String[5];

		for (int i = 0; i < filepaths.size(); i++) {

			
			CloseableHttpClient httpclient = HttpClients.createDefault();
			
			try {
				if (checkFile(filenames.get(i), repository, owner)) {

					variables = filenames.get(i).split("-");

					repoTest = variables[1];
					ownerTest = variables[2];

					elemFecha[0] = variables[3];
					elemFecha[1] = variables[4];
					elemFecha[2] = variables[5];
					elemFecha[3] = variables[6];
					elemFecha[4] = variables[7];

					dateTest = getDateCorrect(elemFecha);
					fecha = getDatesInstant(dateTest);

					LOG.info("repoTest: " + repoTest + ", ownerTest: " + ownerTest + ", dateToCorrect: "
							+ fecha.toString());

					
					LOG.info(filepaths.get(i));
					HttpGet httpget = new HttpGet("http://localhost:5050/getTestReport?filepath="+filenames.get(i));

					LOG.info("Request Type: " + httpget.getMethod());

					HttpResponse httpresponse = httpclient.execute(httpget);

					HttpEntity entity = httpresponse.getEntity();
					String jsonData = EntityUtils.toString(entity, "UTF-8");
					nodes = new ObjectMapper().readTree(jsonData);

					iter = nodes.iterator();
					parameterNode = iter.next();

					testMetrics = new TestMetrics(repoTest, ownerTest, fecha);

					this.testMetricsService.saveTestMetrics(testMetrics);

					comprobarContenido(iter, parameterNode, testMetrics);

					file = new File(filepaths.get(i));
					str = file.getPath().replace(".json", ".jsondone");
					if (file.renameTo(new File(str))) {
						LOG.info("Archivo procesado: " + filenames.get(i));
					}

					httpclient.close();
				}
			} catch (Exception e) {
				LOG.info("Error en la lectura de los archivos");
			}
			finally {
				httpclient.close();
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

	private boolean checkFile(String filename, String repository, String owner) {

		String[] variables = filename.split("-");

		boolean correcto = false;

		if (variables[1].equals(repository) && variables[2].equals(owner)) {

			correcto = true;

		}

		return correcto;
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

	public List<String> listFilesNames(final File folder, List<String> filenames, String extension) {

		List<String> updatedFiles = filenames;
		String ext;

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesNames(fileEntry, filenames, extension);
			} else {
				ext = FilenameUtils.getExtension(fileEntry.getName());
				if (ext.equals(extension)) {
					filenames.add(fileEntry.getName());
				}
			}
		}

		return updatedFiles;

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
