<?php
namespace App\Controller;

use App\Entity\User;
use App\Form\UserType;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Core\Encoder\UserPasswordEncoderInterface;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Security\Core\Validator\Constraints\UserPassword;

class RegistrationController extends AbstractController
{
    private $passwordEncoder;

    /*public function __construct(UserPasswordEncoderInterface $passwordEncoder)
    {
        $this->passwordEncoder = $passwordEncoder;
    }*/

    /**
     * @Route("/registration", name="registration")
     */
    #[Route('/signup', name: 'app_signup')]
    public function signup(ManagerRegistry $managerRegistry, Request $request)
    {
        $user = new User();

        $form = $this->createForm(UserType::class, $user);

        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Encode the new users password
            //$user->setPassword($this->passwordEncoder->encodePassword($user, $user->getPassword()));

            // Set their role
            $user->setRoles(['ROLE_USER']);

            // Save
            $em = $managerRegistry->getManager();
            $em->persist($user);
            $em->flush();

            return $this->redirectToRoute('app_signup');
        }

        return $this->render('registration/signup.html.twig', [
            'form' => $form->createView(),
        ]);
    }
    
    #[Route('/contact', name: 'app_contact')]
    public function contact(): Response
    {
        return $this->render('registration/contact.html.twig'
            
        );
    }

    #[Route('/home', name: 'app_home')]
    public function indexhome(): Response
    {
        return $this->render('user/index.html.twig'
            
        );
    }


    
}