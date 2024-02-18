<?php

namespace App\Form;

use App\Entity\Application;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;


class ApplicationType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            // Add fields for user details
            ->add('userFirstName', null, [
                'label' => 'First Name',
                'mapped' => false,
                'data' => $options['user']->getFname(),
            ])
            ->add('userLastName', null, [
                'label' => 'Last Name',
                'mapped' => false,
                'data' => $options['user']->getLname(),
            ])
            ->add('userEmail', null, [
                'label' => 'Email',
                'mapped' => false,
                'data' => $options['user']->getEmail(),
            ])
            ->add('userPhoneNumber', null, [
                'label' => 'Phone Number',
                'mapped' => false,
                'data' => $options['user']->getPhonenumber(),
            ])

            /* ->add('cv', FileType::class, [

                'required' => false,
            ])*/
            ->add('cv', FileType::class, [
                'required' => false,
                'data_class' => null, // Set data_class to null for non-entity fields
            ]);;
    }


    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Application::class,
            'user' => null,
        ]);
    }
}
