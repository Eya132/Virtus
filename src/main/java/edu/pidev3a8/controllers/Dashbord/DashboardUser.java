package edu.pidev3a8.controllers.Dashbord;

import edu.pidev3a8.entities.User;

public class DashboardUser {

    private User loggedInUser;

    // Méthode pour définir l'utilisateur connecté
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        // Vous pouvez maintenant utiliser loggedInUser pour afficher des informations dans l'interface utilisateur
        System.out.println("Utilisateur connecté (DashboardUser): " + loggedInUser.getNomUser());
    }
}