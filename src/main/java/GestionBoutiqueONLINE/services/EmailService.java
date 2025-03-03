package GestionBoutiqueONLINE.services;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {
    public static void sendOrderConfirmationEmail(String toEmail, String orderDetails, String paymentDetails) {
        // Configuration du serveur SMTP
        String host = "smtp.gmail.com"; // Serveur SMTP de Gmail
        String fromEmail = "doghmenesabee@gmail.com"; // Votre adresse Gmail
        String password = "tbib oznz mnql kjsj"; // Mot de passe d'application

        // Propriétés pour la session
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Créer une session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Créer le message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Confirmation de commande");

            // Corps de l'e-mail
            String emailContent = "Merci pour votre commande !\n\n"
                    + "Détails de la commande :\n" + orderDetails + "\n\n"
                    + "Détails du paiement :\n" + paymentDetails + "\n\n"
                    + "Cordialement,\nL'équipe MatchMate";
            message.setText(emailContent);

            // Envoyer le message
            Transport.send(message);
            System.out.println("E-mail de confirmation de commande envoyé avec succès à : " + toEmail);
        } catch (MessagingException e) {
            System.err.println("Erreur lors de l'envoi de l'e-mail de confirmation de commande : " + e.getMessage());
        }
    }
    }

