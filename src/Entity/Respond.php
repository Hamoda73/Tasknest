<?php

namespace App\Entity;

use App\Repository\RespondRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: RespondRepository::class)]
class Respond
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank]
    #[Assert\Length(min: 10)]
    private ?string $message = null;

    #[ORM\OneToOne(cascade: ['persist', 'remove'])]
    private ?Complaint $Complaint = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getMessage(): ?string
    {
        return $this->message;
    }

    public function setMessage(string $message): static
    {
        $this->message = $message;

        return $this;
    }

    public function getComplaint(): ?Complaint
    {
        return $this->Complaint;
    }

    public function setComplaint(?Complaint $Complaint): static
    {
        $this->Complaint = $Complaint;

        return $this;
    }
}
