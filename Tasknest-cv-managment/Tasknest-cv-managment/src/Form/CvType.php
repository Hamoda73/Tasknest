<?php

namespace App\Form;

use App\Entity\CV;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\CheckboxType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
 use Symfony\Component\Form\Extension\Core\Type\CollectionType;
class CvType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('user')
        ->add('bio')
        ->add('description')
        ->add('language', ChoiceType::class, [
            'choices' => [
                'English' => 'English',
                'French' => 'French',
                'German' => 'German',
                'Arabic' => 'Arabic',
                // Add more languages as needed
            ],
            'expanded' => true, // Render as checkboxes
            'multiple' => true, // Allow multiple selections
        ])
        ->add('location')
        ->add('certification')
        ->add('contact');
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => CV::class,
        ]);
    }
}
