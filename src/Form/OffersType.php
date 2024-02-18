<?php

namespace App\Form;

use App\Entity\Offers;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class OffersType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('entrepriseName')
            ->add('domain')
            ->add('post')
            ->add('description')
            ->add('salary')
            ->add('period', ChoiceType::class, [
                'choices' => [
                    '1 month' => '1-month',
                    '2-3 months' => '2-3 months',
                    'More than 3 months' => 'more-than-3-months',
                    'More than 6 months' => 'more-than-6-months',
                    'More than 1 year' => 'more-than-1-year',
                    'Negociatable' => 'negociatable',
                ],
                'placeholder' => 'Choose an option or enter custom',
                'required' => false,
                'attr' => [
                    'class' => 'custom-period-field',
                ],
            ])
            ->add('localisation');
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Offers::class,
        ]);
    }
}
