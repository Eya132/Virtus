package GestionRegime.services;

public class PlayerService {

    private MailService mailService;

    public PlayerService() {
        this.mailService = new MailService();
    }

    public void createPlayerAccount(String nom, String prenom, int age, double poids) {
        // Simule la création d'un compte joueur
        System.out.println("Compte joueur créé : " + nom + " " + prenom);

        // Prépare le contenu de l'e-mail
        String subject = "Nouveau compte joueur créé";
        String content = "Informations du joueur :\n"
                + "Nom : " + nom + "\n"
                + "Prénom : " + prenom + "\n"
                + "Âge : " + age + "\n"
                + "Poids : " + poids + " kg";

        // Envoie l'e-mail
        mailService.sendMail(subject, content);
    }
}