<?php

namespace App\Form;

use App\Entity\Comments;
use App\Entity\User;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\TextareaType; // Use TextareaType
use Symfony\Component\Form\Extension\Core\Type\SubmitType;


class CommentType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('content', TextAreaType::class, [ // Change to TextType
            'label' => 'Comment:',
            'required' => true,
        ])
        ->add('submit', SubmitType::class, [
            'label' => 'Submit',
        ]);
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Comments::class,
        ]);
    }
}
