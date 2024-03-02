<?php

namespace App\Entity;

use App\Repository\OffersRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use phpDocumentor\Reflection\Types\Boolean;
use phpDocumentor\Reflection\Types\Integer;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: OffersRepository::class)]
class Offers
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 20)]
    #[Assert\NotBlank(message: "Your Entreprise Name cannot be blank.")]
    #[Assert\Length(
        max: 20,
        maxMessage: "Your Entreprise Name cannot contain more than {{ limit }} characters."
    )]
    #[Assert\Regex(
        pattern: "/^[a-zA-Z0-9\s]*$/",
        message: "this field must contain letters,numbers and spaces only."
    )]
    private ?string $entrepriseName = null;



    #[ORM\Column(length: 20)]
    #[Assert\NotBlank(message: "This field cannot be blank.")]
    #[Assert\Length(
        max: 20,
        maxMessage: "this field cannot contain more than {{ limit }} characters."
    )]
    #[Assert\Regex(
        pattern: "/^[a-zA-Z\s]*$/",
        message: "this field must contain letters and spaces only."
    )]
    private ?string $domain = null;





    #[ORM\Column(length: 20)]
    #[Assert\NotBlank(message: "This field cannot be blank.")]
    #[Assert\Length(
        max: 20,
        maxMessage: "this field cannot contain more than {{ limit }} characters."
    )]
    #[Assert\Regex(
        pattern: "/^[a-zA-Z\s]*$/",
        message: "this field must contain letters and spaces only."
    )]
    private ?string $post = null;



    #[ORM\Column(type: Types::TEXT)]
    #[Assert\NotBlank(message: "Your Description cannot be blank.")]
    #[Assert\Length(
        max: 1000,
        maxMessage: "Your Description cannot contain more than {{ limit }} characters."
    )]
    private ?string $description = null;



    #[ORM\Column(length: 20)]
    #[Assert\NotBlank(message: "this field cannot be blank.")]

    private ?string $period = null;

    #[ORM\Column(length: 20)]
    #[Assert\NotBlank(message: "this field cannot be blank.")]
    #[Assert\Positive(message: "This field must be a positive number.")]
    private ?int $salary;


    #[ORM\Column(length: 20)]
    #[Assert\NotBlank(message: "Your Location cannot be blank.")]
    #[Assert\Length(
        max: 20,
        maxMessage: "Your Location cannot contain more than {{ limit }} characters."
    )]
    #[Assert\Regex(
        pattern: "/^[a-zA-Z\s]*$/",
        message: "Your Location must only contain letters and spaces."
    )]

    private ?string $localisation = null;





    #[ORM\ManyToOne(inversedBy: 'offers')]
    #[ORM\JoinColumn(nullable: false)]
    private ?User $User = null;


    //cascade 
    #[ORM\OneToMany(targetEntity: Application::class, mappedBy: 'Offers', cascade: ["remove"])]
    private Collection $applications;

    public function __construct()
    {
        $this->applications = new ArrayCollection(); //offer has many applications 
    }




    public function getId(): ?int
    {
        return $this->id;
    }

    public function getEntrepriseName(): ?string
    {
        return $this->entrepriseName;
    }

    public function setEntrepriseName(string $entrepriseName): static
    {
        $this->entrepriseName = $entrepriseName;

        return $this;
    }




    public function getDomain(): ?string
    {
        return $this->domain;
    }

    public function setDomain(string $domain): static
    {
        $this->domain = $domain;

        return $this;
    }

    public function getPost(): ?string
    {
        return $this->post;
    }

    public function setPost(string $post): static
    {
        $this->post = $post;

        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(string $description): static
    {
        $this->description = $description;

        return $this;
    }

    public function getLocalisation(): ?string
    {
        return $this->localisation;
    }

    public function setLocalisation(string $localisation): static
    {
        $this->localisation = $localisation;

        return $this;
    }

    public function getPeriod(): ?string
    {
        return $this->period;
    }

    public function setPeriod(string $period): static
    {
        $this->period = $period;

        return $this;
    }
    public function getSalary(): ?int
    {
        return $this->salary;
    }

    public function setSalary(int $salary): static
    {
        $this->salary = $salary;

        return $this;
    }


    public function getUser(): ?User
    {
        return $this->User;
    }

    public function setUser(?User $User): static
    {
        $this->User = $User;

        return $this;
    }

    /**
     * @return Collection<int, Application>
     */
    public function getApplications(): Collection
    {
        return $this->applications;
    }

    public function addApplication(Application $application): static
    {
        if (!$this->applications->contains($application)) {
            $this->applications->add($application);
            $application->setOffers($this);
        }

        return $this;
    }

    public function removeApplication(Application $application): static
    {
        if ($this->applications->removeElement($application)) {

            if ($application->getOffers() === $this) {
                $application->setOffers(null);
            }
        }

        return $this;
    }
    public function __toString()
    {

        return (string)$this->getUser();
    }
}
