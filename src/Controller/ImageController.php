<?php

namespace App\Controller;

use App\Repository\UserRepository;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;

class ImageController extends AbstractController
{#[Route('/user/photo/{id}', name: 'user_photo')]
    public function userPhoto(int $id, UserRepository $userRepository): Response
    {
        try {
            $user = $userRepository->find($id);

            if (!$user || !$user->getPhotoUser()) {
                return $this->redirectToRoute('default_user_photo');
            }
    
            $photoPath = $this->getSafePhotoPath($user->getPhotoUser());

            
            if (!$photoPath || !file_exists($photoPath)) {
                return $this->redirectToRoute('default_user_photo');
            }
    
            return new Response(
                file_get_contents($photoPath),
                200,
                [
                    'Content-Type' => mime_content_type($photoPath),
                    'Content-Disposition' => 'inline; filename="user-photo"'
                ]
            );
        } catch (\Exception $e) {
            // Log l'erreur
            error_log("User photo error: " . $e->getMessage());
            return $this->redirectToRoute('default_user_photo');
        }
    }
    
    private function getSafePhotoPath(string $relativePath): ?string
{
    $publicDir = $this->getParameter('kernel.project_dir') . '/public';
    $fullPath = realpath($publicDir . $relativePath);

    if (!$fullPath || strpos($fullPath, realpath($publicDir)) !== 0) {
        return null;
    }

    return $fullPath;
}



}

