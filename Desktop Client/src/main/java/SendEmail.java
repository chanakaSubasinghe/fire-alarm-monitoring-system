// importing dependencies
import com.sun.mail.smtp.SMTPTransport;

import com.sun.mail.smtp.SMTPTransport;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

// class definition
public class SendEmail {

	// declaring global variables
    private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final String USERNAME = "gotukolamalluma@gmail.com";
    private static final String PASSWORD = "YOUR_PASSWORD";

    private static final String EMAIL_FROM = "gotukolamalluma@gmail.com";
    private static final String EMAIL_TO = "gotukolamalluma@gmail.com";
    private static final String EMAIL_TO_CC = "";

    private static final String EMAIL_SUBJECT = "The fire alert has been activated!!";
    private static final String EMAIL_TEXT = "please leave the building by the nearest exit and go to the nearest assembly point immediately!!";
    
    // defining constructor
    SendEmail() {
    	// get system properties and include necessary data
    	Properties prop = System.getProperties();
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", SMTP_SERVER); //optional, defined in SMTPTransport
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "25"); // default port 25

        // declaring session
        Session session = Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);

        // try catch block
        try {

			// from
            msg.setFrom(new InternetAddress(EMAIL_FROM));

			// to
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(EMAIL_TO, false));

			// cc
            msg.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(EMAIL_TO_CC, false));

			// subject
            msg.setSubject(EMAIL_SUBJECT);

			// content
            msg.setText(EMAIL_TEXT);

            msg.setSentDate(new Date());

			// Get SMTPTransport
            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

			// connect
            t.connect(SMTP_SERVER, USERNAME, PASSWORD);

			// send
            t.sendMessage(msg, msg.getAllRecipients());

            // user message when email sent successfully.
            System.out.println("Email sent successfully.");

            // closing SMTPTransport
            t.close();

        } catch (MessagingException e) {
        	// print error
            e.printStackTrace();
        }
    }
}
