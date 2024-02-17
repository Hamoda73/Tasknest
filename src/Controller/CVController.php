<?php

namespace App\Controller;

use App\Entity\CV;
use App\Form\CvType;
use App\Repository\CVRepository;
use App\Repository\UserRepository;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
//use Symfony\Component\HttpFoundation\Request;


class CVController extends AbstractController
{
     #[Route('/user', name: 'home')]
   public function home(): Response
    {
        return $this->render('user/Home.html.twig', [
            'controller_name' => 'CVController',
        ]);
    }


    #[Route('user/addcv/{id}', name: 'addcv')]
    public function addcv($id,UserRepository $userRepository, CVRepository $cvRepository, ManagerRegistry $managerRegistry, Request $request): Response
    {
        $entityManager = $managerRegistry->getManager();

        $user = $userRepository->find($id);
    
        // Check if CV already exists for the user
        $existingCV = $cvRepository->findOneBy(['user' => $user]);
        if ($existingCV) {
            // Redirect to some route or display an error message indicating CV already exists
            return $this->redirectToRoute('showcvcreated', ['idc' => $existingCV->getId()]);
        }

        $cv = new CV();
        $cv->setUser($user);
    
        $cvForm = $this->createForm(CvType::class, $cv);
    
        $cvForm->handleRequest($request);
        if ($cvForm->isSubmitted() && $cvForm->isValid()) {
            $selectedLanguages = $cvForm->get('language')->getData();
            $cv->setLanguage($selectedLanguages);
            $entityManager->persist($cv);
            $entityManager->flush();
            $cvId = $cv->getId();

            return $this->redirectToRoute('addSkill', ['idc' => $cvId]);
        }
    
        return $this->render('user/cv/addcv.html.twig', [
            'cvForm' => $cvForm->createView(),
        ]);
    }

    #[Route('user/showcvcreated/{idc}', name: 'showcvcreated')]
    public function showcvcreated($idc,CVRepository $cvRepository): Response
    {
        $cvWithSkills = $cvRepository->findCVWithSkillsByCVId($idc);
        $cv = $cvWithSkills['cv'];
        $skills = $cvWithSkills['skills'];
        return $this->render('user/cv/showcvcreated.html.twig', [
            'cv' => $cv,
            'skills' => $skills,
        ]);
    }

    #[Route('user/editcv/{id}', name: 'editcv')]
public function editCV($id, CVRepository $CVRepository, ManagerRegistry $ManagerRegistry, Request $request): Response
{
    $entityManager = $ManagerRegistry->getManager();
    $cv = $CVRepository->find($id);

    // Create the form using the CV entity
    $cvForm = $this->createForm(CvType::class, $cv);
    
    $cvForm->handleRequest($request);
    
    if ($cvForm->isSubmitted() && $cvForm->isValid()) {
        $entityManager->persist($cv);
        $entityManager->flush();
    }
    
    return $this->render('user/cv/editcv.html.twig', [
        'cv' => $cv,
        'cvForm' => $cvForm->createView(),
    ]);
}

#[Route('user/removecv/{id}', name: 'removecv')]
public function removecv($id,CVRepository $CVRepository,ManagerRegistry $ManagerRegistry): Response
{
    $em = $ManagerRegistry->getManager();
    $dataid = $CVRepository->find($id);
    $em->remove($dataid);
    $em->flush();
    return $this->redirectToRoute('showcvs');
}

#[Route('user/showcvs', name: 'showcvs')]
public function showcvs(CVRepository $cvRepository): Response
{
    $cv = $cvRepository->findAll();
    return $this->render('user/cv/showcvs.html.twig', [
        'cv' => $cv,
    ]);
}


/*----------------------------------------  ADMIN  -----------------------------------------------*/

#[Route('/admin', name: 'admin')]
public function admin(): Response
 {
     return $this->render('admin/dashboard.html.twig', [
         'controller_name' => 'CVController',
     ]);
 }

 #[Route('admin/cv/showcvsadmin', name: 'showcvsadmin')]
public function showcvsadmin(CVRepository $cvRepository): Response
{
    $cv = $cvRepository->findAll();
    return $this->render('admin/cv/showcvs.html.twig', [
        'cv' => $cv,
    ]);
}

#[Route('admin/removecvadmin/{id}', name: 'removecvadmin')]
public function removecvadmin($id,CVRepository $CVRepository,ManagerRegistry $ManagerRegistry): Response
{
    $em = $ManagerRegistry->getManager();
    $dataid = $CVRepository->find($id);
    $em->remove($dataid);
    $em->flush();
    return $this->redirectToRoute('showcvsadmin');
}

}
