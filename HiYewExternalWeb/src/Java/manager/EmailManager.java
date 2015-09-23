package manager;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import java.util.Date;
import java.util.Properties;
import javax.ejb.EJBException;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import util.SMTPAuthenticator;

public class EmailManager {

    String emailServerName = "mailauth.comp.nus.edu.sg";
    // Replace with your real name and unix email address
    String emailFromAddress = "Koh Yi Wen<yiwenkoh@comp.nus.edu.sg>";
    // Replace with your real name and NUSNET email address
    String toEmailAddress = "Mabel<yiwenkoh@nus.edu.sg>";
    String mailer = "JavaMailer";

    public EmailManager(String newPassword) {
        this.emailPassword(newPassword);
    }

    // For testing purpose
    public static void main(String args[]) throws Exception {
        EmailManager emailManager = new EmailManager("your_new_password");
    }

    public void emailPassword(String newPassword) {
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            javax.mail.Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);
            if (msg != null) {
                msg.setFrom(InternetAddress.parse(emailFromAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
                msg.setSubject("HiYew - Your password has been reset successfully!");
                
                Multipart multipart = new MimeMultipart("related");
		BodyPart htmlPart = new MimeBodyPart();
                
                String messageText = "Your new password: " + newPassword;

                String message = "<div class=\"text\">";
                message = message + "<font face=\"Century Gothic\" size=\"2\"><b>";
                message = message + "<font color=\"#8A4117\">";
                message = message + "Hi,</font><br /><br />";
                message = message + "<font color=\"#C58917\">";
                message = message + "Your Username: " + "<br />";
                message = message + "Your New Password: " + "</font>" + "<br /><br /><br /><br />";
                message = message + "<font color=\"#8A4117\">";
                message = message + "Sincerely,<br />";
                message = message + "The Fantasy Team</font>";
                message = message + "</b></font></div>";
                message = "<html><style type=\"text/css\"></style><body>" + message + "</body></html>";

                htmlPart.setContent(message, "text/html");
		multipart.addBodyPart(htmlPart);
                msg.setContent(multipart);
                msg.setHeader("X-Mailer", mailer);
                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);
                Transport.send(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new EJBException(e.getMessage());
        }
    }
}
