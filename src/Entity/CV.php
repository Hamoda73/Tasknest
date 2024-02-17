<?php

namespace App\Entity;

use App\Repository\CVRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: CVRepository::class)]
class CV
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;
    
    #[ORM\Column(length: 255, nullable: true)]
    #[Assert\Length( 
        max: 40,
        maxMessage: "Your BIO cannot contain more than {{ limit }} characters."
    )]
    #[Assert\Regex(
        pattern: "/^[a-zA-Z\s\-\.\,]*$/",
        message: "Your bio must only contain letters, hyphens (-), periods (.), comma (,) and spaces."
    )]
    private ?string $bio = null;

    #[ORM\Column(type: Types::TEXT, nullable: true)]
    #[Assert\NotBlank(message: "Your Description cannot be blank.")]
    #[Assert\Length( 
        max: 1000,
        maxMessage: "Your Description cannot contain more than {{ limit }} characters."
    )]
    private ?string $description = null;

    #[ORM\Column(type: 'json', nullable: true)]
    #[Assert\Count(min: 1, minMessage: "Select at least one language.")]
    private ?array $language = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Your Location cannot be blank.")]
    #[Assert\Length( 
        max: 20,
        maxMessage: "Your Location cannot contain more than {{ limit }} characters."
    )]
    #[Assert\Regex(
        pattern: "/^[a-zA-Z\s]*$/",
        message: "Your Location must only contain letters and spaces."
    )]
    private ?string $location = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Your Certification cannot be blank.")]
    #[Assert\Length( 
        max: 40,
        maxMessage: "Your Certification cannot contain more than {{ limit }} characters."
    )]
    #[Assert\Regex(
        pattern: "/^[a-zA-Z\s\-\.\,]*$/",
        message: "Your Certification must only contain letters , hyphens (-), periods (.), comma (,) and spaces."
    )]
    private ?string $certification = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Your Contact cannot be blank.")]
    #[Assert\Regex(
        pattern: "/^https?:\/\/(www\.)?facebook\.com\/[a-zA-Z0-9_.-]+\/?(\?locale=[a-zA-Z_]+)?$/",
        message: "Your Contact information must be in the format of a Facebook profile URL."
    )]
    private ?string $contact = null;

    #[ORM\OneToOne(inversedBy: "cv")]
    private ?User $user = null;
    public function setUser(?User $user): static
{
    $this->user = $user;
    return $this;
}

    #[ORM\OneToMany(targetEntity: Skill::class, mappedBy: 'cv', cascade: ['remove'])]
    private Collection $skills;

    public function __construct()
    {
        $this->skills = new ArrayCollection();
    }



    public function getId(): ?int
    {
        return $this->id;
    }

    public function getBio(): ?string
    {
        return $this->bio;
    }

    public function setBio(?string $bio): static
    {
        $this->bio = $bio;

        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(?string $description): static
    {
        $this->description = $description;

        return $this;
    }

    public function getLanguage(): ?array
    {
        return $this->language;
    }
    
    public function setLanguage(?array $language): self
    {
        $this->language = $language;
    
        return $this;
    }

    public function getLocation(): ?string
    {
        return $this->location;
    }
/**
     * @see UserInterface
     */
   /* public function getRoles(): array
    {
        $roles = $this->roles;
        // guarantee every user at least has ROLE_USER
        $roles[] = 'ROLE_USER';

        return array_unique($roles);
    }

    public function setRoles(array $roles): static
    {
        $this->roles = $roles;

        return $this;
    }
*/
    public function setLocation(string $location): static
    {
        $this->location = $location;

        return $this;
    }

    public function getCertification(): ?string
    {
        return $this->certification;
    }

    public function setCertification(string $certification): static
    {
        $this->certification = $certification;

        return $this;
    }

    public function getContact(): ?string
    {
        return $this->contact;
    }

    public function setContact(string $contact): static
    {
        $this->contact = $contact;

        return $this;
    }

    public function getUser(): ?User
    {
        return $this->user;
    }

   /* public function setUser(?User $user): static
    {
        $this->user = $user;

        return $this;
    }*/

    /**
     * @return Collection<int, Skill>
     */
    public function getSkills(): Collection
    {
        return $this->skills;
    }

    public function addSkill(Skill $skill): static
    {
        if (!$this->skills->contains($skill)) {
            $this->skills->add($skill);
            $skill->setCv($this);
        }

        return $this;
    }

    public function removeSkill(Skill $skill): static
    {
        if ($this->skills->removeElement($skill)) {
            // set the owning side to null (unless already changed)
            if ($skill->getCv() === $this) {
                $skill->setCv(null);
            }
        }

        return $this;
    }


    
}
