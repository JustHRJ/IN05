package util;

import javax.mail.*;

public class SMTPAuthenticator extends javax.mail.Authenticator {
    // Replace with your actual unix id
    private static final String SMTP_AUTH_USER = "yiwenkoh";
    // Replace with your actual unix password
    private static final String SMTP_AUTH_PWD = "dexmab22!(";

    public SMTPAuthenticator() {
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        String username = SMTP_AUTH_USER;
        String password = SMTP_AUTH_PWD;
        return new PasswordAuthentication(username, password);
    }
}