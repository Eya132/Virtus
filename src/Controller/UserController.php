<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Repository\UserRepository;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\JsonResponse;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use App\Entity\User;
use App\Form\UserType;

class UserController extends AbstractController
{
    #[Route('/dashboard/user', name: 'app_dashboard_user')]
    public function index(UserRepository $userRepository): Response
    {
        // Données fictives - À remplacer par la récupération depuis votre base de données
        $users = $userRepository->findAll();
        // Données fictives - À remplacer par la récupération depuis votre base de données

        return $this->render('user/dashboard.html.twig', [
            'users' => $users,
        ]);
        

    }

    #[Route('/dashboard/user/new', name: 'app_dashboard_user_new')]
    public function new(Request $request, EntityManagerInterface $entityManager, UserPasswordHasherInterface $passwordHasher): Response
    {
        $user = new User();
        $user->setIsActive(true);
        $form = $this->createForm(UserType::class, $user , [
            'is_edit' => true
        ] );
        $form->handleRequest($request);

        $photoFile = $form->get('photo_user')->getData(); // Handle photo_user upload

        if ($photoFile) {
            $newFilename = uniqid().'.'.$photoFile->guessExtension();
            $photoFile->move(
                $this->getParameter('uploads_directory'),
                $newFilename
            );
            $user->setPhotoUser($newFilename);
        } else {
            $user->setPhotoUser('default_photo.png'); // Default photo
        }

        $pieceJointeFile = $form->get('piece_jointe')->getData();
        if ($pieceJointeFile) {
            $newFilename = uniqid().'.'.$pieceJointeFile->guessExtension();
            $pieceJointeFile->move(
                $this->getParameter('uploads_directory'),
                $newFilename
            );
            $user->setPieceJointe($newFilename);
        }

        if ($form->isSubmitted() && $form->isValid()) {
            // Ne met à jour le mot de passe que si fourni
            if ($form->get('password_user')->getData()) {
                $hashedPassword = $passwordHasher->hashPassword(
                    $user,
                    $form->get('password_user')->getData()
                );
                $user->setPasswordUser($hashedPassword);
            }
            
            $entityManager->persist($user);
            $entityManager->flush();

            $this->addFlash('success', 'Utilisateur ajouté avec succès!');
            return $this->redirectToRoute('app_dashboard_user');
        }

        return $this->render('user/new.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/dashboard/user/edit/{id}', name: 'app_dashboard_user_edit')]
    public function edit(Request $request, User $user, EntityManagerInterface $entityManager, UserPasswordHasherInterface $passwordHasher): Response
    {
        $form = $this->createForm(UserType::class, $user, ['is_edit' => true]);
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            // Gestion du mot de passe seulement s'il a été modifié
            if ($form->get('password_user')->getData()) {
                $hashedPassword = $passwordHasher->hashPassword(
                    $user,
                    $form->get('password_user')->getData()
                );
                $user->setPasswordUser($hashedPassword);
            }

            // Gestion des fichiers
            $photoFile = $form->get('photo_user')->getData(); // Handle photo_user upload

            if ($photoFile) {
                $newFilename = uniqid().'.'.$photoFile->guessExtension();
                $photoFile->move(
                    $this->getParameter('uploads_directory'),
                    $newFilename
                );
                $user->setPhotoUser($newFilename);
            } else {
                $user->setPhotoUser('default_photo.png'); // Default photo
            }

            $pieceJointeFile = $form->get('piece_jointe')->getData();
            if ($pieceJointeFile) {
                $newFilename = uniqid().'.'.$pieceJointeFile->guessExtension();
                $pieceJointeFile->move(
                    $this->getParameter('pieces_jointes_directory'),
                    $newFilename
                );
                $user->setPieceJointe($newFilename);
            }

            $entityManager->flush();
            $this->addFlash('success', 'Utilisateur modifié avec succès!');
            return $this->redirectToRoute('app_dashboard_user');
        }

        return $this->render('user/edit.html.twig', [
            'form' => $form->createView(),
            'user' => $user,
        ]);
    }

    #[Route('/dashboard/user/delete/{id}', name: 'app_dashboard_user_delete', methods: ['POST', 'DELETE'])]
    public function delete(Request $request, User $user, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$user->getIdUser(), $request->request->get('_token'))) {
            $entityManager->remove($user);
            $entityManager->flush();

            if ($request->isXmlHttpRequest()) {
                return $this->json(['status' => 'success']);
            }
            
            $this->addFlash('success', 'Utilisateur supprimé avec succès');
            return $this->redirectToRoute('app_dashboard_user');
        }

        if ($request->isXmlHttpRequest()) {
            return $this->json(['status' => 'error', 'message' => 'Token CSRF invalide'], 400);
        }
        
        $this->addFlash('error', 'Token CSRF invalide');
        return $this->redirectToRoute('app_dashboard_user');
    }

        #[Route('/dashboard/user/toggle/{id}', name: 'app_dashboard_user_toggle', methods: ['POST', 'PUT'])]
        public function toggleActive(Request $request, User $user, EntityManagerInterface $entityManager): Response
        {
    if ($this->isCsrfTokenValid('toggle'.$user->getIdUser(), $request->request->get('_token'))) {
        $user->setIsActive(!$user->isActive());
        $entityManager->flush();

        if ($request->isXmlHttpRequest()) {
            return $this->json([
                'status' => 'success',
                'isActive' => $user->isActive(),
                'newButton' => $this->renderView('dashboard/user/_toggle_button.html.twig', ['user' => $user])
            ]);
        }
        
        $this->addFlash('success', $user->isActive() ? 'Utilisateur activé avec succès' : 'Utilisateur désactivé avec succès');
        return $this->redirectToRoute('app_dashboard_user');
    }

    if ($request->isXmlHttpRequest()) {
        return $this->json(['status' => 'error', 'message' => 'Token CSRF invalide'], 400);
    }
    
    $this->addFlash('error', 'Token CSRF invalide');
    return $this->redirectToRoute('app_dashboard_user');
}

    #[Route('/deactivate/{id}', name: 'app_dashboard_user_deactivate', methods: ['POST'])]
public function deactivate(Request $request, User $user, EntityManagerInterface $em): JsonResponse
{
    $data = json_decode($request->getContent(), true);

    if (!$this->isCsrfTokenValid('deactivate'.$user->getIdUser(), $data['_token'] ?? '')) {
        return $this->json(['success' => false, 'error' => 'Token invalide'], 400);
    }

    if (isset($data['minutes'])) {
        // Désactivation temporaire
        $reactivateAt = new \DateTime("+{$data['minutes']} minutes");
        $user->setIsActive(false);
        $user->setReactivateAt($reactivateAt);
    } elseif (isset($data['manual'])) {
        // Désactivation manuelle
        $user->setIsActive(false);
        $user->setReactivateAt(null);
    }

    $em->flush();

    return $this->json(['success' => true]);
}

        #[Route('/activate/{id}', name: 'app_dashboard_user_activate', methods: ['POST'])]
        public function activate(Request $request, User $user, EntityManagerInterface $em): Response
    {
    if ($this->isCsrfTokenValid('activate'.$user->getIdUser(), $request->request->get('_token'))) {
        $user->setIsActive(true);
        $user->setReactivateAt(null);
        $em->flush();
        
        $this->addFlash('success', 'Utilisateur réactivé avec succès');
    } else {
        $this->addFlash('error', 'Token CSRF invalide');
    }
    
    return $this->redirectToRoute('app_dashboard_user');
}
}