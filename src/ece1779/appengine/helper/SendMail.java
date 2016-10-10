package ece1779.appengine.helper;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
	public static 
		void send( 	String senderEmail,
					String senderName,
					String recipientEmail,
					String recipientName,
					String subject,
					String msgBody ) 
	{
		Properties props = new Properties();
	    Session session = Session.getDefaultInstance(props, null);
	
	    try {
	        Message msg = new MimeMessage(session);
	        msg.setFrom(new InternetAddress(senderEmail, senderName));
	        msg.addRecipient(Message.RecipientType.TO, 
	        		new InternetAddress(recipientEmail,recipientName));
	        msg.setSubject(subject);
	        msg.setText(msgBody);
	        Transport.send(msg);
	    } catch (Exception e) {
	    	;
	    }
	}	
}

