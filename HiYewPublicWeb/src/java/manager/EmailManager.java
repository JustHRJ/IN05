package manager;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import java.util.Date;
import java.util.Properties;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import util.SMTPAuthenticator;

@ManagedBean
public class EmailManager {

    String emailServerName = "mailauth.comp.nus.edu.sg";
    // Replace with your real name and unix email address
    String emailFromAddress = "HiYew Team<mabel.kohyw@gmail.com>";
    // Replace with your real name and NUSNET email address
    String toEmailAddress = "";
    String mailer = "JavaMailer";

    private String name = "";
    private String email = "";
    private String mobile = "";
    private String subject = "";

    public EmailManager() {
    }

    // For testing purpose
    public static void main(String args[]) throws Exception {
    }

    public void emailContact() {
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
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("" + "<" + "hiyewmetalwelding@gmail.com" + ">", false));
                msg.setSubject("[Public Portal] A message from " + name);

                Multipart multipart = new MimeMultipart("related");
                BodyPart htmlPart = new MimeBodyPart();

                String message = "<div class=\"text\">";
                message = message + "From: <b>" + name + "</b><br />";
                message = message + "Email: <b>" + email + "</b><br />";
                message = message + "Mobile: <b>" + mobile + "</b><br />";
                message = message + "Subject: <b>" + subject + "</b><br /><br />";
                message = message + "</div>";
                message = "<html><body>" + message + "</body></html>";

                htmlPart.setContent(message, "text/html");
                multipart.addBodyPart(htmlPart);
                msg.setContent(multipart);
                msg.setHeader("X-Mailer", mailer);
                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);
                Transport.send(msg);
                name = "";
                email = "";
                mobile = "";
                subject = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new EJBException(e.getMessage());
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

}
