<?php

namespace App\Entity;

use App\Repository\UserRepository;
use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Symfony\Component\Security\Core\User\PasswordAuthenticatedUserInterface;
use Symfony\Component\Security\Core\User\UserInterface;
use Doctrine\DBAL\Types\Types;
use phpDocumentor\Reflection\Types\Boolean;
use Symfony\Component\Validator\Constraints as Assert;
use App\Entity\Dislikes;

#[ORM\Entity(repositoryClass: UserRepository::class)]
class User implements UserInterface, PasswordAuthenticatedUserInterface
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 180, unique: true)]
    #[Assert\NotBlank(message: "Fill in the Email blank.")]
    #[Assert\Email(message: "The email '{{ value }}' is not a valid email.")]
    private ?string $email = null;

    
    #[ORM\Column(length: 180)]
    #[Assert\NotBlank(message: "Fill in the Firstname blank.")]
    #[Assert\Length(min: 4)]
    private ?string $fname = null;
    
    
    #[ORM\Column(length: 180)]
    #[Assert\NotBlank(message: "Fill in the Lastname blank.")]
    #[Assert\Length(min: 5)]
    private ?string $lname = null;

    
    #[ORM\Column(length: 180)]
    #[Assert\NotBlank(message: "Fill in the Phone number blank.")]
    #[Assert\Length(min: 8, max: 8)]
    private ?int $phonenumber = null;

    #[ORM\Column(length: 180)]
    #[Assert\NotBlank(message: "Fill in the Image blank.")]
    private ?string $image = null;

    #[ORM\Column]
    private ?bool $blocked = null;

    
    #[ORM\Column(type: Types::DATE_MUTABLE)]
    #[Assert\NotBlank]
    private ?\DateTimeInterface $birthdate = null;

    #[ORM\Column]
    private array $roles = [];

    /**
     * @var string The hashed password
     */
    
    #[ORM\Column(length: 180)]
    #[Assert\NotBlank(message: "Fill in the Password blank.")]
    #[Assert\Length(min: 6)]
    private ?string $password = null;

    #[ORM\OneToOne(mappedBy: "user", cascade: ["persist", "remove"])]
    private ?CV $cv = null;
    

    #[ORM\OneToMany(mappedBy: 'User', targetEntity: Complaint::class)]
    private Collection $complaints;

    #[ORM\OneToMany(mappedBy: 'user_id', targetEntity: Dislikes::class)]
    private Collection $dislikes;



    public function __construct()
    {
        $this->complaints = new ArrayCollection();
        $this->dislikes = new ArrayCollection();
        
    }

    /**
     * @return Collection<int, Complaint>
     */
    public function getComplaints(): Collection
    {
        return $this->complaints;
    }

    public function addComplaint(Complaint $complaint): static
    {
        if (!$this->complaints->contains($complaint)) {
            $this->complaints->add($complaint);
            $complaint->setUser($this);
        }

        return $this;
    }

    public function removeComplaint(Complaint $complaint): static
    {
        if ($this->complaints->removeElement($complaint)) {
            // set the owning side to null (unless already changed)
            if ($complaint->getUser() === $this) {
                $complaint->setUser(null);
            }
        }

        return $this;
    }


    public function getId(): ?int
    {
        return $this->id;
    }

    public function getEmail(): ?string
    {
        return $this->email;
    }
    public function setCv(?CV $cv): static
    {
        $this->cv = $cv;
        if ($cv !== null) {
            $cv->setUser($this); // Make sure to synchronize the association on both sides
        }
        return $this;
    }
    /*public function setCv(?CV $cv): static
    {
        $this->cv = $cv;
    
        return $this;
    }*/
    
    public function getCV(): ?CV
    {
        return $this->cv;
    }


    public function setEmail(string $email): static
    {
        $this->email = $email;

        return $this;
    }

    /**
     * A visual identifier that represents this user.
     *
     * @see UserInterface
     */
    public function getUserIdentifier(): string
    {
        return (string) $this->email;
    }

    /**
     * @see UserInterface
     */
    public function getRoles(): array
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

    /**
     * @see PasswordAuthenticatedUserInterface
     */
    public function getPassword(): string
    {
        return $this->password;
    }

    public function setPassword(string $password): self
    {
        $this->password = $password;

        return $this;
    }

    /**
     * Returning a salt is only needed if you are not using a modern
     * hashing algorithm (e.g. bcrypt or sodium) in your security.yaml.
     *
     * @see UserInterface
     */
    public function getSalt(): ?string
    {
        return null;
    }
    

    /**
     * @see UserInterface
     */
    public function eraseCredentials(): void
    {
        // If you store any temporary, sensitive data on the user, clear it here
        // $this->plainPassword = null;
    }

    public function getFname(): ?string
    {
        return $this->fname;
    }

    public function setFname(string $fname): static
    {
        $this->fname = $fname;

        return $this;
    }

    public function getLname(): ?string
    {
        return $this->lname;
    }

    public function setLname(string $lname): static
    {
        $this->lname = $lname;

        return $this;
    }

    public function getBirthdate(): ?\DateTimeInterface
    {
        return $this->birthdate;
    }

    public function setBirthdate(\DateTimeInterface $birthdate): static
    {
        $this->birthdate = $birthdate;

        return $this;
    }

    public function getPhonenumber(): ?int
    {
        return $this->phonenumber;
    }

    public function setPhonenumber(int $phonenumber): static
    {
        $this->phonenumber = $phonenumber;

        return $this;
    }

    public function getImage(): ?string
    {
        return $this->image;
    }

    public function setImage(string $image): static
    {
        $this->image = $image;

        return $this;
    }

    public function isBlocked(): ?bool
    {
        return $this->blocked;
    }

    public function setBlocked(bool $blocked): self
    {
        $this->blocked = $blocked;

        return $this;
    }
    public function __toString()
    {
        return $this->getLname(); 
    }

    /**
     * @return Collection<int, Dislikes>
     */
    public function getDislikes(): Collection
    {
        return $this->dislikes;
    }

    public function addDislike(Dislikes $dislike): static
    {
        if (!$this->dislikes->contains($dislike)) {
            $this->dislikes->add($dislike);
            $dislike->setUserId($this);
        }

        return $this;
    }

    public function removeDislike(Dislikes $dislike): static
    {
        if ($this->dislikes->removeElement($dislike)) {
            // set the owning side to null (unless already changed)
            if ($dislike->getUserId() === $this) {
                $dislike->setUserId(null);
            }
        }

        return $this;
    }


    
}
