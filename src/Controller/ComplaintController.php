<?php

namespace App\Controller;



use App\Repository\ComplaintRepository;
use App\Entity\Complaint;
use App\Form\ComplaintformType;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;



class ComplaintController extends AbstractController
{
    #[Route('/complaint', name: 'app_complaint')]
    public function index(): Response
    {
        return $this->render('complaint/index.html.twig', [
            'controller_name' => 'ComplaintController',
        ]);
    }

    #[Route('/complaintpage', name: 'app_complaintpage')]
    public function complaintpage(): Response
    {
        return $this->render('complaint/complaintpage.html.twig', [
            'controller_name' => 'ComplaintController',
        ]);
    }
    #[Route('/dashboard', name: 'app_dashboard')]
    public function dashboard(): Response
    {
        return $this->render('admin/dashboard.html.twig', [
            'controller_name' => 'ComplaintController',
        ]);
    }

    #[Route('/dashboardcomplaint', name: 'app_dashboardcomplaint')]
    public function dashboardcomplaint(): Response
    {
        return $this->render('admin/dashboardcomplaint.html.twig', [
            'controller_name' => 'ComplaintController',
        ]);
    }

    #[Route('/addcomplaintform', name: 'app_addcomplaintform')]
    public function addcomplaintform(ManagerRegistry $managerRegistry, Request $req): Response
    {
        $em=$managerRegistry->getManager();
        $complaint=new Complaint();
        
        $form=$this->createForm(ComplaintformType::class,$complaint);
        $form->handleRequest($req);
        
        if($form->isSubmitted() and $form->isValid())
        {
        $em->persist($complaint);
        $em->flush();

        return $this->redirectToRoute('');
        }

        return $this->renderForm('Complaint/complaintpage.html.twig', [
            'f'=>$form
        ]);

    }


    




}
