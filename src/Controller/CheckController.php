<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class CheckController extends AbstractController
{
    #[Route('/check', name: 'check')]
    public function index(): Response
    {
        return $this->render('user/check/Home.html.twig', [
            'controller_name' => 'CheckController',
        ]);
    }

    #[Route('/check2', name: 'check2')]
    public function check2(): Response
    {
        return $this->render('user/off/OffersListing.html.twig', [
            'controller_name' => 'CheckController',
        ]);
    }


    #[Route('/checkadmin', name: 'checkadmin')]
    public function checkadmin(): Response
    {
        return $this->render('admin/dashboard.html.twig', [
            'controller_name' => 'CheckController',
        ]);
    }
}
