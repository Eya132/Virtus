<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Form\Extension\Core\Type\FormType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use App\Entity\User;
use App\Form\UserType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\Security\Http\Authentication\AuthenticationUtils;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

final class AuthController extends AbstractController
{
    #[Route('/auth', name: 'app_auth')]
    public function index(Request $request, EntityManagerInterface $entityManager, UserPasswordHasherInterface $passwordHasher): Response
    {
        $user = new User();
        $form = $this->createForm(UserType::class, $user);

        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Handle piece_jointe file upload
            $pieceJointeFile = $form->get('piece_jointe')->getData();
            if ($pieceJointeFile) {
                $newFilename = uniqid() . '.' . $pieceJointeFile->guessExtension();
                try {
                    $pieceJointeFile->move(
                        $this->getParameter('uploads_directory'),
                        $newFilename
                    );
                    $user->setPieceJointe($newFilename);
                } catch (FileException $e) {
                    $this->addFlash('error', 'Erreur lors du téléchargement de la pièce jointe.');
                    return $this->render('/auth/register.html.twig', [
                        'form' => $form->createView(),
                    ]);
                }
            } else {
                $this->addFlash('error', 'Veuillez sélectionner une pièce jointe.');
                return $this->render('/auth/register.html.twig', [
                    'form' => $form->createView(),
                ]);
            }

            // Hash the password before saving
            $hashedPassword = $passwordHasher->hashPassword($user, $user->getPasswordUser());
            $user->setPasswordUser($hashedPassword);

            // Persist and flush the user to the database
            $entityManager->persist($user);
            $entityManager->flush();

            // Add a success flash message
            $this->addFlash('success', 'Inscription réussie !');

            return $this->redirectToRoute('app_login');
        }

        return $this->render('/auth/register.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/login', name: 'app_login')]
public function login(AuthenticationUtils $authenticationUtils): Response
{
    if ($this->getUser()) {
        return $this->redirectToRoute('app_home');
    }

    $error = $authenticationUtils->getLastAuthenticationError();
    $lastUsername = $authenticationUtils->getLastUsername();

    // Debug: Afficher les valeurs reçues
    dump($lastUsername);
    dump($error);

    return $this->render('auth/login.html.twig', [
        'last_username' => $lastUsername,
        'error' => $error
    ]);
}


    #[Route('/logout', name: 'app_logout')]
    public function logout(): void
    {
        // Symfony will intercept this route and handle the logout process
    }
}