<?php
namespace App\Controller;

use App\Entity\Commande;
use Endroid\QrCode\Builder\BuilderInterface;
use Endroid\QrCode\Encoding\Encoding;
use Endroid\QrCode\ErrorCorrectionLevel;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\ResponseHeaderBag;
use Symfony\Component\Routing\Annotation\Route;

class FactureController extends AbstractController
{
    #[Route('/facture/commande/{id}/qr', name: 'facture_qr_code')]
    public function generateQrCode(
        Commande $commande, 
        BuilderInterface $qrCodeBuilder,
        Request $request
    ): Response {
        // Données à encoder dans le QR code
        $qrData = sprintf(
            "Commande #%s\nProduit: %s\nQuantité: %s\nPrix: %s DT",
            $commande->getId(),
            $commande->getProduit()->getNomProduit(),
            $commande->getQuantiteCommande(),
            $commande->getProduit()->getPrixProduit()
        );

        // Construction du QR code
        $qrCode = $qrCodeBuilder
            ->data($qrData)
            ->encoding(new Encoding('UTF-8'))
            ->size(300)
            ->margin(10)
            ->errorCorrectionLevel(ErrorCorrectionLevel::High)
            ->build();

        // Pour le téléchargement
        if ($request->query->get('download')) {
            $response = new Response($qrCode->getString());
            $disposition = $response->headers->makeDisposition(
                ResponseHeaderBag::DISPOSITION_ATTACHMENT,
                'facture-'.$commande->getId().'.png'
            );
            $response->headers->set('Content-Disposition', $disposition);
            $response->headers->set('Content-Type', $qrCode->getMimeType());
            return $response;
        }

        // Pour l'affichage
        return new Response($qrCode->getString(), 200, [
            'Content-Type' => $qrCode->getMimeType()
        ]);
    }
}