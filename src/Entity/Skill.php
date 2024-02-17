<?php

namespace App\Entity;

use App\Repository\SkillRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: SkillRepository::class)]
#[ORM\Table(name: "skill")]
#[ORM\UniqueConstraint(name: "cv_name_unique", columns: ["cv_id", "name"])]
class Skill
{
    
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Skill name cannot be blank.")]
    #[Assert\Length(
        max: 15, // Adjust the maximum length as needed
        maxMessage: "Skill name cannot be longer than {{ limit }} characters."
    )]
    #[Assert\Regex(
        pattern: "/^[a-zA-Z0-9\s]*$/",
        message: "Your Skill name must only contain letters, numbers and spaces."
    )]
    private ?string $name = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Skill Value cannot be blank.")]
    #[Assert\Range(
        min: 0,
        max: 100,
        notInRangeMessage: "Skill value must be between {{ min }} and {{ max }}.",
        invalidMessage: "Skill value must be a valid number."
    )]
    private ?int $SkillValue = null;

    #[ORM\ManyToOne(inversedBy: 'skills')]
    private ?CV $cv = null;




    public function getId(): ?int
    {
        return $this->id;
    }


    public function getName(): ?string
    {
        return $this->name;
    }

    public function setName(string $name): self
    {
        $this->name = $name;

        return $this;
    }
    public function getSkillValue(): ?int
    {
        return $this->SkillValue;
    }

    public function setSkillValue(int $SkillValue): static
    {
        $this->SkillValue = $SkillValue;

        return $this;
    }

    public function getCv(): ?CV
    {
        return $this->cv;
    }

    public function setCv(?CV $cv): static
    {
        $this->cv = $cv;

        return $this;
    }



}
