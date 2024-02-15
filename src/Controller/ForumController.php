<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Entity\User;
use Monolog\DateTimeImmutable;
use App\Entity\Topic;

class ForumController extends AbstractController
{
    #[Route('/forum', name: 'app_forum')]
    public function index(): Response
    {
        $user = new User();

        $user->setEmail('john.doe@example.com');
        $user->setFname('John');
        $user->setLname('Doe');
        $user->setPhonenumber(1234567890);
        $user->setBirthdate(new DateTimeImmutable('1990-01-01'));

        // Password should be securely hashed before setting
        $user->setPassword('$2y$13$9tG1L.h2oU657P9Z/o9vF.tYz207oP546v67pJ9Z/53qG7yW');

        // Add roles like 'ROLE_USER' or other roles as needed
        $user->setRoles(['ROLE_USER']);

        // ... further customization based on your application logic

        $topics = [
            new Topic(
                1,
                "Web Development",
                "web-development",
                "Discuss various web development technologies and frameworks.",
                new DateTimeImmutable('2024-02-14 20:00:00'),
                $user,
            ),
            // ... other topic objects representing your forum data
        ];
        return $this->render('forum/forum.html.twig', [
            'topics' => $topics,
        ]
     );
    }
}
