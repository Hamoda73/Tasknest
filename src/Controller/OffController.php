<?php

namespace App\Controller;

use App\Entity\Offers;
use App\Entity\User;
use App\Form\OffersType;
use App\Repository\OffersRepository;
use App\Repository\UserRepository;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class OffController extends AbstractController
{
    #[Route('/off', name: 'app_off')]
    public function index(): Response
    {
        return $this->render('off/index.html.twig', [
            'controller_name' => 'OffController',
        ]);
    }

    #[Route('/addformoff', name: 'addformoff')]
    public function addformoff(UserRepository $UserRepository, ManagerRegistry $managerRegistry, Request $req): Response
    {
        $dataid = $UserRepository->find(2);
        $em = $managerRegistry->getManager();
        $offer = new Offers();
        $offer->setUser($dataid);
        $form = $this->createForm(OffersType::class, $offer);
        $form->handleRequest($req);
        if ($form->isSubmitted() and $form->isValid()) { //if form is not empty

            $em->persist($offer);
            $em->flush();
            $offerid = $offer->getUser()->getId();
            return $this->redirectToRoute('show_offers', ['id' => $offerid]);
        }

        return $this->renderForm('user/off/off.html.twig', [
            'form' => $form

        ]);
    }

    #[Route('/show_offers/{id}', name: 'show_offers')]
    public function showOffers(OffersRepository $offersRepository, UserRepository $UserRepository, $id): Response
    {
        $user = $UserRepository->find($id);

        if (!$user) {
            throw $this->createNotFoundException('User not found');
        }

        $offers = $offersRepository->findBy(['User' => $user]);

        return $this->render('user/off/Showoffuser.html.twig', [
            'offers' => $offers,

        ]);
    }


    #[Route('/editOffer/{id}', name: 'editOffer')]
    public function editOffer($id, OffersRepository $offerRepository, ManagerRegistry $ManagerRegistry, Request $Req): Response
    {
        $em = $ManagerRegistry->getManager();

        $offer = $offerRepository->find($id);
        if (!$offer) {
            throw $this->createNotFoundException('Offer not found');
        }
        $form = $this->createForm(OffersType::class, $offer);
        $form->handleRequest($Req);
        if ($form->isSubmitted() and $form->isValid()) {
            $em->persist($offer);
            $em->flush();
            return $this->redirectToRoute('show_offers', ['id' => $offer->getUser()->getId()]);
        }
        return $this->renderForm('user/off/editoffer.html.twig', [
            'form' => $form
        ]);
    }


    #[Route('/deleteOffer/{id}', name: 'deleteOffer')]
    public function deleteOffer($id, OffersRepository $offerRepository, ManagerRegistry $managerRegistry): Response
    {
        $em = $managerRegistry->getManager();
        $dataid = $offerRepository->find($id);
        $em->remove($dataid);
        $em->flush();
        return $this->redirectToRoute('show_offers', ['id' => $dataid->getUser()->getId()]);
    }



    #[Route('/showAlloffers', name: 'showAlloffers')]
    public function showAllffers(OffersRepository $offersRepository): Response
    {
        $offers = $offersRepository->findAll();
        return $this->render('user/off/OffersListing.html.twig', [
            'offers' => $offers, //tableau d'offres

        ]);
    }



    //admin crud

    #[Route('/showoffadmin', name: 'showoffadmin')]
    public function showoffadmin(OffersRepository $offersRepository): Response
    {
        $offers = $offersRepository->findAll();
        return $this->render('admin/showoffadmin.html.twig', [
            'offers' => $offers,

        ]);
    }

    #[Route('/deleteOfferadmin/{id}', name: 'deleteOfferadmin')]
    public function deleteOfferadmin($id, OffersRepository $offerRepository, ManagerRegistry $managerRegistry): Response
    {
        $em = $managerRegistry->getManager();
        $dataid = $offerRepository->find($id);
        $em->remove($dataid);
        $em->flush();
        return $this->redirectToRoute('showoffadmin', ['id' => $dataid->getUser()->getId()]);
    }
}
