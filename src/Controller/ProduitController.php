<?php

namespace App\Controller;

use App\Entity\Produit;
use App\Form\ProduitType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use App\Services\ReferenceGenerator;
use App\Services\PdfGenerator;

final class ProduitController extends AbstractController
{
    #[Route('/produit/ProduitDashboard', name: 'app_produit_dashboard')]
    public function dashboard(EntityManagerInterface $em): Response
    {
        $produits = $em->getRepository(Produit::class)->findAll();
        
        return $this->render('produit/ProduitDashboard.html.twig', [
            'produits' => $produits,
        ]);
    }

    #[Route('/produit/newProduit', name: 'app_produit_new')]
    public function new(
        Request $request, 
        EntityManagerInterface $em,
        ReferenceGenerator $referenceGenerator
    ): Response {
        $produit = new Produit();
        $form = $this->createForm(ProduitType::class, $produit);
        
        // Retirer le champ refProduit du formulaire puisqu'il sera généré automatiquement
        $form->remove('refProduit');
    
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            // Générer la référence automatiquement
            $ref = $referenceGenerator->generateReference($produit->getNomProduit());
            $produit->setRefProduit($ref);
    
            // Gestion de l'upload d'image
            $imageFile = $form->get('imageProduit')->getData();
            if ($imageFile) {
                $newFilename = uniqid().'.'.$imageFile->guessExtension();
                $imageFile->move(
                    $this->getParameter('uploads_directory'),
                    $newFilename
                );
                $produit->setImageProduit($newFilename);
            }
    
            $em->persist($produit);
            $em->flush();
    
            $this->addFlash('success', 'Produit ajouté avec succès!');
            return $this->redirectToRoute('app_produit_dashboard', ['success' => true]);
        }
    
        return $this->render('produit/AjoutProduitBack.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/produit/{id}', name: 'app_produit_delete', methods: ['POST'])]
public function delete(Request $request, Produit $produit, EntityManagerInterface $em): Response
{
    if ($this->isCsrfTokenValid('delete'.$produit->getIdProduit(), $request->request->get('_token'))) {
        $em->remove($produit);
        $em->flush();
        $this->addFlash('success', 'Produit supprimé avec succès!');
    }

    return $this->redirectToRoute('app_produit_dashboard');
}

#[Route('/produit/{id}/editProduit', name: 'app_produit_edit')]
public function edit(
    Request $request, 
    Produit $produit, 
    EntityManagerInterface $em,
    ReferenceGenerator $referenceGenerator,
    \Psr\Log\LoggerInterface $logger
): Response {

    $logger->info('Début de la méthode edit');
    $ancienNom = $produit->getNomProduit();
    $ancienneImage = $produit->getImageProduit();
    
    $form = $this->createForm(ProduitType::class, $produit);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        $logger->info('Formulaire soumis et valide');
        if ($produit->getNomProduit() !== $ancienNom) {
            $nouvelleRef = $referenceGenerator->generateReference($produit->getNomProduit());
            $produit->setRefProduit($nouvelleRef);
        }

        $keepImage = $request->request->get('keep_image', false);
        $logger->info('Keep image value: '.($keepImage ? 'true' : 'false'));
        $file = $form->get('imageProduit')->getData();
        
        if ($keepImage) {
            // Garder l'image existante
            $produit->setImageProduit($ancienneImage);
        } else {
            if ($file) {
                // Upload nouvelle image
                $filename = uniqid().'.'.$file->guessExtension();
                $file->move($this->getParameter('uploads_directory'), $filename);
                $produit->setImageProduit($filename);
            } else {
                // Pas de nouvelle image et case décochée
                $produit->setImageProduit(null);
            }
        }

        $em->flush();
        $this->addFlash('success', 'Produit mis à jour avec succès!');
        return $this->redirectToRoute('app_produit_dashboard');
    }

    return $this->render('produit/ModifProduitBack.html.twig', [
        'form' => $form->createView(),
        'produit' => $produit,
    ]);
}


#[Route('/produits/front', name: 'app_produit_front')]
public function frontProduit(EntityManagerInterface $em): Response
{
    $produits = $em->getRepository(Produit::class)->findAll();
    
    return $this->render('produit/produitFront.html.twig', [
        'produits' => $produits,
        'current_page' => 'produits',
        'page_title' => 'Produits - MatchMate'
    ]);
}
#[Route('/produit/export-pdf', name: 'app_produit_export_pdf')]
public function exportToPdf(EntityManagerInterface $em, PdfGenerator $pdfGenerator): Response
{
    // Récupérer tous les produits
    $produits = $em->getRepository(Produit::class)->findAll();
    
    // Rendre le template Twig en HTML
    $html = $this->renderView('produit/export_pdf.html.twig', [
        'produits' => $produits,
        'date_export' => new \DateTime()
    ]);
    
    // Générer et retourner le PDF
    return $pdfGenerator->generatePdfFromHtml($html, 'liste_produits_'.date('Y-m-d').'.pdf');
}




}
