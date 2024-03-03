<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\Persistence\ManagerRegistry;
use App\Repository\UserRepository;
use Symfony\Component\HttpFoundation\Request;

use App\Form\UserType;
use Exception;
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
    public function updateuser($id, ManagerRegistry $managerRegistry, UserRepository $userRepository, Request $request, UserPasswordHasherInterface $passwordHasher): Response
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
            'form' => $form->createView()
        ]);
    }


    #[Route('/deleteuser/{id}', name: 'app_deleteuser')]
    public function deleteuser($id, UserRepository $userRepository, ManagerRegistry $managerRegistry ): Response
    {
        $em = $managerRegistry->getManager();
        $data = $userRepository->find($id);
        $em->remove($data);
        $em->flush();

        return $this->redirectToRoute('app_signin');
    }


    /*------------------------------  ADMIN  ------------------------------------*/


    #[Route('/updateadmin/{id}', name: 'app_updateadmin')]
    public function updateadmin($id, ManagerRegistry $managerRegistry, UserRepository $userRepository, Request $request, UserPasswordHasherInterface $passwordHasher): Response
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

        return $this->render('user_options/updateadmin.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/admindeleteuser/{id}', name: 'app_admindeleteuser')]
    public function admindeleteuser($id, UserRepository $userRepository, ManagerRegistry $managerRegistry ): Response
    {
        $em = $managerRegistry->getManager();
        $data = $userRepository->find($id);
        $em->remove($data);
        $em->flush();

        return $this->redirectToRoute('app_useraccounts');
    }

    #[Route('/adminblockuser/{id}', name: 'app_adminblockuser')]
    public function adminblockuser($id, UserRepository $userRepository, ManagerRegistry $managerRegistry ): Response
    {
        $em = $managerRegistry->getManager();
        $data = $userRepository->find($id);
        $data->setBlocked(!$data->isBlocked());
        $em->persist($data);
        $em->flush();
        return $this->redirectToRoute('app_useraccounts');
    }

}

