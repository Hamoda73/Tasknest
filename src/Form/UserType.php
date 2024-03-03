<?php

namespace App\Form;

use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\Extension\Core\Type\RepeatedType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
//use App\Form\DataTransformer\FileToPathTransformer;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Validator\Constraints\NotBlank;

class UserType extends AbstractType
{
    /*private $fileTransformer;

    public function __construct(FileToPathTransformer $fileTransformer)
    {
        $this->fileTransformer = $fileTransformer;
    }*/

    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('fname', TextType::class, [
            'constraints' => [
                new NotBlank(),
            ],
        ])
        ->add('lname')
        ->add('birthdate')
        ->add('phonenumber')
        ->add('email')
        ->add('image')
        ->add('password', RepeatedType::class, [
            'type'=>PasswordType::class,
            'first_options'=>['label'=>'Password'],
            'second_options'=>['label'=>'Confirm Password']
        ])
        ;
        //$builder->get('image')->addModelTransformer($this->fileTransformer);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            // Configure your form options here
        ]);
    }
}
