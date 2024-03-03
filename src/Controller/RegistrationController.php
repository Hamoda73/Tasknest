<?php
namespace App\Controller;

use App\Entity\User;
use App\Form\UserType;
use App\Repository\UserRepository;
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
    #[Route('/roleChoice', name: 'app_roleChoice')]
    public function roleChoice(ManagerRegistry $managerRegistry, UserPasswordHasherInterface $passwordHasher, Request $request)
    {
        return $this->render('registration/rolechoice.html.twig');
    }

    #[Route('/signupfreelancer', name: 'app_signupfreelancer')]
public function signupfreelancer(ManagerRegistry $managerRegistry, UserRepository $userRepository, Request $request, UserPasswordHasherInterface $passwordHasher)
{
    $em = $managerRegistry->getManager();
        $data = $userRepository->getUserWithLastId(); 
        $form = $this->createForm(UserType::class, $data);
        $form->handleRequest($request);

        if($form->isSubmitted() && $form->isValid()) {
            // Hash the password if it has been changed in the form
            $plainPassword = $form->get('password')->getData(); // Assuming 'password' is the name of your password field in the form
            if (!empty($plainPassword)) {
                $hashedPassword = $passwordHasher->hashPassword($data, $plainPassword);
                $data->setPassword($hashedPassword);
            }
            $data->setRoles(['ROLE_FREELANCER']);
            $em->flush();
            return $this->redirectToRoute('app_signin');
        }

        return $this->render('registration/signupfreelancer.html.twig', [
            'form' => $form->createView()
        ]);
}

#[Route('/signupcompany', name: 'app_signupcompany')]
public function signupcompany(ManagerRegistry $managerRegistry, UserRepository $userRepository, Request $request, UserPasswordHasherInterface $passwordHasher)
{
    $em = $managerRegistry->getManager();
        $data = $userRepository->getUserWithLastId(); 
        $form = $this->createForm(UserType::class, $data);
        $form->handleRequest($request);

        if($form->isSubmitted() && $form->isValid()) {
            // Hash the password if it has been changed in the form
            $plainPassword = $form->get('password')->getData(); // Assuming 'password' is the name of your password field in the form
            if (!empty($plainPassword)) {
                $hashedPassword = $passwordHasher->hashPassword($data, $plainPassword);
                $data->setPassword($hashedPassword);
            }
            $data->setRoles(['ROLE_COMPANY']);
            $em->flush();
            return $this->redirectToRoute('app_signin');
        }

        return $this->render('registration/signupfreelancer.html.twig', [
            'form' => $form->createView()
        ]);
}

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
            $user->setBlocked(false);

            
            
            $em = $managerRegistry->getManager();
            $em->persist($user);
            $em->flush();
            return $this->redirectToRoute('app_roleChoice');
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
    public function home(UserRepository $userRepository): Response
    {
        $user = $this->getUser();
        if($user !== null && $user->getRoles() !== null) {
            if (in_array('ROLE_ADMIN', $user->getRoles())) {
                // Return an error response
                return $this->render('admin/user_admin_error.html.twig');
            }
        }
        return $this->render('user/index.html.twig');
    }


/*----------------------------------------  ADMIN  -----------------------------------------------*/

    #[Route('/dashboard', name: 'app_dashboard')]
    public function dashboard(UserRepository $userRepository): Response
    {
        $numberOfUsers = $userRepository->countNumberOfUsers();
        /*$user = $this->getUser();
         if (in_array('ROLE_USER', $user->getRoles())) {
                // Return an error response
            return $this->render('admin/user_admin_error.html.twig');
        }*/
        return $this->render('admin/dashboard.html.twig', [
            'numberOfUsers' => $numberOfUsers,
        ]);
    }


    #[Route('/signupadmin', name: 'app_signup_admin')]
    public function signupadmin(ManagerRegistry $managerRegistry, UserPasswordHasherInterface $passwordHasher, Request $request)
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
            $user->setRoles(['ROLE_ADMIN']);

            $em = $managerRegistry->getManager();
            $em->persist($user);
            $em->flush();

            return $this->redirectToRoute('app_signup');
        }

        return $this->render('registration/signup.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/useraccounts', name: 'app_useraccounts')]
    public function showdb(UserRepository $userRepository, Request $request): Response
    {
        $user = $userRepository->findAll();
        
        return $this->render('admin/useraccounts.html.twig', [
            'user' => $user
        ]);
    }
}