<?php

namespace App\Entity;

use App\Repository\OffersRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use phpDocumentor\Reflection\Types\Integer;

#[ORM\Entity(repositoryClass: OffersRepository::class)]
class Offers
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 20)]
    private ?string $entrepriseName = null;

    #[ORM\Column(length: 20)]
    private ?string $domain = null;

    #[ORM\Column(length: 20)]
    private ?string $post = null;

    #[ORM\Column(type: Types::TEXT)]
    private ?string $description = null;


    #[ORM\Column(length: 20)]
    private ?string $period = null;

    #[ORM\Column(length: 20)]
    private ?int $salary;

    #[ORM\Column(length: 20)]
    private ?string $localisation = null;



    #[ORM\ManyToOne(inversedBy: 'offers')]
    #[ORM\JoinColumn(nullable: false)]
    private ?User $User = null;

    #[ORM\OneToMany(targetEntity: Application::class, mappedBy: 'Offers', cascade: ["remove"])]
    private Collection $applications;

    public function __construct()
    {
        $this->applications = new ArrayCollection();
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
            // set the owning side to null (unless already changed)
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
