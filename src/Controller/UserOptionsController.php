<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\Persistence\ManagerRegistry;
use App\Repository\UserRepository;
use Symfony\Component\HttpFoundation\Request;
use App\Form\UserType;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

class UserOptionsController extends AbstractController
{
    #[Route('/user/options', name: 'app_user_options')]
    public function index(): Response
    {
        return $this->render('user_options/index.html.twig', [
            'controller_name' => 'UserOptionsController',
        ]);
    }

    #[Route('/updateuser/{id}', name: 'app_updateuser')]
    public function authoredit($id, ManagerRegistry $managerRegistry, UserRepository $userRepository, Request $request, UserPasswordHasherInterface $passwordHasher): Response
    {
        $em = $managerRegistry->getManager();
        $data = $userRepository->find($id); 
        $form = $this->createForm(UserType::class, $data);
        $form->handleRequest($request);

        if($form->isSubmitted() && $form->isValid()) {
            // Hash the password if it has been changed in the form
            $plainPassword = $form->get('password')->getData(); // Assuming 'password' is the name of your password field in the form
            if (!empty($plainPassword)) {
                $hashedPassword = $passwordHasher->hashPassword($data, $plainPassword);
                $data->setPassword($hashedPassword);
            }

            $em->flush();
        }

        return $this->render('user_options/updateuser.html.twig', [
            'f' => $form->createView(),
        ]);
    }


    #[Route('/deleteuser/{id}', name: 'app_deleteuser')]
    public function authordelete($id, UserRepository $userRepository, ManagerRegistry $managerRegistry ): Response
    {
        $em = $managerRegistry->getManager();
        $data = $userRepository->find($id);
        $em->remove($data);
        $em->flush();

        return $this->redirectToRoute('app_logout');
    }
}
