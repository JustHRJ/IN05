package manager;

import entity.ProductQuotationDescription;
import java.util.ArrayList;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
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
    String emailFromAddress = "HiYew Team<hiyewmetalwelding@gmail.com>";
    // Replace with your real name and NUSNET email address
    String toEmailAddress = "";
    String mailer = "JavaMailer";

    public EmailManager() {
    }

    public EmailManager(String name, String username, String newPassword, String email) {
        this.emailPassword(name, newPassword, newPassword, email);
    }

    // For testing purpose
    public static void main(String args[]) throws Exception {
        // EmailManager emailManager = new EmailManager("your_name", "your username", "your_new_password", "hurulez@gmail.com");
    }

    public void emailPassword(String name, String username, String newPassword, String email) {
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
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
                msg.setSubject("Confidential - HiYew Employment Details");

                Multipart multipart = new MimeMultipart("related");
                BodyPart htmlPart = new MimeBodyPart();

                String message = "<div class=\"text\">";
                message = message + "Dear <b>" + name + "</b>,<br /><br />";
                message = message + "Your username:  <b>" + username + "</b><br/>";
                message = message + "Your new password: <b>" + newPassword + "</b><br /><br />";
                message = message + "Best Regards,<br />";
                message = message + "HiYew Team";
                message = message + "</div>";
                message = "<html><body>" + message + "</body></html>";
                System.out.println("psw: " + newPassword);
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

    public void emailReminder(String email, Vector im, String projNo, String customer) {
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
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
                msg.setSubject("Reminder - ISO 9001 documents  (project #" + projNo + ")");

                Multipart multipart = new MimeMultipart("related");
                BodyPart htmlPart = new MimeBodyPart();

                String message = "<div class=\"text\">";
                message = message + "Dear <b>" + customer + "</b>,<br /><br />";
                message = message + "According to our records, there are outstanding documents required  to meet our company ISO 9001 standards. The following documents include:" + "</b><br /><br />";

                for (int i = 1; i <= im.size(); i++) {
                    message = message  + i  + ". " +  im.get(i-1).toString() + "</b><br />";
                }
                
                message = message + "<br/><br/>Please kindly do so. For any enquiries or confirmation, please contact HiYew." + "<br /><br /><br />   <br />";
                message = message + "Best Regards,<br />";
                message = message + "HiYew Team";
                message = message + "</div>";
                message = "<html><body>" + message + "</body></html>";

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

    public void emailProductQuotationPriceUpdate(String name, String email, String productQuotationNo) {
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
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
                msg.setSubject("HiYew - Product Quotation #" + productQuotationNo);

                Multipart multipart = new MimeMultipart("related");
                BodyPart htmlPart = new MimeBodyPart();

                String message = "<div class=\"text\">";
                message = message + "Dear <b>" + name + "</b>,<br /><br />";
                message = message + "We have updated the quoted unit price for your item(s) for Product Quotation <b>#" + productQuotationNo + "</b>.<br/>";
                message = message + "You may go to HiYew Customer Portal to view the updates.<br /><br />";
                message = message + "Thank you!<br /><br />";
                message = message + "Best Regards,<br />";
                message = message + "HiYew Team";
                message = message + "</div>";
                message = "<html><body>" + message + "</body></html>";

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

    public void emailGermanySupplierToRFQ(String qNo, ArrayList<ProductQuotationDescription> pqdList) {
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
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("hiyewgersup@gmail.com", false));
                msg.setSubject("HiYew - Product Quotation #" + qNo);

                Multipart multipart = new MimeMultipart("related");
                BodyPart htmlPart = new MimeBodyPart();

                String message = "<div class=\"text\">";
                message = message + "Hi there,<br /><br />";
                message = message + "We would like to request for price quotation for the following item(s).<br/><br/>";

                for (ProductQuotationDescription pdq : pqdList) {
                    message = message + "Item #" + pdq.getProductQuotationDescNo() + "<br />";
                    message = message + "Item Type: <b>" + pdq.getProductType() + "</b><br />";
                    message = message + "Item Name: <b>" + pdq.getItemName() + "</b><br />";
                    message = message + "Quantity: <b>" + pdq.getQuantity() + "</b><br /><br />";
                }

                message = message + "Please do not hesitate to email us should you need any further clarification. Thank you!<br /><br />";
                message = message + "Best Regards,<br />";
                message = message + "HiYew Team";
                message = message + "</div>";
                message = "<html><body>" + message + "</body></html>";

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

    public void emailProductPODeliveryDateUpdate(String name, String email, String productPONo) {
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
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
                msg.setSubject("HiYew - Product Purchase Order #" + productPONo);

                Multipart multipart = new MimeMultipart("related");
                BodyPart htmlPart = new MimeBodyPart();

                String message = "<div class=\"text\">";
                message = message + "Dear <b>" + name + "</b>,<br /><br />";
                message = message + "We have updated the delivery date for your item(s) for Product Purchase Order <b>#" + productPONo + "</b>.<br/>";
                message = message + "You may go to HiYew Customer Portal to view the updates.<br /><br />";
                message = message + "Thank you!<br /><br />";
                message = message + "Best Regards,<br />";
                message = message + "HiYew Team";
                message = message + "</div>";
                message = "<html><body>" + message + "</body></html>";

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

    public void emailGermanySupplierToPurchase(String poNumber, ArrayList<ProductQuotationDescription> pqdList) {
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
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("hiyewgersup@gmail.com", false));
                msg.setSubject("HiYew - Product Purchase Order #" + poNumber);

                Multipart multipart = new MimeMultipart("related");
                BodyPart htmlPart = new MimeBodyPart();

                String message = "<div class=\"text\">";
                message = message + "Hi there,<br /><br />";
                message = message + "We would like to purchase the following item(s) from your company.<br/><br/>";

                for (ProductQuotationDescription pdq : pqdList) {
                    message = message + "Item #" + pdq.getProductQuotationDescNo() + "<br />";
                    message = message + "Item Type: <b>" + pdq.getProductType() + "</b><br />";
                    message = message + "Item Name: <b>" + pdq.getItemName() + "</b><br />";
                    message = message + "Quantity: <b>" + pdq.getQuantity() + "</b><br /><br />";
                }

                message = message + "Please do not hesitate to email us should you need any further clarification. Thank you!<br /><br />";
                message = message + "Best Regards,<br />";
                message = message + "HiYew Team";
                message = message + "</div>";
                message = "<html><body>" + message + "</body></html>";

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

    public void emailActivation(String code, String email) {
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
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
                msg.setSubject("Confidential - HiYew Employment Details");

                Multipart multipart = new MimeMultipart("related");
                BodyPart htmlPart = new MimeBodyPart();

                String message = "<div class=\"text\">";
                message = message + "To whom it may concern,<br /><br />";
                message = message + "This message is intended for supplier registration with HiYew Corporation Limited.<br /><br />";
                message = message + "Your Activation Code is  <b>" + code + "</b>. ";
                message = message + "Your are required to provide this code for registration within a day.<br /><br />Thank you.<br /><br />";
                message = message + "Best Regards,<br />";
                message = message + "HiYew Team";
                message = message + "</div>";
                message = "<html><body>" + message + "</body></html>";

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
    
    public void emailPBSent(String companyName, String email) {
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
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(companyName + "<" + email + ">", false));
                msg.setSubject("Invitation to Bid!");

                Multipart multipart = new MimeMultipart("related");
                BodyPart htmlPart = new MimeBodyPart();

                String message = "<div class=\"text\">";
                message = message + "Dear <b>" + companyName + "</b>,<br /><br />";
                message = message + "This is to notify that there is a new bidding open to"+ companyName + ".<br /><br />";
                message = message + "Please log in to Hi-Yew External Portal for more details.<br /><br />";
     
                message = message + "Best Regards,<br />";
                message = message + "HiYew Team";
                message = message + "</div>";
                message = "<html><body>" + message + "</body></html>";

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
    
     public void emailPBResult(String companyName, String email) {
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
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(companyName + "<" + email + ">", false));
                msg.setSubject("Congratulations! Your bid has been accepted!");

                Multipart multipart = new MimeMultipart("related");
                BodyPart htmlPart = new MimeBodyPart();

                String message = "<div class=\"text\">";
                message = message + "Dear <b>" + companyName + "</b>,<br /><br />";
                message = message + "Congratulations!This is to notify that your bid has been accepted!<br /><br />";
                message = message + "Please log in to Hi-Yew External Portal for more details and proceed to fulfil the request!<br /><br />";
     
                message = message + "Best Regards,<br />";
                message = message + "HiYew Team";
                message = message + "</div>";
                message = "<html><body>" + message + "</body></html>";

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
