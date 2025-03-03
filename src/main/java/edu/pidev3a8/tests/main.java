package edu.pidev3a8.tests;

import edu.pidev3a8.entities.*;
import edu.pidev3a8.services.EmailService;
import edu.pidev3a8.tools.myConnection;
import edu.pidev3a8.utils.generateResetToken;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import java.time.LocalDate;

public class main {
    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Charge la bibliothèque native OpenCV
        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1); // Crée une matrice 3x3
        System.out.println("Mat = " + mat.dump());
        System.out.println(Core.VERSION);
        myConnection mc = myConnection.getInstance();

        User user = new User();
        user.setEmailUser("hbib@gmail.com");
        user.setPasswordUser("password123");
        user.setNomUser("aaaaaaaa");
        user.setPrenomUser("bensalem");
        user.setDateNaissanceUser(LocalDate.of(1991, 1, 1)); // Date de naissance valide
        user.setSexeUser(UserSexe.F);
        user.setTelephoneUser("123456789");
        user.setPhotoUser("photo.jpg");
        user.setDescriptionUser("Un utilisateur test");
        user.setMaxDistanceUser(10);
        user.setNiveau_joueur(User_Niveau.Debutant);
        user.setAdresseUser("123 Rue Test");
        user.setRole(UserRole.ADMIN);
        //user.setId_user("779JMT3498");
        user.setSalaire(500.5);
        user.setExperience(Experience.FOUR_YEARS);
        user.setIs_Premuim(true);


        String email = "doghmenesabee@gmail.com";
        String token = generateResetToken.generateResetToken();
        EmailService.sendResetEmail(email, token);


        //UserService us = new UserService();
        //us.addEntity(user);
        //us.updateEntity(user);
        // us.deleteEntity(user);
        // System.out.println(us.getEntities());


    }
}
