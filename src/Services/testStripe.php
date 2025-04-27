<?php
require_once 'vendor/autoload.php';

\Stripe\Stripe::setApiKey(getenv('STRIPE_SECRET_KEY'));

try {
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

    echo "Session created successfully:\n";
    print_r($session);
} catch (\Exception $e) {
    echo "Error:\n";
    echo $e->getMessage();
}