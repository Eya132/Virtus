<?php
namespace App\Controller;

use App\Entity\Produit;
use App\Repository\ProduitRepository;
use Psr\Log\LoggerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;
use Symfony\Component\HttpFoundation\Response;

class PaymentController extends AbstractController
{
    private LoggerInterface $logger;

    public function __construct(LoggerInterface $logger)
    {
        $this->logger = $logger;
    }
    #[Route('/api/payment/create-session', name: 'payment_create_session', methods: ['POST'])]
    public function createSession(Request $request, ProduitRepository $produitRepo): JsonResponse
    {
        // Loggueur de débogage
        file_put_contents('payment.log', date('[Y-m-d H:i:s]')." Requête reçue\n", FILE_APPEND);
    
        try {
            $data = json_decode($request->getContent(), true);
            file_put_contents('payment.log', print_r($data, true), FILE_APPEND);
    
            if (empty($data['product_id'])) {
                throw new \Exception('Product ID manquant');
            }
    
            $produit = $produitRepo->find($data['product_id']);
            if (!$produit) {
                throw new \Exception('Produit introuvable');
            }
    
            \Stripe\Stripe::setApiKey($_ENV['STRIPE_SECRET_KEY']);
            $session = \Stripe\Checkout\Session::create([
                'payment_method_types' => ['card'],
                'line_items' => [[
                    'price_data' => [
                        'currency' => 'eur',
                        'product_data' => ['name' => $produit->getNomProduit()],
                        'unit_amount' => (int)($produit->getPrixProduit() * 100),
                    ],
                    'quantity' => $data['quantity'] ?? 1,
                ]],
                'mode' => 'payment',
                'success_url' => $request->getSchemeAndHttpHost().$this->generateUrl('payment_success'),
                'cancel_url' => $request->getSchemeAndHttpHost().$this->generateUrl('payment_cancel'),
            ]);
    
            return new JsonResponse(['id' => $session->id]);
    
        } catch (\Exception $e) {
            file_put_contents('payment.log', "ERREUR: ".$e->getMessage()."\n", FILE_APPEND);
            return new JsonResponse(['error' => $e->getMessage()], 400);
        }
    }



    #[Route('/payment/success', name: 'payment_success')]
    public function success(Request $request): Response
    {
        $sessionId = $request->query->get('session_id');
        
        try {
            \Stripe\Stripe::setApiKey($_ENV['STRIPE_SECRET_KEY']);
            $session = \Stripe\Checkout\Session::retrieve($sessionId);
            
            // Ici vous pourriez enregistrer la commande en base de données
            
            return $this->render('payment/success.html.twig', [
                'session' => $session,
                'redirectUrl' => $this->generateUrl('app_commande_commandeFront')
            ]);

        } catch (\Exception $e) {
            return $this->redirectToRoute('payment_cancel');
        }
    }

    #[Route('/payment/cancel', name: 'payment_cancel')]
    public function cancel(): Response
    {
        return $this->render('payment/cancel.html.twig', [
            'productsUrl' => $this->generateUrl('app_produit_front')
        ]);
    }
}