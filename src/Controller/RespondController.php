<?php

namespace App\Controller;


namespace App\Controller;
use App\Repository\ComplaintRepository;
use App\Entity\Complaint;
use App\Form\ComplaintformType;
use App\Repository\RespondRepository;
use App\Entity\Respond;
use App\Form\RespondType;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Bridge\Doctrine\ManagerRegistry as DoctrineManagerRegistry;
use Symfony\Component\Routing\Annotation\Route;

class RespondController extends AbstractController
{
    #[Route('/respond1', name: 'app_respond1')]
    public function index(): Response
    {
        return $this->render('respond/index.html.twig', [
            'controller_name' => 'RespondController',
        ]);
    }

    #[Route('/respond/{id}', name: 'app_respond')]
    public function respond($id, ManagerRegistry $managerRegistry, ComplaintRepository $complaintRepository, RespondRepository $respondRepository, Request $req): Response
    {
        $em=$managerRegistry->getManager();
        $complaint = $complaintRepository->find($id);


        $respond = new respond();

        $form = $this->createForm(RespondType::class, $respond);
        $form->handleRequest($req);
        //$respond->setComplaint($id);
        if ($form->isSubmitted() && $form->isValid()) {
            
            $em->persist($respond);
            $em->flush();

            return $this->redirectToRoute('app_dashboard');
        }

        return $this->render('admin/respond.html.twig', [
            'complaint' => $complaint,
            'respond' => $respond,
            'form' => $form->createView(),
            'id' => $id,
        ]);
    }

    



    

    




}
