package edu.uclm.esi.devopsmetrics.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

@CucumberContextConfiguration
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", monochrome = true, plugin = {
		"json:target/reports/JSONReports/TestReport.json", "pretty" })
public class TestRunner {

	
	final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");
	final static LocalDateTime now = LocalDateTime.now();
	
	private static void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				System.out.println("SERVER: " + aReply);
			}
		}
	}
	
	@AfterClass
	public static void setupAfter() throws InterruptedException {
		
		String fecha = dtf.format(now);

		String source = "C:\\Users\\Crespo\\.jenkins\\workspace\\devopsmetrics\\target\\reports\\JSONReports\\TestReport.json";
		File srcFile = new File(source);
		
		String filename = "TestReport-devopsmetrics-FcoCrespo-"+ fecha + ".json";
		String newName = "C:\\Users\\Crespo\\.jenkins\\workspace\\devopsmetrics\\target\\reports\\JSONReports\\"+filename;
		File srcFileFinal = new File(newName);

		srcFile.renameTo(srcFileFinal);
		
		if(srcFileFinal.exists()) {
			String server = "esidevopsmetrics.ddns.net";
			int port = 21;
			String user = System.getProperty("server.user");
			String pass = System.getProperty("server.key");

			FTPClient ftpClient = new FTPClient();
			try {

				ftpClient.connect(server, port);
				showServerReply(ftpClient);
				int replyCode = ftpClient.getReplyCode();
				if (!FTPReply.isPositiveCompletion(replyCode)) {
					System.out.println("Operation failed. Server reply code: " + replyCode);
					return;
				}
				boolean success = ftpClient.login(user, pass);
				showServerReply(ftpClient);
				if (!success) {
					System.out.println("Could not login to the server");
					return;
				}
				
				ftpClient.enterLocalPassiveMode();
				
				String dirToCreate = "TestReport-devopsmetrics-FcoCrespo-" + fecha;
				
				success = ftpClient.makeDirectory(dirToCreate);
				showServerReply(ftpClient);
				if (success) {
					System.out.println("Successfully created directory: " + dirToCreate);
				} else {
					System.out.println("Failed to create directory. See server's reply.");
				}
				 
	            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	 
	            String firstRemoteFile = dirToCreate+"/"+filename;
	            InputStream inputStream = new FileInputStream(srcFileFinal);
	 
	            System.out.println("Start uploading first file");
	            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
	            inputStream.close();
	            if (done) {
	                System.out.println("The file "+firstRemoteFile+" is uploaded successfully.");
	            }

			} catch (IOException ex) {
				System.out.println("Error: " + ex.getMessage());
				ex.printStackTrace();
			} finally {
				try {
					if (ftpClient.isConnected()) {
						ftpClient.logout();
						ftpClient.disconnect();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

	}
}