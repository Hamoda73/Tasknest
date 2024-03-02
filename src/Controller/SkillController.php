<?php

namespace App\Controller;
use App\Entity\Skill;
use App\Form\SkillType;
use App\Repository\CVRepository;
use App\Repository\SkillRepository;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpKernel\Exception\BadRequestHttpException;
use Symfony\Component\Routing\Annotation\Route;

class SkillController extends AbstractController
{
    #[Route('user/skill', name: 'app_skill')]
    public function index(): Response
    {
        return $this->render('user/skill/index.html.twig', [
            'controller_name' => 'SkillController',
        ]);
    }
   

    #[Route('user/addSkill/{idc}', name: 'addSkill')]
    public function addSkill($idc, CVRepository $CVRepository,SkillRepository $SkillRepository, ManagerRegistry $ManagerRegistry, Request $request): Response
    {
        $entityManager = $ManagerRegistry->getManager();
    
        $cv = $CVRepository->find($idc);
    
        $skill = new Skill();
        $skill->setCv($cv);
    
        // Create the form for the skill and handle the request
        $skillForm = $this->createForm(SkillType::class, $skill);
        $skillForm->handleRequest($request);

        if ($skillForm->isSubmitted() && $skillForm->isValid()) {  
            // Check if a skill with the same name already exists for this CV
            $existingSkill = $SkillRepository->findOneBy([
                'cv' => $cv,
                'name' => $skill->getName(),
            ]);
    
            if ($existingSkill) {
                // If a skill with the same name exists, add flash message and redirect
                $this->addFlash('error', 'Skill with this name already exists for the CV.');
                return new RedirectResponse($this->generateUrl('addSkill', ['idc' => $cv->getId()]));
            } else {
                // If the skill does not exist add the skill
                $entityManager->persist($skill);
                $entityManager->flush();

                // Redirect the user back to the same page to clear the form fields
                return new RedirectResponse($this->generateUrl('addSkill', ['idc' => $cv->getId()]));
            }
        }
        return $this->render('user/skill/addskill.html.twig', [
            'cv' => $cv,
            'skillForm' => $skillForm->createView(),          
        ]);
    }
    
    #[Route('user/editSkill/{idc}', name: 'editSkill')]
    public function editSkill($idc, SkillRepository $SkillRepository, ManagerRegistry $ManagerRegistry, Request $request): Response
    {
        $em = $ManagerRegistry->getManager();
        $skill = $SkillRepository->find($idc);
        $cv = $skill->getCv();
        $skillForm = $this->createForm(SkillType::class, $skill);
        $skillForm->handleRequest($request);
        if ($skillForm->isSubmitted() && $skillForm->isValid()) {  
            $em->persist($skill);
            $em->flush();
        }
        return $this->render('user/skill/editskill.html.twig', [
            'cv' => $cv,
            'skill' => $skill,
            'skillForm' => $skillForm->createView(),          
        ]);
    }

    #[Route('user/removeskill/{id}', name: 'removeskill')]
    public function removeskill($id,SkillRepository $SkillRepository,ManagerRegistry $ManagerRegistry): Response
    {
        $em = $ManagerRegistry->getManager();
        $dataid = $SkillRepository->find($id);
        $cv=$dataid->getCv()->getId();
        $em->remove($dataid);
        $em->flush();
        return $this->redirectToRoute('editcv', ['id' =>$cv]);
    }


    /*----------------------------------------  ADMIN  -----------------------------------------------*/

    #[Route('admin/skill/showskillsadmin/{id}', name: 'showskillsadmin')]
    public function showskillsadmin(int $id, CVRepository $cvRepository): Response
    {
        $cv = $cvRepository->find($id);
        $skills = $cv->getSkills();
    
        return $this->render('admin/skill/showskills.html.twig', [
            'skills' => $skills,
        ]);
    }

    #[Route('admin/removeskilladmin/{id}', name: 'removeskilladmin')]
    public function removeskilladmin($id, SkillRepository $SkillRepository, ManagerRegistry $ManagerRegistry, Request $request): Response
    {
        $em = $ManagerRegistry->getManager();
        $dataid = $SkillRepository->find($id);
        $cv= $dataid->getCV()->getId();
        $em->remove($dataid);
        $em->flush();
    
        return $this->redirectToRoute('showskillsadmin', ['id' => $cv]);
    }
  
}
