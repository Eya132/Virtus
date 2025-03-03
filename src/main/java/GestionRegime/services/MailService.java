package GestionRegime.services;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.configuration.PropertiesConfiguration;

public class MailService {

    private String smtpHost;
    private int smtpPort;
    private String from;
    private String password;
    private String to;

    public MailService() {
        try {
            // Charge les propriétés de configuration
            PropertiesConfiguration config = new PropertiesConfiguration("config.properties");
            this.smtpHost = config.getString("mail.smtp.host");
            this.smtpPort = config.getInt("mail.smtp.port");
            this.from = config.getString("mail.from");
            this.password = config.getString("mail.password");
            this.to = config.getString("mail.to");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMail(String subject, String content) {
        // Configuration des propriétés SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Crée une session avec authentification
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Crée le message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            // Envoie le message
            Transport.send(message);
            System.out.println("E-mail envoyé avec succès !");
        } catch (AuthenticationFailedException e) {
            System.err.println("Erreur d'authentification : Vérifiez votre adresse e-mail et votre mot de passe.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
            e.printStackTrace();
        }
    }
}