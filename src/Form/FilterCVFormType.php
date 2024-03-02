<?php

namespace App\Form;

use App\Entity\Skill;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class FilterCVFormType extends AbstractType
{
    private $entityManager;

    public function __construct(EntityManagerInterface $entityManager)
    {
        $this->entityManager = $entityManager;
    }

    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $skills = $this->entityManager->getRepository(Skill::class)->createQueryBuilder('s')
            ->select('s.id', 's.name')
            ->distinct()
            ->getQuery()
            ->getResult();

        $choices = [];
        foreach ($skills as $skill) {
            $choices[$skill['name']] = $skill['id'];
        }

        $builder
            ->add('skills', ChoiceType::class, [
                'label' => 'Select Skills:',
                'choices' => $choices,
                'multiple' => true,
                'expanded' => true,
            ]);
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            // Set your data class if needed
        ]);
    }
}
