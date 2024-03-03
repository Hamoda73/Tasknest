<?php

namespace App\Controller;

use App\Entity\Application;
use App\Form\ApplicationType;
use App\Repository\ApplicationRepository;
use App\Repository\OffersRepository;
use App\Repository\UserRepository;
use App\Service\TwilioSMSService;
use Doctrine\ORM\Mapping\Id;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\BinaryFileResponse;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\File\UploadedFile;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\ResponseHeaderBag;
use Symfony\Component\Routing\Annotation\Route;

class ApplicationController extends AbstractController
{
    #[Route('/application', name: 'app_application')]
    public function index(): Response
    {
        return $this->render('application/index.html.twig', [
            'controller_name' => 'ApplicationController',
        ]);
    }



    /*#[Route('/addformapp/{offerId}', name: 'addformapp')]
    public function addformapp(UserRepository $userRepository, OffersRepository $offersRepository, $offerId, Request $request, ManagerRegistry $managerRegistry): Response
    {
        $em = $managerRegistry->getManager();
        $user = $userRepository->find(1);
        $offer = $offersRepository->find($offerId);
        $app = new Application();
        $app->setUser($user);
        $app->setOffers($offer);
        $appForm = $this->createForm(ApplicationType::class, $app, ['user' => $user]);
        $appForm->handleRequest($request);
        if ($appForm->isSubmitted() && $appForm->isValid()) {
            $em->persist($app);
            $em->flush();
            return $this->redirectToRoute("showAlloffers");
        }
        return $this->renderForm('user/application/app.html.twig', [
            'formapp' => $appForm,
            'user' => $user,   //ninsesty aal user
        ]);
    }*/


    #[Route('/addformapp/{offerId}/{userid}', name: 'addformapp')]
    public function addformapp($userid, ApplicationRepository $applicationRepository, UserRepository $userRepository, OffersRepository $offersRepository, $offerId, Request $request, ManagerRegistry $managerRegistry): Response
    {
        $em = $managerRegistry->getManager();
        $user = $userRepository->find($userid);
        $offer = $offersRepository->find($offerId);
        $app = new Application();
        $app->setUser($user);
        $app->setOffers($offer);
        $appForm = $this->createForm(ApplicationType::class, $app, ['user' => $user]);
        $appForm->handleRequest($request);

        if ($appForm->isSubmitted() && $appForm->isValid()) {
            $existingapps = $applicationRepository->findOneBy([  //seartch etha ken l user atheka already applied lel offer hethyka
                'Offers' => $offer,
                'user' => $user,
            ]);
            if ($existingapps) {
                // If an app for this offer and  this user  exists, add flash message and redirect
                $this->addFlash('error', 'You ve already applied for this job.');
                return new RedirectResponse($this->generateUrl('addformapp', ['offerId' => $offerId, 'userid' => $user->getId()]));
            } else {


                /** @var UploadedFile $cvFile */
                $cvFile = $appForm['cv']->getData();

                if ($cvFile) {
                    $originalFilename = pathinfo($cvFile->getClientOriginalName(), PATHINFO_FILENAME);
                    $newFilename = $originalFilename . '-' . uniqid() . '.' . $cvFile->guessExtension();

                    try {
                        $cvFile->move(
                            $this->getParameter('cv_directory'),
                            $newFilename
                        );
                    } catch (FileException $e) {
                        // Handle file upload error
                    }

                    // Store the file path in the database
                    $app->setCv($newFilename);
                }

                $em->persist($app);
                $em->flush();
                return $this->redirectToRoute("showAlloffers", ['iduser' => $userid]);
            }
        } //end else

        return $this->render('user/application/app.html.twig', [
            'formapp' => $appForm->createView(),
            'userid' => $user,
            'offerid' => $offer,

        ]);
    }




    #[Route('/show_apps/{offerId}', name: 'show_apps')]
    public function showApplications(ApplicationRepository $applicationRepository, OffersRepository $offerRepository, $offerId): Response
    {
        $applications = $applicationRepository->findBy(['Offers' => $offerId]);
        $offer = $offerRepository->find($offerId); //recuperation d'offre

        return $this->render('user/application/showoffapps.html.twig', [
            'applications' => $applications,
            'offer' => $offer,
            'userid' => $offer->getUser(),

        ]);
    }

    //user show  change id to work
    //offerId d'offer

    #[Route('/my_applications/{iduser}', name: 'my_applications')]
    public function myApplications($iduser, ApplicationRepository $applicationRepository, UserRepository $userRepository, OffersRepository $offersRepository): Response
    {
        $user = $userRepository->find($iduser);
        //$offer = $offersRepository->find($offerId);
        $applications = $applicationRepository->findBy(['user' => $user]);

        return $this->render('user/application/my_applications.html.twig', [
            'applications' => $applications,
            'userid' => $user,
        ]);
    }



