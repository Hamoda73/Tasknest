<?php

namespace App\Controller;

use App\Repository\ComplaintRepository;
use App\Repository\RespondRepository;

use App\Entity\Complaint;
use App\Entity\Respond;
use App\Form\ComplaintformType;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Bridge\Doctrine\ManagerRegistry as DoctrineManagerRegistry;
use Symfony\Component\Mime\Email;
use Symfony\Component\Mailer\MailerInterface;

use Symfony\UX\Chartjs\Builder\ChartBuilderInterface;
use Symfony\UX\Chartjs\Model\Chart;





class ComplaintController extends AbstractController
{
    #[Route('/complaint', name: 'app_complaint')]
    public function index(): Response
    {
        return $this->render('complaint/index.html.twig', [
            'controller_name' => 'ComplaintController',
        ]);
    }


    #[Route('/dashboard', name: 'app_dashboard')]
    public function dashboard(ComplaintRepository $complaintRepository, RespondRepository $respondRepository): Response
    {
        $complaint = $complaintRepository->findAll();
        $respond = $respondRepository->findAll();

        return $this->render('admin/dashboard.html.twig', [
            'controller_name' => 'ComplaintController',
            'complaint' => $complaint,
            'respond' => $respond,

        ]);
    }

    #[Route('/userdashboard', name: 'app_userdashboard')]
    public function userdashboard(ComplaintRepository $complaintRepository, RespondRepository $respondRepository): Response
    {
        $complaint = $complaintRepository->findAll();
        $respond = $respondRepository->findAll();

        return $this->render('admin/userdashboard.html.twig', [
            'controller_name' => 'ComplaintController',
            'complaint' => $complaint,
            'respond' => $respond,

        ]);
    }



    #[Route('/dashboardcomplaint', name: 'app_dashboardcomplaint')]
    public function dashboardcomplaint(): Response
    {
        return $this->render('admin/dashboardcomplaint.html.twig', [
            'controller_name' => 'ComplaintController',
        ]);
    }
    #[Route('/complaintconsult', name: 'app_complaintconsult')]
    public function complaintconsult(): Response
    {
        return $this->render('admin/complaintconsult.html.twig', [
            'controller_name' => 'ComplaintController',
        ]);
    }



    #[Route('/complaintpage', name: 'app_complaintpage')]
    public function complaintpage(ManagerRegistry $managerRegistry, Request $req, MailerInterface $mailer): Response
    {
        $em = $managerRegistry->getManager();
        $complaint = new complaint();

        $form = $this->createForm(ComplaintformType::class, $complaint);
        $form->handleRequest($req);

        if ($form->isSubmitted() && $form->isValid()) {
            $em->persist($complaint);
            $em->flush();
            
            $email = (new Email())
                ->from('tasknestcompany@gmail.com')
                ->to('md.khelifi@hotmail.com')
                ->subject('Time for Symfony Mailer!')
                ->text('Sending emails is fun again!');
            $mailer->send($email);

            return $this->redirectToRoute('app_complaintpage');
        }

        return $this->render('complaint/complaintpage.html.twig', [
            'form' => $form->createView(),
            'complaint' => $complaint,

        ]);
    }

    #[Route('/complaintconsult', name: 'app_complaintconsult')]
    public function showdb(ComplaintRepository $complaintRepository, RespondRepository $respondRepository): Response
    {
        $complaint = $complaintRepository->findAll();
        $respond = $respondRepository->findAll();
        return $this->render('complaint/complaintconsult.html.twig', [
            'complaint' => $complaint,
            'respond' => $respond,
        ]);
    }

    #[Route('/deletecomplaint/{id}', name: 'app_deletecomplaint')]
    public function deletecomplaint($id, ManagerRegistry $managerRegistry, ComplaintRepository $complaintRepository): Response
    {
        $em = $managerRegistry->getManager();
        $dataid = $complaintRepository->find($id);
        $em->remove($dataid);
        $em->flush();

        return $this->redirectToRoute('app_complaintconsult');
    }

    #[Route('/deletecomplaint1/{id}', name: 'app_deletecomplaint1')]
    public function deletecomplaint1($id, ManagerRegistry $managerRegistry, ComplaintRepository $complaintRepository): Response
    {
        $em = $managerRegistry->getManager();
        $dataid = $complaintRepository->find($id);
        $em->remove($dataid);
        $em->flush();

        return $this->redirectToRoute('app_dashboard');
    }


    #[Route('/editcomplaint/{id}', name: 'app_editcomplaint')]
    public function editcomplaint($id, ManagerRegistry $managerRegistry, ComplaintRepository $complaintRepository, Request $req): Response
    {
        $em = $managerRegistry->getManager();
        $dataid = $complaintRepository->find($id);
        $form = $this->createForm(ComplaintformType::class, $dataid);
        $form->handleRequest($req);
        if ($form->isSubmitted() and $form->isValid()) {
            $em->persist($dataid);
            $em->flush();
            return $this->redirectToRoute('app_complaintconsult');
        }
        return $this->renderForm('complaint/editcomplaint.html.twig', [
            'form' => $form
        ]);
    }


#[Route('/statistics', name: 'app_statistics')]
public function statistics(ComplaintRepository $complaintRepository): Response
{
    $complaintData = [];
    $complaints = $complaintRepository->findAll();

    // Create an associative array to store the count of each complaint type
    $typeCounts = [];

    foreach ($complaints as $complaint) {
        $type = $complaint->getType();

        // Check if the type is already in the array
        if (isset($typeCounts[$type])) {
            // Increment the count if it exists
            $typeCounts[$type]++;
        } else {
            // Add the type to the array if it doesn't exist
            $typeCounts[$type] = 1;
        }
    }

    // Convert the associative array to the desired format for Chart.js
    foreach ($typeCounts as $type => $count) {
        $complaintData[] = [
            'type' => $type,
            'count' => $count,
        ];
    }

    return $this->render('admin/statistics.html.twig', [
        'complaintData' => $complaintData,
    ]);
}

}
