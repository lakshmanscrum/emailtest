package com.csv.emailtest;

import java.util.Properties;
import java.util.concurrent.Callable;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

/**
 * This class will send email using SMTP protocol
 * @author lakshman
 *
 */
public class SendEmail implements Callable<String>{
	int input;
	String email;
	String firstName;
	String lastName;
	public static final  String SMTP_PORT="587";
	public static final  String SMTP_AUTH="true";
	public static final  String SMTP_STARTTTLS_ENABLE = "true";
	final static Logger errorLog = Logger.getLogger("admin");
	final static Logger logFile = Logger.getLogger("file");

	

    public SendEmail(String email,String firstName, String lastName) {
    	this.email = email;
    	this.firstName = firstName;
    	this.lastName = lastName;
    }

    /**
    @Override
    public Integer call() throws Exception {
        return input + 1;
    }
    */
	/**
	 * This method will send email 
	 */
    @Override
	public String call() throws Exception{
    	//System.out.println("inside call()");
		try {
			Properties mailServerProperties;
			Session getMailSession;
			MimeMessage generateMailMessage;
			// Step1
			mailServerProperties = System.getProperties();
			mailServerProperties.put("mail.smtp.port", SMTP_PORT);
			mailServerProperties.put("mail.smtp.auth", SMTP_AUTH);
			mailServerProperties.put("mail.smtp.starttls.enable", SMTP_STARTTTLS_ENABLE);
	 
			// Step2
			getMailSession = Session.getDefaultInstance(mailServerProperties, null);
			generateMailMessage = new MimeMessage(getMailSession);
			generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			generateMailMessage.setSubject("Test email to .." + firstName +" "+firstName);
			String emailBody = "Hi " + firstName +" "+firstName+ "<br>";
			emailBody = emailBody  + "Test email by from . " + "<br><br> Regards, <br>mysite.com";
			generateMailMessage.setContent(emailBody, "text/html");
	 
			Transport transport = getMailSession.getTransport("smtp");
	 

			//transport.connect("smtp.gmail.com", "userid", "password");
			//transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
			logFile.debug("Sending email to " + email);
			// sllep for half second
			Thread.sleep(500);
			transport.close();
			logFile.debug("Mail sent to " + email);
		} catch(Exception e) {
			e.printStackTrace();
			errorLog.error(e.getLocalizedMessage());
		}
		return "success";
		
	}

	

}
