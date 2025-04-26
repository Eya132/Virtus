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
    #[Route('/create-checkout-session', name: 'create_checkout_session', methods: ['POST'])]
public function createCheckoutSession(Request $request, ProduitRepository $produitRepository): JsonResponse
{
    try {
        $data = json_decode($request->getContent(), true);
        $productId = $data['product_id'] ?? null;
        $quantity = $data['quantity'] ?? 1;

        if (!$productId) {
            throw new \InvalidArgumentException('Product ID is required');
        }

        $produit = $produitRepository->find($productId);
        if (!$produit) {
            throw $this->createNotFoundException('Product not found');
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
                'quantity' => $quantity,
            ]],
            'mode' => 'payment',
            'success_url' => $this->generateUrl('payment_success', [], UrlGeneratorInterface::ABSOLUTE_URL),
            'cancel_url' => $this->generateUrl('payment_cancel', [], UrlGeneratorInterface::ABSOLUTE_URL),
        ]);

        return new JsonResponse([
            'id' => $session->id,
            'url' => $session->url
        ]);

    } catch (\Exception $e) {
        return new JsonResponse([
            'error' => $e->getMessage()
        ], Response::HTTP_BAD_REQUEST);
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
            'productsUrl' => $this->generateUrl('app_produits')
        ]);
    }
}