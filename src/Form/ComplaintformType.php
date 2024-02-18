<?php

namespace App\Form;

use App\Entity\Complaint;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\FileType;

class ComplaintformType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('type', ChoiceType::class, [
            'choices' => [
                'General inquiry' => 'General inquiry',
                'Billing issue' => 'Billing issue',
                'Customer support' => 'Customer support',
                // Add more options as needed
            ],
            'label' => 'Complaint type',
            'attr' => [
                'class' => 'form-select rounded-4',
            ],
        ])
            ->add('message')
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Complaint::class,
        ]);
    }
}
