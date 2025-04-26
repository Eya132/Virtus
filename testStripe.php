<?php
require __DIR__.'/vendor/autoload.php';

// Charge les variables d'environnement
(new Symfony\Component\Dotenv\Dotenv())->bootEnv(__DIR__.'/.env');

// Configuration de Stripe
\Stripe\Stripe::setApiKey($_ENV['STRIPE_SECRET_KEY']);

try {
    echo "Tentative de création d'une session Stripe...\n";
    
    $session = \Stripe\Checkout\Session::create([
        'payment_method_types' => ['card'],
        'line_items' => [[
            'price_data' => [
                'currency' => 'eur',
                'product_data' => ['name' => 'Test Product'],
                'unit_amount' => 1000, // 10.00 EUR
            ],
            'quantity' => 1,
        ]],
        'mode' => 'payment',
        'success_url' => 'https://example.com/success',
        'cancel_url' => 'https://example.com/cancel',
    ]);

    echo "Session créée avec succès!\n";
    echo "ID de session: " . $session->id . "\n";
    echo "URL de paiement: " . $session->url . "\n";
    
} catch (\Exception $e) {
    echo "ERREUR:\n";
    echo $e->getMessage() . "\n";
    
    if ($e instanceof \Stripe\Exception\ApiErrorException) {
        echo "Détails Stripe:\n";
        echo json_encode($e->getJsonBody(), JSON_PRETTY_PRINT) . "\n";
    }
}