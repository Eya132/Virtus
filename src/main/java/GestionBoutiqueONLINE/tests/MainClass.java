package GestionBoutiqueONLINE.tests;

import GestionBoutiqueONLINE.entities.Commande;
import GestionBoutiqueONLINE.entities.Produit;
import GestionBoutiqueONLINE.services.CommandeService;
import GestionBoutiqueONLINE.services.ProduitService;
import GestionBoutiqueONLINE.tools.MyConnection;

import java.util.List;

public class MainClass {

    public static void main(String[] args) {
        MyConnection nc = MyConnection.getInstance();
        ProduitService ps = new ProduitService();

        // 🔹 Ajout d'un produit
        Produit p1 = new Produit("Raquette Pro", "Raquette de tennis haut de gamme", 200, 10,"image.png");
        Produit p2 = new Produit("Raquette rouge", "padel one", 370, 20,"sabee.jpg");
        Produit p4 = new Produit("cb", "dgh", 370, 20,"hbib.png");

        Produit update =new Produit();
        //update.setNomProduit("amen");
        //update.setDescriptionProduit("amen");
        //update.setQuantiteProduit(20);
        //update.setPrixProduit(1000);
        //ps.updateProduit(9,update);
        ps.addProduit(p4);
        ps.addProduit(p2);
        System.out.println(ps.getAllDataProduit());
         //Produit produisupp = new Produit();
         //produisupp.setIdProduit(7);
         //ps.deleteProduit(produisupp);


        //System.out.println("***************************");
        //CommandeService commandeService = new CommandeService();
        //Commande nouvelleCommande = new Commande(2, 11, 101, Commande.StatusCommande.EN_ATTENTE);
        //commandeService.addCommande(nouvelleCommande);
        //Commande nouvelleCommande2 = new Commande(6, 9, 11, Commande.StatusCommande.ANNULÉE);
        //commandeService.addCommande(nouvelleCommande2);



        //Commande commandeUpdate = new Commande();
        //commandeUpdate.setQuantiteCommande(50000);
        //commandeUpdate.setStatusCommande(Commande.StatusCommande.LIVRÉE); // Exemple de changement de statut
        //commandeService.updateCommande(4, commandeUpdate);

        //commandeService.deleteCommande(4);


        //System.out.println(commandeService.getAllDataCommande());



    }
}
