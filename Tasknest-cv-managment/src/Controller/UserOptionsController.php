<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class UserOptionsController extends AbstractController
{
    #[Route('/user/options', name: 'app_user_options')]
    public function index(): Response
    {
        return $this->render('user_options/index.html.twig', [
            'controller_name' => 'UserOptionsController',
        ]);
    }
}
