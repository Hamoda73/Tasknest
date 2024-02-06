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
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

class RegistrationController extends AbstractController
{
    private $passwordEncoder;

    /*public function __construct(UserPasswordEncoderInterface $passwordEncoder)
    {
        $this->passwordEncoder = $passwordEncoder;
    }*/
    #[Route('/signup', name: 'app_signup')]
public function signup(ManagerRegistry $managerRegistry, UserPasswordHasherInterface $passwordHasher, Request $request)
{
    $user = new User();

    $form = $this->createForm(UserType::class, $user);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        // Get the password field value from the request
        $formData = $form->getData();
        $plaintextPassword = $formData->getPassword();

        // Make sure the password is not null
        if ($plaintextPassword === null) {
            // Handle the error appropriately
            // For example, you can return a response indicating an error
            return new Response('Password cannot be null', Response::HTTP_BAD_REQUEST);
        }

        // Hash the password
        $hashedPassword = $passwordHasher->hashPassword($user, $plaintextPassword);

        $user->setPassword($hashedPassword);
        $user->setRoles(['ROLE_USER']);

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

    #[Route('/dashboard', name: 'app_dashboard')]
    public function dashboard(): Response
    {
        return $this->render('admin/dashboard.html.twig'
            
        );
    }
}