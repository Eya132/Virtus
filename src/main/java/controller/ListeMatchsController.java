package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;
import Entites.Match1;
import Entites.ListInscri;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.MatchService;
import services.ListInscriService;
import javax.mail.Session;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.Multipart;
import javax.mail.*;
import javax.mail.internet.*;
//import java.awt.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class ListeMatchsController {
    private int userId; // ID de l'utilisateur connecté

    // Méthode pour définir l'ID utilisateur
    public void setUserId(int userId) {
        this.userId = userId;
    }
    @FXML
    private TableView<Match1> matchTable;

    @FXML
    private TableColumn<Match1, String> dateColumn;

    @FXML
    private TableColumn<Match1, String> heureColumn;

    @FXML
    private TableColumn<Match1, String> localisationColumn;

    @FXML
    private TableColumn<Match1, String> terrainColumn;

    @FXML
    private TableColumn<Match1, String> typeSportColumn;

    @FXML
    private ComboBox<String> localisationFilter;

    @FXML
    private DatePicker dateFilter;

    @FXML
    private ComboBox<String> typeSportFilter;

    @FXML
    private MenuButton trierParDateButton;

    private MatchService matchService = new MatchService();
    private ObservableList<Match1> matchs; // Liste observable pour les matchs

    @FXML
    public void initialize() {
        // Configurer les colonnes
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        heureColumn.setCellValueFactory(new PropertyValueFactory<>("heure"));
        localisationColumn.setCellValueFactory(new PropertyValueFactory<>("localisation"));
        terrainColumn.setCellValueFactory(new PropertyValueFactory<>("terrain"));
        typeSportColumn.setCellValueFactory(new PropertyValueFactory<>("typeSport"));

        // Charger les matchs depuis le service
        matchs = FXCollections.observableArrayList(matchService.getAllMatches());
        matchTable.setItems(matchs);

        // Remplir les ComboBox avec les valeurs uniques
        remplirFiltres();

        // Ajouter une colonne pour le bouton "Participer"
        TableColumn<Match1, Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellFactory(param -> new ButtonTableCell());
        matchTable.getColumns().add(actionColumn);

        // Ajouter des écouteurs pour le filtrage automatique
        localisationFilter.valueProperty().addListener((obs, oldVal, newVal) -> appliquerFiltres());
        dateFilter.valueProperty().addListener((obs, oldVal, newVal) -> appliquerFiltres());
        typeSportFilter.valueProperty().addListener((obs, oldVal, newVal) -> appliquerFiltres());

        // Lier les actions des MenuItem pour le tri par date
        for (MenuItem item : trierParDateButton.getItems()) {
            item.setOnAction(event -> trierParDate(event));
        }
    }

    // Remplir les ComboBox avec les valeurs uniques
    private void remplirFiltres() {
        List<String> localisations = matchs.stream()
                .map(Match1::getLocalisation)
                .distinct()
                .collect(Collectors.toList());
        localisationFilter.setItems(FXCollections.observableArrayList(localisations));

        List<String> typesSport = matchs.stream()
                .map(Match1::getTypeSport)
                .distinct()
                .collect(Collectors.toList());
        typeSportFilter.setItems(FXCollections.observableArrayList(typesSport));
    }

    // Appliquer les filtres
    private void appliquerFiltres() {
        String localisation = localisationFilter.getValue();
        LocalDate date = dateFilter.getValue();
        String typeSport = typeSportFilter.getValue();

        // Filtrer la liste des matchs
        FilteredList<Match1> filteredList = matchs.filtered(match -> {
            boolean matchesLocalisation = localisation == null || match.getLocalisation().equals(localisation);
            boolean matchesTypeSport = typeSport == null || match.getTypeSport().equals(typeSport);
            boolean matchesDate = date == null || match.getDate().equals(date); // Utilisation de LocalDate directement

            return matchesLocalisation && matchesDate && matchesTypeSport;
        });

        // Mettre à jour la TableView
        matchTable.setItems(filteredList);
    }

    // Méthode pour trier par date (ascendant ou descendant)
    @FXML
    private void trierParDate(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        boolean ascendant = menuItem.getText().equals("Ascendant");

        matchs.sort((m1, m2) -> {
            LocalDate date1 = LocalDate.parse(m1.getDate().toString()); // Convertir la date en LocalDate
            LocalDate date2 = LocalDate.parse(m2.getDate().toString());
            return ascendant ? date1.compareTo(date2) : date2.compareTo(date1); // Inverser l'ordre si descendant
        });
        matchTable.refresh(); // Rafraîchir la TableView
    }

    // Méthode pour trier par type de sport (ascendant par défaut)
    @FXML
    private void trierParSport() {
        boolean ascendant = true; // Toujours trier en ordre ascendant
        matchs.sort((m1, m2) -> {
            return ascendant ? m1.getTypeSport().compareToIgnoreCase(m2.getTypeSport())
                    : m2.getTypeSport().compareToIgnoreCase(m1.getTypeSport()); // Inverser l'ordre si descendant
        });
        matchTable.refresh(); // Rafraîchir la TableView
    }

    // Classe interne pour le bouton "Participer"
    private class ButtonTableCell extends TableCell<Match1, Void> {
        private final Button participerButton = new Button("Participer");

        public ButtonTableCell() {
            participerButton.setOnAction(event -> {
                Match1 match = getTableView().getItems().get(getIndex());
                participerAuMatch(match);
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                Match1 match = getTableView().getItems().get(getIndex());
                ListInscriService listInscriService = new ListInscriService();
                int nbParticipants = listInscriService.getNombreParticipants(match.getId());

                // Calculer le nombre maximum de participants
                int maxParticipants = calculateMaxParticipants(match.getNbPersonne());

                // Désactiver le bouton si le nombre maximum de participants est atteint
                if (nbParticipants >= maxParticipants) {
                    participerButton.setDisable(true);
                    participerButton.setText("Complet");
                } else {
                    participerButton.setDisable(false);
                    participerButton.setText("Participer");
                }
                setGraphic(participerButton);
            }
        }
    }

    // Méthode pour gérer la participation à un match
    private void participerAuMatch(Match1 match) {
        // Boîte de dialogue pour demander userId
        TextInputDialog userIdDialog = new TextInputDialog();
        userIdDialog.setTitle("Participation au match");
        userIdDialog.setHeaderText("Entrez votre ID utilisateur");
        userIdDialog.setContentText("User ID:");

        // Afficher la boîte de dialogue et attendre la réponse
        userIdDialog.showAndWait().ifPresent(userIdInput -> {
            if (userIdInput.trim().isEmpty()) {
                // Si le champ est vide, afficher une alerte
                showErrorAlert("Champ vide", "L'ID utilisateur ne peut pas être vide.");
                return;
            }

            try {
                // Convertir l'entrée en entier
                int userId = Integer.parseInt(userIdInput);

                // Vérifier si l'utilisateur est le créateur du match
                if (userId == match.getUserId()) {
                    showWarningAlert("Planificateur du match", "Vous êtes le planificateur de ce match et ne pouvez pas y participer.");
                    return;
                }

                // Enregistrer la participation au match
                enregistrerParticipation(match.getId(), userId);

                // Demander l'adresse e-mail de l'utilisateur
                TextInputDialog emailDialog = new TextInputDialog();
                emailDialog.setTitle("Confirmation par e-mail");
                emailDialog.setHeaderText("Entrez votre adresse e-mail pour recevoir une confirmation");
                emailDialog.setContentText("E-mail:");

                // Afficher la boîte de dialogue et attendre la réponse
                emailDialog.showAndWait().ifPresent(email -> {
                    if (email.trim().isEmpty()) {
                        showErrorAlert("Champ vide", "L'adresse e-mail ne peut pas être vide.");
                        return;
                    }

                    // Envoyer un e-mail de confirmation
                    try {
                        sendConfirmationEmail(email, match, userId);
                        showSuccessAlert("Succès", "Un e-mail de confirmation a été envoyé à " + email);
                    } catch (Exception e) {
                        showErrorAlert("Erreur", "Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
                    }
                });
            } catch (NumberFormatException e) {
                showErrorAlert("ID utilisateur invalide", "Veuillez entrer un nombre valide pour l'ID utilisateur.");
            }
        });
    }

    // Méthode pour enregistrer la participation et générer un PDF
    private void enregistrerParticipation(int matchId, int userId) {
        ListInscriService listInscriService = new ListInscriService();
        ListInscri inscription = new ListInscri();
        inscription.setMatchId(matchId);
        inscription.setuserId(userId);

        // Vérifier si l'utilisateur est déjà inscrit
        ListInscri existingInscription = listInscriService.getInscriptionByMatchAndUser(matchId, userId);
        if (existingInscription != null) {
            showWarningAlert("Inscription déjà existante", "Vous êtes déjà inscrit à ce match.");
            return;
        }

        // Si pas d'inscription existante, ajouter l'inscription
        try {
            listInscriService.addInscription(inscription);
            System.out.println("Participation enregistrée avec succès !");

            // Vérifier si le nombre maximum de participants est atteint
            int nbParticipants = listInscriService.getNombreParticipants(matchId);
            int maxParticipants = calculateMaxParticipants(matchService.getMatchById(matchId).getNbPersonne());

            if (nbParticipants >= maxParticipants) {
                // Mettre à jour le statut du match en "confirmé"
                matchService.updateStatutMatch(matchId, "confirmé");
                System.out.println("Statut du match mis à jour en 'confirmé'.");
            }

            // Générer le PDF
            String pdfFilePath = "participation_" + matchId + "_" + userId + ".pdf";
            Match1 match = matchService.getMatchById(matchId);
            generatePDF(pdfFilePath, match, userId);

            // Proposer le téléchargement du PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le PDF");
            fileChooser.setInitialFileName("confirmation_participation.pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));

            Stage stage = (Stage) matchTable.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                // Copier le fichier PDF généré vers l'emplacement choisi par l'utilisateur
                Files.copy(Paths.get(pdfFilePath), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("PDF enregistré à : " + file.getAbsolutePath());

                // Supprimer le fichier PDF temporaire
                Files.deleteIfExists(Paths.get(pdfFilePath));

                // Ouvrir le fichier PDF automatiquement
                if (Desktop.isDesktopSupported()) { // Vérifier si la classe Desktop est supportée
                    Desktop desktop = Desktop.getDesktop();
                    if (file.exists()) { // Vérifier si le fichier existe
                        desktop.open(file); // Ouvrir le fichier avec l'application par défaut
                    }
                } else {
                    System.err.println("Desktop n'est pas supporté sur cette plateforme.");
                }
            }

            // Afficher un message de succès
            showSuccessAlert("Succès", "Votre participation au match a été enregistrée avec succès. Un fichier PDF a été généré.");

            // Rafraîchir la TableView
            matchTable.refresh();
        } catch (Exception e) {
            showErrorAlert("Erreur", "Une erreur s'est produite lors de l'enregistrement de votre participation.");
            e.printStackTrace();
        }
    }

    // Méthode pour générer un PDF
    private void generatePDF(String filePath, Match1 match, int userId) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Définir des styles
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.DARK_GRAY);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            // Ajouter un titre
            Paragraph title = new Paragraph("Confirmation de participation\n\n", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Ajouter une ligne de séparation
            document.add(new Paragraph("------------------------------------------------------------\n"));

            // Ajouter les détails du match
            Paragraph matchDetailsTitle = new Paragraph("Détails du match :", subtitleFont);
            matchDetailsTitle.setSpacingBefore(10);
            document.add(matchDetailsTitle);

            document.add(new Paragraph("Date : " + match.getDate(), normalFont));
            document.add(new Paragraph("Heure : " + match.getHeure(), normalFont));
            document.add(new Paragraph("Localisation : " + match.getLocalisation(), normalFont));
            document.add(new Paragraph("Terrain : " + match.getTerrain(), normalFont));
            document.add(new Paragraph("Type de sport : " + match.getTypeSport(), normalFont));

            // Ajouter les détails de l'utilisateur
            Paragraph userDetailsTitle = new Paragraph("\nDétails de l'utilisateur :", subtitleFont);
            userDetailsTitle.setSpacingBefore(10);
            document.add(userDetailsTitle);

            document.add(new Paragraph("ID Utilisateur : " + userId, normalFont));

            // Ajouter un message de remerciement
            Paragraph thankYouMessage = new Paragraph("\nMerci pour votre participation !", subtitleFont);
            thankYouMessage.setAlignment(Element.ALIGN_CENTER);
            thankYouMessage.setSpacingBefore(20);
            document.add(thankYouMessage);

            // Ajouter des informations supplémentaires
            Paragraph additionalInfo = new Paragraph(
                    "\n\nInformations importantes :\n" +
                            "- En cas de problème, veuillez contacter notre service client au 0123-456-789.\n" +
                            "- Un retard de plus de 15 minutes entraînera l'annulation automatique du match.\n" +
                            "- Merci de votre compréhension et à bientôt !", normalFont);
            additionalInfo.setAlignment(Element.ALIGN_LEFT);
            additionalInfo.setSpacingBefore(20);
            document.add(additionalInfo);

            // Ajouter une ligne de séparation
            document.add(new Paragraph("\n------------------------------------------------------------\n"));

            // Ajouter un pied de page
            Paragraph footer = new Paragraph("© 2023 VotreApplication. Tous droits réservés.", normalFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(10);
            document.add(footer);

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour calculer le nombre maximum de participants
    private int calculateMaxParticipants(int nbPersonne) {
        if (nbPersonne == 2) {
            return 1; // Une seule personne peut participer
        } else if (nbPersonne == 4) {
            return 3; // Trois personnes peuvent participer
        } else {
            return nbPersonne - 1; // Par défaut, nbPersonne - 1
        }
    }

    // Méthode pour envoyer un e-mail de confirmation
    private void sendConfirmationEmail(String email, Match1 match, int userId) throws Exception {
        // Désactiver la vérification stricte des adresses e-mail
        System.setProperty("mail.mime.address.strict", "false");

        // Paramètres du serveur SMTP
        String host = "smtp.gmail.com";
        String username = "nourhenekhazri519@gmail.com";
        String password = "yklc qmik oydl stho";

        // Propriétés pour la configuration du serveur SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Créer une session avec authentification
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Créer un message e-mail
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Confirmation de participation au match");

        // Corps du message
        String body = "Bonjour,\n\n"
                + "Votre participation au match suivant a été confirmée :\n"
                + "Date : " + match.getDate() + "\n"
                + "Heure : " + match.getHeure() + "\n"
                + "Localisation : " + match.getLocalisation() + "\n"
                + "Terrain : " + match.getTerrain() + "\n"
                + "Type de sport : " + match.getTypeSport() + "\n\n"
                + "Merci pour votre participation !\n\n"
                + "Cordialement,\n"
                + "Matchmate";

        // Ajouter le corps du message
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(body);

        // Générer le PDF
        String pdfFilePath = "participation_" + match.getId() + "_" + userId + ".pdf";
        generatePDF(pdfFilePath, match, userId);

        // Ajouter le PDF en pièce jointe
        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile(new File(pdfFilePath));

        // Combiner les parties du message
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);

        // Ajouter le contenu au message
        message.setContent(multipart);

        // Envoyer l'e-mail
        Transport.send(message);

        // Supprimer le fichier PDF temporaire
        Files.deleteIfExists(Paths.get(pdfFilePath));
    }

    // Méthode pour afficher une alerte d'erreur
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour afficher une alerte d'avertissement
    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour afficher une alerte de succès
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}