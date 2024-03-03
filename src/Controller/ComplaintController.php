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
use Symfony\Component\Security\Core\Security;
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


    #[Route('/dashboardall', name: 'app_dashboardall')]
    public function dashboardall(ComplaintRepository $complaintRepository, RespondRepository $respondRepository): Response
    {
        $complaint = $complaintRepository->findAll();
        $respond = $respondRepository->findAll();
        $sum = $respondRepository->countDoneComplaints();
        $total =$complaintRepository->countAllComplaints();

        return $this->render('admin/dashboardall.html.twig', [
            'controller_name' => 'ComplaintController',
            'complaint' => $complaint,
            'respond' => $respond,
            'sum'=>$sum,
            'total'=>$total,

        ]);
    }

    #[Route('/dashboardgi', name: 'app_dashboardgi')]
    public function dashboardgi(ComplaintRepository $complaintRepository, RespondRepository $respondRepository): Response
    {
        $complaint = $complaintRepository->getGeneralInquiryComplaints();
        $respond = $respondRepository->getGeneralInquiryResponds();
        $sum = $respondRepository->countDoneGeneralInquiryComplaints();
        $total =$complaintRepository->countGeneralInquiryComplaints();

        return $this->render('admin/dashboardgi.html.twig', [
            'controller_name' => 'ComplaintController',
            'complaint' => $complaint,
            'respond' => $respond,
            'sum'=>$sum,
            'total'=>$total,

        ]);
    }
    #[Route('/dashboardbi', name: 'app_dashboardbi')]
    public function dashboardbi(ComplaintRepository $complaintRepository, RespondRepository $respondRepository): Response
    {
        $complaint = $complaintRepository->getBillingIssueComplaints();
        $respond = $respondRepository->getBillingIssueResponds();
        $sum = $respondRepository->countDoneBillingIssueComplaints();
        $total =$complaintRepository->countBillingIssueComplaints();

        return $this->render('admin/dashboardbi.html.twig', [
            'controller_name' => 'ComplaintController',
            'complaint' => $complaint,
            'respond' => $respond,
            'sum'=>$sum,
            'total'=>$total,

        ]);
    }

    #[Route('/dashboardcs', name: 'app_dashboardcs')]
    public function dashboardcs(ComplaintRepository $complaintRepository, RespondRepository $respondRepository): Response
    {
        $complaint = $complaintRepository->getCustomerSupportComplaints();
        $respond = $respondRepository->getCustomerSupportResponds();
        $sum = $respondRepository->countDoneCustomerSupportComplaints();
        $total =$complaintRepository->countCustomerSupportComplaints();

        return $this->render('admin/dashboardcs.html.twig', [
            'controller_name' => 'ComplaintController',
            'complaint' => $complaint,
            'respond' => $respond,
            'sum'=>$sum,
            'total'=>$total,

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
        $user = $this->getUser();
        if ($form->isSubmitted() && $form->isValid()) {
            $complaint->setUser($user);           
            $em->persist($complaint);
            $em->flush();
            if ($user) {
                $receiverEmail = $user->getEmail();
                
            $email = (new Email())
                ->from('tasknestcompany@gmail.com')
                ->to($receiverEmail)
                ->subject('Complaint submission')
                ->text('Thank you for contacting us, we received your feedback, our support team will get to you as soon as possible.

                regards');
            $mailer->send($email);
        }

            return $this->redirectToRoute('app_complaintpage');
        }

        return $this->render('complaint/complaintpage.html.twig', [
            'form' => $form->createView(),
            'complaint' => $complaint,

        ]);
    }

   

#[Route('/complaintconsult', name: 'app_complaintconsult')]
public function showdb(ComplaintRepository $complaintRepository, RespondRepository $respondRepository, Security $security): Response
{
    $user = $this->getUser();
    if ($user) {
        $complaints = $complaintRepository->findBy(['User' => $user]);
        $responds = $respondRepository->findAll();

        return $this->render('complaint/complaintconsult.html.twig', [
            'complaint' => $complaints,
            'respond' => $responds,
        ]);
    }
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
