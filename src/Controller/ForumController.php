<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Entity\User;
use Monolog\DateTimeImmutable;
use Symfony\Component\Security\Core\Security;
use Symfony\Component\HttpFoundation\Request;
use App\Form\PostFormType;
use Doctrine\ORM\EntityManagerInterface;
use App\Entity\Posts;
use App\Repository\PostsRepository;
use App\Repository\CommentsRepository;
use App\Form\CommentType;
use App\Entity\Comments;
use App\Entity\Likes;
use App\Entity\Dislikes;
use App\Entity\Categories;


class ForumController extends AbstractController
{
    private $entityManager;

    private $commentRepository;

    public function __construct(EntityManagerInterface $entityManager)
    {
        $this->entityManager = $entityManager;
        $this->commentRepository = $this->entityManager->getRepository(Comments::class);
        $this->categoryRepository = $this->entityManager->getRepository(Categories::class);
        $this->postRepository = $this->entityManager->getRepository(Posts::class);
        $this->userRepository = $this->entityManager->getRepository(User::class);

    }
    #[Route('/forum/{id}', name: 'app_forum')]
    public function forum($id): Response
    {
        $posts = $this->postRepository->findBy(['category' => $id]);
        return $this->render('forum/forum.html.twig', [
        'posts' => $posts,
          
        
     ]);
    }
    #[Route('/forumSingle/{id}', name: 'app_forumSingle')]
    public function forumSingle(int $id,Security $security, Request $request, EntityManagerInterface $entityManger, PostsRepository $repo): Response
    {
        if (($security->isGranted('ROLE_FREELANCER')) || ($security->isGranted('ROLE_COMPANY'))){
            $post = $repo->find($id);
            $user = $post->user;
            $currentDateTime = date_create_immutable();

            $comment = new Comments;
            $CurrentUser = $security->getUser();
            $comment->user = $CurrentUser;
            $comment -> setPostId($post);
            $comment -> setDateTime($currentDateTime);

            $formCom = $this->createForm(CommentType::class, $comment);
            $formCom->handleRequest($request);

            $liked= 0; $disliked = 0;
            $like = $this->entityManager->getRepository(Likes::class)->findOneBy([
                'user' => $user,
                'post' => $post
            ]);
            $dislike = $this->entityManager->getRepository(Dislikes::class)->findOneBy([
                'user' => $user,
                'post' => $post
            ]);
    
        if ($like) {
            $liked = 1;
            $disliked = 0;
        }
        if ($dislike) {
            $liked = 0;
            $disliked = 1;
        }

        $comments = $this->commentRepository->findBy(['post' => $post]);

           
            if ($formCom->isSubmitted() && $formCom->isValid()) {
                $entityManger->persist($comment);
                $entityManger->flush();

                return $this->redirectToRoute('app_forumCat');
            }


        return $this->render('forum/forum-single.html.twig',[
            'post' => $post,
            'user' => $user,
            'comment' => $comment,
            'form' => $formCom->createView(),
            'form' => $formCom->createView(),
            'liked' => $liked,
            'disliked' => $disliked,
            'comments' => $comments,
            'str' => "hello world",
        ]
        );
        }
        else{
            return $this->render('registration/rolechoice.html.twig');
         }
     
    }
    #[Route('/forumCat', name: 'app_forumCat')]
    public function forumCat(): Response
    {
        $categories = $this->categoryRepository->findAll();

        return $this->render('forum/forumCat.html.twig',[
        'categories' => $categories,
           

     ]);
    }
    #[Route('/forumCreation', name: 'app_forumCreation')]
    public function forumCreation(Security $security, Request $request, EntityManagerInterface $entityManger): Response
    {


        if ($security->isGranted('ROLE_FREELANCER')) {
            $post = new Posts();
            $user = $security->getUser();
            $userId = $user->getId();
            $post->setUserId($user);
            $currentDateTime = date_create_immutable();
            $post->setDateTime($currentDateTime);



            $form = $this->createForm(PostFormType::class, $post);
            $form->handleRequest($request);
           
            if ($form->isSubmitted() && $form->isValid()) {
                $entityManger->persist($post);
                $entityManger->flush();

                return $this->redirectToRoute('app_forumCat');
            }


            return $this->render('forum/forum.html - Copie.twig', [
            'form' => $form->createView(),
        ]
               
        );}
         else{
            return $this->render('registration/rolechoice.html.twig');
         }
    }

    #[Route('/like/{user_id}/{post_id}', name: 'like')]
    public function like($user_id, $post_id, EntityManagerInterface $entityManger): Response
    {
        $post = $this->entityManager->getRepository(Posts::class)
        ->find($post_id);
        
        $user = $this->entityManager->getRepository(User::class)
        ->find($user_id);
        $like = $this->entityManager->getRepository(Likes::class)->findOneBy([
            'user' => $user,
            'post' => $post
        ]);
        
        $dislike = $this->entityManager->getRepository(Dislikes::class)->findOneBy([
            'user' => $user,
            'post' => $post
        ]);

        if($like){
            $this->entityManager->remove($like);
            $this->entityManager->flush();

        }else{
           $l = new Likes;
           $l->setPost($post);
           $l->setUser($user);
           $this->entityManager->persist($l);
           $this->entityManager->flush();
           if($dislike){
            $this->entityManager->remove($dislike);
            $this->entityManager->flush();
           }
        }
        return $this->redirectToRoute('app_forumSingle', ['id' => $post->getId()]);
    }

    #[Route('/dislike/{user_id}/{post_id}', name: 'dislike')]
    public function dislike($user_id, $post_id, EntityManagerInterface $entityManger): Response
    {
        $post = $this->entityManager->getRepository(Posts::class)
        ->find($post_id);
        
        $user = $this->entityManager->getRepository(User::class)
        ->find($user_id);
        $like = $this->entityManager->getRepository(Likes::class)->findOneBy([
            'user' => $user,
            'post' => $post
        ]);
        
        $dislike = $this->entityManager->getRepository(Dislikes::class)->findOneBy([
            'user' => $user,
            'post' => $post
        ]);

        if($dislike){
            $this->entityManager->remove($dislike);
            $this->entityManager->flush();

        }else{
           $dl = new Dislikes;
           $dl->setPostId($post);
           $dl->setUserId($user);
           $this->entityManager->persist($dl);
           $this->entityManager->flush();
           if($like){
            $this->entityManager->remove($like);
            $this->entityManager->flush();
           }
        }
        return $this->redirectToRoute('app_forumSingle', ['id' => $post->getId()]);
    }




    #[Route('/adminForum', name: 'adminForum')]
    public function adminForum(): Response
    {

        return $this->render('admin/dashboard.html.twig',
           
        
     );
    }



    
}