    #[Route('/deleteappuser/{id}', name: 'deleteappuser')]
    public function deleteappuser($id, ApplicationRepository $appRepository, ManagerRegistry $managerRegistry): Response
    {
        $em = $managerRegistry->getManager();
        $dataid = $appRepository->find($id);
        $idoff = $dataid->getOffers()->getId();
        $em->remove($dataid);
        $em->flush();
        return $this->redirectToRoute('my_applications', ['iduser' => $dataid->getUser()->getId()]);
    }

    #[Route('/deleteappoff/{id}', name: 'deleteappoff')]
    public function deleteappoff($id, ApplicationRepository $appRepository, ManagerRegistry $managerRegistry): Response
    {
        $em = $managerRegistry->getManager();
        $dataid = $appRepository->find($id);
        $idoff = $dataid->getOffers()->getId();
        $em->remove($dataid);
        $em->flush();
        return $this->redirectToRoute('show_apps', ['offerId' => $idoff]);
    }




    #[Route('/editApp/{idapp}', name: 'editApp')]


    public function editApp($idapp, ApplicationRepository $applicationRepository, UserRepository $userRepository, Request $request, ManagerRegistry $managerRegistry): Response
    {
        $em = $managerRegistry->getManager();
        $app = $applicationRepository->find($idapp);

        if (!$app) {
            throw $this->createNotFoundException('Application not found');
        }

        $user = $userRepository->find($app->getUser()->getId());
        $appForm = $this->createForm(ApplicationType::class, $app, ['user' => $user]);
        $appForm->handleRequest($request);

        if ($appForm->isSubmitted() && $appForm->isValid()) {
            /** @var UploadedFile|null $cvFile */
            $cvFile = $appForm->get('cv')->getData();

            if ($cvFile instanceof UploadedFile) {
                // Handle file upload
                $newFilename = uniqid() . '.' . $cvFile->guessExtension(); //uniqid prevent machekl l filename

                try {
                    // Move ll u
                    $cvFile->move(
                        $this->getParameter('cv_directory'),
                        $newFilename
                    );

                    // Update the CV  with the new file path
                    $app->setCv($newFilename);
                } catch (FileException $e) {
                    // Handle file upload error
                }
            }

            $em->persist($app);
            $em->flush();

            return $this->redirectToRoute('my_applications', ['iduser' => $app->getUser()->getId()]);
        }

        return $this->render('user/application/editapp.html.twig', [
            'formapp' => $appForm->createView(),
            'userid' => $user,
        ]);
    }



    #[Route('/showresume/{idapp}', name: 'showresume')]
    public function showresume($idapp, ApplicationRepository $applicationRepository): Response
    {
        $app = $applicationRepository->find($idapp);
        $cvPath = '/uploads/cv/' . $app->getCv();

        return $this->render('user/application/showresume.html.twig', [
            'cvPath' => $cvPath,
        ]);
    }


    //admin

    #[Route('/showappsadmin/{offerId}', name: 'showappsadmin')]
    public function showappsadmin(ApplicationRepository $applicationRepository, $offerId): Response
    {
        $applications = $applicationRepository->findBy(['Offers' => $offerId]);

        return $this->render('admin/showappsadmin.html.twig', [
            'applications' => $applications,
        ]);
    }


    #[Route('/deleteappadmin/{id}', name: 'deleteappadmin')]
    public function deleteappadmin($id, ApplicationRepository $appRepository, ManagerRegistry $managerRegistry): Response
    {
        $em = $managerRegistry->getManager();
        $dataid = $appRepository->find($id);
        $idoff = $dataid->getOffers()->getId();
        $em->remove($dataid);
        $em->flush();
        return $this->redirectToRoute('showappsadmin', ['offerId' => $idoff]);
    }

    //smsss workinn suiiii

    #[Route('/sendSms/{applicationId}', name: 'sendSms')]
    public function sendSms(ApplicationRepository $applicationRepository, $applicationId): Response
    {
        // Retrieve the application entity
        $application = $applicationRepository->find($applicationId);

        if (!$application) {
            throw $this->createNotFoundException('Application not found');
        }

        // Extract necessary information from the application and offer entities
        $enterpriseName = $application->getOffers()->getEntrepriseName();
        $post = $application->getOffers()->getPost();
        $firstName = $application->getUser()->getFname();
        $lastName = $application->getUser()->getLname();
        $phoneNumber = '+216' . $application->getUser()->getPhonenumber();
        // Construct the message
        $message = "Dear $firstName $lastName,\nCongratulations! Your application for the position of $post at $enterpriseName has been accepted. We look forward to having you on our team.";

        // Create an instance of TwilioSMSService manually and pass the required parameters
        $twilioSMSService = new TwilioSMSService(
            $this->getParameter('twilio_account_sid'),
            $this->getParameter('twilio_auth_token'),
            $this->getParameter('twilio_phone_number')
        );

        // Send SMS
        $twilioSMSService->sendSMS($phoneNumber, $message);

        return $this->redirectToRoute('show_apps', ['offerId' => $application->getOffers()->getId()]);
    }
}
