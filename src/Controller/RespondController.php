<?php

namespace App\Controller;

use App\Repository\ComplaintRepository;
use App\Entity\Complaint;
use App\Form\ComplaintformType;
use App\Repository\RespondRepository;
use App\Entity\Respond;
use App\Form\RespondType;
use App\Form\UserType;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Bridge\Doctrine\ManagerRegistry as DoctrineManagerRegistry;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Mime\Email;
use Symfony\Component\Mailer\MailerInterface;
use App\Entity\User;
use App\Repository\UserRepository;

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
public function respond($id, ManagerRegistry $managerRegistry, ComplaintRepository $complaintRepository, RespondRepository $respondRepository, Request $req, MailerInterface $mailer, UserRepository $userRepository): Response
{
    $em = $managerRegistry->getManager();
    $complaint = $complaintRepository->find($id);
    $respond = new Respond();
    $respond->setComplaint($complaint);
    $form = $this->createForm(RespondType::class, $respond);
    $form->handleRequest($req);
    $user = $this->getUser();

    if ($form->isSubmitted() && $form->isValid()) {
        $em->persist($respond);
        $em->flush();

        // Get the user associated with the complaint
        $complaintUser = $complaint->getUser();

        if ($complaintUser) {
            $receiverEmail = $complaintUser->getEmail();
            $message = $form->get('message')->getData();

            $email = (new Email())
                ->from('tasknestcompany@gmail.com')
                ->to($receiverEmail)
                ->subject('Response for your complaint')
                ->text($message);

            $mailer->send($email);
        }

        return $this->redirectToRoute('app_dashboardall');
    }

    return $this->render('admin/respond.html.twig', [
        'complaint' => $complaint,
        'respond' => $respond,
        'form' => $form->createView(),
        'id' => $id,
    ]);
}


    #[Route('/respondedit/{id}/{id1}', name: 'app_respondedit')]
    public function respondedit($id, $id1, ManagerRegistry $managerRegistry, ComplaintRepository $complaintRepository, RespondRepository $respondRepository, Request $req): Response
    {
        $em = $managerRegistry->getManager();
        $dataid = $respondRepository->find($id);

        $complaint = $complaintRepository->find($id1);

        $form = $this->createForm(RespondType::class, $dataid);
        $form->handleRequest($req);

        if ($form->isSubmitted() and $form->isValid()) {
            $em->persist($dataid);
            $em->flush();
            return $this->redirectToRoute('app_dashboardall');
        }


        return $this->renderForm('admin/respondedit.html.twig', [
            'complaint' => $complaint,
            'form' => $form,
        ]);
    }

    #[Route('/deleterespond/{id}', name: 'app_deleterespond')]
    public function deleterespond($id, ManagerRegistry $managerRegistry, RespondRepository $respondRepository): Response
    {
        $em = $managerRegistry->getManager();
        $dataid = $respondRepository->find($id);
        $em->remove($dataid);
        $em->flush();

        return $this->redirectToRoute('app_dashboard');
    }
}
