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

    public EmailManager() {
    }

    public EmailManager(String name, String newPassword, String email) {
        this.emailPassword(name, newPassword, email);
    }
    
    public EmailManager(String name, String username, String password, String email) {
        this.emailSuccessfulRegistration(name, username, password, email);
    }

    // For testing purpose
    public static void main(String args[]) throws Exception {
        // EmailManager emailManager = new EmailManager("your_name", "your_new_password");
    }
    
    public void emailSuccessfulRegistration(String name, String username, String password, String email) {
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
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(name + "<" + email + ">", false));
                msg.setSubject("HiYew - Welcome!");

                Multipart multipart = new MimeMultipart("related");
                BodyPart htmlPart = new MimeBodyPart();

                String message = "<div class=\"text\">";
                message = message + "<font face=\"'Open Sans', Arial\">";
                message = message + "<font color=\"#333\">";
                message = message + "Dear <b>" + name + "</b>,</font><br /><br />";
                message = message + "<font color=\"#333\">";
                message = message + "Your username: <b>" + username + "</b></font>" + "<br />";
                message = message + "Your password: <b>" + password + "</b></font>" + "<br /><br />";
                message = message + "<font color=\"#333\">";
                message = message + "Sincerely,<br />";
                message = message + "HiYew Team</font>";
                message = message + "</font></div>";
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

    public void emailPassword(String name, String newPassword, String email) {
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
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(name + "<" + email + ">", false));
                msg.setSubject("HiYew - Your password has been reset successfully!");

                Multipart multipart = new MimeMultipart("related");
                BodyPart htmlPart = new MimeBodyPart();

                String message = "<div class=\"text\">";
                message = message + "<font face=\"'Open Sans', Arial\">";
                message = message + "<font color=\"#333\">";
                message = message + "Dear <b>" + name + "</b>,</font><br /><br />";
                message = message + "<font color=\"#333\">";
                message = message + "Your new password: <b>" + newPassword + "</b></font>" + "<br /><br />";
                message = message + "<font color=\"#333\">";
                message = message + "Sincerely,<br />";
                message = message + "HiYew Team</font>";
                message = message + "</font></div>";
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