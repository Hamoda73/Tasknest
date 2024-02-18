<?php

namespace App\Entity;

use App\Repository\ApplicationRepository;
use DateTime;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: ApplicationRepository::class)]

#[ORM\Table(name: "application")] //nhebch l user y aaml apply fard offre 
#[ORM\UniqueConstraint(name: "offer_user_unique", columns: ["offers_id", "user_id"])]  //2columns l id user wl id offer

class Application
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;





    #[ORM\ManyToOne(inversedBy: 'applications')]
    private ?Offers $Offers = null;

    #[ORM\ManyToOne(inversedBy: 'applications')]
    private ?User $user = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Please upload your resume.")]

    private ?string $cv = null;


    public function getId(): ?int
    {
        return $this->id;
    }



    public function getOffers(): ?Offers
    {
        return $this->Offers;
    }

    public function setOffers(?Offers $Offers): static
    {
        $this->Offers = $Offers;

        return $this;
    }

    public function getUser(): ?User
    {
        return $this->user;
    }

    public function setUser(?User $user): static
    {
        $this->user = $user;

        return $this;
    }


    public function getCv(): ?string
    {
        return $this->cv;
    }

    public function setCv(string $cv): static
    {
        $this->cv = $cv;

        return $this;
    }
}
