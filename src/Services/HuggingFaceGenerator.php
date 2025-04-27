<?php

namespace App\Services;

use Symfony\Contracts\HttpClient\HttpClientInterface;
use Symfony\Component\DependencyInjection\ParameterBag\ParameterBagInterface;

class HuggingFaceGenerator
{
    private $client;
    private $apiKey;
    private $model;

    public function __construct(HttpClientInterface $client, ParameterBagInterface $params)
    {
        $this->client = $client;
        $this->apiKey = $params->get('HUGGINGFACE_API_KEY');
        $this->model = $params->get('HUGGINGFACE_MODEL');
    }

    public function generateProductDescription(string $productName, string $currentDescription = ''): string
    {
        $response = $this->client->request(
            'POST',
            "https://api-inference.huggingface.co/models/{$this->model}",
            [
                'headers' => [
                    'Authorization' => "Bearer {$this->apiKey}",
                    'Content-Type' => 'application/json',
                    'Accept' => 'application/json'
                ],
                'json' => [
                    'inputs' => $this->buildPrompt($productName, $currentDescription),
                    'parameters' => [
                        'max_length' => 200,
                        'temperature' => 0.7
                    ]
                ],
                'timeout' => 30
            ]
        );

        $data = $response->toArray();
        return $data[0]['generated_text'] 
        ?? $data['generated_text'] 
        ?? $this->getFallbackDescription($productName);
    }

    private function buildPrompt(string $productName, string $currentDescription): string
    {
        return sprintf(
            "Génère une description e-commerce en français pour '%s'. Doit contenir :\n"
            . "- 3 caractéristiques techniques\n"
            . "- 2 avantages clients\n"
            . "- Ton persuasif mais professionnel\n\n"
            . "Description existante : %s",
            $productName,
            $currentDescription ?: "Aucune"
        );
    }

    private function getFallbackDescription(string $productName): string
    {
        return "Découvrez notre produit {$productName}.\n\n"
             . "Caractéristiques :\n"
             . "- Matériaux haute qualité\n"
             . "- Conception durable\n"
             . "- Design ergonomique\n\n"
             . "Avantages :\n"
             . "- Satisfaction garantie\n"
             . "- Livraison rapide";
    }
}