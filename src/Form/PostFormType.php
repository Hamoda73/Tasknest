<?php

namespace App\Form;

use App\Entity\Categories;
use App\Entity\Posts;
use App\Entity\User;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use DateTime;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType; // Use TextareaType



class PostFormType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('title')
            ->add('content', TextAreaType::class, [ // Change to TextType
                'label' => 'Content:',
                'required' => true,
            ])
           
            ->add('image_url', FileType::class, [
                'label' => 'Choose File',
                'required' => false, // Set to true if the file is required
            ])
            ->add('video_url', FileType::class, [
                'label' => 'Choose File',
                'required' => false, // Set to true if the file is required
            ])

            ->add('category_id', EntityType::class, [
                'class' => Categories::class,
                'choice_label' => 'name', // Display category's name instead of ID
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Posts::class,
        ]);
    }
}
