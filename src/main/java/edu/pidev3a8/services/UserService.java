package edu.pidev3a8.services;

import edu.pidev3a8.entities.*;
import edu.pidev3a8.interfaces.IService;
import edu.pidev3a8.tools.myConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Date;
import java.util.*;

import java.sql.*;
import java.time.LocalDate;

import edu.pidev3a8.utils.IDGenerator;


public class UserService implements IService<User> {
    List<User> users;


    @Override
    public void addEntity(User user) {
        // Appeler la méthode addEntity avec un statut par défaut
        addEntity(user, UserStatus.PENDING); // Par défaut, le statut est PENDING
    }

    public void addEntity(User user, UserStatus status) {
        try {
            // Vérification si l'utilisateur est un joueur
            if (user.getRole() == UserRole.PLAYER) {
                if (user.getSalaire() != 0 || user.getExperience() != null) {
                    throw new IllegalArgumentException("Un joueur ne peut pas remplir les champs salaire et experience.");
                }
            }

            // Vérification si l'utilisateur est un nutritionniste
            if (user.getRole() == UserRole.NUTRITIONIST) {
                if (user.getIs_Premuim() != false || user.getNiveau_joueur() != null) {
                    throw new IllegalArgumentException("Un nutritionniste ne peut pas remplir les champs is_Premium et niveau_joueur.");
                }
            }

            // Définir le statut de l'utilisateur
            user.setStatus(status); // Utiliser le statut passé en paramètre
            System.out.println("Statut de l'utilisateur : " + user.getStatus());

            // Hachage du mot de passe
            String hashedPassword = BCrypt.hashpw(user.getPasswordUser(), BCrypt.gensalt());
            String userId = IDGenerator.generateCustomID(user.getSexeUser(), user.getRole());
            user.setId_user(userId);

            // Requête SQL avec le champ status
            String query = "INSERT INTO User (email_user, password_user, nom_user, prenom_user, dateNaissance_user, " +
                    "sexe_user, telephone_user, photo_user, description_user, maxDistance_user, adresse_user, " +
                    "role, niveau_joueur, id_user, experience, salaire, is_premium, piece_jointe, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = new myConnection().getCnx().prepareStatement(query);
            user.setId_user(userId);

            // Remplissage des paramètres de la requête
            pst.setString(1, user.getEmailUser());
            pst.setString(2, hashedPassword);
            pst.setString(3, user.getNomUser());
            pst.setString(4, user.getPrenomUser());
            pst.setDate(5, Date.valueOf(user.getDateNaissanceUser()));
            pst.setString(6, user.getSexeUser().toString());
            pst.setString(7, user.getTelephoneUser());
            pst.setString(8, user.getPhotoUser());
            pst.setString(9, user.getDescriptionUser());
            pst.setInt(10, user.getMaxDistanceUser());
            pst.setString(11, user.getAdresseUser());
            pst.setString(12, user.getRole().toString());

            // Gestion de la valeur null pour niveau_joueur
            if (user.getNiveau_joueur() != null) {
                pst.setString(13, user.getNiveau_joueur().toString());
            } else {
                pst.setNull(13, Types.VARCHAR); // ou pst.setString(13, null);
            }

            pst.setString(14, userId);

            // Gestion de la valeur null pour experience
            if (user.getExperience() != null) {
                pst.setString(15, user.getExperience().toString());
            } else {
                pst.setNull(15, Types.VARCHAR); // ou pst.setString(15, null);
            }

            pst.setDouble(16, user.getSalaire());
            pst.setBoolean(17, user.getIs_Premuim());
            pst.setString(18, user.getPiece_jointe());
            pst.setString(19, user.getStatus().toString()); // Ajouter le statut
            System.out.println("Statut passé à la requête SQL : " + user.getStatus().toString());

            // Exécution de la requête
            pst.executeUpdate();
            System.out.println("Utilisateur ajouté avec succès !");

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    @Override
    public void deleteEntity(User user) {

        try {
            String query = "DELETE FROM User WHERE id_user = ?";
            PreparedStatement pst = new myConnection().getCnx().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);            pst.setString(1, user.getId_user());
            pst.executeUpdate();
            System.out.println("Utilisateur supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEntity(User user) {
        try {
            // Vérification des contraintes métier
            if (user.getRole() == UserRole.PLAYER) {
                if (user.getSalaire() != 0 || user.getExperience() != null) {
                    throw new IllegalArgumentException("Un joueur ne peut pas remplir les champs salaire et expérience.");
                }
            }

            if (user.getRole() == UserRole.NUTRITIONIST) {
                if (user.getIs_Premuim() || user.getNiveau_joueur() != null) {
                    throw new IllegalArgumentException("Un nutritionniste ne peut pas remplir les champs is_Premium et niveau_joueur.");
                }
            }

            // Hachage du mot de passe
            String hashedPassword = BCrypt.hashpw(user.getPasswordUser(), BCrypt.gensalt());

            // Requête SQL
            String query = "UPDATE User SET email_user = ?, password_user = ?, nom_user = ?, prenom_user = ?, " +
                    "dateNaissance_user = ?, sexe_user = ?, telephone_user = ?, photo_user = ?, description_user = ?, " +
                    "maxDistance_user = ?, adresse_user = ?, role = ?, niveau_joueur = ?, experience = ?, salaire = ?, " +
                    "is_premium = ? WHERE id_user = ?";

            Connection conn = new myConnection().getCnx();
            PreparedStatement pst = conn.prepareStatement(query);

            // Paramètres de la requête
            pst.setString(1, user.getEmailUser());
            pst.setString(2, hashedPassword);
            pst.setString(3, user.getNomUser());
            pst.setString(4, user.getPrenomUser());
            pst.setDate(5, (user.getDateNaissanceUser() != null) ? Date.valueOf(user.getDateNaissanceUser()) : null);
            pst.setString(6, (user.getSexeUser() != null) ? user.getSexeUser().toString() : null);
            pst.setString(7, user.getTelephoneUser());
            pst.setString(8, user.getPhotoUser());
            pst.setString(9, user.getDescriptionUser());
            pst.setInt(10, user.getMaxDistanceUser());
            pst.setString(11, user.getAdresseUser());
            pst.setString(12, (user.getRole() != null) ? user.getRole().toString() : null);
            pst.setString(13, (user.getNiveau_joueur() != null) ? user.getNiveau_joueur().toString() : null);
            pst.setString(14, (user.getExperience() != null) ? user.getExperience().toString() : null);
            pst.setDouble(15, user.getSalaire());
            pst.setBoolean(16, user.getIs_Premuim());
            pst.setString(17, user.getId_user());

            // Exécution de la requête
            try {
                pst.executeUpdate();
                System.out.println("Utilisateur mis à jour avec succès !");
            } catch (SQLException e) {
                System.out.println("Erreur SQL lors de la mise à jour de l'utilisateur : " + e.getMessage());
                e.printStackTrace(); // Affichez la stack trace pour plus de détails
            }

            // Si autoCommit est désactivé, valider la transaction
            if (!conn.getAutoCommit()) {
                conn.commit();
                System.out.println("Transaction validée !");
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la mise à jour de l'utilisateur : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    @Override
    public List<User> getEntities() {
        List<User> users = new ArrayList<>();
        try {
            String query = "SELECT * FROM user";
            Statement pst = new myConnection().getCnx().createStatement();
            ResultSet rs = pst.executeQuery(query);

            while (rs.next()) {
                User user = new User();
                user.setId_user(rs.getString("id_user"));
                user.setEmailUser(rs.getString("email_user"));
                user.setPasswordUser(rs.getString("password_user"));
                user.setNomUser(rs.getString("nom_user"));
                user.setPrenomUser(rs.getString("prenom_user"));
                user.setDateNaissanceUser(rs.getDate("dateNaissance_user").toLocalDate());
                user.setSexeUser(UserSexe.valueOf(rs.getString("sexe_user")));
                user.setTelephoneUser(rs.getString("telephone_user"));
                user.setDescriptionUser(rs.getString("description_user"));
                user.setMaxDistanceUser(rs.getInt("maxDistance_user"));
                user.setAdresseUser(rs.getString("adresse_user"));
                user.setIs_Premuim(rs.getBoolean("is_premium"));
                user.setSalaire(rs.getDouble("salaire"));
                user.setRole(UserRole.valueOf(rs.getString("role")));
                user.setNiveau_joueur(User_Niveau.valueOf(rs.getString("niveau_joueur")));

                // Gestion de photoUser
                String photoUser = rs.getString("photo_user");
                if (photoUser != null) {
                    user.setPhotoUser(photoUser);
                } else {
                    user.setPhotoUser(""); // Valeur par défaut
                }

                // Gestion de experience
                String experienceStr = rs.getString("experience");
                if (experienceStr != null) {
                    user.setExperience(Experience.valueOf(experienceStr));
                } else {
                    user.setExperience(null); // Ou une valeur par défaut
                }

                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public List<User> ListEntities() {
        List<User> users = new ArrayList<>();
        try {
            String query = "SELECT id_user, photo_user, nom_user, prenom_user, role FROM user";
            Statement pst = new myConnection().getCnx().createStatement();
            ResultSet rs = pst.executeQuery(query);

            while (rs.next()) {
                User user = new User();
                user.setId_user(rs.getString("id_user"));
                user.setNomUser(rs.getString("nom_user"));
                user.setPrenomUser(rs.getString("prenom_user"));
                user.setRole(UserRole.valueOf(rs.getString("role")));

                // Gestion de photoUser
                String photoUser = rs.getString("photo_user");
                if (photoUser != null) {
                    user.setPhotoUser(photoUser);
                } else {
                    user.setPhotoUser(""); // Valeur par défaut
                }

                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public User getUserById(String id) {
        User user = null;
        try {
            String query = "SELECT * FROM User WHERE id_user = ?";
            PreparedStatement pst = new myConnection().getCnx().prepareStatement(query);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId_user(rs.getString("id_user"));
                user.setNomUser(rs.getString("nom_user"));
                user.setPrenomUser(rs.getString("prenom_user"));
                user.setEmailUser(rs.getString("email_user"));
                user.setPasswordUser(rs.getString("password_user"));

                // Gérer la date de naissance (peut être null)
                java.sql.Date dateNaissance = rs.getDate("dateNaissance_user");
                if (dateNaissance != null) {
                    user.setDateNaissanceUser(dateNaissance.toLocalDate());
                } else {
                    user.setDateNaissanceUser(null); // Ou une valeur par défaut
                }

                // Gérer le sexe (peut être null)
                String sexe = rs.getString("sexe_user");
                if (sexe != null) {
                    user.setSexeUser(UserSexe.valueOf(sexe));
                } else {
                    user.setSexeUser(null); // Ou une valeur par défaut
                }

                user.setTelephoneUser(rs.getString("telephone_user"));
                user.setAdresseUser(rs.getString("adresse_user"));

                // Gérer le rôle (peut être null)
                String role = rs.getString("role");
                if (role != null) {
                    user.setRole(UserRole.valueOf(role));
                } else {
                    user.setRole(null); // Ou une valeur par défaut
                }

                // Gérer le niveau joueur (peut être null)
                String niveauJoueur = rs.getString("niveau_joueur");
                if (niveauJoueur != null) {
                    user.setNiveau_joueur(User_Niveau.valueOf(niveauJoueur));
                } else {
                    user.setNiveau_joueur(null); // Ou une valeur par défaut
                }

                // Gérer l'expérience (peut être null)
                String experience = rs.getString("experience");
                if (experience != null) {
                    user.setExperience(Experience.valueOf(experience));
                } else {
                    user.setExperience(null); // Ou une valeur par défaut
                }

                // Gérer le salaire (peut être null)
                double salaire = rs.getDouble("salaire");
                if (!rs.wasNull()) { // Vérifie si la valeur était NULL dans la base de données
                    user.setSalaire(salaire);
                } else {
                    user.setSalaire(null); // Ou une valeur par défaut
                }

                user.setIs_Premuim(rs.getBoolean("is_premium"));
                user.setDescriptionUser(rs.getString("description_user"));

                // Gérer la distance maximale (peut être null)
                int maxDistance = rs.getInt("maxDistance_user");
                if (!rs.wasNull()) { // Vérifie si la valeur était NULL dans la base de données
                    user.setMaxDistanceUser(maxDistance);
                } else {
                    user.setMaxDistanceUser(null); // Ou une valeur par défaut
                }

                user.setPhotoUser(rs.getString("photo_user"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la récupération de l'utilisateur : " + e.getMessage());
        }
        return user;
    }
    public User getUserByEmail(String email) {
        User user = null;
        try {
            String query = "SELECT * FROM User WHERE email_user = ?";
            PreparedStatement pst = new myConnection().getCnx().prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId_user(rs.getString("id_user"));
                user.setEmailUser(rs.getString("email_user"));
                user.setPasswordUser(rs.getString("password_user"));
                user.setNomUser(rs.getString("nom_user"));
                user.setPrenomUser(rs.getString("prenom_user"));
                user.setDateNaissanceUser(rs.getDate("dateNaissance_user").toLocalDate());
                user.setSexeUser(UserSexe.valueOf(rs.getString("sexe_user")));
                user.setTelephoneUser(rs.getString("telephone_user"));
                user.setAdresseUser(rs.getString("adresse_user"));
                user.setRole(UserRole.valueOf(rs.getString("role")));

                // Gestion de niveau_joueur (vérification de null)
                String niveauJoueurStr = rs.getString("niveau_joueur");
                if (niveauJoueurStr != null) {
                    user.setNiveau_joueur(User_Niveau.valueOf(niveauJoueurStr));
                } else {
                    user.setNiveau_joueur(null); // Ou une valeur par défaut
                }

                // Gestion de experience (vérification de null)
                String experienceStr = rs.getString("experience");
                if (experienceStr != null) {
                    user.setExperience(Experience.valueOf(experienceStr));
                } else {
                    user.setExperience(null); // Ou une valeur par défaut
                }

                user.setSalaire(rs.getDouble("salaire"));
                user.setIs_Premuim(rs.getBoolean("is_premium"));
                user.setDescriptionUser(rs.getString("description_user"));
                user.setMaxDistanceUser(rs.getInt("maxDistance_user"));
                user.setPhotoUser(rs.getString("photo_user"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la récupération de l'utilisateur : " + e.getMessage());
        }
        return user;
    }

    private Map<String, Integer> getAgeStatistics() {
        Map<String, Integer> ageStatistics = new HashMap<>();
        try {
            String query = "SELECT " +
                    "CASE " +
                    "    WHEN TIMESTAMPDIFF(YEAR, dateNaissanceUser, CURDATE()) BETWEEN 12 AND 17 THEN '12-17 ans' " +
                    "    WHEN TIMESTAMPDIFF(YEAR, dateNaissanceUser, CURDATE()) BETWEEN 18 AND 25 THEN '18-25 ans' " +
                    "    WHEN TIMESTAMPDIFF(YEAR, dateNaissanceUser, CURDATE()) BETWEEN 26 AND 35 THEN '26-35 ans' " +
                    "    WHEN TIMESTAMPDIFF(YEAR, dateNaissanceUser, CURDATE()) BETWEEN 36 AND 50 THEN '36-50 ans' " +
                    "    ELSE '50+ ans' " +
                    "END AS trancheAge, " +
                    "COUNT(*) AS nombreUtilisateurs " +
                    "FROM user " +
                    "GROUP BY trancheAge";

            Statement st = myConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String trancheAge = rs.getString("trancheAge");
                int nombreUtilisateurs = rs.getInt("nombreUtilisateurs");
                ageStatistics.put(trancheAge, nombreUtilisateurs);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des statistiques sur l'âge des utilisateurs : " + e.getMessage());
        }
        return ageStatistics;
    }

    public List<User> getAllNutritionists() {
        List<User> nutritionists = new ArrayList<>();
        try {
            String query = "SELECT * FROM User WHERE role = ?"; // Récupérer tous les nutritionnistes
            PreparedStatement pst = new myConnection().getCnx().prepareStatement(query);
            pst.setString(1, UserRole.NUTRITIONIST.toString());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId_user(rs.getString("id_user"));
                user.setEmailUser(rs.getString("email_user"));
                user.setNomUser(rs.getString("nom_user"));
                user.setPrenomUser(rs.getString("prenom_user"));
                user.setRole(UserRole.valueOf(rs.getString("role")));
                user.setPiece_jointe(rs.getString("piece_jointe"));
                user.setStatus(UserStatus.valueOf(rs.getString("status"))); // Récupérer le statut
                nutritionists.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la récupération des nutritionnistes : " + e.getMessage());
        }
        return nutritionists;
    }

    public void validateNutritionist(User user) {
        try {
            String query = "UPDATE User SET status = ? WHERE id_user = ?";
            PreparedStatement pst = new myConnection().getCnx().prepareStatement(query);
            pst.setString(1, UserStatus.VALIDATED.toString()); // Mettre à jour le statut à "VALIDATED"
            pst.setString(2, user.getId_user());
            pst.executeUpdate();
            System.out.println("Nutritionniste validé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la validation du nutritionniste : " + e.getMessage());
        }
        System.out.println("Statut de l'utilisateur : " + user.getStatus());
    }

    private boolean executeQueryAndCheckExistence(String query, String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = myConnection.getInstance().getCnx(); // Remplacez par votre méthode pour obtenir une connexion

            // Prépare la requête SQL
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email); // Remplace le paramètre "email" dans la requête

            // Exécute la requête
            resultSet = preparedStatement.executeQuery();

            // Vérifie si le résultat contient au moins une ligne
            if (resultSet.next()) {
                int count = resultSet.getInt(1); // Récupère le résultat de COUNT(*)
                return count > 0; // Retourne true si l'email existe
            }

            return false; // Si aucun résultat n'est trouvé
        } catch (Exception e) {
            e.printStackTrace();
            return false; // En cas d'erreur, retourne false
        } finally {
            // Ferme les ressources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }







}

